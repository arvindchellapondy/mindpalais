package com.mindpalais.android.model

import android.app.Application
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class UiViewModel(application: Application) : AndroidViewModel(application) {

    val bottomSheetContent = mutableStateOf<@Composable () -> Unit>({})

    private val _expandBottomSheet = MutableStateFlow(false)
    val expandBottomSheet: StateFlow<Boolean> = _expandBottomSheet

    private val _isBottomSheetSwipeable = MutableStateFlow(true)
    val isBottomSheetSwipeable: StateFlow<Boolean> get() = _isBottomSheetSwipeable

    fun setBottomSheetContent(content: @Composable () -> Unit) {
        Log.e("UiViewModel", "setBottomSheetContent")
        bottomSheetContent.value = content
    }

    fun setExpandBottomSheet(expand: Boolean) {
        Log.e("UiViewModel", "setExpandBottomSheet ::"+expand)
        _expandBottomSheet.value = expand
        if(!expand){
            bottomSheetContent.value = {}
        }
    }

    fun setSwipeable(swipeable: Boolean) {
        _isBottomSheetSwipeable.value = swipeable
    }
}
