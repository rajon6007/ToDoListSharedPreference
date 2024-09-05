package com.example.todolistsharedpreference

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todolistsharedpreference.Adapter.TaskAdapter
import com.example.todolistsharedpreference.Data.Task

class MainActivity : AppCompatActivity() {
    private lateinit var taskList:MutableList<Task>
    private lateinit var recyclerView: RecyclerView
    private lateinit var taskAdapter: TaskAdapter
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editText: EditText
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        sharedPreferences = getSharedPreferences("Task", MODE_PRIVATE)

        recyclerView = findViewById(R.id.rView)
        editText = findViewById(R.id.editText)
        taskList = retriveTask()


        val saveButton : Button = findViewById(R.id.saveBtn)
        saveButton.setOnClickListener{
            val taskTxt = editText.text.toString()
            if (taskTxt.isNotEmpty()){
                val task = Task(taskTxt,false)
                taskList.add(task)
                saveTask(taskList)
                taskAdapter.notifyItemInserted(taskList.size-1)
                editText.text.clear()
            }
            else{
                Toast.makeText(this,"Task title can't be empty",Toast.LENGTH_SHORT).show()
            }
        }
        taskAdapter = TaskAdapter(taskList,object :TaskAdapter.TaskClickListener{
            override fun onEditClick(position: Int) {
                editText.setText(taskList[position].title)
                taskList.removeAt(position)
                taskAdapter.notifyDataSetChanged()
            }

            override fun onDeleteClick(position: Int) {
                val alertDialog = AlertDialog.Builder(this@MainActivity)
                alertDialog.setTitle("Delete Task")
                alertDialog.setMessage("Are you sure you want to delete this task?")
                alertDialog.setPositiveButton("Yes"){_,_->
                    deleteTask(position)
                }
                alertDialog.setPositiveButton("No"){_,_-> }
                alertDialog.show()
            }

        })
        recyclerView.adapter= taskAdapter
        recyclerView.layoutManager =LinearLayoutManager(this)


    }

    private fun saveTask(taskList: MutableList<Task>) {
        val editor =sharedPreferences.edit()
        val taskset =HashSet<String>()

        taskList.forEach{ taskset.add(it.title)}
        editor.putStringSet("Task",taskset)
        editor.apply()

    }

    private fun deleteTask(position: Int) {
        taskList.removeAt(position)
        taskAdapter.notifyItemRemoved(position)
        saveTask(taskList)

    }

    private fun retriveTask(): MutableList<Task> {
        val tasks = sharedPreferences.getStringSet("Task",HashSet())?:HashSet()
        return tasks.map { Task(it,false) }.toMutableList()

    }
}