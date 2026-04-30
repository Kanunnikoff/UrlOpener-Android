package software.kanunnikoff.urlopener

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val urlEditText = findViewById<EditText>(R.id.urlEditText)

        findViewById<Button>(R.id.clearButton).setOnClickListener {
            urlEditText.text = null
        }

        findViewById<Button>(R.id.openButton).setOnClickListener {
            val url = urlEditText.text.toString().trim()
            openUrl(url)
        }
    }

    /**
     * Opens a given URL using an implicit intent.
     *
     * @param url The URL to open.
     */
    private fun openUrl(url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        } catch (e: Exception) {
            Snackbar.make(findViewById(android.R.id.content), e.localizedMessage, Snackbar.LENGTH_SHORT)
                .show()
        }
    }
}