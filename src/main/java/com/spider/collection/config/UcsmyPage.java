package com.spider.collection.config;

import com.spider.collection.entity.JobDTO;
import com.spider.collection.entity.TargetTaskDTO;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.selector.Selectable;

import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by Administrator on 2015/10/12.
 */
public class UcsmyPage extends Page {

    private static Logger logger = LoggerFactory.getLogger(UcsmyPage.class);

    public static Page getPageFromJson(Page page, JobDTO job) {

        try {
            //page.addTargetRequests(page.getHtml().links().regex("http://my\\.oschina\\.net/flashsword/blog/\\d+").all());
            Selectable selectable = null;
            Request request = page.getRequest();
            Method method;
            List<TargetTaskDTO> tasks = job.getTasksDTO().getTargetTaskDTOs();
            Object id = null;
            int k= 0;
            for (TargetTaskDTO task : tasks) {
                String[] params = StringUtils.split(task.getExpress(), ";");
                if (job.getTasksDTO().getContentType() == 0)
                    selectable = page.getHtml();
                else if (job.getTasksDTO().getContentType() == 1) {
                    selectable = page.getJson();
                }
                if(StringUtils.startsWith(task.getMethod(),"SELF_")) {
                    request = page.getRequest();
                }

                for (int i = 0; i < params.length; i++) {
                    String temp = StringUtils.substring(params[i], params[i].indexOf("(") + 1, params[i].lastIndexOf(")"));
                    String[] select = StringUtils.split(temp, "$");
                    List<Class> clses = new ArrayList<>();
                    for (int j = 0; j < select.length; j++) {
                        try {
                            Integer.parseInt(select[j]);
                            clses.add(Integer.class);
                        } catch (Exception e) {
                            clses.add(String.class);
                        }
                    }
                    if(StringUtils.startsWith(task.getMethod(),"SELF_")) {
                        method = request.getClass().getMethod(StringUtils.substring(params[i], 0, params[i].indexOf("(")), clses.toArray(new Class[clses.size()]));
                        id = method.invoke(request, select);
                    } else {
                        method = selectable.getClass().getMethod(StringUtils.substring(params[i], 0, params[i].indexOf("(")), clses.toArray(new Class[clses.size()]));
                        selectable = (Selectable) method.invoke(selectable, select);
                    }
                }
                if (task.getFlag()) {
                    if (task.getIsMost()) {
                        List params1 = selectable.all();
                        int pageSize = params1.size();
                        Map tmp = page.getResultItems().getAll();
                        if(k > 0) {
                            pageSize = Integer.parseInt(page.getRequest().getExtra("$pageSize") != null ?page.getRequest().getExtra("$pageSize").toString():null);
                        } else {
                            page.getRequest().putExtra("$pageSize",pageSize);
                        }
                        String priex = "";
                        for (int i = 0; i < pageSize; i++) {
                            if (i > 0) {
                                priex = i + "";
                            }
                            if(StringUtils.startsWith(task.getMethod(),"SELF_")) {
                                page.putField(task.getKey() + priex, id);
                            } else {
                                page.putField(task.getKey() + priex, params1.get(i<params1.size()?i:params1.size()-1));
                            }
                        }
                    } else {
                        if(StringUtils.startsWith(task.getMethod(),"SELF_")) {
                            page.putField(task.getKey(),id);
                        } else {
                            page.putField(task.getKey(), selectable.get());
                        }
                    }
                } else {
                    if (task.getIsMost()) {
                        page.addTargetRequests(selectable.all());
                    } else {

                        String url = selectable.get();
                        logger.info("URL ------------------------------------------------------------ " + url);
                        String temp = page.getRequest().getUrl();
                    }
                }
                k++;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return page;
    }
}
