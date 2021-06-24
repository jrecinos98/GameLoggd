package com.challenge.kippo.ui.main.adapters

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.challenge.kippo.R
import com.challenge.kippo.backend.database.entities.GameData
import com.challenge.kippo.databinding.CustomGameCardBinding
import com.challenge.kippo.ui.fragments.SearchFragment


/**
 * Adapter for RecyclerViews that will display Game Cards (cover, title, genre, rating)
 */
class GameCardAdapter(private val context: Fragment) : RecyclerView.Adapter<GameCardAdapter.GameCardHolder>(){
    private var gameList = ArrayList<GameData>()
    private lateinit var listener : (game : GameData)-> Unit

    /**
     * Wrapper class for each recycler view item.
     */
    inner class GameCardHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        private var gameCover: ImageView = itemView.findViewById(R.id.card_image)
        private var favoriteImage: ImageView = itemView.findViewById(R.id.card_favorite)
        private var cardTitle: TextView = itemView.findViewById(R.id.card_title)
        private var cardGenre : TextView = itemView.findViewById(R.id.card_genre)
        private var cardRating : TextView = itemView.findViewById(R.id.card_rating)
        private var pos = 0
        private val GAME_KEY = "GAME"
        /**
         * Initializes the views based on the corresponding item in the GameList (list[pos])
         */
        fun bindCards(pos: Int){
            if(gameList.size > pos ) {
                val game = gameList[pos]
                //Load image
                Glide.with(context)
                        .asDrawable()
                        .load(game.coverUrl)
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                        .into(gameCover)

                cardTitle.setText(game.title)
                cardGenre.setText(game.genre)
                cardRating.setText("${game.rating}%")
                //The search result GameCards do not include an option to favorite games
                if(context::class.java != SearchFragment::class.java) {
                    favoriteImage.visibility = View.VISIBLE
                    //Set the listener for the favorite image
                    favoriteImage.setOnClickListener(onFavoriteClick(pos))
                    //If Game has been favorited update favoriteImage
                    favoriteImage.isActivated = game.favorited
                }
            }
            this.pos = pos
        }
        fun isValidContextForGlide(context: Context?): Boolean {
            if (context == null) {
                return false
            }
            if (context is Activity) {
                val activity = context
                if (activity.isDestroyed || activity.isFinishing) {
                    return false
                }
            }
            return true
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
        holder.bindCards(position)
    }

    /**
     * @return Total number of items (games) / views on the RecyclerView
     */
    override fun getItemCount(): Int {
        return gameList.size
    }

    /**
     * Triggered when Favorite icon is clicked. It updates the icon state and invokes
     * the call back method if it has been set
     * @param position The index the GameData, whose favorite icon was pressed, is in the gameList
     */
    private fun onFavoriteClick(position: Int): View.OnClickListener {
        return View.OnClickListener { view ->
            val game = gameList[position]
            //Invert the activated state
            view.isActivated = !view.isActivated
            game.favorited = view.isActivated
            listener(game)
        }
    }

    /**
     * Updates the list of games currently displayed
     * @param gameData The updated list of GameData
     */
    fun setGames(gameData: List<GameData>){
        gameList = gameData as ArrayList<GameData>
        //Triggers a re-draw of the updated views.
        notifyDataSetChanged()
    }

    /**
     * Assigns a callback method that will be invoked when the favorite icon is clicked
     * @param onFavorite The callback to be invoked
     */
    fun setOnFavoriteClick(onFavorite : (game : GameData)-> Unit){
        listener = onFavorite
    }
}