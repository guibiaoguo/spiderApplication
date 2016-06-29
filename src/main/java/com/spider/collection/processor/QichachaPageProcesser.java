package com.spider.collection.processor;

import com.spider.collection.entity.JobDTO;
import com.spider.collection.downloader.HttpClientDownloaderBack;
import com.spider.collection.util.AuthenticationUtil;
import com.spider.collection.util.HttpUtil;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by Bill on 2015/11/8.
 */
public class QichachaPageProcesser implements PageProcessor{
    private Site site = Site.me().setDomain("www.qichacha.com").setUserAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:35.0) Gecko/20100101 Firefox/35.0");
    @Override
    public void process(Page page) {
        //page.addTargetRequest(page.getHtml().xpath("/html/body/section[2]/div/div/div[1]/ul/li[1]/div/div/h3/a/@href").toString());
        //page.putField("content", page.getHtml().$("div#content").toString());
        //page.putField("login-box-warp", page.getHtml().$("div.login-box-warp").toString());
        page.putField("注册信息", page.getHtml().$("#detail-1").toString());
        page.putField("股东信息", page.getHtml().$("#detail-2").toString());
        page.putField("主要人员", page.getHtml().$("#detail-3").toString());
        page.putField("分支机构", page.getHtml().$("#detail-4").toString());
        page.putField("变更记录", page.getHtml().$("#detail-5").toString());
        page.putField("regex1", page.getHtml().$("#detail-1 ul").regex("住所.*</label>(.*?)<a"));
        page.putField("regex2", page.getHtml().$("#detail-1 ul>li").regex("经营范围.*</label>.*>(.*)</div>"));
        page.addTargetRequest(page.getUrl().toString());
//        Request request = new Request();
//        request.setMethod("get");
//        request.setUrl("http://qichacha.com/firm_SH_b4d62089f6bc701ca3e2839683bd153");
//        page.addTargetRequest(request);
        //page.addTargetRequests(page.getHtml().links().regex("http:\\/\\/me\\.1688\\.com\\/info\\/[a-z A-Z 0-9]*\\.htm").all());
        //page.putField("address", page.getHtml().xpath("//*[@id=\"content\"]/div[2]/div/div[2]/div/div[1]/div[1]/div[1]/div/div[2]/div[7]/span[2]/text()").toString());
        //page.putField("tags",page.getHtml().xpath("//div[@class='BlogTags']/a/text()").all());
    }

    @Override
    public Site getSite() {
        return site;

    }

    public void setSite(Site site) {
        this.site = site;
    }

    public static void main(String[] args) throws Exception {
        /*
        HttpClientUtil httpClientUtil = new HttpClientUtil();
        TaoBaoPageProcesser taoBaoPageProcesser = new TaoBaoPageProcesser();
        UcsmySite site = httpClientUtil.taobao(taoBaoPageProcesser.getSite());
        site.setDomain("1688.com");
        taoBaoPageProcesser.setSite(site);
        */
        ObjectMapper objectMapper = new ObjectMapper();
        AuthenticationUtil authenticationUtil = new AuthenticationUtil();
        HttpUtil httpUtil = HttpUtil.getInstance();
        Scanner sc = new Scanner(System.in);
        String regedit = "regeditSpiders/spidersId/001";
        //String spiderJson = authenticationUtil.getText("http://localhost:9090/" + regedit);
        String spiderJson = httpUtil.doGetForString("http://localhost:9090/testspider.json");
        Map maps = objectMapper.readValue(spiderJson, Map.class);
        String jobJson = null;
        String spiderId = null;
        System.out.println(maps.get("code"));
        while(!StringUtils.equals(maps.get("code").toString(), "200")) {
            System.out.println(maps.get("msg"));
            Thread.sleep(300);
            spiderJson = httpUtil.doGetForString("http://localhost:9090/test.json");
            maps = objectMapper.readValue(spiderJson, Map.class);
        }
        spiderId = maps.get("spiderID").toString();
        //String getJob = "requestJob/spiderId/***";
        //jobJson = authenticationUtil.getText("http://localhost:9090/" + regedit);
        jobJson = httpUtil.doGetForString("http://localhost:9090/qichacha.json");

        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true) ;
        JobDTO job = objectMapper.readValue(jobJson, JobDTO.class);
        Request[] requestes = new Request[10];
        Map map = new HashMap();
        map.put("index","name");
        map.put("key","TABLE_");
        for (int i = 0; i < requestes.length; i++) {
            StringBuffer buffer = new StringBuffer().append("http://qichacha.com/search").append("?");
            Request request = new Request();
            request = AuthenticationUtil.getQueryData(job.getTasksDTO().getHelpTaskDTOs().get(0).getParame(),request,job.getTasksDTO());
            requestes[i] = request;
        }
        Request request = new Request();
        request.setMethod("GET");
        request.setUrl("http://qichacha.com/search");
        HttpClientDownloaderBack httpClientDownloade = new HttpClientDownloaderBack(job,null);
        Spider.create(new QichachaPageProcesser())
                // .addUrl("https://login.taobao.com/member/login.jhtml")
//                .addRequest(requestes)
                .addRequest(request)
                .addPipeline(new ConsolePipeline())
                .setDownloader(httpClientDownloade)
                //.setScheduler(new QueueScheduler())
                .run();
    }
}
