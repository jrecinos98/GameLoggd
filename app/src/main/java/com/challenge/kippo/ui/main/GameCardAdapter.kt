package com.challenge.kippo.ui.main

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.challenge.kippo.R
import com.challenge.kippo.backend.storage.entities.GameCard
import com.challenge.kippo.databinding.CustomGameCardBinding

class GameCardAdapter (private val list: ArrayList<String>): RecyclerView.Adapter<GameCardAdapter.GameCardHolder>(){


    class GameCardHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        private var gameImage: ImageView = itemView.findViewById(R.id.card_image)
        //private var title: TextView = itemView.findViewById(R.id.card_title)
        private var test_gameImage: ImageView = itemView.findViewById(R.id.test_image)
        private var favorite_image: ImageView = itemView.findViewById(R.id.card_favorite)
        private var test_title: TextView = itemView.findViewById(R.id.test_title)
        private var pos = 0

        fun bindCards(pos : Int, t: String){
            test_gameImage.setImageDrawable(
                    ContextCompat.getDrawable(itemView.context, R.drawable.ic_trending))
            gameImage.setImageDrawable(
                ContextCompat.getDrawable(itemView.context, R.drawable.ic_trending))

            favorite_image.setImageDrawable(
                ContextCompat.getDrawable(itemView.context, R.drawable.ic_search))

            test_title.text = t
            this.pos = pos
        }

        companion object {
            //5
            private val GAME_KEY = "GAME"
        }
    }


    override fun getItemId(position: Int): Long {

        return 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameCardHolder {
        val gameListItem = CustomGameCardBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        //val movie = ListItemBinding.inflate(LayoutInflater.from(context), parent, false)
        Log.d("GAME_CARD", "initialized")
        //val image = ImageView(context)
        return GameCardHolder(gameListItem.root)
    }

    override fun onBindViewHolder(holder: GameCardHolder, position: Int) {
        val title = list[position]
        holder.bindCards(position, title)
    }

    override fun getItemCount(): Int {

        return list.size
    }
    fun setGames(games: List<GameCard>){
        list.apply {
            clear()
            //addAll(games)
        }
        notifyDataSetChanged()
    }
    companion object{
        public const val GRID_COL_COUNT = 2


    }
}