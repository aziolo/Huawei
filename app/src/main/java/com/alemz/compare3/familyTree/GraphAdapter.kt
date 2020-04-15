package com.alemz.compare3.familyTree

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
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
            holder.textViewH.text = current.firstName + " " + current.lastName
            holder.photoH.setImageBitmap(byteArrayToBitmap(current.photo))
  //          holder.textViewW.text = current.firstName + " " + current.lastName
  //          holder.photoW.setImageBitmap(byteArrayToBitmap(current.photo))
        }
    }

    inner class GraphViewHolder(itemView: View) : ViewHolder(itemView) {
        var textViewH: TextView = itemView.findViewById(R.id.textViewH)
        var textViewW: TextView = itemView.findViewById(R.id.textViewW)
        var photoH: ImageView = itemView.findViewById(R.id.tree_photoH)
        var photoW: ImageView = itemView.findViewById(R.id.tree_photoW)
    }

    private fun byteArrayToBitmap(byteArray: ByteArray?): Bitmap? {
        val arrayInputStream = ByteArrayInputStream(byteArray)
        return BitmapFactory.decodeStream(arrayInputStream)
    }
    fun setMember(list: List<FamilyMember>) {
        this.list = list
    }


}