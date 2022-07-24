/*
 * Copyright (c) 2022. by Karel, under GPLv2 see the "license page"
 */

package cu.kareldv.android.freesms.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cu.kareldv.android.freesms.core.api.io.TextbeltApi
import cu.kareldv.android.freesms.core.api.vo.MessageVO
import cu.kareldv.android.freesms.core.api.vo.toHistoryEntity
import cu.kareldv.android.freesms.core.utils.MProxySelector
import cu.kareldv.android.freesms.data.database.HistoryDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

@HiltViewModel
class FragmentSendViewModel
@Inject constructor(
    var textbeltApi: TextbeltApi,
    var mProxySelector: MProxySelector,
    var sendMessageUC: SendMessageUseCase,
    var historyDatabase: HistoryDatabase,
) : ViewModel() {

    val messageModel = MutableLiveData<MessageVO>()
    val neterrorModel = MutableLiveData<Exception>()
    val compositeDisposable = CompositeDisposable()

    fun sendMessage(
        key: String,
        msg: String,
        phone: String,
    ) {

        sendMessageUC(messageModel, neterrorModel, key, msg, phone, compositeDisposable)
    }

    fun postHistoryItem(historyVO: MessageVO, phone: String, msg: String) {
        historyDatabase.insert(historyVO.toHistoryEntity(phone, msg))
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}