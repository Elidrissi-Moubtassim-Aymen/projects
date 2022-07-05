package com.example.movieapplication.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapplication.adapters.FavoriteAdapter
import com.example.movieapplication.R
import com.example.movieapplication.models.Movie
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Favorite() : Fragment(), FavoriteAdapter.RecyclerViewClickInterface {

    var myAdapter : FavoriteAdapter = FavoriteAdapter(mutableListOf(), this)
    var movies : ArrayList<Movie> = ArrayList()
    lateinit var moviesList: List<Movie>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        moviesList = movies
        myAdapter = FavoriteAdapter(moviesList, this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val sharedPreference = context?.getSharedPreferences("FILE", Context.MODE_PRIVATE)

        var tempArray : ArrayList<Movie> = ArrayList<Movie>()
        val gson = Gson()
        val json = sharedPreference?.getString("movie", "")
        if (json != null) {
            if (json.isNotEmpty()) {
                val type = object : TypeToken<ArrayList<Movie>>() {}.type
                tempArray = gson.fromJson(json, type)
            }
        }
        moviesList = tempArray
        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        myAdapter = FavoriteAdapter(moviesList, this)
        view?.findViewById<RecyclerView>(R.id.rv_favorite_movies_list)?.layoutManager = LinearLayoutManager(context)
        view?.findViewById<RecyclerView>(R.id.rv_favorite_movies_list)?.setHasFixedSize(true)
        view?.findViewById<RecyclerView>(R.id.rv_favorite_movies_list)?.adapter = myAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        inflater.inflate(R.menu.search, menu)
        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView
        searchView.maxWidth = Int.MAX_VALUE

        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                myAdapter.filter.filter(p0)
                return true
            }
        })
    }

    override fun onClickItem(position: Int, movies: List<Movie>) {
        val intent = Intent(context, Detail::class.java)
        intent.putExtra("movie_image", movies[position].poster)
        intent.putExtra("movie_title", movies[position].title)
        intent.putExtra("overview", movies[position].overview)
        intent.putExtra("movie", movies[position])
        startActivity(intent)
    }
}