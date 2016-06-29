package com.spider.collection.util;

/**
 * Created by Administrator on 2015/11/3.
 */
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;

import java.io.*;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CertNoUtil {

    public static boolean vIDNumByRegex(String idNum) {

        String curYear = "" + Calendar.getInstance().get(Calendar.YEAR);
        int y3 = Integer.valueOf(curYear.substring(2, 3));
        int y4 = Integer.valueOf(curYear.substring(3, 4));
        // 43 0103 1973 0 9 30 051 9
        return idNum.matches("^(1[1-5]|2[1-3]|3[1-7]|4[1-6]|5[0-4]|6[1-5]|71|8[1-2])\\d{4}(19\\d{2}|20([0-" + (y3 - 1) + "][0-9]|" + y3 + "[0-" + y4
                + "]))(((0[1-9]|1[0-2])(0[1-9]|[1-2][0-9]|3[0-1])))\\d{3}([0-9]|x|X)$");
        // 44 1825 1988 0 7 1 3 003 4
    }

    private static int ID_LENGTH = 17;
    public static boolean vIDNumByCode(String idNum) {

        // 系数列表
        int[] ratioArr = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};

        // 校验码列表
        char[] checkCodeList = {'1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'};

        // 获取身份证号字符数组
        char[] cIds = idNum.toCharArray();

        // 获取最后一位（身份证校验码）
        char oCode = cIds[ID_LENGTH];
        int[] iIds = new int[ID_LENGTH];
        int idSum = 0;// 身份证号第1-17位与系数之积的和
        int residue = 0;// 余数(用加出来和除以11，看余数是多少？)

        for (int i = 0; i < ID_LENGTH; i++) {
            iIds[i] = cIds[i] - '0';
            idSum += iIds[i] * ratioArr[i];
        }

        residue = idSum % 11;// 取得余数

        return Character.toUpperCase(oCode) == checkCodeList[residue];
    }

    public static boolean vId(String idNum) {
        return vIDNumByCode(idNum) && vIDNumByRegex(idNum);
    }

    public static String certMsg (String idNum) throws IOException{
        if(vId(idNum)) {
            String curYear = "" + Calendar.getInstance().get(Calendar.YEAR);
            int y3 = Integer.valueOf(curYear.substring(2, 3));
            int y4 = Integer.valueOf(curYear.substring(3, 4));
            Pattern pattern = Pattern.compile("^((1[1-5]|2[1-3]|3[1-7]|4[1-6]|5[0-4]|6[1-5]|71|8[1-2])\\d{4})((19\\d{2}|20([0-" + (y3 - 1) + "][0-9]|" + y3 + "[0-" + y4
                    + "]))(((0[1-9]|1[0-2])(0[1-9]|[1-2][0-9]|3[0-1]))))(\\d{3})([0-9]|x|X)$");
            Matcher matcher = pattern.matcher(idNum);
            if (matcher.find()) {
                File file = new File("C:\\Users\\Administrator\\Desktop\\sfzbm.xml");
                BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
                String temp = null;
                String tempRegex = "  <R c=\""+matcher.group(1)+"\".*t=\"(.*?)\".*";
                System.out.println(tempRegex);
                while((temp = reader.readLine())!=null) {
                   // System.out.println(temp);
                    Pattern pattern1 = Pattern.compile(tempRegex);
                    Matcher matcher1 = pattern1.matcher(temp);
                    while (matcher1.find()) {
                        System.out.println("身份证号码：" + idNum +"\n"+ "发证地：" + matcher1.group(1)+"\n"+"出生日期：" + matcher.group(4)+"年"+matcher.group(8)+"月"+matcher.group(9)+"日\n"+"性别："+(Integer.parseInt(matcher.group(10))%2 == 0?"女":"男"));
                    }

                }
                System.out.println(matcher.group(1));
                System.out.println(matcher.group(2));
                System.out.println(matcher.group(3));
                System.out.println(matcher.group(4));
                System.out.println(matcher.group(5));
                System.out.println(matcher.group(6));
                System.out.println(matcher.group(7));
                System.out.println(matcher.group(8));
                System.out.println(matcher.group(9));
                System.out.println(matcher.group(10));
                System.out.println(matcher.group(11));
            }
            System.out.println();
        }
        return  null;
    }
    public static void main(String[] args) throws IOException{
        String idNum = "630104198401234794";
        System.out.println(vId(idNum));
        certMsg(idNum);
    }
}
