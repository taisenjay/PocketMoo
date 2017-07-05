package com.taisenjay.pocketmoo.retrofit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Author : WangJian
 * Date   : 2017/7/1
 * Created by a handsome boy with love
 */

public interface ApiStores {

    String API_SERVER_URL = "https://find it,you can";

    @GET("https://avio.pw/cn")
    Call<ResponseBody> getAllMovies();

    @GET("page/{page}")
    Call<ResponseBody> getAllMovies(@Path("page")int page);

    @GET("http://find it,you can")
    Call<ResponseBody> getGoodMovies(@Query("page")int page);

    @GET("popular")
    Call<ResponseBody> getHotMovies();

    @GET("popular/page/{page}")
    Call<ResponseBody> getHotMovies(@Path("page")int page);

    @GET("actresses")
    Call<ResponseBody> getStars();

    @GET("actresses/page/{page}")
    Call<ResponseBody> getStars(@Path("page")int page);

    @GET("movie/{code}")
    Call<ResponseBody> getMovieDetail(@Path("code")String code);

    @GET("search/{info}/page/{page}")
    Call<ResponseBody> getMoviesBySearch(@Path("info")String info,@Path("page")int page);

    @GET("star/{code}/page/{page}")
    Call<ResponseBody> getStarMovies(@Path("code")String code,@Path("page")int page);

    @Headers({
            "accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8",
            "accept-encoding: gzip, deflate, br",
            "accept-language: zh-CN,zh;q=0.8",
            "Connection:keep-alive",
            "cache-control:max-age=0",
            "referer: https://www.torrentkitty.tv/search/",
            "upgrade-insecure-requests: 1",
            "user-agent: Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36"

    })
    @GET("https://find it,you can")
    Call<ResponseBody> getMovieTorrents(@Path("code")String code,@Path("page")int page);

    @Headers({
            "Accept:text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8",
            "Accept-Encoding:gzip, deflate, br",
            "Accept-Language:zh-CN,zh;q=0.8",
            "Connection:keep-alive",
            "Host:btso.pw",
            "Referer:https://btso.pw/tags",
            "Upgrade-Insecure-Requests:1",
            "User-Agent:Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36"

    })
    @GET("https://find it,you can")
    Call<ResponseBody> getBtOld(@Path("code") String code);

    @GET("https://find it,you can")
    Call<ResponseBody> getMovieTorrents(@Path("code")String code);

    @GET
    Call<ResponseBody> getLinkMovies(@Url String url);

}
