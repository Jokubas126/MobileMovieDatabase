package com.jokubas.mmdb.util.extensions

import androidx.databinding.BindingAdapter
import com.google.android.material.slider.RangeSlider

@BindingAdapter("android:valueFrom", "android:valueTo", requireAll = true)
fun RangeSlider.setValueLimits(valueFrom: Float?, valueTo: Float?) {
    valueFrom?.let { setValueFrom(it) }
    valueTo?.let { setValueTo(it) }
}

@BindingAdapter("valueChangedListener")
fun RangeSlider.setOnValueChangedListener(listener: RangeSlider.OnChangeListener) {
    addOnChangeListener(listener)
}