package com.example.mycontacts.util

import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.mycontacts.R

fun RecyclerView.addLoadMoreListener(block: () -> Unit) {
    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager?
            if (layoutManager?.findLastCompletelyVisibleItemPosition() ==
                recyclerView.adapter?.itemCount!! - 1) {
                block()
            }
        }
    })
}

fun ImageView.loadCircleShape(url: String) {
    Glide.with(context)
        .load(url)
        .placeholder(R.drawable.ic_no_image)
        .apply(RequestOptions.circleCropTransform())
        .into(this)
}

fun Fragment.toast(message: CharSequence) = context?.let {
    Toast.makeText(it, message, Toast.LENGTH_SHORT).show()
}
