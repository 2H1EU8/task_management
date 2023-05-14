package com.app.task_management.data

data class Project(
    val name: String? = null, val startDate: String? = null, val endDate: String? = null,
    val description: String? = null, val status: Int? = 0, val process: Double = 0.0
) {
    constructor(): this("", "", "","",0,0.0)
}

/*
* Project : name, sDate, eDate,
* description, status['pending', 'in-progess','finished'],
*  process [0% -> 100%]
* */