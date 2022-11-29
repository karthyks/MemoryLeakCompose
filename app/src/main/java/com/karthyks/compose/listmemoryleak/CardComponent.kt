package com.karthyks.compose.listmemoryleak

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Composable
fun CardComponent(
    id: Int,
    cardViewModel: CardViewModel,
    widgetContent: @Composable (Int) -> Unit,
) {
    LaunchedEffect(key1 = id) {
        cardViewModel.setCurrentId(id)
    }
    val currentId by cardViewModel.state.collectAsState()
    widgetContent(currentId)
}


class CardViewModel : ViewModel() {

    private val _state: MutableStateFlow<Int> = MutableStateFlow(-1)
    val state: StateFlow<Int> by ::_state

    fun setCurrentId(id: Int) {
        _state.value = id
    }
}

class CustomStoreOwner : ViewModelStoreOwner {

    private val store by lazy {
        CustomStore()
    }

    override fun getViewModelStore(): ViewModelStore {
        return store
    }

    fun clear() {
        store.clear()
    }

    inner class CustomStore : ViewModelStore()
}

@Composable
fun rememberStoreOwner(): CustomStoreOwner {
    val storeOwner = remember {
        CustomStoreOwner()
    }
    DisposableEffect(key1 = Unit) {
        onDispose {
            storeOwner.clear()
        }
    }
    return storeOwner
}
