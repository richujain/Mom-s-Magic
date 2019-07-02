package com.pulimoottil.richu.momsmagickeralafoodrecipe;

public class Items {
    private String title, imageurl, youtubeurl,videoby,likescount,dislikescount;
    public Items(){

    }

    public String getLikescount() {
        return likescount;
    }




    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageurl;
    }

    public String getVideoBy() {
        return videoby;
    }

    public Items(String title, String imageurl, String youtubeurl,String videoby,int likescount) {
        this.title = title;
        this.imageurl = imageurl;
        this.youtubeurl = youtubeurl;
        this.videoby = videoby;
        this.likescount = videoby;
    }
}
