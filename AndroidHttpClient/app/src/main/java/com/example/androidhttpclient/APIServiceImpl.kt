package com.example.androidhttpclient

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class APIServiceImpl (private val client : HttpClient) : APIService{

    private val baseUrl = "https://jsonplaceholder.typicode.com/"

    override suspend fun getPosts(): List<Post> {
        return client.get("$baseUrl/posts").body()
    }

    override suspend fun createPost(post: Post): Post {
        return client.post("$baseUrl/posts") {
            contentType(ContentType.Application.Json)
            setBody(post)
        }.body()
    }

    override suspend fun updatePost(
        id: Int,
        post: Post
    ): Post {
        return client.post("$baseUrl/posts/$id") {
            contentType(ContentType.Application.Json)
            setBody(post)
        }.body()
    }

    override suspend fun deletePost(id: Int): Boolean {
        client.delete("$baseUrl/posts/$id")
        return true
    }
}