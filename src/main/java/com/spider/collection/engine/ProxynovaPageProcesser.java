package com.spider.collection.engine;

import com.spider.collection.entity.TargetTaskDTO;
import com.spider.collection.entity.TasksDTO;
import com.spider.collection.pipeline.MySqlPipeline;
import org.apache.http.HttpHost;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.scheduler.RedisScheduler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/10/12.
 */
public class ProxynovaPageProcesser implements PageProcessor {

    private Site site = Site.me().setDomain("kuaidaili.com.com").setHttpProxy(new HttpHost("125.113.198.193",9000));

    @Override
    public void process(Page page) {
        //List<String> links = page.getHtml().links().regex("http:\\/\\/club\\.1688\\.com\\/tab_index.htm\\?.+").all();
        //page.addTargetRequests(links);//*[@id="tbl_proxy_list"]/tbody[1]/tr[2]/td[1]/span
        List params =  page.getHtml().xpath("/html/body/div[2]/div/div[2]/table/tbody/tr/td[1]/text()").all();
        String priex = "";
        for (int i = 0; i < params.size(); i++) {
            if(i>0) {
                priex = i+"";
            }
            //List<String> links = page.getHtml().links().regex("http:\\/\\/club\\.1688\\.com\\/tab_index.htm\\?.+").all();
            //page.addTargetRequests(links);//*[@id="tbl_proxy_list"]/tbody[1]/tr[2]/td[1]/span
            page.putField("hostname"+priex, page.getHtml().xpath("/html/body/div[2]/div/div[2]/table/tbody/tr/td[1]/text()").all().get(i));
            page.putField("port"+priex , page.getHtml().xpath("/html/body/div[2]/div/div[2]/table/tbody/tr/td[2]/text()").all().get(i));
            // page.putField("content", page.getHtml().$("span.row_proxy_ip").all());
//            page.putField("tags", page.getHtml().xpath("//div[@class='BlogTags']/a/text()").all());
        }
        page.addTargetRequests(page.getHtml().xpath("//*[@id=\"listnav\"]/ul").links().regex("/free/inha/\\d+/").all());
       // page.putField("content", page.getHtml().$("span.row_proxy_ip").all());
//        page.putField("tags", page.getHtml().xpath("//div[@class='BlogTags']/a/text()").all());
//        page.putField("hostname", page.getHtml().xpath("/html/body/div[2]/div/div[2]/table/tbody/tr[1]/td[1]/text()"));
//            page.putField("port" , page.getHtml().xpath("/html/body/div[2]/div/div[2]/table/tbody/tr[1]/td[2]/text()"));
    }

    @Override
    public Site getSite() {
        return site;

    }

    public static void main(String[] args) {
        TasksDTO tasks = new TasksDTO();
        tasks.setSaveTable("proxy");
        List<TargetTaskDTO> targetTasks = new ArrayList<>();
        TargetTaskDTO targetTask = new TargetTaskDTO();
        targetTask.setKey("hostname");
        targetTasks.add(targetTask);
        targetTask = new TargetTaskDTO();
        targetTask.setKey("port");
        targetTasks.add(targetTask);
        tasks.setPageSize(33);
        tasks.setPage(2);
        tasks.setTargetTaskDTOs(targetTasks);
        Request request = new Request();
        request.putExtra("$query",0);
        request.putExtra("$pageSize",10);
        request.setUrl("http://www.kuaidaili.com/free/");
        Spider.create(new ProxynovaPageProcesser())
                .addRequest(request)
//                .addUrl("http://www.kuaidaili.com/free/")
                .setScheduler(new RedisScheduler("127.0.0.1"))
                .addPipeline(new ConsolePipeline())
                .addPipeline(new MySqlPipeline(tasks))
                .run();
    }
}
