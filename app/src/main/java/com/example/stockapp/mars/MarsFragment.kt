package com.example.stockapp.mars

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.stockapp.R
import com.example.stockapp.databinding.FragmentMarsBinding
import com.example.stockapp.network.MarsApiFilter

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [MarsFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [MarsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MarsFragment : Fragment() {

    private val viewModel: MarsViewModel by lazy {
        ViewModelProviders.of(this).get(MarsViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        //return inflater.inflate(com.example.qaapp.R.layout.fragment_mars, parent, false);

        var binding = FragmentMarsBinding.inflate(inflater)

        binding.setLifecycleOwner(this)

        binding.viewModel = viewModel

        setHasOptionsMenu(true)

        return binding.root
    }

    /**
     * Inflates the overflow menu that contains filtering options.
     */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        viewModel.updateFilter(
            when (item.itemId) {
                R.id.show_rent_menu -> MarsApiFilter.SHOW_RENT
                R.id.show_buy_menu -> MarsApiFilter.SHOW_BUY
                else -> MarsApiFilter.SHOW_ALL
            }
        )
        return true
    }

}




