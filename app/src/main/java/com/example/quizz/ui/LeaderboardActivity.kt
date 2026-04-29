package com.example.quizz.ui

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.quizz.R
import com.example.quizz.data.ScoreManager

class LeaderboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leaderboard)

        val (highScore, totalPlayed, _) = ScoreManager.getStats(this)
        
        findViewById<TextView>(R.id.tvHighScore).text = "Best Score: ${highScore * 100}"
        findViewById<TextView>(R.id.tvTotalPlayed).text = "Quizzes Played: $totalPlayed"

        findViewById<Button>(R.id.btnBack).setOnClickListener {
            finish()
        }
    }
}