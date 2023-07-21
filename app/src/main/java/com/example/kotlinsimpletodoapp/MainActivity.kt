package com.example.kotlinsimpletodoapp

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.appcompat.app.AlertDialog

data class TaskItem(val task: String, val isCompleted: Boolean)

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Viewの取得
        val addBtn = findViewById<Button>(R.id.addButton)
        val resetBtn = findViewById<Button>(R.id.resetButton)
        val list = findViewById<ListView>(R.id.lv)

        // アダプターに入れてListViewにセット
        val adapter = ArrayAdapter<TaskItem>(
            this,
            android.R.layout.simple_list_item_1,
            mutableListOf() // 空のリストが初期値
        )

        list.adapter = adapter

        addBtn.setOnClickListener {
            // EditTextのインスタンス生成
            val et = EditText(this)
            // 改行できないように設定
            et.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS
            et.maxLines = 1
            et.isSingleLine = true

            AlertDialog.Builder(this)// アラートダイアログ
                .setTitle("タスクを追加") // タイトル
                .setMessage("何をしますか？") // メッセージ
                .setView(et) // EditText（入力欄を表示）
                .setPositiveButton("追加", DialogInterface.OnClickListener{_, _ ->
                    // add()でアダプターに追加
                    val newToDo = et.text.toString()
                    adapter.add(TaskItem(newToDo, false)) // アダプターでリストを管理してるのでそこに追加
                    adapter.notifyDataSetChanged() // 更新を反映
                }) // OKボタン
                .setNegativeButton("キャンセル", null) // キャンセルボタン
                .show() // 表示させる
        }

        resetBtn.setOnClickListener {
            if (adapter.count == 0) return@setOnClickListener
            AlertDialog.Builder(this)
                .setTitle("タスクの全削除")
                .setMessage("本当にタスクを全て削除しますか？")
                .setPositiveButton("Yes", DialogInterface.OnClickListener { _, _ ->
                    adapter.clear() // クリア
                    adapter.notifyDataSetChanged() // 更新を反映
                })
                .setNegativeButton("No", null)
                .show()
        }

        list.setOnItemClickListener { _, _, i, _ ->
            AlertDialog.Builder(this)
                .setTitle("タスクの削除")
                .setMessage("「" + adapter.getItem(i)?.task.toString() + "」を本当に削除しますか？")
                .setPositiveButton("Yes", DialogInterface.OnClickListener { _, _ ->
                    adapter.remove(adapter.getItem(i)) // i番目を取得して削除
                    adapter.notifyDataSetChanged() // 更新を反映
                })
                .setNegativeButton("No", null)
                .show()
        }
    }
}