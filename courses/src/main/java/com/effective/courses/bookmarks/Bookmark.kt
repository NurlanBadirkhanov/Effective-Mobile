package com.effective.courses.bookmarks

fun interface BookmarkChecker { fun isBookmarked(id: String): Boolean }
fun interface BookmarkToggler { fun toggle(id: String): Boolean }