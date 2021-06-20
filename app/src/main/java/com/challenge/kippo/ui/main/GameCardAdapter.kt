package com.challenge.kippo.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.challenge.kippo.R
import com.challenge.kippo.backend.storage.entities.GameData
import com.challenge.kippo.databinding.CustomGameCardBinding

/**
 * Adapter for RecyclerViews that will display Game Cards (cover, title, genre, rating)
 */
class GameCardAdapter (private val list: ArrayList<String>): RecyclerView.Adapter<GameCardAdapter.GameCardHolder>(){

    /**
     * Wrapper class for each recycler view item.
     */
    class GameCardHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        private var gameImage: ImageView = itemView.findViewById(R.id.card_image)
        //private var title: TextView = itemView.findViewById(R.id.card_title)
        private var test_gameImage: ImageView = itemView.findViewById(R.id.test_image)
        private var favorite_image: ImageView = itemView.findViewById(R.id.card_favorite)
        private var test_title: TextView = itemView.findViewById(R.id.test_title)
        private var pos = 0

        /**
         * Initializes the views based on the corresponding item in the GameList (list[pos])
         */
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

    /**
     * Inflates the GameCardLayout and its corresponding view bind object.
     * @return GameCardHolder object containing the inflated view
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameCardHolder {
        val gameListItem = CustomGameCardBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return GameCardHolder(gameListItem.root)
    }

    /**
     * Called when a view is updated or right after onCreateViewHolder
     * It triggers each GameCardHolder object to instantiate the view properties.
     */
    override fun onBindViewHolder(holder: GameCardHolder, position: Int) {
        val title = list[position]
        holder.bindCards(position, title)
    }

    /**
     * @return Total number of items (games) / views on the RecyclerView
     */
    override fun getItemCount(): Int {

        return list.size
    }
    fun setGames(gameData: List<GameData>){
        list.apply {
            clear()
            //addAll(games)
        }
        //Triggers a re-draw of the updated views.
        notifyDataSetChanged()
    }
    companion object{
        public const val GRID_COL_COUNT = 2


    }
}