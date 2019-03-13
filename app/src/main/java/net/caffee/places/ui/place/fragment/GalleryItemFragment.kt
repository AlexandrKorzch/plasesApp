package net.caffee.places.ui.place.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import net.caffee.places.databinding.FragmentPlaceGalleryItemBinding

class GalleryItemFragment : Fragment() {

    companion object {
        private const val TAG = "GalleryItemFragment"
        private const val IMAGE = "image"
        fun getInstance(image: String): GalleryItemFragment {
            val args = Bundle().apply {putString(IMAGE, image)}
            return GalleryItemFragment().apply {
                arguments = args
            }
        }
    }

    var image: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments.let {
            image = it?.getString(IMAGE)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentPlaceGalleryItemBinding.inflate(layoutInflater)
        binding?.let {
            Glide.with(it.root.context)
                    .load(image)
                    .into(it.ivGallery)
        }
        return binding?.root
    }
}