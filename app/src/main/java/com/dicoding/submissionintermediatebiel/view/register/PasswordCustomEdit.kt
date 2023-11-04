package com.dicoding.submissionintermediatebiel.view.register

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.dicoding.submissionintermediatebiel.R

class PasswordCustomEdit: AppCompatEditText {
    private val warningIcon =
        ContextCompat.getDrawable(context, R.drawable.baseline_danger_24) as Drawable
    private val correctIcon =
        ContextCompat.getDrawable(context, R.drawable.baseline_check_circle_24) as Drawable

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        hint = "Masukkan password Anda"
        maxLines = 1
        textAlignment = View.TEXT_ALIGNMENT_TEXT_START
    }

    private fun init() {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.toString().length < 8) {
                    setError("Password tidak boleh kurang dari 8 karakter", null)
                    errorCondition()
                } else {
                    error = null
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                correctCondition().apply {
                    // I want to move the Icon a little bit to the left side
                }
            }
        })


    }

    private fun errorCondition() {
        setIconDrawables(endOfTheText = warningIcon)
    }

    private fun correctCondition() {
        setIconDrawables(endOfTheText = correctIcon)
    }

    private fun setIconDrawables(
        startOfTheText: Drawable? = null,
        topOfTheText: Drawable? = null,
        endOfTheText: Drawable? = null,
        bottomOfTheText: Drawable? = null
    ) {
        // Sets the Drawables (if any) to appear to the left of,
        // above, to the right of, and below the text.
        setCompoundDrawablesWithIntrinsicBounds(
            startOfTheText,
            topOfTheText,
            endOfTheText,
            bottomOfTheText
        )
    }
}