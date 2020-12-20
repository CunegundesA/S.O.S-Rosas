package com.example.sosrosas.View

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.sosrosas.Model.Article
import com.example.sosrosas.Model.News
import com.example.sosrosas.R
import kotlinx.android.synthetic.main.recycler_view_noticias.view.*
import retrofit2.Callback
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class RecyclerViewNoticiasAdapter : RecyclerView.Adapter<RecyclerViewNoticiasAdapter.ViewHolder>{

    private var context : Context
    private var listArticles: List<Article>
    private lateinit var noticiasListerner : NoticiasListerner


    constructor(
        context: Context,
        listArticle: List<Article>
    ){
        this.context = context
        this.listArticles = listArticle
    }

    open fun setListener(fragment: Fragment){
        try {
            noticiasListerner = fragment as NoticiasListerner
        } catch (e: ClassCastException) {
        }
    }

    private fun dataFormat(data : String) : String{
        var newDate : String
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale(Locale.getDefault().country))

        try {
            val date = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(data)
            newDate = dateFormat.format(date)
        }catch (e : ParseException){
            newDate = data
        }
        return newDate
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_view_noticias, parent, false)

        val holder = ViewHolder(view)

        view.tag = holder

        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val article = listArticles.get(position)

        Glide.with(context).load(article.urlToImage).listener(object :RequestListener<Drawable>{
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean,
            ): Boolean {
                holder.bar.visibility = View.GONE
                return false
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean,
            ): Boolean {
                holder.bar.visibility = View.GONE
                return false
            }

        }).into(holder.image)

        holder.title.text = article.title
        holder.desc.text = article.description
        holder.autor.text = article.author
        holder.data.text = dataFormat(article.publishedAt)

        holder.card.setOnClickListener { noticiasListerner.goPagerBrowser(article.url) }

    }

    override fun getItemCount(): Int {
        return listArticles.size
    }

    interface NoticiasListerner{

        fun goPagerBrowser(url: String)
    }

    class ViewHolder : RecyclerView.ViewHolder{

        var card : CardView
        var image : ImageView
        var bar : ProgressBar
        var title : TextView
        var desc : TextView
        var autor : TextView
        var data : TextView

        constructor (itemView: View) : super(itemView){
            card = itemView.card_view_noticias
            image = itemView.img_noticia
            bar = itemView.load_img_noticia
            title = itemView.title_noticia
            desc = itemView.desc_noticia
            autor = itemView.autor_noticia
            data = itemView.data_noticia
        }
    }
}