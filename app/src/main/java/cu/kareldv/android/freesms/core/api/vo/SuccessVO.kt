/*
 * Copyright (c) 2022. by Karel, under GPLv2 see the "license page"
 */

package cu.kareldv.android.freesms.core.api.vo

import com.google.gson.annotations.SerializedName

/**
 * Not Used
 */
data class SuccessVO(
    @SerializedName("success")
    var success: Boolean = true,
    @SerializedName("error")
    var error: String,
)