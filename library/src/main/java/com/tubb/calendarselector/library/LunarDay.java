package com.tubb.calendarselector.library;

import java.util.HashMap;

/**
 * Created by hui.xiong on 2016/2/29.
 */
public class LunarDay {

    private static HashMap<String, String> lunarHolidayMap;

    private static String[] lunarNormalDay = {
        "初一","初二","初三","初四","初五","初六","初七","初八","初九","初十",
                "十一","十二","十三","十四","十五","十六","十七","十八","十九","廿",
                "廿一","廿二","廿三","廿四","廿五","廿六","廿七","廿八","廿九","三十"
    };

    static{
        lunarHolidayMap = new HashMap<>();
        lunarHolidayMap.put("0101", "春节");
        lunarHolidayMap.put("0115", "元宵节");
        lunarHolidayMap.put("0505", "端午节");
        lunarHolidayMap.put("0715", "中元节");
        lunarHolidayMap.put("0815", "中秋节");
        lunarHolidayMap.put("0909", "重阳节");
        lunarHolidayMap.put("1208", "腊八节");
        lunarHolidayMap.put("1224", "小年");

        lunarHolidayMap.put("0201","二月");
        lunarHolidayMap.put("0301","三月");
        lunarHolidayMap.put("0401","四月");
        lunarHolidayMap.put("0501","五月");
        lunarHolidayMap.put("0601","六月");
        lunarHolidayMap.put("0701","七月");
        lunarHolidayMap.put("0801","八月");
        lunarHolidayMap.put("0901","九月");
        lunarHolidayMap.put("1001","十月");
        lunarHolidayMap.put("1101","十一月");
        lunarHolidayMap.put("1201","十二月");

       /* lunarNormalDayMap=new HashMap<>();
        lunarNormalDayMap.put("02","初二");lunarNormalDayMap.put("11","十一");lunarNormalDayMap.put("21","廿一");
        lunarNormalDayMap.put("03","初三");lunarNormalDayMap.put("12","十二");lunarNormalDayMap.put("22","廿二");
        lunarNormalDayMap.put("04","初四");lunarNormalDayMap.put("13","十三");lunarNormalDayMap.put("23","廿三");
        lunarNormalDayMap.put("05","初五");lunarNormalDayMap.put("14","十四");lunarNormalDayMap.put("24","廿四");
        lunarNormalDayMap.put("06","初六");lunarNormalDayMap.put("15","十五");lunarNormalDayMap.put("25","廿五");
        lunarNormalDayMap.put("07","初七");lunarNormalDayMap.put("16","十六");lunarNormalDayMap.put("26","廿六");
        lunarNormalDayMap.put("08","初八");lunarNormalDayMap.put("17","十七");lunarNormalDayMap.put("27","廿七");
        lunarNormalDayMap.put("09","初九");lunarNormalDayMap.put("18","十八");lunarNormalDayMap.put("28","廿八");
        lunarNormalDayMap.put("10","初十");lunarNormalDayMap.put("19","十九");lunarNormalDayMap.put("29","廿九");
                                           lunarNormalDayMap.put("20","廿");lunarNormalDayMap.put("30","三十");*/
    }

    public static String getDayStr(int month,int day){

        String monthDayStr = getMonthDayStr(month, day);
        if(lunarHolidayMap.containsKey(monthDayStr)){
                return lunarHolidayMap.get(monthDayStr);
        }else {
            return lunarNormalDay[day-1];
        }
    }

    public static String getMonthDayStr(int month, int day) {

        String monthStr = month+"";
        if(month < 10){
            monthStr = "0"+monthStr;
        }
        String dayStr = day + "";
        if(day < 10){
            dayStr = "0" + day;
        }

        return monthStr + dayStr;
    }

}
