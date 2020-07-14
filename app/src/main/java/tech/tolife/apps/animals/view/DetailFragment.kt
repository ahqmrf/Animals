package tech.tolife.apps.animals.view

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition

import tech.tolife.apps.animals.R
import tech.tolife.apps.animals.databinding.FragmentDetailBinding
import tech.tolife.apps.animals.model.AnimalPalette

/**
 * A simple [Fragment] subclass.
 */
class DetailFragment : Fragment() {

    private lateinit var binding: FragmentDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            val animal = DetailFragmentArgs.fromBundle(it).animal
            binding.animal = animal
            setBackgroundColor(animal.imageUrl)
        }
    }

    private fun setBackgroundColor(url: String?) {
        url ?: return
        Glide.with(this)
            .asBitmap()
            .load(url)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    Palette.from(resource).generate {
                        val color = it?.lightMutedSwatch?.rgb ?: 0
                        binding.palette = AnimalPalette(color)
                    }
                }

                override fun onLoadCleared(placeholder: Drawable?) {

                }
            })
    }

}
