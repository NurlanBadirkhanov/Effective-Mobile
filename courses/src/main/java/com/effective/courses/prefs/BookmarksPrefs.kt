package com.effective.courses.prefs

import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookmarksPrefs @Inject constructor(
    private val sp: SharedPreferences
) {
    private val KEY = "bookmarks_ids"

    fun isBookmarked(id: String): Boolean = getAll().contains(id)

    fun toggle(id: String): Boolean {
        val set = getAll().toMutableSet()
        val now = if (set.remove(id)) false else { set.add(id); true }
        sp.edit().putStringSet(KEY, set).apply()
        return now
    }

    fun getAll(): Set<String> = sp.getStringSet(KEY, emptySet()) ?: emptySet()
}
