/*
 * Copyright (c) 2022. by Karel, under GPLv2 see the "license page"
 */

package cu.kareldv.android.freesms.core.api.vo

import com.google.gson.annotations.SerializedName

/**
 * Not used but returned by the server when calling to the quota remaining endpoint (see the API)
 */
data class QuotaRemainingVO(
    @SerializedName("quotaRemaining")
    var quotaRemaining: Int = 0,
    @SerializedName("success")
    var success: Boolean = true,
    @SerializedName("error")
    var error: String,
)