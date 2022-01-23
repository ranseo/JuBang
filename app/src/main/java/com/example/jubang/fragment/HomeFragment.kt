package com.example.jubang.fragment

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.viewpager2.widget.ViewPager2
import com.example.jubang.R
import com.example.jubang.adapter.HomePagerAdapter
import com.example.jubang.database.FinalList
import com.example.jubang.database.SaveLiquorDatabase
import com.example.jubang.database.SaveLiquorDetail
import com.example.jubang.database.SaveLiquorEntity
import com.example.jubang.dialog.HomeFragSaveDialog
import com.example.jubang.dialog.LiquorNameAndColor
import com.example.jubang.dialog.OffDialog
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Collections.max


@Suppress("DEPRECATION")
class HomeFragment(
    val fragmentActivity: FragmentActivity,
    private val db: SaveLiquorDatabase,
    val offDialog: OffDialog,

) : Fragment(),View.OnClickListener {
// NavigationView.OnNavigationItemSelectedListener,


    private var tmpTitleId : Int = 0
    private lateinit var callback: OnBackPressedCallback

    private lateinit var broadcastManager: LocalBroadcastManager
    private lateinit var plusFragmentReceiver: BroadcastReceiver

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_home, container, false)


        //네비게이션 바, 왼쪽에서 스타트
        view.iv_menuBtn.setOnClickListener {
            view.homeFragment_layoutDrawer.openDrawer(GravityCompat.START)
        }

        view.menu_profile.setOnClickListener {
            Log.d("네비 :", "----프로필")
            Toast.makeText(requireContext(), "프로필", Toast.LENGTH_LONG).show()
            homeFragment_layoutDrawer.closeDrawers()
        }

        view.menu_save.setOnClickListener {
            val dlg = HomeFragSaveDialog(requireContext())
            dlg.setOnOKClickedListener { content ->
                insert(content)
                sendIntentLong("save_event_action", "save_event_key", 0)
                homeFragment_layoutDrawer.closeDrawers()

            }
            dlg.start()
        }

        view.homeFrag_save.setOnClickListener {
            val dlg = HomeFragSaveDialog(requireContext())
            dlg.setOnOKClickedListener { content ->
                insert(content)
                sendIntentLong("save_event_action", "save_event_key", 0)
                homeFragment_layoutDrawer.closeDrawers()
            }
            dlg.start()
        }

        //view.homeFragment_naviMenu.setNavigationItemSelectedListener(this)

        db.saveLiquorDAO().getLiquorTitleId().observe(viewLifecycleOwner, Observer { titleIdList->

            val titleId = titleIdList.max()
            titleId?.let{getAllTable(titleId!!)}

        })

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (homeFragment_layoutDrawer.isDrawerOpen(GravityCompat.START)) {
                    homeFragment_layoutDrawer.closeDrawers()
                } else {
                    offDialog.offAlert()
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val soju = CountFragment("소주", R.color.soju, db)
        val beer = CountFragment("맥주",R.color.beer, db)
        val plus = PlusFragment()

        val hpAdapter = HomePagerAdapter(this)

        homeFragment_viewPager.adapter = hpAdapter


        homeFragment_viewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                homeFragment_indicator.selectDot(position)
                super.onPageSelected(position)
                Log.e("ViewPagerFragment", " Pag ${position + 1}")
            }
        })
        hpAdapter.addFragment(soju)
        hpAdapter.addFragment(beer)
        hpAdapter.addFragment(plus)


        homeFragment_indicator.createDotPanel(
            hpAdapter.itemCount,
            R.drawable.main_indicator_off,
            R.drawable.main_indicator_on,
            0
        )

        broadcastManager = LocalBroadcastManager.getInstance(view.context)
        plusFragmentReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val liquorNameAndColor = intent?.getParcelableExtra<LiquorNameAndColor>("Result")!!
                val liquorName = liquorNameAndColor.liquorName ?: "주류"
                val color = liquorNameAndColor.color ?: R.color.black
                val newFragment = CountFragment(liquorName, color, db)
                hpAdapter.addFragment(newFragment)

//                homeFragment_indicator.createDotPanel(
//                        hpAdapter.itemCount,
//                        R.drawable.main_indicator_off,
//                        R.drawable.main_indicator_on,
//                        hpAdapter.itemCount
//                )

            }

        }
        val plusFragmentFilter = IntentFilter().apply {
            addAction("PlusFragment_action")
        }

        broadcastManager.registerReceiver(plusFragmentReceiver, plusFragmentFilter)


        //알람 토글 설정
//        tb_alarm.setOnCheckedChangeListener { _, isChecked ->
//            if (!isChecked) {
//                sendIntentInt("key_event_alarm_action", "key_event_alarm", 1)
//            } else {
//                sendIntentInt("key_event_alarm_action", "key_event_alarm", 0)
//            }
//        }

        menu_alarm.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked) {
                sendIntentInt("key_event_alarm_action", "key_event_alarm", 1)
            } else {
                sendIntentInt("key_event_alarm_action", "key_event_alarm", 0)
            }
        }


    }

//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//        activity?.onBackPressedDispatcher?.addCallback()
//    }




//    override fun onNavigationItemSelected(item: MenuItem): Boolean {
//        when (item.itemId) {
//            R.id.menu_profile -> {
//                Log.d("네비 :", "----프로필")
//                Toast.makeText(requireContext(), "프로필", Toast.LENGTH_LONG).show()
//                homeFragment_layoutDrawer.closeDrawers()
//            }
//            R.id.menu_save -> {
//                val dlg = HomeFragSaveDialog(requireContext())
//                dlg.setOnOKClickedListener { content ->
//                    insert(content)
//                    sendIntentLong("save_event_action", "save_event_key", 0)
//                    homeFragment_layoutDrawer.closeDrawers()
//                }
//                dlg.start()
//            }
//        }
//        return false
//    }


    fun insert(saveLiquor: SaveLiquorEntity) {

        lifecycleScope.launch(Dispatchers.IO) {
            db.saveLiquorDAO().insert(saveLiquor)
        }
    }

    override fun onDetach() {
        super.onDetach()
        callback.remove()
    }

    fun sendIntentInt(action: String, key: String, value: Int) {
        val intent = Intent(action).apply {
            putExtra(key, value)
        }
        LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)
    }

    fun sendIntentLong(action: String, key: String, value: Int?) {
        val intent = Intent(action).apply {
            putExtra(key, value)
        }
        LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)
    }

    fun getAllTable(titleId: Int) {
        if(tmpTitleId == titleId) return

        tmpTitleId = titleId

        lifecycleScope.launch(Dispatchers.IO){
            Log.e("getAllTable : " , "$titleId")
            val titleTable = db.saveLiquorDAO().getTitleTable(titleId)
            val liquorTableList = db.saveLiquorDAO().getLiquorTableList(titleId)
            val finalList = FinalList(titleTable,
                liquorTableList
            )
            db.saveLiquorDAO().insert(finalList)

        }

    }

    override fun onClick(v: View?) {

    }
}