package com.jokubas.mmdb.util.extensions

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jokubas.mmdb.util.SaveState

@BindingAdapter("adapter")
fun RecyclerView.setConcatAdapter(adapter: ConcatAdapter?) {
    adapter?.let { setAdapter(adapter) }
}

@BindingAdapter("saveState")
fun RecyclerView.saveState(saveState: SaveState) {
    when (saveState) {
        is SaveState.Save -> {
            saveState.saveState.invoke(layoutManager?.onSaveInstanceState())
        }
        is SaveState.Restore -> {
            saveState.state?.let { layoutManager?.onRestoreInstanceState(it) }
        }
        is SaveState.Default -> {
        }
    }
}