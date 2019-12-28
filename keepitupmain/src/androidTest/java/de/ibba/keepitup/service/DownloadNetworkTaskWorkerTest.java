package de.ibba.keepitup.service;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.MediumTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;

import de.ibba.keepitup.model.AccessType;
import de.ibba.keepitup.model.LogEntry;
import de.ibba.keepitup.model.NetworkTask;
import de.ibba.keepitup.resources.PreferenceManager;
import de.ibba.keepitup.service.network.DNSLookupResult;
import de.ibba.keepitup.service.network.DownloadCommandResult;
import de.ibba.keepitup.test.mock.MockDNSLookup;
import de.ibba.keepitup.test.mock.MockDownloadCommand;
import de.ibba.keepitup.test.mock.MockFileManager;
import de.ibba.keepitup.test.mock.MockTimeService;
import de.ibba.keepitup.test.mock.TestDownloadNetworkTaskWorker;
import de.ibba.keepitup.test.mock.TestRegistry;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

@MediumTest
@RunWith(AndroidJUnit4.class)
public class DownloadNetworkTaskWorkerTest {

    private TestDownloadNetworkTaskWorker downloadNetworkTaskWorker;
    private MockFileManager fileManager;
    private PreferenceManager preferenceManager;

    @Before
    public void beforeEachTestMethod() {
        downloadNetworkTaskWorker = new TestDownloadNetworkTaskWorker(TestRegistry.getContext(), getNetworkTask(), null);
        fileManager = new MockFileManager();
        fileManager.setInternalDownloadDirectory(new File("test"));
        preferenceManager = new PreferenceManager(TestRegistry.getContext());
        preferenceManager.removeAllPreferences();
    }

    @After
    public void afterEachTestMethod() {
        fileManager.reset();
        preferenceManager.removeAllPreferences();
    }

    private void prepareTestDownloadNetworkTaskWorker(DNSLookupResult dnsLookupResult, DownloadCommandResult downloadCommandResult, RuntimeException exception) throws Exception {
        MockDNSLookup mockDNSLookup = new MockDNSLookup("127.0.0.1", dnsLookupResult);
        MockDownloadCommand mockDownloadCommand;
        if (exception == null) {
            mockDownloadCommand = new MockDownloadCommand(TestRegistry.getContext(), getNetworkTask(), new URL("http://127.0.0.1"), new File("folder"), true, downloadCommandResult);
        } else {
            mockDownloadCommand = new MockDownloadCommand(TestRegistry.getContext(), getNetworkTask(), new URL("http://127.0.0.1"), new File("folder"), true, exception);
        }
        downloadNetworkTaskWorker.setMockDNSLookup(mockDNSLookup);
        downloadNetworkTaskWorker.setMockDownloadCommand(mockDownloadCommand);
        downloadNetworkTaskWorker.setMockFileManager(fileManager);
        MockTimeService timeService = (MockTimeService) downloadNetworkTaskWorker.getTimeService();
        timeService.setTimestamp(getTestTimestamp());
    }

    private void prepareTestDownloadNetworkTaskWorker(DNSLookupResult dnsLookupResult, DownloadCommandResult downloadCommandResult) throws Exception {
        prepareTestDownloadNetworkTaskWorker(dnsLookupResult, downloadCommandResult, null);
    }

    private void prepareTestDownloadNetworkTaskWorker(DNSLookupResult dnsLookupResult, RuntimeException exception) throws Exception {
        prepareTestDownloadNetworkTaskWorker(dnsLookupResult, null, exception);
    }

    @Test
    public void testDNSLookupExceptionThrown() throws Exception {
        IllegalArgumentException exception = new IllegalArgumentException("TestException");
        DNSLookupResult dnsLookupResult = new DNSLookupResult(Collections.emptyList(), exception);
        prepareTestDownloadNetworkTaskWorker(dnsLookupResult, (DownloadCommandResult) null);
        LogEntry logEntry = downloadNetworkTaskWorker.execute(getNetworkTask());
        assertEquals(45, logEntry.getNetworkTaskId());
        assertEquals(getTestTimestamp(), logEntry.getTimestamp());
        assertFalse(logEntry.isSuccess());
        assertEquals("DNS lookup for http://127.0.0.1 failed. IllegalArgumentException: TestException", logEntry.getMessage());
    }

    @Test
    public void testInvalidURL() throws Exception {
        DNSLookupResult dnsLookupResult = new DNSLookupResult(Arrays.asList(InetAddress.getByName("127.0.0.1"), InetAddress.getByName("::1")), null);
        prepareTestDownloadNetworkTaskWorker(dnsLookupResult, (DownloadCommandResult) null);
        NetworkTask networkTask = getNetworkTask();
        networkTask.setAddress("invalid url");
        LogEntry logEntry = downloadNetworkTaskWorker.execute(networkTask);
        assertEquals(45, logEntry.getNetworkTaskId());
        assertEquals(getTestTimestamp(), logEntry.getTimestamp());
        assertFalse(logEntry.isSuccess());
        assertEquals("Download not possible. The url invalid url is invalid.", logEntry.getMessage());
    }

    @Test
    public void testInvalidInternalFolder() throws Exception {
        DNSLookupResult dnsLookupResult = new DNSLookupResult(Arrays.asList(InetAddress.getByName("127.0.0.1"), InetAddress.getByName("::1")), null);
        prepareTestDownloadNetworkTaskWorker(dnsLookupResult, (DownloadCommandResult) null);
        preferenceManager.setPreferenceDownloadExternalStorage(false);
        fileManager.setInternalDownloadDirectory(null);
        fileManager.setDefaultDownloadDirectoryName("download");
        LogEntry logEntry = downloadNetworkTaskWorker.execute(getNetworkTask());
        assertEquals(45, logEntry.getNetworkTaskId());
        assertEquals(getTestTimestamp(), logEntry.getTimestamp());
        assertFalse(logEntry.isSuccess());
        assertEquals("Download not possible. The download folder download does not exist and cannot be created.", logEntry.getMessage());
    }

    @Test
    public void testInvalidExternalFolder() throws Exception {
        DNSLookupResult dnsLookupResult = new DNSLookupResult(Arrays.asList(InetAddress.getByName("127.0.0.1"), InetAddress.getByName("::1")), null);
        prepareTestDownloadNetworkTaskWorker(dnsLookupResult, (DownloadCommandResult) null);
        preferenceManager.setPreferenceDownloadExternalStorage(true);
        fileManager.setExternalDirectory(null);
        LogEntry logEntry = downloadNetworkTaskWorker.execute(getNetworkTask());
        assertEquals(45, logEntry.getNetworkTaskId());
        assertEquals(getTestTimestamp(), logEntry.getTimestamp());
        assertFalse(logEntry.isSuccess());
        assertEquals("Download not possible. The download folder download does not exist and cannot be created.", logEntry.getMessage());
    }

    @Test
    public void testConnectionFailed() throws Exception {
        DNSLookupResult dnsLookupResult = new DNSLookupResult(Arrays.asList(InetAddress.getByName("127.0.0.1"), InetAddress.getByName("::1")), null);
        DownloadCommandResult downloadCommandResult = new DownloadCommandResult(false, false, false, false, false, false, -1, null, null, null);
        prepareTestDownloadNetworkTaskWorker(dnsLookupResult, downloadCommandResult);
        LogEntry logEntry = downloadNetworkTaskWorker.execute(getNetworkTask());
        assertEquals(45, logEntry.getNetworkTaskId());
        assertEquals(getTestTimestamp(), logEntry.getTimestamp());
        assertFalse(logEntry.isSuccess());
        assertEquals("Connection to 127.0.0.1 failed.", logEntry.getMessage());
    }

    @Test
    public void testHTTPResponseCodeNotOk() throws Exception {
        DNSLookupResult dnsLookupResult = new DNSLookupResult(Arrays.asList(InetAddress.getByName("127.0.0.1"), InetAddress.getByName("::1")), null);
        DownloadCommandResult downloadCommandResult = new DownloadCommandResult(true, false, false, false, false, false, HttpURLConnection.HTTP_NOT_FOUND, "message", null, null);
        prepareTestDownloadNetworkTaskWorker(dnsLookupResult, downloadCommandResult);
        LogEntry logEntry = downloadNetworkTaskWorker.execute(getNetworkTask());
        assertEquals(45, logEntry.getNetworkTaskId());
        assertEquals(getTestTimestamp(), logEntry.getTimestamp());
        assertFalse(logEntry.isSuccess());
        assertEquals("The download from http://127.0.0.1 failed. Server return code 404 message.", logEntry.getMessage());
    }

    @Test
    public void testDownloadStoppedFileDoesNotExist() throws Exception {
        DNSLookupResult dnsLookupResult = new DNSLookupResult(Arrays.asList(InetAddress.getByName("127.0.0.1"), InetAddress.getByName("::1")), null);
        DownloadCommandResult downloadCommandResult = new DownloadCommandResult(true, false, false, false, false, true, HttpURLConnection.HTTP_OK, null, null, null);
        prepareTestDownloadNetworkTaskWorker(dnsLookupResult, downloadCommandResult);
        LogEntry logEntry = downloadNetworkTaskWorker.execute(getNetworkTask());
        assertEquals(45, logEntry.getNetworkTaskId());
        assertEquals(getTestTimestamp(), logEntry.getTimestamp());
        assertFalse(logEntry.isSuccess());
        assertEquals("The download was stopped.", logEntry.getMessage());
    }

    @Test
    public void testDownloadStoppedFileDoesNotExistWithException() throws Exception {
        DNSLookupResult dnsLookupResult = new DNSLookupResult(Arrays.asList(InetAddress.getByName("127.0.0.1"), InetAddress.getByName("::1")), null);
        DownloadCommandResult downloadCommandResult = new DownloadCommandResult(true, false, false, false, false, true, HttpURLConnection.HTTP_OK, null, null, new Exception("Test"));
        prepareTestDownloadNetworkTaskWorker(dnsLookupResult, downloadCommandResult);
        LogEntry logEntry = downloadNetworkTaskWorker.execute(getNetworkTask());
        assertEquals(45, logEntry.getNetworkTaskId());
        assertEquals(getTestTimestamp(), logEntry.getTimestamp());
        assertFalse(logEntry.isSuccess());
        assertEquals("The download was stopped. Exception: Test", logEntry.getMessage());
    }

    @Test
    public void testDownloadStoppedFileExistNotDelete() throws Exception {
        DNSLookupResult dnsLookupResult = new DNSLookupResult(Arrays.asList(InetAddress.getByName("127.0.0.1"), InetAddress.getByName("::1")), null);
        DownloadCommandResult downloadCommandResult = new DownloadCommandResult(true, false, true, false, false, true, HttpURLConnection.HTTP_OK, null, "testfile", null);
        prepareTestDownloadNetworkTaskWorker(dnsLookupResult, downloadCommandResult);
        preferenceManager.setPreferenceDownloadKeep(true);
        preferenceManager.setPreferenceDownloadExternalStorage(true);
        fileManager.setExternalDirectory(new File("Test"));
        LogEntry logEntry = downloadNetworkTaskWorker.execute(getNetworkTask());
        assertEquals(45, logEntry.getNetworkTaskId());
        assertEquals(getTestTimestamp(), logEntry.getTimestamp());
        assertFalse(logEntry.isSuccess());
        assertEquals("The download was stopped. The file was partially downloaded. Downloaded file: /Test/testfile.", logEntry.getMessage());
    }

    @Test
    public void testDownloadStoppedFileExistNotDeleteWithException() throws Exception {
        DNSLookupResult dnsLookupResult = new DNSLookupResult(Arrays.asList(InetAddress.getByName("127.0.0.1"), InetAddress.getByName("::1")), null);
        DownloadCommandResult downloadCommandResult = new DownloadCommandResult(true, false, true, false, false, true, HttpURLConnection.HTTP_OK, null, "testfile", new Exception("Test"));
        prepareTestDownloadNetworkTaskWorker(dnsLookupResult, downloadCommandResult);
        preferenceManager.setPreferenceDownloadKeep(true);
        preferenceManager.setPreferenceDownloadExternalStorage(true);
        fileManager.setExternalDirectory(new File("Test"));
        LogEntry logEntry = downloadNetworkTaskWorker.execute(getNetworkTask());
        assertEquals(45, logEntry.getNetworkTaskId());
        assertEquals(getTestTimestamp(), logEntry.getTimestamp());
        assertFalse(logEntry.isSuccess());
        assertEquals("The download was stopped. The file was partially downloaded. Downloaded file: /Test/testfile. Exception: Test", logEntry.getMessage());
    }

    @Test
    public void testDownloadStoppedFileExistDeleteFailedInternalStorage() throws Exception {
        DNSLookupResult dnsLookupResult = new DNSLookupResult(Arrays.asList(InetAddress.getByName("127.0.0.1"), InetAddress.getByName("::1")), null);
        DownloadCommandResult downloadCommandResult = new DownloadCommandResult(true, false, true, false, false, true, HttpURLConnection.HTTP_OK, null, "testfile", null);
        prepareTestDownloadNetworkTaskWorker(dnsLookupResult, downloadCommandResult);
        LogEntry logEntry = downloadNetworkTaskWorker.execute(getNetworkTask());
        assertEquals(45, logEntry.getNetworkTaskId());
        assertEquals(getTestTimestamp(), logEntry.getTimestamp());
        assertFalse(logEntry.isSuccess());
        assertEquals("The download was stopped. The file was partially downloaded. The deletion of the partially_downloaded file failed.", logEntry.getMessage());
    }

    @Test
    public void testDownloadStoppedFileExistDeleteFailedInternalStorageWithException() throws Exception {
        DNSLookupResult dnsLookupResult = new DNSLookupResult(Arrays.asList(InetAddress.getByName("127.0.0.1"), InetAddress.getByName("::1")), null);
        DownloadCommandResult downloadCommandResult = new DownloadCommandResult(true, false, true, false, false, true, HttpURLConnection.HTTP_OK, null, "testfile", new Exception("Test"));
        prepareTestDownloadNetworkTaskWorker(dnsLookupResult, downloadCommandResult);
        LogEntry logEntry = downloadNetworkTaskWorker.execute(getNetworkTask());
        assertEquals(45, logEntry.getNetworkTaskId());
        assertEquals(getTestTimestamp(), logEntry.getTimestamp());
        assertFalse(logEntry.isSuccess());
        assertEquals("The download was stopped. The file was partially downloaded. The deletion of the partially_downloaded file failed. Exception: Test", logEntry.getMessage());
    }

    @Test
    public void testDownloadStoppedFileExistDeleteFailedExternalStorage() throws Exception {
        DNSLookupResult dnsLookupResult = new DNSLookupResult(Arrays.asList(InetAddress.getByName("127.0.0.1"), InetAddress.getByName("::1")), null);
        DownloadCommandResult downloadCommandResult = new DownloadCommandResult(true, false, true, false, false, true, HttpURLConnection.HTTP_OK, null, "testfile", null);
        prepareTestDownloadNetworkTaskWorker(dnsLookupResult, downloadCommandResult);
        preferenceManager.setPreferenceDownloadKeep(false);
        preferenceManager.setPreferenceDownloadExternalStorage(true);
        fileManager.setExternalDirectory(new File("Test"));
        LogEntry logEntry = downloadNetworkTaskWorker.execute(getNetworkTask());
        assertEquals(45, logEntry.getNetworkTaskId());
        assertEquals(getTestTimestamp(), logEntry.getTimestamp());
        assertFalse(logEntry.isSuccess());
        assertEquals("The download was stopped. The file was partially downloaded. The deletion of the partially_downloaded file failed.", logEntry.getMessage());
    }

    @Test
    public void testDownloadStoppedFileExistDeleteFailedExternalStorageWithException() throws Exception {
        DNSLookupResult dnsLookupResult = new DNSLookupResult(Arrays.asList(InetAddress.getByName("127.0.0.1"), InetAddress.getByName("::1")), null);
        DownloadCommandResult downloadCommandResult = new DownloadCommandResult(true, false, true, false, false, true, HttpURLConnection.HTTP_OK, null, "testfile", new Exception("Test"));
        prepareTestDownloadNetworkTaskWorker(dnsLookupResult, downloadCommandResult);
        preferenceManager.setPreferenceDownloadKeep(false);
        preferenceManager.setPreferenceDownloadExternalStorage(true);
        fileManager.setExternalDirectory(new File("Test"));
        LogEntry logEntry = downloadNetworkTaskWorker.execute(getNetworkTask());
        assertEquals(45, logEntry.getNetworkTaskId());
        assertEquals(getTestTimestamp(), logEntry.getTimestamp());
        assertFalse(logEntry.isSuccess());
        assertEquals("The download was stopped. The file was partially downloaded. The deletion of the partially_downloaded file failed. Exception: Test", logEntry.getMessage());
    }

    @Test
    public void testDownloadStoppedFileExistDeleteSuccess() throws Exception {
        DNSLookupResult dnsLookupResult = new DNSLookupResult(Arrays.asList(InetAddress.getByName("127.0.0.1"), InetAddress.getByName("::1")), null);
        DownloadCommandResult downloadCommandResult = new DownloadCommandResult(true, false, true, true, false, true, HttpURLConnection.HTTP_OK, null, "testfile", null);
        prepareTestDownloadNetworkTaskWorker(dnsLookupResult, downloadCommandResult);
        LogEntry logEntry = downloadNetworkTaskWorker.execute(getNetworkTask());
        assertEquals(45, logEntry.getNetworkTaskId());
        assertEquals(getTestTimestamp(), logEntry.getTimestamp());
        assertFalse(logEntry.isSuccess());
        assertEquals("The download was stopped. The file was partially downloaded. The partially downloaded file was deleted.", logEntry.getMessage());
    }

    @Test
    public void testDownloadStoppedFileExistDeleteSuccessWithException() throws Exception {
        DNSLookupResult dnsLookupResult = new DNSLookupResult(Arrays.asList(InetAddress.getByName("127.0.0.1"), InetAddress.getByName("::1")), null);
        DownloadCommandResult downloadCommandResult = new DownloadCommandResult(true, false, true, true, false, true, HttpURLConnection.HTTP_OK, null, "testfile", new Exception("Test"));
        prepareTestDownloadNetworkTaskWorker(dnsLookupResult, downloadCommandResult);
        LogEntry logEntry = downloadNetworkTaskWorker.execute(getNetworkTask());
        assertEquals(45, logEntry.getNetworkTaskId());
        assertEquals(getTestTimestamp(), logEntry.getTimestamp());
        assertFalse(logEntry.isSuccess());
        assertEquals("The download was stopped. The file was partially downloaded. The partially downloaded file was deleted. Exception: Test", logEntry.getMessage());
    }

    @Test
    public void testDownloadInvalidFileDoesNotExist() throws Exception {
        DNSLookupResult dnsLookupResult = new DNSLookupResult(Arrays.asList(InetAddress.getByName("127.0.0.1"), InetAddress.getByName("::1")), null);
        DownloadCommandResult downloadCommandResult = new DownloadCommandResult(true, false, false, false, false, false, HttpURLConnection.HTTP_OK, null, null, null);
        prepareTestDownloadNetworkTaskWorker(dnsLookupResult, downloadCommandResult);
        LogEntry logEntry = downloadNetworkTaskWorker.execute(getNetworkTask());
        assertEquals(45, logEntry.getNetworkTaskId());
        assertEquals(getTestTimestamp(), logEntry.getTimestamp());
        assertFalse(logEntry.isSuccess());
        assertEquals("The download was stopped because the network task is no longer valid.", logEntry.getMessage());
    }

    @Test
    public void testDownloadInvalidFileDoesNotExistWithException() throws Exception {
        DNSLookupResult dnsLookupResult = new DNSLookupResult(Arrays.asList(InetAddress.getByName("127.0.0.1"), InetAddress.getByName("::1")), null);
        DownloadCommandResult downloadCommandResult = new DownloadCommandResult(true, false, false, false, false, false, HttpURLConnection.HTTP_OK, null, null, new Exception("Test"));
        prepareTestDownloadNetworkTaskWorker(dnsLookupResult, downloadCommandResult);
        LogEntry logEntry = downloadNetworkTaskWorker.execute(getNetworkTask());
        assertEquals(45, logEntry.getNetworkTaskId());
        assertEquals(getTestTimestamp(), logEntry.getTimestamp());
        assertFalse(logEntry.isSuccess());
        assertEquals("The download was stopped because the network task is no longer valid. Exception: Test", logEntry.getMessage());
    }

    @Test
    public void testDownloadInvalidFileExistNotDelete() throws Exception {
        DNSLookupResult dnsLookupResult = new DNSLookupResult(Arrays.asList(InetAddress.getByName("127.0.0.1"), InetAddress.getByName("::1")), null);
        DownloadCommandResult downloadCommandResult = new DownloadCommandResult(true, false, true, false, false, false, HttpURLConnection.HTTP_OK, null, "testfile", null);
        prepareTestDownloadNetworkTaskWorker(dnsLookupResult, downloadCommandResult);
        preferenceManager.setPreferenceDownloadKeep(true);
        preferenceManager.setPreferenceDownloadExternalStorage(true);
        fileManager.setExternalDirectory(new File("Test"));
        LogEntry logEntry = downloadNetworkTaskWorker.execute(getNetworkTask());
        assertEquals(45, logEntry.getNetworkTaskId());
        assertEquals(getTestTimestamp(), logEntry.getTimestamp());
        assertFalse(logEntry.isSuccess());
        assertEquals("The download was stopped because the network task is no longer valid. The file was partially downloaded. Downloaded file: /Test/testfile.", logEntry.getMessage());
    }

    @Test
    public void testDownloadInvalidFileExistNotDeleteWithException() throws Exception {
        DNSLookupResult dnsLookupResult = new DNSLookupResult(Arrays.asList(InetAddress.getByName("127.0.0.1"), InetAddress.getByName("::1")), null);
        DownloadCommandResult downloadCommandResult = new DownloadCommandResult(true, false, true, false, false, false, HttpURLConnection.HTTP_OK, null, "testfile", new Exception("Test"));
        prepareTestDownloadNetworkTaskWorker(dnsLookupResult, downloadCommandResult);
        preferenceManager.setPreferenceDownloadKeep(true);
        preferenceManager.setPreferenceDownloadExternalStorage(true);
        fileManager.setExternalDirectory(new File("Test"));
        LogEntry logEntry = downloadNetworkTaskWorker.execute(getNetworkTask());
        assertEquals(45, logEntry.getNetworkTaskId());
        assertEquals(getTestTimestamp(), logEntry.getTimestamp());
        assertFalse(logEntry.isSuccess());
        assertEquals("The download was stopped because the network task is no longer valid. The file was partially downloaded. Downloaded file: /Test/testfile. Exception: Test", logEntry.getMessage());
    }

    @Test
    public void testDownloadInvalidFileExistDeleteFailedInternalStorage() throws Exception {
        DNSLookupResult dnsLookupResult = new DNSLookupResult(Arrays.asList(InetAddress.getByName("127.0.0.1"), InetAddress.getByName("::1")), null);
        DownloadCommandResult downloadCommandResult = new DownloadCommandResult(true, false, true, false, false, false, HttpURLConnection.HTTP_OK, null, "testfile", null);
        prepareTestDownloadNetworkTaskWorker(dnsLookupResult, downloadCommandResult);
        LogEntry logEntry = downloadNetworkTaskWorker.execute(getNetworkTask());
        assertEquals(45, logEntry.getNetworkTaskId());
        assertEquals(getTestTimestamp(), logEntry.getTimestamp());
        assertFalse(logEntry.isSuccess());
        assertEquals("The download was stopped because the network task is no longer valid. The file was partially downloaded. The deletion of the partially_downloaded file failed.", logEntry.getMessage());
    }

    @Test
    public void testDownloadInvalidFileExistDeleteFailedInternalStorageWithExcpetion() throws Exception {
        DNSLookupResult dnsLookupResult = new DNSLookupResult(Arrays.asList(InetAddress.getByName("127.0.0.1"), InetAddress.getByName("::1")), null);
        DownloadCommandResult downloadCommandResult = new DownloadCommandResult(true, false, true, false, false, false, HttpURLConnection.HTTP_OK, null, "testfile", new Exception("Test"));
        prepareTestDownloadNetworkTaskWorker(dnsLookupResult, downloadCommandResult);
        LogEntry logEntry = downloadNetworkTaskWorker.execute(getNetworkTask());
        assertEquals(45, logEntry.getNetworkTaskId());
        assertEquals(getTestTimestamp(), logEntry.getTimestamp());
        assertFalse(logEntry.isSuccess());
        assertEquals("The download was stopped because the network task is no longer valid. The file was partially downloaded. The deletion of the partially_downloaded file failed. Exception: Test", logEntry.getMessage());
    }

    @Test
    public void testDownloadInvalidFileExistDeleteFailedExternalStorage() throws Exception {
        DNSLookupResult dnsLookupResult = new DNSLookupResult(Arrays.asList(InetAddress.getByName("127.0.0.1"), InetAddress.getByName("::1")), null);
        DownloadCommandResult downloadCommandResult = new DownloadCommandResult(true, false, true, false, false, false, HttpURLConnection.HTTP_OK, null, "testfile", null);
        prepareTestDownloadNetworkTaskWorker(dnsLookupResult, downloadCommandResult);
        preferenceManager.setPreferenceDownloadKeep(false);
        preferenceManager.setPreferenceDownloadExternalStorage(true);
        fileManager.setExternalDirectory(new File("Test"));
        LogEntry logEntry = downloadNetworkTaskWorker.execute(getNetworkTask());
        assertEquals(45, logEntry.getNetworkTaskId());
        assertEquals(getTestTimestamp(), logEntry.getTimestamp());
        assertFalse(logEntry.isSuccess());
        assertEquals("The download was stopped because the network task is no longer valid. The file was partially downloaded. The deletion of the partially_downloaded file failed.", logEntry.getMessage());
    }

    @Test
    public void testDownloadInvalidFileExistDeleteFailedExternalStorageWithException() throws Exception {
        DNSLookupResult dnsLookupResult = new DNSLookupResult(Arrays.asList(InetAddress.getByName("127.0.0.1"), InetAddress.getByName("::1")), null);
        DownloadCommandResult downloadCommandResult = new DownloadCommandResult(true, false, true, false, false, false, HttpURLConnection.HTTP_OK, null, "testfile", new Exception("Test"));
        prepareTestDownloadNetworkTaskWorker(dnsLookupResult, downloadCommandResult);
        preferenceManager.setPreferenceDownloadKeep(false);
        preferenceManager.setPreferenceDownloadExternalStorage(true);
        fileManager.setExternalDirectory(new File("Test"));
        LogEntry logEntry = downloadNetworkTaskWorker.execute(getNetworkTask());
        assertEquals(45, logEntry.getNetworkTaskId());
        assertEquals(getTestTimestamp(), logEntry.getTimestamp());
        assertFalse(logEntry.isSuccess());
        assertEquals("The download was stopped because the network task is no longer valid. The file was partially downloaded. The deletion of the partially_downloaded file failed. Exception: Test", logEntry.getMessage());
    }

    @Test
    public void testDownloadInvalidFileExistDeleteSuccess() throws Exception {
        DNSLookupResult dnsLookupResult = new DNSLookupResult(Arrays.asList(InetAddress.getByName("127.0.0.1"), InetAddress.getByName("::1")), null);
        DownloadCommandResult downloadCommandResult = new DownloadCommandResult(true, false, true, true, false, false, HttpURLConnection.HTTP_OK, null, "testfile", null);
        prepareTestDownloadNetworkTaskWorker(dnsLookupResult, downloadCommandResult);
        LogEntry logEntry = downloadNetworkTaskWorker.execute(getNetworkTask());
        assertEquals(45, logEntry.getNetworkTaskId());
        assertEquals(getTestTimestamp(), logEntry.getTimestamp());
        assertFalse(logEntry.isSuccess());
        assertEquals("The download was stopped because the network task is no longer valid. The file was partially downloaded. The partially downloaded file was deleted.", logEntry.getMessage());
    }

    @Test
    public void testDownloadInvalidFileExistDeleteSuccessWithException() throws Exception {
        DNSLookupResult dnsLookupResult = new DNSLookupResult(Arrays.asList(InetAddress.getByName("127.0.0.1"), InetAddress.getByName("::1")), null);
        DownloadCommandResult downloadCommandResult = new DownloadCommandResult(true, false, true, true, false, false, HttpURLConnection.HTTP_OK, null, "testfile", new Exception("Test"));
        prepareTestDownloadNetworkTaskWorker(dnsLookupResult, downloadCommandResult);
        LogEntry logEntry = downloadNetworkTaskWorker.execute(getNetworkTask());
        assertEquals(45, logEntry.getNetworkTaskId());
        assertEquals(getTestTimestamp(), logEntry.getTimestamp());
        assertFalse(logEntry.isSuccess());
        assertEquals("The download was stopped because the network task is no longer valid. The file was partially downloaded. The partially downloaded file was deleted. Exception: Test", logEntry.getMessage());
    }

    private long getTestTimestamp() {
        Calendar calendar = new GregorianCalendar(1985, Calendar.DECEMBER, 24, 1, 1, 1);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTimeInMillis();
    }

    private NetworkTask getNetworkTask() {
        NetworkTask task = new NetworkTask();
        task.setId(45);
        task.setIndex(1);
        task.setSchedulerId(0);
        task.setInstances(0);
        task.setAddress("http://127.0.0.1");
        task.setPort(80);
        task.setAccessType(AccessType.DOWNLOAD);
        task.setInterval(15);
        task.setOnlyWifi(false);
        task.setNotification(true);
        task.setRunning(true);
        return task;
    }
}
