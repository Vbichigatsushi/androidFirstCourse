package com.example.epsi1.activity

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.room.Room
import com.example.epsi1.Adapter
import com.example.epsi1.R
import com.example.epsi1.UserDataApplication
import com.example.epsi1.database.AppDatabase
import com.example.epsi1.model.T_InventoryItem
import com.google.android.material.card.MaterialCardView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class InventoryDetails : AppCompatActivity() {
    private lateinit var adapter: Adapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_inventory_details)
        adapter = Adapter(this, arrayOf(), false)

        val inventoryCard = findViewById<LinearLayout>(R.id.main)
        val listView = inventoryCard.getChildAt(3) as ListView
        listView.adapter = adapter

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        supportActionBar?.title = "🏠 Dom'Hub > Inventaire"

        val addButton = findViewById<ImageButton>(R.id.addInventoryItemButton)
        addButton.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Ajouter un item")
            val customLayout: View = layoutInflater.inflate(R.layout.dlg_inventory_item, null)
            builder.setView(customLayout)
            builder.setPositiveButton("OK"){
                _:DialogInterface?,_:Int->
                val editTextNom : EditText = customLayout.findViewById(R.id.editTextNom)
                val editTextNomValue = editTextNom.text.toString()
                val editTextLieu : EditText = customLayout.findViewById(R.id.editTextLieu)
                val editTextLieuValue = editTextLieu.text.toString()
                val editTextQte : EditText = customLayout.findViewById(R.id.editTextQte)
                val editTextQteValue = editTextQte.text.toString()

                if (editTextNomValue.isNotEmpty() && editTextLieuValue.isNotEmpty() && editTextQteValue.isNotEmpty()){
                    CoroutineScope(Dispatchers.IO).launch {
                        (applicationContext as UserDataApplication).database.InventoryItemDao().insertOne(T_InventoryItem(nom = editTextNomValue, qte = editTextQteValue, lieu = editTextLieuValue))
                        getInventoryDatas()
                    }
                }
            }
            val dialog = builder.create()
            dialog.show()
        }
    }

    override fun onResume() {
        super.onResume()
        getInventoryDatas()
    }

    fun getInventoryDatas(){
        CoroutineScope(Dispatchers.IO).launch {
            val dbvalues = (applicationContext as UserDataApplication).database.InventoryItemDao().selectLines()
            withContext(Dispatchers.Main){
                adapter.updateList(dbvalues)
            }
        }
    }
}