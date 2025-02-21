package com.example.epsi1.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface InventoryItemDao {
    @Query("select * from T_InventoryItem order by uid desc limit 100")
    fun selectLines(): Array<T_InventoryItem>

    @Insert
    fun insertOne(vararg inventoryItem: T_InventoryItem)

    @Query("DELETE FROM t_inventoryitem WHERE uid = :id")
    fun deleteRow(vararg id: Int)

    @Query("UPDATE t_inventoryitem set nom = :nom, lieu = :lieu, qte = :qte where uid = :id")
    fun updateRow(nom: String, lieu: String, qte: String, id: Int)
}