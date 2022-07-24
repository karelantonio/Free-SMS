/*
 * Copyright (c) 2022. by Karel, under GPLv2 see the "license page"
 */

package cu.kareldv.android.freesms.view.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import cu.kareldv.android.freesms.R
import cu.kareldv.android.freesms.databinding.ActivityMainBinding
import cu.kareldv.android.freesms.view.fragments.FragmentSend
import cu.kareldv.android.freesms.view.fragments.FragmentSettings
import cu.kareldv.android.freesms.view.fragments.FragmentStatus
import dagger.hilt.android.AndroidEntryPoint

/**
 * The Main Activity, wich contains the viewPager with the send, status and settings fragments
 */
@AndroidEntryPoint
class ActivityMain : AppCompatActivity() {
    /**
     * The View
     */
    lateinit var bind: ActivityMainBinding

    /**
     * Called when the activity is created
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bind.root)
        setSupportActionBar(bind.toolbar)

        setupToolbar()
    }

    /**
     * Setups the toolbar and viewpager
     */
    private fun setupToolbar() {

        val fragments = listOf(
            FragmentSend.newInstance(),
            FragmentStatus.newInstance(),
            FragmentSettings.newInstance()
        )
        val adapt = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int {
                return fragments.size
            }

            override fun createFragment(position: Int): Fragment {
                return fragments[position]
            }

        }
        bind.viewpager.adapter = adapt
        val titles = listOf(getString(R.string.str_send),
            getString(R.string.str_status),
            getString(R.string.str_settings))

        bind.tabs.setupWithViewPager(bind.viewpager, titles)
    }

    /**
     * Creates the Options Menu
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        MenuInflater(this).inflate(R.menu.main_menu, menu!!)
        menu.findItem(R.id.menuItemInfo).apply {
            setOnMenuItemClickListener {
                showInfoDialog()
                return@setOnMenuItemClickListener true
            }
        }
        return true
    }

    /**
     * Show the Info dialg
     */
    private fun showInfoDialog() {
        MaterialAlertDialogBuilder(this).apply {
            setTitle(R.string.dialog_info_title)
            setMessage(R.string.dialog_info_msg)
            setPositiveButton(R.string.dialog_info_gotoapklis) { dlg, i ->
                startActivity(Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://apklis.cu/application/cu.kareldv.android.freesms")))
            }
            setNegativeButton(R.string.dialog_info_twitter) { dlg, i ->
                startActivity(Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://twitter.com/karel_zaldivar")))
            }
            //setNeutralButton(R.string.dialog_info_thanks)
            create().show()
        }
    }
}

//Extensiones
fun TabLayout.setupWithViewPager(viewPager2: ViewPager2, labels: List<String>) {
    if (labels.size != viewPager2.adapter?.itemCount)
        throw Exception("The size of the labels and the pages in the viewpager should be the same")
    TabLayoutMediator(this, viewPager2) { tab, pos ->
        tab.text = labels[pos]
    }.attach()
}