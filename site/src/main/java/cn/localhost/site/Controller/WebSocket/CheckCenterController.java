package cn.localhost.site.Controller.WebSocket;

import cn.localhost.site.Model.MyHeader;
import cn.localhost.site.Model.ReModel.ReCode;
import cn.localhost.site.Service.WebSocket.WebSocketService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

@Controller
@RequestMapping("/checkcenter")
public class CheckCenterController {

    //页面请求
    @GetMapping("/socket/{cid}")
    public ModelAndView socket(@PathVariable String cid) {
        ModelAndView mav=new ModelAndView("/socket");
        mav.addObject("cid", cid);
        return mav;
    }
    //推送数据接口
    @ResponseBody
    @RequestMapping("/socket/push/{cid}")
    public MyHeader pushToWeb(@PathVariable String cid, String message) {
        try {
            WebSocketService.sendInfo(message,cid);
        } catch (IOException e) {
            e.printStackTrace();
            return new MyHeader(ReCode.FAILURE,"推送失败",e.getMessage());
        }
        return new MyHeader(ReCode.OK,"推送成功",cid);
    }

}