package com.example.quizz.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.quizz.MainActivity
import com.example.quizz.R
import com.example.quizz.data.ScoreManager

class ResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val score = intent.getIntExtra("SCORE", 0)
        val total = intent.getIntExtra("TOTAL", 0)

        ScoreManager.saveScore(this, score, total)

        val tvScore: TextView = findViewById(R.id.tvScore)
        tvScore.text = (score * 100).toString()

        val tvCorrectCount: TextView = findViewById(R.id.tvCorrectCount)
        tvCorrectCount.text = "$score/$total Correct"

        findViewById<Button>(R.id.btnPlayAgain).setOnClickListener {
            finish()
        }

        findViewById<Button>(R.id.btnBackToMenu).setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }
    }
}