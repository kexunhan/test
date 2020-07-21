package com.example.test.kolmap;

import cn.hutool.core.util.StrUtil;
import jodd.jerry.Jerry;
import jodd.lagarto.dom.Node;
import org.apache.log4j.Logger;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;

@Component
public class ParseDataUtil {

    static Logger log = Logger.getLogger(ParseDataUtil.class);

    @Autowired
    MongodbUtils mongodbUtils;

    // 直播带货分析-直播商品
    public void liveSellGoodsAnalyzeGoodsListParseData(Page page, Jerry doc, String hostAddr) {
        if (page.getUrl().toString().startsWith(hostAddr + "Blogger/LiveSellGoodsAnalyzeGoodsList") && !page.getHtml().toString().contains("没有更多内容")) {
            Node[] nodes = doc.s("div.item-title a").get();
            for (Node node : nodes) {
                Document document = new Document();
                String[] split = page.getUrl().toString().split("uid=");
                String uid = split[1].split("&")[0];
                document.append("uid", uid);
                document.append("name", node.getTextContent());
                document.append("url", node.getAttribute("href"));
                mongodbUtils.insertOne("LIVE-PRODUCTS",document);
            }
        }
    }

    /**
     * 直播观众分析
     *
     * @param page
     * @param doc
     * @param hostAddr
     */
    public  void liveSellGoodsAnalyzeParseData(Page page, Jerry doc, String hostAddr) {
        if (page.getUrl().toString().startsWith(hostAddr + "Blogger/LiveSellGoodsAnalyze")) {
            String[] split = page.getUrl().toString().split("uid=");
            String uid = split[1].split("&")[0];
            if (null == doc.s(".gender-text").get(0)) {
                log.debug("该播主没有直播观众分析数据 uid=" + uid);
                return;
            }
            Document document = new Document();
            document.append("uid", uid);
            document.append("sex", doc.s(".gender-text").get(0).getTextContent());
            document.append("geographical-distribution-province", doc.s(".section-content").s(".location-table").get(0).getTextContent());
            document.append("geographical-distribution-city", doc.s(".section-content").s(".location-table").get(1).getTextContent());
            mongodbUtils.insertOne("LIVE-AUDIENCE-ANALYSIS",document);

        }
    }

    /**
     * 粉丝特征分析-粉丝重合抖音号
     *
     * @param page
     * @param doc
     * @param hostAddr
     */
    public  void fansSimilarAnalysisParseData(Page page, Jerry doc, String hostAddr) {
        if (page.getUrl().toString().startsWith(hostAddr + "Blogger/FansSimilarAnalysis")) {
            //uid
            String uid = page.getRequest().getExtras().get("uid").toString();
            Node[] nodes = doc.s(".mp-article-list tbody tr").get();
            if (nodes[0].getTextContent().contains("该播主暂无，活跃粉丝重合抖音号")) {
                log.debug("该播主暂无，活跃粉丝重合抖音号 uid=" + uid);
                return;
            }
            for (Node node : nodes) {
                Document document = new Document();
                document.append("uid", uid);
                document.append("name", node.getChild(3).getTextContent());
                document.append("tag", node.getChild(5).getTextContent());
                document.append("fan", node.getChild(7).getTextContent());
                document.append("coincidence-degree", node.getChild(9).getTextContent());
                mongodbUtils.insertOne("FAN-RANKING",document);

            }

        }
    }

    /**
     * 粉丝特征分析-粉丝画像
     *
     * @param page
     * @param doc
     * @param hostAddr
     */
    public  void fansAnalysis(Page page, Jerry doc, String hostAddr) {
        if (page.getUrl().toString().startsWith(hostAddr + "Blogger/FansAnalysis")) {
            Document document = new Document();
            document.append("uid", page.getUrl().toString().split("\\?")[1].substring(4));

            document.append("gender-text-male", doc.s(".gender-text").children().get(0).getTextContent());
            document.append("gender-text-female", doc.s(".gender-text").children().get(1).getTextContent());
            // 地域分布-省份
            document.append("geographical-distribution-province", doc.s(".section-content > table").get(0).getTextContent());
            // 地域分布-城市
            document.append("geographical-distribution-city", doc.s(".section-content > table").get(1).getTextContent());
            // 星座
            document.append("constellation", doc.s(".section-content").get(2).getTextContent());
            mongodbUtils.insertOne("FAN-PORTRAIT",document);

        }
    }

    public  void bloggerDetailParseData(Page page, Jerry doc, String hostAddr) {
        if (page.getUrl().toString().startsWith(hostAddr + "Blogger/Detail?")) {
            Document document = new Document();
            //uid
            String uid = page.getHtml().css("div.info").regex("分类.*").regex("[1-9]\\d*").toString();
            if (StrUtil.isBlank(uid)) {
                String div = doc.s("div.zhibo-code1").children().attr("data-qrcodesrc");
                uid = div.substring(div.lastIndexOf("/") + 1);
            }
            document.append("uid", uid);
            // 名称
            document.append("name", doc.s(".nickname").text());
            // 新闻
            document.append("bloggerTag", doc.s(".dy-tag").text());
            // 简介
            document.append("intro", doc.s(".intro").children().text());
            // 抖音号
            document.append("douYinHao", doc.s(".info").children().last().children().get(0).getTextContent());
            // 地区
            document.append("sex-and-area", doc.s(".info").children().last().children().get(1).getTextContent());
            // 分类
            document.append("age-and-sort", doc.s(".info").children().last().children().get(2).getTextContent());
            // 粉丝数
            document.append("fans", doc.s(".fans").children().get(1).getTextContent());
            // 作品数
            document.append("videos-count", doc.s(".videos-count").text());
            // 飞瓜指数
            document.append("xiagua-index", doc.s(".xiagua-index").text());
            // 昨日排名打败
            document.append("yesterday-ranking", doc.s(".col-sm-6").children().get(0).getTextContent());
            // 近一周排名打败
            document.append("week-ranking", doc.s(".col-sm-6").children().get(1).getTextContent());
            // 总点赞
            document.append("total-likes", doc.s(".col-sm-6").children().get(2).getTextContent());
            // 平均点赞
            document.append("average-likes", doc.s(".col-sm-6").children().get(3).getTextContent());
            // 集均评论
            document.append("set-average-review", doc.s(".col-sm-6").children().next().get(2).getTextContent());
            // 集均分享
            document.append("set-share", doc.s(".col-sm-6").children().last().text());
            mongodbUtils.insertOne("BLOGGER-DETAILS",document);
        }
    }

    /**
     * 橱窗商品
     *
     * @param page
     * @param doc
     * @param hostAddr
     */
    public  void getEcommerceAnalysis(Page page, Jerry doc, String hostAddr) {
        if (page.getUrl().toString().startsWith(hostAddr + "Blogger/GetEcommerceAnalysis")) {
            String cssSelector = doc.s("div.item-title").text();
            if (StrUtil.isNotBlank(cssSelector)) {
                if (page.getUrl().toString().contains("uid=")) {
                    Node[] nodes = doc.s("div.item-title a").get();
                    if (null != nodes) {
                        for (Node node : nodes) {
                            Document document = new Document();
                            String[] split = page.getUrl().toString().split("uid=");
                            String uid = split[1].split("&")[0];
                            document.append("uid", uid);
                            document.append("name", node.getTextContent());
                            document.append("url", node.getAttribute("href"));
                            mongodbUtils.insertOne("WINDOW-MERCHANDIS",document);

                        }
                    }
                }
            }
        }
    }

    /**
     * 电商数据分析-直播商品
     *
     * @param page
     * @param doc
     * @param hostAddr
     */
    public  void getLiveEcommerceAnalysis(Page page, Jerry doc, String hostAddr) {
        if (page.getUrl().toString().startsWith(hostAddr + "Blogger/GetLiveEcommerceAnalysis")) {
            String cssSelector = doc.s("div.item-title").text();
            if (StrUtil.isNotBlank(cssSelector)) {
                if (page.getUrl().toString().contains("uid=")) {
                    Node[] nodes = doc.s(".item-title").children().get();
                    if (null != nodes) {
                        for (Node node : nodes) {
                            Document document = new Document();
                            String[] split = page.getUrl().toString().split("uid=");
                            String uid = split[1].split("&")[0];
                            document.append("uid", uid);
                            document.append("name", node.getTextContent());
                            document.append("url", node.getAttribute("href"));
                            if (StrUtil.isNotBlank(node.getTextContent())) {
                                mongodbUtils.insertOne("BLOGGER-ALL-LIVE-PRODUCTS",document);

                            }

                        }
                    }
                }
            }
        }
    }

    public  void mcnParseData(Page page, Jerry doc, String hostAddr) {
        if (page.getUrl().toString().startsWith(hostAddr + "MCN/Detail?")) {
            Jerry mcnIntro = doc.s(".mcn-intro");
            Jerry mcnInfoItems = doc.s(".mcn-info-item").children();

            System.out.println(doc.s(".col-sm-6").get(0).getTextContent());
            System.out.println(doc.s(".col-sm-6").get(1).getTextContent());
            Document document = new Document();
            document.append("name", doc.s(".name a").text());
            document.append("mcn-intro", mcnIntro.get(0).getTextContent());
            document.append("mcn-website", mcnIntro.get(1).getTextContent());
            document.append("contract-person", mcnInfoItems.get(0).getTextContent());
            document.append("contract-phone", mcnInfoItems.get(1).getTextContent());
            document.append("contract-wechat", mcnInfoItems.get(2).getTextContent());
            document.append("contract-mail", mcnInfoItems.get(3).getTextContent());
            document.append("funs-amount", doc.s(".col-sm-6").get(0).getTextContent());
            document.append("kol-amount", doc.s(".col-sm-6").get(1).getTextContent());
            mongodbUtils.insertOne("MCN",document);

        }
    }
}
