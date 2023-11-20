package com.app.activeparks.ui.event

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

//https://ap-dev.sportforall.gov.ua/api/v1/sport-events/
// day?offset=0&filters[startsFrom]=2023-11-20&filters[startsTo]=2023-11-20&sort[startsAt]=asc

//https://ap-dev.sportforall.gov.ua/api/v1/sport-events?offset=0&limit=10&filters[startsFrom]=2023-11-20&sort[title]=desc
//https://ap-dev.sportforall.gov.ua/api/v1/sport-events?offset=0&limit=10&sort[sort_name]=value&filters[filter_name]=value


class FragmentCalendarEvent : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }


}