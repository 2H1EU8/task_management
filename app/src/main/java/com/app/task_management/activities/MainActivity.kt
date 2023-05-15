/*
* Project name: Task Management
* Author: 2H1eu8
* This class handle all function on Main activity :
*           -switch other fragment
*           -handle bottom nav
*           -intent to create new project activity
* */
package com.app.task_management.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.app.task_management.*
import com.app.task_management.databinding.ActivityMainBinding
import com.app.task_management.fragments.NotifyFragment
import com.app.task_management.fragments.ProjectFragment
import com.app.task_management.fragments.TaskFragment
import com.app.task_management.fragments.TodoFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var addBtn: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}