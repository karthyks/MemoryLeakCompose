package com.karthyks.compose.listmemoryleak

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Composable
fun ListComponent(listViewModel: ListViewModel = viewModel()) {
    val listItems = listViewModel.flow.collectAsState()
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        state = rememberLazyListState()
    ) {
        items(
            items = listItems.value,
            key = { (it.first() * 31) + (it.last() * 31) },
            contentType = { "CardRow" }
        ) {
            CardRow(list = it)
        }
    }
}

@Composable
fun CardRow(list: List<Int>) {
    val reusableModifier = Modifier
        .width(200.dp)
        .height(140.dp)
        .background(color = MaterialTheme.colors.primaryVariant)
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        state = rememberLazyListState()
    ) {
        items(
            items = list,
            key = { it },
            contentType = { "Card" }) {
            Box {
                val storeOwner = rememberStoreOwner()
                CardComponent(
                    id = it,
                    cardViewModel = viewModel(
                        key = "CardViewModel$it",
                        viewModelStoreOwner = storeOwner
                    )
                ) { id ->
                    Box(
                        modifier = reusableModifier
                    ) {
                        SmallWidget(
                            id,
                            smallWidgetViewModel = viewModel(
                                viewModelStoreOwner = storeOwner,
                                key = "SmallWidgetViewModel$id"
                            )
                        )
                    }
                }
            }
        }
    }
}

class ListViewModel : ViewModel() {
    val flow: StateFlow<List<List<Int>>> = MutableStateFlow((0..500).toList().chunked(2))
}
