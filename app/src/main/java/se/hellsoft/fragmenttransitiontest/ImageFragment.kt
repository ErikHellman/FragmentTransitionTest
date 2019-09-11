package se.hellsoft.fragmenttransitiontest

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import se.hellsoft.fragmenttransitiontest.databinding.FragmentItemBinding

class ImageFragment : Fragment() {
    lateinit var binding: FragmentItemBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        val url = arguments?.getString(ARG_URL) ?: throw IllegalArgumentException("Missing URL")
        Picasso.get()
            .load(url)
            .into(binding.imageView, object : Callback {
                override fun onSuccess() {
                    Log.d("ImageFragment", "onSuccess")
                    (view.parent as ViewGroup).doOnPreDraw {
                        startPostponedEnterTransition()
                    }
                    binding.textView.text = "Image Loaded!"
                }

                override fun onError(e: Exception?) {
                    Log.d("ImageFragment", "onError", e)
                }
            })
    }

    companion object {
        const val ARG_URL = "url"
    }
}