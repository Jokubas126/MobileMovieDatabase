package com.jokubas.mmdb.model.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Watchlist")
data class WatchlistMovie(
    @PrimaryKey(autoGenerate = true) val movieId: Int
)