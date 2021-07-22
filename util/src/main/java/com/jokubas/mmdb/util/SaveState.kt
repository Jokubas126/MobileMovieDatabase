package com.jokubas.mmdb.util

import android.os.Parcelable

sealed class SaveState {

    object Default : SaveState()

    data class Save(val saveState: (Parcelable?) -> Unit) : SaveState()

    data class Restore(val state: Parcelable?) : SaveState()
}