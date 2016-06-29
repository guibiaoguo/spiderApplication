package com.spider.collection.pipeline;

import com.spider.collection.entity.Tasks;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;
import us.codecraft.webmagic.utils.FilePersistentBase;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2015/10/31.
 */
public class CsvFilePipline extends FilePersistentBase implements Pipeline {

    private Logger logger = LoggerFactory.getLogger(getClass());
    private List<Tasks> tasks;
    private String fileName;
    public CsvFilePipline(List<Tasks> tasks,String path,String fileName) {
        setPath(path);
        this.tasks = tasks;
        this.fileName = fileName;
    }
    @Override
    public void process(ResultItems resultItems, Task task) {
        try {
            String path = this.path + fileName;
            PrintWriter printWriter = new PrintWriter(new FileWriter(getFile(path),true));
            StringBuffer buffer = new StringBuffer();
            List<String[]> data = new ArrayList<>();
            for (int i = 0; i <tasks.size(); i++) {
                if(tasks.get(i).getFlag() == 0) {
                    String temp = tasks.get(i).getKey();
                    String temp1= resultItems.getAll().get(temp).toString();
                    String[] temps = temp1.substring(1,temp1.length()-1).split(",");
                    data.add(temps);
                }
            }
            String[] tempData = data.get(0);
            File file = new File(path);
            BufferedReader reader = null;
            String temp2 = null;
//            if(reader.markSupported()) {
//                System.out.println("mark supported.");
//                reader.mark((int)file.length()+1);// change to 15, Mark invalid occurs
//            }
            for (int i = 0; i < tempData.length; i++) {
                reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
                boolean flag = true;
                for (int j = 0; j < data.size(); j++) {
                    while(flag && (temp2=reader.readLine())!=null) {
                        Pattern pattern = Pattern.compile(escapeExprSpecialWord(StringUtils.trim(data.get(j)[i])));
                        Matcher matcher = pattern.matcher(temp2);
                        if(matcher.find()) {
                            reader.close();
                            break;
                        }
                    }
                    if(temp2 == null) {
                        buffer.append(StringUtils.trim(data.get(j)[i]));
                        flag = false;
                        if(j<data.size()-1)
                            buffer.append("，");
                    } else {
                        break;
                    }
                }
                if(temp2 == null)
                    buffer.append("\n");
            }
            System.out.println(buffer.toString());
            printWriter.write(buffer.toString());
            printWriter.close();
        } catch (IOException e) {
            logger.warn("write file error", e);
        }
    }

    /**
     * 转义正则特殊字符 （$()*+.[]?\^{},|）
     *
     * @param keyword
     * @return
     */
    public static String escapeExprSpecialWord(String keyword) {
        if (StringUtils.isNotBlank(keyword)) {
            String[] fbsArr = { "\\", "$", "(", ")", "*", "+", ".", "[", "]", "?", "^", "{", "}", "|" };
            for (String key : fbsArr) {
                if (keyword.contains(key)) {
                    keyword = keyword.replace(key, "\\" + key);
                }
            }
        }
        return keyword;
    }
}
