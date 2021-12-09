package com.example.simpletodo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {

    var listOfTasks = mutableListOf<String>()
    lateinit var adapter: TaskItemAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val onClickListener = object : TaskItemAdapter.OnClickListener{
            override fun onItemLongClicked(position: Int) {
                listOfTasks.removeAt(position)

                adapter.notifyDataSetChanged()

                saveItems()
            }

            override fun onItemClicked(position: Int) {
                val taskItem = listOfTasks[position]

                launchComposeView(taskItem, position)
            }
        }

        loadItems()

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)

        adapter = TaskItemAdapter(listOfTasks, onClickListener)

        recyclerView.adapter= adapter

        recyclerView.layoutManager = LinearLayoutManager(this)

        val inputTextField = findViewById<EditText>(R.id.addTaskField)
        //  Set up button and input field
        findViewById<Button>(R.id.button).setOnClickListener{
            val userInputtedTask = inputTextField.text.toString()

            listOfTasks.add(userInputtedTask)

            adapter.notifyItemInserted(listOfTasks.size - 1)

            inputTextField.setText("")

            saveItems()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == 20) {
            // Extract name value from result extras
            val edittedTask = data?.getExtras()?.getString("task")
            val position = data?.getExtras()?.getInt("position")

            if (position != null) {
                if (edittedTask != null) {
                    listOfTasks.set(position, edittedTask)

                    adapter.notifyItemChanged(position)

                    saveItems()
                }
            }
        }
    }

    fun launchComposeView(taskItem: String, position: Int) {
        val i = Intent(this@MainActivity, EditTaskActivity::class.java)
        i.putExtra("task", taskItem)
        i.putExtra("position", position)
        startActivityForResult(i, 20)
    }

    fun getDataFile(): File{
        return File(filesDir, "data.txt")
    }

    fun loadItems(){
        try{
            listOfTasks = FileUtils.readLines(getDataFile(), Charset.defaultCharset())
        } catch(ioException: IOException){
            ioException.printStackTrace()

        }
    }

    fun saveItems(){
        try{
            FileUtils.writeLines(getDataFile(), listOfTasks)
        } catch(ioException: IOException){
            ioException.printStackTrace()
        }
    }

}
