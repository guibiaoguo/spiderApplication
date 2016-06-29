package com.spider.collection.util;

import com.spider.collection.dao.JDBCHelper;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Administrator on 2015/10/22.
 */
public class TestUtil {

    private static BlockingQueue<Map> queue;

    private static JdbcTemplate jdbcTemplate;

    public TestUtil() {

    }

    public static void pushWhenNoDuplicate(Map map) {
        queue.add(map);
    }

    public static synchronized Map poll() {
        if (queue == null) {
            queue = new LinkedBlockingQueue();
            init();
        }
        Map map = queue.poll();
        System.out.println("剩余次数 " + getLeftRequestsCount());
        if (getLeftRequestsCount() == 0) {
            init();
        }
        return map;
    }

    public static void init() {
        jdbcTemplate = JDBCHelper.createMysqlTemplate("spider", "jdbc:mysql://10.1.20.31:3306/test?characterEncoding=utf-8", "root", "root", 1, 2);
        String sql = "SELECT * from proxy where status = 0 and run_status = 0 limit 0,1000 ";
        List<Map<String, Object>> results = jdbcTemplate.queryForList(sql);
        for (Map result : results) {
            pushWhenNoDuplicate(result);
            StringBuffer te = new StringBuffer().append("update ").append("proxy").append(" set run_status = 1 ").append(" where id = ? ");
            jdbcTemplate.update(te.toString(), result.get("id"));
        }
    }

    public static int getLeftRequestsCount() {
        return queue.size();
    }


    public static void main(String[] args) {
        JdbcTemplate jdbcTemplate = JDBCHelper.createMysqlTemplate("spider", "jdbc:mysql://10.1.20.31:3306/test?characterEncoding=utf-8", "root", "root", 1, 2);
        for (int i = 0; i < 100; i++) {
            MyRunable my1 = new MyRunable(i, jdbcTemplate);
            Thread thread1 = new Thread(my1);
            thread1.start();
        }
    }
}

class MyRunable implements Runnable {

    private int num;
    private JdbcTemplate jdbcTemplate;

    public MyRunable(int num, JdbcTemplate jdbcTemplate) {
        this.num = num;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run() {

        // 创建HttpClientBuilder
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        // HttpClient
        CloseableHttpClient closeableHttpClient = httpClientBuilder.build();


        Map result = TestUtil.poll();
        long sttime = 0;
        long dttime = 0;
        while (result != null) {
            try {
                System.out.println("访问 " + Thread.currentThread().getName() + ": " + result.get("hostname") + "-------" + result.get("port") + "--------" + result.get("id"));
                HttpHost proxy = new HttpHost(result.get("hostname").toString(), Integer.parseInt(result.get("port").toString()), "http");
                RequestConfig.Builder requestConfigBuilder = RequestConfig.custom().setConnectionRequestTimeout(15000).setSocketTimeout(15000).setConnectTimeout(15000).setCookieSpec(CookieSpecs.BEST_MATCH)
                        .setExpectContinueEnabled(true)
                        .setStaleConnectionCheckEnabled(true)
                        .setCircularRedirectsAllowed(true)
                        .setTargetPreferredAuthSchemes(Arrays.asList(AuthSchemes.NTLM, AuthSchemes.DIGEST))
                        .setProxyPreferredAuthSchemes(Arrays.asList(AuthSchemes.BASIC))
                        .setProxy(proxy);

                HttpGet httpGet = new HttpGet("https://www.baidu.com");
                httpGet.setConfig(requestConfigBuilder.build());
                Header[] headers = {new BasicHeader("userAgent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:35.0) Gecko/20100101 Firefox/35.0")};
                httpGet.setHeaders(headers);
                sttime = System.currentTimeMillis();
                CloseableHttpResponse response = closeableHttpClient.execute(httpGet);
                dttime = System.currentTimeMillis();
                if (response.getStatusLine().getStatusCode() == 200) {
                    System.out.println("成功访问--------------" + Thread.currentThread().getName() + ": " + result.get("hostname"));
                    System.out.println(EntityUtils.toString(response.getEntity()));
                    jdbcTemplate.update("update proxy set status = 1,time = ? where id = ? ", (dttime - sttime), result.get("id"));
                } else {
                    System.out.println("错误代码 —-------" + response.getStatusLine().getStatusCode());
                    jdbcTemplate.update("update proxy set status = 2,time = ? where id = ? ", (dttime - sttime), result.get("id"));
                }

            } catch (Exception e) {
                System.out.println(e.getMessage());
            } finally {
                if(dttime == 0 ) {
                    dttime = System.currentTimeMillis();
                    jdbcTemplate.update("update proxy set status = 3,time = ? where id = ? ", (dttime - sttime), result.get("id"));
                }
                System.out.println("所费时间-----------------------------" + (dttime - sttime));
                dttime = 0;
                sttime = 0;
                result = TestUtil.poll();
            }
        }
    }
}