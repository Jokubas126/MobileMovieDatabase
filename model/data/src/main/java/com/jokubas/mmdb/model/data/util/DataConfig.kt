package com.jokubas.mmdb.model.data.util

import com.jokubas.mmdb.model.data.dataclasses.Country
import com.jokubas.mmdb.model.data.dataclasses.Genre

fun getAnyNameList(list: List<*>?): List<String> {
    val nameList = mutableListOf<String>()
    list?.let {
        for (value in it)
            when (value) {
                is Genre -> nameList.add(value.name)
                is Country -> nameList.add(value.name)
            }
    }
    return nameList
}