package com.example.movieapplication.home

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.movieapplication.R
import com.example.movieapplication.fragments.Favorite
import com.example.movieapplication.fragments.Home
import com.example.movieapplication.fragments.Popular
import com.example.movieapplication.fragments.TopRated
import com.example.movieapplication.models.Movie
import com.google.android.material.bottomnavigation.BottomNavigationView


class HomeActivity : AppCompatActivity() {

    lateinit var movies: ArrayList<Movie>
    lateinit var moviesList: List<Movie>
    private val popular = Popular()
    private val topRated = TopRated()
    private val home = Home()
    private val favorite = Favorite()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        fragmentSwitch(home)
        findViewById<BottomNavigationView>(R.id.navigation_bar).setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.home -> {
                    fragmentSwitch(home)
                    findViewById<TextView>(R.id.subtitle).text = "Home"
                }
                R.id.popular -> {
                    fragmentSwitch(popular)
                    findViewById<TextView>(R.id.subtitle).text = "Popular"
                }
                R.id.top_rated -> {
                    fragmentSwitch(topRated)
                    findViewById<TextView>(R.id.subtitle).text = "Top Rated"
                }
                R.id.favorite -> {
                    //favorite.moviesList = moviesList
                    fragmentSwitch(favorite)
                    findViewById<TextView>(R.id.subtitle).text = "Favorite"
                }
            }
            true
        }
    }

    private fun fragmentSwitch(fragment : Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.rv_movies_list, fragment)
        transaction.commit()
    }
}