package com.example.capgg.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface GradeDatabaseDao {
    @Insert
    suspend fun insert(grade: GradeRecord)

    @Update
    suspend fun update(grade: GradeRecord)

    @Query("SELECT * FROM grade_record_table WHERE recordId = :key")
    suspend fun get(key: Long): GradeRecord?

    @Query("DELETE FROM grade_record_table WHERE recordId = :key")
    suspend fun delete(key: Long)

    @Query("SELECT * FROM grade_record_table ORDER BY recordId DESC")
    fun getAllRecords(): LiveData<List<GradeRecord>>
}