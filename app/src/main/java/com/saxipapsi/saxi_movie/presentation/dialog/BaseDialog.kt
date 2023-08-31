package com.saxipapsi.saxi_movie.presentation.dialog

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

open class BaseDialog : DialogFragment() {

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setStyle(STYLE_NO_TITLE, R.style.TransparentDialog)
//    }

    override fun show(manager: FragmentManager, tag: String?) {
        val ft: FragmentTransaction = manager.beginTransaction()
        val prevFreeTrialEndSoonDialog: Fragment? = manager.findFragmentByTag(tag)
        prevFreeTrialEndSoonDialog?.let { ft.remove(it) }
        ft.addToBackStack(null)
        super.show(manager, tag)
    }
}