package com.app.task_management.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.app.task_management.R
import com.app.task_management.databinding.ActivityTaskBinding

class TaskActivity : AppCompatActivity() {

    val binding by lazy {
        ActivityTaskBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val navController = findNavController(R.id.taskHostFragment)
        binding.bottomNavigation.setupWithNavController(navController)

        binding.fab.setOnClickListener {
            startActivity(Intent(this,AddProjectActivity::class.java))
        }
    }
}