package org.linlinjava.litemall.wx.interceptors;

import org.linlinjava.litemall.core.context.AppContext;
import org.linlinjava.litemall.wx.service.UserTokenManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class WebInterceptor implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(WebInterceptor.class);

    public static final String LOGIN_TOKEN_KEY = "X-Litemall-Token";
    /**
     * 打印接口 名称
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        /*ServletInputStream inputStream = request.getInputStream();
        inputStream.mark(0);
        String string = IOUtils.toString(inputStream, "UTF-8");*/
        //inputStream.reset();
      //AppContext.setUserId(1);
        logger.info("rest访问路径：{}", request.getRequestURL());
        String token = request.getHeader(LOGIN_TOKEN_KEY);
        if (token == null || token.isEmpty()) {
            return true;
        }
        Integer userId = UserTokenManager.getUserId(token);
        AppContext.setUserId(userId);
        return true;
    }

    /**
     * 清除 AppContext 内的资源 避免内存溢出
     *
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                @Nullable Exception ex) throws Exception {
        AppContext.remove();
    }
}
