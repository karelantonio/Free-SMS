/*
 * Copyright (c) 2022. by Karel, under GPLv2 see the "license page"
 */

package cu.kareldv.android.freesms.core.api.io

import cu.kareldv.android.freesms.core.api.vo.MessageVO
import cu.kareldv.android.freesms.core.api.vo.StatusVO
import io.reactivex.Single
import retrofit2.http.*

/**
 * See https://textbelt.com to get more info about the API
 */
interface TextbeltApi {

    /**
     * Sends a message with the given phone, message and key
     */
    @POST("/text")
    @FormUrlEncoded
    fun sendText(
        @Field("phone") phone: String,
        @Field("message") message: String,
        @Field("key") key: String,
    ): Single<MessageVO>

    /**
     * Gets the state of the message
     */
    @GET("/status/{textId}")
    fun deliveryStatus(
        @Path("textId") textId: String,
    ): Single<StatusVO>

}