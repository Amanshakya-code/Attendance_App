package com.example.attendenceapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.attendenceapp.Constant.constant.Companion.CID
import com.example.attendenceapp.DataBase.DatabaseInstance
import com.example.attendenceapp.Repository.repository
import com.example.attendenceapp.ViewModel.attendenceViewmodel
import com.example.attendenceapp.ViewModel.viewModelFactory
import kotlinx.android.synthetic.main.activity_sheet.*
import kotlinx.android.synthetic.main.toolbar.view.*

class SheetActivity : AppCompatActivity() {
    private lateinit var listItems:ArrayList<String>
    lateinit var adapter:ArrayAdapter<*>
    lateinit var viewModel : attendenceViewmodel
    private var cid:Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sheet)
        setUpToobar()
        val repository = repository(DatabaseInstance.getDatabaseInstance(this))
        val viewModelFactory = viewModelFactory(application,respository = repository)
        viewModel = ViewModelProvider(this,viewModelFactory).get(attendenceViewmodel::class.java)

        cid = intent.getIntExtra(CID,-1)
        listItems = arrayListOf()
        LoadListItems()


    }

    private fun setUpToobar() {
        sheet_Toolbar.loadStatus.visibility = View.GONE
        sheet_Toolbar.saveBtn.visibility = View.GONE
        sheet_Toolbar.title_toolbar.text = "Attendance Sheet"
        sheet_Toolbar.subtitle_toolbar.visibility = View.GONE
        sheet_Toolbar.backbutton.setOnClickListener {
            onBackPressed()
        }
    }

    private fun LoadListItems(){
        viewModel.getALlDistinctMonthData(cid).observe(this, Observer {
            listItems.clear()
            if(it!=null)
            {
                for(date in it){
                    var month = date.substring(3)
                    listItems.add(month)
                }
                Log.i("dismon","$listItems")
                adapter = ArrayAdapter(this,R.layout.sheet_list,R.id.date_list_item,listItems)
                sheetList.adapter = adapter
            }
        })
    }
}