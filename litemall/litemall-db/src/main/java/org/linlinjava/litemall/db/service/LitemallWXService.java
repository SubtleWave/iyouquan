package org.linlinjava.litemall.db.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.linlinjava.litemall.db.cache.RedisCacheManager;
import org.linlinjava.litemall.db.dao.LitemallTraceMapper;
import org.linlinjava.litemall.db.domain.LitemallTrace;
import org.linlinjava.litemall.db.domain.LitemallUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class LitemallWXService {

    Logger logger = LoggerFactory.getLogger(LitemallWXService.class);

    @Value("${wx.gz.appid:''}")
    private String appid;
    @Value("${wx.gz.secret:''}")
    private String secret;


    @Value("${wx.iyouquan.appid:''}")
    private String iYOUQUANappid;
    @Value("${wx.iyouquan.secret:''}")
    private String iYOUQUANsecret;
    @Resource
    private RestTemplate restTemplate;
    @Resource
    private RedisCacheManager redisCacheManager;

    @Resource
    private TraceService traceService;

    @Resource
    private LitemallStorageService litemallStorageService;


    public LitemallUser getUserInfo(String openId){
        logger.info("openId:{}",openId);
        //logger.info("getAccessToken:{}",getAccessToken());

        String url = String.format("https://api.weixin.qq.com/cgi-bin/user/info?access_token=%s&openid=%s&lang=zh_CN", getMPAccessToken(), openId);

        ResponseEntity<String> result = restTemplate.getForEntity(url, String.class);

        if(result.getStatusCode()== HttpStatus.OK){

            JSONObject jsonObject = JSON.parseObject(result.getBody());

            logger.info("userInfo:{}",result.getBody());



            LitemallUser user = new LitemallUser();
            user.setUsername(jsonObject.getString("nickname"));
            user.setWeixinUnionid(jsonObject.getString("unionid"));
            user.setWeixinOpenid(openId);
            user.setAvatar(jsonObject.getString("headimgurl"));
            user.setNickname(jsonObject.getString("nickname"));
            user.setGender(Byte.valueOf(jsonObject.getString("sex")));
            user.setUserLevel((byte) 0);
            user.setStatus((byte) 0);
            user.setLastLoginTime(LocalDateTime.now());
            return user;
        }


        return null;
    }


    public String getAccessToken(String key,String appid,String secret){


        //从redis缓存中获取token
        if(redisCacheManager.hasKey(key)){

            logger.info("key: {},token:{}", key ,redisCacheManager.get(key));
            return redisCacheManager.get(key);
        }


        String url = String.format("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s",appid,secret);

        ResponseEntity<String> result = restTemplate.getForEntity(url, String.class);

        if(result.getStatusCode()== HttpStatus.OK){

            JSONObject jsonObject = JSON.parseObject(result.getBody());

            logger.info("tokenbody:{}",result.getBody());
            String access_token = jsonObject.getString("access_token");
            Long expires_in = jsonObject.getLong("expires_in");

            redisCacheManager.set(key,access_token,expires_in);


            return access_token;
        }


        return null;
    }

    public String getMPAccessToken() {
        String key = "mp.access.token";
        return getAccessToken(key,appid,secret);
    }

    public String getIYOUQUANAccessToken() {
        String key = "iyouquan.access.token";
        return getAccessToken(key,iYOUQUANappid,iYOUQUANsecret);
    }


    public String uploadWXCheck(String imgurl){
        String url = String.format("https://api.weixin.qq.com/wxa/media_check_async?access_token=%s",getIYOUQUANAccessToken());
        ResponseEntity<String> result = restTemplate.postForEntity(url, "{\"media_url\": \""+imgurl+"\", \"media_type\": \"2\"}", String.class);
        logger.info("imageUrlcheck:{}",result.getBody());
        JSONObject jsonObject = JSON.parseObject(result.getBody());
        if ("0".equals(jsonObject.getString("errcode"))){
        return jsonObject.getString("trace_id");
        }
        return null;
    }

    public Map<String, Object> getQrCode() {


        //获取临时二维码
        String url = String.format("https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=%s",getMPAccessToken());
        ResponseEntity<String> result = restTemplate.postForEntity(url, "{\"expire_seconds\": 604800, \"action_name\": \"QR_STR_SCENE\", \"action_info\": {\"scene\": {\"scene_str\": \"login\"}}}", String.class);

        logger.info("二维码:{}",result.getBody());

        JSONObject jsonObject = JSON.parseObject(result.getBody());
        Map<String,Object> map=new HashMap<>();
        map.put("ticket",jsonObject.getString("ticket"));
        map.put("url",jsonObject.getString("url"));

        return map;
    }

    public void callMessage(String trace_id, String isrisky) {
        LitemallTrace trace =  traceService.queryByTraceId(trace_id);
        trace.setStatus(isrisky);
        traceService.updateStatus(trace);
        if (!"0".equals(isrisky)){
            litemallStorageService.deleteById(trace.getStorageId());
        }








    }
}
