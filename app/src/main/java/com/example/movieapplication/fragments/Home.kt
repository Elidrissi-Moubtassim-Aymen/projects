package com.example.movieapplication.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cronocode.moviecatalog.services.MovieApiService
import com.example.movieapplication.adapters.MovieAdapter
import com.example.movieapplication.R
import com.example.movieapplication.models.Movie
import com.example.movieapplication.models.MovieResponse
import com.example.movieapplication.services.MovieHomeApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Home : Fragment() , MovieAdapter.RecyclerViewClickInterface {

    lateinit var myAdapter : MovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        myAdapter = MovieAdapter(mutableListOf(), this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<RecyclerView>(R.id.rv_home_movies_list).layoutManager = LinearLayoutManager(context)
        view.findViewById<RecyclerView>(R.id.rv_home_movies_list).setHasFixedSize(true)
        getMovieData { movies : List<Movie> ->
            myAdapter = MovieAdapter(movies, this)
            view.findViewById<RecyclerView>(R.id.rv_home_movies_list).adapter = myAdapter
        }
    }

    private fun getMovieData(callback: (List<Movie>) -> Unit){
        val apiService = MovieApiService.getInstance().create(MovieHomeApiInterface::class.java)
        apiService.getMovieList().enqueue(object : Callback<MovieResponse> {
            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {

            }

            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                return callback(response.body()!!.movies)
            }
        })
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

    override fun onClickItem(position: Int, movies : List<Movie>) {
        val intent = Intent(context, Detail::class.java)
        intent.putExtra("movie_image", movies[position].poster)
        intent.putExtra("movie_title", movies[position].title)
        intent.putExtra("overview", movies[position].overview)
        intent.putExtra("movie", movies[position])
        startActivity(intent)
    }
}