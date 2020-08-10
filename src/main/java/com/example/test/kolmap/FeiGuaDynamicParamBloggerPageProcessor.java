package com.example.test.kolmap;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.example.test.config.Constant;
import com.example.test.dingtalk.DingTalkUtils;
import com.example.test.endpoint.ReceiveMessageEndpoint;
import jodd.jerry.Jerry;
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

import static jodd.jerry.Jerry.jerry;

@Component
public class FeiGuaDynamicParamBloggerPageProcessor implements PageProcessor {


    String hostAddr = "https://dy.feigua.cn/";

    private int pageNo = 1;

    private int currentPage=1;




    public String getHostAddr() {
        return hostAddr;
    }

    private String currentParam;
    private Site site;

    public void setCurrentParam(String currentParam) {
        this.currentParam = currentParam;
    }

    public void setSite(Site site) {
        this.site = site;
    }


    private ParseDataUtil parseDataUtil;

    public FeiGuaDynamicParamBloggerPageProcessor(ParseDataUtil parseDataUtil, MongodbUtils mongodbUtils) {
        this.parseDataUtil = parseDataUtil;
        this.mongodbUtils = mongodbUtils;
    }

    private MongodbUtils mongodbUtils;

    @Override
    public void process(Page page) {
        //如果cookies超时了，就直接退出
        if (page.getHtml().toString().contains("登录/注册")|| page.getHtml().toString().contains("登陆超时")) {
            System.out.println("cookies 超时了");
            // cookie失效
            DingTalkUtils.robotSendMessage();
            Document document = new Document();
            document.append("currentPage", currentPage);
            document.append("currentParam", currentParam);
            Constant.flag= true;
            mongodbUtils.insertOne("PAGE", document);
            throw new RuntimeException("cookies 超时了,重新登陆 currentPage=" + currentPage);
        }
        Jerry doc = jerry(page.getHtml().toString());

        if (page.getUrl().toString().startsWith(hostAddr + "Blogger/Search")) {
            String url = page.getUrl().toString();
            currentPage = Integer.valueOf(url.substring(url.lastIndexOf("=") + 1)) + 1;
            System.out.println("----------currentPage="+currentPage);
            System.out.println("++++++++++"+hostAddr + "Blogger/Search?" + currentParam + "&page=" + currentPage);
            List<String> links = page.getHtml().links().regex(hostAddr + "Blogger/Search\\?" + currentParam + "&page=\\d+#/Blogger/Detail.*").all();
            if (CollectionUtil.isNotEmpty(links)){
                page.addTargetRequest(hostAddr + "Blogger/Search?" + currentParam + "&page=" + currentPage);
            }

        }

        if (page.getUrl().toString().startsWith(hostAddr + "Blogger/Search")) {
            List<String> links = page.getHtml().links().regex(hostAddr + "Blogger/Search\\?" + currentParam + "&page=\\d+#/Blogger/Detail.*").all();
            if (CollectionUtil.isEmpty(links)) {
                return;
            }
            for (String link : links) {
                String targetLink = link.replaceAll("/Blogger/Search\\?" + currentParam + "&page=\\d*#", "");
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
