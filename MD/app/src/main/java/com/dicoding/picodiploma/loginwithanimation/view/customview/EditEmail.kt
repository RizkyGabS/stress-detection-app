package com.dicoding.picodiploma.loginwithanimation.view.customview

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText

class EditEmail : AppCompatEditText {

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

    private fun init() {

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val email = s.toString()
                if (!email.contains("@")) {
                    setError("Email harus mengandung karakter @",null)
                } else {
                    error = null
                }

            }
            override fun afterTextChanged(s: Editable) {
                val email = s.toString()
                if (!email.contains("@")) {
                    setError("Email harus mengandung karakter @",null)
                } else {
                    error = null
                }
            }
        })
    }
}