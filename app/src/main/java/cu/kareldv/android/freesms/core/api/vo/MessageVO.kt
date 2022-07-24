/*
 * Copyright (c) 2022. by Karel, under GPLv2 see the "license page"
 */

package cu.kareldv.android.freesms.core.api.vo

import com.google.gson.annotations.SerializedName
import cu.kareldv.android.freesms.data.database.HistoryEntity

/**
 * Returned by the server when sent a message (See the API)
 * @see cu.kareldv.android.freesms.core.api.io.TextbeltApi.sendText
 */
data class MessageVO(
    @SerializedName("textId")
    var textId: String,
    @SerializedName("quotaRemaining")
    var quotaRemaining: Int = 0,
    @SerializedName("success")
    var success: Boolean = true,
    @SerializedName("error")
    var error: String,
)

fun MessageVO.toHistoryEntity(phone: String, msg: String): HistoryEntity {
    return HistoryEntity(
        id = textId,
        to = phone,
        message = msg,
        lastKnownState = Status.SENDING
    )
}