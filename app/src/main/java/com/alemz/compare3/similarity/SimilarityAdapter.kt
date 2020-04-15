package com.alemz.compare3.similarity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.alemz.compare3.R

class SimilarityAdapter(private val fragment: SimilarityFragment) :
    RecyclerView.Adapter<SimilarityAdapter.ValueHolder>()
{



    inner class ValueHolder(view: View) : RecyclerView.ViewHolder(view) {
        var date: TextView = view.findViewById(R.id.date_tv)
        var value2: TextView = view.findViewById(R.id.value_tv)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ValueHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_simi, parent, false)
        return ValueHolder(itemView)
    }

    override fun getItemCount(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBindViewHolder(holder: ValueHolder, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}