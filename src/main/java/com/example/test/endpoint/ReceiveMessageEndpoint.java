package com.example.test.endpoint;


import com.example.test.config.Constant;
import com.example.test.dto.Content;
import com.example.test.dto.ReceiveMessageDto;
import com.example.test.kolmap.FeiGuaDynamicParamBloggerPageProcessor;
import com.example.test.kolmap.MongodbUtils;
import com.example.test.kolmap.ParseDataUtil;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.*;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;


import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@RestController
@Slf4j
public class ReceiveMessageEndpoint {



    static List<String> paramsList = Arrays.asList(

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


    @Resource
    private MongoTemplate mongoTemplate;

    @Resource
    private MongodbUtils mongodbUtils;

    @Resource
    private ParseDataUtil parseDataUtil;


    @PostMapping("/test")
    public void receiveRobotMassage(@RequestBody ReceiveMessageDto receiveMessageDto) {
        Content text = receiveMessageDto.getText();
        if (Objects.nonNull(text)) {
            log.debug("收到钉钉机器人的信息...");
            String cookie = text.getContent().trim();
            Site site = Site.me().setRetryTimes(3).setSleepTime(8000).setTimeOut(10000).addCookie("PHPSESSID", cookie);
            FeiGuaDynamicParamBloggerPageProcessor feiGuaDynamicParamBloggerPageProcessor = new FeiGuaDynamicParamBloggerPageProcessor(parseDataUtil,mongodbUtils);
            feiGuaDynamicParamBloggerPageProcessor.setSite(site);

            Document page1 = mongoTemplate.getCollection("PAGE").find().first();

            int page = (int) page1.get("pageNo");
            String currentParam = (String) page1.get("param");
            log.debug("page={} \t param={}", page, currentParam);
            log.debug("接收的cookie: {}", feiGuaDynamicParamBloggerPageProcessor.getSite().getCookies());
            mongoTemplate.dropCollection("PAGE");
            Constant.flag=false;
            int index = paramsList.indexOf(currentParam);
            for (int i = index; i < paramsList.size(); i++) {
                log.debug("paramsList.size={},\t 当前为index={}", paramsList.size(), i);
                log.debug("开始请求url:{}Blogger/Search?{}&page={}", feiGuaDynamicParamBloggerPageProcessor.getHostAddr(), paramsList.get(i), page);
                feiGuaDynamicParamBloggerPageProcessor.setCurrentParam(paramsList.get((i)));
                Spider.create(feiGuaDynamicParamBloggerPageProcessor).addUrl(feiGuaDynamicParamBloggerPageProcessor.getHostAddr() + "Blogger/Search?" + paramsList.get(i) + "&page=" + page).thread(1).run();
                if (Constant.flag) {
                    return;
                }
            }


        }
    }


    @GetMapping("/go")
    public void start() {
        String phpSessId = "";
        Site site = Site.me().setRetryTimes(3).setSleepTime(10000).setTimeOut(100000).addCookie("PHPSESSID", phpSessId);
        FeiGuaDynamicParamBloggerPageProcessor feiGuaDynamicParamBloggerPageProcessor = new FeiGuaDynamicParamBloggerPageProcessor(parseDataUtil,mongodbUtils);
        feiGuaDynamicParamBloggerPageProcessor.setSite(site);
        feiGuaDynamicParamBloggerPageProcessor.setCurrentParam(paramsList.get(0));

        Spider.create(feiGuaDynamicParamBloggerPageProcessor).addUrl(feiGuaDynamicParamBloggerPageProcessor.getHostAddr() + "Blogger/Search?" + paramsList.get(0) + "&page=1").thread(1).run();
    }


    @GetMapping("/receiveRobotMassage")
    public void receiveRobotMassage() {
        String cookie = "";
        Site site = Site.me().setRetryTimes(3).setSleepTime(8000).setTimeOut(10000).addCookie("PHPSESSID", cookie);
        FeiGuaDynamicParamBloggerPageProcessor feiGuaDynamicParamBloggerPageProcessor = new FeiGuaDynamicParamBloggerPageProcessor(parseDataUtil,mongodbUtils);
        feiGuaDynamicParamBloggerPageProcessor.setSite(site);


        int page = 1;
        String currentParam = "isWithCommerceEntry=1&multiTag=汽车&fans=10-50";
        log.debug("page={} \t param={}", page, currentParam);
        log.debug("接收的cookie: {}", feiGuaDynamicParamBloggerPageProcessor.getSite().getCookies());
        int index = paramsList.indexOf(currentParam);
        for (int i = index; i < paramsList.size(); i++) {
            log.debug("paramsList.size={},\t 当前为index={}", paramsList.size(), i);
            log.debug("开始请求url:{}Blogger/Search?{}&page={}", feiGuaDynamicParamBloggerPageProcessor.getHostAddr(), paramsList.get(i), page);
            feiGuaDynamicParamBloggerPageProcessor.setCurrentParam(paramsList.get((i)));
            Spider.create(feiGuaDynamicParamBloggerPageProcessor).addUrl(feiGuaDynamicParamBloggerPageProcessor.getHostAddr() + "Blogger/Search?" + paramsList.get(i) + "&page=" + page).thread(1).run();
            if (Constant.flag) {
                return;
            }
        }
    }




    @GetMapping("/log")
    public String log() {
        String name = "张三";
        log.debug("hello {}.", name);
        return "log";
    }

}

