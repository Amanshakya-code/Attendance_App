package com.example.attendenceapp.Adapter

import android.graphics.Color
import android.text.InputType
import android.util.Log
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
import com.example.attendenceapp.ViewModel.attendenceViewmodel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.choice_dialog.view.*
import kotlinx.android.synthetic.main.class_item.view.*
import kotlinx.android.synthetic.main.dialogue1.view.*
import kotlinx.android.synthetic.main.student_item.view.*


class StudentAdapter(var viewModel:attendenceViewmodel) :RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {
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
            if(student.status == "P"){
                student_status_present.text = "P"
                student_status_present.visibility = View.VISIBLE
                student_status_absent.visibility = View.GONE
            }
            else if(student.status == "A"){
                student_status_absent.text = "A"
                student_status_present.visibility = View.GONE
                student_status_absent.visibility = View.VISIBLE
            }
            student_rollnumber.text = student.st_roll
            /*if(student.status == "P"){
                student_cardView.setCardBackgroundColor(Color.parseColor("#A6D66E"))
            }
            else if(student.status == "A"){
                student_cardView.setCardBackgroundColor(Color.parseColor("#F6707D"))
            }*/
            student_cardView.setOnClickListener {
                var currentStatus = student.status
                if(currentStatus == "P")
                {
                    currentStatus = "A"
                    student_status_absent.text = "A"
                    student_status_present.visibility = View.GONE
                    student_status_absent.visibility = View.VISIBLE
                }
                else if(currentStatus == "" || currentStatus == "A")
                {
                    currentStatus = "P"
                    student_status_present.text = "P"
                    student_status_present.visibility = View.VISIBLE
                    student_status_absent.visibility = View.GONE
                }
                student.status = currentStatus
                notifyDataSetChanged()
            }
            student_cardView.setOnLongClickListener {
                val dialogView = LayoutInflater.from(student_cardView.context).inflate(R.layout.choice_dialog,null)
                val alertDialog = MaterialAlertDialogBuilder(student_cardView.context)
                    .setView(dialogView)
                    .show()

                dialogView.deleteStudentbtn.setOnClickListener {
                    MaterialAlertDialogBuilder(student_cardView.context, R.style.CustomMaterialDialog)
                        .setIcon(R.drawable.ic_baseline_delete_24)
                        .setTitle("Do You Want to Delete This Student ?")
                        .setMessage("Are you sure You want to delete this student, Note:- Students Attendance will also be lost!!")
                        .setPositiveButton("Yes") { _, _ ->
                            viewModel.deleteStudent(student.Sid!!)
                            viewModel.deleteStudentStatus(student.Sid!!)
                        }
                        .setNegativeButton("No") { _, _ ->

                        }
                        .show()
                    notifyDataSetChanged()
                    alertDialog.dismiss()
                }

                dialogView.updateStudentbtn.setOnClickListener {
                    val updatedialogview = LayoutInflater.from(student_cardView.context).inflate(R.layout.dialogue1,null)
                    val updateAlertDialog = MaterialAlertDialogBuilder(student_cardView.context)
                        .setView(updatedialogview)
                        .show()
                    updatedialogview.title_dialog.text = "Update Student"
                    updatedialogview.subject_input.setText(student.st_roll)
                    updatedialogview.course_input.setText(student.stname)
                    updatedialogview.add_subject_btn.setText("Update")
                    updatedialogview.Text_input_layout_subject.hint = "Roll Number"
                    updatedialogview.Text_input_layout_course.hint = "Name"
                    updatedialogview.subject_input.inputType = InputType.TYPE_CLASS_NUMBER

                    updatedialogview.add_subject_btn.setOnClickListener {
                        if(updatedialogview.subject_input.text.toString().isNotEmpty() && updatedialogview.course_input.text.toString().isNotEmpty())
                        {
                            val studentll = StudentEntity(student.Sid, student.C_id,updatedialogview.subject_input.text.toString(),updatedialogview.course_input.text.toString())
                            viewModel.updateStudent(studentll)
                            updateAlertDialog.dismiss()
                        }
                    }
                    notifyDataSetChanged()
                    alertDialog.dismiss()
                }
                true
            }
        }

    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

}