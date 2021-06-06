package com.example.capgg.calculator

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.capgg.R
import com.example.capgg.data.GradeDatabase
import com.example.capgg.databinding.FragmentHomePageBinding

class HomePageFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentHomePageBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_home_page, container, false)

        val application = requireNotNull(this.activity).application

        val dataSource = GradeDatabase.getInstance(application).gradeDatabaseDao

        val viewModelFactory = HomePageViewModelFactory(dataSource, application)

        val viewModel = ViewModelProvider(this, viewModelFactory).get(HomePageViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.updateCAP()

        viewModel.navigateToAddGradeRecord.observe(viewLifecycleOwner, Observer { it ->
            it?.let {
                if (it) {
                    this.findNavController()
                        .navigate(
                            HomePageFragmentDirections.actionHomePageFragmentToAddGradeFragment()
                        )
                }
                viewModel.doneNavigating()
            }
        })

        viewModel.totalCAP.observe(viewLifecycleOwner, Observer { it ->
            it?.let {
                binding.capText.text = String.format("%.2f", it)
            }
        })

        val manager = LinearLayoutManager(activity)
        binding.recyclerView.layoutManager = manager

        val adapter = GradeRecordAdapter(GradeRecordListener { gradeRecordId ->
            viewModel.onGradeRecordClicked(gradeRecordId)
        })
        binding.recyclerView.adapter = adapter

        viewModel.records.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
                viewModel.updateCAP()
            }
        })

        viewModel.navigateToRecordEdit.observe(viewLifecycleOwner, Observer {
            it?.let {
                this.findNavController().navigate(
                    HomePageFragmentDirections.actionHomePageFragmentToEditGradeFragment(it)
                )
            }
        })

        viewModel.connected.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it) {
                    binding.bitcoinPriceStatus.setImageResource(R.drawable.ic_baseline_cloud_done_24)
                }
            }
        })

        viewModel.properties.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.bitcoinPriceText.setText(String.format("%.2f", it.data.priceUsd))
                val sdf = java.text.SimpleDateFormat("HH:mm:ss")
                val date = java.util.Date(it.timeStamp)
                binding.bitcoinPriceLastUpdateText.setText(sdf.format(date))
            }
        })

        return binding.root
    }
}