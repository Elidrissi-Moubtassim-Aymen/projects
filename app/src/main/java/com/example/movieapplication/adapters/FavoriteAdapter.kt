package com.example.movieapplication.adapters

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movieapplication.home.HomeActivity
import com.example.movieapplication.models.Movie
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.example.movieapplication.R


class FavoriteAdapter(private var movies: List<Movie>, private val recyclerViewClickInterface: RecyclerViewClickInterface) : RecyclerView.Adapter<FavoriteAdapter.MovieViewHolder>(), Filterable{

    private val allMovies = movies
    private val IMAGE_BASE = "https://image.tmdb.org/t/p/w500/"



    inner class MovieViewHolder(view : View) : RecyclerView.ViewHolder(view){
        fun bindMovie(movie : Movie){
            itemView.findViewById<TextView>(R.id.movie_title).text = movie.title
            itemView.findViewById<TextView>(R.id.movie_release_date).text = movie.release
            Glide.with(itemView).load(IMAGE_BASE + movie.poster).into(itemView.findViewById<ImageView>(R.id.movie_poster))
        }
        init{
            itemView.setOnClickListener {
                recyclerViewClickInterface.onClickItem(adapterPosition, movies)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.fragment_favorite_item, parent, false)
        )
    }

    override fun getItemCount(): Int = movies.size

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bindMovie(movies[position])
        holder.itemView.findViewById<ToggleButton>(R.id.delete).setOnClickListener(View.OnClickListener {
            Toast.makeText(
                holder.itemView.context,
                "Deleted successfully from favorite !",
                Toast.LENGTH_SHORT
            ).show()
            var context = holder.itemView.context
            var sharedPreference = context.getSharedPreferences("FILE", Context.MODE_PRIVATE)
            val editor : SharedPreferences.Editor = sharedPreference.edit()
            var array : ArrayList<Movie> = ArrayList<Movie>()
            var gson = Gson()
            var json = sharedPreference.getString("movie", "")
            if (json != null) {
                if(json.isNotEmpty()) {
                    Log.i("yy", array.toString())
                    val type = object : TypeToken<ArrayList<Movie>>() {}.type
                    array = gson.fromJson(json, type)
                    array.removeAt(position)
                }
            }
            val jsonSend = gson.toJson(array)
            editor.putString("movie", jsonSend)
            editor.commit()
            context.startActivity(Intent(context, HomeActivity::class.java))
        })
    }

    interface RecyclerViewClickInterface {
        fun onClickItem(position : Int, movies : List<Movie>)
    }

    override fun getFilter(): Filter {
        return object: Filter(){
            override fun performFiltering(charsequence: CharSequence?): FilterResults {
                val filterResults = FilterResults()
                if (charsequence == null || charsequence.isEmpty()) {
                    filterResults.count = allMovies.size
                    filterResults.values = allMovies
                } else {
                    val searchChr = charsequence.toString()
                    val itemModal = ArrayList<Movie>()
                    for (items in allMovies) {
                        if (items.title.toString().toLowerCase().contains(searchChr.toLowerCase())) {
                            itemModal.add(items)
                        }
                    }
                    filterResults.count = itemModal.size
                    filterResults.values = itemModal

                }
                return filterResults
            }
            override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
                movies = p1!!.values as List<Movie>
                notifyDataSetChanged()

            }
        }
    }
}