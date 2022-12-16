package com.demo.urlShorterDemo.service

import com.demo.urlShorterDemo.model.LongUrl

interface UrlShortenerService {

    fun createShortUrl(lLongUrl: LongUrl?): String?

    fun fetchShortUrl(lLongUrl: String?): String?

    fun fetchLongUrl(lShortUrl: String?): String?

}