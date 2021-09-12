package com.example.mycontacts.util

import androidx.lifecycle.MutableLiveData

fun <T> List<T>.update(index: Int, item: T): List<T> = toMutableList().apply { this[index] = item }

fun <T> MutableLiveData<List<T>>.addAllAndNotify(items: List<T>) {
    value = when {
        value.isNullOrEmpty() -> {
            items
        }
        else -> {
            val updatedItems = value as MutableList
            updatedItems.addAll(items)
            updatedItems
        }
    }
}
