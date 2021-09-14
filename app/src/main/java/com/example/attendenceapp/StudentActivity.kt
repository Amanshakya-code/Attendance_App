package com.example.attendenceapp

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.DatePicker
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.attendenceapp.Adapter.StudentAdapter
import com.example.attendenceapp.Constant.constant.Companion.CID

import com.example.attendenceapp.Constant.constant.Companion.CLASS_NAME
import com.example.attendenceapp.Constant.constant.Companion.NAMEARRAY
import com.example.attendenceapp.Constant.constant.Companion.POSITION
import com.example.attendenceapp.Constant.constant.Companion.ROLLARRAY
import com.example.attendenceapp.Constant.constant.Companion.SIDARRAY
import com.example.attendenceapp.Constant.constant.Companion.SUBJECT_NAME

import com.example.attendenceapp.Constant.constant.Companion.nameArray
import com.example.attendenceapp.Constant.constant.Companion.rollnumArray
import com.example.attendenceapp.Constant.constant.Companion.sidArray
import com.example.attendenceapp.DataBase.DatabaseInstance
import com.example.attendenceapp.Model.StudentEntity
import com.example.attendenceapp.Model.StudentItem
import com.example.attendenceapp.Model.statusEntity
import com.example.attendenceapp.Repository.repository
import com.example.attendenceapp.ViewModel.attendenceViewmodel
import com.example.attendenceapp.ViewModel.viewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar

import kotlinx.android.synthetic.main.activity_student.*
import kotlinx.android.synthetic.main.dialogue1.view.*
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.android.synthetic.main.toolbar.view.*
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.properties.Delegates

class StudentActivity : AppCompatActivity() {
    private lateinit var subjectName:String
    private lateinit var className:String
    private lateinit var StudentList:ArrayList<StudentEntity>
    private  lateinit var adapter: StudentAdapter
    private var cid:Int = 0
    lateinit var viewModel : attendenceViewmodel
    val currenttime = Calendar.getInstance().time
    val fm = SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault())
    var date = fm.format(currenttime);
    private var position by Delegates.notNull<Int>()
    lateinit var myCalendar: Calendar
    lateinit var dateSetListener: DatePickerDialog.OnDateSetListener


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student)

        subjectName = intent.getStringExtra(SUBJECT_NAME).toString()
        className = intent.getStringExtra(CLASS_NAME).toString()
        position = intent.getIntExtra(POSITION,-1)
        cid = intent.getIntExtra(CID,-1)
        myCalendar = Calendar.getInstance()
        //toolbar_student.subject_tv.text = date.toString()

        val repository = repository(DatabaseInstance.getDatabaseInstance(this))
        val viewModelFactory = viewModelFactory(application,respository = repository)
        viewModel = ViewModelProvider(this,viewModelFactory).get(attendenceViewmodel::class.java)

        student_recylerView.layoutManager = LinearLayoutManager(this)
        StudentList = arrayListOf()
        adapter = StudentAdapter(viewModel)
        student_recylerView.adapter = adapter
        addstudentfb.setOnClickListener {
            showDailog()
        }
        setToolBar()
        viewModel.getAllStudent(cid).observe(this, Observer {
            StudentList.clear()
            Log.i("check","$it")



            adapter.differ.submitList(it)

            adapter.notifyDataSetChanged()
            StudentList.clear()
            for(student in it)
            {
                StudentList.add(student)
            }

        })
        loadStatus.setOnClickListener {
            Toast.makeText(this, "Refreshing...", Toast.LENGTH_SHORT).show()
            loadStatus()
        }
    }



    private fun loadStatus() {

        Log.i("statusfos","$date")


        for(i in 0..StudentList.size-1){
            val student = StudentList.get(i)
            val currentStudentSid = student.Sid!!
            Log.i("chaman","$currentStudentSid")
            viewModel.getStatus(currentStudentSid,date).observe(this, Observer {
                if(it!=null){
                    student.status = it.status
                }
            })
        }

        /*  viewModel.statusView.observe(this, Observer {

              if(it.size == StudentList.size){
                  Log.i("daks","$it")
                  attendance(it)
              }
          })*/
        adapter.differ.submitList(StudentList)
        Log.i("statusfos","$StudentList")
        adapter.notifyDataSetChanged()
    }

    private fun attendance(mp:HashMap<Int,String>) {
        for (student in StudentList)
        {
            var currentStudentSid = student.Sid
            var status = mp.get(currentStudentSid)
            if(status!=null)
                student.status = status!!
        }
    }

    private fun showDailog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialogue1,null)
        dialogView.title_dialog.text = "Add Student"
        dialogView.Text_input_layout_subject.hint = "Roll Number"
        dialogView.Text_input_layout_course.hint = "Name"

        dialogView.subject_input.inputType = InputType.TYPE_CLASS_NUMBER

        val alertDialog = MaterialAlertDialogBuilder(this)
            .setView(dialogView)
            .show()

        dialogView.cancel_btn.setOnClickListener(View.OnClickListener {

            alertDialog.dismiss()

        })

        dialogView.add_subject_btn.setOnClickListener {
            if(dialogView.subject_input.text.toString().isNotEmpty() && dialogView.course_input.text.toString().isNotEmpty())
            {
                val student = StudentEntity(null, cid,dialogView.subject_input.text.toString(),dialogView.course_input.text.toString())
                viewModel.saveStudentData(student)
                alertDialog.dismiss()
            }
        }
    }


    private fun setToolBar() {
        toolbar_student.title_toolbar.text = className
        toolbar_student.subtitle_toolbar.text = date
        toolbar_student.backbutton.setOnClickListener {
            onBackPressed()
        }
        toolbar_student.saveBtn.setOnClickListener {
            for(student in StudentList)
            {
                var state = student.status
                if(state != "P") state = "A"
                val status = statusEntity(null,cid,student.Sid!!,date,state)
                viewModel.saveStatus(status)
            }
            Toast.makeText(this,"Saved" , Toast.LENGTH_SHORT).show()
        }
        toolbar.inflateMenu(R.menu.menu)
        toolbar.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.calendarView->{
                    dateSetListener =
                        DatePickerDialog.OnDateSetListener { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                            myCalendar.set(Calendar.YEAR, year)
                            myCalendar.set(Calendar.MONTH, month)
                            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                            val myformat = "dd-MMM-yyyy"
                            val sdf = SimpleDateFormat(myformat)
                            date = sdf.format(myCalendar.time)
                            toolbar_student.subtitle_toolbar.text = date.toString()
                            for(student in StudentList){
                                student.status = ""
                            }
                            adapter.differ.submitList(StudentList)
                            adapter.notifyDataSetChanged()

                        }
                    val datePickerDialog = DatePickerDialog(
                        this, dateSetListener, myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)
                    )
                    //datePickerDialog.datePicker.minDate = System.currentTimeMillis()
                    datePickerDialog.show()

                }
                R.id.attendanceSheet->{

                    sidArray = Array<Int>(StudentList.size){0}
                    nameArray = Array<String>(StudentList.size){""}
                    rollnumArray = Array<String>(StudentList.size){""}

                    for(i in 0..StudentList.size-1){
                        sidArray[i] = StudentList.get(i).Sid!!
                    }
                    for(i in 0..StudentList.size-1){
                        nameArray[i] = StudentList.get(i).stname
                    }
                    for(i in 0..StudentList.size-1){
                        rollnumArray[i] = StudentList.get(i).st_roll
                    }


                    /* var bundleInt = Bundle()
                     bundleInt.putIntArray(SIDARRAY,sidArray)
                    */
                    val intent = Intent(this,SheetActivity::class.java)
                    intent.putExtra(CID,cid)
                    intent.putExtra(CLASS_NAME,className)
                    /*  intent.putExtra(SIDARRAY,sidArray)
                      intent.putExtra(ROLLARRAY,rollnumArray)
                      intent.putExtra(NAMEARRAY,nameArray)*/
                    startActivity(intent)
                }
            }
            true
        }
    }
}