package com.demo.urlShorterDemo.exception

import java.lang.RuntimeException
class InvalidParametersException(override val message:String): RuntimeException() {
}