package com.example.mmdb.ui.discover

import androidx.databinding.ObservableField
import com.google.android.material.slider.RangeSlider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.text.DecimalFormatSymbols

class DiscoverAppBarContentViewModel(
    coroutineScope: CoroutineScope,
    private val startYearFlow: StateFlow<Int>,
    private val endYearFlow: StateFlow<Int>,
    val onRangeSliderValueChangedListener: RangeSlider.OnChangeListener
) {

    val valueFrom: Float = startYearFlow.value.toFloat()
    val valueTo: Float = endYearFlow.value.toFloat()

    val startYear: ObservableField<String> = ObservableField(valueFrom.toInt().toString())
    val endYear: ObservableField<String> = ObservableField(valueTo.toInt().toString())

    init {

        coroutineScope.launch(Dispatchers.IO) {
            combine(
                startYearFlow,
                endYearFlow
            ) { start, end -> Pair(start, end) }.collect { (start, end) ->
                startYear.set(
                    start.takeUnless {
                        start == valueFrom.toInt()
                    }?.toString() ?: DecimalFormatSymbols.getInstance().infinity
                )
                endYear.set(end.toString())
            }
        }
    }
}