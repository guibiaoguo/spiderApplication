package com.spider.collection.processor;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.spider.collection.entity.CollectionData;
import com.spider.collection.entity.JobDTO;
import com.spider.collection.entity.TargetTaskDTO;
import com.spider.collection.entity.UcsmySite;
import com.spider.collection.main.SpiderApplication;
import com.spider.collection.pipeline.HttpPipline;
import com.spider.collection.util.CrawlerRequestUtils;
import com.spider.collection.util.DateUtils;
import com.spider.collection.util.LoggerUtils;
import com.spider.collection.util.MapBuilder;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Json;
import us.codecraft.webmagic.selector.JsonPathSelector;
import us.codecraft.webmagic.selector.PlainText;
import us.codecraft.webmagic.selector.Selectable;

import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by Administrator on 2015/10/12.
 */
public class UcsmyPageProcessor implements PageProcessor {
    private Logger logger = LoggerUtils.getLogger(getClass());
    private Site site;

    public UcsmyPageProcessor() {
        site = (UcsmySite) SpiderApplication.getJobMap().get("site");
    }

    @Override
    public void process(Page page) {

        //page.addTargetRequests(page.getHtml().links().regex("http://my\\.oschina\\.net/flashsword/blog/\\d+").all());
        Request request = page.getRequest();

        if (request.getExtra("$query") != null && StringUtils.equals(request.getExtra("$query").toString(), "1")) {
            return;
        }
        JobDTO job = (JobDTO) SpiderApplication.getJobMap().get("job");
        Selectable selectable = null;
        Map<String, CollectionData> collectionDataMap = HttpPipline.getCollectionDataMap();
        CollectionData collectionData = null;
        if (collectionDataMap == null) {
            collectionDataMap = new HashMap<>();
            collectionData = new CollectionData();
        } else {
            collectionData = collectionDataMap.get("collectionData");
        }
        Map tempMap = (Map) request.getExtra("$table");
        if (request.getExtra("$UCSMY_PIP") == null) {
            if(tempMap != null ) {
                if (tempMap.containsKey("$count"))
                    tempMap.remove("$count");
                collectionData.getRequests().add(tempMap);
            }
        }
        else {
            System.out.println("test");
        }
        Method method;
        List<TargetTaskDTO> tasks = job.getTasksDTO().getTargetTaskDTOs();
        Object id = null;
        for (TargetTaskDTO task : tasks) {
            try {
                String[] params = StringUtils.split(task.getExpress(), ";");
                if (job.getTasksDTO().getContentType() == 0)
                    selectable = page.getHtml();
                else if (job.getTasksDTO().getContentType() == 1 || job.getTasksDTO().getContentType() == 2) {
                    selectable = page.getJson();
                    if (StringUtils.startsWith(selectable.get(), "[")) {
                        StringBuffer buffer = new StringBuffer().append("{\"rows\":").append(selectable.get()).append("}");
                        selectable = new Json(buffer.toString());
                    }
//                    selectable = selectable.regex("jQuery[0-9]{21}_[0-9]{13}\\((.*)\\)");
//                    selectable = new Json(selectable.get());
//                    selectable = selectable.jsonPath("$..cardNum");
//                    List<Selectable> objs  = selectable.nodes();
////                    selectable = objs.get(0);
//                    selectable = selectable.jsonPath("$..INDEX_KEY");
                }
                if (StringUtils.startsWith(task.getMethod(), "SELF_")) {
                    request = page.getRequest();
                }

                //用表达式执行数据
                for (int i = 0; i < params.length; i++) {
                    String temp = StringUtils.substring(params[i], params[i].indexOf("(") + 1, params[i].lastIndexOf(")"));
                    String[] select = temp.split("\\$\\$");
                    for (int k = 0;k<select.length;k++) {
                        if(select[k].equals(" ") || StringUtils.isEmpty(select[k]))
                            select[k]="";
                    }
                    Object[] parVal = new Object[select.length];
                    List<Class> clses = new ArrayList<>();
                    for (int j = 0; j < select.length; j++) {
                        try {
                            parVal[j] = Integer.parseInt(select[j]);
                            clses.add(int.class);
                        } catch (Exception e) {
                            if (StringUtils.startsWith(temp, "JsonPathSelector")) {
                                clses.add(JsonPathSelector.class);
                            } else {
                                parVal[j] = select[j];
                                clses.add(String.class);
                            }
                        }
                    }
                    try {
                        if (StringUtils.startsWith(task.getMethod(), "SELF_")) {
                            method = request.getClass().getMethod(StringUtils.substring(params[i], 0, params[i].indexOf("(")), clses.toArray(new Class[clses.size()]));
                            id = method.invoke(request, parVal);
                        } else if (StringUtils.startsWith(params[i], "regex") && job.getTasksDTO().getContentType() == 1) {
                            selectable = selectable.regex(select[0]);
                            selectable = new Json(selectable.get());
                        } else if (StringUtils.startsWith(task.getMethod(), "EXPRESS_")) {
                            id = params[0];
                        } else if (StringUtils.startsWith(task.getMethod(), "DATE_")) {
                            id = DateUtils.currtimeToString14();
                        } else if (StringUtils.startsWith(task.getMethod(), "EMPTY_")) {
                            id = "";
                        } else if(StringUtils.startsWith(params[i], "split")) {
                            List<String> list = new ArrayList<>();
                            Object object = selectable.all();
                            if (object instanceof List) {
                                List<Object> items = (List<Object>) object;
                                int count = (Integer) parVal[1];
                                for (Object item : items) {
                                    String tmp = String.valueOf(item).split(parVal[0].toString())[count];
                                    list.add(String.valueOf(tmp));
                                }
                            } else {
                                list.add(String.valueOf(object));
                            }
                            selectable = new PlainText(list);
                        } else if (org.apache.commons.lang3.StringUtils.startsWith(params[i], "saveFile")) {
                            CloseableHttpClient httpClient = page.getResultItems().get("$httpclient");
//                    HttpGet httpget = new HttpGet(selectable.get());
//                    httpget.setConfig(RequestConfig.custom().setProxy(new HttpHost("127.0.0.1", 8888)).build());
//                    httpget.setHeader(new BasicHeader("Cookie", "proxy_token=" + page.getHtml().xpath("//script").regex("proxy_token=(.*?);")));
//                    CloseableHttpResponse response = httpClient.execute(httpget);
                            List<String> list = new ArrayList<>();
                            ObjectMapper objectMapper = new ObjectMapper();
                            Map headers = new HashMap();
//                            headers.put("Content-Type","image/jpeg");
                            for (String url:selectable.all()) {
                                byte[] bytes = saveFile(httpClient,url.trim(),headers);
                                list.add(objectMapper.writeValueAsString(bytes));
                            }
                            selectable = new PlainText(list);
                        } else {
                            //获取方法方法名调用
                            method = selectable.getClass().getMethod(StringUtils.substring(params[i], 0, params[i].indexOf("(")), clses.toArray(new Class[clses.size()]));

                            selectable = (Selectable) method.invoke(selectable, parVal);
                        }
                    } catch ( Exception e) {
                        selectable = new PlainText("");
                        logger.info("参数不存在 " + e.getMessage() );
                    }

                }
//                if (task.getFlag()) {
                if (task.getIsMost()) {
                    int count = 0;
                    List params1 = new ArrayList();
                    if (job.getTasksDTO().getContentType() == 2) {
                        if (selectable != null) {
                            String[] strs = selectable.toString().split(",");
                            for (int i = count; i < strs.length; i++) {
                                String str = strs[i].replace("|", ",").replace("\"", "");
                                params1.add(str);
                            }
                        }
                    } else if (StringUtils.startsWith(task.getMethod(),"POSITION_")){
                        count = Integer.parseInt(StringUtils.substringAfter(task.getMethod(),"POSITION_")) - 1 ;
                        List<String> tmp = selectable.all();
                        for (int i = count; i < tmp.size(); i++) {
                            params1.add(tmp.get(i));
                        }
                    } else if (StringUtils.startsWith(task.getMethod(),"LAST_")){
                        List<String> tmp = selectable.all();
                        params1.add(tmp.get(tmp.size() - 1));
                    } else {
                        params1 = selectable.all();
                    }
                    Map tmp = page.getResultItems().getAll();
//                    if (k > 0) {
//                        pageSize = Integer.parseInt(page.getRequest().getExtra("$pageSize") != null ? page.getRequest().getExtra("$pageSize").toString() : null);
//                    } else {
//                        page.getRequest().putExtra("$pageSize", pageSize);
//                    }
                    int oldPageSize = page.getRequest().getExtra(task.getSaveTable())!=null?Integer.parseInt(page.getRequest().getExtra(task.getSaveTable()).toString()):0;
                    page.getRequest().putExtra(task.getSaveTable(),params1.size()>oldPageSize?params1.size():oldPageSize);
                    String priex = "";
                    for (int i = 0; i < params1.size(); i++) {
                        if (i > 0) {
                            priex = i + "";
                        }
//                        int tempnum = i;
                        if (StringUtils.startsWith(task.getMethod(), "SELF_")) {
                            page.putField(task.getKey() + priex, id);
                        } else if (StringUtils.startsWith(task.getMethod(), "EXPRESS_")) {
                            page.putField(task.getKey() + priex, id);
                        } else if (StringUtils.startsWith(task.getMethod(), "DATE_")) {
                            page.putField(task.getKey() + priex, id);
                        } else if (StringUtils.startsWith(task.getMethod(), "EMPTY_")) {
                            page.putField(task.getKey() + priex, id);
                        } else if (StringUtils.startsWith(task.getMethod(), "REQUEST_")) {
                            String tmp1 = StringUtils.trim(params1.get(i).toString());
                            if(StringUtils.isNotEmpty(tmp1)) {
//                                request.putExtra(StringUtils.substringAfter(task.getKey(),"$UCSMY_"),tmp1);
//                                request.putExtra("$UCSMY_PIP", "$UCSMY");
                                Request restRequest = CrawlerRequestUtils.createRequest(job.getTasksDTO().getHelpTaskDTOs().get(job.getTasksDTO().getHelpTaskDTOs().size() - 1).getHelpUrl(),request.getMethod().toUpperCase(), MapBuilder.instance().put(StringUtils.substringAfter(task.getKey(),"$UCSMY_"),tmp1).map());
                                request.putExtra("$UCSMY_PIP", "$UCSMY");
                                page.addTargetRequest(restRequest);
                            }
                        }else {
                            if (job.getTasksDTO().getContentType() == 2) {
//                                tempnum = i < params1.size() ? i : params1.size() - 1;
                                String str[] = params1.get(i).toString().split(",");
                                if (str.length > Integer.valueOf(task.getMethod())) {
                                    page.putField(task.getKey() + priex, str[Integer.valueOf(task.getMethod())]);
                                }

                            } else {
//                                tempnum = i < params1.size() ? i : params1.size() - 1;
                                page.putField(task.getKey() + priex, StringUtils.trim(params1.get(i).toString()));
                            }

                        }
                    }
                } else {
                    page.getRequest().putExtra(task.getSaveTable(),1);
                    if (StringUtils.startsWith(task.getMethod(), "SELF_")) {
                        page.putField(task.getKey(), id);
                    } else if (StringUtils.startsWith(task.getMethod(), "EXPRESS_")) {
                        page.putField(task.getKey(), id);
                    } else if (StringUtils.startsWith(task.getMethod(), "DATE_")) {
                        page.putField(task.getKey(), id);
                    } else if (StringUtils.startsWith(task.getMethod(), "EMPTY_")) {
                        page.putField(task.getKey(), id);
                    } else if (StringUtils.startsWith(task.getMethod(), "REQUEST_") && StringUtils.isNotEmpty(selectable.get())) {
//                        request.putExtra(StringUtils.substringAfter(task.getKey(),"$UCSMY_"),StringUtils.trim(selectable.get()));
                        Request restRequest = CrawlerRequestUtils.createRequest(job.getTasksDTO().getHelpTaskDTOs().get(job.getTasksDTO().getHelpTaskDTOs().size() - 1).getHelpUrl(),request.getMethod().toUpperCase(), MapBuilder.instance().put(StringUtils.substringAfter(task.getKey(),"$UCSMY_"),selectable.get()).map());
                        restRequest.putExtra("$UCSMY_PIP", "$UCSMY");
                        page.addTargetRequest(restRequest);
                    }else {
                        page.putField(task.getKey(), StringUtils.join(selectable.all(),","));
                    }
                }
//                }
//            else {
//                    if (task.getIsMost()) {
//                        page.addTargetRequests(selectable.all());
//                    } else {
//
//                        String url = selectable.get();
//                        System.out.println("URL ------------------------------------------------------------ " + url);
//                        String temp = page.getRequest().getUrl();
//                        if (StringUtils.equalsIgnoreCase(url, temp) ||  StringUtils.isEmpty(url)|| page.getRequest().getExtra(Request.STATUS_CODE) == "404") {
//                            logger.info("换下一个执行");
////                            request = AuthenticationUtil.getQueryData(null, request, job.getTasks());
////                            page.addTargetRequest(request);
//                        }
//                        else {
//                            request.putExtra("$update","1");
//                            request.setUrl(url);
//                            page.addTargetRequest(request);
//                        }
//                    }
//                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private byte[] saveFile(CloseableHttpClient httpClient,String url,Map<String,String> headers) throws Exception{
        HttpGet httpget = new HttpGet(url);
//        httpget.setConfig(RequestConfig.custom().setProxy(new HttpHost("127.0.0.1", 8888)).build());
        for (Map.Entry<String, String> header : headers.entrySet()) {
            httpget.setHeader(new BasicHeader(header.getKey(),header.getValue().toString()));
        }
        CloseableHttpResponse response = httpClient.execute(httpget);
        byte[] bytes = EntityUtils.toByteArray(response.getEntity());
        return bytes;
    }

    @Override
    public Site getSite() {
        return site;
    }
}
