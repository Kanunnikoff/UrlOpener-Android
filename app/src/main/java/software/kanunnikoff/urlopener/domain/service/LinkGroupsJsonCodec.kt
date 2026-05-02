package software.kanunnikoff.urlopener.domain.service

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import software.kanunnikoff.urlopener.domain.model.LinkGroup
import software.kanunnikoff.urlopener.domain.model.SavedLink

/**
 * Encodes and reads portable JSON without depending on Room identifiers.
 */
class LinkGroupsJsonCodec {

    fun encode(groups: List<LinkGroup>): String {
        val root = JSONObject()
            .put(Schema.VERSION, Schema.CURRENT_VERSION)
            .put(Schema.GROUPS, JSONArray())

        val groupsJson = root.getJSONArray(Schema.GROUPS)

        groups.forEach { group ->
            val linksJson = JSONArray()

            group.links.forEach { link ->
                linksJson.put(
                    JSONObject()
                        .put(Schema.NAME, link.name)
                        .put(Schema.URL, link.url)
                        .put(Schema.CREATED_AT, link.createdAt)
                        .put(Schema.UPDATED_AT, link.updatedAt),
                )
            }

            groupsJson.put(
                JSONObject()
                    .put(Schema.NAME, group.name)
                    .put(Schema.DESCRIPTION, group.description)
                    .put(Schema.CREATED_AT, group.createdAt)
                    .put(Schema.UPDATED_AT, group.updatedAt)
                    .put(Schema.LINKS, linksJson),
            )
        }

        return root.toString(Schema.INDENT_SPACES)
    }

    fun decode(json: String): List<LinkGroup> {
        try {
            val root = JSONObject(json)
            val groupsJson = root.optJSONArray(Schema.GROUPS)
                ?: throw IllegalArgumentException(INVALID_FORMAT_MESSAGE)

            return List(groupsJson.length()) { index ->
                val groupJson = groupsJson.getJSONObject(index)
                val linksJson = groupJson.optJSONArray(Schema.LINKS) ?: JSONArray()

                LinkGroup(
                    name = groupJson.getString(Schema.NAME).trim(),
                    description = groupJson.optString(Schema.DESCRIPTION).trim(),
                    createdAt = groupJson.optLong(Schema.CREATED_AT, System.currentTimeMillis()),
                    updatedAt = groupJson.optLong(Schema.UPDATED_AT, System.currentTimeMillis()),
                    links = List(linksJson.length()) { linkIndex ->
                        val linkJson = linksJson.getJSONObject(linkIndex)

                        SavedLink(
                            name = linkJson.getString(Schema.NAME).trim(),
                            url = linkJson.getString(Schema.URL).trim(),
                            createdAt = linkJson.optLong(Schema.CREATED_AT, System.currentTimeMillis()),
                            updatedAt = linkJson.optLong(Schema.UPDATED_AT, System.currentTimeMillis()),
                        )
                    },
                )
            }
        } catch (exception: JSONException) {
            throw IllegalArgumentException(INVALID_FORMAT_MESSAGE, exception)
        }
    }

    private companion object {
        const val INVALID_FORMAT_MESSAGE = "Invalid UrlOpener JSON"
    }

    private object Schema {
        const val CURRENT_VERSION = 1
        const val INDENT_SPACES = 2

        const val VERSION = "version"
        const val GROUPS = "groups"
        const val LINKS = "links"
        const val NAME = "name"
        const val DESCRIPTION = "description"
        const val URL = "url"
        const val CREATED_AT = "createdAt"
        const val UPDATED_AT = "updatedAt"
    }
}
