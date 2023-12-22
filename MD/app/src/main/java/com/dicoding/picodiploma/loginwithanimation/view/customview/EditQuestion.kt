package com.dicoding.picodiploma.loginwithanimation.view.customview

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText

class EditQuestion : AppCompatEditText {

    private var isErrorPresent = false

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

            }
            override fun afterTextChanged(s: Editable) {
                if (s.isNotEmpty()) {
                    val input = s.toString().toIntOrNull()
                    if (input == null || input !in 0..4) {
                        // Invalid input, set to the nearest valid value (0 or 4)
                        val correctedInput = when {
                            input != null && input < 0 -> 0
                            input != null && input > 4 -> 4
                            else -> 0 // Default to 0 if input is not an integer
                        }

                        // Update the text with the corrected input
                        setText(correctedInput.toString())

                        // Move the cursor to the end of the text
                        setSelection(text?.length ?: 0)

                        // Set error state
                        setError("Input hanya boleh berupa angka 0-4", null)
                        isErrorPresent = true
                    } else {
                        // Valid input, clear error state
                        error = null
                        isErrorPresent = false
                    }
                }
            }
        })
    }
    fun hasError(): Boolean {
        return isErrorPresent
    }
}
