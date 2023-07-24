package com.example.kotlinsimpletodoapp

import android.content.Context

public class TaskPreferences(context: Context) {
    private val sharedPreferences = context.getSharedPreferences("com.example.kotlinsimpletodoapp.TaskPreferences", Context.MODE_PRIVATE)

    fun saveTaskList(taskList: MutableList<TaskItem>) {
        val json = StringBuilder()
        for (taskItem in taskList) {
            json.append("${taskItem.task},${taskItem.isCompleted}|")
        }
        sharedPreferences.edit().putString("taskListKey", json.toString()).apply()
    }

    fun getTaskList(): MutableList<TaskItem> {
        val taskList = mutableListOf<TaskItem>()
        val json = sharedPreferences.getString("taskListKey", null)
        json?.split("|")?.forEach { item ->
            item.split(",")?.let { values ->
                if (values.size == 2) {
                    val taskName = values[0]
                    val isCompleted = values[1].toBoolean()
                    taskList.add(TaskItem(taskName, isCompleted))
                }
            }
        }
        return taskList
    }
}
