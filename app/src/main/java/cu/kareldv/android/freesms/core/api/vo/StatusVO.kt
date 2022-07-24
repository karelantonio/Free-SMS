/*
 * Copyright (c) 2022. by Karel, under GPLv2 see the "license page"
 */

package cu.kareldv.android.freesms.core.api.vo

import com.google.gson.annotations.SerializedName

/**
 * Returned by the server when requestes the status endpoint (See the API)
 * @see cu.kareldv.android.freesms.core.api.io.TextbeltApi.deliveryStatus
 */
data class StatusVO(
    @SerializedName("status")
    var status: Status,
)