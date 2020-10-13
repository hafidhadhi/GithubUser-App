package com.hafidhadhi.consumerapp.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hafidhadhi.consumerapp.R
import com.hafidhadhi.consumerapp.data.local.FavoriteUser
import kotlinx.android.synthetic.main.github_user_item.view.*

class FavoriteAdapter :
    PagingDataAdapter<FavoriteUser, FavoriteAdapter.ViewHolder>(FavoriteDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val rootView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.github_user_item, parent, false)
                return ViewHolder(rootView)
            }
        }

        fun bind(item: FavoriteUser?) {
            if (item != null) {
                with(itemView) {
                    userName.text = item.userName
                    userId.text = context.getString(R.string.id_value, item.id)
                    Glide.with(context.applicationContext)
                        .load(item.avatarUrl)
                        .into(avatar)
                }
            }
        }
    }

    class FavoriteDiffUtil : DiffUtil.ItemCallback<FavoriteUser>() {
        override fun areItemsTheSame(oldItem: FavoriteUser, newItem: FavoriteUser): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: FavoriteUser,
            newItem: FavoriteUser
        ): Boolean {
            return oldItem == newItem
        }
    }
}