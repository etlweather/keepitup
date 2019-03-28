package de.ibba.keepitup.db;

import android.support.test.InstrumentationRegistry;
import android.support.test.filters.MediumTest;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import de.ibba.keepitup.model.AccessType;
import de.ibba.keepitup.model.NetworkTask;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@MediumTest
@RunWith(AndroidJUnit4.class)
public class NetworkTaskDAOTest {

    private NetworkTaskDAO networkTaskDAO;

    @Before
    public void beforeEachTestMethod() {
        networkTaskDAO = new NetworkTaskDAO(InstrumentationRegistry.getTargetContext());
        networkTaskDAO.deleteAllNetworkTasks();
    }

    @After
    public void afterEachTestMethod() {
        networkTaskDAO.deleteAllNetworkTasks();
    }

    @Test
    public void testInsertReadDelete() {
        NetworkTask insertedTask1 = getNetworkTask1();
        networkTaskDAO.insertNetworkTask(insertedTask1);
        List<NetworkTask> readTasks = networkTaskDAO.readAllNetworkTasks();
        assertEquals(1, readTasks.size());
        NetworkTask readTask = readTasks.get(0);
        assertTrue(readTask.getId() > 0);
        assertAreEqual(insertedTask1, readTask);
        readTask = networkTaskDAO.readNetworkTask(readTask.getId());
        assertAreEqual(insertedTask1, readTask);
        NetworkTask insertedTask2 = getNetworkTask2();
        NetworkTask insertedTask3 = getNetworkTask3();
        networkTaskDAO.insertNetworkTask(insertedTask2);
        networkTaskDAO.insertNetworkTask(insertedTask3);
        readTasks = networkTaskDAO.readAllNetworkTasks();
        assertEquals(3, readTasks.size());
        NetworkTask readTask1 = readTasks.get(0);
        NetworkTask readTask3 = readTasks.get(1);
        NetworkTask readTask2 = readTasks.get(2);
        assertTrue(readTask1.getId() > 0);
        assertTrue(readTask2.getId() > 0);
        assertTrue(readTask3.getId() > 0);
        assertAreEqual(insertedTask1, readTask1);
        assertAreEqual(insertedTask2, readTask2);
        assertAreEqual(insertedTask3, readTask3);
        assertEquals(1, readTask1.getIndex());
        assertEquals(10, readTask2.getIndex());
        assertEquals(5, readTask3.getIndex());
        readTask1 = networkTaskDAO.readNetworkTask(readTask1.getId());
        readTask2 = networkTaskDAO.readNetworkTask(readTask2.getId());
        readTask3 = networkTaskDAO.readNetworkTask(readTask3.getId());
        assertAreEqual(insertedTask1, readTask1);
        assertAreEqual(insertedTask2, readTask2);
        assertAreEqual(insertedTask3, readTask3);
        networkTaskDAO.deleteNetworkTask(readTask2);
        readTask2 = networkTaskDAO.readNetworkTask(readTask2.getId());
        assertNull(readTask2);
    }

    @Test
    public void testDeleteIndexCleanup() {
        NetworkTask insertedTask1 = new NetworkTask();
        NetworkTask insertedTask2 = new NetworkTask();
        NetworkTask insertedTask3 = new NetworkTask();
        NetworkTask insertedTask4 = new NetworkTask();
        NetworkTask insertedTask5 = new NetworkTask();
        NetworkTask insertedTask6 = new NetworkTask();
        insertedTask1.setIndex(1);
        insertedTask2.setIndex(2);
        insertedTask3.setIndex(3);
        insertedTask4.setIndex(4);
        insertedTask5.setIndex(5);
        insertedTask6.setIndex(6);
        networkTaskDAO.insertNetworkTask(insertedTask1);
        insertedTask2 = networkTaskDAO.insertNetworkTask(insertedTask2);
        networkTaskDAO.insertNetworkTask(insertedTask3);
        networkTaskDAO.insertNetworkTask(insertedTask4);
        networkTaskDAO.insertNetworkTask(insertedTask5);
        networkTaskDAO.insertNetworkTask(insertedTask6);
        networkTaskDAO.deleteNetworkTask(insertedTask2);
        List<NetworkTask> readTasks = networkTaskDAO.readAllNetworkTasks();
        assertEquals(5, readTasks.size());
        NetworkTask readTask1 = readTasks.get(0);
        NetworkTask readTask2 = readTasks.get(1);
        NetworkTask readTask3 = readTasks.get(2);
        NetworkTask readTask4 = readTasks.get(3);
        NetworkTask readTask5 = readTasks.get(4);
        assertEquals(1, readTask1.getIndex());
        assertEquals(2, readTask2.getIndex());
        assertEquals(3, readTask3.getIndex());
        assertEquals(4, readTask4.getIndex());
        assertEquals(5, readTask5.getIndex());
    }

    @Test
    public void testUpdateSchedulerId() {
        NetworkTask insertedTask1 = getNetworkTask1();
        networkTaskDAO.insertNetworkTask(insertedTask1);
        List<NetworkTask> readTasks = networkTaskDAO.readAllNetworkTasks();
        NetworkTask readTask1 = readTasks.get(0);
        networkTaskDAO.updateNetworkTaskSchedulerId(readTask1.getId(), 25);
        readTask1 = networkTaskDAO.readNetworkTask(readTask1.getId());
        assertEquals(insertedTask1.getIndex(), readTask1.getIndex());
        assertEquals(insertedTask1.getAccessType(), readTask1.getAccessType());
        assertEquals(insertedTask1.getAddress(), readTask1.getAddress());
        assertEquals(insertedTask1.getPort(), readTask1.getPort());
        assertEquals(insertedTask1.getInterval(), readTask1.getInterval());
        assertEquals(insertedTask1.isOnlyWifi(), readTask1.isOnlyWifi());
        assertEquals(insertedTask1.isNotification(), readTask1.isNotification());
        assertEquals(25, readTask1.getSchedulerid());
    }

    @Test
    public void testUpdate() {
        NetworkTask insertedTask1 = getNetworkTask1();
        networkTaskDAO.insertNetworkTask(insertedTask1);
        List<NetworkTask> readTasks = networkTaskDAO.readAllNetworkTasks();
        NetworkTask readTask1 = readTasks.get(0);
        NetworkTask task2 = getNetworkTask2();
        task2.setId(readTask1.getId());
        networkTaskDAO.updateNetworkTask(task2);
        assertAreEqual(insertedTask1, readTask1);
        readTask1 = networkTaskDAO.readNetworkTask(readTask1.getId());
        assertEquals(task2.getAccessType(), readTask1.getAccessType());
        assertEquals(task2.getAddress(), readTask1.getAddress());
        assertEquals(task2.getPort(), readTask1.getPort());
        assertEquals(task2.getInterval(), readTask1.getInterval());
        assertEquals(task2.isOnlyWifi(), readTask1.isOnlyWifi());
        assertEquals(task2.isNotification(), readTask1.isNotification());
        assertEquals(insertedTask1.getIndex(), readTask1.getIndex());
        assertEquals(insertedTask1.getSchedulerid(), readTask1.getSchedulerid());
    }

    private NetworkTask getNetworkTask1() {
        NetworkTask insertedTask1 = new NetworkTask();
        insertedTask1.setId(0);
        insertedTask1.setIndex(1);
        insertedTask1.setSchedulerid(11);
        insertedTask1.setAddress("127.0.0.1");
        insertedTask1.setPort(80);
        insertedTask1.setAccessType(AccessType.PING);
        insertedTask1.setInterval(15);
        insertedTask1.setOnlyWifi(false);
        insertedTask1.setNotification(true);
        return insertedTask1;
    }

    private NetworkTask getNetworkTask2() {
        NetworkTask insertedTask2 = new NetworkTask();
        insertedTask2.setId(0);
        insertedTask2.setIndex(10);
        insertedTask2.setSchedulerid(22);
        insertedTask2.setAddress("host.com");
        insertedTask2.setPort(21);
        insertedTask2.setAccessType(null);
        insertedTask2.setInterval(1);
        insertedTask2.setOnlyWifi(true);
        insertedTask2.setNotification(false);
        return insertedTask2;
    }

    private NetworkTask getNetworkTask3() {
        NetworkTask insertedTask3 = new NetworkTask();
        insertedTask3.setId(0);
        insertedTask3.setIndex(5);
        insertedTask3.setSchedulerid(33);
        insertedTask3.setAddress(null);
        insertedTask3.setPort(456);
        insertedTask3.setAccessType(AccessType.PING);
        insertedTask3.setInterval(200);
        insertedTask3.setOnlyWifi(false);
        insertedTask3.setNotification(false);
        return insertedTask3;
    }

    private void assertAreEqual(NetworkTask task1, NetworkTask task2) {
        assertEquals(task1.getIndex(), task2.getIndex());
        assertEquals(task1.getSchedulerid(), task2.getSchedulerid());
        assertEquals(task1.getAccessType(), task2.getAccessType());
        assertEquals(task1.getAddress(), task2.getAddress());
        assertEquals(task1.getPort(), task2.getPort());
        assertEquals(task1.getInterval(), task2.getInterval());
        assertEquals(task1.isOnlyWifi(), task2.isOnlyWifi());
        assertEquals(task1.isNotification(), task2.isNotification());
    }
}
