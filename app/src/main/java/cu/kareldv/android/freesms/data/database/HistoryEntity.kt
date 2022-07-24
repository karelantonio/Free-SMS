/*
 * Copyright (c) 2022. by Karel, under GPLv2 see the "license page"
 */

package cu.kareldv.android.freesms.data.database

import android.content.ContentValues
import cu.kareldv.android.freesms.core.api.vo.Status

data class HistoryEntity(
    val id: String,
    val to: String,
    val message: String,
    val lastKnownState: Status,
)

fun HistoryEntity.toContentValues(): ContentValues {
    val cv = ContentValues()

    cv.put(HistoryDatabase.ROW_0, id)
    cv.put(HistoryDatabase.ROW_1, to)
    cv.put(HistoryDatabase.ROW_2, message)
    cv.put(HistoryDatabase.ROW_3, lastKnownState.toString())

    return cv
}