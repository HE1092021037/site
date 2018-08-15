package cn.localhost.site.Controller.Account;

import cn.localhost.site.Aop.Trim;
import cn.localhost.site.Model.Account;
import cn.localhost.site.Model.MyHeader;
import cn.localhost.site.Model.RegistAccount;
import cn.localhost.site.Service.Service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RequestMapping("/account")
@RestController
@Transactional
public class AccountCtroller {
    @Autowired
    AccountService  accountService;

    @PostMapping("regist")
    @Trim
    public MyHeader regist(@RequestBody RegistAccount account, HttpServletRequest request){
        return accountService.regist(account, request);
    }

    @PostMapping("login")
    @Trim
    public MyHeader login(@RequestBody Account account, HttpServletRequest request){
        return accountService.login(account, request);
    }

}
