package com.spider.collection.pipeline;

import com.spider.collection.entity.*;
import com.spider.collection.main.SpiderApplication;
import com.spider.collection.scheduler.HttpScheduler;
import com.spider.collection.util.AuthenticationUtil;
import com.spider.collection.util.Base64;
import com.spider.collection.util.Endecrypt;
import com.spider.collection.util.PropertyUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;
import us.codecraft.webmagic.scheduler.Scheduler;

import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Administrator on 2015/11/4.
 */
public class HttpPipline implements Pipeline {

    private static Map<String, CollectionData> collectionDataMap = new HashMap<>();

    private HttpScheduler scheduler;

    private Logger logger = LoggerFactory.getLogger(getClass());

    public HttpPipline(HttpScheduler httpScheduler) {
        this.scheduler = httpScheduler;
    }

    public static Map<String, CollectionData> getCollectionDataMap() {
        return collectionDataMap;
    }

    public static void setCollectionDataMap(Map<String, CollectionData> collectionDataMap) {
        HttpPipline.collectionDataMap = collectionDataMap;
    }


    @Override
    public void process(ResultItems resultItems, Task task) {
        JobDTO jobDTO = (JobDTO) SpiderApplication.getJobMap().get("job");
        TasksDTO tasks = jobDTO.getTasksDTO();
        Map map = resultItems.getAll();
        Boolean flag = false;
        List<TargetTaskDTO> targetTasks = tasks.getTargetTaskDTOs();
        if (resultItems.getRequest().getExtra("$query") != null && StringUtils.equals(resultItems.getRequest().getExtra("$query").toString(), "1")) {
            return;
        }
//        int pageSize = Integer.parseInt(resultItems.getRequest().getExtra("$pageSize") != null ? resultItems.getRequest().getExtra("$pageSize").toString() : "1");
        CollectionData collectionData = collectionDataMap.get("collectionData");
//        if (collectionDataMap == null) {
//            collectionDataMap = new HashMap<>();
//            collectionData = new CollectionData();
//        } else {
//            collectionData = ;
//        }
        Map<String, BlockingDeque<Map>> datas = collectionData.getDatas();
        Map<String, BlockingDeque<Map>> links = collectionData.getLinks();
//        List<Map> requests = collectionData.getRequests();
        Boolean j = false, k = false, insertFlag = false, linkFlag = false;
        Map<String, BlockingDeque<Map>> data = new HashedMap();
        Map<String, BlockingDeque<Map>> link = new HashedMap();
        int pageSize = 0, oldPageSize = 0;
        for (TargetTaskDTO targetTaskDTO : targetTasks) {
            if (StringUtils.isEmpty(targetTaskDTO.getSaveTable()))
                continue;
            BlockingDeque<Map> dataQuery = data.get(targetTaskDTO.getSaveTable()) != null ? data.get(targetTaskDTO.getSaveTable()) : new LinkedBlockingDeque();
            BlockingDeque<Map> linkQuery = link.get(targetTaskDTO.getSaveTable()) != null ? link.get(targetTaskDTO.getSaveTable()) : new LinkedBlockingDeque();
            pageSize = Integer.parseInt(resultItems.getRequest().getExtra(targetTaskDTO.getSaveTable()).toString());
            if (pageSize != oldPageSize) {
                j = false;
                k = false;
                insertFlag = false;
                linkFlag = false;
            }
            for (int i = 0; i < pageSize; i++) {
                flag = false;
                String priex = "";
                if (i > 0) {
                    priex = i + "";
                }
                String param = map.get(targetTaskDTO.getKey() + priex) != null ? map.get(targetTaskDTO.getKey() + priex).toString() : map.get(targetTaskDTO.getKey()) != null ? map.get(targetTaskDTO.getKey()).toString() : "";

                if (targetTaskDTO.getFlag()) {
                    Map tmpData = new HashedMap();
                    if (j && insertFlag && dataQuery.size() > 0) {
                        tmpData = dataQuery.poll();
                    } else {
//                        tmpData = new HashedMap();
                        j = true;
                    }
                    tmpData.put(targetTaskDTO.getKey(), param);
                    try {
                        dataQuery.putLast(tmpData);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    Map tmpLink = new HashedMap();
                    if (k && linkFlag && linkQuery.size() > 0) {
                        tmpLink = linkQuery.poll();

                    } else {
//                        tmpLink = new HashedMap();
                        k = true;
                    }
                    tmpLink.put(targetTaskDTO.getKey(), param);
                    try {
                        linkQuery.putLast(tmpLink);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            insertFlag = j;
            linkFlag = k;
            oldPageSize = pageSize;
            if (dataQuery.size() > 0)
                data.put(targetTaskDTO.getSaveTable(), dataQuery);
            if (linkQuery.size() > 0)
                link.put(targetTaskDTO.getSaveTable(), linkQuery);
        }
        for (Map.Entry<String, BlockingDeque<Map>> dataTmp : data.entrySet()) {
            BlockingDeque<Map> tmp = datas.get(dataTmp.getKey());
            if (tmp == null) {
                datas.put(dataTmp.getKey(), dataTmp.getValue());
            } else {
                tmp.addAll(dataTmp.getValue());
            }
        }
        for (Map.Entry<String, BlockingDeque<Map>> linkTmp : link.entrySet()) {
            BlockingDeque<Map> tmp = links.get(linkTmp.getKey());
            if (tmp == null) {
                links.put(linkTmp.getKey(), linkTmp.getValue());
            } else {
                tmp.addAll(linkTmp.getValue());
            }
        }
//        if (requests == null) {
//            requests = new ArrayList<>();
//        }

//        collectionData.setRequests(requests);
        collectionData.setDatas(datas);
        collectionData.setLinks(links);
//        collectionDataMap.put("collectionData", collectionData);
//        int flagPip = Integer.parseInt(SpiderApplication.getJobMap().get("thread").toString());
//        if(scheduler.getLeftRequestsCount(null) == 0 && flagPip > 0) {
//            for (int i = 0; i < flagPip; i++) {
//                Request request = resultItems.getRequest();
//                request.putExtra(Request.CYCLE_TRIED_TIMES,1);
//                request.putExtra("$UCSMY_PIP","$UCSMY");
//                scheduler.push(request,null);
//            }
//            flagPip --;
//            SpiderApplication.getJobMap().put("thread",flagPip);
//        }

//        int count = requests.size();
//        if (scheduler.getLeftRequestsCount(null) == 0 && count < jobDTO.getTasksDTO().getCount()) {
//            Request request = resultItems.getRequest();
//            request.putExtra(Request.CYCLE_TRIED_TIMES, 1);
//            request.putExtra("$UCSMY_PIP", "$UCSMY");
//            request.putExtra("$UCSMY_IGNORE", "$UCSMY_IGNORE");
//            scheduler.push(request, null);
//        }
    }


}
