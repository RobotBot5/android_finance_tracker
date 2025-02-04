package com.robotbot.financetracker.presentation.profile

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.robotbot.financetracker.R
import com.robotbot.financetracker.databinding.FragmentSettingsBinding
import com.robotbot.financetracker.domain.entities.SettingsEnum
import com.robotbot.financetracker.presentation.FinanceTrackerApp
import com.robotbot.financetracker.presentation.ViewModelFactory
import kotlinx.coroutines.launch
import javax.inject.Inject

class SettingsFragment : Fragment() {

    private val component by lazy {
        (requireActivity().application as FinanceTrackerApp).component
    }

    private var _binding: FragmentSettingsBinding? = null
    private val binding: FragmentSettingsBinding
        get() = _binding ?: throw RuntimeException("FragmentSettingsBinding == null")

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: SettingsViewModel by viewModels { viewModelFactory }

    private val navController by lazy { findNavController() }

    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        binding.settingTheme.setOnClickListener {
            navController.navigate(
                SettingsFragmentDirections.actionSettingsFragmentToThemeChooseFragment(
                    SettingsEnum.THEME
                )
            )
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect {
                    when (it) {
                        SettingsState.Initial -> {}
                        SettingsState.Loading -> {

                        }
                        is SettingsState.Content -> {
                            binding.settingTheme.settingValue = it.translatedSettings.theme
                        }
                        SettingsState.Error -> {

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

    private fun logDebug(msg: String) {
        Log.d(LOG_TAG, msg)
    }

    companion object {
        private const val LOG_TAG = "SettingsFragment"
    }
}