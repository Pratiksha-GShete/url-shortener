package com.demo.urlShorterDemo.controller

import com.demo.urlShorterDemo.exception.InvalidParameterException
import com.demo.urlShorterDemo.model.LongUrl
import com.demo.urlShorterDemo.service.UrlShortenerServiceImpl
import com.demo.urlShorterDemo.util.UrlCommonFetchUtil
import java.io.IOException;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import io.swagger.v3.oas.annotations.Parameter


@RestController
class UrlApiController {

    private val logger: Logger = LoggerFactory.getLogger(UrlApiController::class.java)

    @Autowired
    private val lUrlShortenerServiceImpl: UrlShortenerServiceImpl? = null

    @ApiOperation(value = "API to get a Full URL.")
    @ApiResponses(
        value = [ApiResponse(code = 200, message = "Ok"), ApiResponse(
            code = 201,
            message = "Object created"
        ), ApiResponse(code = 400, message = "Request invalid"), ApiResponse(
            code = 409,
            message = "Object already exists"
        ), ApiResponse(code = 500, message = "Internal server error")]
    )
    @RequestMapping(value = ["/api/url-shortener/url={url}"], method = [RequestMethod.POST])
    fun createUrlShortForLongUrl(@Parameter(description = "long Url to generate the short Url") @Valid @RequestBody longUrl: LongUrl): ResponseEntity<String?>? {
        logger.info("Method : POST /api/url-shortener/url=" + longUrl.fullUrl.toString() + "/")
        val shortUrl: String? = lUrlShortenerServiceImpl?.createShortUrl(longUrl)
        return if (shortUrl != null) {
            ResponseEntity(UrlCommonFetchUtil.localAddress.toString() + "" + shortUrl, HttpStatus.CREATED)
        } else ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @ApiOperation(value = "", hidden = true)
    @RequestMapping(value = ["/api/url-shortener/{shortUrl}"], method = [RequestMethod.GET])
    @Throws(
        IOException::class
    )
    fun redirectShortUrl(response: HttpServletResponse, @PathVariable("shortUrl") shortUrl: String?) {
        val longUrl: String? = lUrlShortenerServiceImpl?.fetchLongUrl(shortUrl)
        if (longUrl != null) {
            response.sendRedirect(longUrl)
        } else {
            throw InvalidParameterException("Invalid Url")
        }
    }

    /*
	 * @ApiOperation(value = "API to get a shortener URL.")
	 *
	 * @RequestMapping(value="/api/short/{shortUrl}", method=RequestMethod.GET)
	 * public ResponseEntity<String> fetchShortUrl(@PathVariable("longUrl") String
	 * longUrl) throws IOException {
	 * logger.info("Method : GET /api/shorturl/"+longUrl+"/"); String shortUrl =
	 * lUrlShortenerServiceImpl.fetchShortUrl(longUrl); if (shortUrl != null) {
	 * return new ResponseEntity<String>(shortUrl, HttpStatus.OK); } return new
	 * ResponseEntity<String>(HttpStatus.NOT_FOUND); }
	 */

    /*
	 * @ApiOperation(value = "API to get a shortener URL.")
	 *
	 * @RequestMapping(value="/api/short/{shortUrl}", method=RequestMethod.GET)
	 * public ResponseEntity<String> fetchShortUrl(@PathVariable("longUrl") String
	 * longUrl) throws IOException {
	 * logger.info("Method : GET /api/shorturl/"+longUrl+"/"); String shortUrl =
	 * lUrlShortenerServiceImpl.fetchShortUrl(longUrl); if (shortUrl != null) {
	 * return new ResponseEntity<String>(shortUrl, HttpStatus.OK); } return new
	 * ResponseEntity<String>(HttpStatus.NOT_FOUND); }
	 */
    @ApiOperation(value = "API to get a Full URL.")
    @RequestMapping(value = ["/api/longurl/{shortUrl}"], method = [RequestMethod.GET])
    @Throws(
        IOException::class
    )
    fun fetchLongUrl(@PathVariable("shortUrl") shortUrl: String): ResponseEntity<String?>? {
        logger.info("Method : GET /api/longurl/$shortUrl/")
        val longUrl: String? = lUrlShortenerServiceImpl?.fetchLongUrl(shortUrl)
        return if (longUrl != null) {
            ResponseEntity(longUrl, HttpStatus.OK)
        } else ResponseEntity("Short Url is not present", HttpStatus.NOT_FOUND)
    }


}