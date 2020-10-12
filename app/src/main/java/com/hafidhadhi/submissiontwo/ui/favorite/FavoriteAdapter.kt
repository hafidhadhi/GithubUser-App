package com.hafidhadhi.submissiontwo.ui.favorite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hafidhadhi.submissiontwo.R
import com.hafidhadhi.submissiontwo.data.local.entity.FavoriteUserEnt
import kotlinx.android.synthetic.main.github_user_item.view.*

class FavoriteAdapter(private val onFavUserClickListener: OnFavUserClickListener) :
    PagingDataAdapter<FavoriteUserEnt, FavoriteAdapter.ViewHolder>(FavoriteDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent, onFavUserClickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class ViewHolder(itemView: View, private val onFavUserClickListener: OnFavUserClickListener) :
        RecyclerView.ViewHolder(itemView), View.OnClickListener {

        private var favUser = FavoriteUserEnt()

        companion object {
            fun from(
                parent: ViewGroup,
                onFavUserClickListener: OnFavUserClickListener
            ): ViewHolder {
                val rootView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.github_user_item, parent, false)
                return ViewHolder(rootView, onFavUserClickListener).apply {
                    rootView.setOnClickListener(this)
                }
            }
        }

        fun bind(item: FavoriteUserEnt?) {
            if (item != null) {
                with(itemView) {
                    userName.text = item.userName
                    userId.text = context.getString(R.string.id_value, item.id)
                    Glide.with(context.applicationContext)
                        .load(item.avatarUrl)
                        .into(avatar)
                }
                favUser = item
            }
        }

        override fun onClick(v: View?) {
            onFavUserClickListener.onFavUserClick(favUser)
        }
    }

    class FavoriteDiffUtil : DiffUtil.ItemCallback<FavoriteUserEnt>() {
        override fun areItemsTheSame(oldItem: FavoriteUserEnt, newItem: FavoriteUserEnt): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: FavoriteUserEnt,
            newItem: FavoriteUserEnt
        ): Boolean {
            return oldItem == newItem
        }

    }

}

interface OnFavUserClickListener {
    fun onFavUserClick(favUser: FavoriteUserEnt)
}