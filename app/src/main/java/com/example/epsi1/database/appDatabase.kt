package com.example.epsi1.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.epsi1.model.InventoryItemDao
import com.example.epsi1.model.T_InventoryItem

@Database(entities = [T_InventoryItem::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun InventoryItemDao(): InventoryItemDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "db_inventory"
                )
                    .build()
                INSTANCE = instance

                instance
            }
        }
    }
}