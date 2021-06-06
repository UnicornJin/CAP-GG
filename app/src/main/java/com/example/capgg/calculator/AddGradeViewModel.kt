package com.example.capgg.calculator

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.capgg.R
import com.example.capgg.data.GradeDatabaseDao
import com.example.capgg.data.GradeRecord
import com.example.capgg.databinding.FragmentAddGradeBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddGradeViewModel(
    val dataSource: GradeDatabaseDao,
    application: Application,
) : AndroidViewModel(application) {

    private val _doSubmission = MutableLiveData<Boolean>()
    val doSubmission: LiveData<Boolean>
        get() = _doSubmission

    fun activeSubmission() {
        _doSubmission.value = true
    }

    fun doneSubmission() {
        _doSubmission.value = null
    }

    suspend fun insert(record: GradeRecord) {
        withContext(Dispatchers.IO) {
            dataSource.insert(record)
        }
    }

    fun generateRecord(
        moduleCode: String,
        moduleCredit: Int,
        moduleGrade: Double,
        suStatus: Boolean
    ): GradeRecord {
        val newRecord = GradeRecord()
        newRecord.moduleCode = moduleCode
        newRecord.moduleCredit = moduleCredit
        newRecord.moduleGrade = moduleGrade
        newRecord.suStatus = suStatus
        return newRecord
    }

    fun getGradeDouble(checkedButtonId: Int): Double {
        return when (checkedButtonId) {
            R.id.grade_choose_a_plus -> 5.0
            R.id.grade_choose_a -> 5.0
            R.id.grade_choose_a_minus -> 4.5
            R.id.grade_choose_b_plus -> 4.0
            R.id.grade_choose_b -> 3.5
            R.id.grade_choose_b_minus -> 3.0
            R.id.grade_choose_c_plus -> 2.5
            R.id.grade_choose_c -> 2.0
            R.id.grade_choose_d_plus -> 1.5
            R.id.grade_choose_d -> 1.0
            else -> 0.0
        }
    }

    fun onSubmission(binding: FragmentAddGradeBinding) {
        try {
            val moduleCode: String = binding.inputModuleCode.text.toString()
            val moduleCredit: Int = binding.inputModuleCredit.text.toString().toInt()
            val moduleGrade: Double = getGradeDouble(binding.gradeChoose.checkedRadioButtonId)
            val suStatus: Boolean = binding.suOptionSwitch.isChecked
            viewModelScope.launch {
                insert(generateRecord(moduleCode, moduleCredit, moduleGrade, suStatus))
            }
        } catch (e: Exception) {
            throw e
        }
    }
}