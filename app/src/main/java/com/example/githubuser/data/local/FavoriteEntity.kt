package com.example.githubuser.data.local

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import kotlinx.parcelize.Parcelize

@Entity(
    tableName = "list_favorite",
    primaryKeys = ["login"]
)
@Parcelize
data class FavoriteEntity (
    @ColumnInfo(name = "login")
    var login: String,
    @ColumnInfo(name = "avatarURL")
    var avatarUrl: String? = null
) : Parcelable