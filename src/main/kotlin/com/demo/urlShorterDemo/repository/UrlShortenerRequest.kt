package com.demo.urlShorterDemo.repository

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiModelProperty
import com.fasterxml.jackson.annotation.JsonProperty

@ApiOperation(value = "URL shortener request object")
@ApiModel
data class UrlShortenerRequest (@JsonProperty("url") var url: String)