package com.exa.android.ui.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import com.exa.android.ui.R
import com.exa.android.ui.Utils.Constants
import com.exa.android.ui.databinding.OtpFragmentBinding


class otpVerify : Fragment() {

    private var _binding : OtpFragmentBinding ? = null
    private val binding get() = _binding!!

    lateinit var phoneNumber : String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = OtpFragmentBinding.inflate(layoutInflater)

        phoneNumber = arguments?.getString("PhoneNumber").toString()
        binding.phoneNumberSelected.text = phoneNumber

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAutoMove(binding.editText1, binding.editText2)
        setupAutoMove(binding.editText2, binding.editText3)
        setupAutoMove(binding.editText3, binding.editText4)
        setupAutoMove(binding.editText4, binding.editText5)

        // You can also add logic to validate the OTP here
        binding.btnSend.setOnClickListener {
            val otp = binding.editText1.text.toString() + binding.editText2.text.toString() + binding.editText3.text.toString() + binding.editText4.text.toString() + binding.editText5.text.toString()
            //Toast.makeText(requireContext(), "You are not yet Registered, Please Register yourself", Toast.LENGTH_SHORT).show()
            // Proceed with verification logic
            if(otp.length == 5) {
                val fragment = verification().javaClass.name

                parentFragmentManager.beginTransaction()
                    .replace(R.id.container, verification(), fragment)
                    .commit()
            }else if(otp.length == 0){
                Toast.makeText(requireContext(), "Please Enter OTP", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Please Enter Valid OTP", Toast.LENGTH_SHORT).show()
            }
        }



    }

    private fun setupAutoMove(currentEditText: EditText, nextEditText: EditText) {
        currentEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s != null && s.length == 1) {
                    nextEditText.requestFocus()
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}