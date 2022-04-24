package com.app.rxdemo.model

import com.google.gson.annotations.SerializedName

import com.google.gson.annotations.Expose


class Comment(
    @field:SerializedName("postId") @field:Expose var postId: Int,
    @field:SerializedName(
        "id"
    ) @field:Expose var id: Int,
    @field:SerializedName("name") @field:Expose var name: String,
    @field:SerializedName(
        "email"
    ) @field:Expose var email: String,
    @field:SerializedName("body") @field:Expose var body: String
)