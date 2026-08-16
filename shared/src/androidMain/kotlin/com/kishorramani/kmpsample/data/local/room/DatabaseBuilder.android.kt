package com.kishorramani.kmpsample.data.local.room

import androidx.room.Room
import androidx.room.RoomDatabase
import com.kishorramani.kmpsample.platform.AndroidContextProvider

actual fun getDatabaseBuilder(inMemory: Boolean): RoomDatabase.Builder<AppDatabase> {
    val context = requireNotNull(AndroidContextProvider.context) {
        "AndroidContextProvider.context must be initialized in MainActivity"
    }
    return if (inMemory) {
        Room.inMemoryDatabaseBuilder<AppDatabase>(
            context = context
        )
    } else {
        val dbFile = context.getDatabasePath("techpulse_room.db")
        Room.databaseBuilder<AppDatabase>(
            context = context,
            name = dbFile.absolutePath
        )
    }
}
