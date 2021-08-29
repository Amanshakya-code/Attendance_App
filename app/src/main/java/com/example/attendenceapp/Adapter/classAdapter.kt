package com.example.attendenceapp.Adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.attendenceapp.R
import com.example.attendenceapp.Constant.constant.Companion.CID
import com.example.attendenceapp.Constant.constant.Companion.CLASS_NAME
import com.example.attendenceapp.Constant.constant.Companion.POSITION
import com.example.attendenceapp.Constant.constant.Companion.SUBJECT_NAME
import com.example.attendenceapp.Model.ClassItem
import com.example.attendenceapp.StudentActivity
import kotlinx.android.synthetic.main.class_item.view.*

class classAdapter: RecyclerView.Adapter<classAdapter.myclassViewHolder>() {

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
        }

    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }


}