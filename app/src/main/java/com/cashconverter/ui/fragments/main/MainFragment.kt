package com.cashconverter.ui.fragments.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.cashconverter.R
import com.cashconverter.databinding.FragmentMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentMainBinding.inflate(inflater,container,false)

        binding.btnConvert.setOnClickListener {
            mainViewModel.convert(
                binding.etAmount.text.toString(),
                binding.fromCurrency.selectedItem.toString(),
                binding.toCurrency.selectedItem.toString()
            )
        }

        binding.etAmount.apply {
            setOnEditorActionListener { v, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    mainViewModel.convert(
                        binding.etAmount.text.toString(),
                        binding.fromCurrency.selectedItem.toString(),
                        binding.toCurrency.selectedItem.toString()
                    )
                    true
                } else {
                    false
                }

            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.conversion.collect{ event ->
                    when(event) {
                        is MainViewModel.CashEvent.Success -> {
                            binding.progressBar.visibility = View.GONE
                            binding.tvResult.text = event.resultText

                        }
                        is MainViewModel.CashEvent.Failure -> {
                            binding.progressBar.visibility = View.GONE
                            binding.tvResult.text = event.errorText
                        }
                        is MainViewModel.CashEvent.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }
                        else -> Unit
                    }

                }
            }
        }

        return binding.root
    }

}