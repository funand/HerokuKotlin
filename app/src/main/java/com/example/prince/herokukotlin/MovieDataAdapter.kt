package com.example.prince.herokukotlin

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.movie_items.view.*
import java.util.*


class MovieDataAdapter(var dataset: ArrayList<MovieDataClass>, private val listener : Listener, val arrayList: ArrayList<MovieDataClass>) : RecyclerView.Adapter<MovieDataAdapter.MovieHolder>() {

    interface Listener{
        fun onItemClick(movieDataClass : MovieDataClass)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.movie_items, parent, false)
        return MovieHolder(view)
    }

    override fun getItemCount(): Int {
        return dataset.count()
    }

    override fun onBindViewHolder(holder: MovieHolder, position: Int) {
        holder.bind(dataset.get(position), listener)
    }

    fun setFilter(arrayList: ArrayList<MovieDataClass>){

    }

    fun findThis(charText: String): Boolean {
        dataset.clear()
//        var temp = charText.toLowerCase()

        if(charText.isNotEmpty()) {

            var temp = charText.toLowerCase()
            println("size: "+arrayList.size)
            arrayList.forEach{
                if (it.title.toLowerCase().contains(temp) || it.genre.toLowerCase().contains(temp))
                dataset.add(it)
            }

        }

        else{
            dataset.addAll(arrayList)
        }

        notifyDataSetChanged()
        return true
    }

    class MovieHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun  bind(movieDataClass: MovieDataClass, listener: Listener){
            itemView.movies_title.text = movieDataClass.title
            itemView.movies_year.text = movieDataClass.year.toString() // ask why I  couldn't just do equals  movieDataClass.title
            if(movieDataClass.poster != ""){
                Picasso.get().load(movieDataClass.poster).fit().into(itemView.movie_img)
                itemView.setOnClickListener{listener.onItemClick(movieDataClass)}
            }
        }
    }
}