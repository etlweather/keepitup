package de.ibba.keepitup.service;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.MediumTest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.net.InetAddress;

import de.ibba.keepitup.model.AccessType;
import de.ibba.keepitup.model.LogEntry;
import de.ibba.keepitup.model.NetworkTask;
import de.ibba.keepitup.service.network.DNSLookupResult;
import de.ibba.keepitup.service.network.PingCommandResult;
import de.ibba.keepitup.test.mock.MockDNSLookup;
import de.ibba.keepitup.test.mock.MockPingCommand;
import de.ibba.keepitup.test.mock.TestPingNetworkTaskWorker;
import de.ibba.keepitup.test.mock.TestRegistry;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@MediumTest
@RunWith(AndroidJUnit4.class)
public class PingNetworkTaskWorkerTest {

    private TestPingNetworkTaskWorker pingNetworkTaskWorker;

    @Before
    public void beforeEachTestMethod() {
        pingNetworkTaskWorker = new TestPingNetworkTaskWorker(TestRegistry.getContext(), getNetworkTask(), null);
    }

    private void prepareTestPingNetworkTaskWorker(DNSLookupResult dnsLookupResult, PingCommandResult pingCommandResult) {
        MockDNSLookup mockDNSLookup = new MockDNSLookup("127.0.0.1", dnsLookupResult);
        MockPingCommand mockPingCommand = new MockPingCommand(TestRegistry.getContext(), "127.0.0.1", false, pingCommandResult);
        pingNetworkTaskWorker.setMockDNSLookup(mockDNSLookup);
        pingNetworkTaskWorker.setMockPingCommand(mockPingCommand);
    }

    @Test
    public void testSuccessfulCallUnparseableResult() throws Exception {
        DNSLookupResult dnsLookupResult = new DNSLookupResult(InetAddress.getByName("127.0.0.1"), null);
        PingCommandResult pingCommandResult = new PingCommandResult(0, "testoutput", null);
        prepareTestPingNetworkTaskWorker(dnsLookupResult, pingCommandResult);
        LogEntry logEntry = pingNetworkTaskWorker.execute(getNetworkTask());
        assertEquals(45, logEntry.getNetworkTaskId());
        assertTrue(logEntry.getTimestamp() > -1);
        assertTrue(logEntry.isSuccess());
        assertEquals("Pinged 127.0.0.1 (IPv4) successfully. testoutput", logEntry.getMessage());
    }

    @Test
    public void testSuccessfulCallParseableResult() throws Exception {
        DNSLookupResult dnsLookupResult = new DNSLookupResult(InetAddress.getByName("127.0.0.1"), null);
        PingCommandResult pingCommandResult = new PingCommandResult(0, getTestIP4Ping(), null);
        prepareTestPingNetworkTaskWorker(dnsLookupResult, pingCommandResult);
        LogEntry logEntry = pingNetworkTaskWorker.execute(getNetworkTask());
        assertEquals(45, logEntry.getNetworkTaskId());
        assertTrue(logEntry.getTimestamp() > -1);
        assertTrue(logEntry.isSuccess());
        assertEquals("Pinged 127.0.0.1 (IPv4) successfully. 3 packets transmitted. 3 packets received. 0% packet loss. 0.083 msec average time.", logEntry.getMessage());
    }

    @Test
    public void testDNSLookupExceptionThrown() {
        IllegalArgumentException exception = new IllegalArgumentException("TestException");
        DNSLookupResult dnsLookupResult = new DNSLookupResult(null, exception);
        prepareTestPingNetworkTaskWorker(dnsLookupResult, null);
        LogEntry logEntry = pingNetworkTaskWorker.execute(getNetworkTask());
        assertEquals(45, logEntry.getNetworkTaskId());
        assertTrue(logEntry.getTimestamp() > -1);
        assertFalse(logEntry.isSuccess());
        assertEquals("DNS lookup for 127.0.0.1 failed. IllegalArgumentException: TestException", logEntry.getMessage());
    }

    @Test
    public void testPingCommandExceptionThrown() throws Exception {
        DNSLookupResult dnsLookupResult = new DNSLookupResult(InetAddress.getByName("127.0.0.1"), null);
        IllegalArgumentException exception = new IllegalArgumentException("TestException");
        PingCommandResult pingCommandResult = new PingCommandResult(0, "testoutput", exception);
        prepareTestPingNetworkTaskWorker(dnsLookupResult, pingCommandResult);
        LogEntry logEntry = pingNetworkTaskWorker.execute(getNetworkTask());
        assertEquals(45, logEntry.getNetworkTaskId());
        assertTrue(logEntry.getTimestamp() > -1);
        assertFalse(logEntry.isSuccess());
        assertEquals("Ping to 127.0.0.1 (IPv4) failed. IllegalArgumentException: TestException", logEntry.getMessage());
    }

    @Test
    public void testFailureCodeReturnedWithMessageUnparseable() throws Exception {
        DNSLookupResult dnsLookupResult = new DNSLookupResult(InetAddress.getByName("127.0.0.1"), null);
        PingCommandResult pingCommandResult = new PingCommandResult(1, "testoutput", null);
        prepareTestPingNetworkTaskWorker(dnsLookupResult, pingCommandResult);
        LogEntry logEntry = pingNetworkTaskWorker.execute(getNetworkTask());
        assertEquals(45, logEntry.getNetworkTaskId());
        assertTrue(logEntry.getTimestamp() > -1);
        assertFalse(logEntry.isSuccess());
        assertEquals("Ping to 127.0.0.1 (IPv4) failed. testoutput", logEntry.getMessage());
    }

    @Test
    public void testFailureCodeReturnedWithMessageParseable() throws Exception {
        DNSLookupResult dnsLookupResult = new DNSLookupResult(InetAddress.getByName("127.0.0.1"), null);
        PingCommandResult pingCommandResult = new PingCommandResult(1, getTestIP4Ping(), null);
        prepareTestPingNetworkTaskWorker(dnsLookupResult, pingCommandResult);
        LogEntry logEntry = pingNetworkTaskWorker.execute(getNetworkTask());
        assertEquals(45, logEntry.getNetworkTaskId());
        assertTrue(logEntry.getTimestamp() > -1);
        assertFalse(logEntry.isSuccess());
        assertEquals("Ping to 127.0.0.1 (IPv4) failed. 3 packets transmitted. 3 packets received. 0% packet loss. 0.083 msec average time.", logEntry.getMessage());
    }

    @Test
    public void testFailureCodeReturnedWithoutMessage() throws Exception {
        DNSLookupResult dnsLookupResult = new DNSLookupResult(InetAddress.getByName("127.0.0.1"), null);
        PingCommandResult pingCommandResult = new PingCommandResult(1, "", null);
        prepareTestPingNetworkTaskWorker(dnsLookupResult, pingCommandResult);
        LogEntry logEntry = pingNetworkTaskWorker.execute(getNetworkTask());
        assertEquals(45, logEntry.getNetworkTaskId());
        assertTrue(logEntry.getTimestamp() > -1);
        assertFalse(logEntry.isSuccess());
        assertEquals("Ping to 127.0.0.1 (IPv4) failed. Return code: 1", logEntry.getMessage());
    }

    private NetworkTask getNetworkTask() {
        NetworkTask task = new NetworkTask();
        task.setId(45);
        task.setIndex(1);
        task.setSchedulerId(0);
        task.setAddress("127.0.0.1");
        task.setPort(80);
        task.setAccessType(AccessType.PING);
        task.setInterval(15);
        task.setOnlyWifi(false);
        task.setNotification(true);
        task.setRunning(true);
        return task;
    }

    private String getTestIP4Ping() {
        return "PING 127.0.0.1 (127.0.0.1) 56(84) bytes of data.\n" +
                "64 bytes from 127.0.0.1: icmp_seq=1 ttl=64 time=0.084 ms\n" +
                " 64 bytes from 127.0.0.1: icmp_seq=2 ttl=64 time=0.083 ms\n" +
                "64 bytes from 127.0.0.1: icmp_seq=3 ttl=64 time=0.083 ms\n\n" +
                "--- 127.0.0.1 ping statistics ---\n" +
                "3 packets transmitted, 3 received, 0% packet loss, time 1998ms\n" +
                "rtt min/avg/max/mdev = 0.083/0.083/0.084/0.007 ms";
    }
}
