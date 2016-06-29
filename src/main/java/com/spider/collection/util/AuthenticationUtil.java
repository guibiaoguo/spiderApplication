package com.spider.collection.util;

import com.spider.collection.entity.Captcha;
import com.spider.collection.entity.JobDTO;
import com.spider.collection.entity.TasksDTO;
import com.spider.collection.entity.Token;
import com.spider.collection.dao.JDBCHelper;
import org.apache.commons.lang.StringUtils;
import org.apache.http.*;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.AuthState;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.*;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Json;
import us.codecraft.webmagic.selector.Selectable;

import javax.imageio.ImageIO;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2015/11/9.
 */
public class AuthenticationUtil {

    private static CloseableHttpClient httpClient;
    private static List<Header> headers;
    private static CloseableHttpResponse response;
    private static RequestConfig requestConfig;
    private static BlockingQueue<Map> queue = null;
    private static JdbcTemplate jdbcTemplate;

    public static void main(String[] args) throws Exception {
        /*
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        AuthenticationUtil authenticationUtil = new AuthenticationUtil(httpClient);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        Job job = objectMapper.readValue(authenticationUtil.getText("http://localhost:9090/1688.json"), Job.class);
//        Job job = objectMapper.readValue(authenticationUtil.getText("http://localhost:9090/renren.json"), Job.class);
//        Job job = objectMapper.readValue(authenticationUtil.getText("http://localhost:9090/baidu.json"), Job.class);
//        Job job = objectMapper.readValue(authenticationUtil.getText("http://localhost:9090/qichacha.json"), Job.class);
//        authenticationUtil.login(job);
//        authenticationUtil.getText("http://me.1688.com/info/czcfllyb.html");
//        authenticationUtil.getText("http://blog.renren.com/blog/304317577/449470467");
//        authenticationUtil.getText("http://pan.baidu.com/disk/home");

//        authenticationUtil.getText("http://qichacha.com/search?key=%E4%B8%8A%E6%B5%B7%E4%BF%A1%E4%BB%A5%E8%81%94%E7%8F%A0%E5%AE%9D%E9%A6%96%E9%A5%B0%E6%9C%89%E9%99%90%E5%85%AC%E5%8F%B8%20&index=name");
        System.out.println("==========================================================================");
//        authenticationUtil.getText("http://qichacha.com/search?key=%E4%B8%8A%E6%B5%B7%E4%B9%9D%E7%94%B3%E6%A8%A1%E5%85%B7%E7%A7%91%E6%8A%80%E6%9C%89%E9%99%90%E5%85%AC%E5%8F%B8&index=name");
        Map map = new HashedMap();
//        map.put("key","TABLE_");
//        map.put("index","name");
//        map.put("c","TABLE_");
        Request request = new Request();
//        authenticationUtil.getQueque(job.getTasks());
        while(authenticationUtil.getLeftRequestsCount()>1)
//            authenticationUtil.getText(authenticationUtil.getQueryData(map,new StringBuffer().append("http://www.qichacha.com/search?"),job.getTasks().getSourceTable()));
            authenticationUtil.getText(authenticationUtil.getQueryData(map,request,job.getTasks()).getUrl());
            */
//        AuthenticationUtil au = new AuthenticationUtil();
//        Map postData = new HashMap();
//        postData.put("password","B0CBBA63545CB0F4272B37FE7A415B58");
//        postData.put("id","Admin");
//        au.postText("http://192.168.0.1/authentication.cgi",postData,null);
//        au.dlinkLogin();
//        au.saveJpsg("http://gsxt.hljaic.gov.cn/validateCode.jspx?type=0&id=0.16758955037221313","d:/hljaic/",2000);
//        au.saveJpsg("http://www.sgs.gov.cn/notice/captcha?preset=&ra=0.6079523067455739","d:/sgs/",2000);
//        au.saveJpsg("http://www.jsgsj.gov.cn:58888/province/rand_img.jsp?type=7&temp=Fri%20Dec%2018%202015%2017:41:05%20GMT+0800%20(%E4%B8%AD%E5%9B%BD%E6%A0%87%E5%87%86%E6%97%B6%E9%97%B4)","d:/jsgsj/",2000);
//        au.saveJpsg("http://www.ahcredit.gov.cn/validateCode.jspx?type=1&id=0.09416846907697618","d:/ahcredit/",2000);
//        au.saveJpsg("http://wsgs.fjaic.gov.cn/creditpub/captcha?preset=str-01,math-01&ra=0.5249313379172236","d:/wsgs/",2000);
//        au.saveJpsg("http://gsxt.jxaic.gov.cn/ECPS/verificationCode.jsp?_=1450431902852","d:/jxaic/",2000);
//        au.saveJpsg("http://218.57.139.24/securitycode?0.3032776340842247","d:/218.57.139.24/",2000);
//        au.saveJpsg("http://gsxt.gdgs.gov.cn/aiccips/verify.html?random=0.15633139247074723","d:/gdgs/",2000);
//        au.saveJpsg("http://gxqyxygs.gov.cn/validateCode.jspx?type=2&id=0.5904668604489416","d:/gxqyxygs/",2000);
//        au.saveJpsg("http://222.143.24.157/validateCode.jspx?type=2&id=0.999803900020197","d:/222.143.24.157/",2000);
//        au.saveJpsg("http://xyjg.egs.gov.cn/ECPS_HB/validateCode.jspx?type=1&_=1450433545129?type=1&_=1450433554980?type=1&_=1450433591484?type=1&_=1450433592245","d:/xyjg/",2000);
//        au.saveJpsg("http://gsxt.hnaic.gov.cn/notice/captcha?preset=&ra=0.8572206743992865","d:/hnaic/",2000);
//        au.saveJpsg("http://gsxt.cqgs.gov.cn/sc.action?width=130&height=40&fs=23&t=1450433713433","d:/cqgs/",2000);
//        au.saveJpsg("http://gsxt.scaic.gov.cn/ztxy.do?method=createYzm&dt=1450433767801&random=1450433767801","d:/scaic/",2000);
//        au.saveJpsg("http://gsxt.gzgs.gov.cn/search!generateCode.shtml?validTag=searchImageCode&1450433826399","d:/gzgs/",2000);
//        au.saveJpsg("http://gsxt.ynaic.gov.cn/notice/captcha?preset=&ra=0.12346981931477785","d:/ynaic/",2000);
//        au.saveJpsg("http://gsxt.xzaic.gov.cn/validateCode.jspx?type=0&id=0.05147376190871","d:/xzaic/",2000);
//        au.saveJpsg("http://117.22.252.219:8002/ztxy.do?method=createYzm&dt=1450433960637&random=1450433960637","d:/117.22.252.219/",2000);
//        au.saveJpsg("http://xygs.gsaic.gov.cn/gsxygs/securitycode.jpg?v=1450434023749","d:/gsaic/",2000);
//        au.saveJpsg("http://218.95.241.36/validateCode.jspx?type=0&id=0.3270889688283205","d:/218.95.241.36/",2000);
//        au.saveJpsg("http://gsxt.ngsh.gov.cn/ECPS/verificationCode.jsp?_=1450434181107","d:/ngsh/",2000);
//        au.saveJpsg("http://gsxt.xjaic.gov.cn:7001/ztxy.do?method=createYzm&dt=1450434216551&random=1450434216551","d:/xjaic/",2000);
//        au.saveJpsg("http://zhaoren.idtag.cn/samename/searchName!validateM.htm?m=0.6257675066590309", "d:/2345/", 200);
//        au.getText("http://zhaoren.idtag.cn/samename/searchName!validateM.htm?m=0.6257675066590309");
//        JobDTO jobDTO = new JobDTO();
//        Captcha captcha = new Captcha();
//        captcha.setFlag(true);
//        captcha.setCapUrl("http://qichacha.com/user_login");
//        captcha.setCodeUrl("//*[@id=\\\"imgcode\\\"]/@src;xpath;http://qichacha.com/");
//        captcha.setCode("verifyLogin");
//        captcha.setRightCode("\\\"success\\\":true;regex");
//        jobDTO.setCaptcha(captcha);
//        jobDTO.setIsAuthentication(true);
//        jobDTO.setLoginUrl("http://qichacha.com/global_user_loginaction");
//        Map login = new HashMap();
//        login.put("name","15626241465");
//        login.put("pswd","123464");
//        jobDTO.setLogin(login);
//        identifyByTesseract("http://qichacha.com/index_validateCode?verifyName=verifyLogin",httpClient);
//        headers.add(new BasicHeader("Host","bww.1024yakexi.com"));
//        for (int i = 0; i < 50; i++) {
//            String content = au.getText("http://bww.1024yakexi.com/pw/thread.php?fid=" + i);
//            Boolean flag = getCheckCode(content,"春节邀请码</a>;regex",new StringBuffer());
//            if(!flag) {
//                System.out.println("****************");
//                System.out.println(content);
//                System.out.println("*****************");
//            }
//        }
        CloseableHttpClient httpClient = AuthenticationUtil.createSSLInsecureClient();
        AuthenticationUtil authenticationUtil = new AuthenticationUtil(httpClient);
        authenticationUtil.getText("https://www.qilerongrong.com.cn/invest/?st=&loantype=2&pageNo=2");
    }

    public static CloseableHttpClient createSSLInsecureClient() {
        try {
            SSLContext sslContext = new SSLContextBuilder()
                    .loadTrustMaterial(null, new TrustStrategy() {
                        //信任所有
                        public boolean isTrusted(X509Certificate[] chain,
                                                 String authType) throws CertificateException {
                            return true;
                        }
                    }).build();
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                    sslContext);
            return HttpClients.custom().setSSLSocketFactory(sslsf).build();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        return  HttpClients.createDefault();
    }

    public void close() {
        try {
            httpClient.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getText(String redirectLocation) throws Exception {
        String content = null;
        try {
            HttpGet httpget = new HttpGet(redirectLocation);
            httpget.setHeaders(headers.toArray(new Header[headers.size()]));
            httpget.setConfig(requestConfig);
            response = httpClient.execute(httpget);
            printHeader(response.getAllHeaders());
            content = EntityUtils.toString(response.getEntity(), "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
            content = null;
        }
        System.out.println(content);
        return content;
    }

    public static void getQueque(TasksDTO tasks, Object id) {
        queue = new LinkedBlockingQueue();
        try {
            if (jdbcTemplate == null)
                jdbcTemplate = JDBCHelper.createMysqlTemplate("spider", "jdbc:mysql://10.1.20.31:3306/test?characterEncoding=utf-8", "root", "root", 1, 2);
            StringBuffer buffer = new StringBuffer().append("SELECT id,url from ").append(tasks.getSourceTable()).append(" where status=0 ");
            if (id != null)
                buffer.append(" and id != '").append(id).append("'");
            buffer.append(" LIMIT 0,5");
            List<Map<String, Object>> results = jdbcTemplate.queryForList(buffer.toString());
            for (Map<String, Object> result : results) {
//                pushWhenNoDuplicate(URLEncoder.encode(StringUtils.trim(result.get("cname").toString()), "utf-8"));
                pushWhenNoDuplicate(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean update(TasksDTO tasks, String id) {
        try {
            jdbcTemplate = JDBCHelper.createMysqlTemplate("spider", "jdbc:mysql://10.1.20.31:3306/test?characterEncoding=utf-8", "root", "root", 1, 2);
            jdbcTemplate.update("update " + tasks.getSourceTable() + " set status = 1 where id = ? ", id);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static Request getQueryData(Map<String, Object> postData, Request request, TasksDTO tasks) {
        if (queue == null) {
            getQueque(tasks, null);
        }
        Map map = poll();
        System.out.println("=================" + getLeftRequestsCount());
        Object id = null;
        Random random = new Random();
        while (map == null) {
            try {
                Thread.sleep(300000 * (random.nextInt(10) + 1));
                getQueque(tasks, null);
                map = poll();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        id = map.get("id");
        if (getLeftRequestsCount() == 0) {
            try {
                getQueque(tasks, id);
                Thread.sleep(300000 * (random.nextInt(10) + 1));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        request.putExtra("id", id);
        request.putExtra("$update", "0");
        if (postData == null || postData.size() == 0) {
            request.setUrl(map.get("url").toString());
            return request;
        }
        StringBuffer buffer = new StringBuffer();
        for (Map.Entry<String, Object> entry : postData.entrySet()) {
            if (entry.getValue() != null && StringUtils.startsWith(entry.getValue().toString(), "TABLE_")) {
                buffer.append(entry.getKey()).append("=").append(map.get(tasks.getTargetTaskDTOs().get(0).getKey())).append("&");
            } else {
                buffer.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
        }
        request.setUrl(buffer.toString());
        return request;
    }

    public static void pushWhenNoDuplicate(Map map) {
        queue.add(map);
    }

    public static synchronized Map poll() {
        return queue.poll();
    }

    public static int getLeftRequestsCount() {
        return queue.size();
    }

    public String postText(String redirectLocation, Map postData, Request request) throws Exception {
        String content = "";
        HttpPost httpost = new HttpPost(redirectLocation);
        httpost.setHeaders(headers.toArray(new Header[headers.size()]));
        httpost.setConfig(requestConfig);
        List<NameValuePair> nvps = getPostData(postData, request);
        httpost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
        response = httpClient.execute(httpost);
        printHeader(response.getAllHeaders());
        content = EntityUtils.toString(response.getEntity(), "UTF-8");
        System.out.println(content);
        return content;
    }

    public CloseableHttpClient getHttpClient() {
        return httpClient;
    }

    public void setHttpClient(CloseableHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public void init() {
        RequestConfig defaultRequestConfig = RequestConfig.custom()
                .setCookieSpec(CookieSpecs.BEST_MATCH)
                .setExpectContinueEnabled(true)
                .setStaleConnectionCheckEnabled(true)
                .setTargetPreferredAuthSchemes(Arrays.asList(AuthSchemes.NTLM, AuthSchemes.DIGEST))
                .setProxyPreferredAuthSchemes(Arrays.asList(AuthSchemes.BASIC))
                .build();
        requestConfig = RequestConfig.copy(defaultRequestConfig)
                .setSocketTimeout(50000)
                .setConnectTimeout(50000)
                .setConnectionRequestTimeout(50000)
                .setCircularRedirectsAllowed(true)
                .build();
        headers = new ArrayList<>();
//        headers.add(new BasicHeader("User-Agent", "User-Agent: Mozilla/5.0 (Windows NT 10.0; WOW64; rv:42.0) Gecko/20100101 Firefox/42.0"));
        jdbcTemplate = JDBCHelper.createMysqlTemplate("spider", "jdbc:mysql://10.1.20.31:3306/test?characterEncoding=utf-8", "root", "root", 1, 2);
    }

    public AuthenticationUtil() {
        init();
        HttpRequestRetryHandler myRetryHandler = new HttpRequestRetryHandler() {

            public boolean retryRequest(IOException exception, int executionCount, HttpContext httpContext) {
                if (executionCount >= 5) {
                    // 如果已经重试了5次，就放弃
                    return false;
                }
                if (exception instanceof InterruptedIOException) {
                    // 超时
                    return false;
                }
                if (exception instanceof UnknownHostException) {
                    // 目标服务器不可达
                    return false;
                }
                if (exception instanceof ConnectTimeoutException) {
                    // 连接被拒绝
                    return false;
                }
                if (exception instanceof SSLException) {
                    // ssl握手异常
                    return false;
                }
                HttpClientContext clientContext = HttpClientContext.adapt(httpContext);
                HttpRequest request = clientContext.getRequest();
                boolean idempotent = !(request instanceof HttpEntityEnclosingRequest);
                if (idempotent) {
                    return true;
                }
                return false;
            }
        };
        httpClient = HttpClientBuilder.create().setRetryHandler(myRetryHandler).build();
    }

    public AuthenticationUtil(CloseableHttpClient httpClient) {
        init();
        this.httpClient = httpClient;
    }

    public static boolean getCheckCode(String content, String regex, StringBuffer img) {
        String[] parames = StringUtils.split(regex, ";");
        if (parames.length > 2) {
            img.append(parames[2]);
        }
        if (parames.length > 1 && StringUtils.equalsIgnoreCase(parames[1], "regex")) {
            Pattern pattern = Pattern.compile(parames[0]);
            Matcher matcher = pattern.matcher(content);
            if (matcher.find()) {
                for (int i = 1; i <= matcher.groupCount(); i++) {
                    if (StringUtils.isNotEmpty(matcher.group(i))) {
                        img.append(matcher.group(i));
                        break;
                    }
                }
                return false;
            }
        } else if (parames.length > 1 && StringUtils.equalsIgnoreCase(parames[1], "xpath")) {
            Html html = new Html(content);
            Selectable selectable = html.xpath(parames[0]);
            String temp = selectable.get();
//            if (StringUtils.isEmpty(temp)) {
//                return true;
//            } else if (parames.length >= 2) {
//                img.append(temp);
//                return false;
//            } else if (!StringUtils.equalsIgnoreCase(temp, parames[2]))
//                return true;
            return checkPage(temp,parames,img);
        }else if (parames.length > 1 && StringUtils.equalsIgnoreCase(parames[1], "css")) {
            Html html = new Html(content);
            Selectable selectable = html.css(parames[0]);
            String temp = selectable.get();
//            if (StringUtils.isEmpty(temp)) {
//                return true;
//            } else if (parames.length >= 2) {
//                img.append(temp);
//                return false;
//            } else if (!StringUtils.equalsIgnoreCase(temp, parames[2]))
//                return true;
            return checkPage(temp,parames,img);
        } else if (parames.length > 1 && StringUtils.equalsIgnoreCase(parames[1], "jsonPath") && StringUtils.isNotBlank(StringUtils.trim(content)) && !StringUtils.equalsIgnoreCase(StringUtils.trim(content),"null")) {
//            System.out.println("****************************** " + content + " ***************************");
            Json json = new Json(content);
            String temp = json.jsonPath(parames[0]).toString();
//            if (StringUtils.isEmpty(temp)) {
//                return true;
//            } else if (parames.length >= 2) {
//                img.append(temp);
//                return false;
//            } else if (!StringUtils.equalsIgnoreCase(temp, parames[2]))
//                return true;
            return checkPage(temp,parames,img);
        }

        return true;
    }

    private static boolean checkPage( String temp,String[] parames,StringBuffer img ) {
        if (StringUtils.isEmpty(temp)) {
            return true;
        } else if (parames.length >= 2) {
            img.append(temp);
            return false;
        } else if (!StringUtils.equalsIgnoreCase(temp, parames[2]))
            return true;
        return true;
    }

    private static final String[][] utmDictionaries=
            {{"6", "b", "t", "f", "l", "5", "w", "h", "q", "i", "s", "e", "c", "p", "m", "u", "9", "8", "y", "2", "z", "k", "j", "r", "x", "n", "-", "0", "3", "4", "d", "1", "a", "o", "7", "v", "g"},
                    {"1", "8", "o", "s", "z", "m", "b", "9", "f", "d", "7", "h", "c", "u", "n", "v", "p", "y", "2", "0", "3", "j", "-", "i", "l", "k", "t", "q", "4", "6", "r", "a", "w", "5", "e", "x", "g"},
                    {"g", "a", "c", "t", "h", "u", "p", "f", "6", "x", "7", "0", "d", "i", "v", "e", "q", "4", "b", "5", "k", "w", "9", "s", "-", "j", "l", "y", "3", "o", "n", "z", "m", "2", "1", "r", "8"},
                    {"s", "6", "h", "0", "y", "l", "d", "x", "e", "a", "k", "z", "u", "f", "4", "r", "b", "-", "p", "g", "3", "n", "m", "7", "o", "c", "i", "8", "v", "2", "1", "9", "q", "w", "t", "j", "5"},
                    {"d", "4", "9", "m", "o", "i", "5", "k", "q", "n", "c", "s", "6", "b", "j", "y", "x", "l", "a", "v", "3", "t", "u", "h", "-", "r", "z", "2", "0", "7", "g", "p", "8", "f", "1", "w", "e"},
                    {"z", "5", "g", "c", "h", "7", "o", "t", "2", "k", "a", "-", "e", "x", "y", "j", "3", "l", "1", "u", "s", "4", "b", "n", "8", "i", "6", "q", "p", "0", "d", "r", "v", "m", "w", "f", "9"},
                    {"p", "x", "3", "d", "6", "5", "8", "k", "t", "l", "z", "b", "4", "n", "r", "v", "y", "m", "g", "a", "0", "1", "c", "9", "-", "2", "7", "q", "j", "h", "e", "w", "u", "s", "f", "o", "i"},
                    {"q", "-", "u", "d", "k", "7", "t", "z", "4", "8", "x", "f", "v", "w", "p", "2", "e", "9", "o", "m", "5", "g", "1", "j", "i", "n", "6", "3", "r", "l", "b", "h", "y", "c", "a", "s", "0"},
                    {"7", "-", "g", "x", "6", "5", "n", "u", "q", "z", "w", "t", "m", "0", "h", "o", "y", "p", "i", "f", "k", "s", "9", "l", "r", "1", "2", "v", "4", "e", "8", "c", "b", "a", "d", "j", "3"},
                    {"1", "t", "8", "z", "o", "f", "l", "5", "2", "y", "q", "9", "p", "g", "r", "x", "e", "s", "d", "4", "n", "b", "u", "a", "m", "c", "h", "j", "3", "v", "i", "0", "-", "w", "7", "k", "6"}};


    public static String convertToUtm(String keyword,String utmCode) {
        String keyCode = String.valueOf(Character.codePointAt(keyword, 0));
        int r = keyCode.length() > 1 ? Integer.parseInt(keyCode.substring(1, 2)) : Integer.parseInt(keyCode);

        String[] utmMap = utmDictionaries[r];

        StringBuilder builder = new StringBuilder();
        String[] utmCodeArray = utmCode.split(",");
        for (int i = 0; i < utmCodeArray.length; i++) {
            int code = Integer.parseInt(utmCodeArray[i]);
            builder.append(utmMap[code]);
        }
        return builder.toString();
    }

    public static String unicodeCodeArrayToString(String[] tokenCodeArray) {
        StringBuilder builder = new StringBuilder(tokenCodeArray.length);
        for (String code : tokenCodeArray) {
            if(StringUtils.isNotEmpty(code)) {
                int codePoint = Integer.parseInt(code);
                builder.append(Character.toChars(codePoint));
            }
        }
        return builder.toString();
    }

    public void saveJpsg(String url, String path, int time) {
        try {
            //Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + img);
            File destF = new File(path);
            if (!destF.exists()) {
                destF.mkdirs();
            }
            for (int i = 0; i < 1000; i++) {

                HttpGet httpget = new HttpGet(url);
                headers.add(new BasicHeader("Accept", "image/webp,image/*,*/*;q=0.8"));
                httpget.setHeaders(headers.toArray(new Header[headers.size()]));
                httpget.setConfig(requestConfig);
                response = httpClient.execute(httpget);
                File storeFile = new File(path + "/" + i + ".jpg");
                FileOutputStream output = new FileOutputStream(storeFile);

// 得到网络资源的字节数组,并写入文件
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    InputStream instream = entity.getContent();

                    byte b[] = new byte[1024];
                    int j = 0;
                    while ((j = instream.read(b)) != -1) {
                        output.write(b, 0, j);
                    }
                    output.flush();
                    output.close();
                }
                Thread.sleep(time);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String saveExe(String url, String name) {
        try {
            //Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + img);
            String path = System.getProperty("user.dir");
            File destF = new File(path);
            if (!destF.exists()) {
                destF.mkdirs();
            }

            HttpGet httpget = new HttpGet(url);
            headers.add(new BasicHeader("Accept", "image/webp,image/*,*/*;q=0.8"));
            httpget.setHeaders(headers.toArray(new Header[headers.size()]));
            httpget.setConfig(requestConfig);
            response = httpClient.execute(httpget);
            File storeFile = new File(path + "/" + name);
            FileOutputStream output = new FileOutputStream(storeFile);
// 得到网络资源的字节数组,并写入文件
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream instream = entity.getContent();

                byte b[] = new byte[1024];
                int j = 0;
                while ((j = instream.read(b)) != -1) {
                    output.write(b, 0, j);
                }
                output.flush();
                output.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return "success";
    }

    public static String identify(String img) {
        String code = null;
        try {
            if (StringUtils.isNotEmpty(img)) {
                //Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + img);
                HttpGet httpget = new HttpGet(img);
                httpget.setHeaders(headers.toArray(new Header[headers.size()]));
                httpget.setConfig(requestConfig);
                response = httpClient.execute(httpget);
                File storeFile = new File("d:/1.jpg");
                FileOutputStream output = new FileOutputStream(storeFile);

// 得到网络资源的字节数组,并写入文件
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    InputStream instream = entity.getContent();

                    byte b[] = new byte[1024];
                    int j = 0;
                    while ((j = instream.read(b)) != -1) {
                        output.write(b, 0, j);
                    }
                    output.flush();
                    output.close();
                }
                Scanner scanner = new Scanner(System.in);
                code = scanner.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return code;
    }

    public static String identifyByTesseract(String img, CloseableHttpClient httpclient) {
        String code = null;
        try {
            if (StringUtils.isNotEmpty(img)) {
                //Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + img);
                HttpGet httpget = new HttpGet(img);
                headers.add(new BasicHeader("Accept", "image/png,image/*;q=0.8,*/*;q=0.5"));
                httpget.setHeaders(headers.toArray(new Header[headers.size()]));
                httpget.setConfig(requestConfig);
                response = httpclient.execute(httpget);
                File storeFile = new File("d:/testdata/1.jpg");
                FileOutputStream output = new FileOutputStream(storeFile);

// 得到网络资源的字节数组,并写入文件
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    InputStream instream = entity.getContent();

                    byte b[] = new byte[1024];
                    int j = 0;
                    while ((j = instream.read(b)) != -1) {
                        output.write(b, 0, j);
                    }
                    output.flush();
                    output.close();
                }

                code = tesseract(storeFile);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return code;
    }

    public static String tesseract(File storeFile) {

        String code = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            HttpPost httppost = new HttpPost("http://localhost:8080/spider/getcaptcha/zh");
            FileBody bin = new FileBody(storeFile);
            StringBody comment = new StringBody("A binary file of some kind", ContentType.APPLICATION_FORM_URLENCODED);
            HttpEntity reqEntity = MultipartEntityBuilder.create().addPart("captcha", bin).build();
            httppost.setEntity(reqEntity);
            HttpResponse response = httpClient.execute(httppost);
            Map map = objectMapper.readValue(EntityUtils.toString(response.getEntity()), Map.class);
            if (StringUtils.equalsIgnoreCase(map.get("success").toString(), "true")) {
                code = map.get("code").toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return code;
    }

    public String postDataMsg(String message, String url) throws Exception {

        String code = null;

        ObjectMapper objectMapper = new ObjectMapper();
        HttpPost httppost = new HttpPost(url);
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        builder.addTextBody("content", message, ContentType.DEFAULT_BINARY);
        HttpEntity entity = builder.build();
        httppost.setEntity(entity);
        HttpResponse response = httpClient.execute(httppost);
        code = EntityUtils.toString(response.getEntity());

        return code;
    }

    private static String tesseract() throws Exception {
        FileInputStream fin = new FileInputStream(new File("D:/testdata/1.jpg"));
        BufferedImage bi = ImageIO.read(fin);
        Vaildcode flt = new Vaildcode(bi);
        flt.fixBackground();    //去除底色
        flt.changeGrey();   //二值化，单色化

        //flt.filteNoise();   //第二次去除干扰
        //flt.getGrey();    //转换为灰度
        //flt.getBrighten();
        /*
        int w = 1;
        while(flt.findWord(0,"C:\\Users\\max\\Pictures\\word"+w+".bmp")){
            w++;
        }
        */

        //flt.getRotate(-30);
        //bi=flt.getProcessedImg();
        flt.saveFile(flt.getProcessedImg(), "d:/testdata/1.jpg", "bmp");
        String recognizeText = new OCRHelper().recognizeText(new File("d:/testdata/1.bmp"));
        return recognizeText;
    }

    public static List<NameValuePair> getPostData(Map<String, Object> map, Request request) {
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
//        Map table = null;
//        if (request != null) {
//            table = (HashMap) request.getExtra("$table");
//        }
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (entry.getValue() != null && StringUtils.startsWith(entry.getValue().toString(), "$UCSMY_"))
                nvps.add(new BasicNameValuePair(entry.getKey(), request.getExtra(entry.getKey().toString())!=null?request.getExtra(entry.getKey().toString()).toString():""));
            else
                nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
        }
        return nvps;
    }

    public static String getData(Map<String, Object> map, Request request) throws Exception{
//        Map table = null;
        StringBuffer url = new StringBuffer();
        if(StringUtils.contains(request.getUrl(),"?")) {
            url.append(request.getUrl());
        } else {
            url.append(request.getUrl()).append("?1=1");
        }
//        if (request != null) {
//            table = (HashMap) request.getExtra("$table");
//        }
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (entry.getValue() != null && StringUtils.startsWith(entry.getValue().toString(), "$UCSMY_"))
                url.append("&").append(entry.getKey()).append("=").append(URLEncoder.encode(request.getExtra(entry.getKey().toString())!=null?request.getExtra(entry.getKey().toString()).toString():"","utf-8"));
            else
                url.append("&").append(entry.getKey()).append("=").append(entry.getValue().toString());
        }
        return url.toString();
    }

    private void printHeader(Header[] headers) {
        for (int i = 0; i < headers.length; i++) {
            System.out.println(headers[i].getName() + "-----===----" + headers[i].getValue());
        }
    }

    public CloseableHttpClient login(JobDTO job) {
        try {
            //UcsmySite site = new UcsmySite();
            Captcha captcha = job.getCaptcha();
            Map postData = job.getLogin();
            Token token = job.getToken();
            StringBuffer tokenBuffer = null;
            Boolean flag = true;
            while (flag) {
                if (flag && captcha.isFlag()) {
                    tokenBuffer = new StringBuffer();
                    getCheckCode(getText(captcha.getCapUrl()), captcha.getCodeUrl(), tokenBuffer);
                    if (StringUtils.startsWith(tokenBuffer.toString(), "http")) {
                        System.out.println("请输入验证码：");
                        String temp = identify(tokenBuffer.toString());
                        tokenBuffer.delete(0, tokenBuffer.length()).append(temp);
                    }
                    postData.put(captcha.getCode(), tokenBuffer);
                }

                if (flag && token.isFlag()) {
                    tokenBuffer = new StringBuffer();
                    getCheckCode(getText(token.getTokenUrl()), token.getCheckToken(), tokenBuffer);
                    postData.put(token.getToken(), tokenBuffer.toString());
                }
                tokenBuffer = new StringBuffer();
                flag = getCheckCode(postText(job.getLoginUrl(), postData, null), captcha.getRightCode(), tokenBuffer);
                captcha.setFlag(flag);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return httpClient;
    }

    public void basicLogin() throws Exception {
        try {
            CredentialsProvider credsProvider = new BasicCredentialsProvider();
            credsProvider.setCredentials(
                    new AuthScope(PropertyUtils.get("basic.router.ip"), AuthScope.ANY_PORT),
                    new UsernamePasswordCredentials(PropertyUtils.get("basic.router.username"), PropertyUtils.get("basic.router.password")));


            AuthCache authCache = new BasicAuthCache();

            HttpClientContext context = HttpClientContext.create();
            context.setCredentialsProvider(credsProvider);
            context.setAuthCache(authCache);
            HttpGet httpget = new HttpGet(PropertyUtils.get("basic.router.release.url"));
            CloseableHttpResponse response1 = httpClient.execute(httpget, context);
            AuthState proxyAuthState = context.getProxyAuthState();
            System.out.println(EntityUtils.toString(response1.getEntity(), "gb2312"));
            Thread.sleep(20000);
            httpget = new HttpGet(PropertyUtils.get("basic.router.renew.url"));
            response1 = httpClient.execute(httpget, context);
            proxyAuthState = context.getProxyAuthState();
            System.out.println(EntityUtils.toString(response1.getEntity(), "gb2312"));
        } catch (Exception e) {
            System.out.println("无法重启BASIC路由器");
        }

    }

    public void dlinkLogin() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String json = getText("http://" + PropertyUtils.get("dlink.router.ip") + "/authentication.cgi?captcha=&dummy=" + System.currentTimeMillis());
            Map temp = objectMapper.readValue(json, Map.class);
            Map postData = new HashMap();
            String ts = PropertyUtils.get("dlink.router.username") + temp.get("challenge");
            AuthenticationUtil.getHeaders().add(new BasicHeader("Cookie", "uid=" + temp.get("uid")));
            String password = HMacMD5Utils.getHmacMd5Str(PropertyUtils.get("dlink.router.admin"), ts);
            System.out.println(password);
            postData.put("password", password);
            postData.put("id", PropertyUtils.get("dlink.router.username"));
            postText("http://192.168.0.1/authentication.cgi", postData, null);
            postData.clear();
            postData.put("EVENT", "WAN-1.DHCP.RELEASE");
            postText("http://" + PropertyUtils.get("dlink.router.ip") + "/service.cgi", postData, null);
            Thread.sleep(50000);
            postData.clear();
            postData.put("EVENT", "WAN-1.DHCP.RENEW");
            postText("http://" + PropertyUtils.get("dlink.router.ip") + "/service.cgi", postData, null);
        } catch (Exception e) {
            System.out.println("无法重启DLINK路由器");
//            e.printStackTrace();
        }

    }

    public static List<Header> getHeaders() {
        return headers;
    }

    public static void setHeaders(List<Header> headers) {
        AuthenticationUtil.headers = headers;
    }

}
