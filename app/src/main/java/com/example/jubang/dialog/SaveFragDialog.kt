package com.example.jubang.dialog

import android.app.Dialog
import android.content.Context
import android.view.Window
import android.widget.Toast
import com.example.jubang.R
import com.example.jubang.database.FinalList
import com.example.jubang.fragment.OnDeleteListener
import kotlinx.android.synthetic.main.dialog_fragment_save.*

class SaveFragDialog(context : Context, val saveLiquor : FinalList, val onDeleteListener: OnDeleteListener ) :  Dialog(context) {

    private val dlg = Dialog(context)

    fun start() {
        //타이틀바 제거
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE)

        //다이얼로그에 사용할 xml파일을 불러온다
        dlg.setContentView(R.layout.dialog_fragment_save)


        //다이얼로그의 바깥 화면을 눌렀을 때 다이얼로그가 닫히지 않도록 함.
        //dlg.setCancelable(false)

        //해당 코드는 custom_popup_delete가 null포인트 에러가 난다
        //맞네 fragment에서ㅓ도 view.이렇게 했지 그래서 에러많이낫었짢아 하하하
//        tv =  custom_popup_delete

        dlg.dialog_delete.setOnClickListener {
            onDeleteListener.onDeleteListener(saveLiquor)
            onDeleteListener.onDeleteListener(saveLiquor.title)
            for(i in saveLiquor.liquor)
                onDeleteListener.onDeleteListener(i)
            Toast.makeText(context, "삭제 되었습니다", Toast.LENGTH_SHORT).show()
            dlg.dismiss()
        }

        dlg.show()
    }

    interface MyDialogOKClickedListener {
        fun onOKClicked(contnt : String)
    }
}