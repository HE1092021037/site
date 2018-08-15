package cn.localhost.site.Model;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

@Getter
@Setter
public class MyHeader {
    private int code;

    private String msg;

    private Object data;

    public MyHeader(int code, String msg, Object data) {
        if(data.equals("")){
            data = new HashMap<>();
        }
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
}
