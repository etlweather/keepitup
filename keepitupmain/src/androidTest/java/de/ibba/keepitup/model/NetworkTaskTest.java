package de.ibba.keepitup.model;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SmallTest;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@SmallTest
@RunWith(AndroidJUnit4.class)
public class NetworkTaskTest {

    @Test
    public void testToBundleDefaultValues() {
        NetworkTask task = new NetworkTask();
        assertEquals(-1, task.getId());
        assertEquals(-1, task.getIndex());
        assertEquals(-1, task.getSchedulerid());
        assertNull(task.getAddress());
        assertEquals(0, task.getPort());
        assertNull(task.getAccessType());
        assertEquals(0, task.getInterval());
        assertFalse(task.isOnlyWifi());
        assertFalse(task.isNotification());
        PersistableBundle persistableBundle = task.toPersistableBundle();
        assertNotNull(persistableBundle);
        task = new NetworkTask(persistableBundle);
        assertEquals(-1, task.getId());
        assertEquals(-1, task.getIndex());
        assertEquals(-1, task.getSchedulerid());
        assertNull(task.getAddress());
        assertEquals(0, task.getPort());
        assertNull(task.getAccessType());
        assertEquals(0, task.getInterval());
        assertFalse(task.isOnlyWifi());
        assertFalse(task.isNotification());
        Bundle bundle = task.toBundle();
        assertNotNull(bundle);
        task = new NetworkTask(bundle);
        assertEquals(-1, task.getId());
        assertEquals(-1, task.getIndex());
        assertEquals(-1, task.getSchedulerid());
        assertNull(task.getAddress());
        assertEquals(0, task.getPort());
        assertNull(task.getAccessType());
        assertEquals(0, task.getInterval());
        assertFalse(task.isOnlyWifi());
        assertFalse(task.isNotification());
    }

    @Test
    public void testToBundleContextValues() {
        NetworkTask task = new NetworkTask(InstrumentationRegistry.getTargetContext());
        assertEquals(-1, task.getId());
        assertEquals(-1, task.getIndex());
        assertEquals(-1, task.getSchedulerid());
        assertEquals("192.168.178.1", task.getAddress());
        assertEquals(22, task.getPort());
        assertEquals(AccessType.PING, task.getAccessType());
        assertEquals(15, task.getInterval());
        assertFalse(task.isOnlyWifi());
        assertFalse(task.isNotification());
        PersistableBundle persistableBundle = task.toPersistableBundle();
        assertNotNull(persistableBundle);
        task = new NetworkTask(persistableBundle);
        assertEquals(-1, task.getId());
        assertEquals(-1, task.getIndex());
        assertEquals(-1, task.getSchedulerid());
        assertEquals("192.168.178.1", task.getAddress());
        assertEquals(22, task.getPort());
        assertEquals(AccessType.PING, task.getAccessType());
        assertEquals(15, task.getInterval());
        assertFalse(task.isOnlyWifi());
        assertFalse(task.isNotification());
        Bundle bundle = task.toBundle();
        assertNotNull(bundle);
        task = new NetworkTask(bundle);
        assertEquals(-1, task.getId());
        assertEquals(-1, task.getIndex());
        assertEquals(-1, task.getSchedulerid());
        assertEquals("192.168.178.1", task.getAddress());
        assertEquals(22, task.getPort());
        assertEquals(AccessType.PING, task.getAccessType());
        assertEquals(15, task.getInterval());
        assertFalse(task.isOnlyWifi());
        assertFalse(task.isNotification());
    }

    @Test
    public void testToBundleValues() {
        NetworkTask task = new NetworkTask();
        task.setId(1);
        task.setIndex(2);
        task.setSchedulerid(3);
        task.setAddress("127.0.0.1");
        task.setPort(23);
        task.setAccessType(AccessType.PING);
        task.setInterval(15);
        task.setOnlyWifi(true);
        task.setNotification(true);
        assertEquals(1, task.getId());
        assertEquals(2, task.getIndex());
        assertEquals(3, task.getSchedulerid());
        assertEquals("127.0.0.1", task.getAddress());
        assertEquals(23, task.getPort());
        assertEquals(AccessType.PING, task.getAccessType());
        assertEquals(15, task.getInterval());
        assertTrue(task.isOnlyWifi());
        assertTrue(task.isNotification());
        PersistableBundle persistableBundle = task.toPersistableBundle();
        assertNotNull(persistableBundle);
        task = new NetworkTask(persistableBundle);
        assertEquals(1, task.getId());
        assertEquals(2, task.getIndex());
        assertEquals(3, task.getSchedulerid());
        assertEquals("127.0.0.1", task.getAddress());
        assertEquals(23, task.getPort());
        assertEquals(AccessType.PING, task.getAccessType());
        assertEquals(15, task.getInterval());
        assertTrue(task.isOnlyWifi());
        assertTrue(task.isNotification());
        Bundle bundle = task.toBundle();
        assertNotNull(bundle);
        task = new NetworkTask(bundle);
        assertEquals(1, task.getId());
        assertEquals(2, task.getIndex());
        assertEquals(3, task.getSchedulerid());
        assertEquals("127.0.0.1", task.getAddress());
        assertEquals(23, task.getPort());
        assertEquals(AccessType.PING, task.getAccessType());
        assertEquals(15, task.getInterval());
        assertTrue(task.isOnlyWifi());
        assertTrue(task.isNotification());
    }
}
