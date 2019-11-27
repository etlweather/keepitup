package de.ibba.keepitup.util;

import android.util.Log;

import com.google.common.net.InetAddresses;
import com.google.common.net.InternetDomainName;

import java.net.IDN;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class URLUtil {

    public static boolean isValidIPAddress(String ipAddress) {
        return InetAddresses.isInetAddress(ipAddress);
    }

    public static boolean isValidHostName(String hostName) {
        return InternetDomainName.isValid(hostName);
    }

    public static boolean isValidURL(String inputUrl) {
        Log.d(URLUtil.class.getName(), "isValidURL, inputUrl is " + inputUrl);
        try {
            URL url = new URL(inputUrl);
            @SuppressWarnings("unused") URI uri = new URI(url.getProtocol(), url.getUserInfo(), IDN.toASCII(url.getHost()), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
            return !StringUtil.isEmpty(IDN.toASCII(url.getHost()));
        } catch (MalformedURLException | URISyntaxException exc) {
            Log.d(URLUtil.class.getName(), "Exception parsing url " + inputUrl, exc);
        }
        return false;
    }

    public static String prefixHTTPProtocol(String inputUrl) {
        Log.d(URLUtil.class.getName(), "prefixHTTPProtocol, inputUrl is " + inputUrl);
        if (inputUrl.toLowerCase().startsWith("http://") || inputUrl.toLowerCase().startsWith("https://")) {
            return inputUrl;
        }
        return "http://" + inputUrl;
    }

    public static String encodeURL(String inputUrl) {
        Log.d(URLUtil.class.getName(), "encodeURL, inputUrl is " + inputUrl);
        try {
            URL url = new URL(inputUrl);
            URI uri = new URI(url.getProtocol(), url.getUserInfo(), IDN.toASCII(url.getHost()), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
            return uri.toASCIIString();
        } catch (MalformedURLException | URISyntaxException exc) {
            Log.d(URLUtil.class.getName(), "Exception parsing url " + inputUrl, exc);
        }
        return inputUrl;
    }

    public static URL getURL(String inputUrl, String inputHost) {
        Log.d(URLUtil.class.getName(), "getURL, inputUrl is " + inputUrl + ", inputHost is " + inputHost);
        String encodedInputUrl = encodeURL(inputUrl);
        if (!isValidURL(encodedInputUrl)) {
            Log.d(URLUtil.class.getName(), "URL " + encodedInputUrl + " is invalid");
            return null;
        }
        try {
            URL url = new URL(encodeURL(inputUrl));
            String host;
            if (inputHost == null) {
                host = url.getHost();
            } else {
                host = inputHost;
            }
            URI uri = new URI(url.getProtocol(), url.getUserInfo(), IDN.toASCII(host), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
            return uri.toURL();
        } catch (MalformedURLException | URISyntaxException exc) {
            Log.d(URLUtil.class.getName(), "Exception parsing url " + inputUrl, exc);
        }
        return null;
    }
}
