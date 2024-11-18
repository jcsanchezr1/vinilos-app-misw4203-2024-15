package com.example.vinilos.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class HeaderViewModel(application: Application) : AndroidViewModel(application) {

    private val _title = MutableLiveData<String>()
    val title: LiveData<String> get() = _title

    private val _addButtonVisibility = MutableLiveData<Int>()
    val addButtonVisibility: LiveData<Int> get() = _addButtonVisibility

    fun setTitleAndAddButtonVisibility(title: String, isAddVisible: Boolean) {
        _title.value = title
        _addButtonVisibility.value = if (isAddVisible) android.view.View.VISIBLE else android.view.View.GONE
    }
}