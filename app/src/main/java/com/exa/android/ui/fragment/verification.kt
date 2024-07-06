package com.exa.android.ui.fragment

import android.os.Bundle
import android.os.Looper
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.exa.android.ui.HomeFragment
import com.exa.android.ui.R
import com.exa.android.ui.Utils.Constants.currentFragment
import com.exa.android.ui.databinding.FragmentVerificationBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class verification : Fragment() {

    private var _binding : FragmentVerificationBinding ? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentVerificationBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.btnVerify.setOnClickListener {
            val fName = binding.firstName.text.toString()
            val lName = binding.lastName.text.toString()
            val email = binding.emailAdd.text.toString()

            if (fName.isEmpty()) {
                binding.firstName.error = "Please Enter First Name"
            } else if (lName.isEmpty()) {
                binding.lastName.error = "Please Enter Last Name"
            } else if (!isValidEmail(email)) {
                binding.emailAdd.error = "Please Enter Valid Email"
            } else {
                verifyUser(fName,lName,email)
            }
        }
    }
    fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun verifyUser(f:String, l:String,e:String){

        binding.details.visibility = View.GONE
        binding.verify.visibility = View.VISIBLE

        CoroutineScope(Dispatchers.Main).launch {
            delay(3000)
            val fragment = HomeFragment()
            val bundle = Bundle().apply {
                putString("name", f +" "+ l)
            }
            fragment.arguments = bundle
            currentFragment = fragment.javaClass.name
            parentFragmentManager.beginTransaction()
                .replace(R.id.container, fragment, currentFragment)
                .commit()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}