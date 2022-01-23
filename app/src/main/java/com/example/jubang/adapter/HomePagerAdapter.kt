package com.example.jubang.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class HomePagerAdapter(fragment : Fragment) :
    FragmentStateAdapter(fragment), AddDeleteFragment {
    private var fragments: ArrayList<Fragment> = ArrayList()

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }

    override fun addFragment(fragment: Fragment) {
        fragments.add(fragment)
        notifyItemInserted(fragments.size - 1)
    }

    override fun removeFragment() {
        fragments.removeLast()
        notifyItemRemoved(fragments.size)
    }
}