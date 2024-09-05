package com.example.todolistsharedpreference.Adapter

import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.todolistsharedpreference.Data.Task
import com.example.todolistsharedpreference.databinding.ListItemBinding

class TaskAdapter(private val tasklist: MutableList<Task>,private val clicklisten: TaskClickListener):
    RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {
    interface TaskClickListener {
        fun onEditClick(position: Int)
        fun onDeleteClick(position: Int)

    }

    class TaskViewHolder(val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(task: Task) {
            binding.textView.text = task.title
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return tasklist.size
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasklist[position]
        holder.bind(task)
        holder.binding.editBtn.setOnClickListener {
            clicklisten.onEditClick(position)
        }
        holder.binding.deleteBtn.setOnClickListener {
            clicklisten.onDeleteClick(position)
        }
        holder.binding.checkBox.isChecked = task.isComplete
        holder.binding.checkBox.setOnCheckedChangeListener { _, isChecked ->
            task.isComplete = isChecked
        }
    }
}

