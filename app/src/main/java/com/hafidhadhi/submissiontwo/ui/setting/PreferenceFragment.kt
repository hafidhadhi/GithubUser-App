package com.hafidhadhi.submissiontwo.ui.setting

import android.app.AlarmManager
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.hafidhadhi.submissiontwo.R
import com.hafidhadhi.submissiontwo.util.triggerReminder
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class PreferenceFragment : PreferenceFragmentCompat() {

    @Inject
    lateinit var alarmManager: AlarmManager

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preference, rootKey)
        setupReminderPref()
        setupChangeLangPref()
    }

    private fun setupReminderPref() {
        val prefKey = getString(R.string.reminder_pref_key)
        findPreference<SwitchPreferenceCompat>(prefKey)?.apply {
            setOnPreferenceChangeListener { preference, newValue ->
                val enabled = newValue as Boolean? ?: false
                val triggerTime = alarmManager.triggerReminder(requireContext(), enabled)
                Log.d(this::class.simpleName,
                    Calendar.getInstance().apply { timeInMillis = triggerTime ?: 0 }
                        .get(Calendar.HOUR_OF_DAY).toString()
                )
                true
            }
        }
    }

    private fun setupChangeLangPref() {
        val prefKey = getString(R.string.change_language_pref_key)
        findPreference<Preference>(prefKey)?.apply {
            summary = Locale.getDefault().displayLanguage
            setOnPreferenceClickListener {
                val intent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                startActivity(intent)
                true
            }
        }
    }
}
