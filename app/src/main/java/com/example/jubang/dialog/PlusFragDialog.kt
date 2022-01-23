package com.example.jubang.dialog

import android.app.Dialog
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.FragmentManager
import com.example.jubang.R
import com.example.jubang.database.SaveLiquorEntity
import com.github.dhaval2404.colorpicker.MaterialColorPickerDialog
import com.github.dhaval2404.colorpicker.listener.ColorListener
import com.github.dhaval2404.colorpicker.model.ColorShape
import kotlinx.android.synthetic.main.dialog_fragment_plus_button.*
import kotlinx.android.synthetic.main.fragment_count.view.*
import kotlin.properties.Delegates

class PlusFragDialog(context:Context, _fragmentManger: FragmentManager) : Dialog(context){
    private val dlg = Dialog(context)
    private val fragmentManger = _fragmentManger
    private lateinit var listener : PlusFragDialog.MyDialogOKClickedListener

    private lateinit var editText: EditText
    private lateinit var textView: TextView
    private lateinit var leaderBoard: LinearLayout

    private lateinit var linearLayout1: LinearLayout
    private lateinit var linearLayout2: LinearLayout

    private lateinit var checkBtn: Button
    private lateinit var cancelBtn: Button


    private var liquorName = "주류"
    private var liquorColor : Int = R.color.black
    private var leaderBoardColor = ""

    fun start() {
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dlg.setContentView(R.layout.dialog_fragment_plus_button)

        editText = dlg.dialog_fragment_plus_button_editText
        textView = dlg.dialog_fragment_plus_button_textview
        leaderBoard = dlg.dialog_fragment_plus_button_leaderboard
        linearLayout1 = dlg.dialog_fragment_plus_button_layout1
        linearLayout2 = dlg.dialog_fragment_plus_button_layout2
        checkBtn = dlg.dialog_fragment_plus_button_checkButton
        cancelBtn = dlg.dialog_fragment_plus_button_cancelButton

        editText.doAfterTextChanged {
            liquorName = editText.text?.toString() ?: "주류"
            textView.text = liquorName
        }

        leaderBoard.setOnClickListener {
            MaterialColorPickerDialog
                .Builder(context)
                .setTitle("테마 색 설정")
                .setColorShape(ColorShape.CIRCLE)
                .setDefaultColor(leaderBoardColor)
                .setColorListener(object : ColorListener {
                    override fun onColorSelected(color: Int, colorHex: String) {
                        leaderBoardColor = colorHex
                        liquorColor = color
                        setLeaderBoardBackground(color)
                    }

                })
                .showBottomSheet(fragmentManger)
        }

        checkBtn.setOnClickListener {
            val liquorNameAndColor = LiquorNameAndColor(liquorName, liquorColor)
            listener.onOKClicked(liquorNameAndColor)
            dlg.dismiss()
        }

        cancelBtn.setOnClickListener {
            dlg.dismiss()
        }


        dlg.show()
    }

    fun setOnOKClickedListener(listener: (LiquorNameAndColor) -> Unit) {
        this.listener = object: MyDialogOKClickedListener {
            override fun onOKClicked(content: LiquorNameAndColor) {
                listener(content)
            }
        }
    }

    interface MyDialogOKClickedListener {
        fun onOKClicked(content: LiquorNameAndColor)
    }

    private fun setLeaderBoardBackground(color: Int) {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            linearLayout1.backgroundTintList = ColorStateList.valueOf(color)
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            linearLayout2.backgroundTintList = ColorStateList.valueOf(color)
        }
    }
}