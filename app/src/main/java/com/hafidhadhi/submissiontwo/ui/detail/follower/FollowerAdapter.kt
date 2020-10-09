package com.hafidhadhi.submissiontwo.ui.detail.follower

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hafidhadhi.submissiontwo.R
import com.hafidhadhi.submissiontwo.data.remote.dto.GithubUser
import kotlinx.android.synthetic.main.github_user_item.view.*

class FollowerAdapter :
    PagingDataAdapter<GithubUser, FollowerAdapter.ViewHolder>(FollowerDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private var githubUser = GithubUser()

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val rootView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.github_user_item, parent, false)
                return ViewHolder(rootView)
            }
        }

        fun bind(item: GithubUser?) {
            Log.d(this::class.simpleName, item.toString())
            if (item != null) {
                with(itemView) {
                    userName.text = item.userName
                    userId.text = context.getString(R.string.id_value, item.id)
                    Glide.with(context.applicationContext)
                        .load(item.avatarUrl)
                        .into(avatar)
                }
                githubUser = item
            }
        }
    }

    class FollowerDiffUtil : DiffUtil.ItemCallback<GithubUser>() {
        override fun areItemsTheSame(oldItem: GithubUser, newItem: GithubUser): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: GithubUser, newItem: GithubUser): Boolean {
            return oldItem == newItem
        }
    }
}