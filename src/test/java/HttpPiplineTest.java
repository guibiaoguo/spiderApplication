import com.spider.collection.util.AuthenticationUtil;
import com.spider.collection.util.HttpUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;
import us.codecraft.webmagic.selector.Json;
import us.codecraft.webmagic.selector.PlainText;
import us.codecraft.webmagic.selector.Selectable;
import us.codecraft.xsoup.Xsoup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/1/6.
 */

public class HttpPiplineTest {

    @Test
    public void Process() {

    }

    @Test
    public void refreshTokenTest() {
        String str = "33,102,117,110,99,116,105,111,110,40,110,41,123,100,111,99,117,109,101,110,116,46,99,111,111,107,105,101,61,39,116,111,107,101,110,61,56,102,97,98,52,54,100,54,54,100,53,53,52,53,49,51,98,57,50,97,97,55,99,48,48,100,56,52,53,99,100,100,59,112,97,116,104,61,47,59,39,59,110,46,119,116,102,61,102,117,110,99,116,105,111,110,40,41,123,114,101,116,117,114,110,39,49,52,44,50,55,44,49,44,50,51,44,50,55,44,49,44,50,55,44,49,52,44,51,49,44,49,52,44,54,44,49,54,44,49,52,44,51,48,44,51,54,44,50,53,44,51,49,44,50,51,44,50,53,44,50,53,44,56,44,49,52,44,54,44,49,54,44,54,44,49,52,44,50,48,44,50,53,44,50,55,44,50,57,44,50,53,44,50,48,39,125,125,40,119,105,110,100,111,119,41,59";
        String token = "8fab46d66d554513b92aa7c00d845cdd";

        String tokenParamStr = AuthenticationUtil.unicodeCodeArrayToString(str.split(","));
        int index1 = tokenParamStr.indexOf("token=");
        int index2 = tokenParamStr.indexOf(";", index1);
        String token1 = tokenParamStr.substring(index1 + 6, index2);
        index1 = tokenParamStr.indexOf("return'");
        index2 = tokenParamStr.indexOf("'", index1 + 7);
        String utmCodeStr = tokenParamStr.substring(index1 + 7, index2);
        String utm = "4867868494db415c97cce4dbd43c82c3";
        String utm1 = AuthenticationUtil.convertToUtm("小米",utmCodeStr);

        Assert.assertEquals(token, token1);
        Assert.assertEquals(utm, utm1);
    }

    @Test
    public void testSelect() {

        String html = "<html><div><a href='https://github.com'>github.com</a></div>" +
                "<table><tr><td>a</td><td>b</td></tr></table></html>";

        Document document = Jsoup.parse(html);

        String result = Xsoup.compile("//a/@href").evaluate(document).get();
        Assert.assertEquals("https://github.com", result);

        List<String> list = Xsoup.compile("//tr/td/text()").evaluate(document).list();
        Assert.assertEquals("a", list.get(0));
        Assert.assertEquals("b", list.get(1));
    }

    @Test
    public void testJson() throws Exception {
        HttpUtil httpUtil = HttpUtil.getInstance();
        String text = httpUtil.doGetForString("https://dbank.lccb.com.cn/f/memberBidInfo/fenyeMemberRequestInfo?pageNo=1&id=11000000054");
        Json json = new Json(text);
        Selectable selectable = json.jsonPath("$..list").regex("\\[(.*)\\]");
        List<String> results = new ArrayList<String>();
        List<String> list = new ArrayList<String>();
        Object object = selectable.all();
        if (object instanceof List) {
            List<Object> items = (List<Object>) object;
            for (Object item : items) {
                String tmp = String.valueOf(item).split(",")[0];
                list.add(String.valueOf(tmp));
            }
        } else {
            list.add(String.valueOf(object));
        }
        selectable = new PlainText(list).regex("\"(.*)\"");
        List<String> selectables = selectable.all();
        System.out.println(json.jsonPath("$..list[0]"));
    }

}
