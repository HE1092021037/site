package cn.localhost.site.Controller.server;

import cn.localhost.site.Model.Account;
import cn.localhost.site.Model.MyHeader;
import cn.localhost.site.Service.Service.AccountService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RequestMapping("/regist/ajax")
@RestController
@Transactional
public class HomeAjax {
    @Resource
    AccountService accountService;

    /**
     * 发送注册短信验证码
     * @return
     */
    @PostMapping("registGetCode")
    public MyHeader registCode(@RequestBody Account account){
        return accountService.registGetCode(account.getPhone());
    }

    /**
     * 检查 账号 重复
     * @return
     */
    @PostMapping("registCheckUser")
    public MyHeader registCheckUser(@RequestBody Account account){
        return accountService.registCheckUser(account);
    }


    /**
     * 检查 电话号 重复
     * @return
     */
    @PostMapping("registCheckMobile")
    public MyHeader registCheckMobile(Account account){
        return accountService.registCheckMobile(account);
    }

}
