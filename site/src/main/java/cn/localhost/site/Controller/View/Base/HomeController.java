package cn.localhost.site.Controller.View.Base;

import cn.localhost.site.Model.Account;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class HomeController {
    @GetMapping("/login")
    public String login(HttpServletRequest request){
        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("Account");
        if(account == null){
            account = new Account();
            account.setNikeName("请登录");
        }
        session.setAttribute("Account",account);
        return "Home/Login";
    }

    @GetMapping("/")
    public String home(HttpServletRequest request){
        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("Account");
        if(account == null){
            account = new Account();
            account.setNikeName("请登录");
        }
        session.setAttribute("Account",account);
        return "Home/Home";
    }

    @GetMapping("/regist")
    public String regio(HttpServletRequest request){
        return "Home/Regist";
    }


}
