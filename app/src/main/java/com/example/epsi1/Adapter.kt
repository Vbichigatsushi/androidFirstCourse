package com.example.epsi1

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import com.example.epsi1.model.T_InventoryItem
import android.widget.TextView
import com.example.epsi1.activity.InventoryDetails
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

        holder.edit.setOnClickListener { //appel du formulaire custom pour editer un item
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Modifier un item")
            val customLayout: View = (context as Activity).layoutInflater.inflate(R.layout.dlg_inventory_item, null)

            val editTextNom : EditText = customLayout.findViewById(R.id.editTextNom)
            editTextNom.text = Editable.Factory.getInstance().newEditable(inventoryItems[position].nom)
            val editTextLieu : EditText = customLayout.findViewById(R.id.editTextLieu)
            editTextLieu.text = Editable.Factory.getInstance().newEditable(inventoryItems[position].lieu)
            val editTextQte : EditText = customLayout.findViewById(R.id.editTextQte)
            editTextQte.text = Editable.Factory.getInstance().newEditable(inventoryItems[position].qte)

            builder.setView(customLayout)
            builder.setPositiveButton("OK"){
                    _: DialogInterface?, _:Int->

                val editTextNomValue = editTextNom.text.toString()
                val editTextLieuValue = editTextLieu.text.toString()
                val editTextQteValue = editTextQte.text.toString()

                if (editTextNomValue.isNotEmpty() && editTextLieuValue.isNotEmpty() && editTextQteValue.isNotEmpty()){
                    CoroutineScope(Dispatchers.IO).launch {
                        (context.applicationContext as UserDataApplication).database.InventoryItemDao().updateRow(editTextNomValue, editTextLieuValue, editTextQteValue, inventoryItems[position].uid)
                        (context as InventoryDetails).getInventoryDatas()
                    }
                }
            }
            val dialog = builder.create()
            dialog.show()
        }
        return cv
    }
}