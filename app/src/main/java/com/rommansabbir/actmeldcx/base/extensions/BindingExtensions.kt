package com.rommansabbir.actmeldcx.base.extensions

import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.rommansabbir.actmeldcx.R
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("setImagePath")
fun ImageView.setImagePath(path: String) {
    Glide.with(this.context).load(File(path)).into(this)
}

@BindingAdapter("setDateTime")
fun TextView.setDateTime(dateTimeInMillis: String) {
    try {
        val formatter = SimpleDateFormat("EEE, d MMM yyyy hh:mm aaa", Locale.getDefault())
        this.text = formatter.format(Date(dateTimeInMillis.toLong())).toString()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun SearchView.customize() {
    val editText =
        this.findViewById<EditText>(androidx.appcompat.R.id.search_src_text)
    editText.setHintTextColor(
        ContextCompat.getColor(
            context,
            R.color.black
        )
    )
    editText.setTextColor(
        ContextCompat.getColor(
            context,
            R.color.black
        )
    )
    editText.textSize = 14f
}

/**
 * Add an action which will be invoked when the text is changing.
 *
 * @return the [SearchView.OnQueryTextListener] added to the [SearchView]
 */
inline fun SearchView.doAfterTextChanged(
    delay: Long = 500,
    crossinline onTextChangedDelayed: (text: String) -> Unit
) = doOnQueryTextListener(delay, onTextChangedDelayed)


/**
 * Add an action which will be invoked after the text changed.
 *
 * @return the [SearchView.OnQueryTextListener] added to the [SearchView]
 */
inline fun SearchView.doOnQueryTextListener(
    delay: Long,
    crossinline onTextChangedDelayed: (text: String) -> Unit
): SearchView.OnQueryTextListener {
    val queryListener = object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean = true
        override fun onQueryTextChange(newText: String?): Boolean {
            handlerPostDelayed(delay) { onTextChangedDelayed.invoke(newText ?: "") }
            return true
        }
    }
    this.setOnQueryTextListener(queryListener)
    return queryListener
}
