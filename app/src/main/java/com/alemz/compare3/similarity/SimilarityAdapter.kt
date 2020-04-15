package com.alemz.compare3.similarity

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.alemz.compare3.R
import com.alemz.compare3.data.Similarity
import java.io.ByteArrayInputStream

class SimilarityAdapter(private val fragment: SimilarityFragment) :
    RecyclerView.Adapter<SimilarityAdapter.ValueHolder>()
{

    private var list: List<Similarity> = ArrayList()

    inner class ValueHolder(view: View) : RecyclerView.ViewHolder(view) {
        var date: TextView = view.findViewById(R.id.date_tv)
        var value2: TextView = view.findViewById(R.id.value_tv)
        var me: ImageView = view.findViewById(R.id.me_photo)
        var you: ImageView = view.findViewById(R.id.you_photo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ValueHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_simi, parent, false)
        return ValueHolder(itemView)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun set(list: List<Similarity>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ValueHolder, position: Int) {
        val current = list[position]

        holder.me.setImageBitmap(byteArrayToBitmap(current.photo1)?.let { scaleBitmap(it) })
        holder.you.setImageBitmap(byteArrayToBitmap(current.photo2)?.let { scaleBitmap(it) })
        holder.date.text = current.data
        holder.value2.text = current.value.toString() + " %"
    }

    private fun byteArrayToBitmap(byteArray: ByteArray?): Bitmap? {
        val arrayInputStream = ByteArrayInputStream(byteArray)
        return BitmapFactory.decodeStream(arrayInputStream)
    }

    private fun scaleBitmap(input: Bitmap): Bitmap {
        val currentWidth = input.width
        val currentHeight = input.height
        val currentPixels = currentWidth * currentHeight
        val maxPixels = 512 * 512 / 4
        if (currentPixels <= maxPixels) {
            return input
        }
        val scaleFactor =
            kotlin.math.sqrt(maxPixels / currentPixels.toDouble())
        val newWidthPx = kotlin.math.floor(currentWidth * scaleFactor).toInt()
        val newHeightPx = kotlin.math.floor(currentHeight * scaleFactor).toInt()
        return Bitmap.createScaledBitmap(input, newWidthPx, newHeightPx, true)
    }




}