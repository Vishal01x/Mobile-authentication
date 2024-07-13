package com.exa.android.ui.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AuthViewModel : ViewModel() {
    private val _phoneNumber = MutableLiveData<String>()
    val phoneNumber : LiveData<String>
        get() = _phoneNumber

    private val _countrySelected = MutableLiveData<String>()
    val countrySelected : LiveData<String>
        get() = _countrySelected

    fun setPhoneNumber(pNumber: String){
        _phoneNumber.value = pNumber
    }

    fun setCountrySelected(country : String){
        _countrySelected.value = country
    }
}