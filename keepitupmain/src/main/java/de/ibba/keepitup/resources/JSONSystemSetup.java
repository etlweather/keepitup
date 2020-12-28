package de.ibba.keepitup.resources;

import android.content.Context;
import android.content.res.Resources;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import de.ibba.keepitup.R;
import de.ibba.keepitup.db.DBSetup;
import de.ibba.keepitup.logging.Log;
import de.ibba.keepitup.util.JSONUtil;
import de.ibba.keepitup.util.NumberUtil;

public class JSONSystemSetup implements ISystemSetup {

    private final Context context;
    private final DBSetup dbSetup;
    private final PreferenceSetup preferenceSetup;

    public JSONSystemSetup(Context context) {
        this.context = context;
        this.dbSetup = new DBSetup(context);
        this.preferenceSetup = new PreferenceSetup(context);
    }

    public SystemSetupResult exportData() {
        Log.d(JSONSystemSetup.class.getName(), "exportData");
        JSONObject root = new JSONObject();
        String dbKey = getResources().getString(R.string.database_json_key);
        String settingsKey = getResources().getString(R.string.preferences_json_key);
        try {
            JSONObject dbData = exportDatabase();
            root.put(dbKey, dbData);
            Log.d(JSONSystemSetup.class.getName(), "Successfully exported database: " + dbData);
        } catch (Exception exc) {
            Log.e(JSONSystemSetup.class.getName(), "Error exporting database", exc);
            return new SystemSetupResult(false, exc.getMessage(), root.toString());
        }
        try {
            JSONObject settings = exportSettings();
            root.put(settingsKey, settings);
            Log.d(JSONSystemSetup.class.getName(), "Successfully exported settings: " + settings);
        } catch (Exception exc) {
            Log.e(JSONSystemSetup.class.getName(), "Error exporting settings", exc);
            return new SystemSetupResult(false, exc.getMessage(), root.toString());
        }
        return new SystemSetupResult(true, "Successful export", root.toString());
    }

    public SystemSetupResult importData(String data) {
        Log.d(JSONSystemSetup.class.getName(), "importData for data " + data);
        JSONObject root;
        try {
            root = new JSONObject(data);
        } catch (Exception exc) {
            Log.e(JSONSystemSetup.class.getName(), "Error importing data", exc);
            return new SystemSetupResult(false, exc.getMessage(), data);
        }
        String dbKey = getResources().getString(R.string.database_json_key);
        String settingsKey = getResources().getString(R.string.preferences_json_key);
        try {
            importDatabase((JSONObject) root.get(dbKey));
            Log.d(JSONSystemSetup.class.getName(), "Successfully imported database.");
        } catch (Exception exc) {
            Log.e(JSONSystemSetup.class.getName(), "Error importing database", exc);
            return new SystemSetupResult(false, exc.getMessage(), data);
        }
        try {
            importSettings((JSONObject) root.get(settingsKey));
            Log.d(JSONSystemSetup.class.getName(), "Successfully imported settings.");
        } catch (Exception exc) {
            Log.e(JSONSystemSetup.class.getName(), "Error importing settings", exc);
            return new SystemSetupResult(false, exc.getMessage(), data);
        }
        return new SystemSetupResult(true, "Successful import", data);
    }

    private void importDatabase(JSONObject dbData) throws JSONException {
        Log.d(JSONSystemSetup.class.getName(), "importDatabase for data " + dbData);
        String taskKey = getResources().getString(R.string.networktask_json_key);
        String logKey = getResources().getString(R.string.logentry_json_key);
        Iterator<String> ids = dbData.keys();
        while (ids.hasNext()) {
            JSONObject taskData = (JSONObject) dbData.get(ids.next());
            Map<String, ?> taskMap = JSONUtil.toMap((JSONObject) taskData.get(taskKey));
            List<?> logList = JSONUtil.toList((JSONArray) taskData.get(logKey));
            dbSetup.importNetworkTaskWithLogs(getContext(), taskMap, filterLogList(logList));
        }
    }

    private void importSettings(JSONObject settings) throws JSONException {
        Log.d(JSONSystemSetup.class.getName(), "importSettings for settings " + settings);
        String globalSettingsKey = getResources().getString(R.string.preferences_global_json_key);
        String defaultsKey = getResources().getString(R.string.preferences_defaults_json_key);
        String systemSettingsKey = getResources().getString(R.string.preferences_system_json_key);
        JSONObject globalSettings = (JSONObject) settings.get(globalSettingsKey);
        JSONObject defaults = (JSONObject) settings.get(defaultsKey);
        JSONObject systemSettings = (JSONObject) settings.get(systemSettingsKey);
        preferenceSetup.importGlobalSettings(JSONUtil.toMap(globalSettings));
        preferenceSetup.importDefaults(JSONUtil.toMap(defaults));
        preferenceSetup.importSystemSettings(JSONUtil.toMap(systemSettings));
    }

    private List<Map<String, ?>> filterLogList(List<?> logList) {
        Log.d(JSONSystemSetup.class.getName(), "filterLogList");
        List<Map<String, ?>> filteredList = new ArrayList<>();
        for (Object log : logList) {
            if (log instanceof Map) {
                filteredList.add((Map<String, ?>) log);
            }
        }
        return filteredList;
    }

    private JSONObject exportDatabase() throws JSONException {
        Log.d(JSONSystemSetup.class.getName(), "exportDatabase");
        JSONObject dbData = new JSONObject();
        List<Map<String, ?>> networkTasks = dbSetup.exportNetworkTasks(getContext());
        for (Map<String, ?> taskMap : networkTasks) {
            long id = getId(taskMap);
            if (id >= 0) {
                List<Map<String, ?>> logs = dbSetup.exportLogsForNetworkTask(getContext(), id);
                JSONObject task = getJSONObjectForNetworkTask(taskMap, logs);
                dbData.put(String.valueOf(id), task);
            }
        }
        return dbData;
    }

    private JSONObject exportSettings() throws JSONException {
        Log.d(JSONSystemSetup.class.getName(), "exportSettings");
        JSONObject settings = new JSONObject();
        String globalSettingsKey = getResources().getString(R.string.preferences_global_json_key);
        String defaultsKey = getResources().getString(R.string.preferences_defaults_json_key);
        String systemSettingsKey = getResources().getString(R.string.preferences_system_json_key);
        Map<String, ?> globalSettings = preferenceSetup.exportGlobalSettings();
        Map<String, ?> defaults = preferenceSetup.exportDefaults();
        Map<String, ?> systemSettings = preferenceSetup.exportSystemSettings();
        settings.put(globalSettingsKey, new JSONObject(globalSettings));
        settings.put(defaultsKey, new JSONObject(defaults));
        settings.put(systemSettingsKey, new JSONObject(systemSettings));
        return settings;
    }

    private long getId(Map<String, ?> map) {
        if (map.containsKey("id")) {
            return NumberUtil.getLongValue(map.get("id"), -1);
        }
        return -1;
    }

    private JSONObject getJSONObjectForNetworkTask(Map<String, ?> taskMap, List<Map<String, ?>> logs) throws JSONException {
        Log.d(JSONSystemSetup.class.getName(), "getJSONObjectForNetworkTask");
        JSONObject task = new JSONObject();
        String taskKey = getResources().getString(R.string.networktask_json_key);
        String logKey = getResources().getString(R.string.logentry_json_key);
        task.put(taskKey, new JSONObject(taskMap));
        task.put(logKey, new JSONArray(logs));
        return task;
    }

    private Context getContext() {
        return context;
    }

    private Resources getResources() {
        return getContext().getResources();
    }
}
