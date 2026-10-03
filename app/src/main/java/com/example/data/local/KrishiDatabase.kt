package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        FarmerProfileEntity::class,
        FarmPlotEntity::class,
        DiseaseScanEntity::class,
        MandiPriceEntity::class,
        FarmExpenseEntity::class,
        MarketplaceListingEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class KrishiDatabase : RoomDatabase() {
    abstract fun farmerDao(): FarmerDao
    abstract fun farmPlotDao(): FarmPlotDao
    abstract fun diseaseScanDao(): DiseaseScanDao
    abstract fun mandiPriceDao(): MandiPriceDao
    abstract fun farmExpenseDao(): FarmExpenseDao
    abstract fun marketplaceListingDao(): MarketplaceListingDao

    companion object {
        @Volatile
        private var INSTANCE: KrishiDatabase? = null

        fun getInstance(context: Context): KrishiDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    KrishiDatabase::class.java,
                    "krishi_mitra.db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
