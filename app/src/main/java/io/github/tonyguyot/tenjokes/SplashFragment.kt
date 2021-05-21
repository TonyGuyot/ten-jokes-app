package io.github.tonyguyot.tenjokes

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController

/**
 * A simple [Fragment] subclass.
 * Use the [SplashFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SplashFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_splash, container, false)

        // Retrieve or create the associated view model
        val sharedPrefs = activity?.getPreferences(Context.MODE_PRIVATE)
        val viewModel: SplashViewModel by viewModels {
            SplashViewModel.Factory(sharedPrefs)
        }

        // Subscribe to data changes
        val title = root.findViewById<TextView>(R.id.splashTitle)
        viewModel.counter.observe(viewLifecycleOwner) { counter ->
            title.text = counter
        }
        viewModel.navigate.observe(viewLifecycleOwner) { navigate ->
            if (navigate) gotoNextScreen()
        }

        // Return the root view
        return root
    }

    private fun gotoNextScreen() {
        val action = SplashFragmentDirections.actionSplashFragmentToMainFragment()
        findNavController().navigate(action)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of this fragment.
         *
         * @return A new instance of fragment SplashFragment.
         */
        @JvmStatic
        fun newInstance() = SplashFragment()
    }
}