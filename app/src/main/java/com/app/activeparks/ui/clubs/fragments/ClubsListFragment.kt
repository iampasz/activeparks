package com.app.activeparks.ui.clubs.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.app.activeparks.MainActivity
import com.app.activeparks.ui.clubs.ClubsViewModelKT
import com.app.activeparks.ui.event.activity.ClubFragment
import com.app.activeparks.ui.homeWithUser.fragments.clubs.HomeClubsAdapter
import com.app.activeparks.util.extention.mainAddFragment
import com.app.activeparks.util.extention.removeFragment
import com.technodreams.activeparks.databinding.FragmentClubsListBinding
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class ClubsListFragment : Fragment() {

    lateinit var binding: FragmentClubsListBinding
    val adapterPublished = HomeClubsAdapter {
        openClubFragment(it.id)
    }

    val adapterUserIsHead = HomeClubsAdapter {
        openClubFragment(it.id)
    }

    val adapterUserIsMember = HomeClubsAdapter {
        openClubFragment(it.id)
    }
    private val clubsViewModelKT: ClubsViewModelKT by activityViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentClubsListBinding
            .inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.close.setOnClickListener {
            parentFragmentManager.removeFragment(this)
        }

        clubsViewModelKT.getCombinatedClubList("5c4bc22a-7c74-431b-9a5a-c1ae1a28ce03")
        initView()
        observe()
    }

    private fun observe() {

        clubsViewModelKT.clubList.observe(viewLifecycleOwner){v->
            val listPublished = v.items.published
            val listUserIsHead = v.items.userIsHead
            val listUserIsMember = v.items.userIsMember

            adapterPublished.list.submitList(listPublished)
            adapterUserIsHead.list.submitList(listUserIsHead)
            adapterUserIsMember.list.submitList(listUserIsMember)

        }
    }

    private fun initView() {
        binding.rvAllClubs.adapter = adapterPublished
        binding.rvMyClubs.adapter = adapterUserIsHead
        binding.rvClubs.adapter = adapterUserIsMember
    }

    private fun openClubFragment(id:String){
        val bundle = Bundle()
        bundle.putString("CLUB_ID", id)
        val clubFragment = ClubFragment()
        clubFragment.arguments = bundle
        mainAddFragment((requireActivity() as MainActivity), clubFragment)
    }

}