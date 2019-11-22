package org.ieselcaminas.victor.minesweeper2019

import android.content.Context
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getDrawable

const val SIZE = 100

class MineButton(context: Context, var row: Int, var col: Int) : ImageButton(context) {

    var state: StateType = StateType.CLOSED
    var imageView: ImageView? = null

    init {

        layoutParams = LinearLayout.LayoutParams(SIZE, SIZE)

        setPadding(0, 0, 0, 0)
        scaleType = ImageView.ScaleType.CENTER
        adjustViewBounds = true

        setBackground(getDrawable(context, R.drawable.boton))
        setOnTouchListener() { view: View, event: MotionEvent ->

            val button: MineButton = view as MineButton
            if (event.action == MotionEvent.ACTION_DOWN) {
                button.background = getDrawable(context, R.drawable.boton_pressed)
            } else {
                if (event.action == MotionEvent.ACTION_UP ||
                    event.action == MotionEvent.ACTION_CANCEL
                ) {
                    button.background = getDrawable(context, R.drawable.boton)
                }
            }
            false
        }

        setOnLongClickListener {
            when (state) {
                StateType.CLOSED -> {
                    state = StateType.FLAG
                    setImageDrawable(getDrawable(context, R.drawable.flag))
                }
                StateType.FLAG -> {
                    state = StateType.QUESTION
                    setImageDrawable(getDrawable(context, R.drawable.question))
                }
                StateType.QUESTION -> {
                    state = StateType.CLOSED
                    setImageDrawable(null)
                }
            }
            true


        }


    }


}