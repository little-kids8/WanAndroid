package com.example.littlekids8.wanandroid.gson;

import com.google.gson.annotations.SerializedName;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;
import org.litepal.crud.LitePalSupport;

import java.io.Serializable;
import java.util.List;

public class Article extends LitePalSupport implements Serializable{

    /**
     * apkLink :
     * author : maning0303
     * chapterId : 358
     * chapterName : 项目基础功能
     * collect : false13
     * courseId :
     * desc : MNCrashMonitor 监听程序崩溃日志,直接页面展示崩溃日志列表,调试方便,测试人员可以随时给程序猿查看日志详情,可以动态添加日志内容,手机直接查看日志内容可以分享,复制,生成长截图。

     * envelopePic : http://www.wanandroid.com/blogimgs/2eef4656-46c9-41d9-9196-b11186b97174.png
     * fresh : true
     * id : 3115
     * link : http://www.wanandroid.com/blog/show/2207
     * niceDate : 23小时前
     * origin :
     * projectLink : https://github.com/maning0303/MNCrashMonitor
     * publishTime : 1530951818000
     * superChapterId : 294
     * superChapterName : 开源项目主Tab
     * tags : [{"name":"项目","url":"/project/list/1?cid=358"}]
     * title : MNCrashMonitor 监听程序崩溃日志,直接页面展示崩溃日志列表
     * type : 0
     * userId : -1
     * visible : 1
     * zan : 0
     */

    private String author;
    private int chapterId;
    private String chapterName;
    private int courseId;
    private String desc;
    private String envelopePic;
    private boolean fresh;

    @SerializedName("id")
    @Column(unique = true)
    private int itemId;

    @Column(nullable = false)
    private String link;
    private String niceDate;
    private String projectLink;
    private long publishTime;
    private int superChapterId;
    private String superChapterName;
    private String title;



    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getChapterId() {
        return chapterId;
    }

    public void setChapterId(int chapterId) {
        this.chapterId = chapterId;
    }

    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getEnvelopePic() {
        return envelopePic;
    }

    public void setEnvelopePic(String envelopePic) {
        this.envelopePic = envelopePic;
    }

    public boolean isFresh() {
        return fresh;
    }

    public void setFresh(boolean fresh) {
        this.fresh = fresh;
    }


    public void setItemId(int itemId){this.itemId = itemId;}


    public int getItemId(){return itemId;}

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getNiceDate() {
        return niceDate;
    }

    public void setNiceDate(String niceDate) {
        this.niceDate = niceDate;
    }

    public String getProjectLink() {
        return projectLink;
    }

    public void setProjectLink(String projectLink) {
        this.projectLink = projectLink;
    }

    public long getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(long publishTime) {
        this.publishTime = publishTime;
    }

    public int getSuperChapterId() {
        return superChapterId;
    }

    public void setSuperChapterId(int superChapterId) {
        this.superChapterId = superChapterId;
    }

    public String getSuperChapterName() {
        return superChapterName;
    }

    public void setSuperChapterName(String superChapterName) {
        this.superChapterName = superChapterName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


}
