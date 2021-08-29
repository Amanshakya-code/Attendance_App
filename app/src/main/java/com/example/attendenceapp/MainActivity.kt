package com.example.attendenceapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.attendenceapp.Adapter.classAdapter
import com.example.attendenceapp.DataBase.DatabaseInstance
import com.example.attendenceapp.Model.ClassItem
import com.example.attendenceapp.Repository.repository
import com.example.attendenceapp.ViewModel.attendenceViewmodel
import com.example.attendenceapp.ViewModel.viewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog.view.*
import kotlinx.android.synthetic.main.toolbar.view.*

class MainActivity : AppCompatActivity() {

    private lateinit var classList:ArrayList<ClassItem>
    private  lateinit var adapter:classAdapter
    lateinit var viewModel : attendenceViewmodel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val repository = repository(DatabaseInstance.getDatabaseInstance(this))
        val viewModelFactory = viewModelFactory(application,respository = repository)
        viewModel = ViewModelProvider(this,viewModelFactory).get(attendenceViewmodel::class.java)


        AddSubjectFb.setOnClickListener {
            showDailog()
        }
        classrecyclerView.layoutManager = LinearLayoutManager(this)
        classList = arrayListOf()
        adapter = classAdapter()
        classrecyclerView.adapter = adapter
        setToolBar()

        viewModel.getClassData().observe(this, Observer {
            adapter.differ.submitList(it)
            adapter.notifyDataSetChanged()
        })

    }
    private fun setToolBar() {
        toolbar_main.title_toolbar.text = "Attendance App"
        toolbar_main.subtitle_toolbar.visibility = View.GONE
        toolbar_main.backbutton.visibility = View.GONE
        toolbar_main.saveBtn.visibility = View.GONE
    }

    private fun showDailog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog,null)
        val alertDialog = MaterialAlertDialogBuilder(this, R.style.CustomMaterialDialog)
            .setView(dialogView)
            .show()

        dialogView.cancel.setOnClickListener {
            alertDialog.dismiss()
        }
        dialogView.addsubjectbtn.setOnClickListener {
            if(dialogView.et01.text.toString().isNotEmpty() && dialogView.etd02.text.toString().isNotEmpty())
            {
                val classitem = ClassItem(null,dialogView.et01.text.toString(),dialogView.etd02.text.toString())
                viewModel.saveClassData(classitem)
                alertDialog.dismiss()
            }
        }
    }
}