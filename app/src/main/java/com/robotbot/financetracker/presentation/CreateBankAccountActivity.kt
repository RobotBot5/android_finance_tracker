package com.robotbot.financetracker.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.robotbot.financetracker.R
import com.robotbot.financetracker.databinding.ActivityCreateBankAccountBinding
import com.robotbot.financetracker.domain.entities.Currency

class CreateBankAccountActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityCreateBankAccountBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.spnCurrency.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, Currency.entries)
    }

    companion object {

        fun newIntent(context: Context): Intent {
            return Intent(context, CreateBankAccountActivity::class.java)
        }

    }
}