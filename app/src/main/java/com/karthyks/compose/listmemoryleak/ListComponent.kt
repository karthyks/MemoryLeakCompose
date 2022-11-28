package com.karthyks.compose.listmemoryleak

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        state = rememberLazyListState()
    ) {
        items(
            items = list,
            key = { it },
            contentType = { "Card" }) {
            CardComponent(id = it)
        }
    }
}

class ListViewModel : ViewModel() {
    val flow: StateFlow<List<List<Int>>> = MutableStateFlow((0..500).toList().chunked(2))
}
