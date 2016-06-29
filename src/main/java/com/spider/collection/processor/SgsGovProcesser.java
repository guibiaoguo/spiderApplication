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

import java.util.Map;
import java.util.Scanner;

/**
 * Created by Administrator on 2015/11/6.
 */
public class SgsGovProcesser implements PageProcessor{

    private Site site = Site.me().setDomain("sgs.gov.cn").setUserAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:35.0) Gecko/20100101 Firefox/35.0");

    @Override
    public void process(Page page) {
        page.addTargetRequest(page.getHtml().links().regex("http://me.1688.com/info/[a-zA-Z0-9*-]+.htm").toString());
        //page.putField("content", page.getHtml().$("div#content").toString());
        //page.putField("login-box-warp", page.getHtml().$("div.login-box-warp").toString());
        page.putField("address", page.getHtml().xpath("/html/body/div[1]/table/tbody/tr/th/table/tbody/tr[1]/td/text()"));
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
        String spiderJson = httpUtil.doGetForString("http://localhost:8080/testspider.json");
        Map maps = objectMapper.readValue(spiderJson, Map.class);
        String jobJson = null;
        String spiderId = null;
        System.out.println(maps.get("code"));
        while(!StringUtils.equals(maps.get("code").toString(), "200")) {
            System.out.println(maps.get("msg"));
            Thread.sleep(300);
            spiderJson = httpUtil.doGetForString("http://localhost:8080/test.json");
            maps = objectMapper.readValue(spiderJson, Map.class);
        }
        spiderId = maps.get("spiderID").toString();
        //String getJob = "requestJob/spiderId/***";
        //jobJson = authenticationUtil.getText("http://localhost:9090/" + regedit);
        jobJson = httpUtil.doGetForString("http://localhost:8080/test.json");

        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true) ;
        JobDTO job = objectMapper.readValue(jobJson, JobDTO.class);
        Request request = new Request();
        request.setMethod("POST");
        request.setUrl("http://www.sgs.gov.cn/lz/etpsInfo.do?method=doSearch");
        HttpClientDownloaderBack httpClientDownloade = new HttpClientDownloaderBack(job,null);
        Spider.create(new TaoBaoPageProcesser())
                // .addUrl("https://login.taobao.com/member/login.jhtml")
                .addRequest(request)
                .addPipeline(new ConsolePipeline())
                .setDownloader(httpClientDownloade)
                .thread(10)
                .run();
    }
}
