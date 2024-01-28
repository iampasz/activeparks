package com.app.activeparks.ui.homeWithUser.fragments.blog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.app.activeparks.MainActivity
import com.app.activeparks.ui.news.BlogViewModel
import com.app.activeparks.ui.news.fragments.BlogFragment
import com.app.activeparks.ui.news.fragments.BlogListFragment
import com.app.activeparks.ui.news.util.NewsTypes
import com.app.activeparks.util.extention.gone
import com.app.activeparks.util.extention.mainAddFragment
import com.technodreams.activeparks.databinding.FragmentHomeBlogBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeBlogFragment : Fragment() {

    private lateinit var binding: FragmentHomeBlogBinding
    val adapter = HomeNewsAdapter {
        val bundle = Bundle()
        bundle.putString(NewsTypes.NEWS_ID.type, it.id)
        bundle.putString(NewsTypes.CLUB_ID.type, it.clubId)

        val blogFragment = BlogFragment()
        blogFragment.arguments = bundle

        mainAddFragment((requireActivity() as MainActivity), blogFragment)
    }
    private val viewModel: BlogViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBlogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onClick()
        viewModel.getNews()
        initView()
        observe()
    }

    private fun observe() {
        with(viewModel) {
            newsList.observe(viewLifecycleOwner) {
                binding.pbNews.gone()
                it?.takeIf { it.items.isNotEmpty() }
                    ?.apply {
                        adapter.list.submitList(items)
                    }
            }
        }
    }

    private fun initView() {
        binding.rvNews.adapter = adapter
    }

    private fun onClick(){
            binding.tvAllPArks.setOnClickListener{
                mainAddFragment((requireActivity() as MainActivity), BlogListFragment())
            }
    }
}