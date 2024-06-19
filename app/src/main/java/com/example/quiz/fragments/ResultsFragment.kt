package com.example.quiz.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.quiz.R
import com.example.quiz.databinding.ResultsFragmentBinding
import kotlin.system.exitProcess


class ResultsFragment:Fragment(R.layout.results_fragment) {


    private var _binding: ResultsFragmentBinding? = null
    private val binding get() = _binding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): ConstraintLayout? {
        _binding = ResultsFragmentBinding.inflate(inflater, container, false)
        return binding?.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val results = arguments?.getString("results")
        val textForSharing = arguments?.getString("answers")
        binding?.resultsTxt?.text = "Ваш результат: $results/10"
        binding?.exitBtn?.setOnClickListener{ exitProcess(0)}
        binding?.restartBtn?.setOnClickListener{findNavController().
            navigate(ResultsFragmentDirections.actionResultsFragmentToMainFragment2())}
        binding?.shareBtn?.setOnClickListener{
            val intentShare = Intent(Intent.ACTION_SEND)
            intentShare.addCategory(Intent.CATEGORY_DEFAULT)
            intentShare.setType("text/plain")
            intentShare.putExtra(Intent.EXTRA_TEXT, textForSharing)
            startActivity(intentShare)
        }
    }

}