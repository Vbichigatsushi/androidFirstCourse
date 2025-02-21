package com.example.epsi1.model
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class T_InventoryItem (
    @PrimaryKey (autoGenerate = true) val uid: Int=0,
    @ColumnInfo(name = "nom") val nom: String?,
    @ColumnInfo(name = "qte") val qte: String?,
    @ColumnInfo(name = "lieu") val lieu: String?,
)