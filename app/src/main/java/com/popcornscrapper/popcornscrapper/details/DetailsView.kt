package com.popcornscrapper.popcornscrapper.details

import com.popcornscrapper.popcornscrapper.BaseView

interface DetailsView : BaseView {
    fun setupMovieTitle(movieTitle: String)
}