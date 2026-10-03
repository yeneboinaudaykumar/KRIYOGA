package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface FarmerDao {
    @Query("SELECT * FROM farmer_profile WHERE id = 1 LIMIT 1")
    fun getProfileFlow(): Flow<FarmerProfileEntity?>

    @Query("SELECT * FROM farmer_profile WHERE id = 1 LIMIT 1")
    suspend fun getProfile(): FarmerProfileEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProfile(profile: FarmerProfileEntity)
}

@Dao
interface FarmPlotDao {
    @Query("SELECT * FROM farm_plots ORDER BY id ASC")
    fun getAllPlotsFlow(): Flow<List<FarmPlotEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlot(plot: FarmPlotEntity)

    @Update
    suspend fun updatePlot(plot: FarmPlotEntity)

    @Query("DELETE FROM farm_plots WHERE id = :plotId")
    suspend fun deletePlot(plotId: Int)
}

@Dao
interface DiseaseScanDao {
    @Query("SELECT * FROM disease_scans ORDER BY timestamp DESC")
    fun getAllScansFlow(): Flow<List<DiseaseScanEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScan(scan: DiseaseScanEntity)

    @Query("DELETE FROM disease_scans WHERE id = :scanId")
    suspend fun deleteScan(scanId: Int)
}

@Dao
interface MandiPriceDao {
    @Query("SELECT * FROM mandi_prices ORDER BY commodity ASC")
    fun getAllMandiPricesFlow(): Flow<List<MandiPriceEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPrices(prices: List<MandiPriceEntity>)

    @Query("SELECT * FROM mandi_prices WHERE commodity LIKE '%' || :query || '%' OR marketName LIKE '%' || :query || '%'")
    fun searchPricesFlow(query: String): Flow<List<MandiPriceEntity>>
}

@Dao
interface FarmExpenseDao {
    @Query("SELECT * FROM farm_expenses ORDER BY dateEpoch DESC")
    fun getAllExpensesFlow(): Flow<List<FarmExpenseEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpense(expense: FarmExpenseEntity)

    @Query("DELETE FROM farm_expenses WHERE id = :expenseId")
    suspend fun deleteExpense(expenseId: Int)
}

@Dao
interface MarketplaceListingDao {
    @Query("SELECT * FROM marketplace_listings ORDER BY postedDateEpoch DESC")
    fun getAllListingsFlow(): Flow<List<MarketplaceListingEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertListing(listing: MarketplaceListingEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertListings(listings: List<MarketplaceListingEntity>)

    @Query("DELETE FROM marketplace_listings WHERE id = :listingId")
    suspend fun deleteListing(listingId: Int)
}
