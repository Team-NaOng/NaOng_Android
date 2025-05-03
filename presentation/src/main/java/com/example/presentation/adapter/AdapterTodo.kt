package com.example.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.presentation.R

class AdapterTodo : RecyclerView.Adapter<AdapterTodo.TodoViewHolder>() {

    private val itemList = mutableListOf<String>()  // 테스트용 텍스트

    fun submitList(newList: List<String>) {
        itemList.clear()
        itemList.addAll(newList)
        notifyDataSetChanged()
    }

    inner class TodoViewHolder(private val binding: ViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String) {
            binding.root.findViewById<TextView>(R.id.textTitle).text = item
            binding.root.findViewById<TextView>(R.id.textTime).visibility =
                if (item.contains("시간없음")) View.GONE else View.VISIBLE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cell_todolist, parent, false)
        return TodoViewHolder(object : ViewBinding {
            override fun getRoot(): View = view
        })
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    override fun getItemCount(): Int = itemList.size
}
