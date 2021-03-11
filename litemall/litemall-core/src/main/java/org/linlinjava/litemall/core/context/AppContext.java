package org.linlinjava.litemall.core.context;

/**
 * 全局ThreadLocal
 */
public class AppContext {
    //全局用户持有容器，用户登录id
    private static ThreadLocal<Integer> userHolder=new ThreadLocal<>();

    // 设置用户id
    public static void setUserId(Integer userId){
        userHolder.set(userId);
    }
    // 获取用户id
    public static Integer getUserId(){
        return userHolder.get();
    }

    // 清除资源 避免内存溢出
    public static void remove(){
        userHolder.remove();
    }

}
