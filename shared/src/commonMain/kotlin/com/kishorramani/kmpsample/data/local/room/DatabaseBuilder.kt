package com.kishorramani.kmpsample.data.local.room

import androidx.room.RoomDatabase

expect fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase>

fun getRoomDatabase(): AppDatabase {
    return getDatabaseBuilder()
        .fallbackToDestructiveMigration(dropAllTables = true)
        .build()
}
