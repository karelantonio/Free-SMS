/*
 * Copyright (c) 2022. by Karel, under GPLv2 see the "license page"
 */

package cu.kareldv.android.freesms.view.fragments

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import cu.kareldv.android.freesms.R

/**
 * Fragment of the viewpager that show the settings
 * @see cu.kareldv.android.freesms.view.activities.ActivityMain
 */
class FragmentSettings : PreferenceFragmentCompat() {

    companion object {
        fun newInstance() = FragmentSettings()
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences)
    }
}