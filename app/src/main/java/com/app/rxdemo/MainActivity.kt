package com.app.rxdemo


import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.rxdemo.adapter.RecyclerAdapter
import com.app.rxdemo.model.Post
import com.app.rxdemo.network.ServiceGenerator
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.*


class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"

    private var recyclerView: RecyclerView? = null

    private val disposables: CompositeDisposable = CompositeDisposable()
    private var adapter: RecyclerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView = findViewById(R.id.recycler_view)

        initRecyclerView()
        getObservers()

    }

    private fun getObservers(){
        getPostsObservable()
            ?.subscribeOn(Schedulers.io())
            ?.flatMap { post -> getCommentsObservable(post) }
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(object : Observer<Post?> {
                override fun onSubscribe(d: Disposable) {
                    disposables.add(d)
                }
                override fun onError(e: Throwable) {
                    Log.e(TAG, "onError: ", e)
                }
                override fun onComplete() {}
                override fun onNext(post: Post) {
                    updatePost(post)
                }
            })
    }

    private fun updatePost(p: Post) {
        Observable
            .fromIterable(adapter!!.getPosts())
            .filter { post -> post.id == p.id }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Post?> {
                override
                fun onNext(post: Post) {
                    Log.d(
                        TAG,
                        "onNext: updating post: " + post.id + ", thread: "
                                + Thread.currentThread().name
                    )
                    adapter!!.updatePost(post)
                }
                override
                fun onComplete() {}
                override fun onSubscribe(d: Disposable) {
                    disposables.add(d)
                }

                override fun onError(e: Throwable) {
                    Log.e(TAG, "onError: ", e)
                }
            })
    }

    private fun getPostsObservable(): Observable<Post?>? {
        return ServiceGenerator.providesRetrofit()
            .posts()
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.flatMap { posts ->
                val waypointList = posts.checkItemsAre<Post>() ?: return@flatMap null
                adapter?.setPosts(waypointList.toMutableList())
//                posts.toMutableList().let { adapter!!.setPosts(it) }
                Observable.fromIterable(posts)
                    .subscribeOn(Schedulers.io())
            }
    }


    private fun getCommentsObservable(post: Post): Observable<Post?>? {
        return ServiceGenerator.providesRetrofit()
            .getComments(post.id)
            ?.map { comments ->
                val delay: Int = (Random().nextInt(5) + 1) * 1000 // sleep thread for x ms
                Thread.sleep(delay.toLong())
                Log.d(
                    TAG,
                    "apply: sleeping thread " + Thread.currentThread().name +
                            " for " + delay.toString() + "ms"
                )
                post.setComments((comments))
                post
            }
            ?.subscribeOn(Schedulers.io())
    }

    private fun initRecyclerView() {
        adapter = RecyclerAdapter()
        recyclerView?.layoutManager = LinearLayoutManager(this)
        recyclerView?.adapter = adapter
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.clear()
    }

    @Suppress("UNCHECKED_CAST")
    inline fun <reified T : Any> List<*>.checkItemsAre() =
        if (all { it is T })
            this as List<T>
        else null


//    private fun sampleRX3Example(){
//        Observable.create<String> {
//            val webPageContent = URL("https://api.plos.org/search?q=title:DNA").readText()
//            it.onNext(webPageContent)
//        }.subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe({
//                d("sent", "success")
//                data.text = it
//            }, {
//                d("not sent", "error")
//            })
//    }
}