/*
 * Copyright (c) 2022. by Karel, under GPLv2 see the "license page"
 */

package cu.kareldv.android.freesms.model

import androidx.lifecycle.MutableLiveData
import cu.kareldv.android.freesms.core.api.io.TextbeltApi
import cu.kareldv.android.freesms.core.api.vo.MessageVO
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers.io
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SendMessageUseCase
@Inject constructor(
    var textbeltApi: TextbeltApi,
) {

    operator fun invoke(
        messageModel: MutableLiveData<MessageVO>,
        neterrorModel: MutableLiveData<Exception>,
        key: String,
        msg: String,
        phone: String,
        compositeDisposable: CompositeDisposable,
    ) {

        compositeDisposable.add(textbeltApi.sendText(phone, msg, key)
            .subscribeOn(io())
            .subscribe({
                messageModel.postValue(it)
            }, {
                neterrorModel.postValue(it as Exception?)
            }))

    }

}