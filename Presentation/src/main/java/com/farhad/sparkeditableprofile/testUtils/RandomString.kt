package com.farhad.sparkeditableprofile.testUtils

import kotlin.random.Random

class RandomString {
    fun get(): String {
        val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        val length = Random.nextInt(1, 30)
        return (1..length)
            .map { Random.nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString("")
    }
}