package org.linlinjava.litemall.wx.web;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import com.alibaba.fastjson.JSON;
import org.linlinjava.litemall.core.notify.NotifyService;
import org.linlinjava.litemall.core.notify.NotifyType;
import org.linlinjava.litemall.core.util.*;
import org.linlinjava.litemall.core.util.bcrypt.BCryptPasswordEncoder;
import org.linlinjava.litemall.db.cache.RedisCacheManager;
import org.linlinjava.litemall.db.domain.LitemallUser;
import org.linlinjava.litemall.db.service.CouponAssignService;
import org.linlinjava.litemall.db.service.LitemallUserService;
import org.linlinjava.litemall.db.service.LitemallWXService;
import org.linlinjava.litemall.wx.annotation.LoginUser;
import org.linlinjava.litemall.wx.dto.UserInfo;
import org.linlinjava.litemall.wx.dto.WxLoginInfo;
import org.linlinjava.litemall.wx.service.CaptchaCodeManager;
import org.linlinjava.litemall.wx.service.UserTokenManager;
import org.linlinjava.litemall.wx.util.XmlUtil;
import org.linlinjava.litemall.wx.vo.UserAuth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.linlinjava.litemall.wx.util.WxResponseCode.*;

/**
 * 鉴权服务
 */
@RestController
@RequestMapping("/wx/auth")
@Validated
public class WxAuthController {
    private final Logger logger = LoggerFactory.getLogger(WxAuthController.class);

    @Autowired
    private LitemallUserService userService;

    @Autowired
    private WxMaService wxService;

    @Autowired
    private NotifyService notifyService;

    @Autowired
    private CouponAssignService couponAssignService;

    @Autowired
    private Environment environment;

    @Autowired
    private RedisCacheManager redisCacheManager;
    @Autowired
    private LitemallWXService litemallWXService;

    @Value("${tt_appid}")
    private String tt_appid;
    @Value("${tt_secret}")
    private String tt_secret;

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 账号登录
     *
     * @param body    请求内容，{ username: xxx, password: xxx }
     * @param request 请求对象
     * @return 登录结果
     */
    @PostMapping("login")
    public Object login(@RequestBody String body, HttpServletRequest request) {
        String username = JacksonUtil.parseString(body, "username");
        String password = JacksonUtil.parseString(body, "password");
        logger.info("登录用户名：" + username + "，密码：" + password);
        if (username == null || password == null) {
            return ResponseUtil.badArgument();
        }

        List<LitemallUser> userList = userService.queryByUsername(username);
        LitemallUser user = null;
        if (userList.size() > 1) {
            return ResponseUtil.serious();
        } else if (userList.size() == 0) {
            return ResponseUtil.fail(AUTH_INVALID_ACCOUNT, "账号不存在");
        } else {
            user = userList.get(0);
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (!encoder.matches(password, user.getPassword())) {
            return ResponseUtil.fail(AUTH_INVALID_ACCOUNT, "账号密码不对");
        }

        // 更新登录情况
        user.setLastLoginTime(LocalDateTime.now());
        user.setLastLoginIp(IpUtil.getIpAddr(request));
        if (userService.updateById(user) == 0) {
            return ResponseUtil.updatedDataFailed();
        }

        UserInfo userInfo = UserInfo.builder()
                .avatarUrl(user.getAvatar())
                .nickName(username)
                .build();
        // userInfo
       /* UserInfo userInfo = new UserInfo();
        userInfo.setNickName(user.getNickname());//默认nickName he username 一致
        userInfo.setAvatarUrl(user.getAvatar());*/

        // token
        String token = UserTokenManager.generateToken(user.getId());

        Map<Object, Object> result = new HashMap<Object, Object>();
        result.put("token", token);
        result.put("userInfo", userInfo);
        return ResponseUtil.ok(result);
    }

    /**
     * 头条登录
     * @return
     */
    @PostMapping("login_by_tt")
    public Object login_by_tt(@RequestBody WxLoginInfo wxLoginInfo, HttpServletRequest request){

        String code = wxLoginInfo.getCode();
        UserInfo userInfo = wxLoginInfo.getUserInfo();
        if (code == null || userInfo == null) {
            return ResponseUtil.badArgument();
        }
        //1. 通过code 获取用户信息
        String url = "https://developer.toutiao.com/api/apps/jscode2session";
        StringBuffer buffer = new StringBuffer(url);
        buffer.append("?code=").append(code)
                .append("&appid=").append(tt_appid)
                .append("&secret=").append(tt_secret);

        UserAuth userAuth = restTemplate.getForObject(buffer.toString(), UserAuth.class);
        String openid = userAuth.getOpenid();
        LitemallUser user = userService.queryByOid(openid);
        if (user == null) {
            user = new LitemallUser();
            user.setUsername(userInfo.getNickName());
            user.setWeixinOpenid(userAuth.getOpenid());
            user.setAvatar(userInfo.getAvatarUrl());
            user.setNickname(userInfo.getNickName());
            user.setGender(userInfo.getGender());
            user.setUserLevel((byte) 0);
            user.setStatus((byte) 0);
            user.setLastLoginTime(LocalDateTime.now());
            user.setLastLoginIp(IpUtil.getIpAddr(request));

            userService.add(user);

            // 新用户发送注册优惠券
            couponAssignService.assignForRegister(user.getId());
        } else {
            user.setLastLoginTime(LocalDateTime.now());
            user.setLastLoginIp(IpUtil.getIpAddr(request));
            if (userService.updateById(user) == 0) {
                return ResponseUtil.updatedDataFailed();
            }
        }
        // token
        String token = UserTokenManager.generateToken(user.getId());

        Map<Object, Object> result = new HashMap<Object, Object>();
        result.put("token", token);
        result.put("userInfo", userInfo);
        return ResponseUtil.ok(result);


    }

    /**
     * 微信登录
     *
     * @param wxLoginInfo 请求内容，{ code: xxx, userInfo: xxx }
     * @param request     请求对象
     * @return 登录结果
     */
    @PostMapping("login_by_weixin")
    public Object loginByWeixin(@RequestBody WxLoginInfo wxLoginInfo, HttpServletRequest request) {
        logger.info(JSON.toJSONString(wxLoginInfo));
        String code = wxLoginInfo.getCode();
        UserInfo userInfo = wxLoginInfo.getUserInfo();
        if (code == null || userInfo == null) {
            return ResponseUtil.badArgument();
        }

        String sessionKey = null;
        String openId = null;
        String unionid = null;
        try {
            WxMaJscode2SessionResult result = this.wxService.getUserService().getSessionInfo(code);
            sessionKey = result.getSessionKey();
            openId = result.getOpenid();
            unionid = result.getUnionid();
        } catch (Exception e) {
            logger.error(JSON.toJSONString(e));
        }

        if (sessionKey == null || unionid == null) {
            return ResponseUtil.fail();
        }

        LitemallUser user = userService.queryByUnionId(unionid);
        if (user == null) {
            user = new LitemallUser();
            user.setUsername(userInfo.getNickName());
            user.setWeixinOpenid(openId);
            user.setWeixinUnionid(unionid);
            user.setAvatar(userInfo.getAvatarUrl());
            user.setNickname(userInfo.getNickName());
            user.setGender(userInfo.getGender());
            user.setUserLevel((byte) 0);
            user.setStatus((byte) 0);
            user.setLastLoginTime(LocalDateTime.now());
            user.setLastLoginIp(IpUtil.getIpAddr(request));
            user.setSessionKey(sessionKey);

            userService.add(user);

            // 新用户发送注册优惠券
            couponAssignService.assignForRegister(user.getId());
        } else {
            user.setLastLoginTime(LocalDateTime.now());
            user.setLastLoginIp(IpUtil.getIpAddr(request));
            user.setSessionKey(sessionKey);
            if (userService.updateById(user) == 0) {
                return ResponseUtil.updatedDataFailed();
            }
        }

        // token
        String token = UserTokenManager.generateToken(user.getId());

        Map<Object, Object> result = new HashMap<Object, Object>();
        result.put("token", token);
        result.put("userInfo", userInfo);
        logger.info(JSON.toJSONString(result));

        return ResponseUtil.ok(result);
    }


    /**
     * 请求注册验证码
     * <p>
     * TODO
     * 这里需要一定机制防止短信验证码被滥用
     *
     * @param body 手机号码 { mobile }
     * @return
     */
    @PostMapping("regCaptcha")
    public Object registerCaptcha(@RequestBody String body) {
        String phoneNumber = JacksonUtil.parseString(body, "mobile");
        if (StringUtils.isEmpty(phoneNumber)) {
            return ResponseUtil.badArgument();
        }
        if (!RegexUtil.isMobileExact(phoneNumber)) {
            return ResponseUtil.badArgumentValue();
        }

        if (!notifyService.isSmsEnable()) {
            return ResponseUtil.fail(AUTH_CAPTCHA_UNSUPPORT, "小程序后台验证码服务不支持");
        }
        String code = CharUtil.getRandomNum(6);
        logger.info("即将发送的验证码：" + code + "TO tel:" + phoneNumber);
        //todo 源码中是先发送 后加入缓存中
        // 应该第一步校验缓存中是否有号码
        // 先加入缓存 再发送 发送失败有重试机制

        //异步发送消息
        notifyService.notifySmsTemplate(phoneNumber, NotifyType.CAPTCHA, new String[]{code});

        //校验 是否重复发送短信 第一次发送则放入缓存
        boolean successful = CaptchaCodeManager.addToCache(phoneNumber, code);
        if (!successful) {
            return ResponseUtil.fail(AUTH_CAPTCHA_FREQUENCY, "验证码未超时1分钟，不能发送");
        }

        return ResponseUtil.ok();
    }

    /**
     * 账号注册
     *
     * @param body    请求内容
     *                {
     *                username: xxx,
     *                password: xxx,
     *                mobile: xxx
     *                code: xxx
     *                }
     *                其中code是手机验证码，目前还不支持手机短信验证码
     * @param request 请求对象
     * @return 登录结果
     * 成功则
     * {
     * errno: 0,
     * errmsg: '成功',
     * data:
     * {
     * token: xxx,
     * tokenExpire: xxx,
     * userInfo: xxx
     * }
     * }
     * 失败则 { errno: XXX, errmsg: XXX }
     */
    //todo 此处有 微信code 现阶段需要考虑考虑 怎么处理这个场景
    // 扫码注册时 需要设置账号密码及手机号验证码
    // 单纯注册时，微信code 就多余了
    @PostMapping("register")
    public Object register(@RequestBody String body, HttpServletRequest request) {
        String username = JacksonUtil.parseString(body, "username");
        String password = JacksonUtil.parseString(body, "password");
        String mobile = JacksonUtil.parseString(body, "mobile");
        String code = JacksonUtil.parseString(body, "code");
        String wxCode = JacksonUtil.parseString(body, "wxCode");

        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password) || StringUtils.isEmpty(mobile)
                || StringUtils.isEmpty(wxCode) || StringUtils.isEmpty(code)) {
            return ResponseUtil.badArgument();
        }

        List<LitemallUser> userList = userService.queryByUsername(username);
        if (userList.size() > 0) {
            return ResponseUtil.fail(AUTH_NAME_REGISTERED, "用户名已注册");
        }

        userList = userService.queryByMobile(mobile);
        if (userList.size() > 0) {
            return ResponseUtil.fail(AUTH_MOBILE_REGISTERED, "手机号已注册");
        }
        if (!RegexUtil.isMobileExact(mobile)) {
            return ResponseUtil.fail(AUTH_INVALID_MOBILE, "手机号格式不正确");
        }
        //判断验证码是否正确
        String cacheCode = CaptchaCodeManager.getCachedCaptcha(mobile);
        if (cacheCode == null || cacheCode.isEmpty() || !cacheCode.equals(code)) {
            return ResponseUtil.fail(AUTH_CAPTCHA_UNMATCH, "验证码错误");
        }

        String openId = null;
        try {
            WxMaJscode2SessionResult result = this.wxService.getUserService().getSessionInfo(wxCode);
            openId = result.getOpenid();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseUtil.fail(AUTH_OPENID_UNACCESS, "openid 获取失败");
        }
        userList = userService.queryByOpenid(openId);
        if (userList.size() > 1) {
            return ResponseUtil.serious();
        }
        if (userList.size() == 1) {
            LitemallUser checkUser = userList.get(0);
            String checkUsername = checkUser.getUsername();
            String checkPassword = checkUser.getPassword();
            if (!checkUsername.equals(openId) || !checkPassword.equals(openId)) {
                return ResponseUtil.fail(AUTH_OPENID_BINDED, "openid已绑定账号");
            }
        }

        LitemallUser user = null;
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(password);
        user = new LitemallUser();
        user.setUsername(username);
        user.setPassword(encodedPassword);
        user.setMobile(mobile);
        user.setWeixinOpenid(openId);
        user.setAvatar("https://yanxuan.nosdn.127.net/80841d741d7fa3073e0ae27bf487339f.jpg?imageView&quality=90&thumbnail=64x64");
        user.setNickname(username);
        user.setGender((byte) 0);
        user.setUserLevel((byte) 0);
        user.setStatus((byte) 0);
        user.setLastLoginTime(LocalDateTime.now());
        user.setLastLoginIp(IpUtil.getIpAddr(request));
        userService.add(user);

        // 给新用户发送注册优惠券
        couponAssignService.assignForRegister(user.getId());

        // userInfo
        UserInfo userInfo = new UserInfo();
        userInfo.setNickName(username);
        userInfo.setAvatarUrl(user.getAvatar());

        // token
        String token = UserTokenManager.generateToken(user.getId());

        Map<Object, Object> result = new HashMap<Object, Object>();
        result.put("token", token);
        result.put("userInfo", userInfo);
        return ResponseUtil.ok(result);
    }


    /**
     * 请求验证码
     * <p>
     * TODO
     * 这里需要一定机制防止短信验证码被滥用
     *
     * @param body 手机号码 { mobile: xxx, type: xxx }
     * @return
     */
    //todo 问题和注册的问题一样
    @PostMapping("captcha")
    public Object captcha(@LoginUser Integer userId, @RequestBody String body) {
        if (userId == null) {
            return ResponseUtil.unlogin();
        }
        String phoneNumber = JacksonUtil.parseString(body, "mobile");
        String captchaType = JacksonUtil.parseString(body, "type");
        if (StringUtils.isEmpty(phoneNumber)) {
            return ResponseUtil.badArgument();
        }
        if (!RegexUtil.isMobileExact(phoneNumber)) {
            return ResponseUtil.badArgumentValue();
        }
        if (StringUtils.isEmpty(captchaType)) {
            return ResponseUtil.badArgument();
        }

        if (!notifyService.isSmsEnable()) {
            return ResponseUtil.fail(AUTH_CAPTCHA_UNSUPPORT, "小程序后台验证码服务不支持");
        }
        String code = CharUtil.getRandomNum(6);
        // TODO
        // 根据type发送不同的验证码
        notifyService.notifySmsTemplate(phoneNumber, NotifyType.CAPTCHA, new String[]{code});

        boolean successful = CaptchaCodeManager.addToCache(phoneNumber, code);
        if (!successful) {
            return ResponseUtil.fail(AUTH_CAPTCHA_FREQUENCY, "验证码未超时1分钟，不能发送");
        }

        return ResponseUtil.ok();
    }

    /**
     * 账号密码重置
     *
     * @param body    请求内容
     *                {
     *                password: xxx,
     *                mobile: xxx
     *                code: xxx
     *                }
     *                其中code是手机验证码，目前还不支持手机短信验证码
     * @param request 请求对象
     * @return 登录结果
     * 成功则 { errno: 0, errmsg: '成功' }
     * 失败则 { errno: XXX, errmsg: XXX }
     */
    @PostMapping("reset")
    public Object reset(@RequestBody String body, HttpServletRequest request) {
        String password = JacksonUtil.parseString(body, "password");
        String mobile = JacksonUtil.parseString(body, "mobile");
        String code = JacksonUtil.parseString(body, "code");

        if (mobile == null || code == null || password == null) {
            return ResponseUtil.badArgument();
        }

        //判断验证码是否正确
        String cacheCode = CaptchaCodeManager.getCachedCaptcha(mobile);
        if (cacheCode == null || cacheCode.isEmpty() || !cacheCode.equals(code))
            return ResponseUtil.fail(AUTH_CAPTCHA_UNMATCH, "验证码错误");

        List<LitemallUser> userList = userService.queryByMobile(mobile);
        LitemallUser user = null;
        if (userList.size() > 1) {
            return ResponseUtil.serious();
        } else if (userList.size() == 0) {
            return ResponseUtil.fail(AUTH_MOBILE_UNREGISTERED, "手机号未注册");
        } else {
            user = userList.get(0);
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(password);
        user.setPassword(encodedPassword);

        if (userService.updateById(user) == 0) {
            return ResponseUtil.updatedDataFailed();
        }

        return ResponseUtil.ok();
    }

    /**
     * 账号手机号码重置
     *
     * @param body    请求内容
     *                {
     *                password: xxx,
     *                mobile: xxx
     *                code: xxx
     *                }
     *                其中code是手机验证码，目前还不支持手机短信验证码
     * @param request 请求对象
     * @return 登录结果
     * 成功则 { errno: 0, errmsg: '成功' }
     * 失败则 { errno: XXX, errmsg: XXX }
     */
    @PostMapping("resetPhone")
    public Object resetPhone(@LoginUser Integer userId, @RequestBody String body, HttpServletRequest request) {
        if (userId == null) {
            return ResponseUtil.unlogin();
        }
        String password = JacksonUtil.parseString(body, "password");
        String mobile = JacksonUtil.parseString(body, "mobile");
        String code = JacksonUtil.parseString(body, "code");

        if (mobile == null || code == null || password == null) {
            return ResponseUtil.badArgument();
        }

        //判断验证码是否正确
        String cacheCode = CaptchaCodeManager.getCachedCaptcha(mobile);
        if (cacheCode == null || cacheCode.isEmpty() || !cacheCode.equals(code))
            return ResponseUtil.fail(AUTH_CAPTCHA_UNMATCH, "验证码错误");

        List<LitemallUser> userList = userService.queryByMobile(mobile);
        LitemallUser user = null;
        if (userList.size() > 1) {
            return ResponseUtil.fail(AUTH_MOBILE_REGISTERED, "手机号已注册");
        }
        user = userService.findById(userId);

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (!encoder.matches(password, user.getPassword())) {
            return ResponseUtil.fail(AUTH_INVALID_ACCOUNT, "账号密码不对");
        }

        user.setMobile(mobile);
        if (userService.updateById(user) == 0) {
            return ResponseUtil.updatedDataFailed();
        }

        return ResponseUtil.ok();
    }

    /**
     * 账号信息更新
     *
     * @param body    请求内容
     *                {
     *                password: xxx,
     *                mobile: xxx
     *                code: xxx
     *                }
     *                其中code是手机验证码，目前还不支持手机短信验证码
     * @param request 请求对象
     * @return 登录结果
     * 成功则 { errno: 0, errmsg: '成功' }
     * 失败则 { errno: XXX, errmsg: XXX }
     */
    @PostMapping("profile")
    public Object profile(@LoginUser Integer userId, @RequestBody String body, HttpServletRequest request) {
        if (userId == null) {
            return ResponseUtil.unlogin();
        }
        String avatar = JacksonUtil.parseString(body, "avatar");
        Byte gender = JacksonUtil.parseByte(body, "gender");
        String nickname = JacksonUtil.parseString(body, "nickname");

        LitemallUser user = userService.findById(userId);
        if (!StringUtils.isEmpty(avatar)) {
            user.setAvatar(avatar);
        }
        if (gender != null) {
            user.setGender(gender);
        }
        if (!StringUtils.isEmpty(nickname)) {
            user.setNickname(nickname);
        }

        if (userService.updateById(user) == 0) {
            return ResponseUtil.updatedDataFailed();
        }

        return ResponseUtil.ok();
    }

    /**
     * 微信手机号码绑定
     *
     * @param userId
     * @param body
     * @return
     */
    @PostMapping("bindPhone")
    public Object bindPhone(@LoginUser Integer userId, @RequestBody String body) {
        if (userId == null) {
            return ResponseUtil.unlogin();
        }
        LitemallUser user = userService.findById(userId);
        String encryptedData = JacksonUtil.parseString(body, "encryptedData");
        String iv = JacksonUtil.parseString(body, "iv");
        WxMaPhoneNumberInfo phoneNumberInfo = this.wxService.getUserService().getPhoneNoInfo(user.getSessionKey(), encryptedData, iv);
        String phone = phoneNumberInfo.getPhoneNumber();
        user.setMobile(phone);
        if (userService.updateById(user) == 0) {
            return ResponseUtil.updatedDataFailed();
        }
        return ResponseUtil.ok();
    }

    @PostMapping("logout")
    public Object logout(@LoginUser Integer userId) {
        if (userId == null) {
            return ResponseUtil.unlogin();
        }
        return ResponseUtil.ok();
    }

    @GetMapping("info")
    public Object info(@LoginUser Integer userId) {
        if (userId == null) {
            return ResponseUtil.unlogin();
        }

        LitemallUser user = userService.findById(userId);
        Map<Object, Object> data = new HashMap<Object, Object>();
        data.put("nickName", user.getNickname());
        data.put("avatar", user.getAvatar());
        data.put("gender", user.getGender());
        data.put("mobile", user.getMobile());

        return ResponseUtil.ok(data);
    }

    /**
     * 微信回调
     * 正确的返回：
     * {
     * "access_token":"ACCESS_TOKEN",
     * "expires_in":7200,
     * "refresh_token":"REFRESH_TOKEN",
     * "openid":"OPENID",
     * "scope":"SCOPE",
     * "unionid": "o6_bmasdasdsad6_2sgVt7hMZOPfL"
     * }
     * 错误返回样例：
     * <p>
     * {"errcode":40029,"errmsg":"invalid code"}
     */
    @GetMapping("/callback")
    public Object callback(@RequestParam String code, @RequestParam String state) throws Exception {
        logger.info("code: {},state :{}", code, state);
        Map<Object, Object> data = new HashMap<Object, Object>();
        if (StringUtils.isEmpty(code)) {
            return ResponseUtil.unlogin();
        }
        StringBuilder url = new StringBuilder();
        url.append("https://api.weixin.qq.com/sns/oauth2/access_token?");
        url.append("appid=" + environment.getProperty("litemall.wx.app-id"));
        url.append("&secret=" + environment.getProperty("litemall.wx.app-secret"));
        //这是微信回调给你的code
        url.append("&code=" + code);
        url.append("&grant_type=authorization_code");
        String result = HttpClientUtils.get(url.toString(), "UTF-8");
        System.out.println("result:" + result.toString());
        // 根据unionid 查询是否有这个用户  有则直接取userId转变为token  和用户信息返回
        // 没有的话则 获取用户信息 新建用户
        //2.通过access_token和openid获取用户信息
        String userInfoUrl = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID";

        String userInfoStr = HttpClientUtils.get(userInfoUrl, "UTF-8");
        logger.info("userInfoObject:{}", userInfoStr);
        //插入数据库
        data.put("token", "''''");
        data.put("userInfo", "userInfo");
        return ResponseUtil.ok(data);
    }


    /***
     * 微信服务器触发get请求用于检测签名
     * @return
     */
    @GetMapping("/handleWxCheckSignature")
    @ResponseBody
    public String handleWxCheckSignature(HttpServletRequest request) {
        String echostr = request.getParameter("echostr");

        return echostr;
    }

    /**
     * 接收微信推送事件
     *
     * @param request
     * @return
     */
    @PostMapping("/handleWxCheckSignature")
    @ResponseBody
    public String handleWxEvent(HttpServletRequest request) {


        try {
            InputStream inputStream = request.getInputStream();

            Map<String, Object> map = XmlUtil.parseXML(inputStream);
            String userOpenId = (String) map.get("FromUserName");
            String event = (String) map.get("Event");
            logger.info("handleWxCheckSignature event:{}----userOpenId:{}", event, userOpenId);
            LitemallUser userInfo = litemallWXService.getUserInfo(userOpenId);
            if ("subscribe".equals(event)) {

                // TODO:获取unionId判断用户是否存在,不存在则获取新增用户,自己的业务
                LitemallUser user = userService.queryByUnionId(userInfo.getWeixinUnionid());
                Integer userId = user == null ? null : user.getId();
                if (null == user) {
                    userService.add(userInfo);
                    // 新用户发送注册优惠券
                    couponAssignService.assignForRegister(userInfo.getId());
                    userId = userInfo.getId();
                }
                //自己生成的二维码不管是关注还是扫码都能取到ticket凭证
                String ticket = (String) map.get("Ticket");
                redisCacheManager.set(ticket, userId.toString(), 10 * 60);

                logger.info("用户关注:{}", userOpenId);
            } else if ("SCAN".equals(event)) {
                LitemallUser user = userService.queryByUnionId(userInfo.getWeixinUnionid());
                if (null != user) {
                    logger.info("用户扫码:{}", userOpenId);
                    String ticket = (String) map.get("Ticket");
                    redisCacheManager.set(ticket, user.getId().toString(), 10 * 60);
                }
                //自己生成的二维码不管是关注还是扫码都能取到ticket凭证


            } else if ("unsubscribe".equals(event)) {
                logger.info("用户取消订阅 openId:{}", userOpenId);
            }
            logger.info("接收参数:{}", map);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "success";

    }

    /**
     * 用于检测扫码和关注状态
     *
     * @return
     */
    @GetMapping("/checkLogin")
    @ResponseBody
    public Object checkLogin(@RequestParam String ticket) {
        //如果redis中有ticket凭证则说明用户已扫码说明登陆成功
        if (redisCacheManager.hasKey(ticket)) {
            String id = redisCacheManager.get(ticket);
            // token
            String token = UserTokenManager.generateToken(Integer.valueOf(id));

            LitemallUser user = userService.findById(Integer.valueOf(id));
            UserInfo userInfo = UserInfo.builder()
                    .avatarUrl(user.getAvatar())
                    .nickName(user.getNickname())
                    .build();

            Map<Object, Object> result = new HashMap<Object, Object>();
            result.put("token", token);
            result.put("userInfo", userInfo);
            //扫码通过则删除
            redisCacheManager.delete(ticket);
            return ResponseUtil.ok(result);
        }
        return ResponseUtil.fail(AUTH_INVALID_ACCOUNT, "微信平台还未返回数据");
    }


    /**
     * 获取二维码参数
     *
     * @return
     */
    @GetMapping("/getQrCode")
    @ResponseBody
    public Object getQrCode() {


        return ResponseUtil.ok(litemallWXService.getQrCode());
    }

    @GetMapping("/handleWxCallMessage")
    @ResponseBody
    public String handleWxCallMessage(HttpServletRequest request) throws IOException {
        String echostr = request.getParameter("echostr");
        return echostr;
    }


    @PostMapping("/handleWxCallMessage")
    @ResponseBody
    public String callMessage(HttpServletRequest request) throws IOException {

        InputStream inputStream = request.getInputStream();

        Map<String, Object> map = XmlUtil.parseXML(inputStream);
        logger.info("微信返回数据：{}",map);
        String event = (String) map.get("Event");
        if ("wxa_media_check".equals(event)){
            String trace_id = (String)map.get("trace_id");
            String isrisky = (String)map.get("isrisky");
            litemallWXService.callMessage(trace_id,isrisky);
        }
        return "success";
    }



}
