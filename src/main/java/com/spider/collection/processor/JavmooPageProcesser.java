package com.spider.collection.processor;

import com.spider.collection.entity.HelpTaskDTO;
import com.spider.collection.entity.JobDTO;
import com.spider.collection.entity.TargetTaskDTO;
import com.spider.collection.downloader.HttpClientDownloaderBack;
import com.spider.collection.entity.TasksDTO;
import com.spider.collection.pipeline.MySqlPipeline;
import com.spider.collection.util.AuthenticationUtil;
import org.apache.commons.lang.StringUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.*;


/**
 * Created by Bill on 2015/11/14.
 */
public class JavmooPageProcesser implements PageProcessor {

    private Site site = Site.me().setDomain("javmoo.info")
            .setUserAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:35.0) Gecko/20100101 Firefox/35.0")
            .setSleepTime(10000)
            .setTimeOut(10000)
            .setRetryTimes(3)
            .setCycleRetryTimes(3);

    private TasksDTO tasks;

    public TasksDTO getTasks() {
        return tasks;
    }

    public void setTasks(TasksDTO tasks) {
        this.tasks = tasks;
    }

    public JavmooPageProcesser(TasksDTO tasks) {
        this.tasks = tasks;
    }

    @Override
    public void process(Page page) {

        //List<String> links = page.getHtml().links().regex("http:\\/\\/club\\.1688\\.com\\/tab_index.htm\\?.+").all();
        //page.addTargetRequests(links);//*[@id="tbl_proxy_list"]/tbody[1]/tr[2]/td[1]/span
//        List params =  page.getHtml().xpath("//*[@id=\"waterfall\"]/div/a/div[2]/span/text()").all();
        List params = page.getHtml().xpath("//*[@id=\"waterfall\"]/div/a/div[2]/span/text()").all();
        //*[@id="doc"]/div[5]/div[1]/div[3]/div[4]/div[2]/div[1]/div[1]/a/h2
        String priex = "";
        for (int i = 0; i < params.size(); i++) {
            if (i > 0) {
                priex = i + "";
            }
            page.putField("actresses_id"+priex,page.getRequest().getExtra("id"));
            page.putField("movie" + priex, page.getHtml().xpath("//*[@id=\"waterfall\"]/div/a/div[2]/span/text()").all().get(i));
            page.putField("url" + priex, page.getHtml().xpath("//*[@id=\"waterfall\"]/div/a/@href").all().get(i));
            page.putField("designator" + priex, page.getHtml().xpath("//*[@id=\"waterfall\"]/div/a/div[2]/span/date[1]/text()").all().get(i));
            page.putField("publishdate" + priex, page.getHtml().xpath("//*[@id=\"waterfall\"]/div/a/div[2]/span/date[2]/text()").all().get(i));
        }
        String url = page.getHtml().$(".pagination>li:last-child").xpath("//a/@href").get();
        System.out.println("URL ------------------------------------------------------------ " + url);
        String temp = page.getRequest().getUrl();
        Request request = new Request();
        if (StringUtils.equalsIgnoreCase(url, temp) ||  StringUtils.isEmpty(url)|| page.getRequest().getExtra(Request.STATUS_CODE) == "404") {
            System.out.println("换下一个执行");
        }
        else {
            request.putExtra("id",page.getRequest().getExtra("id"));
            request.setUrl(url);
            page.addTargetRequest(request);
        }

    }

    @Override
    public Site getSite() {
        return site;

    }

    public void setSite(Site site) {
        this.site = site;
    }

    public static void main(String[] args) throws Exception {

        TasksDTO tasks = new TasksDTO();
        tasks.setSaveTable("tb_eds_movie");
        tasks.setSourceTable("tb_eds_actresses");
        List<TargetTaskDTO> targetTasks = new ArrayList<>();
        TargetTaskDTO targetTask = new TargetTaskDTO();
        targetTask.setKey("movie");
        targetTasks.add(targetTask);
        targetTask = new TargetTaskDTO();
        targetTask.setKey("url");
        targetTasks.add(targetTask);
        targetTask = new TargetTaskDTO();
        targetTask.setKey("designator");
        targetTasks.add(targetTask);
        targetTask = new TargetTaskDTO();
        targetTask.setKey("publishdate");
        targetTasks.add(targetTask);
        targetTask = new TargetTaskDTO();
        targetTask.setKey("actresses_id");
        targetTasks.add(targetTask);
        tasks.setPageSize(20);
        tasks.setPage(2);
        List<HelpTaskDTO> helpTasks = new ArrayList<>();
        HelpTaskDTO helpTask = new HelpTaskDTO();
        helpTask.setHttpMethod("get");
        helpTask.setHelpUrl("TABLE_");
        helpTask.setRightCode("//*[@id=\"waterfall\"]/div/a/div[2]/span/text();xpath;0");
        tasks.setTargetTaskDTOs(targetTasks);
        helpTasks.add(helpTask);
        tasks.setHelpTaskDTOs(helpTasks);
        Request request = new Request();
        request = AuthenticationUtil.getQueryData(null,request,tasks);
        JobDTO job = new JobDTO();
        job.setTasksDTO(tasks);
        job.setIsAuthentication(false);
        JavmooPageProcesser javmooPageProcesser = new JavmooPageProcesser(tasks);
        Set statucode = new HashSet();
        statucode.add(200);
        statucode.add(404);
        javmooPageProcesser.setSite(javmooPageProcesser.getSite().setAcceptStatCode(statucode));
        HttpClientDownloaderBack httpClientDownloaderBack1 = new HttpClientDownloaderBack(job,null);
        Spider.create(javmooPageProcesser)
                .addRequest(request)
                .addPipeline(new ConsolePipeline())
                .addPipeline(new MySqlPipeline(tasks))
                .setDownloader(httpClientDownloaderBack1)
                        .thread(2)
                .run();

    }
}
