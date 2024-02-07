package com.example.findyourway

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView


internal class CaptionedImagesAdapter(
    private val captions: ArrayList<String>,
    private val imageIds: ArrayList<Long>,
    private var listener: ListenerCIA? = null
) :
    RecyclerView.Adapter<CaptionedImagesAdapter.ViewHolder>() {
    class ViewHolder(cardView: CardView) : RecyclerView.ViewHolder(
        cardView
    )
    interface ListenerCIA{
        fun onClick(position: Int)
    }
    override fun getItemCount(): Int {
        return captions.size
    }
    fun setListener(listener: ListenerCIA?){
        this.listener = listener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val cv = LayoutInflater.from(parent.context).inflate(R.layout.card_captioned_image, parent, false) as CardView
        return ViewHolder(cv)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cardView = holder.itemView as CardView
        val imageView: ImageView = cardView.findViewById(R.id.info_image)

        val drawable = ContextCompat.getDrawable(cardView.context,
            cardView.context.resources.getIdentifier(("p".plus((imageIds[position]).toString())),
                "drawable",cardView.context.packageName))
        imageView.setImageDrawable(drawable)
        imageView.contentDescription = captions[position]
        val textView = cardView.findViewById<TextView>(R.id.info_text)
        textView.text = captions[position]

        cardView.setOnClickListener {
            listener?.onClick(position)
        }
    }
}