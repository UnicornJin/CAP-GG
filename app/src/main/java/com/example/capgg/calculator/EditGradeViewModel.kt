package com.example.capgg.calculator

import android.util.Log
import androidx.annotation.NonNull
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capgg.R
import com.example.capgg.data.GradeDatabaseDao
import com.example.capgg.data.GradeRecord
import com.example.capgg.databinding.FragmentAddGradeBinding
import com.example.capgg.databinding.FragmentEditGradeBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditGradeViewModel(private val recordId: Long, private val dataSource: GradeDatabaseDao) :
    ViewModel() {

    fun getCheckedButtonId(binding: FragmentEditGradeBinding, grade: Double): Int? {
        return when (grade) {
            5.0 -> binding.editGradeChooseA.id
            4.5 -> binding.editGradeChooseAMinus.id
            4.0 -> binding.editGradeChooseBPlus.id
            3.5 -> binding.editGradeChooseB.id
            3.0 -> binding.editGradeChooseBMinus.id
            2.5 -> binding.editGradeChooseCPlus.id
            2.0 -> binding.editGradeChooseC.id
            1.5 -> binding.editGradeChooseDPlus.id
            1.0 -> binding.editGradeChooseD.id
            0.0 -> binding.editGradeChooseF.id
            else -> null
        }
    }

    private val _navigateToHomePage = MutableLiveData<Boolean?>()
    val navigateToHomePage: LiveData<Boolean?>
        get() = _navigateToHomePage

    fun onDoneNavigate() {
        _navigateToHomePage.value = null
    }

    private val _activeSubmission = MutableLiveData<Boolean?>()
    val activeSubmission: LiveData<Boolean?>
        get() = _activeSubmission

    fun doSubmission() {
        Log.i(">>>", "receive signal")
        _activeSubmission.value = true
    }

    fun doneSubmission() {
        _activeSubmission.value = null
    }

    suspend fun update(record: GradeRecord) {
        withContext(Dispatchers.IO) {
            dataSource.update(record)
            Log.i("Data", "Record updated: ${dataSource.get(recordId)?.moduleCode}")

        }
    }

    suspend fun delete(recordId: Long) {
        withContext(Dispatchers.IO) {
            dataSource.delete(recordId)
            Log.i("Data", "Record deleted: ${recordId}")
        }
    }

    suspend fun insert(record: GradeRecord) {
        withContext(Dispatchers.IO) {
            dataSource.insert(record)
        }
    }

    fun onUpdateGradeRecord(binding: FragmentEditGradeBinding) {
        try {
            val moduleCode: String = binding.editModuleCode.text.toString()
            val moduleCredit: Int = binding.editModuleCredit.text.toString().toInt()
            val moduleGrade: Double = getGradeDouble(binding.editGradeChoose.checkedRadioButtonId)
            val suStatus: Boolean = binding.editSuOptionSwitch.isChecked
            viewModelScope.launch {
                delete(recordId)
                insert(generateRecord(moduleCode, moduleCredit, moduleGrade, suStatus))
                _navigateToHomePage.value = true
            }
        } catch (e: Exception) {
            throw e
        }
    }

    fun onCancelUpdate() {
        _navigateToHomePage.value = true
    }

    fun onDeleteRecord() {
        viewModelScope.launch {
            delete(recordId)
            _navigateToHomePage.value = true
        }
    }

    fun getGradeDouble(checkedButtonId: Int): Double {
        return when (checkedButtonId) {
            R.id.edit_grade_choose_a_plus -> 5.0
            R.id.edit_grade_choose_a -> 5.0
            R.id.edit_grade_choose_a_minus -> 4.5
            R.id.edit_grade_choose_b_plus -> 4.0
            R.id.edit_grade_choose_b -> 3.5
            R.id.edit_grade_choose_b_minus -> 3.0
            R.id.edit_grade_choose_c_plus -> 2.5
            R.id.edit_grade_choose_c -> 2.0
            R.id.edit_grade_choose_d_plus -> 1.5
            R.id.edit_grade_choose_d -> 1.0
            else -> 0.0
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
}