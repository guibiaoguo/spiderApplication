package com.spider.collection.util;

/**
 * Created by Administrator on 2015/12/10.
 */

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.util.EntityUtils;

import java.net.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author van
 *
 */
public class Router {
    private static final String USERNAME_PASSWORD = "admin:admin";
    public Router() {
        this.disconnect();
    }

    //http://192.168.3.1/do_cmd.htm?CMD=SYS_CONF&CCMD=0&nowait=1重启路由器
    private void disconnect() {
        String urlStr = "http://192.168.1.253";
        this.runCgi(urlStr, USERNAME_PASSWORD);
    }

    public static  String setSetting1() throws ClientProtocolException, IOException, URISyntaxException
    {
        DefaultHttpClient client = new DefaultHttpClient();
        client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, new HttpHost("i2.api.weibo.com"));
        String url =	"http://xxxxx/2/common/apps/set_settings.json";
        URI uri = new URI("http://xxxxxx/common/apps/set_settings.json");
        client.getCredentialsProvider().setCredentials( new AuthScope(uri.getHost(), uri.getPort()),  new UsernamePasswordCredentials("15810336966", "kobe1392936"));
        HttpPost post = new HttpPost(url);
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("source", "2975945008"));
        params.add(new BasicNameValuePair("item_id", "2"));
        params.add(new BasicNameValuePair("settings", "[{\"name\":\"sky\"}]"));

        HttpHost host = new HttpHost(uri.getHost(),uri.getPort(),"http");
        AuthCache authCache = new BasicAuthCache();
        // Generate BASIC scheme object and add it to the local auth cache
        BasicScheme basicAuth = new BasicScheme();
        authCache.put(host, basicAuth);

        // Add AuthCache to the execution context

//uri.getHost();
        BasicHttpContext localcontext = new BasicHttpContext();
        localcontext.setAttribute(ClientContext.AUTH_CACHE, authCache);
        post.setHeader("Content-Type", "application/x-www-form-urlencoded");

        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params,
                "UTF8");
        post.setEntity(entity);
// client.execute(post);
// HttpResponse response =client.execute(post);
        HttpResponse response = client.execute(host, post, localcontext);
//HttpResponse response =client.execute(post)
        //System.out.println(url);
        System.out.println("set  "+ EntityUtils.toString(response.getEntity()));

        return null;

    }

    private void runCgi(String urlStr, String authorizationStr) {
        URL xUrl = null;
        HttpURLConnection xHuc = null;
        try {
            xUrl = new URL(urlStr);
            if (xUrl != null) {
                xHuc = (HttpURLConnection) xUrl.openConnection();
                if (xHuc != null) {
                    if (!"".equals(authorizationStr)) {
                        xHuc.setRequestProperty("Authorization", "Basic "
                                + Base64.encodeBytes(USERNAME_PASSWORD
                                .getBytes()));
                    }
                    xHuc.setRequestProperty("Content-Length", "0");
                    xHuc.setRequestProperty("Content-Type",
                            "application/x-www-form-urlencoded");
                    xHuc.connect();
                    String aa = xHuc.getResponseMessage();
                    ////下面这些是用来获取IP地址的
                    InputStream in=xHuc.getInputStream();
                    int chint=0;
                    StringBuffer sb=new StringBuffer();
                    while((chint=in.read())!=-1){
                        sb.append((char)chint);
                    }
                    String html=sb.toString();
                    System.out.println(sb);
                    int first=html.indexOf("wanIP=\"");
                    String content=html.substring(459,471);
                    System.out.println(content);
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (xHuc != null) {
                xHuc.disconnect();
            }
        }
    }
    public static void main(String[] args) {
        new Router();
    }

}
