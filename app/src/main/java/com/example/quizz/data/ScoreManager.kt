package com.example.quizz.data

import android.content.Context

object ScoreManager {
    private const val PREFS_NAME = "quizz_prefs"
    private const val KEY_HIGH_SCORE = "high_score"
    private const val KEY_TOTAL_PLAYED = "total_played"
    private const val KEY_TOTAL_CORRECT = "total_correct"

    fun saveScore(context: Context, score: Int, totalInQuiz: Int) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val currentHighScore = prefs.getInt(KEY_HIGH_SCORE, 0)
        
        val editor = prefs.edit()
        
        if (score > currentHighScore) {
            editor.putInt(KEY_HIGH_SCORE, score)
        }
        
        val totalPlayed = prefs.getInt(KEY_TOTAL_PLAYED, 0)
        val totalCorrect = prefs.getInt(KEY_TOTAL_CORRECT, 0)
        
        editor.putInt(KEY_TOTAL_PLAYED, totalPlayed + 1)
        editor.putInt(KEY_TOTAL_CORRECT, totalCorrect + score)
        
        editor.apply()
    }

    fun getHighScore(context: Context): Int {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getInt(KEY_HIGH_SCORE, 0)
    }

    fun getStats(context: Context): Triple<Int, Int, Int> {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return Triple(
            prefs.getInt(KEY_HIGH_SCORE, 0),
            prefs.getInt(KEY_TOTAL_PLAYED, 0),
            prefs.getInt(KEY_TOTAL_CORRECT, 0)
        )
    }
}