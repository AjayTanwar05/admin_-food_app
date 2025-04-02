package com.ajaytanwar.adminfoodapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ajaytanwar.adminfoodapp.databinding.ActivityAllItemBinding
import com.ajaytanwar.adminfoodapp.databinding.ActivityNewAccountBinding

class New_account_activity : AppCompatActivity() {


    private val binding: ActivityNewAccountBinding by lazy {
        ActivityNewAccountBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        binding.backbutton.setOnClickListener {
            finish()
        }

    }
}