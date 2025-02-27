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

package net.ibbaa.keepitup.service;

import android.content.Intent;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.MediumTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import net.ibbaa.keepitup.db.LogDAO;
import net.ibbaa.keepitup.db.NetworkTaskDAO;
import net.ibbaa.keepitup.model.AccessType;
import net.ibbaa.keepitup.model.LogEntry;
import net.ibbaa.keepitup.model.NetworkTask;
import net.ibbaa.keepitup.test.mock.TestRegistry;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@MediumTest
@RunWith(AndroidJUnit4.class)
public class NetworkTaskProcessBroadcastReceiverTest {

    private NetworkTaskDAO networkTaskDAO;
    private LogDAO logDAO;
    private NetworkTaskProcessBroadcastReceiver broadcastReceiver;

    @Before
    public void beforeEachTestMethod() {
        networkTaskDAO = new NetworkTaskDAO(TestRegistry.getContext());
        networkTaskDAO.deleteAllNetworkTasks();
        logDAO = new LogDAO(TestRegistry.getContext());
        logDAO.deleteAllLogs();
        broadcastReceiver = new NetworkTaskProcessBroadcastReceiver();
    }

    @After
    public void afterEachTestMethod() {
        networkTaskDAO.deleteAllNetworkTasks();
        logDAO.deleteAllLogs();
    }

    @Test
    public void testLogWritten() {
        NetworkTask task = getNetworkTask();
        task = networkTaskDAO.insertNetworkTask(task);
        networkTaskDAO.updateNetworkTaskRunning(task.getId(), true);
        Intent intent = new Intent();
        intent.putExtras(task.toBundle());
        broadcastReceiver.onReceive(TestRegistry.getContext(), intent);
        List<LogEntry> entries = logDAO.readAllLogsForNetworkTask(task.getId());
        assertEquals(1, entries.size());
        LogEntry entry = entries.get(0);
        assertEquals(task.getId(), entry.getNetworkTaskId());
        assertEquals(getTestTimestamp(), entry.getTimestamp());
        assertTrue(entry.isSuccess());
        assertEquals("successful", entry.getMessage());
    }

    @Test
    public void testExecutionSkippedNetworkTaskDoesNotExist() {
        NetworkTask task = getNetworkTask();
        Intent intent = new Intent();
        intent.putExtras(task.toBundle());
        broadcastReceiver.onReceive(TestRegistry.getContext(), intent);
        List<LogEntry> entries = logDAO.readAllLogsForNetworkTask(task.getId());
        assertEquals(0, entries.size());
    }

    @Test
    public void testExecutionSkippedMarkedAsNotRunning() {
        NetworkTask task = getNetworkTask();
        task = networkTaskDAO.insertNetworkTask(task);
        networkTaskDAO.updateNetworkTaskRunning(task.getId(), false);
        Intent intent = new Intent();
        intent.putExtras(task.toBundle());
        broadcastReceiver.onReceive(TestRegistry.getContext(), intent);
        List<LogEntry> entries = logDAO.readAllLogsForNetworkTask(task.getId());
        assertEquals(0, entries.size());
    }

    @Test
    public void testExecutionSkippedNotValid() {
        NetworkTask task = getNetworkTask();
        task = networkTaskDAO.insertNetworkTask(task);
        networkTaskDAO.updateNetworkTaskRunning(task.getId(), true);
        task.setSchedulerId(task.getSchedulerId() + 1);
        Intent intent = new Intent();
        intent.putExtras(task.toBundle());
        broadcastReceiver.onReceive(TestRegistry.getContext(), intent);
        List<LogEntry> entries = logDAO.readAllLogsForNetworkTask(task.getId());
        assertEquals(0, entries.size());
    }

    private long getTestTimestamp() {
        Calendar calendar = new GregorianCalendar(1970, Calendar.JANUARY, 1, 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 1);
        return calendar.getTimeInMillis();
    }

    private NetworkTask getNetworkTask() {
        NetworkTask networkTask = new NetworkTask();
        networkTask.setId(1);
        networkTask.setIndex(1);
        networkTask.setSchedulerId(1);
        networkTask.setInstances(0);
        networkTask.setAddress("127.0.0.1");
        networkTask.setPort(80);
        networkTask.setAccessType(AccessType.PING);
        networkTask.setInterval(15);
        networkTask.setOnlyWifi(false);
        networkTask.setNotification(true);
        networkTask.setRunning(false);
        networkTask.setLastScheduled(1);
        return networkTask;
    }
}
