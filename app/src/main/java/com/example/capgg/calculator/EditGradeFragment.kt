package com.example.capgg.calculator

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.capgg.data.GradeDatabase
import com.example.capgg.databinding.FragmentAddGradeBinding
import com.example.capgg.databinding.FragmentEditGradeBinding

class EditGradeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentEditGradeBinding.inflate(inflater, container, false)

        val application = requireNotNull(this.activity).application

        val arguments = EditGradeFragmentArgs.fromBundle(requireArguments())

        val dataSource = GradeDatabase.getInstance(application).gradeDatabaseDao
        val viewModelFactory = EditGradeViewModelFactory(arguments.recordId, dataSource)

        val viewModel =
            ViewModelProvider(this, viewModelFactory).get(EditGradeViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.activeSubmission.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it) {
                    viewModel.onUpdateGradeRecord(binding)
                }
            }
        })

        viewModel.navigateToHomePage.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it) {
                    Log.i("EditFragment", "Doing Navigation")
                    findNavController().navigate(
                        EditGradeFragmentDirections.actionEditGradeFragmentToHomePageFragment()
                    )
                    Log.i("EditFragment", "Navigated")
                    viewModel.onDoneNavigate()
                }
            }
        })

        return binding.root
    }
}