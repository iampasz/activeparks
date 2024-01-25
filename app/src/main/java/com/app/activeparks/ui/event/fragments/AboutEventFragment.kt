package com.app.activeparks.ui.event.fragments

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.app.activeparks.data.model.sportevents.ItemEvent
import com.app.activeparks.ui.event.viewmodel.EventViewModel
import com.technodreams.activeparks.databinding.FragmentAboutEventBinding
import org.koin.androidx.viewmodel.ext.android.activityViewModel

@Suppress("DEPRECATION")
class AboutEventFragment : Fragment() {

    lateinit var binding: FragmentAboutEventBinding
    private val viewModel: EventViewModel by activityViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAboutEventBinding
            .inflate(inflater, container, false)

        return binding.root
    }

    @SuppressLint("ObsoleteSdkInt")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.eventDetails.observe(
            viewLifecycleOwner
        ) { events: ItemEvent ->
            if (events.fullDescription != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    binding.description.text = Html.fromHtml(events.fullDescription, Html.FROM_HTML_MODE_COMPACT)
                } else {
                    binding.description.text = Html.fromHtml(events.fullDescription)
                }
            }
        }
    }
}