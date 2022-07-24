/*
 * Copyright (c) 2022. by Karel, under GPLv2 see the "license page"
 */

package cu.kareldv.android.freesms.view.fragments

import android.content.*
import android.content.Context.CLIPBOARD_SERVICE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import cu.kareldv.android.freesms.R
import cu.kareldv.android.freesms.core.api.vo.Status
import cu.kareldv.android.freesms.data.database.HistoryEntity
import cu.kareldv.android.freesms.databinding.FragmentStatusFragmentBinding
import cu.kareldv.android.freesms.model.FragmentStatusViewModel
import cu.kareldv.android.freesms.view.adapter.StatusAdapter
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.functions.Consumer

/**
 * Fragment of the viewPager that show the history
 * @see cu.kareldv.android.freesms.view.activities.ActivityMain
 */
@AndroidEntryPoint
class FragmentStatus : Fragment() {

    companion object {
        fun newInstance() = FragmentStatus()
    }

    private val viewModel by viewModels<FragmentStatusViewModel>()
    private lateinit var bind: FragmentStatusFragmentBinding
    private lateinit var adapter: StatusAdapter

    /**
     * Called when the fragment is created
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireContext().registerReceiver(object : BroadcastReceiver() {
            override fun onReceive(ctx: Context?, intnt: Intent?) {
                viewModel.fetchItemsAndSort()
            }
        }, IntentFilter("action_update_items"))
    }

    /**
     * creates the view
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        adapter = StatusAdapter(inflater)
        adapter.context = requireContext()
        adapter.onItemClicked = Consumer(this@FragmentStatus::onItemClicked)
        bind = FragmentStatusFragmentBinding.inflate(inflater, container, false)
        return bind.root
    }

    /**
     * Called after onCreateView, binds the UI and sets the event Handlers
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.items.observe(this.viewLifecycleOwner) {
            adapter.items.clear()
            adapter.items.addAll(it)
            adapter.notifyDataSetChanged()
        }
        bind.root.setOnRefreshListener {
            bind.root.isRefreshing = false
            viewModel.fetchItemsAndSort()
        }
        bind.statusRecycler.adapter = adapter
        viewModel.errorsLiveData.observe(viewLifecycleOwner) {
            MaterialAlertDialogBuilder(requireContext()).apply {
                setTitle(R.string.dialog_statuserr_title)
                setMessage(String.format(
                    getString(R.string.dialog_statuserr_msg), it.toString()
                ))
                create().show()
            }
        }
        viewModel.statusLiveData.observe(viewLifecycleOwner) {
            MaterialAlertDialogBuilder(requireContext()).apply {
                setTitle(R.string.dialog_statusresp_title)
                setMessage(when (it.second.status) {
                    Status.SENDING -> R.string.dialog_statusresp_status_sending
                    Status.UNKNOWN -> R.string.dialog_statusresp_status_unknown
                    Status.FAILED -> R.string.dialog_statusresp_status_failed
                    Status.DELIVERED -> R.string.dialog_statusresp_status_delivered
                    Status.SENT -> R.string.dialog_statusresp_status_sent
                    //else -> throw RuntimeException("None of the before items matched !")
                })
                create().show()
            }
        }
        viewModel.fetchItemsAndSort()
    }

    /**
     * Called when an item from the list is clicked
     */
    private fun onItemClicked(historyEntity: HistoryEntity) {
        MaterialAlertDialogBuilder(requireContext()).apply {
            setTitle(R.string.dialog_items_pickoption_title)
            setItems(R.array.array_dialog_pickoption_items) { _, i ->
                when (i) {
                    0 -> copy(historyEntity.to)
                    1 -> copy(historyEntity.message)
                    2 -> getState(historyEntity)
                    3 -> viewModel.deleteItem(historyEntity)
                    4 -> viewModel.clearItems()
                }
            }
            create().show()
        }
    }

    /**
     * Copies the given value into the clipboard
     */
    fun copy(value: String) {
        val cp = requireContext().getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        cp.setPrimaryClip(
            ClipData(ClipDescription("noinfo", arrayOf("text/plain")),
                ClipData.Item(value))
        )
        Snackbar.make(bind.root, R.string.snack_copiedcorrectly, Snackbar.LENGTH_SHORT).show()
    }

    /**
     * Requests the server the state of the given message
     */
    fun getState(historyEntity: HistoryEntity) {
        viewModel.fetchStatus(historyEntity)
    }

}