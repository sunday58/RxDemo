package com.app.rxdemo.adapter

import com.app.rxdemo.model.Post


import android.widget.ProgressBar

import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView

import android.view.LayoutInflater
import android.view.View

import android.view.ViewGroup
import androidx.annotation.NonNull
import com.app.rxdemo.R


class RecyclerAdapter : RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>() {
    private var posts: MutableList<Post?> = ArrayList()

    @NonNull
    override fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.layout_post_list_item, null, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(@NonNull holder: MyViewHolder, position: Int) {
        holder.bind(posts[position])
    }

    override fun getItemCount(): Int {
        return posts.size
    }

    fun setPosts(posts: MutableList<Post?>) {
        this.posts = posts
        notifyDataSetChanged()
    }

    fun updatePost(post: Post) {
        posts[posts.indexOf(post)] = post
        notifyItemChanged(posts.indexOf(post))
    }

    fun getPosts(): List<Post?> {
        return posts
    }

    inner class MyViewHolder(@NonNull itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView = itemView.findViewById(R.id.title)
        private var numComments: TextView = itemView.findViewById(R.id.num_comments)
        var progressBar: ProgressBar = itemView.findViewById(R.id.progress_bar)
        fun bind(post: Post?) {
            title.text = post?.title
            if (post?.getComments() == null) {
                showProgressBar(true)
                numComments.text = ""
            } else {
                showProgressBar(false)
                numComments.text = post.getComments().size.toString()
            }
        }

        private fun showProgressBar(showProgressBar: Boolean) {
            if (showProgressBar) {
                progressBar.visibility = View.VISIBLE
            } else {
                progressBar.visibility = View.GONE
            }
        }

    }

    companion object {
        private const val TAG = "RecyclerAdapter"
    }
}