package com.spider.collection.scheduler;

import com.spider.collection.entity.CollectionData;
import com.spider.collection.entity.JobDTO;
import com.spider.collection.entity.Link;
import com.spider.collection.entity.TasksDTO;
import com.spider.collection.main.SpiderApplication;
import com.spider.collection.pipeline.HttpPipline;

import com.spider.collection.util.AuthenticationUtil;
import com.spider.collection.util.HttpUtil;
import com.spider.collection.util.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import us.codecraft.webmagic.Request;

import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.scheduler.DuplicateRemovedScheduler;
import us.codecraft.webmagic.scheduler.MonitorableScheduler;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Administrator on 2015/12/8.
 */
public class HttpScheduler extends DuplicateRemovedScheduler implements MonitorableScheduler {
    private BlockingQueue<Request> queue = new LinkedBlockingQueue();
    private Logger logger = LoggerFactory.getLogger(getClass());
    private String uuid;
    private SpiderApplication spiderApplication;
    private double verion = 2.6;
    private static int countFlag = 0;

    public Logger getLogger() {
        return logger;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }


    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public SpiderApplication getSpiderApplication() {
        return spiderApplication;
    }

    public void setSpiderApplication(SpiderApplication spiderApplication) {
        this.spiderApplication = spiderApplication;
    }

    public HttpScheduler(TasksDTO tasks) {
        initQueue();
    }

    public HttpScheduler(String uuid) {
        this.uuid = uuid;
        initQueue();
    }

    public void initQueue() {
        boolean flag = true;
//        while (flag) {
        try {
//            if(JDBCHelper.getJdbcTemplate("spider") == null)
//                jdbcTemplate = JDBCHelper.createMysqlTemplate("spider", "jdbc:mysql://10.1.20.31:3306/test?characterEncoding=utf-8", "root", "root", 1, 2);
//            else {
//                jdbcTemplate = JDBCHelper.getJdbcTemplate("spider");
//            }
//            HttpUtil httpUtil = HttpUtil.getInstance();
            AuthenticationUtil authenticationUtil = new AuthenticationUtil();
            JobDTO jobDTO = (JobDTO) SpiderApplication.getJobMap().get("job");
            if (StringUtils.isEmpty(jobDTO.getTasksDTO().getSourceTable())) {
                return;
            }
            Link results = null;

            ObjectMapper objectMapper = new ObjectMapper();
//            String link = httpUtil.doGetForString(PropertyUtils.get("link.url") + "/" + jobDTO.getTasksDTO().getSourceTable() + "/" + jobDTO.getTasksDTO().getCount());
            String link = authenticationUtil.getText(PropertyUtils.get("link.url") + "/" + jobDTO.getTasksDTO().getSourceTable() + "/" + jobDTO.getTasksDTO().getCount());
//                if(org.apache.commons.lang.StringUtils.isEmpty(link)) {
//                    logger.info("服务器更新中，喝杯咖啡，休息5分钟再开始工作");
//                    Thread.sleep(300000);
//                    continue;
//                }
            results = objectMapper.readValue(link, Link.class);
//                if (results.getLinks().size() == 0) {
//                    Random random = new Random();
//                    int num = random.nextInt(5) + 1;
//                    logger.info("任务已经完成了，休息" + 1/2.0 * num + "分钟再开始新的任务");
//                    Thread.sleep(30000 * num);
////                    if (spiderApplication != null) {
//                    spiderApplication = new SpiderApplication();
//                    spiderApplication.getJob(jobDTO.getId() + "");
////                    }
//                    flag = true;
//                    authenticationUtil.close();
//                    continue;
//                } else {
//                    flag = false;
//                }
            if (results == null) {
                return;
            }
            if (spiderApplication == null) {
                spiderApplication = new SpiderApplication();
            }
            if (results.getLinks().size() == 0) {
                SpiderApplication.getJobMap().put("$count",0);
                spiderApplication.postData();
                logger.info("任务已经完成,休息10秒！");
                Thread.sleep(10000);
                spiderApplication.getJob(jobDTO.getId() + "");
            } else {
                Map<String, CollectionData> collectionDataMap = HttpPipline.getCollectionDataMap();
                CollectionData collectionData = null;
                if (collectionDataMap == null) {
                    collectionDataMap = new HashMap<>();
                    collectionData = new CollectionData();
                } else {
                    collectionData = collectionDataMap.get("collectionData");
                }

                collectionData.setSourceLink(results.getSourceLink());
                collectionDataMap.put("collectionData", collectionData);
                HttpPipline.setCollectionDataMap(collectionDataMap);
                List<Map<String,Object>> resultLinks = results.getLinks();
                SpiderApplication.getJobMap().put("$count",resultLinks.size());
                SpiderApplication.getJobMap().put("$sourceLink",results.getSourceLink());
                for (Map<String, Object> result :resultLinks) {
                    Request request = new Request();
                    request.setUrl(result.get("url") != null ? result.get("url").toString() : "");
                    for (Map.Entry<String, Object> data : result.entrySet()) {
                        request.putExtra(data.getKey(), data.getValue());
                    }
                    request.putExtra("$update", 0);
                    request.putExtra("$query", 0);
                    request.putExtra("$table", result);
                    pushWhenNoDuplicate(request, null);
                }
                flag = false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            logger.info("服务器更新中，喝杯咖啡，休息5分钟再开始工作");
            try {
                Thread.sleep(30000);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        } finally {
        }
//        }
    }

    public void pushWhenNoDuplicate(Request request, Task task) {
        this.queue.add(request);
    }

    public synchronized Request poll(Task task) {
        Request request = this.queue.poll();
//        boolean flag = true;
//        Random random = new Random();
//        ObjectMapper objectMapper = new ObjectMapper();
        JobDTO jobDTO = (JobDTO) SpiderApplication.getJobMap().get("job");


        while (request == null) {
//            AuthenticationUtil authenticationUtil = new AuthenticationUtil();
            if (StringUtils.isEmpty(jobDTO.getTasksDTO().getSourceTable())) {
                return request;
            }
//            logger.info("=================" + getLeftRequestsCount(task));
            try {
//                Map<String, CollectionData> collectionDataMap = HttpPipline.getCollectionDataMap();
//                if (collectionDataMap != null) {
//                    CollectionData collectionData = collectionDataMap.get("collectionData");
//                    if (collectionData.getDatas().size() == 0 && collectionData.getLinks().size() == 0 && collectionData.getRequests().size() == 0) {
//                        logger.info("没有数据");
//                    } else {
//                        String msg = "";
////                        Map postData = new HashMap();
//                        String json = objectMapper.writeValueAsString(collectionData);
//                        System.out.println(json.length() / 1024 / 1024 + "M");
//                        long startTime = System.currentTimeMillis();
//                        logger.info("开始加密进行传输！");
////                        postData.put("content",new Endecrypt().get3DESEncrypt(json, PropertyUtils.get("spider.spkey")));
//                        String temp = new Endecrypt().get3DESEncrypt(json, PropertyUtils.get("spider.spkey"));
//                        long endTime = System.currentTimeMillis();
//                        logger.info("加密所需时间" + (endTime - startTime));
//                        logger.info("开始提交数据，请等待，喝杯咖啡再回来！");
//                        msg = authenticationUtil.postDataMsg(temp, PropertyUtils.get("save.url"));
//                        endTime = System.currentTimeMillis();
//                        logger.info("提交数据所花时间" + (endTime - startTime));
//                        HttpPipline.setCollectionDataMap(null);
//                    }
//                }
                if (spiderApplication == null)
                    spiderApplication = new SpiderApplication();
//                spiderApplication.getJob(jobDTO.getId() + "");
//                spiderApplication.update(jobDTO.getId() + "" );

                Map<String, CollectionData> collectionDataMap = HttpPipline.getCollectionDataMap();
                CollectionData collectionData = null;
                if (collectionDataMap == null) {
                    collectionDataMap = new HashMap<>();
                    collectionData = new CollectionData();
                } else {
                    collectionData = collectionDataMap.get("collectionData");
                }
//                int count = collectionData.getRequests().size();
//                if (getLeftRequestsCount(null) == 0 && count!=0 && count != jobDTO.getTasksDTO().getCount()) {
//                    Request request1 = new Request();
//                    Map<String,Object> result = collectionData.getRequests().get(0);
//                    request.setUrl(result.get("url") != null ? result.get("url").toString() : "");
//                    for (Map.Entry<String, Object> data : result.entrySet()) {
//                        request.putExtra(data.getKey(), data.getValue());
//                    }
//                    request.putExtra("$update", 0);
//                    request.putExtra("$query", 0);
//                    request.putExtra("$table", result);
//                    pushWhenNoDuplicate(request, null);
//                    request.putExtra(Request.CYCLE_TRIED_TIMES, 1);
//                    request.putExtra("$UCSMY_PIP", "$UCSMY");
//                    push(request, null);
//                } else {
                Boolean flag = false;
                int tmpCount = Integer.parseInt(SpiderApplication.getJobMap().get("$count").toString());
                if (collectionData.getRequests().size() >= tmpCount || countFlag >= 10 ) {
                    flag = spiderApplication.postData();
                    countFlag = 0;
//                    tmpCount = 0;
                    SpiderApplication.getJobMap().put("$count",0);
                } else if (tmpCount != 0 ) {
                    if (request==null && getLeftRequestsCount(null) == 0 && collectionData.getRequests().size() < tmpCount) {
                        request = new Request();
                        request.putExtra("$UCSMY_IGNORE", "$UCSMY_IGNORE");
//                        request.putExtra(Request.CYCLE_TRIED_TIMES, 1);
                        request.setUrl("http://www.baidu.com");
                        request.setMethod("get");
                        request.putExtra("$UCSMY_PIP", "$UCSMY");
                        pushWhenNoDuplicate(request,null);
                        countFlag ++;
//                        logger.info("任务已经完成，喝杯咖啡，休息10秒钟再开始工作");
//                        Thread.sleep(10000);
                    }
                }
                if (flag || tmpCount == 0) {
                    initQueue();
                }
                request = this.queue.poll();
//                }
            } catch (Exception e) {
                e.printStackTrace();
//                logger.info("服务器更新中，喝杯咖啡，休息30秒钟再开始工作");
//                try {
//                    Thread.sleep(30000);
//                } catch (Exception e1) {
//                    e1.printStackTrace();
//                }
            } finally {
//                authenticationUtil.close();
            }
        }

//        if(getLeftRequestsCount(task) == 0) {
//            try{
//                CollectionData collectionData = HttpPipline.getCollectionDataMap().get("collectionData");
//                HttpPipline.setCollectionDataMap(null);
//                initQueue();
//                int num = random.nextInt(10) + 1;
//                System.out.println("");
////                Thread.sleep(60000 * (random.nextInt(10)+1));
//            }catch (Exception e) {
//                e.printStackTrace();
//            }
//        }

        return request;
    }


    public int getLeftRequestsCount(Task task) {
        return this.queue.size();
    }

    public int getTotalRequestsCount(Task task) {
        return this.getDuplicateRemover().getTotalRequestsCount(task);
    }

    public void clear() {
        this.queue.clear();
    }
}
