package com.example.epsi1

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageButton
import android.widget.LinearLayout
import com.example.epsi1.model.T_InventoryItem
import android.widget.TextView
import androidx.room.Room
import com.example.epsi1.activity.InventoryDetails
import com.example.epsi1.database.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Adapter(private val context: Context, private var inventoryItems: Array<T_InventoryItem>, private val isMainWindow:Boolean = true) : BaseAdapter() {
    companion object {
        private var inflater: LayoutInflater? = null
    }

    init {
        inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    fun updateList(newList: Array<T_InventoryItem>){
        inventoryItems = newList
        notifyDataSetChanged()
    }

    class Holder {
        lateinit var nom: TextView
        lateinit var lieu: TextView
        lateinit var qte: TextView
        lateinit var edit: ImageButton
        lateinit var delete: ImageButton
        lateinit var actionButtons: LinearLayout
    }

    private fun initHolder(view: View): Holder {
        val holder = Holder()
        holder.nom = view.findViewById(R.id.nom)
        holder.lieu = view.findViewById(R.id.lieu)
        holder.qte = view.findViewById(R.id.qte)
        holder.actionButtons = view.findViewById(R.id.actionButtons)
        holder.edit = view.findViewById(R.id.edit)
        holder.delete = view.findViewById(R.id.delete)
        return holder
    }

    override fun getCount(): Int {
        return inventoryItems.size
    }

    override fun getItem(position: Int): Any {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()

    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var cv = convertView
        if (cv == null) {
            cv = inflater!!.inflate(R.layout.item_inventory_layout, parent, false)
        }
        val holder = initHolder(cv!!)

        holder.nom.text = inventoryItems[position].nom
        holder.lieu.text = inventoryItems[position].lieu
        holder.qte.text = inventoryItems[position].qte

        if (isMainWindow){
            holder.actionButtons.visibility = View.GONE
        }

        holder.delete.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                (context.applicationContext as UserDataApplication).database.InventoryItemDao().deleteRow(inventoryItems[position].uid)
                (context as InventoryDetails).getInventoryDatas()
            }

        }

        holder.edit.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                //(context.applicationContext as UserDataApplication).database.InventoryItemDao().deleteRow(inventoryItems[position].uid)
                (context as InventoryDetails).getInventoryDatas()
            }

        }

        return cv
    }
}