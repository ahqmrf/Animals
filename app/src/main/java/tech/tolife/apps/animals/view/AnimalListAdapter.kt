package tech.tolife.apps.animals.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item_animal.view.*
import tech.tolife.apps.animals.R
import tech.tolife.apps.animals.databinding.ListItemAnimalBinding
import tech.tolife.apps.animals.model.Animal
import tech.tolife.apps.animals.util.getProgressDrawable
import tech.tolife.apps.animals.util.loadImage

class AnimalListAdapter(private val animals: ArrayList<Animal>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    fun update(newAnimals: List<Animal>) {
        animals.clear()
        animals.addAll(newAnimals)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = DataBindingUtil.inflate<ListItemAnimalBinding>(
            LayoutInflater.from(parent.context),
            R.layout.list_item_animal,
            parent,
            false
        )
        return AnimalViewHolder(binding)
    }

    override fun getItemCount() = animals.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val animal = animals[position]
        val viewHolder = holder as? AnimalViewHolder ?: return
        viewHolder.itemView.setOnClickListener {
            val action = ListFragmentDirections.actionDetail(animal)
            Navigation.findNavController(it).navigate(action)
        }
        viewHolder.binding.animal = animal
    }

    class AnimalViewHolder(val binding: ListItemAnimalBinding) : RecyclerView.ViewHolder(binding.root)
}