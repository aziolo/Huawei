package com.alemz.compare3
import android.view.View
import android.widget.ImageView

import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.alemz.compare3.R
import de.blox.graphview.ViewHolder



internal class SimpleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var textView: TextView = itemView.findViewById(R.id.textViewa)
    var photo: ImageView = itemView.findViewById(R.id.tree_photo)

}