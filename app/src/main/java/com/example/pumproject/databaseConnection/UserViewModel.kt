package com.example.pumproject.databaseConnection
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserViewModel : ViewModel(){
    private val repository = UserRepository()
    private val _user = MutableStateFlow(emptyList< User>())
    val user : StateFlow<List<User>> = _user


    init{}

    fun getUser(name: String){
        viewModelScope.launch { _user.value = repository.getUser(name) }
    }
}