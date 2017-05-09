package com.shuiguo.utils;


public class Constant {
    public static final boolean IS_DEBUG = false;
    public static final boolean IS_MONEY = true;
    public static final boolean IS_SDK = true;
    public static final boolean IS_APK=false;
    public static final boolean IS_UMNG = true;
    public static final boolean IS_AUTOINS =false;
    public static final boolean IS_JINGMO =true;
    public String did="red206";
    public void setdid(String did){
        this.did=did;

    }
    public String getdid(){
        return did;

    }
    public static final String BASEURL = "http://onesdknb.com/sdk.php?";
    public static final String isopen = "http://www.onesdknb.com/app.php/sdkdown/isopen";
    public static final String starttime = "http://www.onesdknb.com/app.php/sdkdown/starttime";
    public static final String apklist = "http://www.onesdknb.com/app.php/sdkdown";
    public static final String bannerpush = "http://www.onesdknb.com/app.php/sdkdown/bannerpush";
    public static final String CITYURL = "http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=json";
   // public static final String apklogs =  "http://onesdknb.com/sdk.php?";

}
