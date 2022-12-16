package com.demo.urlShorterDemo

import com.demo.urlShorterDemo.exception.ErrorMessage
import com.demo.urlShorterDemo.exception.InvalidParametersException
import com.demo.urlShorterDemo.repository.UrlShortenerRequest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UrlControllerTests(@Autowired val testRestTemplate: TestRestTemplate) {

	val url : String = "https://github.com"

	@Test
	fun `Assert create shortener url, content and status code`() {
		val urlRequest = UrlShortenerRequest(url)
		val entity = testRestTemplate.postForEntity("/api/url-shortener/", urlRequest, String::class.java)
		assertNotNull(entity)
		assertEquals(HttpStatus.CREATED, entity.statusCode)
		assertNotNull(entity.body)
	}


	@Test
	fun `Assert create shortener url, 400 status code`() {
		val urlRequest = UrlShortenerRequest("https://github.com")
		val entity = testRestTemplate.postForEntity("/api/url-shortener/", urlRequest, String::class.java)
		assertNotNull(entity)
		assertEquals(entity.statusCode, HttpStatus.BAD_REQUEST)
		val mapper = ObjectMapper().registerModule(KotlinModule())
		val apiError = mapper.readValue(entity.body.toString(), InvalidParametersException::class.java)
		assertNotNull(apiError)
	}

	@Test
	fun `Assert get full url, content and status code`() {
		val urlRequest = UrlShortenerRequest(url)
		val entity = testRestTemplate.postForEntity("/api/url-shortener/", urlRequest, String::class.java)
		assertNotNull(entity)
		assertEquals(HttpStatus.CREATED, entity.statusCode)
		assertNotNull(entity.body)

		val getEntity = testRestTemplate.getForEntity("/api/url-shortener/"+entity.body, String::class.java)
		assertNotNull(getEntity)
		assertEquals(HttpStatus.FOUND, getEntity.statusCode)
		assertNotNull(getEntity.body)
		assertEquals(url, getEntity.body)
	}

	@Test
	fun `Assert get full url invalidEncode, content and 404 status code`() {
		val getEntity = testRestTemplate.getForEntity("/api/url-shortener/123", String::class.java)
		assertNotNull(getEntity)
		assertEquals(HttpStatus.NOT_FOUND, getEntity.statusCode)

	}

	@Test
	fun `Assert get full url validEncode, content and 404 status code`() {
		val getEntity = testRestTemplate.getForEntity("/api/url-shortener/abcfdg", String::class.java)
		assertNotNull(getEntity)
		assertEquals(HttpStatus.NOT_FOUND, getEntity.statusCode)

	}

}
