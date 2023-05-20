package com.example.dzgif

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class DataAdapter(private val ctx: Context, private val modelList: ArrayList<DataModel>) :
    RecyclerView.Adapter<DataAdapter.ViewHolder>() {

    private var onItemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }

    interface OnItemClickListener {
        fun onItemClick(pos: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = modelList[position]
        Glide.with(ctx).load(data.imageUrl).into(holder.imageView)
    }

    override fun getItemCount(): Int {
        return modelList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imgView)

        init {
            itemView.setOnClickListener { v ->
                onItemClickListener?.let {
                    val position = bindingAdapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        it.onItemClick(position)
                    }
                }
            }
        }
    }
}
