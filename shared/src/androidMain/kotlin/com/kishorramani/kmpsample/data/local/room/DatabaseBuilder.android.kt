package com.kishorramani.kmpsample.data.local.room

import androidx.room.Room
import androidx.room.RoomDatabase
import com.kishorramani.kmpsample.platform.AndroidContextProvider

actual fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    val context = requireNotNull(AndroidContextProvider.context) {
        "AndroidContextProvider.context must be initialized in MainActivity"
    }
    val dbFile = context.getDatabasePath("techpulse_room.db")
    return Room.databaseBuilder<AppDatabase>(
        context = context,
        name = dbFile.absolutePath
    )
}
