package com.spider.collection.util;

import org.apache.commons.lang.StringUtils;
import org.apache.http.*;
import org.apache.http.client.CookieStore;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;

import org.apache.http.impl.client.*;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import us.codecraft.webmagic.Site;

import java.io.*;
import java.net.URLEncoder;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2015/10/14.
 */
public class HttpClientUtil {
    // The configuration items
    private static String userName = "guoa1234@163.com";
    private static String password = "qsiof0310";
    private static String redirectURL = "http://blog.renren.com/blog/304317577/449470467";
    static CookieStore cookieStore = null;
    // Don't change the following URL
    private static String renRenLoginURL = "http://www.renren.com/PLogin.do";

    // The HttpClient is used in one session
    private CloseableHttpResponse response;
    private CloseableHttpClient httpclient = HttpClientBuilder.create().build();

    public Site login(Site site,String loginUrl,Map<String,Object> map) {
        HttpPost httpost = new HttpPost(renRenLoginURL);
        // All the parameters post to the web site
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
        }

        try {
            httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
            response = httpclient.execute(httpost);
            Header[] headers = response.getAllHeaders();
            for (int i = 0; i <headers.length; i++) {
                System.out.println("---------------------------" + headers[i].getName() + "------" + headers[i].getValue());
                if(headers[i].getName().equals("Set-Cookie")) {
                    String setCookie = headers[i].getValue();
                    String[] temp = setCookie.split(";");
                    for (int j = 0; j < temp.length; j++) {
                        String [] temp1 = temp[j].split("=");
                        System.out.println(temp1[0] + "=" + temp1[1]);
                        site.addCookie(temp1[0],temp1[1]);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return site;
        } finally {
            httpost.abort();
        }
        return  site;
    }
    public boolean login() {
        HttpPost httpost = new HttpPost(renRenLoginURL);
        // All the parameters post to the web site
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("origURL", redirectURL));
        nvps.add(new BasicNameValuePair("domain", "renren.com"));
        nvps.add(new BasicNameValuePair("isplogin", "true"));
        nvps.add(new BasicNameValuePair("formName", ""));
        nvps.add(new BasicNameValuePair("method", ""));
        nvps.add(new BasicNameValuePair("submit", "登录"));
        nvps.add(new BasicNameValuePair("email", userName));
        nvps.add(new BasicNameValuePair("password", password));
        try {
            httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
            response = httpclient.execute(httpost);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            httpost.abort();
        }
        return true;
    }

    public boolean beijing() {
        HttpPost httpost = new HttpPost("https://login.taobao.com/member/login.jhtml?redirectURL=https%3A%2F%2Flogin.1688.com%2Fmember%2Fjump.htm%3Ftarget%3Dhttps%253A%252F%252Flogin.1688.com%252Fmember%252FmarketSigninJump.htm%253FDone%253Dhttp%25253A%25252F%25252Fmember.1688.com%25252Fmember%25252Foperations%25252Fmember_operations_jump_engine.htm%25253Ftracelog%25253Dlogin%252526operSceneId%25253Dafter_pass_from_taobao%252526defaultTarget%25253Dhttp%2525253A%2525252F%2525252Fwork.1688.com%2525252F%2525253Ftracelog%2525253Dlogin_target_is_blank_1688");
        // All the parameters post to the web site
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("ua",""));
        nvps.add(new BasicNameValuePair("TPL_username", "代码神童09"));
        nvps.add(new BasicNameValuePair("TPL_password", "qsiof0310?"));
        try {
            httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
            response = httpclient.execute(httpost);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            httpost.abort();
        }
        return true;
    }

    public Site taobao(Site site,String loginUrl,Map<String,Object> map) {
        HttpPost httpost = new HttpPost("https://login.taobao.com/member/login.jhtml");
        // All the parameters post to the web site
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("ua","044UW5TcyMNYQwiAiwTR3tCf0J/QnhEcUpkMmQ=|Um5OcktySnNOek57Qn1CeC4=|U2xMHDJ+H2QJZwBxX39RaFR6WnQoSS9DJFogDlgO|VGhXd1llXGVdZFltWWxSalJqXWBCekN+S3RKc097QHlDfEhxTWM1|VWldfS0RMQU9AiIeJgYoUiEKKhQ0CC17WjdMIXETPWs9|VmNDbUMV|V2NDbUMV|WGRYeCgGZhtmH2VScVI2UT5fORtmD2gCawwuRSJHZAFsCWMOdVYyVTpbPR99HWAFYVMoRSlIM141SBZPCTlZJFkgWnNMdEoBKBcvEFx1SnJNAX8Bf10gSS5ELUpoA2QBKBcvEFx1SnJMAHoDZk9wSHY4UW0eIxw+AiAcJBEtGCMeJhklBzsZZgxUK2AZNxc5F0EX|WWdHFysULQ0wECwSLRk5DTAOLhIrEi8POwY7GycZIBs7DzIPWQ8=|WmZYeCgGWjtdMVYoUn5Cf1FxXwNABCgUIAA9HSAAPwA7FUMV|W2daelQEOwM8CTwcS2VZYF5nWWBZY1doUWpTJhs5AT8FPAI6DjsBPAE0Cj8BNGNNbVEHKX8=|XGZGFjgWNgoqFSEbLhNFEw==|XWVFFTtkP3ktUStGPVsyVzpuUnxcDDgCOBgnGSZwUG1NY01tU25aZFwKXA==|XmREFDplPngsUCpHPFozVjtvU31dYUF/QnZNdCJ0|X2REFDplPngsUCpHPFozVjtvU31daUl0VGpVb1BvOW8=|QHpaCiR7IGYyTjRZIkQtSCVxTWNDfl5gX2VdaD5o|QXtbCyV6IWczSiNZI106QS15RWtLd1dpVmxWbTtt|QnhYCCZ5ImQwSSBaIF45Qi56RmhIdVVrVWpWYjRi|Q3lZCSdnM3odcRxfOUA8QRV0WnpGZlhmWWZTBVM=|RHxcDCJ9JmA0SDJfJEIrTiN3S2VFFSEULw8wCTJkRHlZd1l5R3lMdUsdSw==|RX9fDyFhNXwbdxpZP0Y6RxNyXHxBYV9hVGxVA1U=|RnxcDCJ9JmA0SDJfJEIrTiN3S2VFeVlnWWxWaz1r|R39fDyF+JWM3SzFcJ0EoTSB0SGZGFiIWLw8wCTFnR3padFp6RH1Ee0MVQw==|SHBQAC5xKmw4RD5TKE4nQi97R2lJGS0ULg4xDzRiQn9fcV9/QXpFekEXQQ==|SXBQAC5uOmIfdgtqAU0qUTQaOg41CioUKBQ0DTILNw9ZDw==|SnJSAiwCInJHfkZmWGBeCCgVNRs1FSwSKxQqfCo=|S3FRAS9wK205RT9SKU8mQy56RmhIdVVsUmtVaD5o|THZWBihoPHUSfhNQNk8zThp7VXVJaVBuVm1TBVM=|TXZWBihoPHUSfhNQNk8zThp7VXVOe1tmRn9GfUB4Lng=|TnVVBStrP3YRfRBTNUwwTRl4VnZNdlZrS3JLfkF+KH4=|T3RUBCpqPncQfBFSNE0xTBh5V3dPc1NuTndPcUt/KX8=|cEtrOxVVAUgvQy5tC3IOcydGaEh9XWBAeUJ/QHkveQ==|cUpqOhRUAEkuQi9sCnMPciZHaUlyRmZbe0J5RX1GEEY=|cklpORdXA0otQSxvCXAMcSVEakpwSGhVdUx3SX1AFkA=|c0pqOhRUAFgkTitKN28SewZnDCICNw04GCEfIgI7DjMLPmg+|dE5uPhBQBE0qRitoDncLdiJDbU1wUGhXblZvOW8=|dU9vPxE/HyICOgU8BzpsOg==|dk5uPhBPFFIGegBtFnAZfBFFeVd3JxMoEjINMwZQcE1tQ21NdUF1QX8pfw==|d01tPRM9HSEBOgc6BjlvOQ==|eEJiMhxDGF4KdgxhGnwVcB1JdVt7R2dcYVxjWQ9Z|eUJiMhxDGF4KdgxhGnwVcB1JdVt7T3VVaEhzT3NMdyF3|ekFhMR9AG10JdQ9iGX8Wcx5Kdlh4RHlFZVh4Q39Be0YQRg==|e0BgMB5BGlwIdA5jGH4Xch9Ld1l5RXhHZ1p6QX1GfUMVQw==|fEdnNxlGHVsPcwlkH3kQdRhMcF5+Sn9fYkJ5RXBOcyVz|fUZmNhhHHFoOcghlHngRdBlNcV9/Q35BYVx8R3hFf0IUQg==|fkVlNRtEH1kNcQtmHXsSdxpOclx8SH1dYEB7RHtOey17|f0RkNBpFHlgMcApnHHoTdhtPc119QXxBYVx8R3hAeUMVQw==|YFt7KwVaAUcTbxV4A2UMaQRQbEJiVmxMcVFqVW9VYTdh|YVp6KgRbAEYSbhR5AmQNaAVRbUNjX2Jff0JiWWdaZlkPWQ==|Yll5KQdYA0URbRd6AWcOawZSbkBgXGFefkNjWGZZZ18JXw==|Y1h4KAZZAkQQbBZ7AGYPagdTb0FhXWBcfEFhWmRdZFoMWg==|ZF19LQNcB0EVbAV/BXscZwtfY01tWWNcfEN3T29UbVZjWQ9Z|ZV19LQNcB0EVbAV/BXscZwtfY01tPQkzDy8QJB9JaVR0WnRUb1ZsWWQyZA==|Zlx8LAJdBkAUaBJ/BGILbgNXa0VlWHhDek9zTxlP|Z119LQNcB0EVbAV/BXscZwtfY01tUXFKc0Z/QhRC|aFFxIQ9QC00ZYAlzCXcQawdTb0FhVGBdfUJ2QmJZbVlmWw1b|aVFxIQ9PG1I1WTR3EWgUaT1cclICNw8yEiwUKX9fYkJsQmJYZFtkXghe|alBwIA5RCkwYYQhyCHYRagZSbkBgXX1He0R9RxFH|a1FxIQ9PG1I1WTR3EWgUaT1cclJuTnRId0xwJnA=|bFR0JApKHlcwXDFyFG0RbDhZd1cHMgk0FCoSL3lZZERqRGReYV5gWQ9Z|bVR0JApKHkY7UjRYMUombwhkCV04FjYKNwoyEiwVKwsxCDEFOW85|blZ2JghIHEQ5UDZaM0gkbQpmCyUFVWlUbFZ2SHFLa1dqU29NdVVrVW1Pd1dpUnJNeC4OMxM9XSZLJRU8Az0TMwkxDzIOWA4=|b1V1JQtLH1YxXTBzFWwQbTlYdlZrS3FJd0twJnA=|ECoKWnQ0YDhFLEomTzRYEXYad1l5RWVfZ1lmUwVT|ESsLW3U1YTlELUsnTjVZEHcbdlh4RWVfZ1ltWA5Y|EigIWHY2YitMIE0OaBFtEEQlCysXNw01DDELXQs=|EygIWHY2YitMIE0OaBFtEEQlCysePgMjGSwWLBNFEw==|FC8PX3ExZSxLJ0oJbxZqF0MiDCwZOQQkHisfIRtNGw==|FS4OXnAwZC1KJksIbhdrFkIjDS0YOAUlHysXKxdBFw==|Fi0NXXMzZy5JJUgLbRRoFUEgDi4bOwYmHCgXLBFHEQ==|FywMXHIyZi9IJEkKbBVpFEAhDy8aOgcnHSkXIxZAFg==|GCMDU309aSBHK0YFYxpmG08uACAVNQgoEiYeIBRCFA==|GSAAUH4hejxoEXgCeAZhGnYiHjAQJBwmBjkDOxsuEyofKnwq|GiICUnwjeD5qE3oAegRjGHQgHDISQnZDfV1iVmo8HCEBLwEhFCkcIhhOGA==|GyEBUX8/ayJFKUQHYRhkGU0sAiIfPwo3AjkDVQM=|HCYGVngnfDpuF34EfgBnHHAkGDYWKgo/AjcCOmw6|HSUFVXtVdSUQLho6BD0HUXFMbEJsTHlFcEVwJnA=|HiQEVHolfjhsFXwGfAJlHnImGjQUKQk8ADQLNmA2|HyUFVXs7byZBLUADZRxgHUkoBiYaOg8wDTEFUwU=|ADoaSmQkcDleMl8cegN/AlY3GTkEJBEoFSgcShw=|ATsbS2VLa1Z2Q3pHeUAWQA==|AjsbS2UlcSlUPVs3XiVJAGcLZjJXeVlsWGZGeEB1VWBbYFxmMGY=|AzsbS2VLazsONwsrFS0TRWVYeFZ4WG1XalJtO20=|BD4eTmBOblJyR31AdUsdSw==|BT8fT2EhdTxbN1oZfwZ6B1MyHDwAIBUvEycfSR8=|Bj0dTWMjdz5ZNVgbfQR4BVEwHj4LNxcqCj8KNA8zZTM=|BzwcTGIidj9YNFkafAV5BFAxHz8KPh4jAzYDOAY9az0=|CDMTQ20teTBXO1YVcwp2C18+EDALMBAtDTgNOQU9az0=|CTISQmwseDFWOlcUcgt3Cl4/ETEEPh4jAzYCPAA8ajw=|CjMTQ20teTBXO1YVcwp2C18+EDAFPAgoFi0SMgY6DzsCVAI=|CzMTQ20teTBXO1YVcwp2C18+EDBgVWxYeEZ+RxExDCwCLAw4BzkGOG44|DDcXR2kpfTRTP1IRdw5yD1s6FDQBIRw8CDYJNQlfCQ==|DTYWRmgofDVSPlMQdg9zDlo7FTUNNBQpCT0EOQQ5bzk=|DjYWRmgofCRYMlc2SxNuB3obcF5+LhojFjYPNwtdfUBgTmBAdEx3T3Ikcg==|DzUVRWsrfzZRPVATdQxwDVk4FjYLKx8nHCgRRxE=|MAoqelQUQBhkDmsKdy9SO0YnTGJCfl5qUmhUaz1r|MQg1CCgVNQoqFi8TMw01Dy8XIwM5ASEdIRg4BCQYIx4+CjcXKRw8AD4eIBs7BDsbJRw8AjwcIh09Az8fJAQ7Dy8QJQU7GyQEJRAwDS0SLAwzDCwZOQMjGDgAIBk5BycGOg8vEyoKNggoCTUKKhYqCjYLKwozEzIKKgswEDELKwo/Hz4KKgs3CioLNwsrCjYJKQg0CioLNw4uDzEJKQg3DS0MNRU0DCwNMwcnBjgEJAUwEDEFJQQ6AyMCOwIiAz0GJgc7BSUEOAEhADwEJAU5AiIDPQAgAT8DIwI8AyMCPAIiAz0EJAU7AyMCOgEhADkMLA0yCioLNA8vDjYKKgs0ASEAPwsrCjQJKQg2CioLMAgoCTELKwo0DS0MMgoqCzIGJgc/AiIDOwcnBj4BIQA6BCQFPgYmBz8HJwY+BSUEPwoqCzIKKgsyCV8="));
        nvps.add(new BasicNameValuePair("TPL_username", "15626241465"));
        nvps.add(new BasicNameValuePair("TPL_password_2", "7590ea30adcb62dda1d441042a79c1da3f7f6d8a32958812ab43616d3126e108af52bf2b4fc7d1e84adcc33b8ad3317965d9d07246d3fddb934e851b47293893ae466e3c45f0be091a91d19a10a4ae7509bfaef0b3dc331d969cd1da6784e3b967b785457fcf121e87980ee6bf38bdd9cb5d0241abea2324e16e4c8243db9782"));
        nvps.add(new BasicNameValuePair("TPL_checkcode",""));
        nvps.add(new BasicNameValuePair("loginsite","3"));
        nvps.add(new BasicNameValuePair("newlogin","0"));
        nvps.add(new BasicNameValuePair("TPL_redirect_url","https://login.1688.com/member/jump.htm?target=https%3A%2F%2Flogin.1688.com%2Fmember%2FmarketSigninJump.htm%3FDone%3Dhttp%253A%252F%252Fmember.1688.com%252Fmember%252Foperations%252Fmember_operations_jump_engine.htm%253Ftracelog%253Dlogin%2526operSceneId%253Dafter_pass_from_taobao%2526defaultTarget%253Dhttp%25253A%25252F%25252Fwork.1688.com%25252F%25253Ftracelog%25253Dlogin_target_is_blank_1688"));
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

        try {
            httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
            response = httpclient.execute(httpost);
            //System.out.println(response.getEntity().getContentLength());
            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(),"gb2312"));
            String data = null;
            String checkCode = "<div class=\"field field-checkcode \" id=\"l_f_code\" >";
            String url = null;
            while((data=reader.readLine()) != null) {
                //当条件满足时，将返回true，否则返回false
                Pattern pattern = Pattern.compile("<img id=\"J_StandardCode_m.*?data-src=\"(.*?)\"");
                Matcher matcher = pattern.matcher(data);
                while(matcher.find()){
                    Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + matcher.group(1));
                }
            }
            List<NameValuePair> nvps1 = new ArrayList<NameValuePair>();
            for(NameValuePair basicNameValuePair:nvps) {
                if(!StringUtils.equals("TPL_checkcode",basicNameValuePair.getName())) {
                    //System.out.println(basicNameValuePair.getName()+"=" + basicNameValuePair.getValue());
                    nvps1.add(basicNameValuePair);
                }

            }

            Scanner sc = new Scanner(System.in);
            nvps1.add(new BasicNameValuePair("TPL_checkcode", sc.next()));
            httpost.setEntity(new UrlEncodedFormEntity(nvps1, HTTP.UTF_8));
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            String test = httpclient.execute(httpost,responseHandler);
            response = httpclient.execute(httpost);
            reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(),"gb2312"));
            String token = null;
            while((data=reader.readLine()) != null) {
                //当条件满足时，将返回true，否则返回false
                Pattern pattern = Pattern.compile("<input type=\"hidden\" id=\"J_HToken.*?value=\"(.*?)\"");
                Matcher matcher = pattern.matcher(data);
                while(matcher.find()){
                    //Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + matcher.group(1));
                    token = matcher.group(1);
                }
                System.out.println(new String(data.getBytes()));
            }
            System.out.println("token======================" + token);
            /*
            HttpPost post = new HttpPost("https://login.taobao.com/member/login.jhtml");
            // All the parameters post to the web site
            List<NameValuePair> nvps2 = new ArrayList<NameValuePair>();
            nvps2.add(new BasicNameValuePair("",""));
            response = httpclient.execute(post);
            Header[] headers = response.getAllHeaders();
            for (int i = 0; i <headers.length; i++) {
                System.out.println("---------------------------" + headers[i].getName() + "------" + headers[i].getValue());
                if(headers[i].getName().equals("Set-Cookie")) {
                    String setCookie = headers[i].getValue();
                    String[] temp = setCookie.split(";");
                    for (int j = 0; j < temp.length; j++) {
                        String [] temp1 = temp[j].split("=");
                        System.out.println(temp1[0] + "=" + temp1[1]);
                        site.addCookie(temp1[0],temp1[1]);
                    }
                }
            }
            */
            //getText("http://me.1688.com/info/teabeibei.html?spm=0.0.0.0.w3Bcst");
            //https://passport.alipay.com/mini_apply_st.js?site=0&token=1Lgyss-3MTC_Vnbx8xgh0-Q&callback=stCallback7
            String url1 = "https://passport.alipay.com/mini_apply_st.js?site=0&token="+token+"&callback=stCallback7";
            System.out.println(url1);
            HttpGet httpget = new HttpGet(url1);
            ResponseHandler<String> responseHandler1 = new BasicResponseHandler();
            String st = httpclient.execute(httpget,responseHandler1);
            System.out.println("st======================"+st);

        } catch (Exception e) {
            e.printStackTrace();
            return site;
        } finally {
            httpost.abort();
        }
        return site;
    }

    public static String getUTF8XMLString(String xml) {
        // A StringBuffer Object
        StringBuffer sb = new StringBuffer();
        sb.append(xml);
        String xmString = "";
        String xmlUTF8="";
        try {
            xmString = new String(sb.toString().getBytes("UTF-8"));
            xmlUTF8 = URLEncoder.encode(xmString, "UTF-8");
            //System.out.println("utf-8 编码：" + xmlUTF8) ;
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // return to String Formed
        return xmlUTF8;
    }

    private String getRedirectLocation() {
        Header locationHeader = response.getFirstHeader("Location");
        if (locationHeader == null) {
            return null;
        }
        return locationHeader.getValue();
    }

    public  String getText(String redirectLocation) {
        HttpGet httpget = new HttpGet(redirectLocation);
        //CloseableHttpClient httpclient = HttpClientBuilder.create().build();
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        String responseBody = "";
        try {
            responseBody = httpclient.execute(httpget, responseHandler);
        } catch (Exception e) {
            e.printStackTrace();
            responseBody = null;
        } finally {
            httpget.abort();
            httpclient.getConnectionManager().shutdown();
        }
        return responseBody;
    }

    public static void setCookieStore(HttpResponse httpResponse) {
        System.out.println("----setCookieStore");
        cookieStore = new BasicCookieStore();
        // JSESSIONID
        String setCookie = httpResponse.getFirstHeader("Set-Cookie")
                .getValue();
        String JSESSIONID = setCookie.substring("JSESSIONID=".length(),
                setCookie.indexOf(";"));
        System.out.println("JSESSIONID:" + JSESSIONID);
        // 新建一个Cookie
        BasicClientCookie cookie = new BasicClientCookie("JSESSIONID",
                "abcFZsVF5A5kC-dPUMLbv");

        // cookie.setAttribute(ClientCookie.VERSION_ATTR, "0");
        // cookie.setAttribute(ClientCookie.DOMAIN_ATTR, "127.0.0.1");
        // cookie.setAttribute(ClientCookie.PORT_ATTR, "8080");
        // cookie.setAttribute(ClientCookie.PATH_ATTR, "/CwlProWeb");
        cookieStore.addCookie(cookie);
    }

    public void printText() {
        if (login()) {
            //String redirectLocation = getRedirectLocation();
            //if (redirectLocation != null) {
            System.out.println(getText("https://login.taobao.com/member/vst.htm?st=1G1njxx3YHrWIV8ywdxoxSA&TPL_username=15626241465"));
                System.out.println(getText("http://me.1688.com/info/teabeibei.html?spm=0.0.0.0.w3Bcst"));
            //}
        }
    }

    public HttpResponse getResponse() {
        return response;
    }

    public void setResponse(CloseableHttpResponse response) {
        this.response = response;
    }

    public static void main(String[] args) throws IOException {

        HttpClientUtil renRen = new HttpClientUtil();
        Site site = new Site();
        Map<String,Object> map = new HashMap<String,Object>();
        //renRen.taobao(site, "",map);

       // System.out.println(response.toString());
        renRen.printText();
        String str = "<img id=\"J_StandardCode_m\" src=\"https://assets.alicdn.com/apps/login/static/img/blank.gif\" data-src=\"https://pin.aliyun.com/get_img?sessionid=fc41836c168f20a806d1939b48ec2b30&identity=taobao.login&type=bak_150_40&dt=1445416117260\"  class=\"check-code-img\"";
        Pattern pattern = Pattern.compile("<img id=\"J_StandardCode_m.*?data-src=\"(.*?)\"");
        Matcher matcher = pattern.matcher(str);
        while(matcher.find()){
            System.out.println("Group 0:"+matcher.group(0));//得到第0组——整个匹配
            System.out.println("Group 1:"+matcher.group(1));//得到第一组匹配——与(or)匹配的
            //System.out.println("Group 2:"+matcher.group(2));//得到第二组匹配——与(ld!)匹配的，组也就是子表达式
            //System.out.println("Group 3:"+matcher.group(3));//得到第二组匹配——与(ld!)匹配的，组也就是子表达式
            //System.out.println("Group 4:"+matcher.group(4));//得到第二组匹配——与(ld!)匹配的，组也就是子表达式
            //System.out.println("Group 5:"+matcher.group(5));//得到第二组匹配——与(ld!)匹配的，组也就是子表达式
            System.out.println("Start 0:"+matcher.start(0)+" End 0:"+matcher.end(0));//总匹配的索引
            System.out.println("Start 1:"+matcher.start(1)+" End 1:"+matcher.end(1));//第一组匹配的索引
            //System.out.println("Start 2:"+matcher.start(2)+" End 2:"+matcher.end(2));//第二组匹配的索引
            System.out.println(str.substring(matcher.start(0),matcher.end(1)));//从总匹配开始索引到第1组匹配的结束索引之间子串——Wor
        }
    }
}
