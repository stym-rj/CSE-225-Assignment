package com.example.moviescope

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.bumptech.glide.Glide
import com.example.moviescope.databinding.ActivityMovieDescriptionBinding
import com.example.moviescope.networkUtils.GenreData
import com.example.moviescope.networkUtils.MoviesData
import com.example.moviescope.networkUtils.genreApi
import retrofit2.Call
import retrofit2.Response

class MovieDescription : AppCompatActivity() {

    private lateinit var binding: ActivityMovieDescriptionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDescriptionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle: Bundle?= intent.extras
        val data = bundle!!.getSerializable("moviesData")as MoviesData

        val backgroundImage = data.backgroundImage
        val posterImage = data.poster
        val movieTitle = data.movieTitle
        val rating = data.rating
        val ratingCount = data.ratingCount
        val about = data.about
        val language = when (data.language) {
            "en" -> "English"
            "ko" -> "Korean"
            "de" -> "German"
            "es" -> "Spanish"
            "ja" -> "Japanese"
            "id" -> "Indonesian"
            "sv" -> "Swedish"
            else -> "Unknown"
        }
        val genre = data.genreList
        val releaseDate = data.releaseDate


        Glide.with(this).load("${imageUrlPrefix}${backgroundImage}").into(binding.ivMovieBackground)
        binding.tvMovieName.text = movieTitle
        binding.tvRatingCount.text = "($ratingCount)"
        binding.rbRating.rating = rating / 2f
        binding.tvAbout.text = about
        binding.tvLanguage.text = language

        var genreList: ArrayList<String> = ArrayList()
        genreApi.fetchGenre().enqueue(object : retrofit2.Callback<List<GenreData>?> {
            override fun onResponse(
                call: Call<List<GenreData>?>,
                response: Response<List<GenreData>?>
            ) {
                if (response.isSuccessful) {
                    val data = response.body() ?: listOf()
                    genreList = ArrayList(data.filter { it -> it.id in genre!!.toList() }.map { it -> it.name })

                    binding.tvGenre.text = genreList.joinToString(separator = ", ")
                }
            }

            override fun onFailure(call: Call<List<GenreData>?>, t: Throwable) {
                Log.d("printing failure in genre : ", "${t.message}")
            }
        })
        binding.tvReleaseDate.text = releaseDate
        Glide.with(this).load("${imageUrlPrefix}${posterImage}").into(binding.iv1)
        Glide.with(this).load("${imageUrlPrefix}${backgroundImage}").into(binding.iv2)
    }
}