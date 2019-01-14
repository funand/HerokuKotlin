package com.example.prince.herokukotlin
import android.arch.lifecycle.LiveData
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.widget.SearchView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity(), MovieDataAdapter.Listener, SearchView.OnQueryTextListener {

    override fun onQueryTextSubmit(query: String): Boolean {
        return false
    }

    override fun onQueryTextChange(query: String): Boolean {
        val textEntered = query
        return mAdapter!!.findThis(textEntered)

    }

    private var BASE_URL = "https://movies-sample.herokuapp.com/api/"

    var mCompositeDisposable: CompositeDisposable? = null

    private var mAdapter: MovieDataAdapter? = null
    private var mDataSet: ArrayList<MovieDataClass>? = null
    private var searchView : SearchView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mCompositeDisposable = CompositeDisposable()

        initRecyclerView()

        loadJSON()

        searchView = findViewById<SearchView>(R.id.search)
        searchView?.setOnQueryTextListener(this)
    }
    private fun initRecyclerView() {
        recycleview.setHasFixedSize(true)
        recycleview.setLayoutManager(GridLayoutManager(this, 3))
    }

    private fun loadJSON() {
        val requestInterface = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(MovieJSONInterface::class.java)

        mCompositeDisposable?.add(requestInterface.getMovies()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(this::handleResponse, this::handleError))
    }

    private fun handleResponse(dataArray: DataArray) {
        mDataSet = ArrayList(dataArray.data)
        mAdapter = MovieDataAdapter(mDataSet!!, this, mDataSet!!)

        recycleview.adapter = mAdapter
    }

    private fun handleError(error: Throwable) {

    }

    override fun onItemClick(movieDataClass: MovieDataClass) {

    }

    override fun onDestroy() {
        super.onDestroy()
        mCompositeDisposable?.clear()
    }

}
