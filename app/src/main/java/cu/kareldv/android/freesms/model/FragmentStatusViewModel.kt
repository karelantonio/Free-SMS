/*
 * Copyright (c) 2022. by Karel, under GPLv2 see the "license page"
 */

package cu.kareldv.android.freesms.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cu.kareldv.android.freesms.core.api.io.TextbeltApi
import cu.kareldv.android.freesms.core.api.vo.Status
import cu.kareldv.android.freesms.core.api.vo.StatusVO
import cu.kareldv.android.freesms.data.database.HistoryDatabase
import cu.kareldv.android.freesms.data.database.HistoryEntity
import cu.kareldv.android.freesms.data.database.toContentValues
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class FragmentStatusViewModel
@Inject constructor(
    val historyDatabase: HistoryDatabase,
    val api: TextbeltApi,
) : ViewModel() {

    val compositeD = CompositeDisposable()
    val errorsLiveData = MutableLiveData<Exception>()
    val statusLiveData = MutableLiveData<Pair<HistoryEntity, StatusVO>>()
    val items = MutableLiveData<List<HistoryEntity>>()
    val updateItems = MutableLiveData<Void?>()


    fun fetchItemsAndSort() {
        val list = mutableListOf<HistoryEntity>()
        with(historyDatabase.readableDatabase) {
            val curs = rawQuery("SELECT * FROM ${HistoryDatabase.TABLE_NAME}", arrayOfNulls(0))

            if (curs.moveToFirst()) {
                do {

                    list.add(HistoryEntity(
                        id = curs.getString(0),
                        to = curs.getString(1),
                        message = curs.getString(2),
                        lastKnownState = Status.valueOf(curs.getString(3))
                    ))

                } while (curs.moveToNext())
            }
            curs.close()
            close()
        }
        list.reverse()
        items.postValue(list)
    }

    fun fetchStatus(historyEntity: HistoryEntity) {
        compositeD.add(api.deliveryStatus(historyEntity.id)
            .subscribeOn(Schedulers.io())
            .subscribe({
                statusLiveData.postValue(Pair(historyEntity, it))
                if (it.status != historyEntity.lastKnownState) {
                    val newItem = HistoryEntity(
                        id = historyEntity.id,
                        to = historyEntity.to,
                        message = historyEntity.message,
                        lastKnownState = it.status
                    )
                    updateItem(newItem)
                    fetchItemsAndSort()
                }
            }, {
                if (it is Exception)
                    errorsLiveData.postValue(it)
            }))
    }

    fun updateItem(itm: HistoryEntity) {
        with(historyDatabase.writableDatabase) {
            update(HistoryDatabase.TABLE_NAME, itm.toContentValues(), "id=?", arrayOf(itm.id))
            close()
        }
    }

    fun deleteItem(entity: HistoryEntity) {
        historyDatabase.remove(entity.id)
        fetchItemsAndSort()
    }

    fun clearItems() {
        historyDatabase.clearAll()
        fetchItemsAndSort()
    }

    override fun onCleared() {
        super.onCleared()
        compositeD.dispose()
    }
}