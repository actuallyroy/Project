package com.elselse.test

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyAdapter(val items: List<TheProject>): RecyclerView.Adapter<MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater:LayoutInflater = LayoutInflater.from(parent.context);
        val view = inflater.inflate(R.layout.recycler_view_item, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.idTxt.text = items.get(position).code.toString()
        holder.logicTxt.text = items.get(position).anything
    }

    override fun getItemCount(): Int {
        return items.size
    }
}

class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
    var idTxt = itemView.findViewById<TextView>(R.id.id_text)
    var logicTxt = itemView.findViewById<TextView>(R.id.logic_text)
}