
package com.st.nfc.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.st.CMEApplication;
import com.st.R;
import com.st.nfc.BasicActivity;

/**
 * 此视图将允许用户编辑全局首选项。
 *
 * This view will let the user edit global preferences.
 * @author Gerhard Klostermeier
 */
public class PreferencesActivity extends BasicActivity {

    /**
     * Enumeration with all preferences. This enumeration implements
     * "toString()" so it can be used to access the shared preferences (e.g.
     * SharedPreferences.getBoolean(Pref.AutoReconnect.toString(), false)).
     */
    public enum Preference {
        AutoReconnect("auto_reconnect"),
        SaveLastUsedKeyFiles("save_last_used_key_files"),
        UseCustomSectorCount("use_custom_sector_count"),
        CustomSectorCount("custom_sector_count"),
        UseInternalStorage("use_internal_storage"),
        RetryAuthentication("retry_authentication");
        // Add more preferences here (comma separated).

        private final String text;

        private Preference(final String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    CheckBox mPrefAutoReconnect;
    CheckBox mPrefSaveLastUsedKeyFiles;
    CheckBox mUseCustomSectorCount;
    CheckBox mRetryAuthentication;
    CheckBox mUseInternalStorage;
    EditText mCustomSectorCount;

    /**
     * Initialize the preferences with the last stored ones.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_nfc_preferences);

        // Get preferences (init. the member variables).
        mPrefAutoReconnect = (CheckBox) findViewById(
                R.id.checkBoxPreferencesAutoReconnect);
        mPrefSaveLastUsedKeyFiles = (CheckBox) findViewById(
                R.id.checkBoxPreferencesSaveLastUsedKeyFiles);
        mUseCustomSectorCount = (CheckBox) findViewById(
                R.id.checkBoxPreferencesUseCustomSectorCount);
        mCustomSectorCount = (EditText) findViewById(
                R.id.editTextPreferencesCustomSectorCount);
        mUseInternalStorage = (CheckBox) findViewById(
                R.id.checkBoxPreferencesUseInternalStorage);
        mRetryAuthentication = (CheckBox) findViewById(
                R.id.checkBoxPreferencesRetryAuthentication);

        // Assign the last stored values.
        SharedPreferences pref = CMEApplication.getPreferences();
        mPrefAutoReconnect.setChecked(pref.getBoolean(
                Preference.AutoReconnect.toString(), false));
        mPrefSaveLastUsedKeyFiles.setChecked(pref.getBoolean(
                Preference.SaveLastUsedKeyFiles.toString(), true));
        mUseCustomSectorCount.setChecked(pref.getBoolean(
                Preference.UseCustomSectorCount.toString(), false));
        mCustomSectorCount.setEnabled(mUseCustomSectorCount.isChecked());
        mCustomSectorCount.setText("" + pref.getInt(
                Preference.CustomSectorCount.toString(), 16));
        mUseInternalStorage.setChecked(pref.getBoolean(
                Preference.UseInternalStorage.toString(), false));
        mRetryAuthentication.setChecked(pref.getBoolean(
                Preference.RetryAuthentication.toString(), true));
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_nfc_preferences;
    }

    @Override
    public void onInitView() {

    }

    /**
     * Show information on the "auto reconnect" preference.
     * @param view The View object that triggered the method
     * (in this case the info on auto reconnect button).
     */
    public void onShowAutoReconnectInfo(View view) {
        new AlertDialog.Builder(this)
            .setTitle(R.string.dialog_auto_reconnect_title)
            .setMessage(R.string.dialog_auto_reconnect)
            .setIcon(android.R.drawable.ic_dialog_info)
            .setPositiveButton(R.string.action_ok,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                             // Do nothing.
                        }
            }).show();
    }

    /**
     * Enable or disable the custom sector count text box according to the
     * checkbox state..
     * @param view The View object that triggered the method
     * (in this case the use custom sector count checkbox).
     */
    public void onUseCustomSectorCountChanged(View view) {
        mCustomSectorCount.setEnabled(mUseCustomSectorCount.isChecked());
    }

    /**
     * Show information on the "use custom sector count" preference.
     * @param view The View object that triggered the method
     * (in this case the info on custom sector count button).
     */
    public void onShowCustomSectorCountInfo(View view) {
        new AlertDialog.Builder(this)
            .setTitle(R.string.dialog_custom_sector_count_title)
            .setMessage(R.string.dialog_custom_sector_count)
            .setIcon(android.R.drawable.ic_dialog_info)
            .setPositiveButton(R.string.action_ok,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Do nothing.
                        }
            }).show();
    }

    /**
     * Show information on the "use internal storage" preference.
     * @param view The View object that triggered the method
     * (in this case the info on use internal storage button).
     */
    public void onShowUseInternalStorageInfo(View view) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.dialog_use_internal_storage_title)
                .setMessage(R.string.dialog_use_internal_storage)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setPositiveButton(R.string.action_ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Do nothing.
                            }
                        }).show();
    }

    /**
     * Show information on the "retry authentication" preference.
     * @param view The View object that triggered the method
     * (in this case the info on use internal storage button).
     */
    public void onShowRetryAuthenticationInfo(View view) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.dialog_retry_authentication_title)
                .setMessage(R.string.dialog_retry_authentication)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setPositiveButton(R.string.action_ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Do nothing.
                            }
                        }).show();
    }

    /**
     * Save the preferences (to the application context,
     * {@link CMEApplication#getPreferences()}).
     * @param view The View object that triggered the method
     * (in this case the save button).
     */
    public void onSave(View view) {
        // Check if settings are valid.
        int customSectorCount = Integer.parseInt(
                mCustomSectorCount.getText().toString());
        if (customSectorCount > 40 || customSectorCount <= 0) {
            Toast.makeText(this, R.string.info_sector_count_error,
                    Toast.LENGTH_LONG).show();
            return;
        }

        // Save preferences.
        SharedPreferences.Editor edit = CMEApplication.getPreferences().edit();
        edit.putBoolean(Preference.AutoReconnect.toString(),
                mPrefAutoReconnect.isChecked());
        edit.putBoolean(Preference.SaveLastUsedKeyFiles.toString(),
                mPrefSaveLastUsedKeyFiles.isChecked());
        edit.putBoolean(Preference.UseCustomSectorCount.toString(),
                mUseCustomSectorCount.isChecked());
        edit.putBoolean(Preference.RetryAuthentication.toString(),
                mRetryAuthentication.isChecked());
        edit.putBoolean(Preference.UseInternalStorage.toString(),
                mUseInternalStorage.isChecked());
        edit.putInt(Preference.CustomSectorCount.toString(),
                customSectorCount);
        edit.apply();

        // Exit the preferences view.
        finish();
    }

    /**
     * Exit the preferences view without saving anything.
     * @param view The View object that triggered the method
     * (in this case the cancel button).
     */
    public void onCancel(View view) {
        finish();
    }
}