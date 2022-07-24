/*
 * Copyright (c) 2022. by Karel, under GPLv2 see the "license page"
 */

package cu.kareldv.android.freesms.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import cu.kareldv.android.freesms.R
import cu.kareldv.android.freesms.core.api.vo.Status
import cu.kareldv.android.freesms.data.database.HistoryEntity
import cu.kareldv.android.freesms.databinding.StatusRecyclerItemBinding
import io.reactivex.functions.Consumer

/**
 * The adapter of the history's recyclerview
 */
class StatusAdapter(val inflater: LayoutInflater) : Adapter<Holder>() {
    val items = mutableListOf<HistoryEntity>()
    var onItemClicked: Consumer<HistoryEntity> = Consumer<HistoryEntity> {}
    lateinit var context: Context

    /**
     * Creates a ViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(StatusRecyclerItemBinding.inflate(inflater), context)
    }

    /**
     * Binds the given holder with the UI
     */
    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.historyEntity = items.get(position)
        holder(onItemClicked)
    }

    /**
     * Gets the item count
     */
    override fun getItemCount() = items.size

}

/**
 * --Holder--
 */
class Holder(
    val statusRecyclerItemBinding: StatusRecyclerItemBinding,
    val context: Context,
) : ViewHolder(statusRecyclerItemBinding.root) {
    var historyEntity: HistoryEntity? = null

    operator fun invoke(onItemClicked: Consumer<HistoryEntity>) {
        statusRecyclerItemBinding.statusItemTo.text = historyEntity?.to
        statusRecyclerItemBinding.statusItemMessage.text = historyEntity?.message

        statusRecyclerItemBinding.statusItemIcon.setImageDrawable(AppCompatResources.getDrawable(
            context,
            when (historyEntity?.lastKnownState) {
                Status.SENDING -> R.drawable.ic_baseline_access_time_24
                Status.SENT -> R.drawable.ic_baseline_send_24
                Status.DELIVERED -> R.drawable.ic_baseline_done_all_24
                Status.FAILED -> R.drawable.ic_baseline_warning_24
                Status.UNKNOWN -> R.drawable.ic_baseline_sentiment_very_dissatisfied_24
                else -> R.drawable.ic_baseline_sentiment_very_dissatisfied_24
            }))

        statusRecyclerItemBinding.root.setOnClickListener {
            onItemClicked.accept(historyEntity!!)
        }
    }
}