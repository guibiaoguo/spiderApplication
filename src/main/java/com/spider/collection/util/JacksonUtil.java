package com.spider.collection.util;

import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.JsonNodeFactory;

import java.io.IOException;
import java.util.*;

/**
 * Created by Administrator on 2015/10/12.
 */
public class JacksonUtil {

    private ObjectMapper objectMapper;
    private JsonGenerator jsonGenerator;
    private AccountBean bean = null;

    public JacksonUtil() {
        objectMapper = new ObjectMapper();
        bean = new AccountBean();
        bean.setAddress("china-Guangzhou");
        bean.setEmail("hoojo_@126.com");
        bean.setId(1);
        bean.setName("hoojo");
        try {
            jsonGenerator = objectMapper.getJsonFactory().createJsonGenerator(System.out, JsonEncoding.UTF8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeEntityJSON(Object object) {

        try {
            System.out.println("jsonGenerator");
            //writeObject可以转换java对象，eg:JavaBean/Map/List/Array等
            jsonGenerator.writeObject(bean);
            System.out.println();

            System.out.println("ObjectMapper");
            //writeValue具有和writeObject相同的功能
            objectMapper.writeValue(System.out, bean);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

/**
 * <b>function:</b>将map转换成json字符串
 * @author hoojo
 * @createDate 2010-11-23 下午06:05:26
 */

    public void writeMapJSON(Map<String, Object> map) {
        try {
            map = new HashMap<String, Object>();
            map.put("name", bean.getName());
            map.put("account", bean);
            bean = new AccountBean();
            bean.setAddress("china-Beijin");
            bean.setEmail("hoojo@qq.com");
            map.put("account2", bean);

            System.out.println("jsonGenerator");
            jsonGenerator.writeObject(map);
            System.out.println("");

            System.out.println("objectMapper");
            objectMapper.writeValue(System.out, map);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
/**
 * <b>function:</b>将list集合转换成json字符串
 * @author hoojo
 * @createDate 2010-11-23 下午06:05:59
 */

    public void writeListJSON() {
        try {
            List<AccountBean> list = new ArrayList<AccountBean>();
            list.add(bean);

            bean = new AccountBean();
            bean.setId(2);
            bean.setAddress("address2");
            bean.setEmail("email2");
            bean.setName("haha2");
            list.add(bean);

            System.out.println("jsonGenerator");
            //list转换成JSON字符串
            jsonGenerator.writeObject(list);
            System.out.println();
            System.out.println("ObjectMapper");
            //用objectMapper直接返回list转换成的JSON字符串
            System.out.println("1###" + objectMapper.writeValueAsString(list));
            System.out.print("2###");
            //objectMapper list转换成JSON字符串
            objectMapper.writeValue(System.out, list);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeOthersJSON() {
        try {
            String[] arr = { "a", "b", "c" };
            System.out.println("jsonGenerator");
            String str = "hello world jackson!";
            //byte
            jsonGenerator.writeBinary(str.getBytes());
            //boolean
            jsonGenerator.writeBoolean(true);
            //null
            jsonGenerator.writeNull();
            //float
            jsonGenerator.writeNumber(2.2f);
            //char
            jsonGenerator.writeRaw("c");
            //String
            jsonGenerator.writeRaw(str, 5, 10);
            //String
            jsonGenerator.writeRawValue(str, 5, 5);
            //String
            jsonGenerator.writeString(str);
            jsonGenerator.writeTree(JsonNodeFactory.instance.POJONode(str));
            System.out.println();

            //Object
            jsonGenerator.writeStartObject();//{
            jsonGenerator.writeObjectFieldStart("user");//user:{
            jsonGenerator.writeStringField("name", "jackson");//name:jackson
            jsonGenerator.writeBooleanField("sex", true);//sex:true
            jsonGenerator.writeNumberField("age", 22);//age:22
            jsonGenerator.writeEndObject();//}

            jsonGenerator.writeArrayFieldStart("infos");//infos:[
            jsonGenerator.writeNumber(22);//22
            jsonGenerator.writeString("this is array");//this is array
            jsonGenerator.writeEndArray();//]

            jsonGenerator.writeEndObject();//}


            AccountBean bean = new AccountBean();
            bean.setAddress("address");
            bean.setEmail("email");
            bean.setId(1);
            bean.setName("haha");
            //complex Object
            jsonGenerator.writeStartObject();//{
            jsonGenerator.writeObjectField("user", bean);//user:{bean}
            jsonGenerator.writeObjectField("infos", arr);//infos:[array]
            jsonGenerator.writeEndObject();//}

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void readJson2Entity( String json) {
        json = "{\"address\":\"address\",\"name\":\"haha\",\"id\":1,\"email\":\"email\"}";
        try {
            AccountBean acc = objectMapper.readValue(json, AccountBean.class);
            System.out.println(acc.getName());
            System.out.println(acc);
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * <b>function:</b>json字符串转换成list<map>
     * @author hoojo
     * @createDate 2010-11-23 下午06:12:01
     */

    public void readJson2List( String json) {
        json = "[{\"address\": \"address2\",\"name\":\"haha2\",\"id\":2,\"email\":\"email2\"},"+
                "{\"address\":\"address\",\"name\":\"haha\",\"id\":1,\"email\":\"email\"}]";
        try {
            List<LinkedHashMap<String, Object>> list = objectMapper.readValue(json, List.class);
            System.out.println(list.size());
            for (int i = 0; i < list.size(); i++) {
                Map<String, Object> map = list.get(i);
                Set<String> set = map.keySet();
                for (Iterator<String> it = set.iterator();it.hasNext();) {
                    String key = it.next();
                    System.out.println(key + ":" + map.get(key));
                }
            }
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * <b>function:</b>json字符串转换成Array
     * @author hoojo
     * @createDate 2010-11-23 下午06:14:01
     */

    public void readJson2Array( String json) {
        json = "[{\"address\": \"address2\",\"name\":\"haha2\",\"id\":2,\"email\":\"email2\"},"+
                "{\"address\":\"address\",\"name\":\"haha\",\"id\":1,\"email\":\"email\"}]";
        try {
            AccountBean[] arr = objectMapper.readValue(json, AccountBean[].class);
            System.out.println(arr.length);
            for (int i = 0; i < arr.length; i++) {
                System.out.println(arr[i]);
            }

        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

/**
 * <b>function:</b>json字符串转换Map集合
 * @author hoojo
 * @createDate Nov 27, 2010 3:00:06 PM
 */

    public void readJson2Map( String json) {
       //json = "{\"success\":true,\"A\":{\"address\": \"address2\",\"name\":\"haha2\",\"id\":2,\"email\":\"email2\"},"+
        //        "\"B\":{\"address\":\"address\",\"name\":\"haha\",\"id\":1,\"email\":\"email\"}}";
        try {
            Map<String, Map<String, Object>> maps = objectMapper.readValue(json, Map.class);
            System.out.println(maps.size());
            Set<String> key = maps.keySet();
            Iterator<String> iter = key.iterator();
            while (iter.hasNext()) {
                String field = iter.next();
                System.out.println(field + ":" + maps.get(field));
            }
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * <b>function:</b>java对象转换成xml文档
     * 需要额外的jar包 stax2-api.jar
     * @author hoojo
     * @createDate 2010-11-23 下午06:11:21
     */
/*
    public void writeObject2Xml() {
        //stax2-api-3.0.2.jar
        System.out.println("XmlMapper");
        XmlMapper xml = new XmlMapper();
        try {
            //javaBean转换成xml
            //xml.writeValue(System.out, bean);
            StringWriter sw = new StringWriter();
            xml.writeValue(sw, bean);
            System.out.println(sw.toString());
            //List转换成xml
            List<AccountBean> list = new ArrayList<AccountBean>();
            list.add(bean);
            list.add(bean);
            System.out.println(xml.writeValueAsString(list));

            //Map转换xml文档
            Map<String, AccountBean> map = new HashMap<String, AccountBean>();
            map.put("A", bean);
            map.put("B", bean);
            System.out.println(xml.writeValueAsString(map));
        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
  */
    public static void main(String[] args) {
        JacksonUtil jacksonUtil = new JacksonUtil();
        String json = "{\"DstUrl\":\"http://www.baidu.com/30\",\"Name\":\"百度\",\"Key\":\"link\",\"Express\":\"\",\"Methd\":\"link\",\"Flag\":\"0\", \"JobId\":\"001\",\"Status\":\"0\",\"IsAuthentication\":\"0\", \"Authentication_id\":\"001\"}";
        HttpClientUtil httpClientUtil = new HttpClientUtil();
        String jobJson = httpClientUtil.getText("http://localhost:8080/datacenter/spider.json");
        jacksonUtil.readJson2Map(json);
        System.out.println(jobJson);
    }
}
