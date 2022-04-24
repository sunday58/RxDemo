package com.app.rxdemo.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class Post(
    @field:Expose @field:SerializedName("userId") var userId: Int,
    @field:Expose @field:SerializedName(
        "id"
    ) var id: Int,
    @field:Expose @field:SerializedName("title") var title: String,
    @field:Expose @field:SerializedName(
        "body"
    ) var body: String,
    comments: List<Comment>
) {

    private var comments: List<Comment?>
    fun getComments(): List<Comment?> {
        return comments
    }

    fun setComments(comments: List<Comment?>) {
        this.comments = comments
    }

    override fun toString(): String {
        return "Post{" +
                "userId=" + userId +
                ", id=" + id +
                ", title='" + title + '\'' +
                ", body='" + body + '\'' +
                '}'
    }

    init {
        this.comments = comments
    }
}