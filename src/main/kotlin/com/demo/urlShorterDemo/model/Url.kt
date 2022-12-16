package com.demo.urlShorterDemo.model

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;


@Entity
@Table(name="url",uniqueConstraints = arrayOf(UniqueConstraint(columnNames = ["long_url", "short_url"])))
class Url{
    @Id
    @Column(name = "long_url", length = 1000)
    private var longUrl: String? = null

    @Column(name = "short_url")
    private var shortUrl: String? = null

    private var creationDateTime: LocalDateTime? = null

    private var expireDateTime: LocalDateTime? = null

    fun getLongUrl(): String? {
        return longUrl
    }

    fun setLongUrl(longUrl: String?) {
        this.longUrl = longUrl
    }

    fun getShortUrl(): String? {
        return shortUrl
    }

    fun setShortUrl(shortUrl: String?) {
        this.shortUrl = shortUrl
    }

    fun getCreationDateTime(): LocalDateTime? {
        return creationDateTime
    }

    fun setCreationDateTime(creationDateTime: LocalDateTime?) {
        this.creationDateTime = creationDateTime
    }

    fun getExpireDateTime(): LocalDateTime? {
        return expireDateTime
    }

    fun setExpireDateTime(expireDateTime: LocalDateTime?) {
        this.expireDateTime = expireDateTime
    }

}
