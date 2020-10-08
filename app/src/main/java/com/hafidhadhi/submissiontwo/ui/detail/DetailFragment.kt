package com.hafidhadhi.submissiontwo.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.hafidhadhi.submissiontwo.R
import com.hafidhadhi.submissiontwo.data.remote.dto.GithubUser
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_detail.*

const val EXTRA_GITHUB_USER = "EXTRA_GITHUB_USER"

@AndroidEntryPoint
class DetailFragment : Fragment() {

    private val args by navArgs<DetailFragmentArgs>()
    private lateinit var githubUser: GithubUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        githubUser = args.githubUser
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupToolbar()
        setupViewPagerNTab()
    }

    private fun setupToolbar() {
        toolbar.title = githubUser.userName
        toolbar.subtitle = getString(R.string.id_value, githubUser.id)
    }

    private fun setupViewPagerNTab() {
        followerFollowing.apply {
            adapter = FollowerFollowingAdapter(this@DetailFragment)
        }
        TabLayoutMediator(tabLayout, followerFollowing) { tab, position ->
            tab.text =
                if (position == 0) getString(R.string.follower_title, githubUser.followers ?: 0)
                else getString(R.string.following_title, githubUser.following ?: 0)
        }.attach()
    }

    inner class FollowerFollowingAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
        override fun getItemCount(): Int = 2

        override fun createFragment(position: Int): Fragment =
            if (position == 0)
                FollowerFragment().apply {
                    arguments = Bundle().apply {
                        putParcelable(EXTRA_GITHUB_USER, githubUser)
                    }
                }
            else
                FollowingFragment().apply {
                    arguments = Bundle().apply {
                        putParcelable(EXTRA_GITHUB_USER, githubUser)
                    }
                }
    }
}