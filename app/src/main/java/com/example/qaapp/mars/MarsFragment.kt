package com.example.qaapp.mars

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.example.qaapp.databinding.FragmentMarsBinding

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


        return binding.root
    }

}




