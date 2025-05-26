package com.example.testfin.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.testfin.R

class BackupAdapter(private val backupList: MutableList<com.example.testfin.data.BackupData>) :
    RecyclerView.Adapter<BackupAdapter.BackupViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BackupViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_backup, parent, false)
        return BackupViewHolder(view)
    }

    override fun onBindViewHolder(holder: BackupViewHolder, position: Int) {
        val backup = backupList[position]
        holder.tvBackupName.text = backup.name
        holder.tvBackupDate.text = backup.date
    }

    override fun getItemCount(): Int {
        return backupList.size
    }

    class BackupViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvBackupName: TextView = itemView.findViewById(R.id.tvBackupName)
        val tvBackupDate: TextView = itemView.findViewById(R.id.tvBackupDate)
    }
}
