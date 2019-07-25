package com.example.duyustory

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide

class MainAdapter(private val context: Context, private val catList: List<Cat>) : RecyclerView.Adapter<MainAdapter.MainViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        return MainViewHolder(LayoutInflater.from(context).inflate(R.layout.recycler_row, parent, false))
    }

    override fun getItemCount() = catList.size

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.bind(catList[position])
    }

    inner class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener{
        private val tvTitle = itemView.findViewById<TextView>(R.id.recycler_title)
        private val tvContent = itemView.findViewById<TextView>(R.id.recycler_content)
        private val imageCatPicture = itemView.findViewById<ImageView>(R.id.recycler_catPicture)

        fun bind(catData: Cat) {
            tvTitle.text = catData.title
            tvContent.text = catData.contents
            Glide.with(context).load(catData.image).into(imageCatPicture)

            imageCatPicture.setOnClickListener(this)
        }

        override fun onClick(v : View?) {
            when (v?.id) {
                // TODO : Image Data (imageCatPicture.Data) 를 Intent로 넘겨주고 PictureActivity 에서 띄워주기
            }
        }
    }
}
