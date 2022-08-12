package com.example.myapplication.ui

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import androidx.fragment.app.Fragment
import com.example.myapplication.databinding.MainFragmentBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainFragment: Fragment() {

    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!

    //Receiver
    private val openContacts = registerForActivityResult(ActivityResultContracts.PickContact()) {

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btClick.setOnClickListener {
            showContacts()
        }
    }

    private fun startAnimation() {
        val objectAnimator = ObjectAnimator.ofFloat(binding.btClick, "rotation", 0f, 360f)
        objectAnimator.duration = 2000
        objectAnimator.doOnStart {
            binding.btClick.isClickable = false
        }
        objectAnimator.doOnEnd {
            binding.btClick.isClickable = true
        }
        objectAnimator.start()
    }

    private fun showToastMessage() {
        Toast.makeText(context, "Action is Toast!", Toast.LENGTH_LONG).show()
    }

    private fun showContacts() {
        val intent = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)

        openContacts.launch(null)
    }

    private fun showNotification() {

    }
}