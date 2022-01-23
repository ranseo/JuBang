package com.example.jubang.dialog

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LiquorNameAndColor(val liquorName: String?, val color: Int?) : Parcelable
