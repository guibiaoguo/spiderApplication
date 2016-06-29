package com.spider.collection;


import org.apache.commons.lang.StringUtils;
import org.apache.http.*;
import org.apache.http.client.CookieStore;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.cookie.CookieSpecProvider;
import org.apache.http.impl.client.*;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.impl.cookie.BestMatchSpecFactory;
import org.apache.http.impl.cookie.BrowserCompatSpecFactory;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import us.codecraft.webmagic.Site;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2015/10/21.
 */
public class TaoBaoLogin {

    // The HttpClient is used in one session
    private CloseableHttpResponse response;
    private CloseableHttpClient httpclient;
    private Header[] headers;
    static CookieStore cookieStore = null;
    private String testUrl = "http://me.1688.com/info/aliasonline.html";
    static HttpClientContext context = null;

    public CloseableHttpClient getHttpclient() {
        return httpclient;
    }

    public void setHttpclient(CloseableHttpClient httpclient) {
        this.httpclient = httpclient;
    }

    public TaoBaoLogin() {

        httpclient = HttpClientBuilder.create().build();
        Header[] headers = {
                new BasicHeader("Host","login.taobao.com"),
                new BasicHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64; rv:35.0) Gecko/20100101 Firefox/35.0"),
                new BasicHeader("Referer","https://login.taobao.com/member/login.jhtml"),
                new BasicHeader("Content-Type","application/x-www-form-urlencoded"),
                new BasicHeader("Connection","Keep-Alive")};
        this.headers = headers;
    }
    public static void main(String[] args) throws Exception{
        TaoBaoLogin taoBaoLogin = new TaoBaoLogin();
        Site site = new Site();
        taoBaoLogin.taobao(site);
        //taoBaoLogin.testContext();
        taoBaoLogin.testCookieStore();
        //System.out.println("淘宝");
    }

    public List<NameValuePair> getPostData() {
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("ua","044UW5TcyMNYQwiAiwTR3tCf0J/QnhEcUpkMmQ=|Um5OcktySnNOek57Qn1CeC4=|U2xMHDJ+H2QJZwBxX39RaFR6WnQoSS9DJFogDlgO|VGhXd1llXGVdZFltWWxSalJqXWBCekN+S3RKc097QHlDfEhxTWM1|VWldfS0RMQU9AiIeJgYoUiEKKhQ0CC17WjdMIXETPWs9|VmNDbUMV|V2NDbUMV|WGRYeCgGZhtmH2VScVI2UT5fORtmD2gCawwuRSJHZAFsCWMOdVYyVTpbPR99HWAFYVMoRSlIM141SBZPCTlZJFkgWnNMdEoBKBcvEFx1SnJNAX8Bf10gSS5ELUpoA2QBKBcvEFx1SnJMAHoDZk9wSHY4UW0eIxw+AiAcJBEtGCMeJhklBzsZZgxUK2AZNxc5F0EX|WWdHFysULQ0wECwSLRk5DTAOLhIrEi8POwY7GycZIBs7DzIPWQ8=|WmZYeCgGWjtdMVYoUn5Cf1FxXwNABCgUIAA9HSAAPwA7FUMV|W2daelQEOwM8CTwcS2VZYF5nWWBZY1doUWpTJhs5AT8FPAI6DjsBPAE0Cj8BNGNNbVEHKX8=|XGZGFjgWNgoqFSEbLhNFEw==|XWVFFTtkP3ktUStGPVsyVzpuUnxcDDgCOBgnGSZwUG1NY01tU25aZFwKXA==|XmREFDplPngsUCpHPFozVjtvU31dYUF/QnZNdCJ0|X2REFDplPngsUCpHPFozVjtvU31daUl0VGpVb1BvOW8=|QHpaCiR7IGYyTjRZIkQtSCVxTWNDfl5gX2VdaD5o|QXtbCyV6IWczSiNZI106QS15RWtLd1dpVmxWbTtt|QnhYCCZ5ImQwSSBaIF45Qi56RmhIdVVrVWpWYjRi|Q3lZCSdnM3odcRxfOUA8QRV0WnpGZlhmWWZTBVM=|RHxcDCJ9JmA0SDJfJEIrTiN3S2VFFSEULw8wCTJkRHlZd1l5R3lMdUsdSw==|RX9fDyFhNXwbdxpZP0Y6RxNyXHxBYV9hVGxVA1U=|RnxcDCJ9JmA0SDJfJEIrTiN3S2VFeVlnWWxWaz1r|R39fDyF+JWM3SzFcJ0EoTSB0SGZGFiIWLw8wCTFnR3padFp6RH1Ee0MVQw==|SHBQAC5xKmw4RD5TKE4nQi97R2lJGS0ULg4xDzRiQn9fcV9/QXpFekEXQQ==|SXBQAC5uOmIfdgtqAU0qUTQaOg41CioUKBQ0DTILNw9ZDw==|SnJSAiwCInJHfkZmWGBeCCgVNRs1FSwSKxQqfCo=|S3FRAS9wK205RT9SKU8mQy56RmhIdVVsUmtVaD5o|THZWBihoPHUSfhNQNk8zThp7VXVJaVBuVm1TBVM=|TXZWBihoPHUSfhNQNk8zThp7VXVOe1tmRn9GfUB4Lng=|TnVVBStrP3YRfRBTNUwwTRl4VnZNdlZrS3JLfkF+KH4=|T3RUBCpqPncQfBFSNE0xTBh5V3dPc1NuTndPcUt/KX8=|cEtrOxVVAUgvQy5tC3IOcydGaEh9XWBAeUJ/QHkveQ==|cUpqOhRUAEkuQi9sCnMPciZHaUlyRmZbe0J5RX1GEEY=|cklpORdXA0otQSxvCXAMcSVEakpwSGhVdUx3SX1AFkA=|c0pqOhRUAFgkTitKN28SewZnDCICNw04GCEfIgI7DjMLPmg+|dE5uPhBQBE0qRitoDncLdiJDbU1wUGhXblZvOW8=|dU9vPxE/HyICOgU8BzpsOg==|dk5uPhBPFFIGegBtFnAZfBFFeVd3JxMoEjINMwZQcE1tQ21NdUF1QX8pfw==|d01tPRM9HSEBOgc6BjlvOQ==|eEJiMhxDGF4KdgxhGnwVcB1JdVt7R2dcYVxjWQ9Z|eUJiMhxDGF4KdgxhGnwVcB1JdVt7T3VVaEhzT3NMdyF3|ekFhMR9AG10JdQ9iGX8Wcx5Kdlh4RHlFZVh4Q39Be0YQRg==|e0BgMB5BGlwIdA5jGH4Xch9Ld1l5RXhHZ1p6QX1GfUMVQw==|fEdnNxlGHVsPcwlkH3kQdRhMcF5+Sn9fYkJ5RXBOcyVz|fUZmNhhHHFoOcghlHngRdBlNcV9/Q35BYVx8R3hFf0IUQg==|fkVlNRtEH1kNcQtmHXsSdxpOclx8SH1dYEB7RHtOey17|f0RkNBpFHlgMcApnHHoTdhtPc119QXxBYVx8R3hAeUMVQw==|YFt7KwVaAUcTbxV4A2UMaQRQbEJiVmxMcVFqVW9VYTdh|YVp6KgRbAEYSbhR5AmQNaAVRbUNjX2Jff0JiWWdaZlkPWQ==|Yll5KQdYA0URbRd6AWcOawZSbkBgXGFefkNjWGZZZ18JXw==|Y1h4KAZZAkQQbBZ7AGYPagdTb0FhXWBcfEFhWmRdZFoMWg==|ZF19LQNcB0EVbAV/BXscZwtfY01tWWNcfEN3T29UbVZjWQ9Z|ZV19LQNcB0EVbAV/BXscZwtfY01tPQkzDy8QJB9JaVR0WnRUb1ZsWWQyZA==|Zlx8LAJdBkAUaBJ/BGILbgNXa0VlWHhDek9zTxlP|Z119LQNcB0EVbAV/BXscZwtfY01tUXFKc0Z/QhRC|aFFxIQ9QC00ZYAlzCXcQawdTb0FhVGBdfUJ2QmJZbVlmWw1b|aVFxIQ9PG1I1WTR3EWgUaT1cclICNw8yEiwUKX9fYkJsQmJYZFtkXghe|alBwIA5RCkwYYQhyCHYRagZSbkBgXX1He0R9RxFH|a1FxIQ9PG1I1WTR3EWgUaT1cclJuTnRId0xwJnA=|bFR0JApKHlcwXDFyFG0RbDhZd1cHMgk0FCoSL3lZZERqRGReYV5gWQ9Z|bVR0JApKHkY7UjRYMUombwhkCV04FjYKNwoyEiwVKwsxCDEFOW85|blZ2JghIHEQ5UDZaM0gkbQpmCyUFVWlUbFZ2SHFLa1dqU29NdVVrVW1Pd1dpUnJNeC4OMxM9XSZLJRU8Az0TMwkxDzIOWA4=|b1V1JQtLH1YxXTBzFWwQbTlYdlZrS3FJd0twJnA=|ECoKWnQ0YDhFLEomTzRYEXYad1l5RWVfZ1lmUwVT|ESsLW3U1YTlELUsnTjVZEHcbdlh4RWVfZ1ltWA5Y|EigIWHY2YitMIE0OaBFtEEQlCysXNw01DDELXQs=|EygIWHY2YitMIE0OaBFtEEQlCysePgMjGSwWLBNFEw==|FC8PX3ExZSxLJ0oJbxZqF0MiDCwZOQQkHisfIRtNGw==|FS4OXnAwZC1KJksIbhdrFkIjDS0YOAUlHysXKxdBFw==|Fi0NXXMzZy5JJUgLbRRoFUEgDi4bOwYmHCgXLBFHEQ==|FywMXHIyZi9IJEkKbBVpFEAhDy8aOgcnHSkXIxZAFg==|GCMDU309aSBHK0YFYxpmG08uACAVNQgoEiYeIBRCFA==|GSAAUH4hejxoEXgCeAZhGnYiHjAQJBwmBjkDOxsuEyofKnwq|GiICUnwjeD5qE3oAegRjGHQgHDISQnZDfV1iVmo8HCEBLwEhFCkcIhhOGA==|GyEBUX8/ayJFKUQHYRhkGU0sAiIfPwo3AjkDVQM=|HCYGVngnfDpuF34EfgBnHHAkGDYWKgo/AjcCOmw6|HSUFVXtVdSUQLho6BD0HUXFMbEJsTHlFcEVwJnA=|HiQEVHolfjhsFXwGfAJlHnImGjQUKQk8ADQLNmA2|HyUFVXs7byZBLUADZRxgHUkoBiYaOg8wDTEFUwU=|ADoaSmQkcDleMl8cegN/AlY3GTkEJBEoFSgcShw=|ATsbS2VLa1Z2Q3pHeUAWQA==|AjsbS2UlcSlUPVs3XiVJAGcLZjJXeVlsWGZGeEB1VWBbYFxmMGY=|AzsbS2VLazsONwsrFS0TRWVYeFZ4WG1XalJtO20=|BD4eTmBOblJyR31AdUsdSw==|BT8fT2EhdTxbN1oZfwZ6B1MyHDwAIBUvEycfSR8=|Bj0dTWMjdz5ZNVgbfQR4BVEwHj4LNxcqCj8KNA8zZTM=|BzwcTGIidj9YNFkafAV5BFAxHz8KPh4jAzYDOAY9az0=|CDMTQ20teTBXO1YVcwp2C18+EDALMBAtDTgNOQU9az0=|CTISQmwseDFWOlcUcgt3Cl4/ETEEPh4jAzYCPAA8ajw=|CjMTQ20teTBXO1YVcwp2C18+EDAFPAgoFi0SMgY6DzsCVAI=|CzMTQ20teTBXO1YVcwp2C18+EDBgVWxYeEZ+RxExDCwCLAw4BzkGOG44|DDcXR2kpfTRTP1IRdw5yD1s6FDQBIRw8CDYJNQlfCQ==|DTYWRmgofDVSPlMQdg9zDlo7FTUNNBQpCT0EOQQ5bzk=|DjYWRmgofCRYMlc2SxNuB3obcF5+LhojFjYPNwtdfUBgTmBAdEx3T3Ikcg==|DzUVRWsrfzZRPVATdQxwDVk4FjYLKx8nHCgRRxE=|MAoqelQUQBhkDmsKdy9SO0YnTGJCfl5qUmhUaz1r|MQg1CCgVNQoqFi8TMw01Dy8XIwM5ASEdIRg4BCQYIx4+CjcXKRw8AD4eIBs7BDsbJRw8AjwcIh09Az8fJAQ7Dy8QJQU7GyQEJRAwDS0SLAwzDCwZOQMjGDgAIBk5BycGOg8vEyoKNggoCTUKKhYqCjYLKwozEzIKKgswEDELKwo/Hz4KKgs3CioLNwsrCjYJKQg0CioLNw4uDzEJKQg3DS0MNRU0DCwNMwcnBjgEJAUwEDEFJQQ6AyMCOwIiAz0GJgc7BSUEOAEhADwEJAU5AiIDPQAgAT8DIwI8AyMCPAIiAz0EJAU7AyMCOgEhADkMLA0yCioLNA8vDjYKKgs0ASEAPwsrCjQJKQg2CioLMAgoCTELKwo0DS0MMgoqCzIGJgc/AiIDOwcnBj4BIQA6BCQFPgYmBz8HJwY+BSUEPwoqCzIKKgsyCV8="));
        nvps.add(new BasicNameValuePair("TPL_username", "15626241465"));
        nvps.add(new BasicNameValuePair("TPL_password_2", "7590ea30adcb62dda1d441042a79c1da3f7f6d8a32958812ab43616d3126e108af52bf2b4fc7d1e84adcc33b8ad3317965d9d07246d3fddb934e851b47293893ae466e3c45f0be091a91d19a10a4ae7509bfaef0b3dc331d969cd1da6784e3b967b785457fcf121e87980ee6bf38bdd9cb5d0241abea2324e16e4c8243db9782"));
        nvps.add(new BasicNameValuePair("TPL_checkcode",""));
        nvps.add(new BasicNameValuePair("loginsite","3"));
        nvps.add(new BasicNameValuePair("newlogin","0"));
        nvps.add(new BasicNameValuePair("TPL_redirect_url",""));
        nvps.add(new BasicNameValuePair("from","b2b"));
        nvps.add(new BasicNameValuePair("fc","default"));
        nvps.add(new BasicNameValuePair("style","b2b"));
        nvps.add(new BasicNameValuePair("css_style",""));
        nvps.add(new BasicNameValuePair("tid","XOR_1_000000000000000000000000000000_152B3425440975797276017A"));
        nvps.add(new BasicNameValuePair("support","000001"));
        nvps.add(new BasicNameValuePair("CtrlVersion","1,0,0,7"));
        nvps.add(new BasicNameValuePair("loginType","3"));
        nvps.add(new BasicNameValuePair("minititle",""));
        nvps.add(new BasicNameValuePair("minipara",""));
        nvps.add(new BasicNameValuePair("umto","NaN"));
        nvps.add(new BasicNameValuePair("pstrong",""));
        nvps.add(new BasicNameValuePair("sign",""));
        nvps.add(new BasicNameValuePair("need_sign",""));
        nvps.add(new BasicNameValuePair("islgnore",""));
        nvps.add(new BasicNameValuePair("full_redirect","true"));
        nvps.add(new BasicNameValuePair("popid",""));
        nvps.add(new BasicNameValuePair("callback",""));
        nvps.add(new BasicNameValuePair("guf",""));
        nvps.add(new BasicNameValuePair("not_duplite_str",""));
        nvps.add(new BasicNameValuePair("need_user_id",""));
        nvps.add(new BasicNameValuePair("poy",""));
        nvps.add(new BasicNameValuePair("gvfdcname","10"));
        nvps.add(new BasicNameValuePair("gvfdce",""));
        nvps.add(new BasicNameValuePair("from_encoding",""));
        nvps.add(new BasicNameValuePair("sub",""));
        nvps.add(new BasicNameValuePair("loginASR","1"));
        nvps.add(new BasicNameValuePair("loginASRSuc","1"));
        nvps.add(new BasicNameValuePair("allp",""));
        nvps.add(new BasicNameValuePair("oslanguage",""));
        nvps.add(new BasicNameValuePair("sr","1440*900"));
        nvps.add(new BasicNameValuePair("loginASRSuc","1"));
        nvps.add(new BasicNameValuePair("osVer",""));
        nvps.add(new BasicNameValuePair("naviVer","firefox|41"));
        return nvps;
    }
    public boolean needCheckCode(String content) {
        //\u8bf7\u8f93\u5165\u9a8c\u8bc1\u7801这六个字是请输入验证码的utf-8编码
        Pattern pattern = Pattern.compile("请输入验证码");
        Matcher matcher = pattern.matcher(content);
        if(matcher.find()) {
            System.out.println("此次安全验证异常，您需要输入验证码");
            return true;
        } else {
            System.out.println("此次安全验证通过，您这次不需要输入验证码");
            return false;
        }
    }

    public String getCheckCode(String content) {
        String img = null;
        Pattern pattern = Pattern.compile("<img id=\"J_StandardCode_m.*?data-src=\"(.*?)\"");
        Matcher matcher = pattern.matcher(content);
        while(matcher.find()) {
            img = matcher.group(1);
        }
        return img;
    }

    public boolean loginWithCheckCode(String content,StringBuffer token) {

        //检测验证码错误的正则表达式，\u9a8c\u8bc1\u7801\u9519\u8bef 是验证码错误五个字的编码
        Pattern pattern = Pattern.compile("验证码错误");
        Matcher matcher = pattern.matcher(content);
        if(matcher.find()) {
            System.out.println("验证码错误，请重新输入");
            return true;
        } else {
            pattern = Pattern.compile("<input type=\"hidden\" id=\"J_HToken.*?value=\"(.*?)\"");
            matcher = pattern.matcher(content);
            while(matcher.find()){
                token.append(matcher.group(1));
            }
            return false;
        }
    }
    public boolean getStByToken(String content,StringBuffer st) {
        //检测验证码错误的正则表达式，\u9a8c\u8bc1\u7801\u9519\u8bef 是验证码错误五个字的编码
        Pattern pattern = Pattern.compile("\\{\"st\":\"(.*?)\"\\}");
        Matcher matcher = pattern.matcher(content);
        if(matcher.find()) {
            st.append(matcher.group(1));
            return false;
        } else {
            return true;
        }
    }
    public void testContext() throws Exception {
        System.out.println("----testContext");
        // 使用context方式
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(testUrl);
        //httpGet.setHeaders(headers);
        RequestConfig defaultRequestConfig = RequestConfig.custom()
                .setCookieSpec(CookieSpecs.BEST_MATCH)
                .setExpectContinueEnabled(true)
                .setStaleConnectionCheckEnabled(true)
                .setTargetPreferredAuthSchemes(Arrays.asList(AuthSchemes.NTLM, AuthSchemes.DIGEST))
                .setProxyPreferredAuthSchemes(Arrays.asList(AuthSchemes.BASIC))
                .build();
        RequestConfig requestConfig = RequestConfig.copy(defaultRequestConfig)
                .setSocketTimeout(5000)
                .setConnectTimeout(5000)
                .setConnectionRequestTimeout(5000)
                .setCircularRedirectsAllowed(true)
                .build();
        httpGet.setConfig(requestConfig);
        System.out.println("request line:" + httpGet.getRequestLine());
        try {
            // 执行get请求
            CloseableHttpResponse httpResponse = client.execute(httpGet,context);
            //System.out.println("context cookies:" + context.getCookieStore().getCookies());
            printResponse(httpResponse);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                // 关闭流并释放资源
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void printResponse(CloseableHttpResponse httpResponse)
            throws IOException {
        // 获取响应消息实体
        HttpEntity entity = httpResponse.getEntity();
        // 响应状态
        System.out.println("status:" + httpResponse.getStatusLine());
        System.out.println("headers:");
        HeaderIterator iterator = httpResponse.headerIterator();
        while (iterator.hasNext()) {
            System.out.println("\t" + iterator.next());
        }
        // 判断响应实体是否为空
        if (entity != null) {
            String responseString = EntityUtils.toString(entity);
            System.out.println("response length:" + responseString.length());
            System.out.println("response content:"
                    + responseString.replace("\r\n", ""));
        }
    }

    public static void setContext() {
        System.out.println("----setContext");
        context = HttpClientContext.create();
        Registry<CookieSpecProvider> registry = RegistryBuilder
                .<CookieSpecProvider> create()
                .register(CookieSpecs.BEST_MATCH, new BestMatchSpecFactory())
                .register(CookieSpecs.BROWSER_COMPATIBILITY,
                        new BrowserCompatSpecFactory()).build();
        context.setCookieSpecRegistry(registry);
        context.setCookieStore(cookieStore);
    }

    public  void setCookieStore(CloseableHttpResponse httpResponse) {
        System.out.println("----setCookieStore");
        cookieStore = new BasicCookieStore();
        Header[] headers = httpResponse.getAllHeaders();
        for (int i = 0; i <headers.length; i++) {
            System.out.println("---------------------------" + headers[i].getName() + "------" + headers[i].getValue());
            if(StringUtils.containsIgnoreCase(headers[i].getName(),"cookie")) {
                String setCookie = headers[i].getValue();
                String[] temp = setCookie.split(";");
                for (int j = 0; j < temp.length; j++) {
                    System.out.println(temp[j]);
                    String [] temp1 = temp[j].split("=");
                    BasicClientCookie cookie = null;
                    if(temp1.length==2) {
                        cookie = new BasicClientCookie(temp1[0],
                                temp1[1]);
                    }
                    else {
                        cookie = new BasicClientCookie(temp1[0],
                                "");
                    }
                    if(StringUtils.equalsIgnoreCase(temp1[0],"domain"))
                        cookie.setDomain(temp1[1]);
                    if(StringUtils.equalsIgnoreCase(temp1[0],"path"))
                        cookie.setPath(temp1[1]);
                    if(StringUtils.equalsIgnoreCase(temp1[0],"Version"))
                        cookie.setPath(temp1[1]);
                    cookie.setVersion(0);
                    cookieStore.addCookie(cookie);
                }
            }
        }

    }

    public void testCookieStore() throws Exception {
        System.out.println("----testCookieStore");
        // 使用cookieStore方式
        CloseableHttpClient client = HttpClients.custom()
                .setDefaultCookieStore(cookieStore).build();
        HttpGet httpGet = new HttpGet(testUrl);
        System.out.println("request line:" + httpGet.getRequestLine());
        try {
            // 执行get请求
            CloseableHttpResponse httpResponse = client.execute(httpGet);
            System.out.println("cookie store:" + cookieStore.getCookies());
            printResponse(httpResponse);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                // 关闭流并释放资源
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Site taobao(Site site) {
        //UcsmySite site = new UcsmySite();
        RequestConfig defaultRequestConfig = RequestConfig.custom()
                .setCookieSpec(CookieSpecs.BEST_MATCH)
                .setExpectContinueEnabled(true)
                .setStaleConnectionCheckEnabled(true)
                .setTargetPreferredAuthSchemes(Arrays.asList(AuthSchemes.NTLM, AuthSchemes.DIGEST))
                .setProxyPreferredAuthSchemes(Arrays.asList(AuthSchemes.BASIC))
                .build();
        RequestConfig requestConfig = RequestConfig.copy(defaultRequestConfig)
                .setSocketTimeout(5000)
                .setConnectTimeout(5000)
                .setConnectionRequestTimeout(5000)
                .setCircularRedirectsAllowed(true)
                .build();
        HttpPost httpost = new HttpPost("https://login.taobao.com/member/login.jhtml");
        httpost.setHeaders(headers);
        httpost.setConfig(requestConfig);
        // All the parameters post to the web site
        List<NameValuePair> nvps = getPostData();
        String url =null;
        try {
            httpost.setEntity(new UrlEncodedFormEntity(nvps,"utf-8"));
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            response = httpclient.execute(httpost);
            String content = EntityUtils.toString(response.getEntity());
            StringBuffer token = new StringBuffer();
            StringBuffer st = new StringBuffer();
            Boolean flag = needCheckCode(content);
            while(flag) {

                List<NameValuePair> nvps1 = new ArrayList<NameValuePair>();
                for(NameValuePair basicNameValuePair:nvps) {
                    if(!StringUtils.equals("TPL_checkcode", basicNameValuePair.getName())) {
                        //System.out.println(basicNameValuePair.getName()+"=" + basicNameValuePair.getValue());
                        nvps1.add(basicNameValuePair);
                    }

                }

                Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + getCheckCode(content));

                Scanner sc = new Scanner(System.in);
                nvps1.add(new BasicNameValuePair("TPL_checkcode", sc.next()));
                httpost.setEntity(new UrlEncodedFormEntity(nvps1, "UTF-8"));
                httpost.setHeaders(headers);
                httpost.setConfig(requestConfig);
                CloseableHttpResponse cresponse = httpclient.execute(httpost);
                String tokenContent = EntityUtils.toString(cresponse.getEntity());

                //检测验证码错误的正则表达式，\u9a8c\u8bc1\u7801\u9519\u8bef 是验证码错误五个字的编码
                flag = loginWithCheckCode(tokenContent,token);
                if(!flag) {
                    System.out.println("token======================" + token);
                    //System.out.println(tokenContent);
                    //setCookieStore(response);
                    //setContext();
                }
                HttpGet sthttpGet = new HttpGet("https://passport.alipay.com/mini_apply_st.js?site=3&token="+token.toString()+"&callback=stCallback6");
                sthttpGet.setHeaders(headers);
                sthttpGet.setConfig(requestConfig);
                String stcontent = httpclient.execute(sthttpGet, responseHandler);
                flag = getStByToken(stcontent,st);
                if(!flag) {
                    //setCookieStore(response);

                   // System.out.println(stcontent);
                    System.out.println("st==========================" + st);
                }
            }
            //testContext();
            url = "https://login.taobao.com/member/vst.htm?st="+st.toString()+"&TPL_username=15626241465";
            //Scanner sc = new Scanner(System.in);
            //System.out.println(sc.next());
            System.out.println("=========================================================================================");
            HttpGet getMethod = new HttpGet("https://login.taobao.com/member/vst.htm?st="+st.toString()+"&TPL_username=15626241465");
            getMethod.setConfig(requestConfig);
            //getMethod.setHeaders(headers);
            response = httpclient.execute(getMethod);
            //setCookieStore(response);
            //System.out.println(cs);
            getMethod = new HttpGet("http://me.1688.com/info/teabeibei.html?spm=0.0.0.0.IOezU8+");
            //getMethod.setHeaders(headers);
            getMethod.setConfig(requestConfig);
            response = httpclient.execute(getMethod);
            //setContext();
            //setCookieStore(response);
            //System.out.println(EntityUtils.toString(response.getEntity()));
            //setContext();
            /*
            Header[] headers = ct.getAllHeaders();
            for (int i = 0; i <headers.length; i++) {
                System.out.println("---------------------------" + headers[i].getName() + "------" + headers[i].getValue());
                if(headers[i].getName().equals("Set-Cookie")) {
                    String setCookie = headers[i].getValue();
                    String[] temp = setCookie.split(";");
                    for (int j = 0; j < temp.length; j++) {
                        System.out.println(temp[j]);
                        String [] temp1 = temp[j].split("=");
                        //System.out.println(temp1[0] + "=" + temp1[1]);
                        if(temp1.length==2)
                            site.addCookie(temp1[0],temp1[1]);
                        else {
                            site.addCookie(temp1[0],"");
                        }
                    }
                }
            }
            */
        } catch (Exception e) {
            e.printStackTrace();
            return site;
        }
        return site;
    }
}