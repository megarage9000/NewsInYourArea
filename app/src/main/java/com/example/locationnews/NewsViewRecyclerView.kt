package com.example.locationnews

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.locationnews.model.NewsGet

class NewsViewRecyclerView (private var newsList: MutableList<NewsGet>,
                            val context: Context,
                            val layoutInflater: LayoutInflater)
                            : RecyclerView.Adapter<NewsViewRecyclerView.NewsViewHolder>(){
    class NewsViewHolder(inflater: LayoutInflater, parent: ViewGroup, view: View)
        : RecyclerView.ViewHolder(view) {

            fun setContent(newsContent: NewsGet) {
                var newsView = this.itemView
                newsView.findViewById<TextView>(R.id.NewsTitle).text = newsContent.title
                newsView.findViewById<TextView>(R.id.NewsPublisher).text = "Source: " + newsContent.publisher
                newsView.findViewById<TextView>(R.id.NewsDescription).text = newsContent.description
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.news_view_item, parent, false)
        val layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        view.layoutParams = layoutParams
        return NewsViewHolder(inflater, parent, view)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.setContent(
            newsList[position]
        )
    }

    override fun getItemCount(): Int {
        return newsList.size
    }

    fun updateList(newNewsList: MutableList<NewsGet>) {
        newsList = newNewsList
        notifyDataSetChanged()
    }
}