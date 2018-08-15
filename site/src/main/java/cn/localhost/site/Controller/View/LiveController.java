package cn.localhost.site.Controller.View;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("live")
public class LiveController {
    @GetMapping("/init")
    public String init(){
        return "Live";
    }
}
