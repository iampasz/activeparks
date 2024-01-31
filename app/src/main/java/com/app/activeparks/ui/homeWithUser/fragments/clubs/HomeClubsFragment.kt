package com.app.activeparks.ui.homeWithUser.fragments.clubs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.app.activeparks.MainActivity
import com.app.activeparks.ui.clubs.fragments.ClubsListFragment
import com.app.activeparks.ui.event.activity.ClubFragment
import com.app.activeparks.util.extention.gone
import com.app.activeparks.util.extention.mainAddFragment
import com.app.activeparks.util.extention.mainReplaceFragment
import com.technodreams.activeparks.databinding.FragmentHomeClubsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeClubsFragment : Fragment() {

    private lateinit var binding: FragmentHomeClubsBinding
    val adapter = HomeClubsAdapter{

        val bundle = Bundle()
        bundle.putString("CLUB_ID", it.id)
        val clubFragment = ClubFragment()
        clubFragment.arguments = bundle
        mainAddFragment((requireActivity() as MainActivity), clubFragment)

    }
    private val viewModel: HomeClubsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeClubsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onClick()
        viewModel.getCombinatedClubList()
        initView()
        observe()
    }

    private fun observe() {
        viewModel.combineClubList.observe(viewLifecycleOwner){v->
            val listUserIsHead = v.items.userIsHead
            adapter.list.submitList(listUserIsHead)
            binding.pbClubs.gone()
        }
    }

    private fun initView() {
        binding.rvClubs.adapter = adapter
    }

    private fun onClick(){
        binding.tvAllClubs.setOnClickListener{
            mainReplaceFragment((requireActivity() as MainActivity) , ClubsListFragment())
        }
    }

}