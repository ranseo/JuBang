package com.example.jubang.indicator

import android.content.Context
import android.media.Image
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.ImageView
import android.widget.LinearLayout

class MainCircleIndicator : LinearLayout {

    private var _context : Context ?= null

    //인디케이터 기본 모습
    private var _defaultCircle: Int = 0

    //인디케이터 선택 됐을 때 모습
    private var _selectCircle: Int = 0


    private var imageDot: MutableList<ImageView> = mutableListOf()
    //?
    private var temp = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, 4.5f, resources.displayMetrics)

    constructor(context: Context): super(context) {
        _context = context
    }

    constructor(context: Context, attrs: AttributeSet): super(context,attrs) {
        _context = context
    }


    /**
     * 기본 점 샘성
     * @param count 점의 갯수
     * @param defaultCircle 기본 점의 이미지
     * @param selectCircle 선택된 점의 이미지
     * @param position 선택된 점의 포지션
     */

    fun createDotPanel(count:Int, defaultCircle: Int, selectCircle:Int, position:Int) {
        this.removeAllViews()

        _defaultCircle = defaultCircle
        _selectCircle = selectCircle

        for(i in 0 until count) {
            imageDot.add(ImageView(_context).apply{setPadding(temp.toInt(), 0, temp.toInt(),0)})

            this.addView(imageDot[i])
        }

        selectDot(position)
    }

    fun selectDot(position: Int) {

        for(i in imageDot.indices) {

            if(i == position) {
                imageDot[i].setImageResource(_selectCircle)
            } else {
                imageDot[i].setImageResource(_defaultCircle)
            }
        }
    }

}