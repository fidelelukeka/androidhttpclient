package com.example.androidhttpclient

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class MainViewModel : ViewModel() {
    private val client = HttpClient(Android){
        install(ContentNegotiation){
            json(
                Json{
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                }
            )
        }
    }
    private val apiService = APIServiceImpl(client)

    private var _isLoading = mutableStateOf(false)
    val isLoading : State<Boolean> = _isLoading

    private var _errorMessage : MutableState<String?> = mutableStateOf(null)
    val errorMessage : State<String?> = _errorMessage

    private var _posts : SnapshotStateList<Post> = mutableStateListOf()
    val posts : List<Post> = _posts

    private var _activePost : MutableState<Post> = mutableStateOf(Post())
    val activePost : State<Post> = _activePost

    fun fetchPosts(){
        try {
            viewModelScope.launch{
                _isLoading.value = true
                val post = apiService.getPosts()
                _posts.clear()
                _posts.addAll(post)
                _isLoading.value = false
            }
        }catch (e : Exception){
            _errorMessage.value = e.message
            _isLoading.value = false
        }
    }

    fun createPost(){
        try {
            viewModelScope.launch{
                _isLoading.value = true
                val postCreated = apiService.createPost(_activePost.value)
                _posts.add(postCreated)
                _isLoading.value = false
            }
        }catch (e : Exception){
            _errorMessage.value =  e.message
            _isLoading.value = false
        }
    }

    fun updatePost(){
        try {
            viewModelScope.launch{
                _isLoading.value = true
                val postUpdated = apiService.updatePost(_activePost.value.id!!, _activePost.value)
                val index = _posts.indexOfFirst { it.id == postUpdated.id }
                if(index != -1){
                    _posts[index] = postUpdated
                    _errorMessage.value = "Post with id " + postUpdated.id + " is updated"
                }
                _isLoading.value = false
            }
        }catch (e : Exception){
            _errorMessage.value =  e.message
            _isLoading.value = false
        }
    }

    fun deletePost(id : Int){
        try {
            viewModelScope.launch{
                _isLoading.value = true
                val isPostDeleted = apiService.deletePost(id)
                if(isPostDeleted){
                    _posts.removeIf { it.id == id }
                    _errorMessage.value = "Post with id $id is deleted"
                }
                _isLoading.value = false
            }
        }catch (e : Exception){
            _errorMessage.value =  e.message
            _isLoading.value = false
        }
    }
}