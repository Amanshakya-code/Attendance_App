package com.example.attendenceapp

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.attendenceapp.Constant.constant.Companion.CLASS_NAME
import com.example.attendenceapp.Constant.constant.Companion.MONTH
import com.example.attendenceapp.Constant.constant.Companion.nameArray
import com.example.attendenceapp.Constant.constant.Companion.rollnumArray
import com.example.attendenceapp.Constant.constant.Companion.sidArray
import com.example.attendenceapp.DataBase.DatabaseInstance
import com.example.attendenceapp.Repository.repository
import com.example.attendenceapp.ViewModel.attendenceViewmodel
import com.example.attendenceapp.ViewModel.viewModelFactory
import com.itextpdf.kernel.geom.PageSize
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Cell
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import com.itextpdf.layout.property.HorizontalAlignment
import com.itextpdf.layout.property.TextAlignment
import kotlinx.android.synthetic.main.activity_sheet_pdf.*
import kotlinx.android.synthetic.main.activity_student.*
import kotlinx.android.synthetic.main.toolbar.view.*
import java.io.File
import java.io.FileOutputStream
import java.util.*

class SheetPdfActivity : AppCompatActivity() {

    lateinit var viewModel : attendenceViewmodel
    var month = ""
    var classname = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sheet_pdf)
        val repository = repository(DatabaseInstance.getDatabaseInstance(this))
        val viewModelFactory = viewModelFactory(application,respository = repository)
        viewModel = ViewModelProvider(this,viewModelFactory).get(attendenceViewmodel::class.java)
        month = intent.getStringExtra(MONTH).toString()
        classname = intent.getStringExtra(CLASS_NAME).toString()
        for(i in 0..sidArray!!.size-1){
            Log.i("ddj","${sidArray[i]}--->${nameArray!![i]}---->${rollnumArray!![i]}--->$month")
        }
        setuptoolbat()
        showTable()
    }

    private fun setuptoolbat() {
        pdf_toolbar.loadStatus.visibility = View.GONE
        pdf_toolbar.subtitle_toolbar.text = month
        pdf_toolbar.title_toolbar.text = classname

        pdf_toolbar.backbutton.setOnClickListener {
            onBackPressed()
        }
    }

    private fun showTable() {
        var dayInMonth = getDayInMonth(month!!)
        Log.i("simple","$dayInMonth")


        // row of the table
        var tableRow = Array<TableRow>(sidArray!!.size+1){TableRow(this)}
        var rollTextViews = Array<TextView>(sidArray.size+1){TextView(this)}
        var nameTextView = Array<TextView>(sidArray.size+1){TextView(this)}
        var statusTextViews = Array<TextView>(sidArray.size+1){TextView(this)}
       // var statusView = Array<TextView>(dayInMonth){Array<TextView>(sidArray.size){ TextView(this) } }
        val statusView = Array(sidArray.size + 1) {
            arrayOfNulls<TextView>(
                dayInMonth+1
            )
        }
        Log.i("checkindex","${sidArray.size+1}")
        for(i in 0..sidArray.size){
            for(j in 0..dayInMonth-1){
                statusView[i][j] = TextView(this)
            }
        }

        rollTextViews[0].text = "Roll"
        rollTextViews[0].setTypeface(rollTextViews[0].typeface,Typeface.BOLD)
        nameTextView[0].text = "Name"
        nameTextView[0].setTypeface(nameTextView[0].typeface,Typeface.BOLD)
        for(i in 0..dayInMonth-1){
            statusView[0][i]!!.text = (i+1).toString()
            statusView[0][i]!!.setTypeface(statusView[0][i]!!.typeface,Typeface.BOLD)
        }
        for(i in 1..sidArray.size){
            rollTextViews[i].text = rollnumArray?.get(i-1).toString()
            nameTextView[i].text = nameArray!![i-1]
            Log.i("ooo","$dayInMonth")
            for(j in 1..dayInMonth){
                var day = j.toString()
                if(day.length == 1) day = "0"+day
                var date = day+"-"+month
                Log.i("checkingforsid","${sidArray[i-1]} ---> ${date}")
                //viewModel.getStatusForSheet(sidArray[i-1],date)
                viewModel.getAllstatusfirsheet(sidArray[i-1],date).observe(this, {
                    Log.i("getting","$it")
                    if(it!=null){
                        statusView[i][j-1]!!.text = it.status
                        if(it.status == "P"){
                            statusView[i][j-1]!!.setBackgroundColor(Color.parseColor("#A6D66E"))
                        }else if(it.status == "A"){
                            statusView[i][j-1]!!.setBackgroundColor(Color.parseColor("#F6707D"))
                        }
                    }
                })
            }
        }
        for(i in 0..sidArray!!.size){
            if(i%2 == 0){
                tableRow[i].setBackgroundColor(Color.parseColor("#EEEEEE"))
                tableRow[i].setPadding(0,0,10,10)
            }
            else{
                tableRow[i].setBackgroundColor(Color.parseColor("#E4E4E4"))
                tableRow[i].setPadding(0,0,10,10)
            }
            rollTextViews[i].setPadding(16,16,16,16)
            nameTextView[i].setPadding(16,16,16,16)
            tableRow[i].addView(rollTextViews[i])
            tableRow[i].addView(nameTextView[i])
            for(j in 0..dayInMonth-1){
                statusView[i][j]?.setPadding(16,16,16,16)
                tableRow[i].addView(statusView[i][j])
            }
            tableLayout.addView(tableRow[i])
        }
        tableLayout.showDividers = TableLayout.SHOW_DIVIDER_MIDDLE




        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if ((checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
            ) {
                val permissionWrite = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                requestPermissions(
                    permissionWrite,
                    1002
                ) // GIVE AN INTEGER VALUE FOR PERMISSION_CODE_WRITE LIKE 1002
            } else {
                pdf_toolbar.saveBtn.setOnClickListener {
                    val pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    var file = File(pdfPath,"Attendance.pdf")
                    var pdfWriter = PdfWriter(file)
                    var pdfDocument = PdfDocument(pdfWriter)
                    var document = Document(pdfDocument)

                    pdfDocument.defaultPageSize = PageSize.A2.rotate()
                    document.setMargins(12F,12F,12F,12F)
                    var mainTitle = Paragraph("Attendance Sheet").setBold().setFontSize(24F).setTextAlignment(TextAlignment.CENTER)
                    var subtitle = Paragraph(classname).setFontSize(20F).setTextAlignment(TextAlignment.CENTER)

                    var width = FloatArray(dayInMonth+3){50F}
                    var table = Table(width)
                    table.setHorizontalAlignment(HorizontalAlignment.CENTER)


                    for(i in 0..sidArray.size){
                        table.addCell(Cell().add(Paragraph(rollTextViews[i].text.toString())))
                        table.addCell(Cell().add(Paragraph(nameTextView[i].text.toString())))
                        for(j in 0..dayInMonth) {
                            table.addCell(Cell().add(Paragraph(statusView[i][j]?.text.toString())))
                        }
                    }
                    document.add(mainTitle)
                    document.add(subtitle)
                    document.add(table)
                    document.close()
                    Toast.makeText(this,"Download the pdf",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun checkPermissionForFile() {

    }

    private fun downLoadFile() {
        TODO("Not yet implemented")
    }

    private fun getDayInMonth(fulldate: String): Int {
        Log.i("ghg","$fulldate")
        var month = fulldate.substring(0,3)
        Log.i("ghg","$month")
        var monthIndex = 0;
        when(month)
        {
            "Aug" ->{
                monthIndex = 8
            }
            "Sep"->{
                monthIndex = 9
            }
            "Oct"->{
                monthIndex = 10
            }
            "Jul"->{
                monthIndex = 7
            }
            "Jun"->{
                monthIndex = 6
            }
            "May"->{
                monthIndex = 5
            }
            "Apr"->{
                monthIndex = 4
            }
            "Mar"->{
                monthIndex = 3
            }
            "Feb"->{
                monthIndex = 2
            }
            "Jan"->{
                monthIndex = 1
            }
            "Dec"->{
                monthIndex = 12
            }
            "Nov"->{
                monthIndex = 11
            }
        }
        Log.i("ghg","$fulldate")
        var year = (fulldate.substring(4)).toInt()
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.MONTH,monthIndex-1)
        calendar.set(Calendar.YEAR,year)
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
    }
}