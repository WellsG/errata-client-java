package com.example.client.errata;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.params.AuthPolicy;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.NegotiateSchemeFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

public class ErrataClientImpl implements ErrataClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(ErrataClientImpl.class);
    private static final String USERS = "user/";

    private String serverURL;
    private HttpClient httpclient;

    public static ErrataClient create(String url) {
        final ErrataClientImpl impl = new ErrataClientImpl(url);
        return impl;
    }

    public ErrataClientImpl(String serverURL) {
        LOGGER.info("Starting ERRATA client for server url: {}", serverURL);
        this.serverURL = serverURL;
    }

    public synchronized HttpClient client() {
        if (httpclient == null) {
            System.setProperty("sun.security.krb5.debug", "true");
            System.setProperty("jsse.enableSNIExtension", "false");
            System.setProperty("javax.security.auth.useSubjectCredsOnly", "false");
            httpclient = wrapClient(new DefaultHttpClient());
        }
        return httpclient;
    }

    private String execute(String url, Map<String, String> params) throws Exception {
        StringBuffer urls = new StringBuffer();
        urls.append(serverURL).append(url);
        if (params != null) {
            final List<NameValuePair> qparams = new ArrayList<NameValuePair>();
            if (params != null) {
                for (Map.Entry<String, String> param : params.entrySet()) {
                    qparams.add(new BasicNameValuePair(param.getKey(), param.getValue()));
                }
            }
            urls.append("?").append(URLEncodedUtils.format(qparams, "utf-8"));
        }
        LOGGER.info("Excute method: {}", urls.toString());
        HttpUriRequest request = new HttpGet(urls.toString());
        request.setHeader("Content-Type", "application/json");
        request.setHeader("Accept", "application/json");
        try {
            HttpResponse response = client().execute(request);
            return parseResponse(response);
        } finally {
            request.abort();
        }
    }

    private String executePost(String url, Map<String, Object> params) throws Exception {
        StringBuffer urls = new StringBuffer();
        urls.append(serverURL).append(url);
        String jsonParams = new Gson().toJson(params);
        LOGGER.info("Excute method: {}", urls.toString());
        LOGGER.info("Params: {}", jsonParams);
        HttpPost request = new HttpPost(urls.toString());
        request.setHeader("Content-Type", "application/json");
        request.setHeader("Accept", "application/json");
        StringEntity entity = new StringEntity(jsonParams);
        entity.setContentType("application/json");
        request.setEntity(entity);
        try {
            HttpResponse response = client().execute(request);
            return parseResponse(response);
        } finally {
            request.abort();
        }
    }

    private String executePut(String url, Map<String, Object> params) throws Exception {
        StringBuffer urls = new StringBuffer();
        urls.append(serverURL).append(url);
        String jsonParams = new Gson().toJson(params);
        LOGGER.info("Excute method: {}", urls.toString());
        LOGGER.info("Params: {}", jsonParams);
        HttpPut request = new HttpPut(urls.toString());
        request.setHeader("Content-Type", "application/json");
        request.setHeader("Accept", "application/json");
        StringEntity entity = new StringEntity(jsonParams);
        entity.setContentType("application/json");
        request.setEntity(entity);
        try {
            HttpResponse response = client().execute(request);
            return parseResponse(response);
        } finally {
            request.abort();
        }
    }

    private String parseResponse(HttpResponse response) throws Exception {
        StringBuffer sb = new StringBuffer();
        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        String line = "";
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        if (sb.toString().contains("errors") || sb.toString().contains("error")) {
            LOGGER.error("{}", sb.toString());
            throw new Exception("http response code error: " + response.getStatusLine().getStatusCode() + "  " + sb.toString());
        }
        return sb.toString();
    }

    public static org.apache.http.client.HttpClient wrapClient(org.apache.http.client.HttpClient base) {
        try {
            SSLContext ctx = SSLContext.getInstance("TLS");
            X509TrustManager tm = new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType)
                        throws java.security.cert.CertificateException {
                }

                public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType)
                        throws java.security.cert.CertificateException {
                }
            };
            X509HostnameVerifier hostnameVerifier = new X509HostnameVerifier() {
                public void verify(String host, SSLSocket ssl) throws IOException {
                }

                public void verify(String host, String[] cns, String[] subjectAlts) throws SSLException {
                }

                public boolean verify(String arg0, SSLSession arg1) {
                    return true;
                }

                public void verify(String host, java.security.cert.X509Certificate cert) throws SSLException {
                }
            };
            ctx.init(null, new TrustManager[] { tm }, null);
            SSLSocketFactory ssf = new SSLSocketFactory(ctx, hostnameVerifier);
            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("https", 443, ssf));
            registry.register(new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));
            ThreadSafeClientConnManager mgr = new ThreadSafeClientConnManager(registry);

            NegotiateSchemeFactory nsf = new NegotiateSchemeFactory();
            DefaultHttpClient httpclient = new DefaultHttpClient(mgr, base.getParams());
            httpclient.getAuthSchemes().register(AuthPolicy.SPNEGO, nsf);
            Credentials credential = new Credentials() {
                public String getPassword() {
                    return null;
                }

                public Principal getUserPrincipal() {
                    return null;
                }
            };
            httpclient.getCredentialsProvider().setCredentials(new AuthScope(null, -1, null), credential);
            return httpclient;
        } catch (Exception ex) {
            LOGGER.error("Wrap httpclient error.", ex);
            return null;
        }
    }

    public ErrataUser createUser(ErrataUser user) throws Exception {
        Map<String, Object> params = ErrataMapper.getInstance().mapMap(user);
        String response = executePost("user", params);
        return parseErrataUser(response);
    }

    public ErrataUser updateUser(String loginName, ErrataUser user) throws Exception {
        Map<String, Object> params = ErrataMapper.getInstance().mapMap(user);
        String response = executePut(USERS + loginName, params);
        return parseErrataUser(response);
    }

    public ErrataUser getUser(String loginName) throws Exception {
        String response = execute(USERS + loginName, null);
        return parseErrataUser(response);
    }

    @SuppressWarnings("unchecked")
    private ErrataUser parseErrataUser(String response) {
        ErrataUser errataUser = null;
        if (response != null && !response.equals("")) {
            try {
                Map<String, Object> userMap = new Gson().fromJson(response, Map.class);
                errataUser = ErrataMapper.getInstance().mapErrataUser(userMap);
            } catch (Exception e) {
                LOGGER.error("", e);
            }
        }
        return errataUser;
    }
}
