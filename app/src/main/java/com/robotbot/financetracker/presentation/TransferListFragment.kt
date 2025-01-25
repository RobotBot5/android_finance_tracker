package com.robotbot.financetracker.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.robotbot.financetracker.databinding.FragmentTransfersListBinding

class TransferListFragment : Fragment() {

    private var _binding: FragmentTransfersListBinding? = null
    private val binding: FragmentTransfersListBinding
        get() = _binding ?: throw RuntimeException("FragmentTransfersListBinding == null")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTransfersListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}