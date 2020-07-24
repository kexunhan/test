package com.example.test.kolmap;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.example.test.dingtalk.DingTalkUtils;
import jodd.jerry.Jerry;
import org.apache.log4j.Logger;
import org.bson.Document;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.model.HttpRequestBody;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.utils.HttpConstant;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static jodd.jerry.Jerry.jerry;

@Component
public class FeiGuaDynamicParamBloggerPageProcessor implements PageProcessor {

    Logger log = Logger.getLogger(FeiGuaDynamicParamBloggerPageProcessor.class);

    String hostAddr = "https://dy.feigua.cn/";

    private int pageNo = 1;

    public List<String> getParamsList() {
        return paramsList;
    }

    public String getHostAddr() {
        return hostAddr;
    }

    List<String> paramsList = Arrays.asList(

            // 网红美女
            "isWithCommerceEntry=1&multiTag=网红美女&fans=10-50&likes=10-50",
            "isWithCommerceEntry=1&multiTag=网红美女&fans=10-50&likes=50-100&age=18-24",
            "isWithCommerceEntry=1&multiTag=网红美女&fans=10-50&likes=50-100&age=25-30",
            "isWithCommerceEntry=1&multiTag=网红美女&fans=10-50&likes=50-100&age=31-35",
            "isWithCommerceEntry=1&multiTag=网红美女&fans=10-50&likes=50-100&age=36-40",
            "isWithCommerceEntry=1&multiTag=网红美女&fans=10-50&likes=50-100&age=40",
            "isWithCommerceEntry=1&multiTag=网红美女&fans=10-50&likes=100-200&age=18-24",
            "isWithCommerceEntry=1&multiTag=网红美女&fans=10-50&likes=100-200&age=25-30",
            "isWithCommerceEntry=1&multiTag=网红美女&fans=10-50&likes=100-200&age=31-35",
            "isWithCommerceEntry=1&multiTag=网红美女&fans=10-50&likes=100-200&age=36-40",
            "isWithCommerceEntry=1&multiTag=网红美女&fans=10-50&likes=100-200&age=40",
            "isWithCommerceEntry=1&multiTag=网红美女&fans=10-50&likes=200",
            "isWithCommerceEntry=1&multiTag=网红美女&fans=50-100",
            "isWithCommerceEntry=1&multiTag=网红美女&fans=100-500",
            "isWithCommerceEntry=1&multiTag=网红美女&fans=500-1000",
            "isWithCommerceEntry=1&multiTag=网红美女&fans=1000",



            // 网红帅哥
            "isWithCommerceEntry=1&multiTag=网红帅哥&fans=10-50",
            "isWithCommerceEntry=1&multiTag=网红帅哥&fans=50-100",
            "isWithCommerceEntry=1&multiTag=网红帅哥&fans=100-500",
            "isWithCommerceEntry=1&multiTag=网红帅哥&fans=500-1000",
            "isWithCommerceEntry=1&multiTag=网红帅哥&fans=1000",

            // 搞笑
            "isWithCommerceEntry=1&multiTag=搞笑&fans=10-50&likes=10-50",
            "isWithCommerceEntry=1&multiTag=搞笑&fans=10-50&likes=50-100",
            "isWithCommerceEntry=1&multiTag=搞笑&fans=10-50&likes=100-200",
            "isWithCommerceEntry=1&multiTag=搞笑&fans=10-50&likes=200",

            "isWithCommerceEntry=1&multiTag=搞笑&fans=50-100",
            "isWithCommerceEntry=1&multiTag=搞笑&fans=100-500",
            "isWithCommerceEntry=1&multiTag=搞笑&fans=500-1000",
            "isWithCommerceEntry=1&multiTag=搞笑&fans=1000",

            // 情感
            "isWithCommerceEntry=1&multiTag=情感&fans=10-50&likes=10-50",
            "isWithCommerceEntry=1&multiTag=情感&fans=10-50&likes=50-100",
            "isWithCommerceEntry=1&multiTag=情感&fans=10-50&likes=100-200",
            "isWithCommerceEntry=1&multiTag=情感&fans=10-50&likes=200",
            "isWithCommerceEntry=1&multiTag=情感&fans=50-100",
            "isWithCommerceEntry=1&multiTag=情感&fans=100-500",
            "isWithCommerceEntry=1&multiTag=情感&fans=500-1000",
            "isWithCommerceEntry=1&multiTag=情感&fans=1000",

            // 剧情
            "isWithCommerceEntry=1&multiTag=剧情&fans=10-50",
            "isWithCommerceEntry=1&multiTag=剧情&fans=50-100",
            "isWithCommerceEntry=1&multiTag=剧情&fans=100-500",
            "isWithCommerceEntry=1&multiTag=剧情&fans=500-1000",
            "isWithCommerceEntry=1&multiTag=剧情&fans=1000",

            // 美食
            "isWithCommerceEntry=1&multiTag=美食&fans=10-50&likes=10-50",
            "isWithCommerceEntry=1&multiTag=美食&fans=10-50&likes=50-100",
            "isWithCommerceEntry=1&multiTag=美食&fans=10-50&likes=100-200",
            "isWithCommerceEntry=1&multiTag=美食&fans=10-50&likes=200",
            "isWithCommerceEntry=1&multiTag=美食&fans=50-100",
            "isWithCommerceEntry=1&multiTag=美食&fans=100-500",
            "isWithCommerceEntry=1&multiTag=美食&fans=500-1000",
            "isWithCommerceEntry=1&multiTag=美食&fans=1000",

            // 美妆
            "isWithCommerceEntry=1&multiTag=美妆&fans=10-50",
            "isWithCommerceEntry=1&multiTag=美妆&fans=50-100",
            "isWithCommerceEntry=1&multiTag=美妆&fans=100-500",
            "isWithCommerceEntry=1&multiTag=美妆&fans=500-1000",
            "isWithCommerceEntry=1&multiTag=美妆&fans=1000",

            // 种草
            "isWithCommerceEntry=1&multiTag=种草&fans=10-50&likes=10-50",
            "isWithCommerceEntry=1&multiTag=种草&fans=10-50&likes=50-100",
            "isWithCommerceEntry=1&multiTag=种草&fans=10-50&likes=100-200",
            "isWithCommerceEntry=1&multiTag=种草&fans=10-50&likes=200",
            "isWithCommerceEntry=1&multiTag=种草&fans=50-100",
            "isWithCommerceEntry=1&multiTag=种草&fans=100-500",
            "isWithCommerceEntry=1&multiTag=种草&fans=500-1000",
            "isWithCommerceEntry=1&multiTag=种草&fans=1000",

            // 穿搭
            "isWithCommerceEntry=1&multiTag=穿搭&fans=10-50&likes=10-50",
            "isWithCommerceEntry=1&multiTag=穿搭&fans=10-50&likes=50-100",
            "isWithCommerceEntry=1&multiTag=穿搭&fans=10-50&likes=100-200",
            "isWithCommerceEntry=1&multiTag=穿搭&fans=10-50&likes=200",
            "isWithCommerceEntry=1&multiTag=穿搭&fans=50-100",
            "isWithCommerceEntry=1&multiTag=穿搭&fans=100-500",
            "isWithCommerceEntry=1&multiTag=穿搭&fans=500-1000",
            "isWithCommerceEntry=1&multiTag=穿搭&fans=1000",

            // 明星
            "isWithCommerceEntry=1&multiTag=明星&fans=10-50",
            "isWithCommerceEntry=1&multiTag=明星&fans=50-100",
            "isWithCommerceEntry=1&multiTag=明星&fans=100-500",
            "isWithCommerceEntry=1&multiTag=明星&fans=500-1000",
            "isWithCommerceEntry=1&multiTag=明星&fans=1000",

            // 影视娱乐
            "isWithCommerceEntry=1&multiTag=影视娱乐&fans=10-50&likes=10-50",
            "isWithCommerceEntry=1&multiTag=影视娱乐&fans=10-50&likes=50-100",
            "isWithCommerceEntry=1&multiTag=影视娱乐&fans=10-50&likes=100-200",
            "isWithCommerceEntry=1&multiTag=影视娱乐&fans=10-50&likes=200",
            "isWithCommerceEntry=1&multiTag=影视娱乐&fans=50-100",
            "isWithCommerceEntry=1&multiTag=影视娱乐&fans=100-500",
            "isWithCommerceEntry=1&multiTag=影视娱乐&fans=500-1000",
            "isWithCommerceEntry=1&multiTag=影视娱乐&fans=1000",

            // 游戏
            "isWithCommerceEntry=1&multiTag=游戏&fans=10-50&likes=10-50",
            "isWithCommerceEntry=1&multiTag=游戏&fans=10-50&likes=50-100",
            "isWithCommerceEntry=1&multiTag=游戏&fans=10-50&likes=100-200",
            "isWithCommerceEntry=1&multiTag=游戏&fans=10-50&likes=200",
            "isWithCommerceEntry=1&multiTag=游戏&fans=50-100",
            "isWithCommerceEntry=1&multiTag=游戏&fans=100-500",
            "isWithCommerceEntry=1&multiTag=游戏&fans=500-1000",
            "isWithCommerceEntry=1&multiTag=游戏&fans=1000",


            // 宠物
            "isWithCommerceEntry=1&multiTag=宠物&fans=10-50",
            "isWithCommerceEntry=1&multiTag=宠物&fans=50-100",
            "isWithCommerceEntry=1&multiTag=宠物&fans=100-500",
            "isWithCommerceEntry=1&multiTag=宠物&fans=500-1000",
            "isWithCommerceEntry=1&multiTag=宠物&fans=1000",

            // 音乐
            "isWithCommerceEntry=1&multiTag=音乐&fans=10-50&likes=10-50",
            "isWithCommerceEntry=1&multiTag=音乐&fans=10-50&likes=50-100",
            "isWithCommerceEntry=1&multiTag=音乐&fans=10-50&likes=100-200",
            "isWithCommerceEntry=1&multiTag=音乐&fans=10-50&likes=200",
            "isWithCommerceEntry=1&multiTag=音乐&fans=50-100",
            "isWithCommerceEntry=1&multiTag=音乐&fans=100-500",
            "isWithCommerceEntry=1&multiTag=音乐&fans=500-1000",
            "isWithCommerceEntry=1&multiTag=音乐&fans=1000",

            // 舞蹈
            "isWithCommerceEntry=1&multiTag=舞蹈&fans=10-50",
            "isWithCommerceEntry=1&multiTag=舞蹈&fans=50-100",
            "isWithCommerceEntry=1&multiTag=舞蹈&fans=100-500",
            "isWithCommerceEntry=1&multiTag=舞蹈&fans=500-1000",
            "isWithCommerceEntry=1&multiTag=舞蹈&fans=1000",

            // 萌娃
            "isWithCommerceEntry=1&multiTag=萌娃&fans=10-50",
            "isWithCommerceEntry=1&multiTag=萌娃&fans=50-100",
            "isWithCommerceEntry=1&multiTag=萌娃&fans=100-500",
            "isWithCommerceEntry=1&multiTag=萌娃&fans=500-1000",
            "isWithCommerceEntry=1&multiTag=萌娃&fans=1000",

            // 生活
            "isWithCommerceEntry=1&multiTag=生活&fans=10-50&likes=10-50",
            "isWithCommerceEntry=1&multiTag=生活&fans=10-50&likes=50-100&age=18-24",
            "isWithCommerceEntry=1&multiTag=生活&fans=10-50&likes=50-100&age=25-30",
            "isWithCommerceEntry=1&multiTag=生活&fans=10-50&likes=50-100&age=31-35",
            "isWithCommerceEntry=1&multiTag=生活&fans=10-50&likes=50-100&age=36-40",
            "isWithCommerceEntry=1&multiTag=生活&fans=10-50&likes=50-100&age=40",

            "isWithCommerceEntry=1&multiTag=生活&fans=10-50&likes=100-200&age=18-24",
            "isWithCommerceEntry=1&multiTag=生活&fans=10-50&likes=100-200&age=25-30",
            "isWithCommerceEntry=1&multiTag=生活&fans=10-50&likes=100-200&age=31-35",
            "isWithCommerceEntry=1&multiTag=生活&fans=10-50&likes=100-200&age=36-40",
            "isWithCommerceEntry=1&multiTag=生活&fans=10-50&likes=100-200&age=40",

            "isWithCommerceEntry=1&multiTag=生活&fans=10-50&likes=200&age=18-24",
            "isWithCommerceEntry=1&multiTag=生活&fans=10-50&likes=200&age=25-30",
            "isWithCommerceEntry=1&multiTag=生活&fans=10-50&likes=200&age=31-35",
            "isWithCommerceEntry=1&multiTag=生活&fans=10-50&likes=200&age=36-40",
            "isWithCommerceEntry=1&multiTag=生活&fans=10-50&likes=200&age=40",

            "isWithCommerceEntry=1&multiTag=生活&fans=50-100",
            "isWithCommerceEntry=1&multiTag=生活&fans=100-500",
            "isWithCommerceEntry=1&multiTag=生活&fans=500-1000",
            "isWithCommerceEntry=1&multiTag=生活&fans=1000",

            // 健康
            "isWithCommerceEntry=1&multiTag=健康&fans=10-50",
            "isWithCommerceEntry=1&multiTag=健康&fans=50-100",
            "isWithCommerceEntry=1&multiTag=健康&fans=100-500",
            "isWithCommerceEntry=1&multiTag=健康&fans=500-1000",
            "isWithCommerceEntry=1&multiTag=健康&fans=1000",

            // 体育
            "isWithCommerceEntry=1&multiTag=体育&fans=10-50",
            "isWithCommerceEntry=1&multiTag=体育&fans=50-100",
            "isWithCommerceEntry=1&multiTag=体育&fans=100-500",
            "isWithCommerceEntry=1&multiTag=体育&fans=500-1000",
            "isWithCommerceEntry=1&multiTag=体育&fans=1000",

            // 旅行
            "isWithCommerceEntry=1&multiTag=旅行&fans=10-50",
            "isWithCommerceEntry=1&multiTag=旅行&fans=50-100",
            "isWithCommerceEntry=1&multiTag=旅行&fans=100-500",
            "isWithCommerceEntry=1&multiTag=旅行&fans=500-1000",
            "isWithCommerceEntry=1&multiTag=旅行&fans=1000",

            // 动漫
            "isWithCommerceEntry=1&multiTag=动漫&fans=10-50",
            "isWithCommerceEntry=1&multiTag=动漫&fans=50-100",
            "isWithCommerceEntry=1&multiTag=动漫&fans=100-500",
            "isWithCommerceEntry=1&multiTag=动漫&fans=500-1000",
            "isWithCommerceEntry=1&multiTag=动漫&fans=1000",

            // 创意
            "isWithCommerceEntry=1&multiTag=创意&fans=10-50",
            "isWithCommerceEntry=1&multiTag=创意&fans=50-100",
            "isWithCommerceEntry=1&multiTag=创意&fans=100-500",
            "isWithCommerceEntry=1&multiTag=创意&fans=500-1000",
            "isWithCommerceEntry=1&multiTag=创意&fans=1000",

            // 时尚
            "isWithCommerceEntry=1&multiTag=时尚&fans=10-50",
            "isWithCommerceEntry=1&multiTag=时尚&fans=50-100",
            "isWithCommerceEntry=1&multiTag=时尚&fans=100-500",
            "isWithCommerceEntry=1&multiTag=时尚&fans=500-1000",
            "isWithCommerceEntry=1&multiTag=时尚&fans=1000",

            // 母婴育儿
            "isWithCommerceEntry=1&multiTag=母婴育儿&fans=10-50",
            "isWithCommerceEntry=1&multiTag=母婴育儿&fans=50-100",
            "isWithCommerceEntry=1&multiTag=母婴育儿&fans=100-500",
            "isWithCommerceEntry=1&multiTag=母婴育儿&fans=500-1000",
            "isWithCommerceEntry=1&multiTag=母婴育儿&fans=1000",

            // 教育
            "isWithCommerceEntry=1&multiTag=教育&fans=10-50",
            "isWithCommerceEntry=1&multiTag=教育&fans=50-100",
            "isWithCommerceEntry=1&multiTag=教育&fans=100-500",
            "isWithCommerceEntry=1&multiTag=教育&fans=500-1000",
            "isWithCommerceEntry=1&multiTag=教育&fans=1000",

            // 职场教育
            "isWithCommerceEntry=1&multiTag=职场教育&fans=10-50",
            "isWithCommerceEntry=1&multiTag=职场教育&fans=50-100",
            "isWithCommerceEntry=1&multiTag=职场教育&fans=100-500",
            "isWithCommerceEntry=1&multiTag=职场教育&fans=500-1000",
            "isWithCommerceEntry=1&multiTag=职场教育&fans=1000",

            // 汽车
            "isWithCommerceEntry=1&multiTag=汽车&fans=10-50",
            "isWithCommerceEntry=1&multiTag=汽车&fans=50-100",
            "isWithCommerceEntry=1&multiTag=汽车&fans=100-500",
            "isWithCommerceEntry=1&multiTag=汽车&fans=500-1000",
            "isWithCommerceEntry=1&multiTag=汽车&fans=1000",

            // 家具
            "isWithCommerceEntry=1&multiTag=家具&fans=10-50",
            "isWithCommerceEntry=1&multiTag=家具&fans=50-100",
            "isWithCommerceEntry=1&multiTag=家具&fans=100-500",
            "isWithCommerceEntry=1&multiTag=家具&fans=500-1000",
            "isWithCommerceEntry=1&multiTag=家具&fans=1000",

            // 科技
            "isWithCommerceEntry=1&multiTag=科技&fans=10-50",
            "isWithCommerceEntry=1&multiTag=科技&fans=50-100",
            "isWithCommerceEntry=1&multiTag=科技&fans=100-500",
            "isWithCommerceEntry=1&multiTag=科技&fans=500-1000",
            "isWithCommerceEntry=1&multiTag=科技&fans=1000",

            // 摄影教学
            "isWithCommerceEntry=1&multiTag=摄影教学&fans=10-50",
            "isWithCommerceEntry=1&multiTag=摄影教学&fans=50-100",
            "isWithCommerceEntry=1&multiTag=摄影教学&fans=100-500",
            "isWithCommerceEntry=1&multiTag=摄影教学&fans=500-1000",
            "isWithCommerceEntry=1&multiTag=摄影教学&fans=1000",

            // 政务
            "isWithCommerceEntry=1&multiTag=政务&fans=10-50",
            "isWithCommerceEntry=1&multiTag=政务&fans=50-100",
            "isWithCommerceEntry=1&multiTag=政务&fans=100-500",
            "isWithCommerceEntry=1&multiTag=政务&fans=500-1000",
            "isWithCommerceEntry=1&multiTag=政务&fans=1000",

            // 知识资讯
            "isWithCommerceEntry=1&multiTag=知识资讯&fans=10-50",
            "isWithCommerceEntry=1&multiTag=知识资讯&fans=50-100",
            "isWithCommerceEntry=1&multiTag=知识资讯&fans=100-500",
            "isWithCommerceEntry=1&multiTag=知识资讯&fans=500-1000",
            "isWithCommerceEntry=1&multiTag=知识资讯&fans=1000",

            // 办公软件
            "isWithCommerceEntry=1&multiTag=知识资讯&fans=10-50",
            "isWithCommerceEntry=1&multiTag=知识资讯&fans=50-100",
            "isWithCommerceEntry=1&multiTag=知识资讯&fans=100-500",
            "isWithCommerceEntry=1&multiTag=知识资讯&fans=500-1000",

            // 文学艺术
            "isWithCommerceEntry=1&multiTag=文学艺术&fans=10-50",
            "isWithCommerceEntry=1&multiTag=文学艺术&fans=50-100",
            "isWithCommerceEntry=1&multiTag=文学艺术&fans=100-500",
            "isWithCommerceEntry=1&multiTag=文学艺术&fans=500-1000",
            "isWithCommerceEntry=1&multiTag=文学艺术&fans=1000",

            // 手工手绘
            "isWithCommerceEntry=1&multiTag=手工手绘&fans=10-50",
            "isWithCommerceEntry=1&multiTag=手工手绘&fans=50-100",
            "isWithCommerceEntry=1&multiTag=手工手绘&fans=100-500",
            "isWithCommerceEntry=1&multiTag=手工手绘&fans=500-1000",

            // 户外
            "isWithCommerceEntry=1&multiTag=户外&fans=10-50",
            "isWithCommerceEntry=1&multiTag=户外&fans=50-100",
            "isWithCommerceEntry=1&multiTag=户外&fans=100-500",
            "isWithCommerceEntry=1&multiTag=户外&fans=500-1000"
    );

    public String getParam(){
        return paramsList.get(0);
    }


    private String params;
    private Site site;

    public Boolean getFlag() {
        return flag;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }

    private Boolean flag= false;

    public void setParams(String params) {
        this.params = params;
    }

    public void setSite(Site site) {
        this.site = site;
    }


    @Resource
    private ParseDataUtil parseDataUtil;

    @Resource
    private MongodbUtils mongodbUtils;

    @Override
    public void process(Page page) {
        //如果cookies超时了，就直接退出
        if (page.getHtml().toString().contains("登录/注册")|| page.getHtml().toString().contains("登陆超时")) {
            System.out.println("cookies 超时了");
            // cookie失效
            DingTalkUtils.robotSendMessage();
            Document document = new Document();
            document.append("pageNo", pageNo);
            document.append("param",params);
            mongodbUtils.insertOne("PAGE", document);
            flag=true;
            throw new RuntimeException("cookies 超时了,重新登陆 pageNo=" + pageNo);
        }

        Jerry doc = jerry(page.getHtml().toString());
        if (page.getUrl().toString().startsWith(hostAddr + "Blogger/Search")) {
            String url = page.getUrl().toString();
            pageNo = Integer.valueOf(url.substring(url.lastIndexOf("=") + 1)) + 1;
            log.error("pageNo=" + pageNo + ";  url=" + page.getUrl());
            System.out.println("----------pageNo="+pageNo);
            System.out.println("++++++++++"+hostAddr + "Blogger/Search?" + params + "&page=" + pageNo);
            List<String> links = page.getHtml().links().regex(hostAddr + "Blogger/Search\\?" + params + "&page=\\d+#/Blogger/Detail.*").all();
            if (CollectionUtil.isNotEmpty(links)){
                page.addTargetRequest(hostAddr + "Blogger/Search?" + params + "&page=" + pageNo);
            }

        }
        if (page.getUrl().toString().startsWith(hostAddr + "Blogger/Search")) {
            List<String> links = page.getHtml().links().regex(hostAddr + "Blogger/Search\\?" + params + "&page=\\d+#/Blogger/Detail.*").all();
            if (CollectionUtil.isEmpty(links)) {
                return;
            }
            for (String link : links) {
                String targetLink = link.replaceAll("/Blogger/Search\\?" + params + "&page=\\d*#", "");
                page.addTargetRequest(targetLink);
            }
        }
        if (page.getUrl().toString().startsWith(hostAddr + "Blogger/Detail")) {
            // 粉丝特征分析
            Request req = new Request();
            req.setMethod(HttpConstant.Method.POST);
            Map<String, Object> parm = new HashMap<String, Object>();
            String uid = page.getHtml().css("div.info").regex("分类.*").regex("[1-9]\\d*").toString();
            if (StrUtil.isBlank(uid)) {
                String div = doc.s("div.zhibo-code1").children().attr("data-qrcodesrc");
                uid = div.substring(div.lastIndexOf("/") + 1);
            }
            parm.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            req.setRequestBody(HttpRequestBody.form(parm, "UTF-8"));
            System.out.println(uid);
            req.setUrl(hostAddr + "Blogger/FansAnalysis?uid=" + uid);
            page.addTargetRequest(req);

            // 粉丝重合抖音号
            Request request1 = new Request();
            request1.setMethod(HttpConstant.Method.POST);
            parm.put("uid", uid);
            request1.setExtras(parm);
            parm.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            request1.setRequestBody(HttpRequestBody.form(parm, "UTF-8"));
            request1.setUrl(hostAddr + "Blogger/FansSimilarAnalysis");
            page.addTargetRequest(request1);
            // 获取当前时间的前一天
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, -1);
            String customDate = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
            String id = doc.s("#js-btn-collect-blogger").attr("data-id");
            // 全部直播商品
            Request request2 = new Request();
            request2.setUrl(hostAddr + "Blogger/GetLiveEcommerceAnalysis?keyword=&newpingpai=&isPartial=true&datetype=1&sort=0&livesort=0&isAwemeRela=0&uid=" + uid + "&id=" + id + "&customDate=" + customDate + "&page=1");
            page.addTargetRequest(request2);
            // 橱窗商品
            Request request3 = new Request();
            request3.setUrl(hostAddr + "Blogger/GetEcommerceAnalysis?keyword=&newpingpai=&isPartial=true&datetype=1&sort=0&livesort=0&isAwemeRela=0&uid=" + uid + "&id=" + id + "&customDate=" + customDate + "&page=1");
            page.addTargetRequest(request3);

            // 直播带货分析
            Request request4 = new Request();
            request4.setUrl(hostAddr + "Blogger/LiveSellGoodsAnalyzeGoodsList?datetype=1&keyword=&livesort=0&newPingpai=&uid=" + uid + "&page=1");
            page.addTargetRequest(request4);

            // 直播带货分析
            Request request5 = new Request();
            request5.setUrl(hostAddr + "Blogger/LiveSellGoodsAnalyze?uid=" + uid);
            page.addTargetRequest(request5);
        }

        // 直播带货分析
        if (page.getUrl().toString().startsWith(hostAddr + "Blogger/LiveSellGoodsAnalyzeGoodsList") && !page.getHtml().toString().contains("没有更多内容")) {
            if (StrUtil.isBlank(page.getHtml().css("div.media-list").toString())) {
                return;
            }
            String url = page.getUrl().toString();
            pageNo = Integer.valueOf(url.substring(url.lastIndexOf("=") + 1)) + 1;
            if (url.contains("page=")) {
                String[] split = url.split("page=");
                String replaceUrl = split[0] + "page=" + pageNo;
                page.addTargetRequest(replaceUrl);
            }

        }

        //橱窗商品
        if (page.getUrl().toString().startsWith(hostAddr + "Blogger/GetEcommerceAnalysis") && !page.getHtml().toString().contains("没有更多内容")) {
            if (StrUtil.isBlank(page.getHtml().css("div.media-list").toString())) {
                return;
            }
            String url = page.getUrl().toString();
            pageNo = Integer.valueOf(url.substring(url.lastIndexOf("=") + 1)) + 1;
            if (url.contains("page=")) {
                String[] split = url.split("page=");
                String replaceUrl = split[0] + "page=" + pageNo;
                page.addTargetRequest(replaceUrl);
            }

        }
        // 全部直播商品
        if (page.getUrl().toString().startsWith(hostAddr + "Blogger/GetLiveEcommerceAnalysis") && !page.getHtml().toString().contains("没有更多内容")) {
            if (StrUtil.isBlank(page.getHtml().css("div.media-list").toString())) {
                return;
            }
            String url = page.getUrl().toString();
            pageNo = Integer.valueOf(url.substring(url.lastIndexOf("=") + 1)) + 1;
            if (url.contains("page=")) {
                String[] split = url.split("page=");
                String replaceUrl = split[0] + "page=" + pageNo;
                page.addTargetRequest(replaceUrl);
            }
        }

        // 电商数据分析-直播商品
        parseDataUtil.getLiveEcommerceAnalysis(page, doc, hostAddr);
        // 橱窗商品
        parseDataUtil.getEcommerceAnalysis(page, doc, hostAddr);
        // 播主详细信息
        parseDataUtil.bloggerDetailParseData(page, doc, hostAddr);
        // 粉丝特征分析-粉丝画像
        parseDataUtil.fansAnalysis(page, doc, hostAddr);
        // 粉丝特征分析-粉丝重合抖音号
        parseDataUtil.fansSimilarAnalysisParseData(page, doc, hostAddr);
        // 直播带货分析-直播商品
        parseDataUtil.liveSellGoodsAnalyzeGoodsListParseData(page, doc, hostAddr);
        // 直播观众分析
        parseDataUtil.liveSellGoodsAnalyzeParseData(page, doc, hostAddr);
    }

    @Override
    public Site getSite() {
        System.out.println(site);
        return site;
    }
}
