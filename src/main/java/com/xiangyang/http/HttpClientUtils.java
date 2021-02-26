package com.xiangyang.http;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.net.ssl.SSLException;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.NameValuePair;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.log4j.Logger;

public class HttpClientUtils {
    private static Logger logger = Logger.getLogger(HttpClientUtils.class);
    private static HttpClientEnhanceConfig httpClientEnhanceConfig;
    private static PoolingHttpClientConnectionManager cm;

    public HttpClientUtils() {
    }

    private static void initPoolingHttpClientConnectionManager() {
        if (null == cm) {
            ConnectionSocketFactory plainsf = PlainConnectionSocketFactory.getSocketFactory();
            LayeredConnectionSocketFactory sslsf = SSLConnectionSocketFactory.getSocketFactory();
            Registry<ConnectionSocketFactory> registry =RegistryBuilder.<ConnectionSocketFactory>create().register("http", plainsf).register("https", sslsf).build();
            cm = new PoolingHttpClientConnectionManager(registry);
            cm.setMaxTotal(httpClientEnhanceConfig.getMaxTotal());
            cm.setDefaultMaxPerRoute(httpClientEnhanceConfig.getDefaultMaxPerRoute());
            HashMap<String, Integer> dataMap = httpClientEnhanceConfig.getHostMaxPerRoute();
            if (null != dataMap && !dataMap.isEmpty()) {
                Iterator i$ = dataMap.entrySet().iterator();

                while(i$.hasNext()) {
                    Map.Entry<String, Integer> entry = (Map.Entry)i$.next();
                    HttpHost httpHost = new HttpHost((String)entry.getKey());
                    cm.setMaxPerRoute(new HttpRoute(httpHost), (Integer)entry.getValue());
                }
            }

//            HttpclientThreadPoolMonitor monitor = new HttpclientThreadPoolMonitor(cm);
//            monitor.startMonitor();
        }

    }

    public static CloseableHttpClient getHttpClient(int connectTimeout, int readTimeout) {
        CloseableHttpClient closeableHttpClient = null;
        if (httpClientEnhanceConfig.getIsUsePool()) {
            closeableHttpClient = getHttpClientByPool(connectTimeout, readTimeout);
        } else {
            closeableHttpClient = HttpClients.createDefault();
        }

        return closeableHttpClient;
    }

    public static CloseableHttpClient getHttpClientByPool(int connectTimeout, int readTimeout) {
        if (null == cm) {
            initPoolingHttpClientConnectionManager();
        }

        HttpRequestRetryHandler myRetryHandler = getCustomHttpRequestRetryHandler();
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(readTimeout).setConnectTimeout(connectTimeout).setConnectionRequestTimeout(2000).build();
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm).setDefaultRequestConfig(requestConfig).setRetryHandler(myRetryHandler).build();

        return httpClient;
    }

    public static List<NameValuePair> convertMapToNameValuePair(Map<String, Object> requestParams) {
        if (requestParams != null && !requestParams.isEmpty()) {
            List<NameValuePair> formParams = new ArrayList();
            Iterator i$ = requestParams.entrySet().iterator();

            while(i$.hasNext()) {
                Map.Entry<String, Object> entry = (Map.Entry)i$.next();
                if (null != entry.getValue()) {
                    formParams.add(new BasicNameValuePair((String)entry.getKey(), entry.getValue().toString()));
                } else {
                    formParams.add(new BasicNameValuePair((String)entry.getKey(), (String)null));
                }
            }

            return formParams;
        } else {
            return null;
        }
    }

    private static HttpRequestRetryHandler getCustomHttpRequestRetryHandler() {
        HttpRequestRetryHandler myRetryHandler = new HttpRequestRetryHandler() {
            public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
                if (executionCount > HttpClientUtils.httpClientEnhanceConfig.getRetryRequestCount()) {
                    return false;
                } else if (exception instanceof InterruptedIOException) {
                    return false;
                } else if (exception instanceof UnknownHostException) {
                    return false;
                } else if (exception instanceof ConnectTimeoutException) {
                    return false;
                } else if (exception instanceof SSLException) {
                    return false;
                } else if (exception instanceof NoHttpResponseException) {
                    HttpClientUtils.logger.error("http client retries again " + executionCount + " in NoHttpResponseException request");
                    return true;
                } else {
                    HttpClientContext clientContext = HttpClientContext.adapt(context);
                    HttpRequest request = clientContext.getRequest();
                    boolean idempotent = !(request instanceof HttpEntityEnclosingRequest);
                    return idempotent;
                }
            }
        };
        return myRetryHandler;
    }

    public static RequestConfig getRequestConfig(int connectTimeout, int readTimeout) {
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(readTimeout).setConnectTimeout(connectTimeout).setConnectionRequestTimeout(2000).build();
        return requestConfig;
    }

    public static Integer getConnectTimeout() {
        return httpClientEnhanceConfig.getConnectTimeout();
    }

    public static Integer getReadTimeout() {
        return httpClientEnhanceConfig.getReadTimeout();
    }

    public static boolean isUsePool() {
        return httpClientEnhanceConfig.getIsUsePool();
    }

    public static String getRequestContentEncoding() {
        return httpClientEnhanceConfig.getRequestContentEncoding();
    }


    public static Integer getLogChannel() {
        return httpClientEnhanceConfig.getLogChannel();
    }

    public static void setHttpClientEnhanceConfig(HttpClientEnhanceConfig httpClientEnhanceConfig) {
        HttpClientUtils.httpClientEnhanceConfig = httpClientEnhanceConfig;
    }
}
