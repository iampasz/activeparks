package com.app.activeparks.ui.participants

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.app.activeparks.data.model.user.UserParticipants
import com.app.activeparks.ui.participants.adapter.UsersAdaper
import com.app.activeparks.ui.participants.adapter.UsersAdaper.UsersListener
import com.app.activeparks.ui.people.UserActivity
import com.app.activeparks.util.extention.StringTypes
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.technodreams.activeparks.R
import com.technodreams.activeparks.databinding.FragmentUsersBinding
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class ParticipantsFragment : Fragment() {

    lateinit var binding: FragmentUsersBinding
    private val viewModelPart: ParticipantsViewModel by activityViewModel()
    private val isAdmin = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUsersBinding
            .inflate(inflater, container, false)
        binding = FragmentUsersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        update()
        initObserve()
    }

    fun update() {
        if (viewModelPart.isEvent) {
            viewModelPart.getHeadsEventUsers()
            viewModelPart.getMembersEventUsers()
            if (isAdmin) {
                viewModelPart.getApplyingEventUsers()
            }
        } else {
            viewModelPart.getHeadsClubUsers()
            viewModelPart.getMembersClubUsers()
            if (isAdmin) {
                viewModelPart.getApplyingClubUsers()
            }
        }
    }

    fun initObserve() {
        //Clubs
        viewModelPart.getUsersListHeadsClub.observe(viewLifecycleOwner) { item: UserParticipants ->
            binding.listNull.visibility = if (item.items.size > 0) View.GONE else View.VISIBLE
            binding.listHeads.adapter = UsersAdaper(
                activity,
                item.items,
                true,
                if (isAdmin) 1 else 0
            ).setOnClubsListener(object : UsersListener {
                override fun onInfo(id: String) {
                    startActivity(
                        Intent(activity, UserActivity::class.java).putExtra(
                            StringTypes.ID.type,
                            id
                        )
                    )
                }

                override fun approve(user: String) {}
                override fun reject(user: String) {
                    dialogShow(true)
                }
            })
        }

        viewModelPart.getUsersListApplyingClub.observe(viewLifecycleOwner) { item: UserParticipants ->
            binding.listNull.visibility = if (item.items.size > 0) View.GONE else View.VISIBLE
            binding.listApplying.adapter = UsersAdaper(
                activity,
                item.items,
                true,
                if (isAdmin) 1 else 0
            ).setOnClubsListener(object : UsersListener {
                override fun onInfo(id: String) {
                    startActivity(
                        Intent(activity, UserActivity::class.java).putExtra(
                            StringTypes.ID.type,
                            id
                        )
                    )
                }

                override fun approve(user: String) {}
                override fun reject(user: String) {
                    dialogShow(true)
                }
            })
        }

        viewModelPart.getUsersListMembersClub.observe(viewLifecycleOwner) { item: UserParticipants ->
            binding.listTitleTwo.visibility =
                if (item.items.size > 0) View.VISIBLE else View.GONE
            binding.listMembers.adapter = UsersAdaper(
                activity,
                item.items,
                true,
                if (isAdmin) 1 else 0
            ).setOnClubsListener(object : UsersListener {
                override fun onInfo(id: String) {
                    startActivity(
                        Intent(activity, UserActivity::class.java).putExtra(
                            StringTypes.ID.type,
                            id
                        )
                    )
                }

                override fun approve(user: String) {}
                override fun reject(user: String) {
                    dialogShow(false)
                }
            })
        }

        //Events
        viewModelPart.getUsersListHeadsEvents.observe(viewLifecycleOwner) { item: UserParticipants ->

            binding.listNull.visibility = if (item.items.size > 0) View.GONE else View.VISIBLE
            binding.listHeads.adapter = UsersAdaper(
                activity,
                item.items,
                true,
                if (isAdmin) 1 else 0
            ).setOnClubsListener(object : UsersListener {
                override fun onInfo(id: String) {
                    startActivity(
                        Intent(activity, UserActivity::class.java).putExtra(
                            StringTypes.ID.type,
                            id
                        )
                    )
                }

                override fun approve(user: String) {}
                override fun reject(user: String) {
                    dialogShow(true)
                }
            })
        }

        viewModelPart.getUsersListApplyingEvents.observe(viewLifecycleOwner) { item: UserParticipants ->
            binding.listNull.visibility = if (item.items.size > 0) View.GONE else View.VISIBLE
            binding.listApplying.adapter = UsersAdaper(
                activity,
                item.items,
                true,
                if (isAdmin) 1 else 0
            ).setOnClubsListener(object : UsersListener {
                override fun onInfo(id: String) {
                    startActivity(
                        Intent(activity, UserActivity::class.java).putExtra(
                            StringTypes.ID.type,
                            id
                        )
                    )
                }

                override fun approve(user: String) {}
                override fun reject(user: String) {
                    dialogShow(true)
                }
            })
        }

        viewModelPart.getUsersListMembersEvents.observe(viewLifecycleOwner) { item: UserParticipants ->
            binding.listTitleTwo.visibility =
                if (item.items.size > 0) View.VISIBLE else View.GONE
            binding.listMembers.adapter = UsersAdaper(
                activity,
                item.items,
                true,
                if (isAdmin) 1 else 0
            ).setOnClubsListener(object : UsersListener {
                override fun onInfo(id: String) {
                    startActivity(
                        Intent(activity, UserActivity::class.java).putExtra(
                            StringTypes.ID.type,
                            id
                        )
                    )
                }

                override fun approve(user: String) {}
                override fun reject(user: String) {
                    dialogShow(false)
                }
            })
        }
    }

    fun dialogShow(type: Boolean) {
        val dialog = BottomSheetDialog(requireContext(), R.style.CustomBottomSheetDialogTheme)
        dialog.setContentView(R.layout.dialog_participants)
        val updateAction = dialog.findViewById<TextView>(R.id.update_action)
        updateAction?.text = if (type) "Зняти організатора" else "Зробити організатором"
        updateAction?.setOnClickListener {

            dialog.dismiss()
            Handler(Looper.getMainLooper())
                .postDelayed({ update() }, 500)
        }
        val removeAction = dialog.findViewById<TextView>(R.id.remove_action)
        removeAction?.setOnClickListener {
            dialog.dismiss()
            Handler(Looper.getMainLooper())
                .postDelayed({ update() }, 500)
        }
        val cancel = dialog.findViewById<TextView>(R.id.cancel)!!
        cancel.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }
}