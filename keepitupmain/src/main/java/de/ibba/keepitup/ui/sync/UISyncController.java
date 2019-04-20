package de.ibba.keepitup.ui.sync;

import android.content.Context;
import android.util.Log;

import de.ibba.keepitup.R;
import de.ibba.keepitup.db.LogDAO;
import de.ibba.keepitup.resources.ServiceFactory;
import de.ibba.keepitup.resources.ServiceFactoryContributor;
import de.ibba.keepitup.ui.adapter.NetworkTaskAdapter;

public class UISyncController {

    private static IHandler handler;
    private static ServiceFactory serviceFactory;
    private static Runnable runnable;
    private static NetworkTaskAdapter adapter;

    public static void start(NetworkTaskAdapter adapter) {
        Log.d(UISyncController.class.getName(), "starting UI sync");
        UISyncController.adapter = adapter;
        if (serviceFactory == null) {
            Log.d(UISyncController.class.getName(), "Creating service factory");
            serviceFactory = getServiceFactory(UISyncController.adapter.getContext());
            Log.d(UISyncController.class.getName(), "Created service factory class is " + serviceFactory.getClass().getName());
        }
        if (handler == null) {
            Log.d(UISyncController.class.getName(), "Creating handler");
            handler = serviceFactory.createHandler();
            Log.d(UISyncController.class.getName(), "Created handler class is " + handler.getClass().getName());
        }
        if (isRunning()) {
            stop();
        }
        UISyncController.runnable = new UISyncRunnable();
        Log.d(UISyncController.class.getName(), "Starting...");
        handler.start(runnable);
    }

    private static void refresh(Runnable runnable, long refreshInterval) {
        Log.d(UISyncController.class.getName(), "refreshing ui sync with interval of " + refreshInterval);
        UISyncController.runnable = runnable;
        handler.startDelayed(runnable, refreshInterval * 1000);
    }

    public static void stop() {
        Log.d(UISyncController.class.getName(), "stopping UI sync");
        if (isRunning()) {
            Log.d(UISyncController.class.getName(), "Stopping...");
            handler.stop(runnable);
            runnable = null;
            adapter = null;
        } else {
            Log.d(UISyncController.class.getName(), "Not running.");
        }
    }

    public static boolean isRunning() {
        Log.d(UISyncController.class.getName(), "ui sync running: " + (UISyncController.runnable != null));
        return runnable != null;
    }

    private static ServiceFactory getServiceFactory(Context context) {
        ServiceFactoryContributor factoryContributor = new ServiceFactoryContributor(context);
        return factoryContributor.createServiceFactory();
    }

    public static class UISyncRunnable implements Runnable {

        private UISyncAsyncTask uiSyncAsyncTask;

        @Override
        public void run() {
            LogDAO logDAO = new LogDAO(UISyncController.adapter.getContext());
            uiSyncAsyncTask = serviceFactory.createUISyncAsyncTask();
            uiSyncAsyncTask.start(new UISyncHolder(adapter.getAllItems(), adapter, logDAO));
            long refreshInterval = UISyncController.adapter.getResources().getInteger(R.integer.ui_sync_refresh_interval);
            Log.d(UISyncController.UISyncRunnable.class.getName(), "loaded refreshInterval setting " + refreshInterval);
            UISyncController.refresh(this, refreshInterval);
        }

        public UISyncAsyncTask getUiSyncAsyncTask() {
            return uiSyncAsyncTask;
        }
    }
}
