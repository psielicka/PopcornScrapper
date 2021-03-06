package com.popcornscrapper.popcornscrapper

import android.content.Context
import android.os.Handler
import android.support.design.widget.TextInputLayout
import android.support.v7.widget.AppCompatAutoCompleteTextView
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import com.popcornscrapper.popcornscrapper.model.utils.views.CustomDropDownAdapter

class AutocompleteDropDown : AppCompatAutoCompleteTextView {

    private var dropDownAdapter: CustomDropDownAdapter? = null
    private var textInputLayout: TextInputLayout? = null
    private val dropDownDelay: Long = 800
    private var lastEditText: Long = 0
    private val dropDownHandler = Handler()

    private var textWatcher: TextWatcher? = null

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    fun setupDropDown() {
        setDropDownBackgroundDrawable(ResUtil.getDrawable(R.drawable.rounded_frame_white))
        threshold = 0
        setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                showDropDown()
            }
        }
        setLines(1)
    }

    fun setupDropDownTextWatcher(adapter: CustomDropDownAdapter, textInputLayout: TextInputLayout) {
        dropDownAdapter = adapter
        this.textInputLayout = textInputLayout
        this.textWatcher = getTextWatcher(adapter)
        addTextChangedListener(textWatcher)
    }

    private fun getTextWatcher(adapter: CustomDropDownAdapter): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(
                charSequence: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {
            }

            override fun onTextChanged(
                charSequence: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {
                dropDownHandler.removeCallbacks(handleDropDownSearchRequest(null, null))
            }

            override fun afterTextChanged(editable: Editable) {
                lastEditText = System.currentTimeMillis()
                dropDownHandler.postDelayed(
                    handleDropDownSearchRequest(adapter, editable),
                    dropDownDelay
                )
            }
        }
    }

    // workaround for treshold = 0. Documentation: When threshold is less than or equals 0, a threshold of 1 is applied.
    override fun enoughToFilter(): Boolean{
        return true
    }

    private fun handleDropDownSearchRequest(
        adapter: CustomDropDownAdapter?,
        editable: Editable?
    ): Runnable {
        return Runnable {
            if (System.currentTimeMillis() > lastEditText + dropDownDelay - 500) {
                adapter?.handleDropDownSearchRequest(editable.toString().toLowerCase(), textInputLayout)
            }
        }
    }
}
