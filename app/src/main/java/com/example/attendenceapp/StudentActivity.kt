package com.example.attendenceapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.attendenceapp.Adapter.StudentAdapter
import com.example.attendenceapp.Constant.constant.Companion.CID

import com.example.attendenceapp.Constant.constant.Companion.CLASS_NAME
import com.example.attendenceapp.Constant.constant.Companion.POSITION
import com.example.attendenceapp.Constant.constant.Companion.SUBJECT_NAME
import com.example.attendenceapp.DataBase.DatabaseInstance
import com.example.attendenceapp.Model.StudentEntity
import com.example.attendenceapp.Model.StudentItem
import com.example.attendenceapp.Model.statusEntity
import com.example.attendenceapp.Repository.repository
import com.example.attendenceapp.ViewModel.attendenceViewmodel
import com.example.attendenceapp.ViewModel.viewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder

import kotlinx.android.synthetic.main.activity_student.*
import kotlinx.android.synthetic.main.dialog.view.*
import kotlinx.android.synthetic.main.toolbar.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.properties.Delegates

class StudentActivity : AppCompatActivity() {
    private lateinit var subjectName:String
    private lateinit var className:String
    private lateinit var StudentList:ArrayList<StudentItem>
    private  lateinit var adapter: StudentAdapter
    private var cid:Int = 0
    lateinit var viewModel : attendenceViewmodel
    val currenttime = Calendar.getInstance().time
    val fm = SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault())
    var date = fm.format(currenttime);
    private var position by Delegates.notNull<Int>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student)

        subjectName = intent.getStringExtra(SUBJECT_NAME).toString()
        className = intent.getStringExtra(CLASS_NAME).toString()
        position = intent.getIntExtra(POSITION,-1)
        cid = intent.getIntExtra(CID,-1)

        val repository = repository(DatabaseInstance.getDatabaseInstance(this))
        val viewModelFactory = viewModelFactory(application,respository = repository)
        viewModel = ViewModelProvider(this,viewModelFactory).get(attendenceViewmodel::class.java)

        student_recylerView.layoutManager = LinearLayoutManager(this)
        StudentList = arrayListOf()
        adapter = StudentAdapter(StudentList)
        student_recylerView.adapter = adapter
        addstudentfb.setOnClickListener {
            showDailog()
        }
        setToolBar()
        viewModel.getAllStudent(cid).observe(this, Observer {
            StudentList.clear()
            Log.i("check","$it")
            for(student in it)
            {
                val st = StudentItem(student.Sid!!,student.st_roll,student.stname)
                StudentList.add(st)
            }
            adapter.notifyDataSetChanged()
           // loadStatus()

        })

    }

    private fun loadStatus() {
       Log.i("statusfos","$StudentList")
        for(studentdb in StudentList)
        {
            val currentStudentSid = studentdb.sid
            val CurrentStudentStatus = viewModel.getStatus(currentStudentSid,date)
            studentdb.status = CurrentStudentStatus
        }
        Log.i("statusfos","$StudentList")
        adapter.notifyDataSetChanged()
    }

    private fun showDailog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog,null)
        dialogView.title_dialog.text = "Add Student"
        dialogView.et01.hint = "Enter RollNumber"
        dialogView.etd02.hint = "Enter Name"

        val alertDialog = MaterialAlertDialogBuilder(this, R.style.CustomMaterialDialog)
            .setView(dialogView)
            .show()

        dialogView.cancel.setOnClickListener {
            alertDialog.dismiss()
        }

        dialogView.addsubjectbtn.setOnClickListener {
            if(dialogView.et01.text.toString().isNotEmpty() && dialogView.etd02.text.toString().isNotEmpty())
            {
                val student = StudentEntity(null, cid,dialogView.et01.text.toString(),dialogView.etd02.text.toString())
                viewModel.saveStudentData(student)
                alertDialog.dismiss()
            }
        }
    }
    private fun setToolBar() {
        toolbar_student.title_toolbar.text = className
        toolbar_student.subtitle_toolbar.text = subjectName
        toolbar_student.backbutton.setOnClickListener {
            onBackPressed()
        }
        toolbar_student.saveBtn.setOnClickListener {
            for(student in StudentList)
            {
                var state = student.status
                if(state != "P") state = "A"
                val status = statusEntity(null,student.sid,date,state)
                viewModel.saveStatus(status)
            }
        }
    }
}