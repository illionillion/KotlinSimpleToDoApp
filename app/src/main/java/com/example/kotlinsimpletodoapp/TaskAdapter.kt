package com.example.kotlinsimpletodoapp

import android.content.Context
import android.content.DialogInterface
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog

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
        val view: View = convertView ?: LayoutInflater.from(parent?.context)
            .inflate(R.layout.item_task, parent, false)
        val taskNameEditText: EditText = view.findViewById(R.id.taskNameEditText)
        val completeButton: Button = view.findViewById((R.id.completeButton))
        val deleteButton: Button = view.findViewById(R.id.deleteButton)

        taskNameEditText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS
        taskNameEditText.maxLines = 1
        taskNameEditText.isSingleLine = true

        val taskName = tasks[position].task
        taskNameEditText.setText(taskName)

        deleteButton.setOnClickListener{
            parent?.context?.let { it ->
                AlertDialog.Builder(it)
                    .setTitle("タスクの削除")
                    .setMessage("「" + tasks[position]?.task.toString() + "」を本当に削除しますか？")
                    .setPositiveButton("Yes", DialogInterface.OnClickListener { _, _ ->
                        tasks.removeAt(position) // i番目を取得して削除
                        notifyDataSetChanged() // 更新を反映
                    })
                    .setNegativeButton("No", null)
                    .show()
            }
        }


        return view
    }
}