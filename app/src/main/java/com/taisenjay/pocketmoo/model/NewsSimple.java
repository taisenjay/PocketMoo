package com.taisenjay.pocketmoo.model;

import java.io.Serializable;

/**
 * Author : WangJian
 * Date   : 2017/7/3
 * Created by a handsome boy with love
 */

public class NewsSimple implements Serializable{
    public String detailUrl;
    public String title;
    public String coverUrl;
    public String date;
    public String contentPart;

//    @Override
//    public String toString() {
//        return "NewsSimple{" +
//                "detailUrl='" + detailUrl + '\'' +
//                ", title='" + title + '\'' +
//                ", coverUrl='" + coverUrl + '\'' +
//                ", date='" + date + '\'' +
//                ", contentPart='" + contentPart + '\'' +
//                '}';
//    }
}
