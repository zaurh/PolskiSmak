package com.zaurh.polskismak.data.local

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/**
 * Version 1 of the app cached recipes and favorites in a Room database called "meals".
 * This reads the favorite ids out of it once, so users keep their favorites after updating,
 * and then deletes the old database.
 */
class LegacyFavoritesReader @Inject constructor(
    @param:ApplicationContext private val context: Context,
) {
    fun readAndDelete(): Set<Int> {
        val dbFile = context.getDatabasePath(LEGACY_DB_NAME)
        if (!dbFile.exists()) return emptySet()

        val ids = runCatching {
            SQLiteDatabase.openDatabase(dbFile.path, null, SQLiteDatabase.OPEN_READONLY).use { db ->
                db.rawQuery("SELECT mealId FROM favorites", null).use { cursor ->
                    buildSet { while (cursor.moveToNext()) add(cursor.getInt(0)) }
                }
            }
        }.getOrDefault(emptySet())

        context.deleteDatabase(LEGACY_DB_NAME)
        return ids
    }

    private companion object {
        const val LEGACY_DB_NAME = "meals"
    }
}
