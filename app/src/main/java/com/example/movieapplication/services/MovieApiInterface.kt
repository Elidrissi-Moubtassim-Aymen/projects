package com.example.movieapplication.services

import com.example.movieapplication.models.MovieResponse
import retrofit2.Call
import retrofit2.http.GET

interface MovieApiInterface {

    @GET("/3/movie/popular?api_key=1fe13390deedcd876152dfcf9f6dbecb")
    fun getMovieList(): Call<MovieResponse>
}