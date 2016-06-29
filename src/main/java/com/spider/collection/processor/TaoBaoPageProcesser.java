package com.spider.collection.processor;

import com.spider.collection.entity.TargetTaskDTO;
import com.spider.collection.entity.TasksDTO;
import com.spider.collection.pipeline.MySqlPipeline;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.scheduler.RedisScheduler;

import java.util.ArrayList;
import java.util.List;

/**
 * @author code4crafter@gmail.com <br>
 */
public class TaoBaoPageProcesser implements PageProcessor {

    private Site site = Site.me().setDomain("1688.com");

    @Override
    public void process(Page page) {
        //List<String> links = page.getHtml().links().regex("http:\\/\\/club\\.1688\\.com\\/tab_index.htm\\?.+").all();
        //page.addTargetRequests(links);//*[@id="tbl_proxy_list"]/tbody[1]/tr[2]/td[1]/span
        List params =  page.getHtml().xpath("//*[@id=\"doc\"]/div[5]/div[1]/div[3]/div/div[2]/div[1]/div[1]/a/h2/text()").all();
        //*[@id="doc"]/div[5]/div[1]/div[3]/div[4]/div[2]/div[1]/div[1]/a/h2
        String priex = "";
        for (int i = 0; i < params.size(); i++) {
            if(i>0) {
                priex = i+"";
            }
            //List<String> links = page.getHtml().links().regex("http:\\/\\/club\\.1688\\.com\\/tab_index.htm\\?.+").all();
            //page.addTargetRequests(links);//*[@id="tbl_proxy_list"]/tbody[1]/tr[2]/td[1]/span
            page.putField("clubname"+priex, page.getHtml().xpath("//*[@id=\"doc\"]/div[5]/div[1]/div[3]/div/div[2]/div[1]/div[1]/a/h2/text()").all().get(i));
            page.putField("cluburl"+priex , page.getHtml().xpath("//*[@id=\"doc\"]/div[5]/div[1]/div[3]/div/div[2]/div[1]/div[1]/a/@href").all().get(i));
            // page.putField("content", page.getHtml().$("span.row_proxy_ip").all());
//            page.putField("tags", page.getHtml().xpath("//div[@class='BlogTags']/a/text()").all());
        }
        page.addTargetRequests(page.getHtml().xpath("//*[@id=\"doc\"]/div[5]/div[1]/div[4]/div").$(".qui-next").links().all());
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
        tasks.setSaveTable("tb_eds_club");
        List<TargetTaskDTO> targetTasks = new ArrayList<>();
        TargetTaskDTO targetTask = new TargetTaskDTO();
        targetTask.setKey("clubname");
        targetTasks.add(targetTask);
        targetTask = new TargetTaskDTO();
        targetTask.setKey("cluburl");
        targetTasks.add(targetTask);
        tasks.setPageSize(20);
        tasks.setPage(2);
        tasks.setTargetTaskDTOs(targetTasks);
        Spider.create(new TaoBaoPageProcesser())
//                .addUrl("http://club.1688.com/tab_index.htm?c=0")
//                .addUrl("http://club.1688.com/tab_index.htm?c=1")
//                .addUrl("http://club.1688.com/tab_index.htm?c=2")
//                .addUrl("http://club.1688.com/tab_index.htm?c=3")
//                .addUrl("http://club.1688.com/tab_index.htm?c=4")
//                .addUrl("http://club.1688.com/tab_index.htm?c=5")
//                .addUrl("http://club.1688.com/tab_index.htm?c=6")
//                .addUrl("http://club.1688.com/tab_index.htm?c=7")
//                .addUrl("http://club.1688.com/tab_index.htm?c=8")
                .addUrl("http://club.1688.com/tab_index.htm?c=9")
                .addPipeline(new ConsolePipeline())
                .setScheduler(new RedisScheduler("127.0.0.1"))
//                .addPipeline(new MySqlPipeline(tasks))
                .thread(100)
                .start();
    }
}
