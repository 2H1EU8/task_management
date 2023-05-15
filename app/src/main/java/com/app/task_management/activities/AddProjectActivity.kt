/*
* Project name: Task Management
* Author: 2H1eu8
* This class handle all function on Create new project activity :
*           -validate date
*           -get date picker
*           -insert date to FireBase RealTime Data Base
* */

package com.app.task_management.activities

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import br.com.simplepass.loadingbutton.customViews.CircularProgressButton
import com.app.task_management.R
import com.app.task_management.data.Project
import com.app.task_management.util.Resource
import com.app.task_management.viewmodel.ProjectViewModel
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class AddProjectActivity : AppCompatActivity() {

    private lateinit var backBtn: TextView
    private lateinit var createBtn: CircularProgressButton
    private lateinit var startPicker: MaterialButton
    private lateinit var endPicker: MaterialButton
    private lateinit var startDate: TextView
    private lateinit var endDate: TextView

    private var sD = Calendar.getInstance()
    private var eD = Calendar.getInstance()

    private lateinit var projectName: EditText
    private lateinit var projectDescription: EditText

    private val viewModel by viewModels<ProjectViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_project)

        //handle back button
        backBtn = findViewById(R.id.backBtn)
        backBtn.setOnClickListener {
            onBackPressed();
        }

        //handle date picker
        startDate = findViewById(R.id.startDate)
        endDate = findViewById(R.id.endDate)
        startPicker = findViewById(R.id.startDate_picker)
        endPicker = findViewById(R.id.endDate_picker)

        startPicker.setOnClickListener {
            val c = Calendar.getInstance()
            val mYear = c.get(Calendar.YEAR)
            val mMonth = c.get(Calendar.MONTH)
            val mDay = c.get(Calendar.DAY_OF_MONTH)

            // Tạo DatePickerDialog
            val dateStartPickerDialog = DatePickerDialog(this,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    // set data for text
                    sD.set(year,monthOfYear,dayOfMonth)
                    startDate.text = "$dayOfMonth/${monthOfYear + 1}\n$year"
                }, mYear, mMonth, mDay)
            dateStartPickerDialog.show()

        }

        endPicker.setOnClickListener {
            val c = Calendar.getInstance()
            val mYear = c.get(Calendar.YEAR)
            val mMonth = c.get(Calendar.MONTH)
            val mDay = c.get(Calendar.DAY_OF_MONTH)

            val dateEndPickerDialog = DatePickerDialog(this,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    // set data for text
                    eD.set(year,monthOfYear,dayOfMonth)
                    if(eD.before(sD)) {
                        Toast.makeText(this,"End date must before start date, please change date planted", Toast.LENGTH_LONG).show()
                        eD.set(0,0,0)
                        return@OnDateSetListener;
                    }else endDate.text = "$dayOfMonth/${monthOfYear + 1}\n$year"
                }, mYear, mMonth, mDay)
            dateEndPickerDialog.show()
        }
        //end handle date picker

        //edit text
        projectName = findViewById(R.id.editTextProjectName)
        projectDescription = findViewById(R.id.editTextProjectDescription)


        //handle button
        createBtn = findViewById(R.id.buttonCreateProject)
        createBtn.setOnClickListener {
            val pName = projectName.text.toString()
            val pDes = projectDescription.text.toString()
            // validate project name
            if (pName.isNotEmpty()) {
                if(pName.length > 100) {
                    projectName.error = "Project name must shorter than 100 character"
                    return@setOnClickListener
                }
            }else {
                projectName.error = "Project name cannot be empty"
                return@setOnClickListener
            }
            //validate project description
            if(pDes.isEmpty()) {
                projectDescription.error = "Project description cannot be empty"
                return@setOnClickListener
            }
            //validate day
            if(eD.before(sD)) {
                Toast.makeText(this,"End date must before start date, please change date planted", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            //add project
            val project  = Project(
                pName,
                startDate.text.toString().trim(),
                endDate.text.toString().trim(),
                pDes
            )
            viewModel.addProject(project)
            //update view
            lifecycleScope.launchWhenStarted {
                viewModel.task.collect{
                    when(it) {
                        is Resource.Loading -> {
                            createBtn.startAnimation()
                        }
                        is Resource.Success -> {
                            createBtn.revertAnimation()
                            Toast.makeText(this@AddProjectActivity,"Add success!",Toast.LENGTH_LONG).show()
                            startActivity(Intent(this@AddProjectActivity,TaskActivity::class.java))
                        }
                        is Resource.Error -> {
                            createBtn.revertAnimation()
                        }
                        else -> Unit
                    }
                }
            }
        }
    }
}