package com.shuiguo.utils;

public class CityCode {

    public static int getShortCityName(String City) {

        int city;
        switch (City) {
            case "北京":
                city = 1;
                break;
            case "上海":
                city = 2;
                break;
            case "天津":
                city = 3;
                break;
            case "重庆":
                city = 4;
                break;
            case "黑龙江":
                city = 5;
                break;
            case "吉林":
                city = 6;
                break;
            case "辽宁":
                city = 7;
                break;
            case "内蒙古":
                city = 8;
                break;
            case "河北":
                city = 9;
                break;
            case "山西":
                city = 10;
                break;
            case "陕西":
                city = 11;
                break;
            case "山东":
                city = 12;
                break;
            case "新疆":
                city = 13;
                break;
            case "西藏":
                city = 14;
                break;
            case "青海":
                city = 15;
                break;
            case "甘肃":
                city = 16;
                break;
            case "宁夏":
                city = 17;
                break;
            case "河南":
                city = 18;
                break;
            case "江苏":
                city = 19;
                break;
            case "湖北":
                city = 20;
                break;
            case "浙江":
                city = 21;
                break;
            case "安徽":
                city = 22;
                break;
            case "福建":
                city = 23;
                break;
            case "江西":
                city = 24;
                break;
            case "湖南":
                city = 25;
                break;
            case "贵州":
                city = 26;
                break;
            case "四川":
                city = 27;
                break;
            case "广东":
                city = 28;
                break;
            case "云南":
                city = 29;
                break;
            case "广西":
                city = 30;
                break;
            case "海南":
                city = 31;
                break;
            case "香港":
                city = 32;
                break;
            case "澳门":
                city = 33;
                break;
            case "台湾":
                city = 34;
                break;
            default:
                city = 0;
                break;
        }
        return city;
    }

    public static String getCity(int cityCode){

        String cityName = "";
        switch (cityCode) {
            case 1:
                cityName = "北京";
                break;
            case 2:
                cityName = "上海";
                break;
            case 3:
                cityName = "天津";
                break;
            case 4:
                cityName = "重庆";
                break;
            case 5:
                cityName = "黑龙江";
                break;
            case 6:
                cityName = "吉林";
                break;
            case 7:
                cityName = "辽宁";
                break;
            case 8:
                cityName = "内蒙古";
                break;
            case 9:
                cityName = "河北";
                break;
            case 10:
                cityName = "山西";
                break;
            case 11:
                cityName = "陕西";
                break;
            case 12:
                cityName = "山东";
                break;
            case 13:
                cityName = "新疆";
                break;
            case 14:
                cityName = "西藏";
                break;
            case 15:
                cityName = "青海";
                break;
            case 16:
                cityName = "甘肃";
                break;
            case 17:
                cityName = "宁夏";
                break;
            case 18:
                cityName = "河南";
                break;
            case 19:
                cityName = "江苏";
                break;
            case 20:
                cityName = "湖北";
                break;
            case 21:
                cityName = "浙江";
                break;
            case 22:
                cityName = "安徽";
                break;
            case 23:
                cityName = "福建";
                break;
            case 24:
                cityName = "江西";
                break;
            case 25:
                cityName = "湖南";
                break;
            case 26:
                cityName = "贵州";
                break;
            case 27:
                cityName = "四川";
                break;
            case 28:
                cityName = "广东";
                break;
            case 29:
                cityName = "云南";
                break;
            case 30:
                cityName = "广西";
                break;
            case 31:
                cityName = "海南";
                break;
            case 32:
                cityName = "香港";
                break;
            case 33:
                cityName = "澳门";
                break;
            case 34:
                cityName = "台湾";
                break;
            default:
                cityName = "重庆";
                break;
        }

        return cityName;
    }

}
