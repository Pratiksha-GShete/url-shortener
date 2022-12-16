package com.demo.urlShorterDemo.schedule

import org.springframework.stereotype.Component
import java.util.HashMap
import com.demo.urlShorterDemo.model.Url
import com.demo.urlShorterDemo.repository.UrlDaoImpl
import com.demo.urlShorterDemo.util.UrlCommonFetchUtil
import java.time.LocalDateTime
import java.util.HashSet
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.beans.factory.annotation.Autowired
import java.util.List

@Component
class ScheduleConfiguration {

    @Autowired
    private val lUrlDaoImpl: UrlDaoImpl? = null

    @Autowired
    private val lUrlCommonFetchUtil: UrlCommonFetchUtil? = null

    @Scheduled(cron = "0 30 4 15 * ?")
    fun doScheduledWork() {
        val lUrlList: List<Url?>? = lUrlDaoImpl?.fetchAllUrl()
        val lUrlSet: Set<String?> = HashSet()
        for (url in lUrlList!!) {
            val lUrlMap = lUrlCommonFetchUtil!!.urlCache
            lUrlMap?.cleanUp()
            val lShortUrlMap = lUrlCommonFetchUtil!!.shortUrlCache
            lShortUrlMap?.cleanUp()
            if (!lUrlCommonFetchUtil.validateUrl(url!!.getLongUrl())) {
                lUrlSet.plus(url.getLongUrl())
            } else if (!lUrlCommonFetchUtil.validateExpireDate(url.getExpireDateTime())) {
                val lUrl = Url()
                lUrl.setLongUrl(url.getLongUrl())
                lUrl.setCreationDateTime(LocalDateTime.now())
                lUrl.setExpireDateTime(LocalDateTime.now().plusMonths(6))
                lUrl.setShortUrl(lUrlCommonFetchUtil.randomString())
                lUrlDaoImpl?.saveUrl(lUrl, 0)
                url.getLongUrl()?.let { lUrlMap?.put(it, populateHashMapForUrl(url)) }
                url.getShortUrl()?.let { lShortUrlMap?.put(it, url.getLongUrl()!!) }
            } else {
                url.getLongUrl()?.let { lUrlMap?.put(it, populateHashMapForUrl(url)) }
                url.getShortUrl()?.let { lShortUrlMap?.put(it, url.getLongUrl()!!) }

            }
        }
        if (lUrlSet.isNotEmpty()) {
            lUrlDaoImpl?.deleteUrlFromDb(lUrlSet)
        }
    }

    fun populateHashMapForUrl(url: Url?): HashMap<String, Any?> {
        val lMap = HashMap<String, Any?>()
        lMap[UrlCommonFetchUtil.EXPIRE_DATE] = url!!.getExpireDateTime()
        lMap[UrlCommonFetchUtil.RANDOM_URL] = url.getShortUrl()
        return lMap
    }


}