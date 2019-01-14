package com.example.prince.herokukotlin

import io.reactivex.Observable
import retrofit2.http.GET

interface MovieJSONInterface {

    @GET("movies/")
    fun getMovies() : Observable<DataArray>
}