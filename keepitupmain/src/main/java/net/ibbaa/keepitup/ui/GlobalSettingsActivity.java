/*
 * Copyright (c) 2021. Alwin Ibba
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.ibbaa.keepitup.ui;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.google.android.material.switchmaterial.SwitchMaterial;

import net.ibbaa.keepitup.R;
import net.ibbaa.keepitup.logging.Log;
import net.ibbaa.keepitup.resources.PreferenceManager;
import net.ibbaa.keepitup.resources.PreferenceSetup;
import net.ibbaa.keepitup.service.IFileManager;
import net.ibbaa.keepitup.ui.dialog.FileChooseDialog;
import net.ibbaa.keepitup.ui.dialog.SettingsInput;
import net.ibbaa.keepitup.ui.dialog.SettingsInputDialog;
import net.ibbaa.keepitup.ui.validation.ConnectCountFieldValidator;
import net.ibbaa.keepitup.ui.validation.PingCountFieldValidator;
import net.ibbaa.keepitup.util.BundleUtil;
import net.ibbaa.keepitup.util.FileUtil;
import net.ibbaa.keepitup.util.NumberUtil;
import net.ibbaa.keepitup.util.StringUtil;

import java.io.File;
import java.util.Collections;
import java.util.List;

public class GlobalSettingsActivity extends SettingsInputActivity {

    private TextView pingCountText;
    private TextView connectCountText;
    private SwitchMaterial notificationInactiveNetworkSwitch;
    private TextView notificationInactiveNetworkOnOffText;
    private SwitchMaterial downloadExternalStorageSwitch;
    private TextView downloadExternalStorageOnOffText;
    private TextView downloadFolderText;
    private SwitchMaterial downloadKeepSwitch;
    private TextView downloadKeepOnOffText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        setContentView(R.layout.activity_global_settings);
        preparePingCountField();
        prepareConnectCountField();
        prepareNotificationInactiveNetworkSwitch();
        prepareDownloadExternalStorageSwitch();
        prepareDownloadFolderField();
        prepareDownloadKeepSwitch();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_global_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_action_activity_global_settings_reset) {
            Log.d(GlobalSettingsActivity.class.getName(), "menu_action_activity_global_settings_reset triggered");
            PreferenceSetup preferenceSetup = new PreferenceSetup(this);
            preferenceSetup.removeGlobalSettings();
            recreateActivity();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void preparePingCountField() {
        Log.d(GlobalSettingsActivity.class.getName(), "preparePingCountField");
        PreferenceManager preferenceManager = new PreferenceManager(this);
        pingCountText = findViewById(R.id.textview_activity_global_settings_ping_count);
        setPingCount(String.valueOf(preferenceManager.getPreferencePingCount()));
        CardView pingCountCardView = findViewById(R.id.cardview_activity_global_settings_ping_count);
        pingCountCardView.setOnClickListener(this::showPingCountInputDialog);
    }

    private void prepareConnectCountField() {
        Log.d(GlobalSettingsActivity.class.getName(), "prepareConnectCountField");
        PreferenceManager preferenceManager = new PreferenceManager(this);
        connectCountText = findViewById(R.id.textview_activity_global_settings_connect_count);
        setConnectCount(String.valueOf(preferenceManager.getPreferenceConnectCount()));
        CardView connectCountCardView = findViewById(R.id.cardview_activity_global_settings_connect_count);
        connectCountCardView.setOnClickListener(this::showConnectCountInputDialog);
    }

    private void prepareNotificationInactiveNetworkSwitch() {
        Log.d(GlobalSettingsActivity.class.getName(), "prepareNotificationInactiveNetworkSwitch");
        PreferenceManager preferenceManager = new PreferenceManager(this);
        notificationInactiveNetworkSwitch = findViewById(R.id.switch_activity_global_settings_notification_inactive_network);
        notificationInactiveNetworkOnOffText = findViewById(R.id.textview_activity_global_settings_notification_inactive_network_on_off);
        notificationInactiveNetworkSwitch.setOnCheckedChangeListener(null);
        notificationInactiveNetworkSwitch.setChecked(preferenceManager.getPreferenceNotificationInactiveNetwork());
        notificationInactiveNetworkSwitch.setOnCheckedChangeListener(this::onNotificationInactiveNetworkCheckedChanged);
        prepareNotificationInactiveNetworkOnOffText();
    }

    private void prepareNotificationInactiveNetworkOnOffText() {
        notificationInactiveNetworkOnOffText.setText(notificationInactiveNetworkSwitch.isChecked() ? getResources().getString(R.string.string_yes) : getResources().getString(R.string.string_no));
    }

    private void onNotificationInactiveNetworkCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Log.d(GlobalSettingsActivity.class.getName(), "onNotificationInactiveNetworkCheckedChanged, new value is " + isChecked);
        PreferenceManager preferenceManager = new PreferenceManager(this);
        preferenceManager.setPreferenceNotificationInactiveNetwork(isChecked);
        prepareNotificationInactiveNetworkOnOffText();
    }

    private void prepareDownloadExternalStorageSwitch() {
        Log.d(GlobalSettingsActivity.class.getName(), "prepareDownloadExternalStorageSwitch");
        PreferenceManager preferenceManager = new PreferenceManager(this);
        downloadExternalStorageSwitch = findViewById(R.id.switch_activity_global_settings_download_external_storage);
        downloadExternalStorageOnOffText = findViewById(R.id.textview_activity_global_settings_download_external_storage_on_off);
        downloadExternalStorageSwitch.setOnCheckedChangeListener(null);
        downloadExternalStorageSwitch.setChecked(preferenceManager.getPreferenceDownloadExternalStorage());
        downloadExternalStorageSwitch.setOnCheckedChangeListener(this::onDownloadExternalStorageCheckedChanged);
        prepareDownloadExternalStorageOnOffText();
    }

    private void prepareDownloadExternalStorageOnOffText() {
        downloadExternalStorageOnOffText.setText(downloadExternalStorageSwitch.isChecked() ? getResources().getString(R.string.string_yes) : getResources().getString(R.string.string_no));
    }

    private void onDownloadExternalStorageCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Log.d(GlobalSettingsActivity.class.getName(), "onDownloadExternalStorageCheckedChanged, new value is " + isChecked);
        PreferenceManager preferenceManager = new PreferenceManager(this);
        preferenceManager.setPreferenceDownloadExternalStorage(isChecked);
        prepareDownloadExternalStorageOnOffText();
        prepareDownloadFolderField();
        prepareDownloadKeepSwitch();
    }

    private void prepareDownloadFolderField() {
        Log.d(GlobalSettingsActivity.class.getName(), "prepareDownloadFolderField");
        PreferenceManager preferenceManager = new PreferenceManager(this);
        CardView downloadFolderCardView = findViewById(R.id.cardview_activity_global_settings_download_folder);
        downloadFolderText = findViewById(R.id.textview_activity_global_settings_download_folder);
        if (downloadExternalStorageSwitch.isChecked()) {
            String downloadFolder = getExternalDownloadFolder();
            Log.d(GlobalSettingsActivity.class.getName(), "External download folder is " + downloadFolder);
            if (downloadFolder != null) {
                setDownloadFolder(downloadFolder);
                downloadFolderCardView.setEnabled(true);
                downloadFolderCardView.setOnClickListener(this::showDownloadFolderChooseDialog);
            } else {
                Log.e(GlobalSettingsActivity.class.getName(), "Error accessing download folder.");
                Log.d(GlobalSettingsActivity.class.getName(), "Reset to internal folder.");
                setDownloadFolder(getResources().getString(R.string.text_activity_global_settings_download_folder_internal));
                downloadFolderCardView.setEnabled(false);
                downloadFolderCardView.setOnClickListener(null);
                preferenceManager.setPreferenceDownloadExternalStorage(false);
                downloadExternalStorageSwitch.setChecked(false);
                prepareDownloadExternalStorageOnOffText();
                prepareDownloadKeepSwitch();
                Log.d(GlobalSettingsActivity.class.getName(), "Showing error dialog.");
                showErrorDialog(getResources().getString(R.string.text_dialog_general_error_external_root_access));
            }
        } else {
            setDownloadFolder(getResources().getString(R.string.text_activity_global_settings_download_folder_internal));
            downloadFolderCardView.setEnabled(false);
            downloadFolderCardView.setOnClickListener(null);
        }
    }

    private void prepareDownloadKeepSwitch() {
        Log.d(GlobalSettingsActivity.class.getName(), "prepareDownloadKeepSwitch");
        PreferenceManager preferenceManager = new PreferenceManager(this);
        downloadKeepSwitch = findViewById(R.id.switch_activity_global_settings_download_keep);
        downloadKeepOnOffText = findViewById(R.id.textview_activity_global_settings_download_keep_on_off);
        if (downloadExternalStorageSwitch.isChecked()) {
            downloadKeepSwitch.setOnCheckedChangeListener(null);
            downloadKeepSwitch.setChecked(preferenceManager.getPreferenceDownloadKeep());
            downloadKeepSwitch.setOnCheckedChangeListener(this::onDownloadKeepCheckedChanged);
            downloadKeepSwitch.setEnabled(true);
        } else {
            downloadKeepSwitch.setOnCheckedChangeListener(null);
            downloadKeepSwitch.setChecked(false);
            downloadKeepSwitch.setEnabled(false);
        }
        prepareDownloadKeepOnOffText();
    }

    private void prepareDownloadKeepOnOffText() {
        downloadKeepOnOffText.setText(downloadKeepSwitch.isChecked() ? getResources().getString(R.string.string_yes) : getResources().getString(R.string.string_no));
    }

    private void onDownloadKeepCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Log.d(GlobalSettingsActivity.class.getName(), "onDownloadKeepCheckedChanged, new value is " + isChecked);
        PreferenceManager preferenceManager = new PreferenceManager(this);
        preferenceManager.setPreferenceDownloadKeep(isChecked);
        prepareDownloadKeepOnOffText();
    }

    private String getPingCount() {
        return StringUtil.notNull(pingCountText.getText());
    }

    private void setPingCount(String pingCount) {
        pingCountText.setText(StringUtil.notNull(pingCount));
    }

    private String getConnectCount() {
        return StringUtil.notNull(connectCountText.getText());
    }

    private void setConnectCount(String connectCount) {
        connectCountText.setText(StringUtil.notNull(connectCount));
    }

    private String getDownloadFolder() {
        return StringUtil.notNull(downloadFolderText.getText());
    }

    private void setDownloadFolder(String downloadFolder) {
        downloadFolderText.setText(StringUtil.notNull(downloadFolder));
    }

    private void showPingCountInputDialog(View view) {
        Log.d(GlobalSettingsActivity.class.getName(), "showPingCountInputDialog");
        List<String> validators = Collections.singletonList(PingCountFieldValidator.class.getName());
        SettingsInput input = new SettingsInput(SettingsInput.Type.PINGCOUNT, getPingCount(), getResources().getString(R.string.label_activity_global_settings_ping_count), validators);
        showInputDialog(input.toBundle());
    }

    private void showConnectCountInputDialog(View view) {
        Log.d(GlobalSettingsActivity.class.getName(), "showConnectCountInputDialog");
        List<String> validators = Collections.singletonList(ConnectCountFieldValidator.class.getName());
        SettingsInput input = new SettingsInput(SettingsInput.Type.CONNECTCOUNT, getConnectCount(), getResources().getString(R.string.label_activity_global_settings_connect_count), validators);
        showInputDialog(input.toBundle());
    }

    private void showDownloadFolderChooseDialog(View view) {
        Log.d(GlobalSettingsActivity.class.getName(), "showDownloadFolderChooseDialog");
        FileChooseDialog fileChooseDialog = new FileChooseDialog();
        String root = getExternalRootFolder();
        Log.d(GlobalSettingsActivity.class.getName(), "External root folder is " + root);
        if (root == null) {
            Log.e(GlobalSettingsActivity.class.getName(), "Error accessing root folder.");
            Log.d(GlobalSettingsActivity.class.getName(), "Showing error dialog.");
            showErrorDialog(getResources().getString(R.string.text_dialog_general_error_external_root_access));
            return;
        }
        String folder = getPreferenceDownloadFolder();
        Log.d(GlobalSettingsActivity.class.getName(), "Preference download folder is " + folder);
        if (folder == null) {
            Log.e(GlobalSettingsActivity.class.getName(), "Error accessing download folder.");
            Log.d(GlobalSettingsActivity.class.getName(), "Showing error dialog.");
            showErrorDialog(getResources().getString(R.string.text_dialog_general_error_external_root_access));
            return;
        }
        Bundle bundle = BundleUtil.stringsToBundle(new String[]{fileChooseDialog.getFolderRootKey(), fileChooseDialog.getFolderKey(), fileChooseDialog.getFileModeKey(), fileChooseDialog.getTypeKey()}, new String[]{root, folder, FileChooseDialog.Mode.FOLDER.name(), FileChooseDialog.Type.DOWNLOADFOLDER.name()});
        fileChooseDialog.setArguments(bundle);
        fileChooseDialog.show(getSupportFragmentManager(), GlobalSettingsActivity.class.getName());
    }

    private void showInputDialog(Bundle bundle) {
        Log.d(GlobalSettingsActivity.class.getName(), "showInputDialog, opening SettingsInputDialog");
        SettingsInputDialog inputDialog = new SettingsInputDialog();
        inputDialog.setArguments(bundle);
        inputDialog.show(getSupportFragmentManager(), GlobalSettingsActivity.class.getName());
    }

    private String getExternalRootFolder() {
        Log.d(GlobalSettingsActivity.class.getName(), "getExternalRootFolder");
        PreferenceManager preferenceManager = new PreferenceManager(this);
        IFileManager fileManager = getFileManager();
        File root = FileUtil.getExternalRootDirectory(fileManager, preferenceManager);
        Log.d(GlobalSettingsActivity.class.getName(), "External root folder is " + root);
        if (root == null) {
            return null;
        }
        return root.getAbsolutePath();
    }

    private String getPreferenceDownloadFolder() {
        Log.d(GlobalSettingsActivity.class.getName(), "getPreferenceDownloadFolder");
        String downloadFolder = getExternalDownloadFolder();
        if (downloadFolder == null) {
            return null;
        }
        PreferenceManager preferenceManager = new PreferenceManager(this);
        return preferenceManager.getPreferenceDownloadFolder();
    }

    private String getExternalDownloadFolder() {
        Log.d(GlobalSettingsActivity.class.getName(), "getExternalDownloadFolder");
        PreferenceManager preferenceManager = new PreferenceManager(this);
        String folder = preferenceManager.getPreferenceDownloadFolder();
        IFileManager fileManager = getFileManager();
        File downloadFolder = FileUtil.getExternalDirectory(fileManager, preferenceManager, folder);
        Log.d(GlobalSettingsActivity.class.getName(), "External download folder is " + downloadFolder);
        if (downloadFolder == null) {
            return null;
        }
        return downloadFolder.getAbsolutePath();
    }

    @Override
    public void onInputDialogOkClicked(SettingsInputDialog inputDialog, SettingsInput.Type type) {
        Log.d(GlobalSettingsActivity.class.getName(), "onInputDialogOkClicked, type is " + type + ", value is " + inputDialog.getValue());
        PreferenceManager preferenceManager = new PreferenceManager(this);
        if (SettingsInput.Type.PINGCOUNT.equals(type)) {
            setPingCount(inputDialog.getValue());
            preferenceManager.setPreferencePingCount(NumberUtil.getIntValue(getPingCount(), getResources().getInteger(R.integer.ping_count_default)));
        } else if (SettingsInput.Type.CONNECTCOUNT.equals(type)) {
            setConnectCount(inputDialog.getValue());
            preferenceManager.setPreferenceConnectCount(NumberUtil.getIntValue(getConnectCount(), getResources().getInteger(R.integer.connect_count_default)));
        } else {
            Log.e(GlobalSettingsActivity.class.getName(), "type " + type + " unknown");
        }
        inputDialog.dismiss();
    }

    @Override
    public void onFileChooseDialogOkClicked(FileChooseDialog folderChooseDialog, FileChooseDialog.Type type) {
        Log.d(GlobalSettingsActivity.class.getName(), "onFileChooseDialogOkClicked, type is " + type);
        IFileManager fileManager = getFileManager();
        PreferenceManager preferenceManager = new PreferenceManager(this);
        if (FileChooseDialog.Type.DOWNLOADFOLDER.equals(type)) {
            String folder = folderChooseDialog.getFolder();
            File downloadFolder = FileUtil.getExternalDirectory(fileManager, preferenceManager, folder);
            Log.d(GlobalSettingsActivity.class.getName(), "External download folder is " + downloadFolder);
            if (downloadFolder == null) {
                Log.e(GlobalSettingsActivity.class.getName(), "Error accessing download folder.");
                folderChooseDialog.dismiss();
                Log.d(GlobalSettingsActivity.class.getName(), "Showing error dialog.");
                showErrorDialog(getResources().getString(R.string.text_dialog_general_error_external_download_create));
                return;
            }
            preferenceManager.setPreferenceDownloadFolder(folder);
            setDownloadFolder(downloadFolder.getAbsolutePath());
        } else {
            Log.e(GlobalSettingsActivity.class.getName(), "Unknown type " + type);
        }
        folderChooseDialog.dismiss();
    }
}
