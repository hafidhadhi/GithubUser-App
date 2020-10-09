package com.hafidhadhi.submissiontwo.ui.search

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

class SearchUserAdapter(private val onUserClickListener: OnUserClickListener) :
    PagingDataAdapter<GithubUser, SearchUserAdapter.ViewHolder>(SearchUserDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent, onUserClickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class ViewHolder(itemView: View, private val onUserClickListener: OnUserClickListener) :
        RecyclerView.ViewHolder(itemView), View.OnClickListener {

        private var githubUser = GithubUser()

        companion object {
            fun from(parent: ViewGroup, onUserClickListener: OnUserClickListener): ViewHolder {
                val rootView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.github_user_item, parent, false)
                return ViewHolder(rootView, onUserClickListener).apply {
                    rootView.setOnClickListener(this)
                }
            }
        }

        fun bind(item: GithubUser?) {
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

        override fun onClick(v: View?) {
            onUserClickListener.onUserClick(githubUser)
        }
    }

    class SearchUserDiffUtil : DiffUtil.ItemCallback<GithubUser>() {
        override fun areItemsTheSame(oldItem: GithubUser, newItem: GithubUser): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: GithubUser, newItem: GithubUser): Boolean {
            return oldItem == newItem
        }

    }

}

interface OnUserClickListener {
    fun onUserClick(githubUser: GithubUser)
}