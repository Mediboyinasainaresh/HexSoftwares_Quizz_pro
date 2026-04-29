package com.example.quizz

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.quizz.data.QuizRepository
import com.example.quizz.data.ScoreManager
import com.example.quizz.ui.QuizActivity
import com.example.quizz.ui.LeaderboardActivity
import com.example.quizz.ui.adapter.CategoryAdapter

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        updateScore()

        findViewById<ImageButton>(R.id.btnLeaderboard).setOnClickListener {
            startActivity(Intent(this, LeaderboardActivity::class.java))
        }

        val rvCategories: RecyclerView = findViewById(R.id.rvCategories)
        rvCategories.layoutManager = GridLayoutManager(this, 2)
        
        val adapter = CategoryAdapter(QuizRepository.categories) { category ->
            val intent = Intent(this, QuizActivity::class.java)
            intent.putExtra("CATEGORY_ID", category.id)
            intent.putExtra("CATEGORY_NAME", category.name)
            startActivity(intent)
        }
        rvCategories.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        updateScore()
    }

    private fun updateScore() {
        val (_, _, totalCorrect) = ScoreManager.getStats(this)
        findViewById<TextView>(R.id.tvTotalPoints).text = (totalCorrect * 100).toString()
    }
}