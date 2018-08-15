package cn.localhost.site.Service.Service;

import cn.localhost.site.Common.ALiYunSmsErrorCode;
import cn.localhost.site.Common.Exception.MySQLException;
import cn.localhost.site.Mapper.AccountMapper;
import cn.localhost.site.Model.Account;
import cn.localhost.site.Model.MyHeader;
import cn.localhost.site.Model.ReModel.ReCode;
import cn.localhost.site.Model.ReModel.ReSMS;
import cn.localhost.site.Model.RegistAccount;
import cn.localhost.site.Model.SQLModel.AddModel;
import cn.localhost.site.Model.SQLModel.QueryModel;
import cn.localhost.site.Model.SQLModel.SQLBaseModel;
import cn.localhost.site.Model.SQLModel.UpDateModel;
import cn.localhost.site.Service.SmsService;
import cn.localhost.site.Utils.CommonUtils;
import cn.localhost.site.Utils.DESUtil;
import cn.localhost.site.Utils.RedisUtils;
import com.aliyuncs.exceptions.ClientException;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Log4j2
public class AccountService {

    @Resource
    AccountMapper accountMapper;

    @Resource
    SmsService smsService;

    @Resource
    RedisUtils redisUtils;

    public MyHeader regist(RegistAccount account, HttpServletRequest request) {
        log.info("请求注册帐号");
        if(!account.getCode().equals(redisUtils.get(account.getPhone()))){
            return new MyHeader(ReCode.CODE_ERROR,"验证码错误","");
        }
        try {
            String passWord = DESUtil.decryption(account.getPassWord(), "hetianqi");
            account.setPassWord(DESUtil.encryption(passWord));
        } catch (Exception e) {
            e.printStackTrace();
            return new MyHeader(ReCode.FAILURE, "注册异常 请联系管理员", "");
        }
        QueryModel queryModel = new QueryModel();
        queryModel.setTableName("account");
        queryModel.setQueryType(QueryModel.TOTAL);
        Account bean = new Account();
        bean.setUserName(account.getUserName());
        queryModel.setPojo(bean);
        if (accountMapper.queryTotal(queryModel) > 0) {
            log.info("用户已存在");
            return new MyHeader(ReCode.USER_EXIST, "用户已存在", account);
        }
        bean.setPhone(account.getPhone());
        queryModel.setPojo(bean);
        if (accountMapper.queryTotal(queryModel) > 0) {
            log.info("手机号已注册");
            return new MyHeader(ReCode.MOBILE_EXIST, "手机号已注册", account);
        }
        account.setAccountId(CommonUtils.getUUID());
        account.setCreateTime(CommonUtils.getNowTime(0));
        account.setRole(Account.ORDINARY);
        queryModel.setPojo(account);
        request.getSession().setAttribute("Account",account);

        AddModel addModel = new AddModel();
        addModel.setPojo(account);
        addModel.setTableName("account");
        return accountMapper.add((AddModel) addModel) <= 0 ?
                new MyHeader(ReCode.FAILURE, "注册失败 请联系管理员", "") :
                new MyHeader(ReCode.OK, "注册成功 即将跳转主页", "");
    }

    public MyHeader login(Account account, HttpServletRequest request) {
        log.info("请求帐号登录 userName=" + account.getUserName());
        String passWord = "";
        try {
            passWord = DESUtil.decryption(account.getPassWord(), "hetianqi");
            passWord = DESUtil.encryption(passWord);
        } catch (Exception e) {
            e.printStackTrace();
            return new MyHeader(ReCode.FAILURE, "登录异常 请联系管理员", "");
        }
        account.setPassWord(passWord);
        QueryModel queryModel = new QueryModel();
        queryModel.setTableName("account");
        queryModel.setQueryType(QueryModel.TRAVERSE);
        Account bean = new Account();
        bean.setUserName(account.getUserName());
        queryModel.setPojo(bean);
        List<Account> beanList = accountMapper.query(queryModel);
        if (beanList.size() <= 0) {
            return new MyHeader(ReCode.NOUSER, "帐号不存在,请检查用户名", "");
        }
        Account oldBean = beanList.get(0);
        if (!oldBean.getPassWord().equals(account.getPassWord())) {
            return new MyHeader(ReCode.PASSWORD_ERROR, "密码错误,请重试", "");
        }
        oldBean.setLastLoginTime(CommonUtils.getNowTime(0));
        UpDateModel upDateModel = new UpDateModel();
        upDateModel.setTableName("account");
        upDateModel.setPojo(oldBean);
        if (accountMapper.update(upDateModel) <= 0) {
            return new MyHeader(ReCode.FAILURE, "登录失败,请稍候重试", "");
        }
        HttpSession session = request.getSession();
        session.setAttribute("Account",oldBean);
        if(oldBean.getLastLoginTime() == null || oldBean.getLastLoginTime().equals("")){
            return new MyHeader(ReCode.OK, "咸鱼+1~", "");
        }
        return new MyHeader(ReCode.OK, oldBean.getNikeName()+" 欢迎回来~", "");
    }

    public MyHeader registCheckUser(Account account){
        QueryModel pojo = new QueryModel();
        pojo.setTableName("account");
        pojo.setQueryType(QueryModel.TRAVERSE);
        pojo.setPojo(account);
        List<Account> accounts = query(pojo);
        if(accounts.size() <= 0){
            return new MyHeader(ReCode.OK,"OK","");
        }
        return new MyHeader(ReCode.USER_EXIST,"这个账号已经被注册了","");
    }

    public MyHeader registCheckMobile(Account account){
        QueryModel pojo = new QueryModel();
        pojo.setTableName("account");
        pojo.setQueryType(QueryModel.TRAVERSE);
        pojo.setPojo(account);
        List<Account> accounts = query(pojo);
        if(accounts.size() <= 0){
            return new MyHeader(ReCode.OK,"OK","");
        }
        return new MyHeader(ReCode.USER_EXIST,"这个手机号码已经被注册过了","");
    }

    public MyHeader registGetCode(String phone){
        ReSMS response = null;
        try {
            if(redisUtils.exists(phone)){
                return new MyHeader(ReCode.SEND_CODE_TIME_ERROR,"请求频繁,请稍后再试","");
            }
            response = smsService.sendSms(phone);
            if(!response.getCode().equals("OK")){
                String alySMS_Msg =
                        ALiYunSmsErrorCode.reCodeMap.get(response.getCode()) != null
                                ?
                                ALiYunSmsErrorCode.reCodeMap.get(response.getCode())
                        :
                                "验证码发送失败,请联系管理员";
                return new MyHeader(ReCode.SEND_CODE_ERROR,alySMS_Msg,"");
            }
        } catch (ClientException e) {
            e.printStackTrace();
            return new MyHeader(ReCode.SEND_CODE_ERROR,"验证码发送失败,请联系管理员","");
        }
        redisUtils.set(phone,response.getRedisValue(),(long)3,TimeUnit.MINUTES);
        return new MyHeader(ReCode.OK,"验证码已发送","");
    }

    private List<Account> query(QueryModel queryModel){
        return accountMapper.query(queryModel);
    }

}
