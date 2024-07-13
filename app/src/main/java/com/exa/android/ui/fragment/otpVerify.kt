package com.exa.android.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.exa.android.ui.R
import com.exa.android.ui.databinding.OtpFragmentBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.crashlytics.internal.Logger.TAG

class otpVerify : Fragment() {

    private var _binding: OtpFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private lateinit var verificationId: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = OtpFragmentBinding.inflate(layoutInflater)

        auth = FirebaseAuth.getInstance()
        val phoneNumber = arguments?.getString("PhoneNumber").toString()
        verificationId = arguments?.getString("VerificationId").toString()
        binding.phoneNumberSelected.text = phoneNumber

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.phoneNumberSelected.setOnClickListener {
            loadFragment(signIn())
        }

        binding.btnSend.setOnClickListener {
            val otp = binding.PinView.text.toString()
            if (otp.length == 6) {
                verifyPhoneNumberWithCode(verificationId, otp)
            } else {
                Toast.makeText(requireContext(), "Please enter a valid OTP", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun verifyPhoneNumberWithCode(verificationId: String, code: String) {
        val credential = PhoneAuthProvider.getCredential(verificationId, code)
        signInWithPhoneAuthCredential(credential)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithCredential:success")
                    loadFragment(verification())
                } else {
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        Toast.makeText(requireContext(), "Invalid OTP", Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }

    private fun loadFragment(fragment: Fragment) {
        val fragmentTag = fragment.javaClass.name
        parentFragmentManager.beginTransaction()
            .replace(R.id.container, fragment, fragmentTag)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
