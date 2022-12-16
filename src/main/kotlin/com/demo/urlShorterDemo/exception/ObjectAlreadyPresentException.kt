package com.demo.urlShorterDemo.exception

import java.lang.RuntimeException

class ObjectAlreadyPresentException(override val message:String): RuntimeException() {
}