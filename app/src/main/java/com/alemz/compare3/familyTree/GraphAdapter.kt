package com.alemz.compare3.familyTree

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProviders
import com.alemz.compare3.R
import com.alemz.compare3.data.FamilyMember
import de.blox.graphview.BaseGraphAdapter
import de.blox.graphview.Graph
import de.blox.graphview.ViewHolder
import java.io.ByteArrayInputStream


class GraphAdapter(private val fragment: FamilyTreeFragment, graph: Graph) :
    BaseGraphAdapter<ViewHolder?>(graph) {
    private var list: List<FamilyMember> = ArrayList()

    private val viewModel: FTViewModel by lazy {
        ViewModelProviders.of(fragment).get(
            FTViewModel::class.java
        )
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GraphViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.node, parent, false)
        return GraphViewHolder(itemView)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder?, data: Any?, position: Int) {
        val holder = viewHolder as GraphViewHolder
        if (list.isNotEmpty() && position < list.size){
            val current = list[position]
            //Log.e("adapter size = ", list.size.toString())
            holder.name1.text = current.firstName + "\n" + current.lastName
            holder.photo1.setImageBitmap(byteArrayToBitmap(current.photo))
            if (current.marriedTo !== null){
                holder.card2.isVisible = true
                val beloved = viewModel.getBeloved(current.marriedTo)
                holder.name2.text = beloved.firstName + "\n" + beloved.lastName
                holder.photo2.setImageBitmap(byteArrayToBitmap(beloved.photo))
                Log.e("adapter current = ",beloved.toString())
            }
            if (current.marriedTo == null) {
                holder.card2.isVisible = false
            }
        }
    }

    inner class GraphViewHolder(itemView: View) : ViewHolder(itemView) {
        var name1: TextView = itemView.findViewById(R.id.textView1)
        var name2: TextView = itemView.findViewById(R.id.textView2)
        var photo1: ImageView = itemView.findViewById(R.id.tree_photo1)
        var photo2: ImageView = itemView.findViewById(R.id.tree_photo2)
        var card1: CardView = itemView.findViewById(R.id.cardView_1)
        var card2: CardView = itemView.findViewById(R.id.cardView_2)
    }

    private fun byteArrayToBitmap(byteArray: ByteArray?): Bitmap? {
        val arrayInputStream = ByteArrayInputStream(byteArray)
        return BitmapFactory.decodeStream(arrayInputStream)
    }
    fun setMember(list: List<FamilyMember>) {
        this.list = list
    }


}