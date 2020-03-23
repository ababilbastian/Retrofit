package id.putraprima.retrofit.ui;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import id.putraprima.retrofit.R;

public class SettingFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences,rootKey);
    }
}
