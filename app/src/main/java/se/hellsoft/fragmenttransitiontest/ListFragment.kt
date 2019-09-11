package se.hellsoft.fragmenttransitiontest

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.ChangeBounds
import com.squareup.picasso.Picasso
import se.hellsoft.fragmenttransitiontest.databinding.FragmentListBinding
import se.hellsoft.fragmenttransitiontest.databinding.ImageItemBinding

class ListFragment : Fragment() {
    lateinit var binding: FragmentListBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navigation = object : Navigation {
            override fun navigateToImage(url: String, imageView: ImageView) {
                Log.d("ListFragment", "Navigating to $url with shared element: $imageView")
                val fragment = ImageFragment()
                fragment.sharedElementEnterTransition = ChangeBounds().also { it.duration = 5000L }
                fragment.sharedElementReturnTransition = ChangeBounds().also { it.duration = 5000L }
                fragment.arguments = Bundle().also { it.putString(ImageFragment.ARG_URL, url) }

                requireFragmentManager()
                    .beginTransaction()
                    .setReorderingAllowed(true)
                    .addSharedElement(imageView, getString(R.string.imageViewTransitionName))
                    .addToBackStack(null)
                    .replace(R.id.fragment_container, fragment)
                    .commit()
            }
        }
        binding.list.adapter = Adapter(navigation).also { it.submitList(items) }

    }

    companion object {
        val items = listOf(
            "https://images.unsplash.com/photo-1568197745148-a19e064969f4?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=2251&q=80",
            "https://images.unsplash.com/photo-1568143100438-58f5b47e0a9a?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=2251&q=80",
            "https://images.unsplash.com/photo-1568164528001-fdf61ebe1a7e?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=2251&q=80",
            "https://images.unsplash.com/photo-1568186704829-7daaf3327194?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=2250&q=80",
            "https://images.unsplash.com/photo-1568121596923-63a557f57d6c?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=2250&q=80"
        )
    }
}

interface Navigation {
    fun navigateToImage(url: String, imageView: ImageView)
}

class Adapter(val navigation: Navigation) : ListAdapter<String, ImageViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val imageItemBinding = ImageItemBinding.inflate(inflater, parent, false)
        return ImageViewHolder(imageItemBinding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val item = getItem(position)
        holder.bindTo(item)
        holder.binding.imageView.setOnClickListener {
            navigation.navigateToImage(item, holder.binding.imageView)
        }
    }

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean =
                oldItem == newItem
        }
    }
}

class ImageViewHolder(val binding: ImageItemBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bindTo(url: String) {
        Picasso.get()
            .load(url)
            .into(binding.imageView)
    }
}