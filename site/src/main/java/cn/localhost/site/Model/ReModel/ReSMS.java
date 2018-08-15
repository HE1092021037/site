package cn.localhost.site.Model.ReModel;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReSMS extends SendSmsResponse {
    private String redisValue;
}
