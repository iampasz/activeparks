package com.app.activeparks.ui.clubs.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.app.activeparks.MainActivity
import com.app.activeparks.ui.clubs.ClubsViewModelKT
import com.app.activeparks.ui.homeWithUser.fragments.blog.HomeNewsAdapter
import com.app.activeparks.ui.news.fragments.BlogFragment
import com.app.activeparks.ui.news.util.NewsTypes
import com.app.activeparks.util.extention.mainAddFragment
import com.technodreams.activeparks.databinding.FragmentClubsBlogListBinding
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class ClubsBlogListFragment : Fragment() {

    private lateinit var binding: FragmentClubsBlogListBinding
    private val viewModel: ClubsViewModelKT by activityViewModel()

    val adapter = HomeNewsAdapter {

        val bundle = Bundle()
        bundle.putString(NewsTypes.NEWS_ID.type, it.id)
        bundle.putString(NewsTypes.CLUB_ID.type, it.clubId)

        val blogFragment = BlogFragment()
        blogFragment.arguments = bundle

        mainAddFragment((requireActivity() as MainActivity), blogFragment)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentClubsBlogListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        observe()
    }

    private fun observe() {
        with(viewModel) {
            newsClubList.observe(viewLifecycleOwner) {
                it?.takeIf { it.items.isNotEmpty() }
                    ?.apply {
                        adapter.list.submitList(items)
                    }
            }

            currentClubId.observe(viewLifecycleOwner){
                viewModel.getClubNewsList(it)
            }
        }
    }

    private fun initView() {
        binding.rvBlogs.adapter = adapter
    }
}