package com.jetpack.whatsappclone.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jetpack.whatsappclone.model.SampleData
import com.jetpack.whatsappclone.repository.WhatsAppRepository
import com.jetpack.whatsappclone.utils.WhatsAppResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
class WhatsAppChatViewModel(
    private val whatsAppRepository: WhatsAppRepository
): ViewModel() {
    private val date = SimpleDateFormat("hh:mm a")
    private val strDate: String = date.format(Date())
    private val _getSampleData = MutableLiveData<List<SampleData>>()
    val getSampleData: LiveData<List<SampleData>> get() = _getSampleData
    private val _flag = MutableLiveData(false)
    val flag: LiveData<Boolean> get() = _flag

    //firestore val
    val whatsAppStateFlow = MutableStateFlow<WhatsAppResponse?>(null)

    //Static value
    private val chatListItem = mutableListOf(
        SampleData("Name 1", "Hi, Welcome", "Sample Url", strDate),
        SampleData("Name 2", "Hi, Welcome", "Sample Url", strDate),
        SampleData("Name 3", "Hi, Welcome", "Sample Url", strDate),
        SampleData("Name 4", "Hi, Welcome", "Sample Url", strDate),
        SampleData("Name 5", "Hi, Welcome", "Sample Url", strDate)
    )

    init {
        viewModelScope.launch {
            whatsAppRepository.getChatList().collect {
                whatsAppStateFlow.value = it
            }
        }
        //_getSampleData.value = chatListItem
    }

    fun addChat(data: SampleData) {
        _flag.value = _flag.value != true
        chatListItem.addAll(listOf(data))
        _getSampleData.value = chatListItem
    }
}










