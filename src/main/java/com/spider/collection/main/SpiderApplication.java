package com.spider.collection.main;

import com.spider.collection.entity.CollectionData;
import com.spider.collection.entity.JobDTO;
import com.spider.collection.entity.ProxyDTO;
import com.spider.collection.entity.UcsmySite;
import com.spider.collection.processor.UcsmyPageProcessor;
import com.spider.collection.util.*;
import com.spider.collection.downloader.HttpClientDownloaderBack;
import com.spider.collection.pipeline.HttpPipline;
import com.spider.collection.scheduler.HttpScheduler;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import us.codecraft.webmagic.Spider;

import java.io.*;
import java.net.URLEncoder;
import java.util.*;

/**
 * Created by Administrator on 2015/10/12.
 */
public class SpiderApplication {

    private Logger logger = LoggerUtils.getLogger(SpiderApplication.class);
    private Spider spider;
    private static Map<String, Object> jobMap;

    private static String uuid;
    private double verion = 3.1;
    public String count;
    public String collection;
    /**
     * 获取任务
     *
     * @param jobId
     */
    public void getJob(String jobId) {
        //
        boolean flag = true;
        ObjectMapper objectMapper = new ObjectMapper();
        while (flag) {

            try {
//                HttpUtil httpUtil = HttpUtil.getInstance();
                AuthenticationUtil authenticationUtil = new AuthenticationUtil();
//                String spiderJson = httpUtil.doGetForString(PropertyUtils.get("spider.version.url") + verion);
                String spiderJson = authenticationUtil.getText(PropertyUtils.get("spider.version.url") + verion);
                Map maps = objectMapper.readValue(spiderJson, Map.class);
                File backfile = new File(System.getProperty("user.dir") + "/spiderApplication.exe.bak");
                backfile.delete();
                if (StringUtils.equalsIgnoreCase(maps.get("success").toString(), "true")) {
                    File file = new File(System.getProperty("user.dir") + "/spiderApplication.exe");
                    backfile = new File(System.getProperty("user.dir") + "/spiderApplication.exe.bak");
                    file.renameTo(backfile);
                    logger.info("版本更新，请休息一下，喝杯咖啡吧，再起来工作,当前版本为" + verion);
//                    authenticationUtil.saveExe(maps.get("url").toString(), maps.get("name").toString());
                    backfile.delete();
                    ProcessBuilder pb = new ProcessBuilder();
                    pb.directory(new File(System.getProperty("user.dir")));
                    pb.command(maps.get("name").toString());
                    pb.redirectErrorStream(false);
                    Process process = pb.start();
                    InputStream is = process.getInputStream();
                    InputStreamReader isr = new InputStreamReader(is, "GBK");
                    BufferedReader br = new BufferedReader(isr);
                    String line;
                    while ((line = br.readLine()) != null) {
//                        System.out.println(line);
                    }
                }
//                postData(objectMapper,authenticationUtil);
                String regditserver = PropertyUtils.get("spider.url");
                if (jobMap == null) {
                    jobMap = new HashedMap();
                }
                count = jobMap.get("count") != null ? jobMap.get("count").toString() : "";
                collection = jobMap.get("collection") != null ? jobMap.get("collection").toString() : "";
                if (StringUtils.isNotEmpty(uuid) && StringUtils.isNotEmpty(jobId) && StringUtils.isNotEmpty(count))
                    regditserver += "/" + uuid + "/" + jobId + "/" + count + "?info=" + URLEncoder.encode(collection, "utf-8") + "&agentID=" + PropertyUtils.get("AgentID") + "&version=" + verion;
                else {
                    regditserver += "?agentID=" + PropertyUtils.get("AgentID") + "&version=" + verion;
                }
                Map post = new HashMap();
//            post.put("info", RuntimeTest.getOsInfo());
//                spiderJson = httpUtil.doGetForString(regditserver);
                spiderJson = authenticationUtil.getText(regditserver);
                if (StringUtils.isEmpty(spiderJson)) {
//                    spiderJson = httpUtil.doGetForString(regditserver + "?agentID=" + PropertyUtils.get("AgentID"));
                    spiderJson = authenticationUtil.getText(regditserver + "?agentID=" + PropertyUtils.get("AgentID"));
                    logger.info("服务器更新中，喝杯咖啡，休息5分钟再开始工作");
                    Thread.sleep(30000);
                    continue;
                }
                logger.info(spiderJson);
                maps = objectMapper.readValue(spiderJson, Map.class);
                //
                if (StringUtils.isEmpty(spiderJson) || !StringUtils.equals(maps.get("code").toString(), "200")) {
                    logger.info(maps.get("msg").toString());
                    Thread.sleep(300);
//                    spiderJson = httpUtil.doGetForString(regditserver);
                    spiderJson = authenticationUtil.getText(regditserver);
                    maps = objectMapper.readValue(spiderJson, Map.class);
                }
                uuid = maps.get("uuid").toString();
                String joburl = maps.get("url").toString();
//                String jobJson = httpUtil.doGetForString(joburl + "/" + PropertyUtils.get("job.url") + "/" + uuid);
                String jobJson = authenticationUtil.getText(joburl + "/" + PropertyUtils.get("job.url") + "/" + uuid);
                if (StringUtils.isEmpty(jobJson)) {
                    logger.info("目前没有工作，喝杯咖啡，休息5分钟再开始工作");
                    Thread.sleep(30000);
                    continue;
                }
                objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
//                Map map = objectMapper.readValue(jobJson,Map.class);
                JobDTO job = objectMapper.readValue(jobJson, JobDTO.class);
                jobMap.put("job", job);
                jobMap.put("thread", job.getThread());
                flag = false;
                UcsmySite site = new UcsmySite().setSleepRandom(job.isSleepRandom());
//                List<ProxyDTO> proxys = job.getProxyDTOs();
//                List<String[]> httpProxyList = new ArrayList<String[]>();
//                for (int i = 0; i < 1; i++) {
//                    String[] httpProxy = new String[2];
//                    ProxyDTO proxy = proxys.get(i);
//                    httpProxy[0] = proxy.getProxyHost();
//                    httpProxy[1] = proxy.getProxyPort();
//                    httpProxyList.add(httpProxy);
//                }
//                site.getHttpProxyPool().enable(true);
//                site.setHttpProxyPool(httpProxyList);
//                site.setHttpProxyPool(Lists.<String[]>newArrayList(new String[]{"101.201.197.222","3128"}));
                Set statucode = new HashSet();
                statucode.add(200);
//                statucode.add(404);
//                statucode.add(502);
//                statucode.add(521);
//                statucode.add(302);
                site.setDomain(job.getDomain()).setCharset(job.getCharset()).setCycleRetryTimes(3)
                        .setSleepTime(job.getSleepTime())
                        .setAcceptStatCode(statucode)
                        .setTimeOut(job.getTimeOut()).setUserAgent(job.getUserAgent());
                Map<String, Object> map = job.getHeader();
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    site.addHeader(entry.getKey(), entry.getValue().toString());
//                if(entry.getKey() == "Cookie")
//                    site.addCookie(entry.getKey().toString(),entry.getValue().toString());
                }
                jobMap.put("site", site);
            } catch (Exception e) {
                e.printStackTrace();
                logger.info("服务器更新中，喝杯咖啡，休息5分钟再开始工作");
                try {
                    Thread.sleep(300000);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            } finally {
//                authenticationUtil.close();
            }
        }
    }

    public boolean postData() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
//            HttpUtil httpUtil = HttpUtil.getInstance();
            AuthenticationUtil authenticationUtil = new AuthenticationUtil();
            Map<String, CollectionData> collectionDataMap = HttpPipline.getCollectionDataMap();
            CollectionData collectionData = collectionDataMap.get("collectionData");
            if (collectionDataMap != null && collectionData.getRequests().size() > 0) {
//            if (collectionData.getRequests().size() == jobDTO.getTasksDTO().getCount() ) {
//                logger.info("没有数据");
//            } else {
                String msg = "";
//                        Map postData = new HashMap();
                String json = null;
                collectionData.setSourceLink(jobMap.get("$sourceLink").toString());
                json = objectMapper.writeValueAsString(collectionData);

//                        File dataFile = new File(PropertyUtils.get("spider.path") + "/" + DateUtils.currtimeToString12());
//                        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(dataFile, true));
//                        bufferedWriter.write(json + "\n");
//                        bufferedWriter.flush();
//                        bufferedWriter.close();
                logger.info("**********************上传数据是" + json.length() / 1024.0 + "KB *********************");
                long startTime = System.currentTimeMillis();
                logger.info("开始加密进行传输！");
//                        postData.put("content",new Endecrypt().get3DESEncrypt(json, PropertyUtils.get("spider.spkey")));
//                        String temp = new Endecrypt().get3DESEncrypt(URLEncoder.encode(json,"utf-8"), PropertyUtils.get("spider.spkey"));
                json = com.spider.collection.util.Base64.encodeBytes(json.getBytes("utf-8"));
                String temp = URLEncoder.encode(URLEncoder.encode(json, "utf-8"), "utf-8");
                long endTime = System.currentTimeMillis();
                logger.info("加密所需时间" + (endTime - startTime));
                logger.info("开始提交数据，请等待，喝杯咖啡再回来！");
                msg = authenticationUtil.postDataMsg(temp, PropertyUtils.get("save.url"));
//                Map data = new HashMap();
//                data.put("content",temp);
//                httpUtil.doPostForString(PropertyUtils.get("save.url"),data);
                endTime = System.currentTimeMillis();
                logger.info("提交数据所花时间" + (endTime - startTime));
                if(StringUtils.isNotEmpty(msg)) {
                    SpiderApplication.getJobMap().put("count", objectMapper.readValue(msg, Map.class).get("count"));
                    SpiderApplication.getJobMap().put("collection", objectMapper.readValue(msg, Map.class).get("collection"));
                }
//                jobDTO = (JobDTO) SpiderApplication.getJobMap().get("job");
//                SpiderApplication.getJobMap().put("thread",jobDTO.getThread());
//                int k = 0;
//                        while (org.apache.commons.lang.StringUtils.isEmpty(msg)) {
//                            k++;
//                            if(k == 10 ) {
//                                logger.info("网络异常严重，已经超过10次不成功，放弃本任务，任务放弃提交");
//                                break;
//                            } else {
//                                logger.info("网络异常，请等待一分钟后提交");
//                            }
//                            Thread.sleep(60000);
//                            startTime = System.currentTimeMillis();
//                            msg = authenticationUtil.postDataMsg(temp,PropertyUtils.get("save.url"));
//                            endTime = System.currentTimeMillis();
//                            logger.info("提交数据所花时间" + (endTime - startTime));
//                        }
                HttpPipline.getCollectionDataMap().put("collectionData",new CollectionData());
                return true;
            }
//        }
        } catch (Exception e) {
            logger.info("提交数据出现了异常{}",e.getMessage());
            e.printStackTrace();
        } finally {
//            authenticationUtil.close();
        }
        return false;
    }

    /**
     * 爬虫初始化
     */
    public Spider init() {
        try {
            HttpPipline.getCollectionDataMap().put("collectionData",new CollectionData());
            HttpScheduler httpScheduler = new HttpScheduler(uuid);
            HttpClientDownloaderBack httpClientDownloaderBack = new HttpClientDownloaderBack(httpScheduler);
            spider = Spider.create(new UcsmyPageProcessor())
                    //从https://github.com/code4craft开始f
                    .setDownloader(httpClientDownloaderBack)
//                    .addRequest(httpScheduler.poll(null))
//                            .addRequest(datacenterScheduler.poll(null))
//                            .addRequest(request)
//                            .addUrl("http://me.1688.com/info/xiaomei0522.html?spm=0.0.0.0.uWHLPE")
                    //设置Scheduler，使用Redis来管理URL队列
//                            .setScheduler(new RedisScheduler("10.1.20.31"))
                    //设置Pipeline，将结果以json方式保存到文件
                    //.addPipeline(new CsvFilePipline(job.getTasks(),"D:/data/webmagic",job.getName()+".json"))
                    // .addPipeline(new HttpPipline())
//                    .addPipeline(new ConsolePipeline())
                    .addPipeline(new HttpPipline(httpScheduler))
//                     .addPipeline(new MySqlPipeline(job.getTasksDTO()))
//                    .setScheduler(datacenterScheduler)
                    .setScheduler(httpScheduler)
                    .thread(Integer.parseInt(jobMap.get("thread").toString()))
//            .thread(5)
            ;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return spider;
    }

    public static void main(String[] args) throws Exception {
        if (args.length > 0) {
            PropertyUtils.setType(true);
            PropertyUtils.setDefaultProperties(args[0].split("=")[1]);
        }
        SpiderApplication spiderApplication = new SpiderApplication();
        spiderApplication.getJob("");
        spiderApplication.init().start();

    }

    public Spider getSpider() {
        return spider;
    }

    public void update(String jobId) {
//        spider.clearPipeline();
//        spider.stop();
        getJob(jobId);
//        spider.thread(Integer.parseInt(jobMap.get("thread").toString())).start();
        init().start();
        if (spider != null)
            spider.close();
    }

    public Logger getLogger() {
        return logger;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    public void setSpider(Spider spider) {
        this.spider = spider;
    }

    public static Map getJobMap() {
        return jobMap;
    }

    public static void setJobMap(Map jobMap) {
        SpiderApplication.jobMap = jobMap;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}


