package com.example.movieapplication.adapters

import android.content.Context
import android.content.SharedPreferences
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movieapplication.R
import com.example.movieapplication.models.Movie
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class MovieAdapter(private var movies: List<Movie>, private val recyclerViewClickInterface: RecyclerViewClickInterface) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>(), Filterable{

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
            LayoutInflater.from(parent.context).inflate(R.layout.movie_item, parent, false)
        )
    }

    override fun getItemCount(): Int = movies.size

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bindMovie(movies[position])
        holder.itemView.findViewById<ToggleButton>(R.id.heart_change).setOnClickListener(View.OnClickListener {
            holder.itemView.findViewById<ToggleButton>(R.id.heart_change).textOn
            Toast.makeText(holder.itemView.context, "Added successfully to favorite !", Toast.LENGTH_SHORT).show()
            val handler = Handler(Looper.getMainLooper())
            handler.postDelayed({
                holder.itemView.findViewById<ToggleButton>(R.id.heart_change).toggle()
            }, 2000)
            holder.itemView.findViewById<ToggleButton>(R.id.heart_change).visibility = View.GONE
            //send.send(allMovies[position])
            //bundle.putParcelable("movie", allMovies[position])
            //SharedPreferences.Editor = Context.
            var context = holder.itemView.context
            var sharedPreference = context.getSharedPreferences("FILE", Context.MODE_PRIVATE)
            val editor : SharedPreferences.Editor = sharedPreference.edit()
            //editor.clear().commit()
            var tempArray : ArrayList<Movie> = ArrayList<Movie>()
            var gson = Gson()
            var json = sharedPreference.getString("movie", "")
            if (json != null) {
                if(json.isNotEmpty()) {
                    //Log.i("yy", tempArray.toString())
                    val type = object : TypeToken<ArrayList<Movie>>() {}.type
                    tempArray = gson.fromJson(json, type)
                }
            }
            tempArray.add(allMovies[position])
            json = gson.toJson(tempArray)
            editor.putString("movie", json)
            editor.commit()
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