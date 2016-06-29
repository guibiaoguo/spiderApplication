package com.spider.collection.downloader;

import com.google.common.collect.Sets;
import com.spider.collection.entity.*;
import com.spider.collection.main.SpiderApplication;
import com.spider.collection.pipeline.HttpPipline;
import com.spider.collection.scheduler.HttpScheduler;
import com.spider.collection.util.AuthenticationUtil;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.downloader.AbstractDownloader;
import us.codecraft.webmagic.downloader.HttpClientGenerator;
import us.codecraft.webmagic.scheduler.Scheduler;
import us.codecraft.webmagic.selector.PlainText;
import us.codecraft.webmagic.utils.HttpConstant;
import us.codecraft.webmagic.utils.UrlUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2015/11/17.
 */
public class HttpClientDownloaderBack extends AbstractDownloader {

    private HttpScheduler scheduler;

    private Logger logger = LoggerFactory.getLogger(getClass());

    private final Map<String, CloseableHttpClient> httpClients = new HashMap<String, CloseableHttpClient>();

    private SpiderApplication spiderApplication;

    private HttpClientGeneratorBack httpClientGenerator = new HttpClientGeneratorBack();
    private static int f = 0;

    public HttpClientDownloaderBack(HttpScheduler scheduler) {
        this.scheduler = scheduler;
    }

    public HttpClientDownloaderBack(JobDTO jobDTO, HttpScheduler scheduler) {
        this.scheduler = scheduler;
    }

    private CloseableHttpClient getHttpClient(Site site) {
        JobDTO job = (JobDTO) SpiderApplication.getJobMap().get("job");
        if (site == null) {
            return httpClientGenerator.getClient(null);
        }

        if (job.isClearClient()) {
            httpClients.clear();
        }

        String domain = site.getDomain();
        CloseableHttpClient httpClient = httpClients.get(domain);
        if (httpClient == null) {
            synchronized (this) {
                httpClient = httpClients.get(domain);
                if (httpClient == null) {
                    httpClient = httpClientGenerator.getClient(site);
                    if (job.getIsAuthentication()) {
//                        AuthenticationUtil authenticationUtil1 = new AuthenticationUtil(httpClient);
//                        httpClient = authenticationUtil1.login(job);
                    }
                    httpClients.put(domain, httpClient);
                }
            }
        }
        return httpClient;
    }

    @Override
    public Page download(Request request, Task task) {
//        JobDTO job = ((JobDTO) SpiderApplication.getJobMap().get("job")).getTasksDTO().getHelpTaskDTOs();
        Site site = (Site)SpiderApplication.getJobMap().get("site");
//        if (task != null) {
//            site = task.getSite();
//        }
        Set<Integer> acceptStatCode;
        String charset = null;
        Map<String, String> headers = null;
        if (site != null) {
            acceptStatCode = site.getAcceptStatCode();
            charset = site.getCharset();
            headers = site.getHeaders();
        } else {
            acceptStatCode = Sets.newHashSet(200);
        }
        logger.info("downloading page {}", request.getUrl());
        CloseableHttpResponse httpResponse = null;

        int statusCode = 0;
//        List<HelpTaskDTO> helpTasks = job.getTasksDTO().getHelpTaskDTOs();
        Page page = null;
        Boolean flag = true;

        while (flag) {
            if (request.getExtra("$query") != null && StringUtils.equalsIgnoreCase(request.getExtra("$query").toString(), "1")) {
                request = scheduler.poll(null);
            }

            if (request.getExtra("$UCSMY_IGNORE") != null) {
                return page;
            }
            StringBuffer temp = new StringBuffer();
            for (HelpTaskDTO helpTask : ((JobDTO) SpiderApplication.getJobMap().get("job")).getTasksDTO().getHelpTaskDTOs()) {
                try {
                    request.setMethod(helpTask.getHttpMethod());
                    if (StringUtils.equalsIgnoreCase(request.getMethod(), "post")) {
                        List<NameValuePair> nvps = AuthenticationUtil.getPostData(helpTask.getParame(), request);
                        if (StringUtils.contains(helpTask.getHelpUrl(), "$CAPTCHA_")) {
                            String capUrl = StringUtils.substringAfter(helpTask.getHelpUrl(), "$CAPTCHA_");
                            if (StringUtils.startsWithIgnoreCase(capUrl, "http"))
                                request.setUrl(capUrl);
                            else {
                                request.setUrl(StringUtils.substringBefore(request.getUrl(), "/") + capUrl);
                            }
                            String codeUrl = temp.toString();
                            if (StringUtils.startsWithIgnoreCase(codeUrl, "http"))
                                codeUrl = temp.toString();
                            else {
                                codeUrl = StringUtils.substringBefore(request.getUrl(), "/") + temp.toString();
                            }
//                            String code = AuthenticationUtil.identifyByTesseract(codeUrl, httpClient);
//                            request.putExtra(StringUtils.substringBefore(helpTask.getHelpUrl(), "_$CAPTCHA_"), code);
//                            nvps.add(new BasicNameValuePair(StringUtils.substringBefore(helpTask.getHelpUrl(), "_$CAPTCHA_"), code));
                        } else {
                            request.setUrl(helpTask.getHelpUrl());
                        }
                        request.putExtra("nameValuePair", nvps);
                    } else if (StringUtils.equalsIgnoreCase(request.getMethod(), "get")) {
                        if (StringUtils.contains(helpTask.getHelpUrl(), "$SELF_")) {
                            String ts = StringUtils.substringBefore(helpTask.getHelpUrl(), "_$SELF_");
                            if (StringUtils.isNotEmpty(ts)) {
                                temp.append("&").append(ts).append("=").append(request.getExtra(ts));
                            }
                            if (!StringUtils.startsWithIgnoreCase(temp.toString(), "http")) {
                                temp.insert(0, StringUtils.substringBeforeLast(request.getUrl(), "/"));
                            }
                            request.setUrl(temp.toString());
                        } else if (StringUtils.contains(helpTask.getHelpUrl(), "$HEAD_")) {
                            request.setUrl(StringUtils.substringAfter(helpTask.getHelpUrl(), "$HEAD_"));
                         } else if (StringUtils.startsWith(helpTask.getHelpUrl(), "$UCSMY_")) {
//                            request = AuthenticationUtil.getQueryData(helpTask.getParame(), request, job.getTasksDTO());
                            request.setUrl(StringUtils.substringAfter(helpTask.getHelpUrl(), "$UCSMY_"));
                            String url = AuthenticationUtil.getData(helpTask.getParame(), request);
                            request.setUrl(url);
                        } else if (StringUtils.contains(helpTask.getHelpUrl(), "$RANDOM_")) {
                            String ts = StringUtils.substringBefore(helpTask.getHelpUrl(), "_$RANDOM_");
                            site.addCookie("_utm", "");
                            site.addCookie("token", "");
                            site.addHeader("Cookie", "");
                            if (StringUtils.isNotEmpty(ts)) {
                                if (StringUtils.isEmpty(request.getUrl())) {
                                    request.setUrl(StringUtils.substringAfter(helpTask.getHelpUrl(), "_$RANDOM_"));
                                    request.putExtra("url", StringUtils.substringAfter(helpTask.getHelpUrl(), "_$RANDOM_"));
                                }
                                request.setUrl(request.getUrl().replace("search", "tongji").replace("$RP", request.getExtra("id") != null ? request.getExtra("id").toString() : temp.toString()) + "?" + ts + "=" + System.currentTimeMillis());
                            }
                        } else if(StringUtils.isEmpty(request.getUrl())){
                            request.setUrl(helpTask.getHelpUrl());
                        }
                    }
                    CloseableHttpClient httpClient = getHttpClient(site);
                    HttpUriRequest httpUriRequest = getHttpUriRequest(request, site, headers);
//                    if(httpUriRequest == null) {
//                        spiderApplication = new SpiderApplication();
//                        spiderApplication.getJob(job.getId() + "");
//                    }
                    httpResponse = httpClient.execute(httpUriRequest);
                    statusCode = httpResponse.getStatusLine().getStatusCode();
                    request.putExtra(Request.STATUS_CODE, statusCode);
                    if (statusAccept(acceptStatCode, statusCode)) {
                        page = handleResponse(request, charset, httpResponse, task);
                        temp.delete(0, temp.length());
                        flag = AuthenticationUtil.getCheckCode(page.getRawText(), helpTask.getRightCode(), temp);
                        page.putField("$httpclient",httpClient);
                        onSuccess(request);
                    } else {
                        logger.warn("code error " + statusCode + "\t" + request.getUrl());
                        page = null;
//                        logger.info("睡眠" + site.getSleepTime() / 1000 + "秒再开始工作！");
//                        Thread.sleep(site.getSleepTime());
                        scheduler.clear();
                        spiderApplication = new SpiderApplication();
                        spiderApplication.postData();
                        spiderApplication.getJob(((JobDTO) SpiderApplication.getJobMap().get("job")).getId() + "");
                        flag = false;
                    }
                } catch (Exception e) {
                    logger.info("download page " + request.getUrl() + " error", e);
                    e.printStackTrace();
                   addRequest(request);
                    if (site.getCycleRetryTimes() > 0) {
                        request.putExtra("$UCSMY_PIP", "$UCSMY");
                        return addToCycleRetry(request, site);
                    }
                    onError(request);
                    return null;
                } finally {
                    request.putExtra(Request.STATUS_CODE, statusCode);
                    try {
                        if (httpResponse != null) {
                            //ensure the connection is released back to pool
//                            if (request.getExtra("$update")!=null && StringUtils.equals(request.getExtra("$update").toString(), "0"))
//                                AuthenticationUtil.update(job.getTasksDTO(),request.getExtra("id").toString());
                            request.putExtra("$Cookie", httpResponse.getLastHeader("SET-COOKIE") != null ? httpResponse.getLastHeader("SET-COOKIE").getValue() : "");
                            EntityUtils.consume(httpResponse.getEntity());
                            logger.info("获取到的验证文字--------------" + temp);
//                                if (request.getExtra("$UCSMY_PIP") == null) {
                            if (flag) {
                                addRequest(request);
//                                }
//                                else {
//                                    logger.info("test URL ");
//                                }
//                                if (scheduler.getLeftRequestsCount(null) == 0 && count < job.getTasksDTO().getCount()) {
//                                    request.putExtra(Request.CYCLE_TRIED_TIMES, 1);
//                                    request.putExtra("$UCSMY_PIP", "$UCSMY");
//                                    scheduler.push(request, null);
//                                }
                                try {
//                                    AuthenticationUtil authenticationUtil = new AuthenticationUtil();
//                                    authenticationUtil.basicLogin();
//                                    authenticationUtil.dlinkLogin();
                                } catch (Exception e) {
                                    logger.info("无法断线重拨路由器！");
                                }
                                logger.info("睡眠" + site.getSleepTime() / 1000 + "秒再开始工作！");
                                Thread.sleep(site.getSleepTime());
                                if (StringUtils.endsWithIgnoreCase(temp.toString(), "$TRUE")) {
                                    flag = false;
                                    break;
                                }
                                request.putExtra("$query", 1);
                                if (StringUtils.contains(helpTask.getHelpUrl(), "$CAPTCHA_")) {
                                    request.putExtra("$query", 2);
                                }

                                break;
                            } else {
//                                collectionData.getRequests().add(tempMap);
                                if (StringUtils.contains(helpTask.getHelpUrl(), "$HEAD_")) {
                                    site.addHeader(StringUtils.substringBefore(helpTask.getHelpUrl(), "_$HEAD_"), temp.toString());
                                } else if (StringUtils.contains(helpTask.getHelpUrl(), "$RANDOM_")) {
                                    site.addHeader("Cookie", request.getExtra("$Cookie").toString());
                                    String tmpToken = AuthenticationUtil.unicodeCodeArrayToString(temp.toString().split(","));
                                    Pattern pattern = Pattern.compile("return\'(.*?)\'");
                                    Matcher matcher = pattern.matcher(tmpToken);

                                    if (matcher.find()) {
                                        String tokenParamStr = AuthenticationUtil.convertToUtm(request.getExtra("id").toString().replaceAll("<[^>]*>", ""), matcher.group(1));
//                                        System.out.println(tokenParamStr);
                                        site.addCookie("_utm", tokenParamStr);
                                        pattern = Pattern.compile("token=(.*?);");
                                        matcher = pattern.matcher(tmpToken);
                                        if (matcher.find()) {
//                                            System.out.println(matcher.group(1));
                                            site.addCookie("token", matcher.group(1));
                                        }
                                        request.setUrl(request.getExtra("url").toString().replace("tongji", "company").replace("$RP", request.getExtra("id") != null ? request.getExtra("id").toString() : ""));
//                                        request.setUrl(request.getExtra("url").toString());
                                    }
                                }

                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return page;
    }

    public void addRequest (Request request) {
        Map<String, CollectionData> collectionDataMap = HttpPipline.getCollectionDataMap();
        CollectionData collectionData = collectionDataMap.get("collectionData");;
//        if (collectionDataMap == null) {
//            collectionDataMap = new HashMap<>();
//            collectionData = new CollectionData();
//        } else {
//            collectionData =
//        }
        Map tempMap = (Map)request.getExtra("$table");
        if (tempMap !=null && request.getExtra("$UCSMY_PIP") == null) {
            int count = request.getExtra("$count") != null ?Integer.parseInt(request.getExtra("$count").toString()):0;
            tempMap.put("$count",++count);
            collectionData.getRequests().add(tempMap);
        }

//        collectionDataMap.put("collectionData", collectionData);
    }

    protected Page handleResponse(Request request, String charset, HttpResponse httpResponse, Task task) throws IOException {
        String content = getContent(charset, httpResponse);
        Page page = new Page();
        page.setRawText(content);
        page.setUrl(new PlainText(request.getUrl()));
        page.setRequest(request);
        page.setStatusCode(httpResponse.getStatusLine().getStatusCode());
        return page;
    }

    protected String getContent(String charset, HttpResponse httpResponse) throws IOException {
        if (charset == null) {
            byte[] contentBytes = IOUtils.toByteArray(httpResponse.getEntity().getContent());
            String htmlCharset = getHtmlCharset(httpResponse, contentBytes);
            if (htmlCharset != null) {
                return new String(contentBytes, htmlCharset);
            } else {
                logger.warn("Charset autodetect failed, use {} as charset. Please specify charset in Site.setCharset()", Charset.defaultCharset());
                return new String(contentBytes);
            }
        } else {
            return IOUtils.toString(httpResponse.getEntity().getContent(), charset);
        }
    }

    protected String getHtmlCharset(HttpResponse httpResponse, byte[] contentBytes) throws IOException {
        String charset;
        // charset
        // 1??encoding in http header Content-Type
        String value = httpResponse.getEntity().getContentType().getValue();
        charset = UrlUtils.getCharset(value);
        if (StringUtils.isNotBlank(charset)) {
            logger.debug("Auto get charset: {}", charset);
            return charset;
        }
        // use default charset to decode first time
        Charset defaultCharset = Charset.defaultCharset();
        String content = new String(contentBytes, defaultCharset.name());
        // 2??charset in meta
        if (StringUtils.isNotEmpty(content)) {
            Document document = Jsoup.parse(content);
            Elements links = document.select("meta");
            for (Element link : links) {
                // 2.1??html4.01 <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
                String metaContent = link.attr("content");
                String metaCharset = link.attr("charset");
                if (metaContent.indexOf("charset") != -1) {
                    metaContent = metaContent.substring(metaContent.indexOf("charset"), metaContent.length());
                    charset = metaContent.split("=")[1];
                    break;
                }
                // 2.2??html5 <meta charset="UTF-8" />
                else if (StringUtils.isNotEmpty(metaCharset)) {
                    charset = metaCharset;
                    break;
                }
            }
        }
        logger.debug("Auto get charset: {}", charset);
        // 3??todo use tools as cpdetector for content decode
        return charset;
    }

    @Override
    public void setThread(int thread) {
        httpClientGenerator.setPoolSize(thread);
    }

    protected boolean statusAccept(Set<Integer> acceptStatCode, int statusCode) {
        return acceptStatCode.contains(statusCode);
    }

    protected HttpUriRequest getHttpUriRequest(Request request, Site site, Map<String, String> headers) {
        JobDTO job = (JobDTO) SpiderApplication.getJobMap().get("job");
        RequestBuilder requestBuilder = selectRequestMethod(request).setUri(request.getUrl());
        if (headers != null) {
            for (Map.Entry<String, String> headerEntry : headers.entrySet()) {
                requestBuilder.addHeader(headerEntry.getKey(), headerEntry.getValue());
            }
            List<CookieDTO> cookieDTOs = job.getCookieDTOs();
            Random random = new Random();
            if (cookieDTOs.size() > 0) {
                int i = random.nextInt(cookieDTOs.size());
                requestBuilder.addHeader("Cookie", cookieDTOs.get(i).getCookie());
                request.putExtra("$cookieID", cookieDTOs.get(i).getId());
            }
        }

        RequestConfig.Builder requestConfigBuilder = RequestConfig.custom().setConnectionRequestTimeout(site.getTimeOut()).setSocketTimeout(site.getTimeOut()).setConnectTimeout(site.getTimeOut()).setCookieSpec(CookieSpecs.BEST_MATCH)
                .setExpectContinueEnabled(true)
                .setStaleConnectionCheckEnabled(true)
                .setCircularRedirectsAllowed(true)
                .setTargetPreferredAuthSchemes(Arrays.asList(AuthSchemes.NTLM, AuthSchemes.DIGEST))
                .setProxyPreferredAuthSchemes(Arrays.asList(AuthSchemes.BASIC));
        if (site.getHttpProxyPool().isEnable()) {
//            HttpHost host = new HttpHost("127.0.0.1", 8888);
//            requestConfigBuilder.setProxy(host);
//            request.putExtra(Request.PROXY, host);
            HttpHost host = site.getHttpProxyFromPool();
            if(host != null) {
                requestConfigBuilder.setProxy(host);
                request.putExtra(Request.PROXY, host);
            }
        }
        requestBuilder.setConfig(requestConfigBuilder.build());
        return requestBuilder.build();
    }

    protected RequestBuilder selectRequestMethod(Request request) {
        String method = request.getMethod();
        if (method == null || method.equalsIgnoreCase(HttpConstant.Method.GET)) {
            //default get
            return RequestBuilder.get();
        } else if (method.equalsIgnoreCase(HttpConstant.Method.POST)) {
            RequestBuilder requestBuilder = RequestBuilder.post();
            List<NameValuePair> nameValuePairs = (List<NameValuePair>) request.getExtra("nameValuePair");
            try {
                requestBuilder.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return requestBuilder;
        } else if (method.equalsIgnoreCase(HttpConstant.Method.HEAD)) {
            return RequestBuilder.head();
        } else if (method.equalsIgnoreCase(HttpConstant.Method.PUT)) {
            return RequestBuilder.put();
        } else if (method.equalsIgnoreCase(HttpConstant.Method.DELETE)) {
            return RequestBuilder.delete();
        } else if (method.equalsIgnoreCase(HttpConstant.Method.TRACE)) {
            return RequestBuilder.trace();
        }
        throw new IllegalArgumentException("Illegal HTTP Method " + method);
    }


}
