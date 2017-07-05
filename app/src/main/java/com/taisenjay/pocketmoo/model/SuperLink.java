package com.taisenjay.pocketmoo.model;

/**
 * Author : WangJian
 * Date   : 2017/7/1
 * Created by a handsome boy with love
 */

public class SuperLink {

    public SuperLink(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String name;
    public String url;

    @Override
    public String toString() {
        return "SuperLink{" +
                "name='" + name + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
