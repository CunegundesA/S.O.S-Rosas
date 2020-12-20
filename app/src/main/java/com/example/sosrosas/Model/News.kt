package com.example.sosrosas.Model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class News (
    @SerializedName("status")
    @Expose
    var status: String,
    @SerializedName("totalResults")
    @Expose
    var totalResult : Int,
    @SerializedName("articles")
    @Expose
    var article: List<Article>
)