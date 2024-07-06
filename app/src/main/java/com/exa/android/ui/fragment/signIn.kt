package com.exa.android.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.exa.android.ui.R
import com.exa.android.ui.Utils.Constants
import com.exa.android.ui.databinding.FragmentSignInBinding
import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.google.i18n.phonenumbers.Phonenumber


class signIn : Fragment() {

    private var _binding : FragmentSignInBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       _binding = FragmentSignInBinding.inflate(layoutInflater)

        binding.ccp.registerCarrierNumberEditText(binding.etPhoneNumber)


        binding.btnSend.setOnClickListener {
            verifyNumber()
        }
        return binding.root
    }

    fun verifyNumber(){

        val phoneNumber = binding.etPhoneNumber.text.toString().trim()
        val selectedCountryCode = binding.ccp.selectedCountryCodeWithPlus

        if (phoneNumber.isNotEmpty()) {
            val fullNumber = selectedCountryCode + phoneNumber
            if (isValidPhoneNumber(fullNumber, binding.ccp.selectedCountryNameCode)) {
                val bundle = Bundle().apply {
                    putString("PhoneNumber", phoneNumber)
                }
                val fragment = otpVerify()
                fragment.arguments = bundle
                Constants.currentFragment = fragment.javaClass.name
                parentFragmentManager.beginTransaction()
                    .replace(R.id.container, fragment, Constants.currentFragment)
                    .commit()
            } else {
                // Show error message
                binding.etPhoneNumber.error = "Invalid phone number"
            }
        } else {
            // Show error message
            binding.etPhoneNumber.error = "Please enter a phone number"
        }
    }

    private fun isValidPhoneNumber(phoneNumber: String, countryCode: String): Boolean {
        val phoneUtil = PhoneNumberUtil.getInstance()
        return try {
            val numberProto: Phonenumber.PhoneNumber = phoneUtil.parse(phoneNumber, countryCode)
            phoneUtil.isValidNumber(numberProto)
        } catch (e: Exception) {
            false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}