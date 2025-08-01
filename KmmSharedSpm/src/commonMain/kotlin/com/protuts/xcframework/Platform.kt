package com.protuts.xcframework

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform