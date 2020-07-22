package com.example.test.endpoint;


import com.example.test.dto.Content;
import com.example.test.dto.ReceiveMessageDto;
import com.example.test.kolmap.FeiGuaDynamicParamBloggerPageProcessor;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.*;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;


import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

@RestController
public class ReceiveMessageEndpoint {


    @Resource
    private FeiGuaDynamicParamBloggerPageProcessor feiGuaDynamicParamBloggerPageProcessor;

    @Resource
    private MongoTemplate mongoTemplate;


    @PostMapping("/test")
    public void receiveRobotMassage(@RequestBody ReceiveMessageDto receiveMessageDto) throws Exception {
        Content text = receiveMessageDto.getText();
        if (Objects.nonNull(text)) {
            String cookie = text.getContent().trim();
            System.out.println("cookie=" + cookie);
            Site site = Site.me().setRetryTimes(3).setSleepTime(8000).setTimeOut(10000).addCookie("PHPSESSID", cookie);
            feiGuaDynamicParamBloggerPageProcessor.setSite(site);

            Document page1 = mongoTemplate.getCollection("PAGE").find().first();

            int page = (int) page1.get("pageNo");
            String param = (String) page1.get("param");

            System.out.println("page=" + page);
            System.out.println("param=" + param);
            System.out.println(feiGuaDynamicParamBloggerPageProcessor.getSite().getCookies());
            mongoTemplate.dropCollection("PAGE");
            List<String> paramsList = feiGuaDynamicParamBloggerPageProcessor.getParamsList();
            System.out.println(paramsList);

            int index = paramsList.indexOf(param);
            feiGuaDynamicParamBloggerPageProcessor.setFlag(false);
            for (int i = index; i < paramsList.size(); i++) {
                System.out.println("i=" + i);
                paramsList.get(i);
                System.out.println(feiGuaDynamicParamBloggerPageProcessor.getHostAddr() + "Blogger/Search?" + paramsList.get(i) + "&page=" + page);
                if (feiGuaDynamicParamBloggerPageProcessor.getFlag()) {
                    return;
                }
                feiGuaDynamicParamBloggerPageProcessor.setParams(paramsList.get(index));
                Spider.create(feiGuaDynamicParamBloggerPageProcessor).addUrl(feiGuaDynamicParamBloggerPageProcessor.getHostAddr() + "Blogger/Search?" + paramsList.get(index) + "&page=" + page).thread(1).run();
                Thread.sleep(300000);
                System.out.println("-----------------------i=" + i);
            }
        }
    }


    @GetMapping("/go")
    public void start() {
        String phpSessId = "";
        Site site = Site.me().setRetryTimes(3).setSleepTime(10000).setTimeOut(100000).addCookie("PHPSESSID", phpSessId);
        feiGuaDynamicParamBloggerPageProcessor.setSite(site);
        if (feiGuaDynamicParamBloggerPageProcessor.getFlag()) {
            return;
        }
        feiGuaDynamicParamBloggerPageProcessor.setParams(feiGuaDynamicParamBloggerPageProcessor.getParam());
        Spider.create(feiGuaDynamicParamBloggerPageProcessor).addUrl(feiGuaDynamicParamBloggerPageProcessor.getHostAddr() + "Blogger/Search?" + feiGuaDynamicParamBloggerPageProcessor.getParam() + "&page=1").thread(1).run();
    }


//    @GetMapping("/hahahhahahaahhahaahhahah")
//    public void sdsd() {
//
//        String cookie = "_uab_collina=159230123278931226381934; chl=key=FeiGuaRank&word=dGFn; searchnew=1; focus=03C3733494937CB9; ASP.NET_SessionId=y1vtc320u1t3ezy0vkprg234; Hm_lvt_b9de64757508c82eff06065c30f71250=1595136505,1595210394,1595297071,1595303379; SaveUserName=; Hm_lpvt_b9de64757508c82eff06065c30f71250=1595320527; FEIGUA=UserId=28eba0b7d0c1f4cf671d1470e674ddff&SubUserId=5a34d0aa27e62f69&NickName=b2d862d43284188dc78c824a1700ac7e&checksum=23b2cb323ff6&FEIGUALIMITID=8487cc4bac56492eb911c889f4dd79e7; 94381b828320425e9b26a185023605e1=11c014ebad67002f7ad1f454a99db94ff3175e23b2ba00288265cf59a438582aba83f0be6ceb71d9c12faee93bcfa0b7437fec1711588069ef1e8876fba73991165e8c978acdb302e4561fa7d21e7573cad6e06126271432c887d0ac1054ed2f85b4950a6d395fe1f7971adc6b9153250c92b126ebb996843d8f4cd15fefe12d";
//        Site site = Site.me().setRetryTimes(3).setSleepTime(1000).setTimeOut(10000).addCookie("PHPSESSID", cookie);
//        feiGuaDynamicParamBloggerPageProcessor.setSite(site);
//
//
//        int page = 1;
//        String param = "isWithCommerceEntry=1&multiTag=网红帅哥&fans=10-50";
//
//        System.out.println(feiGuaDynamicParamBloggerPageProcessor.getSite().getCookies());
//        List<String> paramsList = feiGuaDynamicParamBloggerPageProcessor.getParamsList();
//        System.out.println(paramsList);
//
//        int index = paramsList.indexOf(param);
//        feiGuaDynamicParamBloggerPageProcessor.setFlag(false);
//        for (int i = index; i < paramsList.size(); i++) {
//            System.out.println("i=" + i);
//            paramsList.get(i);
//            System.out.println(feiGuaDynamicParamBloggerPageProcessor.getHostAddr() + "Blogger/Search?" + paramsList.get(i) + "&page=" + page);
//            if (feiGuaDynamicParamBloggerPageProcessor.getFlag()) {
//                return;
//            }
//            feiGuaDynamicParamBloggerPageProcessor.setParams(paramsList.get(index));
//            Spider.create(feiGuaDynamicParamBloggerPageProcessor).addUrl(feiGuaDynamicParamBloggerPageProcessor.getHostAddr() + "Blogger/Search?" + paramsList.get(index) + "&page=" + page).thread(1).run();
//            try {
//                Thread.sleep(2000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            System.out.println("-----------------------i=" + i);
//        }
//    }
}

