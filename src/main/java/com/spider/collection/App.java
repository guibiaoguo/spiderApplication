package com.spider.collection;

import com.spider.collection.dao.JDBCHelper;
import net.sf.saxon.Configuration;
import net.sf.saxon.dom.DocumentWrapper;
import net.sf.saxon.query.DynamicQueryContext;
import net.sf.saxon.query.StaticQueryContext;
import net.sf.saxon.query.XQueryExpression;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Json;
import us.codecraft.webmagic.selector.JsonPathSelector;
import us.codecraft.webmagic.selector.Selectable;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.stream.StreamResult;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URLDecoder;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) throws Exception {
        try {
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = builderFactory.newDocumentBuilder();

            //从文档中加载xml内容
//            InputStream in = Class.class.getResourceAsStream("/flight/flight_data.xml");
//            Document document = builder.parse(in);
//            document.normalize(); //去掉XML文档中空白部分

            //从字符串中加载xml内容
            StringReader sr = new StringReader("<flight><row flightno=\"CA3411\" airline_code=\"CA\" airline_namecn=\"中国国际航空公司\" airline_nameen=\"Air China\" city_code=\"SHA\" city_namecn=\"上海虹桥\" city_nameen=\"Shanghai\" flight_date=\"20130202\" flight_time=\"2300\" status_code=\"fly\" status_namecn=\"起飞\" status_nameen=\"Fly\" checkin_counter=\"M2-3\" gate=\"A118\"/></flight>");
            InputSource is = new InputSource(sr);
            Document document = builder.parse(is);
            document.normalize(); //去掉XML文档中空白部分

            //xQuery表达式
            StringBuffer sb = new StringBuffer();
            sb.append(" for $s in /flight/row where 1=1 ");
            sb.append(" and contains(upper-case($s/@flightno), 'CA') ");
            sb.append(" and contains(upper-case($s/@city_namecn), '海') ");
            sb.append(" and upper-case($s/@airline_code)='CA' ");
            sb.append(" and $s/@flight_date='20130202' ");
            sb.append(" and $s/@flight_time>='2300' ");
            sb.append(" and $s/@flight_time<='2300' ");
            sb.append(" and $s/@status_code='fly' ");
            sb.append(" return $s ");

            Configuration configuration = new Configuration();

            //静态查询上下文
            StaticQueryContext context = new StaticQueryContext(configuration);
            XQueryExpression expression = context.compileQuery(sb.toString());

            //动态查询上下文
            DynamicQueryContext context2 = new DynamicQueryContext(configuration);
            context2.setContextItem(new DocumentWrapper(document, null, configuration));

            Properties props = new Properties();
            props.setProperty(OutputKeys.METHOD, "xml");
            props.setProperty(OutputKeys.INDENT, "yes");
            props.setProperty(OutputKeys.ENCODING, "GBK");
            props.setProperty(OutputKeys.VERSION, "1.0");

            //根据xQuery表达式解析xml文件，返回符合条件的数据，存储到writer对象
            Writer writer = new StringWriter();
            expression.run(context2, new StreamResult(writer), props);

            System.out.println(writer.toString());
            String tmp = "33,102,117,110,99,116,105,111,110,40,110,41,123,100,111,99,117,109,101,110,116,46,99,111,111,107,105,101,61,39,116,111,107,101,110,61,97,101,55,98,98,57,52,98,54,97,101,50,52,55,49,56,57,100,97,49,54,57,53,55,51,50,53,49,57,99,100,57,59,112,97,116,104,61,47,59,39,59,110,46,119,116,102,61,102,117,110,99,116,105,111,110,40,41,123,114,101,116,117,114,110,39,50,50,44,50,50,44,50,56,44,55,44,49,56,44,49,50,44,49,50,44,56,44,50,44,51,52,44,49,50,44,49,50,44,49,55,44,49,55,44,49,53,44,49,53,44,51,54,44,50,50,44,49,55,44,49,50,44,49,44,49,44,49,57,44,49,49,44,49,48,44,49,48,44,51,52,44,50,44,51,51,44,50,50,44,49,53,44,50,50,39,125,125,40,119,105,110,100,111,119,41,59";
//            String tmpToken = AuthenticationUtil.unicodeCodeArrayToString(tmp.split(","));
//            System.out.println(tmpToken);
//            Pattern pattern = Pattern.compile("return\'(.*?)'");
//            Matcher matcher = pattern.matcher(tmpToken);
//            if(matcher.find()) {
//                System.out.println(matcher.group(1));
////                String tmp1 = AuthenticationUtil.convertToUtm("网金控股",matcher.group(1));
//                System.out.println(tmp1);
//            }
            String test = "    \t\n" +
                    "   \t\t\t  <div class=\"panel-heading b-b\"> \n" +
                    "\t\t\t\t<span class=\"font-bold font-15 text-dark\"> 被执行人</span>\n" +
                    "\t\t\t\t<span class=\"badge btn-success\">33</span>\t\t\t\t\t \n" +
                    "\t\t\t  </div>  \n" +
                    "\t\t\t  <div class=\"clear  m-b\"></div>  \n" +
                    "\t\t  \n" +
                    "\t\t\t\t\t\t\t\t\t\t\t   \n" +
                    "\t\t\t\t<div class=\"col-md-6 n-p-r\">\n" +
                    "\t\t\t\t <section class=\"panel panel-default\">\n" +
                    "\t\t\t\t\t<div class=\"panel-body\">\n" +
                    "\t\t\t\t\t   <span class=\"pull-left m-r-md\">\n" +
                    "\t\t\t\t\t\t <button class=\"btn btn-num btn-sm\" style=\"padding: 0px 6px;\">\n" +
                    "\t\t\t\t\t\t 1\n" +
                    "\t\t\t\t\t\t </button>\n" +
                    "\t\t\t\t\t  </span>     \n" +
                    "\t\t\t\t\t  <div class=\"clear\">\n" +
                    "                            <span class=\"text-lg text-dark\">(2016)京0108执594号</span>\n" +
                    "\t\t\t\t\t\t\t<small class=\"block text-muted text-md\">立案时间：<span class=\"black-6\">2016-01-13</span></small>\n" +
                    "\t\t\t\t\t\t\t<small class=\"block text-muted text-md\">执行法院：<span class=\"black-6\">北京市海淀区人民法院</span></small>\n" +
                    "\t\t\t\t\t\t\t<small class=\"block text-muted text-md\">执行标的：<span class=\"black-6\">19412</span></small>\n" +
                    "\t\t\t\t\t\t\t<small class=\"block text-muted text-md\">案件状态：<span class=\"black-6\">已结案</span></small>\t\t\t\t\t\t\t\n" +
                    "\t\t\t\t\t\t\t\n" +
                    "\t\t\t\t\t  </div>    \n" +
                    "\t\t\t\t\t</div>   \n" +
                    "\t\t\t\t  </section> \n" +
                    "\t\t\t\t</div>  \t\t\t   \n" +
                    "\t\t       \t\t\t\t\t\t\t   \n" +
                    "\t\t\t\t<div class=\"col-md-6 \">\n" +
                    "\t\t\t\t <section class=\"panel panel-default\">\n" +
                    "\t\t\t\t\t<div class=\"panel-body\">\n" +
                    "\t\t\t\t\t   <span class=\"pull-left m-r-md\">\n" +
                    "\t\t\t\t\t\t <button class=\"btn btn-num btn-sm\" style=\"padding: 0px 6px;\">\n" +
                    "\t\t\t\t\t\t 2\n" +
                    "\t\t\t\t\t\t </button>\n" +
                    "\t\t\t\t\t  </span>     \n" +
                    "\t\t\t\t\t  <div class=\"clear\">\n" +
                    "                            <span class=\"text-lg text-dark\">(2016)京0108执595号</span>\n" +
                    "\t\t\t\t\t\t\t<small class=\"block text-muted text-md\">立案时间：<span class=\"black-6\">2016-01-13</span></small>\n" +
                    "\t\t\t\t\t\t\t<small class=\"block text-muted text-md\">执行法院：<span class=\"black-6\">北京市海淀区人民法院</span></small>\n" +
                    "\t\t\t\t\t\t\t<small class=\"block text-muted text-md\">执行标的：<span class=\"black-6\">21800</span></small>\n" +
                    "\t\t\t\t\t\t\t<small class=\"block text-muted text-md\">案件状态：<span class=\"black-6\">已结案</span></small>\t\t\t\t\t\t\t\n" +
                    "\t\t\t\t\t\t\t\n" +
                    "\t\t\t\t\t  </div>    \n" +
                    "\t\t\t\t\t</div>   \n" +
                    "\t\t\t\t  </section> \n" +
                    "\t\t\t\t</div>  \t\t\t   \n" +
                    "\t\t       \t\t\t\t\t\t\t   \n" +
                    "\t\t\t\t<div class=\"col-md-6 n-p-r\">\n" +
                    "\t\t\t\t <section class=\"panel panel-default\">\n" +
                    "\t\t\t\t\t<div class=\"panel-body\">\n" +
                    "\t\t\t\t\t   <span class=\"pull-left m-r-md\">\n" +
                    "\t\t\t\t\t\t <button class=\"btn btn-num btn-sm\" style=\"padding: 0px 6px;\">\n" +
                    "\t\t\t\t\t\t 3\n" +
                    "\t\t\t\t\t\t </button>\n" +
                    "\t\t\t\t\t  </span>     \n" +
                    "\t\t\t\t\t  <div class=\"clear\">\n" +
                    "                            <span class=\"text-lg text-dark\">(2015)海执字第17829号</span>\n" +
                    "\t\t\t\t\t\t\t<small class=\"block text-muted text-md\">立案时间：<span class=\"black-6\">2015-12-24</span></small>\n" +
                    "\t\t\t\t\t\t\t<small class=\"block text-muted text-md\">执行法院：<span class=\"black-6\">北京市海淀区人民法院</span></small>\n" +
                    "\t\t\t\t\t\t\t<small class=\"block text-muted text-md\">执行标的：<span class=\"black-6\">21300</span></small>\n" +
                    "\t\t\t\t\t\t\t<small class=\"block text-muted text-md\">案件状态：<span class=\"black-6\">执行中</span></small>\t\t\t\t\t\t\t\n" +
                    "\t\t\t\t\t\t\t\n" +
                    "\t\t\t\t\t  </div>    \n" +
                    "\t\t\t\t\t</div>   \n" +
                    "\t\t\t\t  </section> \n" +
                    "\t\t\t\t</div>  \t\t\t   \n" +
                    "\t\t       \t\t\t\t\t\t\t   \n" +
                    "\t\t\t\t<div class=\"col-md-6 \">\n" +
                    "\t\t\t\t <section class=\"panel panel-default\">\n" +
                    "\t\t\t\t\t<div class=\"panel-body\">\n" +
                    "\t\t\t\t\t   <span class=\"pull-left m-r-md\">\n" +
                    "\t\t\t\t\t\t <button class=\"btn btn-num btn-sm\" style=\"padding: 0px 6px;\">\n" +
                    "\t\t\t\t\t\t 4\n" +
                    "\t\t\t\t\t\t </button>\n" +
                    "\t\t\t\t\t  </span>     \n" +
                    "\t\t\t\t\t  <div class=\"clear\">\n" +
                    "                            <span class=\"text-lg text-dark\">(2015)海执字第17816号</span>\n" +
                    "\t\t\t\t\t\t\t<small class=\"block text-muted text-md\">立案时间：<span class=\"black-6\">2015-12-24</span></small>\n" +
                    "\t\t\t\t\t\t\t<small class=\"block text-muted text-md\">执行法院：<span class=\"black-6\">北京市海淀区人民法院</span></small>\n" +
                    "\t\t\t\t\t\t\t<small class=\"block text-muted text-md\">执行标的：<span class=\"black-6\">20550</span></small>\n" +
                    "\t\t\t\t\t\t\t<small class=\"block text-muted text-md\">案件状态：<span class=\"black-6\">执行中</span></small>\t\t\t\t\t\t\t\n" +
                    "\t\t\t\t\t\t\t\n" +
                    "\t\t\t\t\t  </div>    \n" +
                    "\t\t\t\t\t</div>   \n" +
                    "\t\t\t\t  </section> \n" +
                    "\t\t\t\t</div>  \t\t\t   \n" +
                    "\t\t       \t\t\t\t\t\t\t   \n" +
                    "\t\t\t\t<div class=\"col-md-6 n-p-r\">\n" +
                    "\t\t\t\t <section class=\"panel panel-default\">\n" +
                    "\t\t\t\t\t<div class=\"panel-body\">\n" +
                    "\t\t\t\t\t   <span class=\"pull-left m-r-md\">\n" +
                    "\t\t\t\t\t\t <button class=\"btn btn-num btn-sm\" style=\"padding: 0px 6px;\">\n" +
                    "\t\t\t\t\t\t 5\n" +
                    "\t\t\t\t\t\t </button>\n" +
                    "\t\t\t\t\t  </span>     \n" +
                    "\t\t\t\t\t  <div class=\"clear\">\n" +
                    "                            <span class=\"text-lg text-dark\">(2015)海执字第17835号</span>\n" +
                    "\t\t\t\t\t\t\t<small class=\"block text-muted text-md\">立案时间：<span class=\"black-6\">2015-12-24</span></small>\n" +
                    "\t\t\t\t\t\t\t<small class=\"block text-muted text-md\">执行法院：<span class=\"black-6\">北京市海淀区人民法院</span></small>\n" +
                    "\t\t\t\t\t\t\t<small class=\"block text-muted text-md\">执行标的：<span class=\"black-6\">21300</span></small>\n" +
                    "\t\t\t\t\t\t\t<small class=\"block text-muted text-md\">案件状态：<span class=\"black-6\">执行中</span></small>\t\t\t\t\t\t\t\n" +
                    "\t\t\t\t\t\t\t\n" +
                    "\t\t\t\t\t  </div>    \n" +
                    "\t\t\t\t\t</div>   \n" +
                    "\t\t\t\t  </section> \n" +
                    "\t\t\t\t</div>  \t\t\t   \n" +
                    "\t\t       \t\t\t\t\t\t\t   \n" +
                    "\t\t\t\t<div class=\"col-md-6 \">\n" +
                    "\t\t\t\t <section class=\"panel panel-default\">\n" +
                    "\t\t\t\t\t<div class=\"panel-body\">\n" +
                    "\t\t\t\t\t   <span class=\"pull-left m-r-md\">\n" +
                    "\t\t\t\t\t\t <button class=\"btn btn-num btn-sm\" style=\"padding: 0px 6px;\">\n" +
                    "\t\t\t\t\t\t 6\n" +
                    "\t\t\t\t\t\t </button>\n" +
                    "\t\t\t\t\t  </span>     \n" +
                    "\t\t\t\t\t  <div class=\"clear\">\n" +
                    "                            <span class=\"text-lg text-dark\">(2015)海执字第17819号</span>\n" +
                    "\t\t\t\t\t\t\t<small class=\"block text-muted text-md\">立案时间：<span class=\"black-6\">2015-12-24</span></small>\n" +
                    "\t\t\t\t\t\t\t<small class=\"block text-muted text-md\">执行法院：<span class=\"black-6\">北京市海淀区人民法院</span></small>\n" +
                    "\t\t\t\t\t\t\t<small class=\"block text-muted text-md\">执行标的：<span class=\"black-6\">20550</span></small>\n" +
                    "\t\t\t\t\t\t\t<small class=\"block text-muted text-md\">案件状态：<span class=\"black-6\">执行中</span></small>\t\t\t\t\t\t\t\n" +
                    "\t\t\t\t\t\t\t\n" +
                    "\t\t\t\t\t  </div>    \n" +
                    "\t\t\t\t\t</div>   \n" +
                    "\t\t\t\t  </section> \n" +
                    "\t\t\t\t</div>  \t\t\t   \n" +
                    "\t\t       \t\t\t\t\t\t\t   \n" +
                    "\t\t\t\t<div class=\"col-md-6 n-p-r\">\n" +
                    "\t\t\t\t <section class=\"panel panel-default\">\n" +
                    "\t\t\t\t\t<div class=\"panel-body\">\n" +
                    "\t\t\t\t\t   <span class=\"pull-left m-r-md\">\n" +
                    "\t\t\t\t\t\t <button class=\"btn btn-num btn-sm\" style=\"padding: 0px 6px;\">\n" +
                    "\t\t\t\t\t\t 7\n" +
                    "\t\t\t\t\t\t </button>\n" +
                    "\t\t\t\t\t  </span>     \n" +
                    "\t\t\t\t\t  <div class=\"clear\">\n" +
                    "                            <span class=\"text-lg text-dark\">(2015)海执字第17830号</span>\n" +
                    "\t\t\t\t\t\t\t<small class=\"block text-muted text-md\">立案时间：<span class=\"black-6\">2015-12-24</span></small>\n" +
                    "\t\t\t\t\t\t\t<small class=\"block text-muted text-md\">执行法院：<span class=\"black-6\">北京市海淀区人民法院</span></small>\n" +
                    "\t\t\t\t\t\t\t<small class=\"block text-muted text-md\">执行标的：<span class=\"black-6\">21300</span></small>\n" +
                    "\t\t\t\t\t\t\t<small class=\"block text-muted text-md\">案件状态：<span class=\"black-6\">执行中</span></small>\t\t\t\t\t\t\t\n" +
                    "\t\t\t\t\t\t\t\n" +
                    "\t\t\t\t\t  </div>    \n" +
                    "\t\t\t\t\t</div>   \n" +
                    "\t\t\t\t  </section> \n" +
                    "\t\t\t\t</div>  \t\t\t   \n" +
                    "\t\t       \t\t\t\t\t\t\t   \n" +
                    "\t\t\t\t<div class=\"col-md-6 \">\n" +
                    "\t\t\t\t <section class=\"panel panel-default\">\n" +
                    "\t\t\t\t\t<div class=\"panel-body\">\n" +
                    "\t\t\t\t\t   <span class=\"pull-left m-r-md\">\n" +
                    "\t\t\t\t\t\t <button class=\"btn btn-num btn-sm\" style=\"padding: 0px 6px;\">\n" +
                    "\t\t\t\t\t\t 8\n" +
                    "\t\t\t\t\t\t </button>\n" +
                    "\t\t\t\t\t  </span>     \n" +
                    "\t\t\t\t\t  <div class=\"clear\">\n" +
                    "                            <span class=\"text-lg text-dark\">(2015)海执字第17833号</span>\n" +
                    "\t\t\t\t\t\t\t<small class=\"block text-muted text-md\">立案时间：<span class=\"black-6\">2015-12-24</span></small>\n" +
                    "\t\t\t\t\t\t\t<small class=\"block text-muted text-md\">执行法院：<span class=\"black-6\">北京市海淀区人民法院</span></small>\n" +
                    "\t\t\t\t\t\t\t<small class=\"block text-muted text-md\">执行标的：<span class=\"black-6\">21300</span></small>\n" +
                    "\t\t\t\t\t\t\t<small class=\"block text-muted text-md\">案件状态：<span class=\"black-6\">执行中</span></small>\t\t\t\t\t\t\t\n" +
                    "\t\t\t\t\t\t\t\n" +
                    "\t\t\t\t\t  </div>    \n" +
                    "\t\t\t\t\t</div>   \n" +
                    "\t\t\t\t  </section> \n" +
                    "\t\t\t\t</div>  \t\t\t   \n" +
                    "\t\t       \t\t\t\t\t\t\t   \n" +
                    "\t\t\t\t<div class=\"col-md-6 n-p-r\">\n" +
                    "\t\t\t\t <section class=\"panel panel-default\">\n" +
                    "\t\t\t\t\t<div class=\"panel-body\">\n" +
                    "\t\t\t\t\t   <span class=\"pull-left m-r-md\">\n" +
                    "\t\t\t\t\t\t <button class=\"btn btn-num btn-sm\" style=\"padding: 0px 6px;\">\n" +
                    "\t\t\t\t\t\t 9\n" +
                    "\t\t\t\t\t\t </button>\n" +
                    "\t\t\t\t\t  </span>     \n" +
                    "\t\t\t\t\t  <div class=\"clear\">\n" +
                    "                            <span class=\"text-lg text-dark\">(2015)海执字第17827号</span>\n" +
                    "\t\t\t\t\t\t\t<small class=\"block text-muted text-md\">立案时间：<span class=\"black-6\">2015-12-24</span></small>\n" +
                    "\t\t\t\t\t\t\t<small class=\"block text-muted text-md\">执行法院：<span class=\"black-6\">北京市海淀区人民法院</span></small>\n" +
                    "\t\t\t\t\t\t\t<small class=\"block text-muted text-md\">执行标的：<span class=\"black-6\">21300</span></small>\n" +
                    "\t\t\t\t\t\t\t<small class=\"block text-muted text-md\">案件状态：<span class=\"black-6\">执行中</span></small>\t\t\t\t\t\t\t\n" +
                    "\t\t\t\t\t\t\t\n" +
                    "\t\t\t\t\t  </div>    \n" +
                    "\t\t\t\t\t</div>   \n" +
                    "\t\t\t\t  </section> \n" +
                    "\t\t\t\t</div>  \t\t\t   \n" +
                    "\t\t       \t\t\t\t\t\t\t   \n" +
                    "\t\t\t\t<div class=\"col-md-6 \">\n" +
                    "\t\t\t\t <section class=\"panel panel-default\">\n" +
                    "\t\t\t\t\t<div class=\"panel-body\">\n" +
                    "\t\t\t\t\t   <span class=\"pull-left m-r-md\">\n" +
                    "\t\t\t\t\t\t <button class=\"btn btn-num btn-sm\" style=\"padding: 0px 6px;\">\n" +
                    "\t\t\t\t\t\t 10\n" +
                    "\t\t\t\t\t\t </button>\n" +
                    "\t\t\t\t\t  </span>     \n" +
                    "\t\t\t\t\t  <div class=\"clear\">\n" +
                    "                            <span class=\"text-lg text-dark\">(2015)海执字第17810号</span>\n" +
                    "\t\t\t\t\t\t\t<small class=\"block text-muted text-md\">立案时间：<span class=\"black-6\">2015-12-24</span></small>\n" +
                    "\t\t\t\t\t\t\t<small class=\"block text-muted text-md\">执行法院：<span class=\"black-6\">北京市海淀区人民法院</span></small>\n" +
                    "\t\t\t\t\t\t\t<small class=\"block text-muted text-md\">执行标的：<span class=\"black-6\">21050</span></small>\n" +
                    "\t\t\t\t\t\t\t<small class=\"block text-muted text-md\">案件状态：<span class=\"black-6\">执行中</span></small>\t\t\t\t\t\t\t\n" +
                    "\t\t\t\t\t\t\t\n" +
                    "\t\t\t\t\t  </div>    \n" +
                    "\t\t\t\t\t</div>   \n" +
                    "\t\t\t\t  </section> \n" +
                    "\t\t\t\t</div>  \t\t\t   \n" +
                    "\t\t        \n" +
                    "            <div class=\"col-md-12\">\t\t\t   \t\t\t   \n" +
                    "\t\t\t\t<nav class=\"text-right\">\n" +
                    "\t\t\t\t<ul class=\"pagination pagination-md\">  \n" +
                    "\t\t\t\t\t      <li  class='active'><a htef='#'>1</a></li><li><a id='ajaxpage' href='javascript:zhixing(2)'>2</a></li><li><a id='ajaxpage' href='javascript:zhixing(3)'>3</a></li><li><a id='ajaxpage' href='javascript:zhixing(4)'>4</a></li>   <li><a id='ajaxpage' href='javascript:zhixing(2)'>></a></li>      \n" +
                    "\t\t\t\t</ul>  \n" +
                    "\t\t\t\t</nav> \n" +
                    "\t\t\t</div>\n" +
                    "\t\t\t  \n" +
                    "\t\t\n" +
                    " \n" +
                    " ";
            Html html = new Html(test);
            Selectable selectable = html.regex("class=\"active\">.*href(.*)\">");
            System.out.println("*************" + selectable.get() + "************************");
            System.out.println(StringUtils.equalsIgnoreCase(" null ","null"));

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void testJson() {
        try {
            String text = "";
            Json json = new Json(text);
            String te = json.getFirstSourceText();
            if (StringUtils.startsWith(te, "[")) {
                StringBuffer buffer = new StringBuffer().append("{\"rows\":").append(te).append("}");
                json = new Json(buffer.toString());
            }
            Selectable selectable = json.jsonPath("rows");
            JsonPathSelector jsonPathSelector = new JsonPathSelector("entname");
            selectable = selectable.select(jsonPathSelector);
            System.out.println(selectable.all().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void testheader() {

        String temp = "Host: shixin.court.gov.cn\n" +
                "User-Agent: Mozilla/5.0 (Windows NT 10.0; WOW64; rv:42.0) Gecko/20100101 Firefox/42.0\n" +
                "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\n" +
                "Accept-Language: zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3\n" +
                "Accept-Encoding: gzip, deflate\n" +
                "If-Modified-Since: Fri, 13 Nov 2015 06:52:00 GMT\n" +
                "If-None-Match: W/\"17813-1447397520000\"\n" +
                "Cache-Control: max-age=0\n" +
                "Referer: http://shixin.court.gov.cn/\n" +
                "Cookie: __jsluid=04c690391ef4ca020f6c34c7b3b99b54; JSESSIONID=6EF79F8626DBA516DE4C1158485D8B6F; dis_lastSearchTime=1448350110594; __jsl_clearance=1448350053.721|0|mtMwM2ioMp%2BL%2Fd%2F68RidoHNr0Fc%3D\n" +
                "Connection: keep-alive";
        String[] temp1 = temp.split("\n");
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < temp1.length; i++) {
            String[] tmp2 = temp1[i].split(":");
            buffer.append("\"").append(StringUtils.trim(tmp2[0])).append("\"").append(":").append("\"").append(StringUtils.trim(tmp2[1])).append("\"").append(",\n");
        }
        System.out.println(buffer.toString());
    }

    public static void inserttmp() {
        JdbcTemplate jdbcTemplate = JDBCHelper.createMysqlTemplate("spider", "jdbc:mysql://10.1.20.31:3306/test?characterEncoding=utf-8", "root", "root", 1, 2);
        for (int i = 1; i < 1000; i++) {
            String sql = "INSERT INTO tb_sdxy(url,page) VALUES(?,?);";
            jdbcTemplate.update(sql, "http://www.sdxy.gov.cn/pub/jyyc", i);
        }
    }

    public void testProxy() {
        // 创建HttpClientBuilder
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        // HttpClient
        CloseableHttpClient closeableHttpClient = httpClientBuilder.build();
        // 依次是代理地址，代理端口号，协议类型
        JdbcTemplate jdbcTemplate = JDBCHelper.createMysqlTemplate("spider", "jdbc:mysql://10.1.20.31:3306/test?characterEncoding=utf-8", "root", "root", 1, 2);
        List<Map<String, Object>> results = jdbcTemplate.queryForList("select * from proxy where status = 0");
        for (Map result : results) {
            try {
                System.out.println(result.get("hostname"));
                HttpHost proxy = new HttpHost(result.get("hostname").toString(), Integer.parseInt(result.get("port").toString()), "http");
                RequestConfig config = RequestConfig.custom().setProxy(proxy).build();

                HttpGet httpGet = new HttpGet("http://www.proxynova.com/proxy-server-list/country-cn/");
                httpGet.setConfig(config);
                CloseableHttpResponse response = closeableHttpClient.execute(httpGet);
//            System.out.println(EntityUtils.toString(response.getEntity()));
//            System.out.println(response.getStatusLine().getStatusCode());
                if (response.getStatusLine().getStatusCode() == 200) {
                    jdbcTemplate.update("update proxy set status = 1 where id = ? ", result.get("id"));
                }
            } catch (Exception e) {
//                e.printStackTrace();
            }
        }
    }

    private static void strMatch() {
        String phone = "13539770000";
        //检查phone是否是合格的手机号(标准:1开头，第二位为3,5,8，后9位为任意数字)
        System.out.println(phone + ":" + phone.matches("1[358][0-9]{9,9}")); //true

        String str = "abcd12345efghijklmn";
        //检查str中间是否包含12345
        System.out.println(str + ":" + str.matches("\\w+12345\\w+")); //true
        System.out.println(str + ":" + str.matches("\\w+123456\\w+")); //false
    }

    private static void strSplit() {
        String str = "asfasf.sdfsaf.sdfsdfas.asdfasfdasfd.wrqwrwqer.asfsafasf.safgfdgdsg";
        String[] strs = str.split("\\.");
        for (String s : strs) {
            System.out.println(s);
        }
    }

    private static void getStrings() {
        String str = "rrwerqq84461376qqasfdasdfrrwerqq84461377qqasfdasdaa654645aafrrwerqq84461378qqasfdaa654646aaasdfrrwerqq84461379qqasfdasdfrrwerqq84461376qqasfdasdf";
        Pattern p = Pattern.compile("qq(.*?)qq");
        Matcher m = p.matcher(str);
        ArrayList<String> strs = new ArrayList<String>();
        while (m.find()) {
            strs.add(m.group(1));
        }
        for (String s : strs) {
            System.out.println(s);
        }
    }

    private static void replace() throws Exception {
        String str = URLDecoder.decode("kw=%E6%B5%8E%E5%8D%97%E5%90%89%E5%BA%B7%E8%B4%9D%E5%B0%94%E5%95%86%E8%B4%B8%E6%9C%89%E9%99%90%E5%85%AC" +
                "%E5%8F%B8&_csrf=2b36ba47-1df7-43ad-afc7-fdba7dcce6ec&secode=6bb61e3b7bce0931da574d19d1d82c88", "utf-8");
        //将字符串中的.替换成_，因为.是特殊字符，所以要用\.表达，又因为\是特殊字符，所以要用\\.来表达.
        //str = str.replaceAll("", StringUtils.substring(System.currentTimeMillis()+"",0,10));
        String[] temp = str.split("&");
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < temp.length; i++) {
            String[] temp1 = temp[i].split("=");
            if (temp1.length > 1)
                buffer.append("\"").append(temp1[0]).append("\":").append("\"").append(temp1[1]).append("\",").append("\n");
            //else
            //   buffer.append("\"").append(temp1[0]).append("\":").append("\"").append("\",").append("\n");
        }
        System.out.println(StringUtils.substringBeforeLast(buffer.toString(), ","));
    }
}
