package com.example.attendenceapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.attendenceapp.Constant.constant
import com.example.attendenceapp.Constant.constant.Companion.CID
import com.example.attendenceapp.Constant.constant.Companion.CLASS_NAME
import com.example.attendenceapp.Constant.constant.Companion.MONTH
import com.example.attendenceapp.Constant.constant.Companion.NAMEARRAY
import com.example.attendenceapp.Constant.constant.Companion.ROLLARRAY
import com.example.attendenceapp.Constant.constant.Companion.SIDARRAY
import com.example.attendenceapp.Constant.constant.Companion.nameArray
import com.example.attendenceapp.Constant.constant.Companion.rollnumArray
import com.example.attendenceapp.Constant.constant.Companion.sidArray
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
    private var className:String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sheet)
        setUpToobar()



        val repository = repository(DatabaseInstance.getDatabaseInstance(this))
        val viewModelFactory = viewModelFactory(application,respository = repository)
        viewModel = ViewModelProvider(this,viewModelFactory).get(attendenceViewmodel::class.java)

        cid = intent.getIntExtra(CID,-1)
        className = intent.getStringExtra(CLASS_NAME).toString()

      /*  val sidArray = intent.getIntArrayExtra(SIDARRAY)
        val nameArray = intent.getStringArrayExtra(NAMEARRAY)
        val rollnumArray = intent.getStringArrayExtra(ROLLARRAY)*/

        for(i in 0..sidArray.size-1){
            Log.i("ddj","${sidArray[i]}--->${nameArray[i]}---->${rollnumArray[i]}")
        }
        listItems = arrayListOf()
        LoadListItems()

        sheetList.setOnItemClickListener { parent, view, position, id ->
            openSheetActivity(position)
        }



    }

    private fun openSheetActivity(position:Int) {
        val intent = Intent(this,SheetPdfActivity::class.java)
        intent.putExtra(MONTH,listItems.get(position).toString())
        intent.putExtra(CLASS_NAME,className)
        startActivity(intent)
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