package com.example.quiz.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.quiz.R
import com.example.quiz.databinding.QuestionsFragmentBinding
import java.io.BufferedReader
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader

class QuestionsFragment : Fragment(R.layout.questions_fragment) {
    private var _binding: QuestionsFragmentBinding? = null
    private val binding get() = _binding!!
    private var numberOfQuestion = 0
    private val questions = arrayListOf<String>()
    private val answers = arrayListOf<String>()
    private val answerOptions = arrayListOf<String>()
    private val playerAnswers = arrayListOf<Int>()
    private var playerPoints = 0
    private var textForSharing = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = QuestionsFragmentBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        readFile()
        Log.e("TEXT", answers.toString())
        binding.questionTxt.text = questions[0]
        showQuestion()
        binding.nextBtn.setOnClickListener { nextQuestion() }
        binding.previousBtn.setOnClickListener { previousQuestion() }
        binding.finishBtn.setOnClickListener { checkAnswersAndFinishGame() }
    }

    private fun checkAnswersAndFinishGame() {
        recordingAnswer()
        if(-1 in playerAnswers){
            Toast.makeText(context,"Ответьте на все вопросы",Toast.LENGTH_LONG).show()
        }else{
            var question = 0
            for (playerAnswer in playerAnswers){
                if (answerOptions[playerAnswer+4*question] == answers[question]){
                    playerPoints++
                    textForSharing += "Вопрос:${questions[question]}\nВаш ответ:${answerOptions[playerAnswer + 4 * question]}\n \n"
                }
                question++
            }
            findNavController().
            navigate(QuestionsFragmentDirections.actionQuestion1Fragment2ToResultsFragment(playerPoints.toString(),textForSharing))
            Log.e("POINTS", playerPoints.toString())
        }
    }

    private fun recordingAnswer(){
        val radioButtons = listOf(
            binding.answerOption1,
            binding.answerOption2,
            binding.answerOption3,
            binding.answerOption4
        )
        var index = -1
        for ((i, option) in radioButtons.withIndex()) {
            if (option.isChecked) {
                index = i
                break
            }
        }
        if (numberOfQuestion < playerAnswers.size) {
            playerAnswers[numberOfQuestion] = index
        } else {
            playerAnswers.add(index)
        }
        Log.e("CHECKED", playerAnswers.toString())
    }

    private fun nextQuestion() {
        val radioButtons = listOf(
            binding.answerOption1,
            binding.answerOption2,
            binding.answerOption3,
            binding.answerOption4
        )
        recordingAnswer()
        numberOfQuestion++
        for (option in radioButtons) {
            if (option.isChecked) {
                binding.radioGroup.clearCheck()
            }
        }
        showQuestion()
        if (numberOfQuestion < playerAnswers.size) {
            when (playerAnswers[numberOfQuestion]) {
            0 -> binding.answerOption1.isChecked = true
            1 -> binding.answerOption2.isChecked = true
            2 -> binding.answerOption3.isChecked = true
            3 -> binding.answerOption4.isChecked = true
            }
        }
    }

    @SuppressLint("ResourceType")
    private fun previousQuestion() {
        numberOfQuestion--
        showQuestion()
        if(playerAnswers[numberOfQuestion] == -1){
            binding.radioGroup.clearCheck()
        }else{
            when (playerAnswers[numberOfQuestion]) {
            0 -> binding.answerOption1.isChecked = true
            1 -> binding.answerOption2.isChecked = true
            2 -> binding.answerOption3.isChecked = true
            3 -> binding.answerOption4.isChecked = true
            }
        }
        Log.e("CHECKED", playerAnswers.toString())
    }

    private fun readFile() {
        var string: String? = ""
        val `is`: InputStream = this.resources.openRawResource(R.raw.questions)
        val reader = BufferedReader(InputStreamReader(`is`))
        while (true) {
            try {
                if (reader.readLine().also { string = it } == null) break
            } catch (e: IOException) {
                e.printStackTrace()
            }
            if (string?.get(0)?.isDigit() == true) {
                questions.add(string!!)
            } else if (string?.startsWith("Ответ") == true) {
                answers.add(string!!.split(':')[1].trim())
            } else {
                answerOptions.add(string!!)
            }
        }
        `is`.close()
    }

    private fun showQuestion() {
        binding.questionTxt.text = questions[numberOfQuestion]
        val firstIndex = 4 * numberOfQuestion
        binding.answerOption1.text = answerOptions[firstIndex]
        binding.answerOption1.textSize = 20F
        binding.answerOption2.text = answerOptions[firstIndex + 1]
        binding.answerOption2.textSize = 20F
        binding.answerOption3.text = answerOptions[firstIndex + 2]
        binding.answerOption3.textSize = 20F
        binding.answerOption4.text = answerOptions[firstIndex + 3]
        binding.answerOption4.textSize = 20F
        when (numberOfQuestion) {
            in 1..questions.size - 2 -> {
                binding.previousBtn.visibility = View.VISIBLE
                binding.finishBtn.visibility = View.INVISIBLE
                binding.nextBtn.visibility = View.VISIBLE
            }

            0 -> {
                binding.previousBtn.visibility = View.INVISIBLE
                binding.finishBtn.visibility = View.INVISIBLE
                binding.nextBtn.visibility = View.VISIBLE
            }

            questions.size - 1 -> {
                binding.previousBtn.visibility = View.VISIBLE
                binding.finishBtn.visibility = View.VISIBLE
                binding.nextBtn.visibility = View.INVISIBLE
            }
        }
    }
}