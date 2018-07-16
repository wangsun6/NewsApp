package com.becsupport.android.arunachalnews.earn;

/**
 * Created by WANGSUN on 23-Jul-17.
 */

public class App_info {

    String name,url,define,link;
    int priority,day_click,total_click;

    public App_info() {
    }

    public App_info(String name, String url, String define,String link,int priority, int day_click, int total_click) {
        this.name=name;
        this.link=link;
        this.priority = priority;
        this.url = url;
        this.define = define;
        this.day_click = day_click;
        this.total_click = total_click;
    }

    public String getName() {
        return name;
    }

    public String getLink() {
        return link;
    }

    public String getUrl() {
        return url;
    }

    public String getDefine() {
        return define;
    }

    public int getPriority() {
        return priority;
    }

    public int getDay_click() {
        return day_click;
    }

    public int getTotal_click() {
        return total_click;
    }
}
