package com.demo.urlShorterDemo.util

import com.demo.urlShorterDemo.exception.InvalidParametersException
import com.demo.urlShorterDemo.model.LongUrl
import com.demo.urlShorterDemo.model.Url
import com.demo.urlShorterDemo.repository.UrlDaoImpl
import java.net.URL;
import java.util.HashMap;
import java.util.List;


import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit

import com.google.common.cache.CacheBuilder

import com.google.common.cache.Cache


@Component
class UrlCommonFetchUtil {
    @Autowired
    private val lUrlDaoImpl: UrlDaoImpl? = null

    var urlCache: Cache<String, HashMap<String, Any?>>? = null
    var shortUrlCache: Cache<String, String>? = null



    @PostConstruct
    fun fetchAllUrlFromDb() {
        val lUrlList: List<Url?>? = lUrlDaoImpl!!.fetchAllUrl()
        urlCache = CacheBuilder.newBuilder().maximumSize(cacheServiceMaxEntries.toLong())
                .expireAfterAccess(cacheExpireTimePeriod.toLong(), TimeUnit.HOURS).build()
        shortUrlCache = CacheBuilder.newBuilder().maximumSize(cacheServiceMaxEntries.toLong())
                .expireAfterAccess(cacheExpireTimePeriod.toLong(), TimeUnit.HOURS).build()
        for (url in lUrlList!!) {
            val lMapUrl = HashMap<String, Any?>()
            lMapUrl[EXPIRE_DATE] = url?.getExpireDateTime()
            lMapUrl[RANDOM_URL] = url?.getShortUrl()
            (urlCache as Cache<String, HashMap<String, Any?>>?)?.put(url?.getLongUrl()!!, lMapUrl)
            (shortUrlCache as Cache<String, String>?)?.put(url!!.getShortUrl()!!, url!!.getLongUrl()!!)

        }
    }

    fun validateUrl(url: String?): Boolean {
        return try {
            URL(url).toURI()
            true
        } catch (e: Exception) {
            false
        }
    }

    fun validateLongUrl(lLongUrl: LongUrl?) {
        if (lLongUrl == null || !StringUtils.hasText(lLongUrl.fullUrl)) {
            throw InvalidParametersException("Long Url is empty")
        }
        if (!validateUrl(lLongUrl.fullUrl)) {
            throw InvalidParametersException("Invalid Url : " + lLongUrl.fullUrl)
        }
    }

    fun randomString(): String? {
        var randomStr = ""
        val possibleChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
        for (i in 0..8) randomStr += possibleChars[Math.floor(Math.random() * possibleChars.length)
                .toInt()]
        return randomStr
    }

    fun validateExpireDate(lExpiredate: LocalDateTime?): Boolean {
        return if (LocalDateTime.now().isBefore(lExpiredate)) {
            true
        } else false
    }
    companion object {
        const val EXPIRE_DATE = "EXPIRE_DATE"
        const val RANDOM_URL = "RANDOM_URL"
        const val localAddress = "http://localhost:8080/api/url-shortener/"
        const val cacheServiceMaxEntries = 20000
        const val cacheExpireTimePeriod = 24
    }

}