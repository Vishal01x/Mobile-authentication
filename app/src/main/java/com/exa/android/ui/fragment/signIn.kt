package com.exa.android.ui.fragment

import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.exa.android.ui.R
import com.exa.android.ui.Utils.Constants
import com.exa.android.ui.ViewModel.AuthViewModel
import com.exa.android.ui.databinding.FragmentSignInBinding
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.crashlytics.internal.Logger.TAG
import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.google.i18n.phonenumbers.Phonenumber
import java.util.concurrent.TimeUnit

class signIn : Fragment() {

    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!
    private val authViewModel: AuthViewModel by activityViewModels()
    private lateinit var auth: FirebaseAuth
    private lateinit var storedVerificationId: String
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignInBinding.inflate(layoutInflater)

        auth = FirebaseAuth.getInstance()

        authViewModel.phoneNumber.observe(viewLifecycleOwner, Observer {
            binding.etPhoneNumber.text = Editable.Factory.getInstance().newEditable(it)
        })

        authViewModel.countrySelected.observe(viewLifecycleOwner, Observer {
            binding.ccp.setCountryForNameCode(it)
        })

        binding.ccp.registerCarrierNumberEditText(binding.etPhoneNumber)
        binding.btnSend.setOnClickListener {
            verifyNumber()
        }
        return binding.root
    }

    private fun verifyNumber() {
        val phoneNumber = binding.etPhoneNumber.text.toString().trim()
        val selectedCountryCode = binding.ccp.selectedCountryCodeWithPlus

        if (phoneNumber.isNotEmpty()) {
            val fullNumber = selectedCountryCode + phoneNumber
            if (isValidPhoneNumber(fullNumber, binding.ccp.selectedCountryNameCode)) {
                authViewModel.setPhoneNumber(phoneNumber)
                authViewModel.setCountrySelected(binding.ccp.selectedCountryNameCode)
                startPhoneNumberVerification(fullNumber)
            } else {
                binding.etPhoneNumber.error = "Invalid phone number"
            }
        } else {
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

    private fun startPhoneNumberVerification(phoneNumber: String) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(requireActivity())
            .setCallbacks(callbacks)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            Log.d(TAG, "onVerificationCompleted:$credential")
        }

        override fun onVerificationFailed(e: FirebaseException) {
            Log.w(TAG, "onVerificationFailed", e)
            when (e) {
                is FirebaseAuthInvalidCredentialsException -> {
                    binding.etPhoneNumber.error = "Invalid phone number"
                }
                is FirebaseTooManyRequestsException -> {
                    binding.etPhoneNumber.error = "SMS quota exceeded"
                }
                else -> {
                    binding.etPhoneNumber.error = e.localizedMessage
                }
            }
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken
        ) {
            Log.d(TAG, "onCodeSent:$verificationId")
            storedVerificationId = verificationId
            resendToken = token
            loadFragment(binding.etPhoneNumber.text.toString(), verificationId)
        }
    }

    private fun loadFragment(phoneNumber: String, verificationId: String) {
        val bundle = Bundle().apply {
            putString("PhoneNumber", phoneNumber)
            putString("VerificationId", verificationId)
        }
        val fragment = otpVerify()
        fragment.arguments = bundle
        Constants.currentFragment = fragment.javaClass.name
        parentFragmentManager.beginTransaction()
            .replace(R.id.container, fragment, Constants.currentFragment)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
