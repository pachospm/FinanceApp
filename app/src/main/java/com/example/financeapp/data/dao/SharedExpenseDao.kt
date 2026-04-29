package com.example.financeapp.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.financeapp.data.model.Budget
import com.example.financeapp.data.model.ExpenseCategory
import com.example.financeapp.data.model.SharedExpense
import kotlinx.coroutines.flow.Flow

@Dao
interface SharedExpenseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(sharedExpense: SharedExpense)


    @Query("SELECT * FROM shared_expenses WHERE creatorUserId = :userId ORDER BY date DESC")
    fun getSharedExpensesByUser(userId: Long): Flow<List<SharedExpense>>


    @Query("SELECT * FROM shared_expenses WHERE creatorUserId = :userId AND settled = 0")
    fun getUnsettledSharedExpenses(userId: Long): Flow<List<SharedExpense>>


    @Update
    suspend fun update(sharedExpense: SharedExpense)


    @Delete
    suspend fun delete(sharedExpense: SharedExpense)

}