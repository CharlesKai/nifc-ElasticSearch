package com.nifc.es.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @ClassName DateUtils
 * @Description 日期工具类
 * @Author charlesYan
 * @Date 2019/8/6 9:44
 * @Version 1.0
 **/
public class DateUtils {

    /**
     * @Author charlesYan
     * @Description 将String类型日期转换成Date类型
     * @Date 10:04 2019/8/6
     * @Param [aMask, strDate]
     * @return java.util.Date
     **/
    public static final Date convertStringToDate(String aMask, String strDate){
        SimpleDateFormat df = null;
        Date date = null;
        if(null != strDate && strDate.length()>0){
            strDate = strDate.replace("Z"," UTC");
            df = new SimpleDateFormat(aMask);
            try {
                date = df.parse(strDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return date;
    }


    public static final String formarDateToStr(String aMask, Date date){
        SimpleDateFormat df = null;
        String formateDate = null;
        if(null != date){
            df = new SimpleDateFormat(aMask);
            formateDate = df.format(date);
        }

        return formateDate;
    }

    public static void main(String[] args) {
        String strDate = "2019-08-05T06:36:15.000Z";
        strDate = strDate.replace("Z"," UTC");
        Date date = DateUtils.convertStringToDate("yyyy-MM-dd'T'HH:mm:ss.SSS Z", strDate);
        System.out.println(date);
        long time = date.getTime();
        System.out.println("时间串：" + time);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strDateFormat = sdf.format(date);
        System.out.println(strDateFormat);

    }
}
