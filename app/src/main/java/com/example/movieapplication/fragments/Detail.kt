package com.example.movieapplication.fragments

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.movieapplication.R

class Detail : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val intent = getIntent()
        val movie_image = intent.getStringExtra("movie_image")
        val movie_titre = intent.getStringExtra("movie_title")
        val description = intent.getStringExtra("overview")
        val IMAGE_BASE = "https://image.tmdb.org/t/p/w500/"
        Glide.with(this).load(IMAGE_BASE + movie_image).into(findViewById<ImageView>(R.id.film_image))
        findViewById<TextView>(R.id.movie_title).text = movie_titre
        findViewById<TextView>(R.id.overview).text = description
    }
}