package com.robotbot.financetracker.presentation.profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.robotbot.financetracker.databinding.FragmentSettingsBinding
import com.robotbot.financetracker.databinding.FragmentThemeChooseBinding
import com.robotbot.financetracker.presentation.FinanceTrackerApp
import com.robotbot.financetracker.presentation.ViewModelFactory
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class ThemeChooseFragment : Fragment() {

    private val component by lazy {
        (requireActivity().application as FinanceTrackerApp).component
    }

    private var _binding: FragmentThemeChooseBinding? = null
    private val binding: FragmentThemeChooseBinding
        get() = _binding ?: throw RuntimeException("FragmentThemeChooseBinding == null")

    @Inject
    lateinit var adapter: ThemeSettingsAdapter

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: ThemeChooseViewModel by viewModels { viewModelFactory }

    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentThemeChooseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter.onSettingClickListener = {
            viewModel.setThemeInSettings(it)
        }
        binding.rvList.adapter = adapter
        observeViewModel()
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect {
                    when (it) {
                        ThemeChooseState.Initial -> {}
                        ThemeChooseState.Loading -> {

                        }
                        is ThemeChooseState.Content -> {
                            adapter.submitList(it.settings)
                        }
                        ThemeChooseState.Error -> {

                        }

                        ThemeChooseState.WorkEnded -> {
                            findNavController().popBackStack()
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}