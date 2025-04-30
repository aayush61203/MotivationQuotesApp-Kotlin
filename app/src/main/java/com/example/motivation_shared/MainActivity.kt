package com.example.motivation_shared

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.motivation_shared.ApiCall
import com.example.motivation_shared.QuotesModel
import com.example.motivation_shared.R

class MainActivity : AppCompatActivity() {
    private var position: Int = 0
    private lateinit var quoteslist: List<QuotesModel>
    private lateinit var tvQuotes: TextView
    private lateinit var tvAuthor: TextView
    private lateinit var btnNext: ImageView
    private lateinit var btnPrevious: ImageView
    private val handler = Handler(Looper.getMainLooper())
    private val autoNextRunnable = Runnable { autoNextQuote() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.motivation_shared)

        // Initialize views
        tvQuotes = findViewById(R.id.tv_quotes)
        tvAuthor = findViewById(R.id.tv_author)
        btnNext = findViewById(R.id.btn_next)
        btnPrevious = findViewById(R.id.btn_previous)

        // Make API call to get random quotes
        ApiCall().getRandomQuotes { listquote ->
            if (listquote != null) {
                quoteslist = listquote
                displayQuote()
            } else {
                Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show()
            }
        }

        // Button click listeners
        btnNext.setOnClickListener {
            nextQuote()
        }

        btnPrevious.setOnClickListener {
            previousQuote()
        }

        // Auto next quote every 5 seconds
        handler.postDelayed(autoNextRunnable, 5000)

        // Share button click listener
        findViewById<Button>(R.id.btn_share).setOnClickListener {
            shareQuote()
        }
    }

    private fun autoNextQuote() {
        nextQuote()

        // Check if it's the last quote, restart from the first one
        if (position == quoteslist.size - 1) {
            position = 0
            displayQuote()
        }

        // Schedule the next automatic click after 5 seconds
        handler.postDelayed(autoNextRunnable, 5000)
    }

    // Display the next quote
    private fun nextQuote() {
        if (position < quoteslist.size - 1) {
            position++
            displayQuote()
        } else {
            Toast.makeText(this@MainActivity, "You Reached the Last page", Toast.LENGTH_SHORT).show()
        }
    }

    // Display the previous quote
    private fun previousQuote() {
        if (position > 0) {
            position--
            displayQuote()
        } else {
            Toast.makeText(this@MainActivity, "You are on the 1st Page", Toast.LENGTH_SHORT).show()
        }
    }

    // Display the current quote
    private fun displayQuote() {
        tvQuotes.text = quoteslist[position].text
        tvAuthor.text = "~ " + quoteslist[position].author
    }

    // Share the current quote
    private fun shareQuote() {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        val shareBody = "Quote: ${quoteslist[position].text}\nAuthor: ${quoteslist[position].author}"
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody)
        startActivity(Intent.createChooser(shareIntent, "Share using"))
    }

    override fun onDestroy() {
        super.onDestroy()
        // Remove the autoNextRunnable callbacks when the activity is destroyed
        handler.removeCallbacks(autoNextRunnable)
    }
}
