package com.app.activeparks.ui.news.fragments

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.graphics.drawable.LevelListDrawable
import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.app.activeparks.ui.news.BlogViewModel
import com.app.activeparks.ui.news.util.NewsTypes
import com.app.activeparks.util.LoadImage
import com.app.activeparks.util.extention.gone
import com.app.activeparks.util.extention.removeFragment
import com.bumptech.glide.Glide
import com.technodreams.activeparks.R
import com.technodreams.activeparks.databinding.FragmentBlogBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

@Suppress("DEPRECATION")
class BlogFragment : Fragment(), Html.ImageGetter {

    private lateinit var binding: FragmentBlogBinding
    private val blogViewModel: BlogViewModel by viewModel()
    private lateinit var blogId: String
    private lateinit var clubId: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        blogId = arguments?.getString(NewsTypes.NEWS_ID.type, "") ?: ""
        clubId = arguments?.getString(NewsTypes.CLUB_ID.type, "") ?: ""

        binding = FragmentBlogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        update()
        initListener()
        observe()
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    private fun observe() {

        blogViewModel.newsDetails.observe(viewLifecycleOwner) { news ->

            if (news.imageUrl.isNotEmpty()) {
                Glide
                    .with(binding.include.photo.context)
                    .load(news.imageUrl)
                    .error(R.drawable.ic_prew)
                    .into(binding.include.photo)
            } else {
                binding.include.photo.gone()
            }

            var web =
                "<html><head><LINK href=\"https://ap.sportforall.gov.ua/images/index.css\" rel=\"stylesheet\"/></head><body>" + news.body + "</body></html>"
            web = web.replace("<img ", "<br><img ").replace("<img>", "")
            binding.html.movementMethod = LinkMovementMethod.getInstance()
            binding.html.text = Html.fromHtml(web, this, null)
            binding.include.nameEvent.text = news.title


            val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            try {
                val date = format.parse(news.publishedAt)
                binding.include.date.text = (
                        "Опубліковано: " + date?.let {
                            SimpleDateFormat(
                                "dd MMMM yyyy",
                                Locale("uk", "UA")
                            ).format(it)
                        }
                        )
            } catch (e: ParseException) {
                e.printStackTrace()
            }
        }

        blogViewModel.newsClubDetails.observe(viewLifecycleOwner) {news ->
            news?.photo?.let {
                Glide
                    .with(binding.include.photo.context)
                    .load(news.photo)
                    .error(R.drawable.ic_prew)
                    .into(binding.include.photo)
            }
            var web =
                "<html><head><LINK href=\"https://ap.sportforall.gov.ua/images/index.css\" rel=\"stylesheet\"/></head><body>" + news.body + "</body></html>"
            web = web.replace("<img ", "<br><img ").replace("<img>", "")
            binding.html.movementMethod = LinkMovementMethod.getInstance()
            binding.html.text = Html.fromHtml(web, this, null)
            binding.include.nameEvent.text = news.title

            val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            try {
                val date = format.parse(news.publishedAt)
                binding.include.date.text = (
                        "Опубліковано: " + date?.let {
                            SimpleDateFormat(
                                "dd MMMM yyyy",
                                Locale("uk", "UA")
                            ).format(it)
                        }
                        )
            } catch (e: ParseException) {
                e.printStackTrace()
            }
        }
    }

    private fun initListener() {
        binding.close.setOnClickListener {
            parentFragmentManager.removeFragment(this)
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun getDrawable(source: String?): Drawable {
        val d = LevelListDrawable()
        val empty = resources.getDrawable(R.drawable.logo_active_parks)
        d.addLevel(0, 0, empty)
        d.setBounds(0, 0, empty.intrinsicWidth, empty.intrinsicHeight)
        LoadImage(binding.html.width).setListener {
            val t: CharSequence = binding.html.text
            binding.html.text = t
        }.execute(source, d)
        return d
    }

    private fun update(){
        if (clubId.isEmpty()) {
            blogViewModel.getNewsDetails(blogId)
        } else {
            blogViewModel.getClubNewsDetails(clubId, blogId)
        }
    }
}