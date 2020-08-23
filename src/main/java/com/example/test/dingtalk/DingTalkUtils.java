package com.example.test.dingtalk;


import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request;
import com.dingtalk.api.request.OapiMessageSendToConversationRequest;
import com.dingtalk.api.request.OapiRobotSendRequest;
import com.dingtalk.api.response.OapiMessageCorpconversationAsyncsendV2Response;
import com.dingtalk.api.response.OapiMessageSendToConversationResponse;
import com.dingtalk.api.response.OapiRobotSendResponse;
import com.taobao.api.ApiException;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class DingTalkUtils {

    /**
     * 发送通知
     *
     * @throws ApiException
     */
    public static void sendNotification() {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/message/corpconversation/asyncsend_v2");
        OapiMessageCorpconversationAsyncsendV2Request request = new OapiMessageCorpconversationAsyncsendV2Request();
        // 用户id
        // https://oa.dingtalk.com/index.htm#/welcome --通讯录管理找用户ID
        request.setUseridList("manager363");
        request.setAgentId(818943077L);
        request.setToAllUser(false);

        OapiMessageCorpconversationAsyncsendV2Request.Msg msg = new OapiMessageCorpconversationAsyncsendV2Request.Msg();
        msg.setMsgtype("text");
        msg.setText(new OapiMessageCorpconversationAsyncsendV2Request.Text());
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
        String format = sdf.format(new Date());
        msg.getText().setContent(format+"cookie失效，立即回复 @机器人 cookie");
        request.setMsg(msg);
        try {
            client.execute(request, AccessTokenUtil.getToken());
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }




    /**
     *
     * 群机器人发消息
     *
     */
    public static   void robotSendMessage(){
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/robot/send?access_token=ab5143f5c69fe57c2178d88679dcadd1661c1a29ee18f2adb29ede5e343c4d35");
        OapiRobotSendRequest request = new OapiRobotSendRequest();
        request.setMsgtype("text");
        OapiRobotSendRequest.Text text = new OapiRobotSendRequest.Text();
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
        String format = sdf.format(new Date());
        text.setContent(format+" cookie失效，立即回复 @机器人 cookie");
        request.setText(text);
        try {
            client.execute(request);
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        robotSendMessage();
    }
}
