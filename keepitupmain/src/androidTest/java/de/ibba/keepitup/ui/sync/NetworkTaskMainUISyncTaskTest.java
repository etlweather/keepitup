package de.ibba.keepitup.ui.sync;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.MediumTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;
import java.util.GregorianCalendar;

import de.ibba.keepitup.db.LogDAO;
import de.ibba.keepitup.db.NetworkTaskDAO;
import de.ibba.keepitup.model.AccessType;
import de.ibba.keepitup.model.LogEntry;
import de.ibba.keepitup.model.NetworkTask;
import de.ibba.keepitup.test.mock.TestRegistry;
import de.ibba.keepitup.ui.BaseUITest;
import de.ibba.keepitup.ui.NetworkTaskMainActivity;
import de.ibba.keepitup.ui.adapter.NetworkTaskAdapter;
import de.ibba.keepitup.ui.adapter.NetworkTaskUIWrapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@MediumTest
@RunWith(AndroidJUnit4.class)
public class NetworkTaskMainUISyncTaskTest extends BaseUITest {

    @Rule
    public final ActivityTestRule<NetworkTaskMainActivity> rule = new ActivityTestRule<>(NetworkTaskMainActivity.class, false, false);

    private NetworkTaskMainActivity activity;
    private NetworkTaskMainUISyncTask syncTask;
    private NetworkTaskDAO networkTaskDAO;
    private LogDAO logDAO;

    @Before
    public void beforeEachTestMethod() {
        super.beforeEachTestMethod();
        activity = (NetworkTaskMainActivity) launchRecyclerViewBaseActivity(rule);
        syncTask = new NetworkTaskMainUISyncTask(activity, (NetworkTaskAdapter) activity.getAdapter());
        networkTaskDAO = new NetworkTaskDAO(TestRegistry.getContext());
        networkTaskDAO.deleteAllNetworkTasks();
        logDAO = new LogDAO(TestRegistry.getContext());
        logDAO.deleteAllLogs();
    }

    @After
    public void afterEachTestMethod() {
        logDAO.deleteAllLogs();
        networkTaskDAO.deleteAllNetworkTasks();
    }

    @Test
    public void testMostRecentLogEntryReturned() {
        NetworkTask task = networkTaskDAO.insertNetworkTask(getNetworkTask1());
        logDAO.insertAndDeleteLog(getLogEntryWithNetworkTaskId(task.getId(), new GregorianCalendar(1980, Calendar.MARCH, 17).getTime().getTime()));
        LogEntry logEntry2 = logDAO.insertAndDeleteLog(getLogEntryWithNetworkTaskId(task.getId(), new GregorianCalendar(1980, Calendar.MARCH, 18).getTime().getTime()));
        NetworkTaskUIWrapper wrapper = syncTask.doInBackground(task);
        assertTrue(task.isEqual(wrapper.getNetworkTask()));
        assertTrue(logEntry2.isEqual(wrapper.getLogEntry()));
    }

    @Test
    public void testAdapterLogUpdate() {
        NetworkTask task1 = networkTaskDAO.insertNetworkTask(getNetworkTask1());
        NetworkTask task2 = networkTaskDAO.insertNetworkTask(getNetworkTask2());
        NetworkTask task3 = networkTaskDAO.insertNetworkTask(getNetworkTask3());
        LogEntry logEntry = logDAO.insertAndDeleteLog(getLogEntryWithNetworkTaskId(task2.getId(), new GregorianCalendar(1980, Calendar.MARCH, 17).getTime().getTime()));
        NetworkTaskUIWrapper wrapper1 = new NetworkTaskUIWrapper(task1, null);
        NetworkTaskUIWrapper wrapper2 = new NetworkTaskUIWrapper(task2, null);
        NetworkTaskUIWrapper wrapper3 = new NetworkTaskUIWrapper(task3, null);
        NetworkTaskAdapter adapter = (NetworkTaskAdapter) activity.getAdapter();
        adapter.addItem(wrapper1);
        adapter.addItem(wrapper2);
        adapter.addItem(wrapper3);
        activity.runOnUiThread(() -> syncTask.onPostExecute(new NetworkTaskUIWrapper(task2, logEntry)));
        InstrumentationRegistry.getInstrumentation().waitForIdleSync();
        wrapper2 = adapter.getItem(1);
        assertTrue(task2.isEqual(wrapper2.getNetworkTask()));
        assertTrue(logEntry.isEqual(wrapper2.getLogEntry()));
    }

    @Test
    public void testAdapterTaskAndLogUpdate() {
        NetworkTask task1 = networkTaskDAO.insertNetworkTask(getNetworkTask1());
        NetworkTask task2 = networkTaskDAO.insertNetworkTask(getNetworkTask2());
        NetworkTask task3 = networkTaskDAO.insertNetworkTask(getNetworkTask3());
        LogEntry logEntry = logDAO.insertAndDeleteLog(getLogEntryWithNetworkTaskId(task2.getId(), new GregorianCalendar(1980, Calendar.MARCH, 17).getTime().getTime()));
        NetworkTaskUIWrapper wrapper1 = new NetworkTaskUIWrapper(task1, null);
        NetworkTaskUIWrapper wrapper2 = new NetworkTaskUIWrapper(task2, logEntry);
        NetworkTaskUIWrapper wrapper3 = new NetworkTaskUIWrapper(task3, null);
        NetworkTaskAdapter adapter = (NetworkTaskAdapter) activity.getAdapter();
        adapter.addItem(wrapper1);
        adapter.addItem(wrapper2);
        adapter.addItem(wrapper3);
        networkTaskDAO.increaseNetworkTaskInstances(task2.getId());
        NetworkTask updatedTask2 = networkTaskDAO.readNetworkTask(task2.getId());
        LogEntry otherLogEntry = logDAO.insertAndDeleteLog(getLogEntryWithNetworkTaskId(task2.getId(), new GregorianCalendar(1980, Calendar.MARCH, 18).getTime().getTime()));
        activity.runOnUiThread(() -> syncTask.onPostExecute(new NetworkTaskUIWrapper(updatedTask2, otherLogEntry)));
        InstrumentationRegistry.getInstrumentation().waitForIdleSync();
        wrapper2 = adapter.getItem(1);
        assertTrue(updatedTask2.isEqual(wrapper2.getNetworkTask()));
        assertTrue(otherLogEntry.isEqual(wrapper2.getLogEntry()));
    }

    @Test
    public void testAdapterTaskUpdate() {
        NetworkTask task = networkTaskDAO.insertNetworkTask(getNetworkTask1());
        NetworkTaskAdapter adapter = (NetworkTaskAdapter) activity.getAdapter();
        NetworkTaskUIWrapper wrapper = new NetworkTaskUIWrapper(task, null);
        adapter.addItem(wrapper);
        networkTaskDAO.increaseNetworkTaskInstances(task.getId());
        NetworkTask updatedTask = networkTaskDAO.readNetworkTask(task.getId());
        final NetworkTaskUIWrapper newWrapper = syncTask.doInBackground(updatedTask);
        assertNotNull(newWrapper);
        activity.runOnUiThread(() -> syncTask.onPostExecute(newWrapper));
        InstrumentationRegistry.getInstrumentation().waitForIdleSync();
        adapter = (NetworkTaskAdapter) activity.getAdapter();
        assertEquals(2, adapter.getItemCount());
        wrapper = adapter.getItem(0);
        assertTrue(updatedTask.isEqual(wrapper.getNetworkTask()));
        assertNull(wrapper.getLogEntry());
    }

    @Test
    public void testNullAdapterUpdate() {
        NetworkTask task1 = networkTaskDAO.insertNetworkTask(getNetworkTask1());
        NetworkTask task2 = networkTaskDAO.insertNetworkTask(getNetworkTask2());
        NetworkTask task3 = networkTaskDAO.insertNetworkTask(getNetworkTask3());
        LogEntry logEntry = logDAO.insertAndDeleteLog(getLogEntryWithNetworkTaskId(task2.getId(), new GregorianCalendar(1980, Calendar.MARCH, 17).getTime().getTime()));
        NetworkTaskUIWrapper wrapper1 = new NetworkTaskUIWrapper(task1, null);
        NetworkTaskUIWrapper wrapper2 = new NetworkTaskUIWrapper(task2, null);
        NetworkTaskUIWrapper wrapper3 = new NetworkTaskUIWrapper(task3, null);
        NetworkTaskAdapter adapter = (NetworkTaskAdapter) activity.getAdapter();
        adapter.addItem(wrapper1);
        adapter.addItem(wrapper2);
        adapter.addItem(wrapper3);
        NetworkTaskMainUISyncTask nullSyncTask = new NetworkTaskMainUISyncTask(activity, null);
        activity.runOnUiThread(() -> nullSyncTask.onPostExecute(new NetworkTaskUIWrapper(task2, logEntry)));
        InstrumentationRegistry.getInstrumentation().waitForIdleSync();
        wrapper2 = adapter.getItem(1);
        assertTrue(task2.isEqual(wrapper2.getNetworkTask()));
        assertNull(wrapper2.getLogEntry());
    }

    private NetworkTask getNetworkTask1() {
        NetworkTask task = new NetworkTask();
        task.setId(0);
        task.setIndex(1);
        task.setSchedulerId(0);
        task.setInstances(0);
        task.setAddress("127.0.0.1");
        task.setPort(80);
        task.setAccessType(AccessType.PING);
        task.setInterval(15);
        task.setOnlyWifi(false);
        task.setNotification(true);
        task.setRunning(true);
        return task;
    }

    private NetworkTask getNetworkTask2() {
        NetworkTask task = new NetworkTask();
        task.setId(1);
        task.setIndex(2);
        task.setSchedulerId(5);
        task.setInstances(0);
        task.setAddress("192.168.178.1");
        task.setPort(25);
        task.setAccessType(AccessType.DOWNLOAD);
        task.setInterval(10);
        task.setOnlyWifi(false);
        task.setNotification(true);
        task.setRunning(false);
        return task;
    }

    private NetworkTask getNetworkTask3() {
        NetworkTask task = new NetworkTask();
        task.setId(2);
        task.setIndex(3);
        task.setSchedulerId(789);
        task.setInstances(0);
        task.setAddress("www.host.com");
        task.setPort(456);
        task.setAccessType(AccessType.CONNECT);
        task.setInterval(20);
        task.setOnlyWifi(true);
        task.setNotification(true);
        task.setRunning(true);
        return task;
    }

    private LogEntry getLogEntryWithNetworkTaskId(long networkTaskId, long timestamp) {
        LogEntry logEntry = new LogEntry();
        logEntry.setId(0);
        logEntry.setNetworkTaskId(networkTaskId);
        logEntry.setSuccess(true);
        logEntry.setTimestamp(timestamp);
        logEntry.setMessage("TestMessage");
        return logEntry;
    }
}
