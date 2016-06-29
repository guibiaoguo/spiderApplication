package com.spider.collection.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.utils.HttpConstant;

import java.util.Map;

/**
 * Created by ucs_yuananyun on 2016/4/6.
 */
public class CrawlerRequestUtils {
    /**
     * 创建一个Post请求
     *
     * @param targetUrl
     * @param params
     * @return
     */
    public  static Request createRequest(String targetUrl, String method, Map<String, String> params) {
        Request targetRequest = new Request(targetUrl);
        if (HttpConstant.Method.POST.equals(method)) {
            targetRequest.setMethod(HttpConstant.Method.POST);
            if (params != null && params.size() > 0) {
                NameValuePair[] nameValuePairs = new NameValuePair[params.size()];
                int i = 0;
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    nameValuePairs[i++] = new BasicNameValuePair(entry.getKey(), entry.getValue());
                }
                targetRequest.putExtra("nameValuePair", nameValuePairs);
            }
        } else if (HttpConstant.Method.GET.equals(method)) {
            targetRequest.setMethod(HttpConstant.Method.GET);
            if (params != null && params.size() > 0) {
                StringBuilder urlBuilder = new StringBuilder(targetUrl);
                if (!targetUrl.contains("?") && !StringUtils.endsWith(targetUrl,"/") && StringUtils.isNotEmpty(targetUrl))
                    urlBuilder.append("?");

                for (Map.Entry<String, String> entry : params.entrySet()) {
                    if(StringUtils.isEmpty(entry.getKey()))
                        urlBuilder.append(entry.getValue());
                    else
                        urlBuilder.append("&").append(entry.getKey()).append("=").append(entry.getValue());
                }
                targetRequest.setUrl(urlBuilder.toString());
            }
        }
        return targetRequest;
    }


}
