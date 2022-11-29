package com.karthyks.compose.listmemoryleak

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Composable
fun SmallWidget(
    id: Int,
    smallWidgetViewModel: SmallWidgetViewModel
) {
    DisposableEffect(key1 = id) {
        smallWidgetViewModel.setCurrentID(id)
        onDispose {
            println("ID $id widget is disposed")
        }
    }
    val state = smallWidgetViewModel.state.collectAsState()
    Box(modifier = Modifier.size(30.dp)) {
        WidgetClicker(
            id = state.value,
            onOddClick = smallWidgetViewModel::onOddClick,
            onEvenClick = smallWidgetViewModel::onEvenClick,
        )
    }
}

@Composable
fun WidgetClicker(
    id: Int,
    onOddClick: () -> Unit,
    @Suppress("UNUSED_PARAMETER") onEvenClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colors.primary)
            .clickable { onOddClick() }
            .wrapContentSize(align = Alignment.Center)
    ) {
        Text(text = "$id")
    }
}

class SmallWidgetViewModel : ViewModel() {

    private val _state: MutableStateFlow<Int> = MutableStateFlow(-1)
    val state: StateFlow<Int> by ::_state

    fun setCurrentID(id: Int) {
        _state.value = id
    }

    fun onOddClick() {
        println("Odd clicked ${state.value}")
    }

    fun onEvenClick() {
        println("Even clicked ${state.value}")
    }

    override fun onCleared() {
        super.onCleared()
        println("${state.value} Viewmodel is cleared")
    }
}
