package com.example.quizz.ui

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.quizz.R
import com.example.quizz.data.QuizRepository
import com.example.quizz.model.Question
import com.example.quizz.util.AnimationUtils
import com.google.android.material.bottomsheet.BottomSheetDialog

class QuizActivity : AppCompatActivity() {

    private lateinit var tvQuestion: TextView
    private lateinit var tvQuestionProgressText: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var tvTimer: TextView
    private lateinit var tvLives: TextView
    private lateinit var options: List<View>

    private var currentQuestionIndex = 0
    private var score = 0
    private var lives = 3
    private lateinit var questions: List<Question>
    private var currentShuffledOptions: List<String> = emptyList()
    private var currentCorrectIndex: Int = -1
    
    private var countDownTimer: CountDownTimer? = null
    private val timeLimit = 15000L // 15 seconds

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        val categoryId = intent.getStringExtra("CATEGORY_ID") ?: ""
        questions = QuizRepository.getQuestionsForCategory(categoryId, this).shuffled()

        tvQuestion = findViewById(R.id.tvQuestion)
        tvQuestionProgressText = findViewById(R.id.tvQuestionProgressText)
        progressBar = findViewById(R.id.progressBar)
        tvTimer = findViewById(R.id.tvTimer)
        tvLives = findViewById(R.id.tvLives)
        
        options = listOf(
            findViewById(R.id.optionA),
            findViewById(R.id.optionB),
            findViewById(R.id.optionC),
            findViewById(R.id.optionD)
        )

        progressBar.max = questions.size

        showQuestion()
    }

    private fun showQuestion() {
        if (currentQuestionIndex >= questions.size || lives <= 0) {
            finishQuiz()
            return
        }

        val question = questions[currentQuestionIndex]
        
        // Logic for shuffling options but keeping track of correct answer
        val originalCorrectOption = question.options[question.correctAnswerIndex]
        currentShuffledOptions = question.options.shuffled()
        currentCorrectIndex = currentShuffledOptions.indexOf(originalCorrectOption)

        tvQuestion.text = question.text
        tvQuestionProgressText.text = "QUESTION ${currentQuestionIndex + 1}/${questions.size}"
        progressBar.progress = currentQuestionIndex + 1
        tvLives.text = "LIVES: $lives"

        val prefixes = listOf("A", "B", "C", "D")
        options.forEachIndexed { index, view ->
            val tvOptionText: TextView = view.findViewById(R.id.tvOptionText)
            val tvPrefix: TextView = view.findViewById(R.id.tvPrefix)
            
            // Reset background to original Neo-Brutalism style
            view.setBackgroundResource(R.drawable.bg_button_secondary_neo)
            
            tvOptionText.text = currentShuffledOptions[index]
            tvPrefix.text = prefixes[index]
            
            view.setOnClickListener {
                // 3. Simple Click Animation
                view.animate().scaleX(0.95f).scaleY(0.95f).setDuration(100).withEndAction {
                    view.animate().scaleX(1.0f).scaleY(1.0f).setDuration(100).start()
                    checkAnswer(index)
                }
            }
        }
        
        startTimer()
    }

    private fun startTimer() {
        countDownTimer?.cancel()
        countDownTimer = object : CountDownTimer(timeLimit, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val seconds = millisUntilFinished / 1000
                tvTimer.text = "TIME: ${seconds}s"
                if (seconds <= 5) {
                    tvTimer.setTextColor(Color.parseColor("#FF3366")) // Accent/Hot Pink
                    AnimationUtils.bounce(tvTimer)
                } else {
                    tvTimer.setTextColor(Color.parseColor("#1A1B41")) // Text/Deep Indigo
                }
            }

            override fun onFinish() {
                handleTimeout()
            }
        }.start()
    }

    private fun handleTimeout() {
        lives--
        vibrateDevice()
        Toast.makeText(this, "Wrong! (Out of time)", Toast.LENGTH_SHORT).show()
        showFeedback(false, true)
    }

    private fun checkAnswer(selectedIndex: Int) {
        countDownTimer?.cancel()
        val isCorrect = selectedIndex == currentCorrectIndex
        
        // 1. Add option selection highlight
        options[selectedIndex].setBackgroundColor(Color.BLUE)

        if (isCorrect) {
            score++
            // 2. Add correct/wrong feedback
            Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show()
        } else {
            lives--
            vibrateDevice()
            // 2. Add correct/wrong feedback
            Toast.makeText(this, "Wrong!", Toast.LENGTH_SHORT).show()
        }

        showFeedback(isCorrect)
    }

    private fun showFeedback(isCorrect: Boolean, isTimeout: Boolean = false) {
        val dialog = BottomSheetDialog(this, R.style.BottomSheetDialogTheme)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_feedback, null)
        dialog.setContentView(view)
        dialog.setCancelable(false)

        val container: View = view.findViewById(R.id.feedbackContainer)
        val icon: ImageView = view.findViewById(R.id.ivFeedbackIcon)
        val text: TextView = view.findViewById(R.id.tvFeedbackText)
        val btn: Button = view.findViewById(R.id.btnContinue)

        if (isCorrect) {
            container.setBackgroundResource(R.drawable.bg_bottom_sheet_correct)
            icon.setImageResource(android.R.drawable.checkbox_on_background)
            text.text = "NAILED IT!"
            AnimationUtils.bounce(icon)
        } else {
            container.setBackgroundResource(R.drawable.bg_bottom_sheet_wrong)
            icon.setImageResource(android.R.drawable.ic_delete)
            text.text = if (isTimeout) "OUT OF TIME!" else "OOF, NOT QUITE"
            AnimationUtils.shake(container)
        }

        btn.setOnClickListener {
            AnimationUtils.press(btn) {
                dialog.dismiss()
                nextQuestion()
            }
        }

        dialog.show()
    }

    private fun nextQuestion() {
        currentQuestionIndex++
        showQuestion()
    }

    private fun vibrateDevice() {
        val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(300, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(300)
        }
    }

    private fun finishQuiz() {
        countDownTimer?.cancel()
        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra("SCORE", score)
        intent.putExtra("TOTAL", questions.size)
        startActivity(intent)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        countDownTimer?.cancel()
    }
}