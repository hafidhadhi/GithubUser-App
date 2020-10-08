package com.hafidhadhi.submissiontwo.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import com.hafidhadhi.submissiontwo.R
import com.hafidhadhi.submissiontwo.data.remote.dto.GithubUser
import com.hafidhadhi.submissiontwo.ui.detail.follower.FollowerFragment
import com.hafidhadhi.submissiontwo.ui.detail.following.FollowingFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_detail.*

@AndroidEntryPoint
class DetailFragment : Fragment() {

    private val args by navArgs<DetailFragmentArgs>()
    private val detailViewModel by viewModels<DetailViewModel>()
    private lateinit var githubUser: GithubUser

    companion object {
        const val EXTRA_GITHUB_USER = "EXTRA_GITHUB_USER"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        githubUser = args.githubUser.apply {
            userName.takeIf { savedInstanceState == null }?.let {
                detailViewModel.getUser(it)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initObserver()
        setupToolbar()
        setupViewPagerNTab()
    }

    private fun initObserver() {
        detailViewModel.userData.observe(viewLifecycleOwner, {
            githubUser = it
            setupToolbar()
            refreshTab()
        })

        detailViewModel.isError.observe(viewLifecycleOwner, {
            Toast.makeText(requireContext(), it.toString(), Toast.LENGTH_LONG).show()
        })
    }

    private fun setupToolbar() {
        val nameData = githubUser.name ?: getString(R.string.no_name)
        val userNameData = githubUser.userName ?: getString(R.string.no_data_symbol)
        val reposCount = githubUser.repos ?: 0
        val reposData = resources.getQuantityString(
            R.plurals.public_repos,
            reposCount,
            reposCount
        )
        val companyData = githubUser.company ?: getString(R.string.no_data_symbol)
        val locationData = githubUser.location ?: getString(R.string.no_data_symbol)
        name.text = nameData
        userNameNRepos.text = getString(R.string.username_and_repos_value, userNameData, reposData)
        companyNLocation.text =
            getString(R.string.company_and_location_value, companyData, locationData)
        Glide.with(requireContext().applicationContext)
            .load(githubUser.avatarUrl)
            .into(profileImage)
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

    private fun refreshTab() {
        for (tabIndex in 0 until tabLayout.tabCount) {
            val tab = tabLayout.getTabAt(tabIndex)
            tab?.text =
                if (tabIndex == 0) getString(R.string.follower_title, githubUser.followers ?: 0)
                else getString(R.string.following_title, githubUser.following ?: 0)
        }
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