package com.example.simpletodo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText

class EditTaskActivity : AppCompatActivity() {

    lateinit var inputText: EditText;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        inputText = findViewById<EditText>(R.id.editTask)

        val taskToEdit = getIntent().getStringExtra("task")
        val position = getIntent().getIntExtra("position", 0)

        inputText.setText(taskToEdit)

        findViewById<Button>(R.id.button2).setOnClickListener{
            onSubmit(findViewById<Button>(R.id.button2), position)
        }
    }

    fun onSubmit(v: View, position: Int) {
        // closes the activity and returns to first screen
        val data = Intent()

        data.putExtra("task", inputText.text.toString())
        data.putExtra("position", position)
        setResult(RESULT_OK, data)
        finish()
    }
}