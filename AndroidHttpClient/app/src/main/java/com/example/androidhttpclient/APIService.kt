package com.example.androidhttpclient

interface APIService {

    suspend fun getPosts(): List<Post>

    suspend fun createPost(post: Post): Post

    suspend fun updatePost(id: Int, post: Post): Post

    suspend fun deletePost(id: Int) : Boolean
}