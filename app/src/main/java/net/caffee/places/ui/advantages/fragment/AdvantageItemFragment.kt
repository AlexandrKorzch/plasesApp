package net.caffee.places.ui.advantages.fragment

import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import net.caffee.places.databinding.FragmentAdvantagesItemBinding


class AdvantageItemFragment : Fragment() {

    companion object {
        private const val TAG = "AdvantageItemFragment"
        const val TITLE = "title"
        const val TEXT = "text"
        const val URL = "imageUrl"
        const val PATH = "imagePath"
        const val POSITION = "position"
        fun getInstance(title: String?,
                        text: String?,
                        imageUrl: String?,
                        imagePath: String?,
                        position: Int): AdvantageItemFragment {
            val args = Bundle().apply {
                putString(TITLE, title)
                putString(TEXT, text)
                putString(URL, imageUrl)
                putString(PATH, imagePath)
                putInt(POSITION, position)
            }
            return AdvantageItemFragment().apply {
                arguments = args
            }
        }
    }

    private var mTitle: String? = null
    private var mText: String? = null
    private var mImageUrl: String? = null
    private var mImagePath: String? = null
    private var mPosition: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments.let {
            mTitle = arguments?.getString(TITLE)
            mText = arguments?.getString(TEXT)
            mImageUrl = arguments?.getString(URL)
            mImagePath = arguments?.getString(PATH)
            mPosition = arguments?.getInt(POSITION)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentAdvantagesItemBinding.inflate(layoutInflater)
        val counter = "${(mPosition?.plus(1))}/4"
        binding?.tvCounter?.text = counter
        binding?.tvTitle?.text = mTitle
        binding?.tvText?.text = mText

        if (!TextUtils.isEmpty(mImagePath)) {
            binding?.ivImage?.setImageBitmap(BitmapFactory.decodeFile(mImagePath))
        } else {
            Glide.with(this)
                    .load(mImageUrl)
                    .into(binding?.ivImage!!)
        }
        return binding?.root
    }
}