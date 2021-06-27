package com.jokubas.mmdb.model.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jokubas.mmdb.model.data.util.KEY_LIST_TITLE
import com.jokubas.mmdb.model.data.util.KEY_MOVIE_LIST_IDS
import com.jokubas.mmdb.model.data.util.KEY_UPDATE_DATE
import java.util.*

@Entity(tableName = "movie_list")
data class CustomMovieList(
    @PrimaryKey(autoGenerate = true)
    var roomId: Int,

    @ColumnInfo(name = KEY_UPDATE_DATE)
    var updateDate: Date?,

    @ColumnInfo(name = KEY_LIST_TITLE)
    var listTitle: String?,

    @ColumnInfo(name = KEY_MOVIE_LIST_IDS)
    var movieIdList: List<Int>?
) {
    constructor() : this(0, Date(), "", null)
}