package com.karthyks.compose.listmemoryleak

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Composable
fun CardComponent(
    id: Int,
    cardViewModel: CardViewModel = viewModel(key = "CardViewModel$id")
) {
    LaunchedEffect(key1 = cardViewModel) {
        cardViewModel.setCurrentId(id)
    }
    Box(
        modifier = Modifier
            .width(200.dp)
            .height(140.dp)
            .background(color = MaterialTheme.colors.primaryVariant)
    ) {
        val currentId by cardViewModel.state.collectAsState()
        SmallWidget(
            currentId,
            smallWidgetViewModel = viewModel(key = "SmallWidgetViewModel$currentId")
        )
    }
}


class CardViewModel : ViewModel() {

    private val _state: MutableStateFlow<Int> = MutableStateFlow(-1)
    val state: StateFlow<Int> by ::_state

    fun setCurrentId(id: Int) {
        _state.value = id
    }
}
