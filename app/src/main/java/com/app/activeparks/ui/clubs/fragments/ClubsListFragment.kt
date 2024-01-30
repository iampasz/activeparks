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
    val adapter = HomeClubsAdapter{

        val bundle = Bundle()
        bundle.putString("CLUB_ID", it.id)
        val clubFragment = ClubFragment()
        clubFragment.arguments = bundle

        mainAddFragment((requireActivity() as MainActivity), clubFragment)
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

        binding.close.setOnClickListener{
            parentFragmentManager.removeFragment(this)
        }

        clubsViewModelKT.getClubs()
        initView()
        observe()
    }

    private fun observe() {
        with(clubsViewModelKT) {
            clubList.observe(viewLifecycleOwner) { response ->
                response?.takeIf { it.isNotEmpty() }
                    ?.apply {
                        adapter.list.submitList(this)
                    } ?: kotlin.run {
                }
            }
        }
    }

    private fun initView() {
        binding.rvClubs.adapter = adapter
    }

}