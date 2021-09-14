package com.example.attendenceapp.Adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.attendenceapp.R
import com.example.attendenceapp.Constant.constant.Companion.CID
import com.example.attendenceapp.Constant.constant.Companion.CLASS_NAME
import com.example.attendenceapp.Constant.constant.Companion.POSITION
import com.example.attendenceapp.Constant.constant.Companion.SUBJECT_NAME
import com.example.attendenceapp.Model.ClassItem
import com.example.attendenceapp.Model.StudentEntity
import com.example.attendenceapp.StudentActivity
import com.example.attendenceapp.ViewModel.attendenceViewmodel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.choice_dialog.view.*
import kotlinx.android.synthetic.main.class_item.view.*
import kotlinx.android.synthetic.main.dialogue1.view.*

class classAdapter(var viewModel:attendenceViewmodel): RecyclerView.Adapter<classAdapter.myclassViewHolder>() {

    inner class myclassViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView)

    private val differcall = object: DiffUtil.ItemCallback<ClassItem>(){
        override fun areItemsTheSame(oldItem: ClassItem, newItem: ClassItem): Boolean {
            return oldItem.C_id == newItem.C_id
        }

        override fun areContentsTheSame(oldItem: ClassItem, newItem: ClassItem): Boolean {
            return oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this,differcall)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myclassViewHolder {
        return myclassViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.class_item,parent,false))
    }

    override fun onBindViewHolder(holder: myclassViewHolder, position: Int) {
        val classIem = differ.currentList[position]
        holder.itemView.apply {
            class_tv.text = classIem.ClassName
            subject_tv.text = classIem.SubjectName

            singleCardView.setOnClickListener {
                val intent = Intent(singleCardView.context,StudentActivity::class.java)
                intent.putExtra(CLASS_NAME,classIem.ClassName)
                intent.putExtra(SUBJECT_NAME,classIem.SubjectName)
                intent.putExtra(POSITION,position)
                intent.putExtra(CID,classIem.C_id)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                singleCardView.context.startActivity(intent)
            }

            singleCardView.setOnLongClickListener {
                val dialogView = LayoutInflater.from(singleCardView.context).inflate(R.layout.choice_dialog,null)
                val alertDialog = MaterialAlertDialogBuilder(singleCardView.context)
                    .setView(dialogView)
                    .show()
                dialogView.deleteStudentbtn.setOnClickListener {
                    MaterialAlertDialogBuilder(singleCardView.context, R.style.CustomMaterialDialog)
                        .setIcon(R.drawable.ic_baseline_delete_24)
                        .setTitle("Do You Want to Delete This Class ?")
                        .setMessage("Are you sure You want to delete this class, Note:- Students Info. associated with this class also lost!!")
                        .setPositiveButton("Yes") { _, _ ->
                            viewModel.deleteClass(classIem.C_id!!)
                            viewModel.deleteStudentFromClass(classIem.C_id)
                            viewModel.deleteStatusFromClass(classIem.C_id)
                        }
                        .setNegativeButton("No") { _, _ ->

                        }
                        .show()
                    notifyDataSetChanged()
                    alertDialog.dismiss()
                }
                dialogView.updateStudentbtn.setOnClickListener {
                    val updatedialogview = LayoutInflater.from(class_tv.context).inflate(R.layout.dialogue1,null)
                    val updateAlertDialog = MaterialAlertDialogBuilder(class_tv.context)
                        .setView(updatedialogview)
                        .show()
                    updatedialogview.title_dialog.text = "Update Class"
                    updatedialogview.subject_input.setText(classIem.ClassName)
                    updatedialogview.course_input.setText(classIem.SubjectName)
                    updatedialogview.add_subject_btn.setText("Update")
                    updatedialogview.add_subject_btn.setOnClickListener {
                        if(updatedialogview.subject_input.text.toString().isNotEmpty() && updatedialogview.course_input.text.toString().isNotEmpty())
                        {
                            val classitem = ClassItem(classIem.C_id,updatedialogview.subject_input.text.toString(),updatedialogview.course_input.text.toString())
                            Log.i("checkingdilog","$classitem")
                            viewModel.updateClassDetails(classitem)
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