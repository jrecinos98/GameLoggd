package com.challenge.kippo

import com.challenge.kippo.backend.api.requests.IgdbEndpoints
import com.challenge.kippo.backend.api.responses.Cover
import com.challenge.kippo.backend.api.responses.Game
import com.challenge.kippo.backend.api.responses.Genre
import com.challenge.kippo.backend.storage.entities.GameData

class RepositoryTests {
    /*
    private fun fetchGameCovers(ids : String, request : IgdbEndpoints) : List<Cover>{
        //println("ID: $ids")
        val response = request.fetchCovers(Cover.buildRequestBody(ids)).execute()
        var covers = listOf<Cover>()
        if(response.isSuccessful){
            covers = response.body()!!
        }
        return covers
    }
    private fun fetchGenres(ids : String, request : IgdbEndpoints) : List<Genre>{
        //println("Genres: $ids")
        val response = request.fetchGenres(Genre.buildRequestBody(ids)).execute()
        var genres = listOf<Genre>()
        if(response.isSuccessful){
            genres = response.body()!!
        }
        return genres

    }

    private fun generateGameCards(games : List<Game>, request : IgdbEndpoints) : List<GameData>{
        var coverIds = ""
        var genreIds = ""
        var gameHashMap = HashMap<Int, GameData>()
        val gameCards = arrayListOf<GameData>()
        val genreHashMap = HashMap<Int, ArrayList<GameData>>()
        for (game in games){
            //Initialize a default gameCard with Game info
            val gameCard = GameData(
                    game.id,
                    "default_cover_url",
                    false,
                    game.name,
                    "test",
                    game.totalRating
            )
            //If the game has a cover artwork add it to hashmap to update later
            if(game.coverId != 0) {
                coverIds += game.coverId.toString() + ","
                gameHashMap.put(game.id, gameCard)
            }
            if(game.genres != null) {
                val genre = game.genres!![0]
                if(genreHashMap.containsKey(genre)){
                    genreHashMap[genre]?.add(gameCard)
                }
                else{
                    val listOfGenre = arrayListOf(gameCard)
                    genreHashMap[genre.id] = listOfGenre

                }
                genreIds += "$genre,"
            }

            gameCards.add(gameCard)
        }
        //removes last comma from query and fetches covers
        val covers = fetchGameCovers(coverIds.substring(0, coverIds.length-1), request)
        val genres = fetchGenres(genreIds.substring(0, genreIds.length - 1), request)
        //Assign cover url to gameCards that have one.
        for (cover in covers){
            val gameData : GameData? = gameHashMap.get(cover.game)
            gameData?.coverUrl = Cover.generateHDImageURL(cover.imageID)
        }
        //Assign genre to gameCards
        for( genre in genres){
            val pendingGames = genreHashMap[genre.id]
            if (pendingGames != null) {
                for(game in pendingGames){
                    game.genre = genre.name
                }
            }
        }
        //println(gameCards)

        return gameCards
    }

     */
}