package com.example.testfin.fragments

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.testfin.R
import com.example.testfin.adapters.BackupAdapter
import com.example.testfin.data.BackupData
import com.example.testfin.data.Transaction
import com.example.testfin.helpers.SharedPrefHelper
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.util.*

class BackupFragment : Fragment() {

    private lateinit var btnExport: Button
    private lateinit var btnImport: Button
    private lateinit var tvBackupStatus: TextView
    private lateinit var rvBackups: RecyclerView
    private lateinit var backupAdapter: BackupAdapter
    private val backupList = mutableListOf<BackupData>()

    private val sharedPrefName = "BackupPrefs"
    private val keyBackupList = "BackupList"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_backup, container, false)

        btnExport = rootView.findViewById(R.id.btnExport)
        btnImport = rootView.findViewById(R.id.btnImport)
        tvBackupStatus = rootView.findViewById(R.id.tvBackupStatus)
        rvBackups = rootView.findViewById(R.id.rvBackups)

        // Setup RecyclerView
        rvBackups.layoutManager = LinearLayoutManager(requireContext())
        backupAdapter = BackupAdapter(backupList)
        rvBackups.adapter = backupAdapter

        // Load previously stored backups
        loadBackupData()

        btnExport.setOnClickListener {
            exportData()
        }

        btnImport.setOnClickListener {
            importData()
        }

        return rootView
    }

    private fun exportData() {
        val jsonData = generateJsonFromTransactions()
        val fileName = "backup_${System.currentTimeMillis()}.json"

        requireContext().openFileOutput(fileName, Context.MODE_PRIVATE).use {
            it.write(jsonData.toByteArray())
        }

        // Add to backup list
        val timeStamp = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())
        val backup = BackupData(fileName, timeStamp)
        backupList.add(0, backup)
        backupAdapter.notifyItemInserted(0)
        saveBackupData()

        tvBackupStatus.text = "Backup saved as $fileName"
        Toast.makeText(requireContext(), "Backup exported", Toast.LENGTH_SHORT).show()
    }


    private fun importData() {
        if (backupList.isNotEmpty()) {
            val fileName = backupList[0].name
            try {
                val json = requireContext().openFileInput(fileName).bufferedReader().use { it.readText() }

                // Convert JSON into a list of Transaction objects
                val type = object : TypeToken<List<Transaction>>() {}.type
                val restoredTransactions: List<Transaction> = Gson().fromJson(json, type)

                // Save restored transactions to SharedPreferences
                val sharedPrefHelper = SharedPrefHelper(requireContext())
                sharedPrefHelper.saveTransactions(restoredTransactions)

                tvBackupStatus.text = "Restored from $fileName"
                Toast.makeText(requireContext(), "Data imported: ${restoredTransactions.size} items", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                tvBackupStatus.text = "Failed to import"
                Toast.makeText(requireContext(), "Error reading file", Toast.LENGTH_SHORT).show()
            }
        }
    }



    private fun saveBackupData() {
        val prefs = requireActivity().getSharedPreferences(sharedPrefName, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        val joined = backupList.joinToString("||") { "${it.name}::${it.date}" }
        editor.putString(keyBackupList, joined)
        editor.apply()
    }

    private fun loadBackupData() {
        val prefs = requireActivity().getSharedPreferences(sharedPrefName, Context.MODE_PRIVATE)
        val raw = prefs.getString(keyBackupList, null)
        if (!raw.isNullOrEmpty()) {
            val items = raw.split("||")
            for (entry in items) {
                val parts = entry.split("::")
                if (parts.size == 2) {
                    backupList.add(BackupData(parts[0], parts[1]))
                }
            }
            backupAdapter.notifyDataSetChanged()
        }
    }

    private fun generateJsonFromTransactions(): String {
        val sharedPrefHelper = SharedPrefHelper(requireContext())
        val transactions = sharedPrefHelper.getTransactions() // Fetch real transactions from SharedPreferences
        return Gson().toJson(transactions) // Convert the transactions list to JSON
    }


}

