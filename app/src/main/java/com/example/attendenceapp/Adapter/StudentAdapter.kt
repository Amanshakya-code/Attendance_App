package com.example.attendenceapp.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.attendenceapp.Model.ClassItem
import com.example.attendenceapp.Model.StudentEntity
import com.example.attendenceapp.R
import com.example.attendenceapp.Model.StudentItem
import kotlinx.android.synthetic.main.student_item.view.*


class StudentAdapter :RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {
    inner class StudentViewHolder(itemView: View):RecyclerView.ViewHolder(itemView)

    private val differcall = object: DiffUtil.ItemCallback<StudentEntity>(){
        override fun areItemsTheSame(oldItem: StudentEntity, newItem: StudentEntity): Boolean {
            return oldItem.Sid == newItem.Sid
        }

        override fun areContentsTheSame(oldItem: StudentEntity, newItem: StudentEntity): Boolean {
            return oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this,differcall)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        return StudentViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.student_item,parent,false))
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        var student = differ.currentList[position]
        holder.itemView.apply {
            student_name.text = student.stname
            student_status.text = student.status
            student_rollnumber.text = student.st_roll

            student_cardView.setOnClickListener {
                var currentStatus = student.status
                if(currentStatus == "P")
                {
                    currentStatus = "A"
                   // student_cardView.setCardBackgroundColor(Color.parseColor("#F6707D"))
                }
                else
                {
                    currentStatus = "P"
                    //student_cardView.setCardBackgroundColor(Color.parseColor("#A6D66E"))
                }
                student.status = currentStatus
                notifyDataSetChanged()
            }
        }

    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

}