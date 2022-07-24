/*
 * Copyright (c) 2022. by Karel, under GPLv2 see the "license page"
 */

package cu.kareldv.android.freesms.model

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ActivityEditProxiesViewModel
@Inject constructor(
    //proxyDB: ProxiesDatabase,
) : ViewModel() {
    fun insertInDB(/*it: ProxyInfo*/) {

    }
}