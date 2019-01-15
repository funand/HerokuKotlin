package com.example.prince.herokukotlin
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.widget.SearchView
import android.widget.Toast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


/*  App to  show movies from this api https://movies-sample.herokuapp.com/api/movies
    using recylcleview in a grid layout. App also allows  user to filter through data. to get specific movies
*/

class MainActivity : AppCompatActivity(), MovieDataAdapter.Listener, SearchView.OnQueryTextListener {

    private var BASE_URL = "https://movies-sample.herokuapp.com/api/"

    var mCompositeDisposable: CompositeDisposable? = null

    var mAdapter: MovieDataAdapter? = null
    var mDataSet: ArrayList<MovieDataClass>? = null    //for recycle view
    var mDataSet1: ArrayList<MovieDataClass>? = null   //for filtering data. using the same dataset wont work cause one has to change when filtering
    var searchView : SearchView? = null


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
        mDataSet = ArrayList(dataArray.data)   //for recycle view
        mDataSet1 = ArrayList(dataArray.data)  //for filtering data

        mAdapter = MovieDataAdapter(mDataSet1!!, this, mDataSet!!)
        recycleview.adapter = mAdapter
    }

    private fun handleError(error: Throwable) {
        Toast.makeText(applicationContext, "Error getting data", Toast.LENGTH_LONG).show()
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        return false
    }

    override fun onQueryTextChange(query: String): Boolean {
        val textEntered = query
        mAdapter!!.findThis(textEntered)
        return false
    }

    override fun onItemClick(movieDataClass: MovieDataClass) {

    }

    override fun onDestroy() {
        super.onDestroy()
        mCompositeDisposable?.clear()
    }



}
