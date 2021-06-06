package com.example.capgg.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "grade_record_table")
data class GradeRecord(
    @PrimaryKey(autoGenerate = true)
    var recordId: Long = 0L,

    @ColumnInfo(name = "module_code")
    var moduleCode: String? = null,

    @ColumnInfo(name = "module_grade")
    var moduleGrade: Double? = null,

    @ColumnInfo(name = "module_credit")
    var moduleCredit: Int = 0,

    @ColumnInfo(name = "su_status")
    var suStatus: Boolean = false
)