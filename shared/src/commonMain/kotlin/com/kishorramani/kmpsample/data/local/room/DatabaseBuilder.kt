package com.kishorramani.kmpsample.data.local.room

import androidx.room.RoomDatabase

expect fun getDatabaseBuilder(inMemory: Boolean = false): RoomDatabase.Builder<AppDatabase>

fun getRoomDatabase(inMemory: Boolean = false): AppDatabase {
    return getDatabaseBuilder(inMemory)
        .fallbackToDestructiveMigration(dropAllTables = true)
        .build()
}
