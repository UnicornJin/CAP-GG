package com.example.capgg.calculator

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.capgg.data.GradeDatabase
import com.example.capgg.databinding.FragmentAddGradeBinding

class AddGradeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentAddGradeBinding.inflate(inflater, container, false)

        val application = requireNotNull(this.activity).application

        val dataSource = GradeDatabase.getInstance(application).gradeDatabaseDao

        val viewModelFactory = AddGradeViewModelFactory(dataSource, application)

        val viewModel = ViewModelProvider(this, viewModelFactory).get(AddGradeViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.doSubmission.observe(viewLifecycleOwner, Observer { it ->
            it?.let {
                if (it) {
                    try {
                        viewModel.onSubmission(binding)
                        this.findNavController()
                            .navigate(AddGradeFragmentDirections.actionAddGradeFragmentToHomePageFragment())
                    } catch (e: Exception) {
                        alertEmptyArg()
                    }
                }
                viewModel.doneSubmission()
            }
        })

        return binding.root
    }

    fun alertEmptyArg() {
        Toast.makeText(context, "Empty Args!", Toast.LENGTH_SHORT).show()
    }
}