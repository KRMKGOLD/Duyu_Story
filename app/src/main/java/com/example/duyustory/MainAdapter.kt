package com.example.duyustory

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

class MainAdapter(private val context: Context, private val catList: List<Cat>) : RecyclerView.Adapter<MainAdapter.MainViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        return MainViewHolder(LayoutInflater.from(context).inflate(R.layout.recycler_row,parent, false))
    }

    override fun getItemCount() = catList.size

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.bind(catList[position])
    }

    inner class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvTitle = itemView.findViewById<TextView>(R.id.recycler_title)
        private val tvContent = itemView.findViewById<TextView>(R.id.recycler_content)
        private val imageCatPicture = itemView.findViewById<ImageView>(R.id.recycler_catPicture)

        fun bind(catData : Cat) {
            tvTitle.text = catData.title
            tvContent.text = catData.contents
            imageCatPicture.setImageResource(R.drawable.abc_ab_share_pack_mtrl_alpha)
        }
    }
}