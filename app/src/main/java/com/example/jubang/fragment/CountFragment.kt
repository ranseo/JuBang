package com.example.jubang.fragment

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.ColorStateList
import android.media.MediaPlayer
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.KeyEvent.KEYCODE_ENTER
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.jubang.R
import com.example.jubang.database.SaveLiquorDatabase
import com.example.jubang.database.SaveLiquorDetail
import com.example.jubang.database.SaveLiquorEntity
import kotlinx.android.synthetic.main.fragment_count.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class CountFragment(var name: String, val color: Int, private var db: SaveLiquorDatabase) :
        Fragment() {


    private lateinit var broadcastManager: LocalBroadcastManager
    private lateinit var volumeDownReceiver: BroadcastReceiver
    private lateinit var alarmToggleReceiver: BroadcastReceiver
    private lateinit var saveLiquorInsert: BroadcastReceiver

    private var warningBuzzer: MediaPlayer? = null

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_count, container, false)


        //볼륨을 내리거나 올릴 때 발생하는 트리거
        //메인 액티비티에서 볼륨키를 조정할 때 키 조작이 해당 프래그먼트에 들어오고, 이를 이용해 코드 조작.


        warningBuzzer = MediaPlayer.create(view.context, R.raw.warning)

//소주인지 맥주인지에 따라 fragment테마가 달라짐
        changeLeaderBoardColor(view, color, name)

//textView를 클릭하면 주량의 수가 바뀜
        tvClickKey(view)

//최대주량에 해당하는 edittext에 관한 키 이벤트 처리.
        edMaxBotKey(view)

//현재 병의 수와 최대 주량이 똑같을 때 alarm울리기 위한 것
//        val alarmTextWatcher = object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//            }
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//            }
//
//            override fun afterTextChanged(s: Editable?) {
//                if (view.et_maxBot.text.toString().isEmpty()) return
//                val maxBot: Int = view.et_maxBot.text.toString().toInt()
//                val currBot: Int = view.tv_currBot.text.toString().toInt()
//
//                if (currBot == maxBot) {
//
//                    if (warningBuzzer == null) {
//                        Log.e("textWatcher: ", "return")
//                        return
//                    }
//                    warningBuzzer?.start()
//                    alarmAlert(view)
//                }
//                Log.e("textWatcher: ", "after")
//            }
//        }


//        view.tv_currBot.addTextChangedListener(alarmTextWatcher)

        return view
    }

    private fun getTitleId(): Int? {
        var titleId: Int? = null
        lifecycleScope.launch(Dispatchers.IO) {
            titleId = db.saveLiquorDAO().getTitleId()

            Log.e(
                    "getTitleId() : ",
                    "db.saveLiquorDAO().getTitleId()  : ${db.saveLiquorDAO().getTitleId()}"
            )
            Log.e("getTitleId() : ", "titleId  : $titleId")
        }
        return titleId
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        broadcastManager = LocalBroadcastManager.getInstance(view.context)
//        volumeDownReceiver = object : BroadcastReceiver() {
//            override fun onReceive(context: Context?, intent: Intent?) {
//                setVolumeOnKeyDown(view, intent, idx)
//            }
//        }
//
//        //알람 기능의 활성화 유무에 따라 발생하는 트리거
//        alarmToggleReceiver = object : BroadcastReceiver() {
//            override fun onReceive(context: Context?, intent: Intent?) {
//                setAlarmOntoggle(view, intent)
//            }
//        }

        saveLiquorInsert = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                //val titleId = intent?.getIntExtra("save_event_key", 0)!!
                lifecycleScope.launch(Dispatchers.IO) {
                    var titleId: Int? = db.saveLiquorDAO().getTitleId()

                    Log.e("saveLiquorInsert ::", "titleId :  $titleId")
                    val cupNum: Int = view.tv_currCup.text.toString().toInt()
                    val bottleNum: Int = view.tv_currBot.text.toString().toInt()

                    var maxBot = 0
                    if (view.et_maxBot.text.toString().isNotEmpty()) {
                        maxBot = view.et_maxBot.text.toString().toInt()
                    }
                    Log.e(
                            "saveLiquorInsert ::",
                            "titleId :  $titleId, cupNum : $cupNum, bottleNum : $bottleNum, maxBot : $maxBot"
                    )
                    val saveLiquorDetail = SaveLiquorDetail(
                            null,
                            name = name,
                            currCup = cupNum,
                            currBot = bottleNum,
                            maxBot = maxBot,
                            titleId = titleId
                    )
                    insert(saveLiquorDetail)
                }

                Log.e("saveLiquorInsert ::", "CountFragment $name")
            }
        }
//        val volumeFilter = IntentFilter().apply {
//            addAction("key_event_action")
//        }
//        val alarmFilter = IntentFilter().apply {
//            addAction("key_event_alarm_action")
//        }
        val saveFilter = IntentFilter().apply {
            addAction("save_event_action")
        }

//        broadcastManager.registerReceiver(volumeDownReceiver, volumeFilter)
//        broadcastManager.registerReceiver(alarmToggleReceiver, alarmFilter)
        broadcastManager.registerReceiver(saveLiquorInsert, saveFilter)

    }

    private fun changeLeaderBoardColor(view: View, color: Int, name: String) {


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            view.currAlcohol.backgroundTintList = ColorStateList.valueOf(color)
            view.maxAlcohol.backgroundTintList = ColorStateList.valueOf(color)
        }

//        view.currAlcohol?.setBackgroundColor(
//                ContextCompat.getColor(
//                        view.context,
//                        color
//                )
//        )
//
//        view.maxAlcohol?.setBackgroundColor(
//                ContextCompat.getColor(
//                        view.context,
//                        color
//                )
//        )
//
//                view.maxAlcohol?.background =
//                    ContextCompat.getDrawable(view.context, R.drawable.border_line_soju)

        view.et_maxBot.hint = name
    }

    private fun minusCup(view: View) {
        var cupNum: Int = view.tv_currCup.text.toString().toInt()
        var bottleNum: Int = view.tv_currBot.text.toString().toInt()
        var flag = false
        when (cupNum) {
            0 -> {
                if (bottleNum > 0) {
                    bottleNum--
                    cupNum = 6
                    flag = true
                }
            }
            else -> {
                cupNum--
            }
        }

        view.tv_currCup.setText(cupNum.toString())
        if (flag) view.tv_currBot.setText(bottleNum.toString())
    }

    private fun minusBot(view: View) {
        var bottleNum: Int = view.tv_currBot.text.toString().toInt()
        if (bottleNum > 0) {
            bottleNum--
        }
        view.tv_currBot.setText(bottleNum.toString())
    }

    private fun plusCup(view: View) {
        var cupNum: Int = view.tv_currCup.text.toString().toInt()
        var bottleNum: Int = view.tv_currBot.text.toString().toInt()
        var flag = false
        when (cupNum) {
            6 -> {
                bottleNum++
                cupNum = 0
                flag = true
            }
            else -> {
                cupNum++
            }
        }
        view.tv_currCup.setText(cupNum.toString())
        if (flag) view.tv_currBot.setText(bottleNum.toString())
    }

    private fun plusBot(view: View) {
        var bottleNum: Int = view.tv_currBot.text.toString().toInt()
        bottleNum++
        view.tv_currBot.setText(bottleNum.toString())
    }

    private fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    private fun edMaxBotKey(view: View) {
        view.et_maxBot.setOnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KEYCODE_ENTER) {
                //Log.d("enter: ", "눌림")
                view.et_maxBot.clearFocus()
                v.hideKeyboard()
                true
            } else {
                false
            }
        }
    }

    private fun tvClickKey(view: View) {
        view.ll_plusCup.setOnClickListener {
            plusCup(view)
        }

        view.ll_minusCup.setOnClickListener {
            minusCup(view)
        }

        view.ll_plusBot.setOnClickListener {
            plusBot(view)
        }

        view.ll_minusBot.setOnClickListener {
            minusBot(view)
        }

    }


//    fun  setVolumeOnKeyDown(view: View, intent: Intent?, idx: Int) {
//        when (idx) {
//            0 -> {
//                when (intent?.getIntExtra(
//                    "key_event_extra_soju",
//                    KeyEvent.KEYCODE_UNKNOWN
//                )) {
//                    KeyEvent.KEYCODE_VOLUME_DOWN -> {
//                        minusCup(view)
//                        Log.e("FragmentOnKeyDown", "soju_down")
//                    }
//                    KeyEvent.KEYCODE_VOLUME_UP -> {
//                        plusCup(view)
//                        Log.e("FragmentOnKeyDown", "soju_up")
//                    }
//                }
//            }
//            1 -> {
//                when (intent?.getIntExtra(
//                    "key_event_extra_beer",
//                    KeyEvent.KEYCODE_UNKNOWN
//                )) {
//                    KeyEvent.KEYCODE_VOLUME_DOWN -> {
//                        minusCup(view)
//                        Log.e("FragmentOnKeyDown", "key_beer_down")
//                    }
//                    KeyEvent.KEYCODE_VOLUME_UP -> {
//                        plusCup(view)
//                        Log.e("FragmentOnKeyDown", "key_beer_up")
//                    }
//                }
//            }
//            else -> {
//            }
//        }
//    }

//    fun setAlarmOntoggle(view: View, intent: Intent?) {
//        when (intent?.getIntExtra("key_event_alarm", 1)) {
//            1 -> {
//                warningBuzzer = MediaPlayer.create(view.context, R.raw.warning)
//                Log.e("alarmToggleReceiver : ", "alarm_on")
//            }
//            0 -> {
//                warningBuzzer = null
//                Log.e("alarmToggleReceiver : ", "alarm_off")
//            }
//        }
//    }

    override fun onDestroy() {
        warningBuzzer?.release()
        super.onDestroy()
    }


    fun alarmAlert(view: View) {
        val offAlertDialog = AlertDialog.Builder(view.context)
                .setTitle("알람을 끄시겠습니까?")
                .setPositiveButton("네") { _, _ -> warningBuzzer?.pause() }
                .setNeutralButton("아니요", null)
                .create()

        offAlertDialog.setCancelable(false)
        offAlertDialog.show()

    }

    fun insert(saveLiquor: SaveLiquorDetail) {

        lifecycleScope.launch(Dispatchers.IO) {
            db.saveLiquorDAO().insert(saveLiquor)
        }
    }
}