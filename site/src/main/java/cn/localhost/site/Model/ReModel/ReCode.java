package cn.localhost.site.Model.ReModel;


public class ReCode {
    public static int OK = 10000;

    public static int FAILURE = 10001;

    /**没有数据*/
    public static int NODATA = 10002;

    /**用户已存在*/
    public static int USER_EXIST = 10003;

    /**手机号码已注册*/
    public static int MOBILE_EXIST = 10004;

    /**没有这个用户*/
    public static int NOUSER = 10005;

    /**密码错误*/
    public static int PASSWORD_ERROR = 10006;

    /**验证码错误*/
    public static int CODE_ERROR = 10007;

    /**短信验证码发送失败*/
    public static int SEND_CODE_ERROR = 10008;

    /**获取验证码时间CD*/
    public static int SEND_CODE_TIME_ERROR = 10009;

}
