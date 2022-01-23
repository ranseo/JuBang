package com.example.jubang

import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.jubang.database.SaveLiquorDatabase
import com.example.jubang.database.SaveLiquorEntity
import com.example.jubang.dialog.OffDialog
import com.example.jubang.fragment.HomeFragment
import com.example.jubang.fragment.SaveFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_count.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(),
    NavigationView.OnNavigationItemSelectedListener,
    BottomNavigationView.OnNavigationItemSelectedListener,
    OffDialog {

    lateinit var db: SaveLiquorDatabase

    private var homeFragment: HomeFragment? = null
    private var saveFragment: SaveFragment? = null
    private var currPos: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lockScreenSet()
        setContentView(R.layout.activity_main)


        db = SaveLiquorDatabase.getInstance(this)!!

        homeFragment = HomeFragment(this, db, this)
        saveFragment = SaveFragment(db, this)


        setMainFragment(0)

        main_bottom_navigation.setOnNavigationItemSelectedListener(this)


        //저장 버튼 누를 시 동작
//        main_tv_save.setOnClickListener {
//            val infoDateAndStore = SaveLiquorEntity(null, store = "유래초밥",date="2021년06월02일")
//            insert(infoDateAndStore)
//        }

    }


    fun setMainFragment(fragIndx: Int) {
        val ft = supportFragmentManager.beginTransaction()
        when (fragIndx) {
            0 -> ft.replace(R.id.main_fragment, homeFragment!!).commit()
            1 -> ft.replace(R.id.main_fragment, saveFragment!!).commit()
        }
    }


    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        val v =
            et_maxBot //지금 kotlin extension 을 사용하구 있기에 이게 되는거지 만약에 view binding 을 사용하고 있으면 이게 될까?
        //그리고 et_maxBot 가 현재 다른 프래ㅔ그먼트를 보여주고 있는 상황에서는 이게 과연 될까?
        //확인결과 다른프래그먼트 상황에서는 v에 null 잡히는 것 같다. 확인 ok

        if (ev!!.action == MotionEvent.ACTION_DOWN) {
            if (v == null) Log.e("TAG :", "v is null}")
            else Log.e("TAG :", "v is ${v.javaClass}")
            if (v is EditText) {
                Log.e("TAG :", "if (v is EditText)")
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains((ev.rawX.toInt()), (ev.rawY.toInt()))) {

                    v.hideKeyboard()
                    v.clearFocus()

                }
            }
            //     v.hideKeyboard() 여기 있으면 에러난다.
        }
        return super.dispatchTouchEvent(ev)
    }

    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            ////////////////////////////////////////
            //하단 네비
            R.id.action_home -> {
                setMainFragment(0)
                return true
            }
            R.id.action_edit -> {
                setMainFragment(1)
                return true
            }
//            R.id.action_statis->{
//                return true
//            }
//            R.id.action_setting ->{
//                return true
//            }
        }
        return false
    }

    override fun onBackPressed() {
        super.onBackPressed()
        //if (!popOffAlert()) return

    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return when (keyCode) {
            KeyEvent.KEYCODE_VOLUME_DOWN, KeyEvent.KEYCODE_VOLUME_UP -> {
                when (currPos) {
                    0 -> {
                        val intent =
                            Intent("key_event_action").apply {
                                putExtra("key_event_extra_soju", keyCode)
                            }
                        Log.e("onKeyDown", " Pag ${currPos}")
                        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
                    }
                    1 -> {
                        val intent =
                            Intent("key_event_action").apply {
                                putExtra("key_event_extra_beer", keyCode)
                            }
                        Log.e("onKeyDown", " Pag ${currPos}")
                        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
                    }
                    else -> {
                    }
                }
                true
            }
            else -> super.onKeyDown(keyCode, event)
        }
    }

    fun lockScreenSet() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O_MR1) {

            //잠금 화면 상단에 액티비티를 표시할지 결정하는 함수?
            setShowWhenLocked(true)

            //활동을 재개할 때 화면을 켜야 하는 지 여부를 지정.
            //원래는 액티비티가 꺼져있으면 액티비티는 정지상태로 전환
            //근데 얘가 TRUE면은 액티비티가 켜져있기 때문에, 액티비티가 표시되는 경우 액티비티가 켜지고 스크린이 켜져 다시 시작.
            setTurnScreenOn(true)

            val keyGuardManager = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
            keyGuardManager.requestDismissKeyguard(this, null)
        } else {
            this.window.addFlags(
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD or
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
            )
        }

    }

    fun popOffAlert(): Boolean {
        var flag: Boolean = false
        val offAlertDialog = AlertDialog.Builder(this)
            .setTitle("종료하시겠습니까?")
            .setPositiveButton("종료") { _, _ ->
                moveTaskToBack(true)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    finishAndRemoveTask()
                } else finish()
                android.os.Process.killProcess(android.os.Process.myPid())
            }
            .setNeutralButton("취소", null)
            .create()


        offAlertDialog.setCancelable(false)
        offAlertDialog.show()

        return flag
    }


    fun insert(saveLiquor: SaveLiquorEntity) {
        lifecycleScope.launch(Dispatchers.IO) {
            db.saveLiquorDAO().insert(saveLiquor)
        }
    }

    override fun offAlert() {
        popOffAlert()
    }

}