/*
 * Copyright (c) 2022. by Karel, under GPLv2 see the "license page"
 */

package cu.kareldv.android.freesms.view.activities

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import cu.kareldv.android.freesms.databinding.ActivityEditProxiesBinding
import cu.kareldv.android.freesms.model.ActivityEditProxiesViewModel
import cu.kareldv.android.freesms.view.dialogs.NewProxyBottomSheetFragment
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.functions.Consumer

/**
 * Also ignore by now :)
 */
@AndroidEntryPoint
class ActivityEditProxies : AppCompatActivity() {
    private val viewModel by viewModels<ActivityEditProxiesViewModel>()
    private lateinit var bind: ActivityEditProxiesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityEditProxiesBinding.inflate(layoutInflater)
        setContentView(bind.root)
        setSupportActionBar(bind.toolbar)
        bind.editFab.setOnClickListener {
            NewProxyBottomSheetFragment.newInstance().apply {
                onProxySpecified = Consumer {

                    viewModel.insertInDB(/*it*/)
                    updateProxies()

                }
                show(supportFragmentManager, "NewProxy")
            }
        }
    }

    fun updateProxies() {

    }
}