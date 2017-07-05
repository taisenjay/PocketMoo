package com.taisenjay.pocketmoo.model;

/**
 * Author : WangJian
 * Date   : 2017/7/2
 * Created by a handsome boy with love
 */

public class Torrent {
    public String address;
    public String name;
    public String date;
    public String size;

    @Override
    public String toString() {
        return "Torrent{" +
                "address='" + address + '\'' +
                ", name='" + name + '\'' +
                ", date='" + date + '\'' +
                ", size='" + size + '\'' +
                '}';
    }
}
