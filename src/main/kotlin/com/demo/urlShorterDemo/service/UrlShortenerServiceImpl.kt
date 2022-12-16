package com.demo.urlShorterDemo.service

import com.demo.urlShorterDemo.exception.InvalidParametersException
import com.demo.urlShorterDemo.exception.ObjectAlreadyPresentException
import com.demo.urlShorterDemo.model.LongUrl
import com.demo.urlShorterDemo.model.Url
import com.demo.urlShorterDemo.repository.UrlDaoImpl
import com.demo.urlShorterDemo.util.UrlCommonFetchUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDateTime


@Service
class UrlShortenerServiceImpl: UrlShortenerService {

    @Autowired
    private val lUrlDaoImpl: UrlDaoImpl? = null

    @Autowired
    private val lUrlCommonFetchUtil: UrlCommonFetchUtil? = null

    fun saveUrlInDb(full_url: String): String? {
        val lUrl = Url()
        lUrl.setLongUrl(full_url)
        lUrl.setCreationDateTime(LocalDateTime.now())
        lUrl.setExpireDateTime(LocalDateTime.now().plusMonths(6))
        lUrl.setShortUrl(lUrlCommonFetchUtil?.randomString())
        return if (lUrlDaoImpl!!.saveUrl(lUrl, 0)!!) {
            val lMap = HashMap<String, Any?>()
            lMap[UrlCommonFetchUtil.EXPIRE_DATE] = lUrl.getExpireDateTime()
            lMap[UrlCommonFetchUtil.RANDOM_URL] = lUrl.getShortUrl()
            lUrlCommonFetchUtil!!?.urlCache?.put(full_url,lMap)
            lUrl.getShortUrl()?.let { lUrlCommonFetchUtil.shortUrlCache?.put(it,full_url) }
            lUrl.getShortUrl()
        } else {
            throw InvalidParametersException("Invalid Data ,try after sometime")
        }
    }

    override fun createShortUrl(longUrl: LongUrl?): String? {
        lUrlCommonFetchUtil?.validateLongUrl(longUrl)
        val lMapUrl = longUrl?.let { lUrlCommonFetchUtil?.urlCache?.getIfPresent(it.fullUrl) }
        if (lMapUrl != null && lMapUrl.isNotEmpty()) {
            val expireDate = lMapUrl[UrlCommonFetchUtil.EXPIRE_DATE] as LocalDateTime?
            if (LocalDateTime.now().isAfter(expireDate)) {
                return longUrl?.fullUrl?.let { saveUrlInDb(it) }
            }
            throw ObjectAlreadyPresentException("Short Url is already present : " + UrlCommonFetchUtil.localAddress + lMapUrl[UrlCommonFetchUtil.RANDOM_URL])
        }
        return longUrl?.fullUrl?.let { saveUrlInDb(it) }
    }

    override fun fetchShortUrl(lLongUrl: String?): String? {
        val lUrl = lUrlDaoImpl!!.fetchUrlDetails("longUrl", lLongUrl!!)
        if (lUrl != null) {
            if (LocalDateTime.now().isAfter(lUrl.getExpireDateTime())) {
                throw InvalidParametersException("Short Url is expired,please generate new short Url")
            }
            return UrlCommonFetchUtil.localAddress.toString() + "" + lUrl.getShortUrl()
        }
        return null
    }

    override fun fetchLongUrl(lShortUrl: String?): String? {
        val lLongUrl = lUrlCommonFetchUtil!!.shortUrlCache!!.getIfPresent(lShortUrl!!)
        if(lLongUrl != null)
        {
             return lLongUrl
        }else {
            val lUrl = lUrlDaoImpl!!.fetchUrlDetails("shortUrl", lShortUrl!!)
            if (lUrl != null) {
                if (LocalDateTime.now().isAfter(lUrl.getExpireDateTime())) {
                    throw InvalidParametersException("Short Url is expired,please generate new short Url")
                }
                return lUrl.getLongUrl()
            }
            return null
        }
    }

}