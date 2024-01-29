package com.app.activeparks.ui.news.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.app.activeparks.MainActivity
import com.app.activeparks.ui.homeWithUser.fragments.blog.HomeNewsAdapter
import com.app.activeparks.ui.news.BlogViewModel
import com.app.activeparks.ui.news.NewsCreateActivity
import com.app.activeparks.ui.news.util.NewsTypes
import com.app.activeparks.util.extention.StringTypes
import com.app.activeparks.util.extention.mainAddFragment
import com.app.activeparks.util.extention.removeFragment
import com.technodreams.activeparks.databinding.FragmentBlogListBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class BlogListFragment : Fragment() {

    private lateinit var binding: FragmentBlogListBinding
    private val viewModel: BlogViewModel by viewModel()

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
        binding = FragmentBlogListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getNews()
        onClick()
        initView()
        observe()
    }

    private fun observe() {
        with(viewModel) {
            newsList.observe(viewLifecycleOwner) {
                it?.takeIf { it.items.isNotEmpty() }
                    ?.apply {
                        adapter.list.submitList(items)
                    }
            }
        }
    }

    private fun initView() {
        binding.rvBlogs.adapter = adapter
    }

    private fun onClick(){
        binding.close.setOnClickListener{
            parentFragmentManager.removeFragment(this)
        }

        binding.create.setOnClickListener{
            startActivity(Intent(activity, NewsCreateActivity::class.java).putExtra(StringTypes.ID.type, id))
        }
    }
}