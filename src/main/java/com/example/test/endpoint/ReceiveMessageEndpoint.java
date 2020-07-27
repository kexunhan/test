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
import javax.xml.crypto.Data;
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
            System.out.println("收到钉钉机器人的信息...");
            String cookie = text.getContent().trim();
            Site site = Site.me().setRetryTimes(3).setSleepTime(8000).setTimeOut(10000).addCookie("PHPSESSID", cookie);
            feiGuaDynamicParamBloggerPageProcessor.setSite(site);

            Document page1 = mongoTemplate.getCollection("PAGE").find().first();

            int page = (int) page1.get("pageNo");
            String param = (String) page1.get("param");

            System.out.println("page=" + page+"\t"+"param=" + param);
            System.out.println("接收的cookie: "+feiGuaDynamicParamBloggerPageProcessor.getSite().getCookies());
            mongoTemplate.dropCollection("PAGE");
            List<String> paramsList = feiGuaDynamicParamBloggerPageProcessor.getParamsList();
            int index = paramsList.indexOf(param);
            System.out.println("paramsList.size="+ paramsList.size()+"\t 当前为index="+index);
            feiGuaDynamicParamBloggerPageProcessor.setFlag(false);
            for (int i = index; i < paramsList.size(); i++) {
                System.out.println("i=" + i);
                System.out.println("开始请求url:"+feiGuaDynamicParamBloggerPageProcessor.getHostAddr() + "Blogger/Search?" + paramsList.get(i) + "&page=" + page);
                if (feiGuaDynamicParamBloggerPageProcessor.getFlag()) {
                    return;
                }
                feiGuaDynamicParamBloggerPageProcessor.setParams(paramsList.get(i));
                Spider.create(feiGuaDynamicParamBloggerPageProcessor).addUrl(feiGuaDynamicParamBloggerPageProcessor.getHostAddr() + "Blogger/Search?" + paramsList.get(i) + "&page=" + page).thread(1).run();
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
//        String cookie = "_uab_collina=159230123278931226381934; chl=key=FeiGuaRank&word=dGFn; searchnew=1; focus=03C3733494937CB9; f88bad2fd0abeceb22b6df002b555f3d=11c014ebad67002f7ad1f454a99db94ff3175e23b2ba00288265cf59a438582aba83f0be6ceb71d9c12faee93bcfa0b7437fec1711588069c841ba6adf9daa3aeb10938eaa62c1969b1d5cb7f4c89e32a3657dfdb9e287efcb81ed3a850114e454349ee7bf77371bf5c42b887831128d8e83fb801230c8e4d9eedeb9aec4b9a5; ASP.NET_SessionId=gaxub5mh0tbfpgyu2x5aa05x; Hm_lvt_b9de64757508c82eff06065c30f71250=1595297071,1595303379,1595497548,1595554100; Hm_lpvt_b9de64757508c82eff06065c30f71250=1595554100; FEIGUA=UserId=28eba0b7d0c1f4cf671d1470e674ddff&SubUserId=5a34d0aa27e62f69&NickName=b2d862d43284188dc78c824a1700ac7e&checksum=b11a351518f2&FEIGUALIMITID=04ba3c9366f24c1aa832059c8f2f21b3; db9edc769ae11a3739ee4bcbf9071fb3=11c014ebad67002f7ad1f454a99db94ff3175e23b2ba00288265cf59a438582aba83f0be6ceb71d9c12faee93bcfa0b7437fec1711588069b7e56b320ced82f849df66dfbfd49d7c722ef92177cb76436019b56c504d3d3af52d3cd1be21c56754aa1a30739398b598072b4bfb5b40004d368cdfc3b4915d988e1abd3aa961a8; SaveUserName=";
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
//            System.out.println(feiGuaDynamicParamBloggerPageProcessor.getHostAddr() + "Blogger/Search?" + paramsList.get(i) + "&page=" + page);
//            if (feiGuaDynamicParamBloggerPageProcessor.getFlag()) {
//                return;
//            }
//            feiGuaDynamicParamBloggerPageProcessor.setParams(paramsList.get(i));
//            Spider.create(feiGuaDynamicParamBloggerPageProcessor).addUrl(feiGuaDynamicParamBloggerPageProcessor.getHostAddr() + "Blogger/Search?" + paramsList.get(i) + "&page=" + page).thread(1).run();
//            try {
//                Thread.sleep(2000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            System.out.println("-----------------------i=" + i);
//        }
//    }
}

