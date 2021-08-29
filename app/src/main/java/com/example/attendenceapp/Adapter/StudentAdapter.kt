package com.example.attendenceapp.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.attendenceapp.R
import com.example.attendenceapp.Model.StudentItem
import kotlinx.android.synthetic.main.student_item.view.*


class StudentAdapter(private val StudentList:ArrayList<StudentItem>) :RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {
    inner class StudentViewHolder(itemView: View):RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        return StudentViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.student_item,parent,false))
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        var student = StudentList[position]
        holder.itemView.apply {
            student_name.text = student.name
            student_status.text = student.status
            student_rollnumber.text = student.roll

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
        return StudentList.size
    }

}