package tech.tolife.apps.animals.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.fragment_list.*

import tech.tolife.apps.animals.R
import tech.tolife.apps.animals.viewmodel.ListViewModel

/**
 * A simple [Fragment] subclass.
 */
class ListFragment : Fragment() {

    private val animalListAdapter by lazy {
        AnimalListAdapter(arrayListOf())
    }

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(ListViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.animals.observe(viewLifecycleOwner, Observer {
            animalListAdapter.update(it)
        })

        viewModel.loading.observe(viewLifecycleOwner, Observer { loading ->
            loadingView.visibility = if(loading) View.VISIBLE else View.GONE
            if (loading) {
                loadError.visibility = View.GONE
            }
        })

        viewModel.loadError.observe(viewLifecycleOwner, Observer {
            loadError.visibility = if(it) View.VISIBLE else View.GONE
        })

        viewModel.refresh()

        animalList.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = animalListAdapter
        }

        swipeRefreshLayout.setOnRefreshListener {
            viewModel.refresh()
            swipeRefreshLayout.isRefreshing = false
        }
    }
}
