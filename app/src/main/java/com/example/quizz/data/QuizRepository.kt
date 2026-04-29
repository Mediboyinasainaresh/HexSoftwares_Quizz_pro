package com.example.quizz.data

import android.content.Context
import com.example.quizz.model.Category
import com.example.quizz.model.Question
import com.google.gson.Gson
import com.google.gson.Strictness
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import java.io.InputStreamReader

object QuizRepository {
    val categories = listOf(
        Category("1", "HISTORY", "Ancient to Modern", "📜"),
        Category("2", "SCIENCE", "Nature and Space", "🧪"),
        Category("3", "SPORTS", "Games and Athletes", "🏆"),
        Category("4", "MUSIC", "Beats and Melodies", "🎸")
    )

    private var questions: List<Question> = emptyList()

    fun loadQuestions(context: Context) {
        try {
            val inputStream = context.assets.open("questions.json")
            val reader = JsonReader(InputStreamReader(inputStream, "UTF-8"))
            
            // Following the specific instruction for malformed JSON handling
            reader.strictness = Strictness.LENIENT
            
            val questionType = object : TypeToken<List<Question>>() {}.type
            questions = Gson().fromJson(reader, questionType)
            reader.close()
        } catch (e: Exception) {
            e.printStackTrace()
            // Fallback to empty list or handled error
        }
    }

    fun getQuestionsForCategory(categoryId: String, context: Context): List<Question> {
        if (questions.isEmpty()) {
            loadQuestions(context)
        }
        return questions.filter { it.categoryId == categoryId }
    }
}