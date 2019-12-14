package com.example.duyustory.main

import android.content.Context
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.duyustory.data.Cat
import com.example.duyustory.picture.PictureActivity
import com.example.duyustory.R

class MainAdapter(private val context: Context, private var catList: List<Cat>) :
    RecyclerView.Adapter<MainAdapter.MainViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        return MainViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.recycler_row,
                parent,
                false
            )
        )
    }

    override fun getItemCount() = catList.size

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.bind(catList[position])
    }

    fun setListData(catList : List<Cat>) {
        this.catList = catList
        notifyDataSetChanged()
    }

    inner class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        private val imageCatPicture = itemView.findViewById<ImageView>(R.id.recycler_catPicture)

        fun bind(catData: Cat) {
            Glide.with(context).load(catData.imageUrl).thumbnail(0.5f).into(imageCatPicture)
            imageCatPicture.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            when (v?.id) {
                R.id.recycler_catPicture -> {
                    val startPictureActivityIntent = Intent(context, PictureActivity::class.java)
                    startPictureActivityIntent.putExtra("Picture_URL",catList[adapterPosition].imageUrl)

                    context.startActivity(startPictureActivityIntent)
                }
            }
        }
    }
}
