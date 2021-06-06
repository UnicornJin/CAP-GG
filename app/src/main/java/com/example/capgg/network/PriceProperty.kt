package com.example.capgg.network

import com.squareup.moshi.Json

data class response(
    @Json(name = "data")
    val data: PriceProperty,
    @Json(name = "timestamp")
    val timeStamp: Long
) {
    data class PriceProperty(
        @Json(name = "id")
        val id: String,
        @Json(name = "rank")
        val rank: Int,
        @Json(name = "symbol")
        val symbol: String,
        @Json(name = "name")
        val name: String,
        @Json(name = "supply")
        val supply: Double,
        @Json(name = "maxSupply")
        val maxSupply: Double,
        @Json(name = "marketCapUsd")
        val marketCapUsd: Double,
        @Json(name = "volumeUsd24Hr")
        val volumeUsd24Hr: Double,
        @Json(name = "priceUsd")
        val priceUsd: Double,
        @Json(name = "changePercent24Hr")
        val changePercent24Hr: Double,
        @Json(name = "vwap24Hr")
        val vwap24Hr: Double
    )
}


