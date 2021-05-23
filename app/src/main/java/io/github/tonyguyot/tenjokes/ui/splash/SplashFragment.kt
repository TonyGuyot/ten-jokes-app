package io.github.tonyguyot.tenjokes.ui.splash

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import io.github.tonyguyot.tenjokes.databinding.FragmentSplashBinding

/**
 * Display the splash screen.
 *
 * Use the [SplashFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SplashFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // inflate the layout for this fragment
        val binding = FragmentSplashBinding.inflate(inflater, container, false)
        context ?: return binding.root

        // retrieve or create the associated view model
        val viewModel: SplashViewModel by viewModels {
            val sharedPrefs = activity?.getPreferences(Context.MODE_PRIVATE)
            SplashViewModel.Factory(sharedPrefs)
        }
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        // subscribe to data changes
        viewModel.counter.observe(viewLifecycleOwner) { counter ->
            // if the counter has been retrieved, we can animate it
            if (!counter.isNullOrEmpty()) performAnimation(binding.appStartCounter)
        }
        viewModel.navigate.observe(viewLifecycleOwner) { navigate ->
            // it's time to go to the main screen
            if (navigate) gotoNextScreen()
        }

        // return the root view
        return binding.root
    }

    private fun performAnimation(view: View) {
        // scale the view up to 3x its default size and back (do it twice)
        val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 3f)
        val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 3f)
        val animator = ObjectAnimator.ofPropertyValuesHolder(view, scaleX, scaleY)
        animator.repeatCount = 2
        animator.repeatMode = ObjectAnimator.REVERSE
        animator.duration = 1000L
        animator.start()
    }

    private fun gotoNextScreen() {
        val action = SplashFragmentDirections.actionSplashFragmentToMainFragment()
        findNavController().navigate(action)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of this fragment.
         *
         * @return A new instance of fragment [SplashFragment].
         */
        @JvmStatic
        fun newInstance() = SplashFragment()
    }
}