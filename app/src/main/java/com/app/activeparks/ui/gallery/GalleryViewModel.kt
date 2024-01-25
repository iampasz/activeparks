package com.app.activeparks.ui.gallery

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.activeparks.data.model.gallery.PhotoItem

class GalleryViewModel : ViewModel() {
    val photoItemList = MutableLiveData<List<PhotoItem>>()
    val photoOfficialItemList = MutableLiveData<List<PhotoItem>>()
}