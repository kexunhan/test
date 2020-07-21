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
            int index = paramsList.indexOf(param);
            feiGuaDynamicParamBloggerPageProcessor.setFlag(false);
            for (int i = index; i < paramsList.size(); i++) {
                if (feiGuaDynamicParamBloggerPageProcessor.getFlag()) {
                    return;
                }
                feiGuaDynamicParamBloggerPageProcessor.setParams(paramsList.get(index));
                Spider.create(feiGuaDynamicParamBloggerPageProcessor).addUrl(feiGuaDynamicParamBloggerPageProcessor.getHostAddr() + "Blogger/Search?" + paramsList.get(index) + "&page=" + page).thread(1).run();
                Thread.sleep(2000);
            }
        }
    }


    @GetMapping("/go")
    public void start() {
        String phpSessId = "";
        Site site = Site.me().setRetryTimes(3).setSleepTime(1000).setTimeOut(100000).addCookie("PHPSESSID", phpSessId);
        feiGuaDynamicParamBloggerPageProcessor.setSite(site);
        if (feiGuaDynamicParamBloggerPageProcessor.getFlag()) {
            return;
        }
        feiGuaDynamicParamBloggerPageProcessor.setParams(feiGuaDynamicParamBloggerPageProcessor.getParam());
        Spider.create(feiGuaDynamicParamBloggerPageProcessor).addUrl(feiGuaDynamicParamBloggerPageProcessor.getHostAddr() + "Blogger/Search?" + feiGuaDynamicParamBloggerPageProcessor.getParam() + "&page=1").thread(1).run();
    }
}
