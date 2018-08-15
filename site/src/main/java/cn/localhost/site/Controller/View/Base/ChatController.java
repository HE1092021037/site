package cn.localhost.site.Controller.View.Base;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ChatController {

    @GetMapping("chat")
    public String chat(Model model, HttpServletRequest request){
        return "Base/chatRoom";
    }
}
