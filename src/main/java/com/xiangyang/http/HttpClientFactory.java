package com.xiangyang.http;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;

public class HttpClientFactory {
    private static volatile Config config;

    private static volatile PoolingHttpClientConnectionManager poolingClientConnectionManager;


    private static volatile CloseableHttpClient closeableHttpClient;

    public static ScheduledExecutorService httpClientScheduledExecutorService;

    private static Object syncLock = new Object();

    public static CloseableHttpClient getHttpClient() {
        return getHttpClient(true);
    }

    public static CloseableHttpClient getHttpClient(boolean forceByPool) {
        if (poolingClientConnectionManager == null) {
            if (forceByPool) {
                synchronized (HttpClientFactory.class) {
                    if (poolingClientConnectionManager == null)
                        setGlobalConfigAndInitManager(Config.builder().build());
                }
                return getHttpClient(config);
            }
            return getHttpClient((Config)null);
        }
        return getHttpClient(config);
    }

    public static CloseableHttpClient getHttpClient(Config config) {
        if (config != HttpClientFactory.config)
            config = Config.builder().copyFromGlobalConfig().copyFromConfig(config).build();
        return getHttpClient(poolingClientConnectionManager, config);
    }

    public static CloseableHttpClient getHttpClient(PoolingHttpClientConnectionManager poolingClientConnectionManager, Config config) {
        if (config == null)
            config = Config.builder().build();
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(config.getReadTimeout()).setConnectTimeout(config.getConnectTimeout()).setConnectionRequestTimeout(HttpClientFactory.config.getConnectionRequestTimeout()).build();
        return getCloseableHttpClient(requestConfig);
    }

    private static CloseableHttpClient getCloseableHttpClient(RequestConfig requestConfig) {
        if (null == closeableHttpClient)
            synchronized (syncLock) {
                if (null == closeableHttpClient)
                    closeableHttpClient = HttpClients.custom().setConnectionManager((HttpClientConnectionManager)poolingClientConnectionManager).setDefaultRequestConfig(requestConfig).setRetryHandler(getCustomHttpRequestRetryHandler(config)).setConnectionManagerShared(true).build();
            }
        return closeableHttpClient;
    }

    public static void setGlobalConfigAndInitManager(Config config) {
        HttpClientFactory.config = config;
        initConnectionManager();
    }

    public static Config getGlobalConfig() {
        return config;
    }

    public static HttpRequestRetryHandler getCustomHttpRequestRetryHandler(final Config config) {
        return new HttpRequestRetryHandler() {
            public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
                if (executionCount > ((config == null) ? 3 : config.getRetryRequestCount()))
                    return false;
                if (exception instanceof java.io.InterruptedIOException)
                    return false;
                if (exception instanceof java.net.UnknownHostException)
                    return false;
                if (exception instanceof org.apache.http.conn.ConnectTimeoutException)
                    return false;
                if (exception instanceof javax.net.ssl.SSLException)
                    return false;
                if (exception instanceof org.apache.http.NoHttpResponseException)
                    return true;
                HttpClientContext clientContext = HttpClientContext.adapt(context);
                HttpRequest request = clientContext.getRequest();
                boolean idempotent = !(request instanceof org.apache.http.HttpEntityEnclosingRequest);
                if (idempotent)
                    return true;
                return false;
            }
        };
    }

    public static void destroyConnectionManager() {
        if (poolingClientConnectionManager != null)
            synchronized (HttpClientFactory.class) {
                if (poolingClientConnectionManager != null) {
                    poolingClientConnectionManager.close();
                    if (null != httpClientScheduledExecutorService)
                        httpClientScheduledExecutorService.shutdown();
//                    if (monitor != null) {
//                        monitor.stop();
//                        monitor = null;
//                    }
                    poolingClientConnectionManager = null;
                }
            }
    }

    public static void initConnectionManager() {
        synchronized (HttpClientFactory.class) {
            destroyConnectionManager();
            PlainConnectionSocketFactory plainConnectionSocketFactory = PlainConnectionSocketFactory.getSocketFactory();
            SSLConnectionSocketFactory sSLConnectionSocketFactory = SSLConnectionSocketFactory.getSocketFactory();
            Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create().register("http", plainConnectionSocketFactory).register("https", sSLConnectionSocketFactory).build();
            poolingClientConnectionManager = new PoolingHttpClientConnectionManager(registry);
            poolingClientConnectionManager.setMaxTotal(config.getMaxTotal());
            poolingClientConnectionManager.setDefaultMaxPerRoute(config.getDefaultMaxPerRoute());
            HashMap<String, Integer> dataMap = config.getHostMaxPerRoute();
            if (null != dataMap && !dataMap.isEmpty())
                for (Map.Entry<String, Integer> entry : dataMap.entrySet()) {
                    HttpHost httpHost = new HttpHost(entry.getKey());
                    poolingClientConnectionManager.setMaxPerRoute(new HttpRoute(httpHost), ((Integer)entry.getValue()).intValue());
                }
//            if (config.isEnableMonitor()) {
//                monitor = new HttpclientMonitor(config.isMonitorUseMetric(), poolingClientConnectionManager);
//                monitor.start();
//            }
            httpClientScheduledExecutorService.scheduleWithFixedDelay(new Runnable() {
                @Override
                public void run() {
                    HttpClientFactory.poolingClientConnectionManager.closeExpiredConnections();
                    HttpClientFactory.poolingClientConnectionManager.closeIdleConnections(20L, TimeUnit.SECONDS);
                }
            },30L, 60L, TimeUnit.SECONDS);

        }
    }

    public static class Config {
        public static final String DEFAULT_HTTP_INPUT_CHARSET = "UTF-8";

        public static final String DEFAULT_HTTP_CONTENT_JSON = "application/json";

        public static final String HTTP_CONTENT_FORM_DATA = "multipart/form-data";

        public static final int DEFAULT_CONNECT_TIMEOUT = 5000;

        public static final int DEFAULT_READ_TIMEOUT = 5000;

        public static final int DEFAULT_CONNECT_REQUEST_TIMEOUT = 2000;

        public static final int DEFAULT_HTTPCLIENT_MAX_TOTAL = 500;

        public static final int DEFAULT_HTTPCLIENT_MAX_PER_ROUTE = 50;

        public static final int DEFAULT_HTTP_REQUEST_MAX_RETRIES = 3;

        public static final boolean DEFAULT_ENABLE_MONITOR = true;

        public static final boolean DEFAULT_MONITOR_USE_METRIC = false;

        private int maxTotal;

        private int defaultMaxPerRoute;

        private int connectTimeout;

        private int readTimeout;

        private int connectionRequestTimeout;

        private HashMap<String, Integer> hostMaxPerRoute;

        private int retryRequestCount;

        private boolean enableMonitor;

        private boolean monitorUseMetric;

        private Config() {}

        public int getMaxTotal() {
            return this.maxTotal;
        }

        public int getDefaultMaxPerRoute() {
            return this.defaultMaxPerRoute;
        }

        public int getConnectTimeout() {
            return this.connectTimeout;
        }

        public int getReadTimeout() {
            return this.readTimeout;
        }

        public int getConnectionRequestTimeout() {
            return this.connectionRequestTimeout;
        }

        public HashMap<String, Integer> getHostMaxPerRoute() {
            return this.hostMaxPerRoute;
        }

        public int getRetryRequestCount() {
            return this.retryRequestCount;
        }

        public boolean isEnableMonitor() {
            return this.enableMonitor;
        }

        public boolean isMonitorUseMetric() {
            return this.monitorUseMetric;
        }

        public static Builder builder() {
            return new Builder();
        }

        public static final class Builder {
            private Integer maxTotal;

            private Integer defaultMaxPerRoute;

            private Integer connectTimeout;

            private Integer readTimeout;

            private Integer connectionRequestTimeout;

            private HashMap<String, Integer> hostMaxPerRoute;

            private Integer retryRequestCount;

            private Boolean enableMonitor;

            private Boolean monitorUseMetric;

            private Builder() {}

            public HttpClientFactory.Config build() {
                HttpClientFactory.Config config = new HttpClientFactory.Config();
                config.retryRequestCount = (this.retryRequestCount == null) ? 3 : this.retryRequestCount.intValue();
                config.connectTimeout = (this.connectTimeout == null) ? 5000 : this.connectTimeout.intValue();
                config.defaultMaxPerRoute = (this.defaultMaxPerRoute == null) ? 50 : this.defaultMaxPerRoute.intValue();
                config.readTimeout = (this.readTimeout == null) ? 5000 : this.readTimeout.intValue();
                config.connectionRequestTimeout = (this.connectionRequestTimeout == null) ? 2000 : this.connectionRequestTimeout.intValue();
                config.maxTotal = (this.maxTotal == null) ? 500 : this.maxTotal.intValue();
                config.hostMaxPerRoute = this.hostMaxPerRoute;
                config.enableMonitor = (this.enableMonitor == null) ? true : this.enableMonitor.booleanValue();
                config.monitorUseMetric = (this.monitorUseMetric == null) ? false : this.monitorUseMetric.booleanValue();
                return config;
            }

            public Builder copyFromGlobalConfig() {
                return copyFromConfig(HttpClientFactory.getGlobalConfig());
            }

            public Builder copyFromConfig(HttpClientFactory.Config config) {
                if (config != null) {
                    this.maxTotal = Integer.valueOf(config.getMaxTotal());
                    this.defaultMaxPerRoute = Integer.valueOf(config.getDefaultMaxPerRoute());
                    this.connectTimeout = Integer.valueOf(config.getConnectTimeout());
                    this.readTimeout = Integer.valueOf(config.getReadTimeout());
                    this.hostMaxPerRoute = config.getHostMaxPerRoute();
                    this.retryRequestCount = Integer.valueOf(config.getRetryRequestCount());
                    this.enableMonitor = Boolean.valueOf(config.isEnableMonitor());
                    this.monitorUseMetric = Boolean.valueOf(config.isMonitorUseMetric());
                }
                return this;
            }

            public Builder connectionRequestTimeout(Integer connectionRequestTimeout) {
                this.connectionRequestTimeout = connectionRequestTimeout;
                return this;
            }

            public Builder monitorUseMetric(Boolean monitorUseMetric) {
                this.monitorUseMetric = monitorUseMetric;
                return this;
            }

            public Builder enableMonitor(Boolean enableMonitor) {
                this.enableMonitor = enableMonitor;
                return this;
            }

            public Builder maxTotal(Integer maxTotal) {
                this.maxTotal = maxTotal;
                return this;
            }

            public Builder defaultMaxPerRoute(Integer defaultMaxPerRoute) {
                this.defaultMaxPerRoute = defaultMaxPerRoute;
                return this;
            }

            public Builder connectTimeout(Integer connectTimeout) {
                if (null == connectTimeout) {
                    this.connectTimeout = Integer.valueOf(HttpClientFactory.config.connectTimeout);
                } else {
                    this.connectTimeout = connectTimeout;
                }
                return this;
            }

            public Builder readTimeout(Integer readTimeout) {
                if (null == readTimeout) {
                    this.readTimeout = Integer.valueOf(HttpClientFactory.config.readTimeout);
                } else {
                    this.readTimeout = readTimeout;
                }
                return this;
            }

            public Builder hostMaxPerRoute(HashMap<String, Integer> hostMaxPerRoute) {
                this.hostMaxPerRoute = hostMaxPerRoute;
                return this;
            }

            public Builder retryRequestCount(Integer retryRequestCount) {
                this.retryRequestCount = retryRequestCount;
                return this;
            }
        }
    }

    public static final class Builder {
        private Integer maxTotal;

        private Integer defaultMaxPerRoute;

        private Integer connectTimeout;

        private Integer readTimeout;

        private Integer connectionRequestTimeout;

        private HashMap<String, Integer> hostMaxPerRoute;

        private Integer retryRequestCount;

        private Boolean enableMonitor;

        private Boolean monitorUseMetric;

        private Builder() {}

        public HttpClientFactory.Config build() {
            HttpClientFactory.Config config = new HttpClientFactory.Config();
            config.retryRequestCount = (this.retryRequestCount == null) ? 3 : this.retryRequestCount.intValue();
            config.connectTimeout = (this.connectTimeout == null) ? 5000 : this.connectTimeout.intValue();
            config.defaultMaxPerRoute = (this.defaultMaxPerRoute == null) ? 50 : this.defaultMaxPerRoute.intValue();
            config.readTimeout = (this.readTimeout == null) ? 5000 : this.readTimeout.intValue();
            config.connectionRequestTimeout = (this.connectionRequestTimeout == null) ? 2000 : this.connectionRequestTimeout.intValue();
            config.maxTotal = (this.maxTotal == null) ? 500 : this.maxTotal.intValue();
            config.hostMaxPerRoute = this.hostMaxPerRoute;
            config.enableMonitor = (this.enableMonitor == null) ? true : this.enableMonitor.booleanValue();
            config.monitorUseMetric = (this.monitorUseMetric == null) ? false : this.monitorUseMetric.booleanValue();
            return config;
        }

        public Builder copyFromGlobalConfig() {
            return copyFromConfig(HttpClientFactory.getGlobalConfig());
        }

        public Builder copyFromConfig(HttpClientFactory.Config config) {
            if (config != null) {
                this.maxTotal = Integer.valueOf(config.getMaxTotal());
                this.defaultMaxPerRoute = Integer.valueOf(config.getDefaultMaxPerRoute());
                this.connectTimeout = Integer.valueOf(config.getConnectTimeout());
                this.readTimeout = Integer.valueOf(config.getReadTimeout());
                this.hostMaxPerRoute = config.getHostMaxPerRoute();
                this.retryRequestCount = Integer.valueOf(config.getRetryRequestCount());
                this.enableMonitor = Boolean.valueOf(config.isEnableMonitor());
                this.monitorUseMetric = Boolean.valueOf(config.isMonitorUseMetric());
            }
            return this;
        }

        public Builder connectionRequestTimeout(Integer connectionRequestTimeout) {
            this.connectionRequestTimeout = connectionRequestTimeout;
            return this;
        }

        public Builder monitorUseMetric(Boolean monitorUseMetric) {
            this.monitorUseMetric = monitorUseMetric;
            return this;
        }

        public Builder enableMonitor(Boolean enableMonitor) {
            this.enableMonitor = enableMonitor;
            return this;
        }

        public Builder maxTotal(Integer maxTotal) {
            this.maxTotal = maxTotal;
            return this;
        }

        public Builder defaultMaxPerRoute(Integer defaultMaxPerRoute) {
            this.defaultMaxPerRoute = defaultMaxPerRoute;
            return this;
        }

        public Builder connectTimeout(Integer connectTimeout) {
            if (null == connectTimeout) {
                this.connectTimeout = Integer.valueOf(HttpClientFactory.config.connectTimeout);
            } else {
                this.connectTimeout = connectTimeout;
            }
            return this;
        }
        public Builder readTimeout(Integer readTimeout) {
            if (null == readTimeout) {
                this.readTimeout = Integer.valueOf(HttpClientFactory.config.readTimeout);
            } else {
                this.readTimeout = readTimeout;
            }
            return this;
        }

        public Builder hostMaxPerRoute(HashMap<String, Integer> hostMaxPerRoute) {
            this.hostMaxPerRoute = hostMaxPerRoute;
            return this;
        }

        public Builder retryRequestCount(Integer retryRequestCount) {
            this.retryRequestCount = retryRequestCount;
            return this;
        }
    }
}
