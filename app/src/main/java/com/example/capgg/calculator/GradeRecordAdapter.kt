package com.example.capgg.calculator

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.capgg.R
import com.example.capgg.data.GradeRecord
import com.example.capgg.databinding.ListItemGradeRecordBinding

class GradeRecordAdapter(val clickListener: GradeRecordListener) :
    ListAdapter<GradeRecord, GradeRecordAdapter.ViewHolder>(GradeRecordDiffCallback()) {
    class ViewHolder private constructor(val binding: ListItemGradeRecordBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            record: GradeRecord,
            clickListener: GradeRecordListener,
            adapter: GradeRecordAdapter
        ) {
            binding.record = record
            binding.clickListener = clickListener
            binding.executePendingBindings()
            setIcon(record.moduleGrade)
            binding.gradeItemModuleTitle.text = record.moduleCode
            binding.gradeItemModuleCredit.text = formatCredit(record.moduleCredit)
            binding.gradeItemModuleGrade.text = formatGrade(record.moduleGrade)
            binding.gradeItemModuleSuStatus.text = formatSUStatus(record.suStatus)
        }

        fun setIcon(grade: Double?) {
            grade?.let {
                if (grade >= 3.0) {
                    binding.gradeItemIcon.setImageResource(R.drawable.ic_good)
                } else {
                    binding.gradeItemIcon.setImageResource(R.drawable.ic_bad)
                }
            }
        }

        fun formatCredit(credit: Int): String {
            return "Module Credit: $credit"
        }

        fun formatGrade(grade: Double?): String {
            return when (grade) {
                5.0 -> "A+/A"
                4.5 -> "A-"
                4.0 -> "B+"
                3.5 -> "B"
                3.0 -> "B-"
                2.5 -> "C+"
                2.0 -> "C"
                1.5 -> "D+"
                1.0 -> "D"
                0.0 -> "F"
                else -> "N.A."
            }
        }

        fun formatSUStatus(su: Boolean): String {
            return if (su) "S/U" else ""
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemGradeRecordBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position)!!, clickListener, this)
    }
}

class GradeRecordDiffCallback : DiffUtil.ItemCallback<GradeRecord>() {
    override fun areItemsTheSame(oldItem: GradeRecord, newItem: GradeRecord): Boolean {
        return oldItem.recordId == newItem.recordId
    }

    override fun areContentsTheSame(oldItem: GradeRecord, newItem: GradeRecord): Boolean {
        return oldItem.recordId == newItem.recordId
                && oldItem.moduleCode == newItem.moduleCode
                && oldItem.moduleCredit == newItem.moduleCredit
                && oldItem.moduleGrade == newItem.moduleGrade
                && oldItem.suStatus == newItem.suStatus
    }

}

class GradeRecordListener(val clickListener: (gradeRecordID: Long) -> Unit) {
    fun onClick(record: GradeRecord) = clickListener(record.recordId)
}