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
import java.util.regex.Pattern

class EmailCustomEdit : AppCompatEditText {
    private val emailCheck = Pattern.compile("[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}")
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
        maxLines = 1
        hint = "Masukkan email Anda"
        textAlignment = View.TEXT_ALIGNMENT_TEXT_START
    }

    private fun init() {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (emailCheck.matcher(p0.toString()).matches()) {
                    error = null
                    errorCondition() // I know it's not logically correct but it works this way anyhow lol and somehow the icon is different as well
                } else {
                    error = "Email tidak Valid!"
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                correctCondition()
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