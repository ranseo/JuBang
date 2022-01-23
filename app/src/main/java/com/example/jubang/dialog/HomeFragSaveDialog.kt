package com.example.jubang.dialog

import android.app.Dialog
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.NumberPicker
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import com.example.jubang.R
import com.example.jubang.database.SaveLiquorDatabase
import com.example.jubang.database.SaveLiquorEntity
import kotlinx.android.synthetic.main.dialog_fragment_home_save.*
import kotlinx.android.synthetic.main.dialog_fragment_save.*
import kotlinx.android.synthetic.main.fragment_count.view.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class HomeFragSaveDialog(val context: Context) {
    private val dlg = Dialog(context)


    private lateinit var checkBtn: Button
    private lateinit var cancelBtn: Button
    private lateinit var et_storeName: EditText
    private lateinit var npYear: NumberPicker
    private lateinit var npMonth: NumberPicker
    private lateinit var npDay: NumberPicker
    private lateinit var listener : MyDialogOKClickedListener

    var year: Int = 0
    var month: Int = 0
    var day: Int = 0
    var weekDay: String=  ""

    var date = ""
    var storeName = ""


    var yearList = (1950..2050).toList()
    var monthList = (1..12).toList()
    var dateList = (1..31).toList()



    fun start() {

        //타이틀바 제거
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE)

        //다이얼로그에 사용할 xml파일을 불러온다
        dlg.setContentView(R.layout.dialog_fragment_home_save)

        checkBtn = dlg.btn_dialog_HomeSaveCheck
        cancelBtn = dlg.btn_dialog_HomeSaveCancel
        et_storeName = dlg.et_dialog_homeSave_storeName
        npYear = dlg.dialog_homeSave_npYear
        npMonth = dlg.dialog_homeSave_npMonth
        npDay = dlg.dialog_homeSave_npDay

        initNumberPicker()

        et_storeName.doAfterTextChanged {
            storeName = et_storeName.text.toString()
        }


        npYear.setOnValueChangedListener { picker, oldVal, newVal ->
                year = newVal
        }

        npMonth.setOnValueChangedListener { picker, oldVal, newVal ->
                month = newVal
        }

        npDay.setOnValueChangedListener { picker, oldVal, newVal ->
                day = newVal
        }

        checkBtn.setOnClickListener {
            date = "${year+1950}년 ${month+1}월 ${day+1}일 ${weekDay}"
            val entity = SaveLiquorEntity(null, storeName, date)
            Log.e("HomeDialog" , "$date + $storeName")
            listener.onOKClicked(entity)
            Toast.makeText(context, "$date\n$storeName 이 저장되었습니다.", Toast.LENGTH_LONG).show()

            dlg.dismiss()
        }

        cancelBtn.setOnClickListener {
            dlg.dismiss()
        }


        dlg.show()
    }

    fun setOnOKClickedListener(listener: (SaveLiquorEntity) -> Unit) {
        this.listener = object: MyDialogOKClickedListener {
            override fun onOKClicked(content: SaveLiquorEntity) {
                listener(content)
            }
        }
    }

    interface MyDialogOKClickedListener {
        fun onOKClicked(content: SaveLiquorEntity)
    }


    private fun initNumberPicker() {
        var now = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            LocalDateTime.now()
        } else {
            null
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            year = now?.year!! - yearList.minOrNull()!!
            month = now?.monthValue!! - monthList.minOrNull()!!
            day = now?.dayOfMonth!! - dateList.minOrNull()!!
            weekDay = now?.format(DateTimeFormatter.ofPattern("E요일"))
        } else {
            null
        }

        var yearStrConvertList = yearList.map { it.toString() }
        var monthStrConvertList = monthList.map { it.toString() }
        var dateStrConvertList = dateList.map { it.toString() }

        val _npYear = dlg.dialog_homeSave_npYear
        val _npMonth = dlg.dialog_homeSave_npMonth
        val _npDay = dlg.dialog_homeSave_npDay



        _npYear.run {
            displayedValues = yearStrConvertList.toTypedArray()
            minValue = 0
            maxValue = yearStrConvertList.size - 1
            descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
            wrapSelectorWheel = false
            value = year
        }

        _npMonth.run {
            minValue = 0
            maxValue = monthStrConvertList.size - 1
            wrapSelectorWheel = false
            displayedValues = monthStrConvertList.toTypedArray()
            npMonth.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
            value = month
        }

        _npDay.run {
            minValue = 0
            maxValue = dateStrConvertList.size - 1
            wrapSelectorWheel = false
            displayedValues = dateStrConvertList.toTypedArray()
            npDay.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
            value = day
        }
    }

}