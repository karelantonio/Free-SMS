/*
 * Copyright (c) 2022. by Karel, under GPLv2 see the "license page"
 */

package cu.kareldv.android.freesms.core.utils

import java.io.IOException
import java.net.Proxy
import java.net.ProxySelector
import java.net.SocketAddress
import java.net.URI

class MProxySelector : ProxySelector() {
    var proxy: Proxy? = null

    override fun select(uri: URI?): MutableList<Proxy> {
        if (proxy != null) {
            return mutableListOf(proxy!!)
        }
        return mutableListOf()
    }

    override fun connectFailed(
        uri: URI?,
        sockAddr: SocketAddress?,
        exc: IOException?,
    ) {
        TODO("Not yet implemented")
    }
}