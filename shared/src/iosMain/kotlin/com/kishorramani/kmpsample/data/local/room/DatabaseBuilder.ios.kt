package com.kishorramani.kmpsample.data.local.room

import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSSearchPathForDirectoriesInDomains
import platform.Foundation.NSUserDomainMask

actual fun getDatabaseBuilder(inMemory: Boolean): RoomDatabase.Builder<AppDatabase> {
    if (inMemory) {
        return Room.inMemoryDatabaseBuilder<AppDatabase>(
            factory = { AppDatabaseConstructor.initialize() }
        ).setDriver(BundledSQLiteDriver())
    }
    val documentDirectory = NSSearchPathForDirectoriesInDomains(
        NSDocumentDirectory,
        NSUserDomainMask,
        true
    ).first() as String
    val dbFilePath = "$documentDirectory/techpulse_room.db"
    return Room.databaseBuilder<AppDatabase>(
        name = dbFilePath,
        factory = { AppDatabaseConstructor.initialize() }
    ).setDriver(BundledSQLiteDriver())
}
