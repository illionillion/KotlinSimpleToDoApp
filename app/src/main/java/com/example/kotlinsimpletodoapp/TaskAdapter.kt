package com.example.kotlinsimpletodoapp

import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.Paint
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.doOnTextChanged

public class TaskAdapter(val context: Context, private val tasks: MutableList<TaskItem> ) : BaseAdapter() {
    override fun getCount(): Int {
        return tasks.count()
    }

    override fun getItem(position: Int): TaskItem {
        return tasks[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val taskItem: TaskItem = tasks[position]
        val view: View = convertView ?: LayoutInflater.from(parent?.context)
            .inflate(R.layout.item_task, parent, false)
        val taskNameEditText: EditText = view.findViewById(R.id.taskNameEditText)
        val completeButton: Button = view.findViewById((R.id.completeButton))
        val deleteButton: Button = view.findViewById(R.id.deleteButton)

        taskNameEditText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS
        taskNameEditText.maxLines = 1
        taskNameEditText.isSingleLine = true

        completeButton.text = if (taskItem.isCompleted) "Incomplete" else "Complete"
        completeButton.setBackgroundColor(Color.parseColor(if (taskItem.isCompleted) "#666666" else "#4CAF50"))
        if (taskItem.isCompleted) {
            // フラグが true の場合は打ち消し線を追加
            taskNameEditText.paintFlags = taskNameEditText.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            // フラグが false の場合は打ち消し線を取り消す
            taskNameEditText.paintFlags = taskNameEditText.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }

        val taskName = taskItem.task
        taskNameEditText.setText(taskName)

        deleteButton.setOnClickListener{
            parent?.context?.let { it ->
                AlertDialog.Builder(it)
                    .setTitle("タスクの削除")
                    .setMessage("「" + taskItem?.task.toString() + "」を本当に削除しますか？")
                    .setPositiveButton("Yes", DialogInterface.OnClickListener { _, _ ->
                        tasks.removeAt(position) // i番目を取得して削除
                        notifyDataSetChanged() // 更新を反映
                    })
                    .setNegativeButton("No", null)
                    .show()
            }
        }

        completeButton.setOnClickListener {

            val prevFlag = taskItem.isCompleted
            val newFlag = !prevFlag
            taskItem.isCompleted = newFlag
            completeButton.text = if(newFlag) "Incomplete" else "Complete"
            completeButton.setBackgroundColor(Color.parseColor(if(newFlag) "#666666" else "#4CAF50"))
            if (newFlag) {
                // フラグが true の場合は打ち消し線を追加
                taskNameEditText.paintFlags = taskNameEditText.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                // フラグが false の場合は打ち消し線を取り消す
                taskNameEditText.paintFlags = taskNameEditText.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            }
            notifyDataSetChanged()

        }

        // 以下が変更が加えられたら、tasks[position].taskに再代入
        taskNameEditText.doOnTextChanged { text, _, _, _ ->
            taskItem.task = text.toString()
        }

        return view
    }
}