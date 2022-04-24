package com.app.rxdemo.network

import com.app.rxdemo.model.Comment
import retrofit2.http.GET
import com.app.rxdemo.model.Post
import io.reactivex.Observable
import retrofit2.http.Path


interface RequestApi {
//    @get:GET("posts")
//    val posts: Observable<List<Post?>?>?

    @GET("posts")
    fun posts(): Observable<List<Post?>?>?

    @GET("posts/{id}/comments")
    fun getComments(
        @Path("id") id: Int
    ): Observable<List<Comment?>?>?
}