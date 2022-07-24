/*
 * Copyright (c) 2022. by Karel, under GPLv2 see the "license page"
 */

package cu.kareldv.android.freesms.view.dialogs

import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import io.reactivex.functions.Consumer
import java.net.Proxy

class NewProxyBottomSheetFragment : BottomSheetDialogFragment() {
    lateinit var onProxySpecified: Consumer<ProxyInfo>


    companion object {
        fun newInstance() = NewProxyBottomSheetFragment()
    }
}

data class ProxyInfo(
    val host: String,
    val port: Int,
    val type: Proxy.Type,
)