package cn.localhost.site.Aop;

import cn.localhost.site.Utils.Aop.AopParams;
import cn.localhost.site.Utils.RedisUtils;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Aspect
@Log4j2
public class SsoInterceptor {

    @Resource
    RedisUtils redisUtils;

    @Pointcut("@annotation(Sso)")
    public void sso() {

    }


    @Around("sso()")
    public Object filter(ProceedingJoinPoint pjd) throws Throwable {
        String class_name = pjd.getTarget().getClass().getName();
        String method_name = pjd.getSignature().getName();
        Object o = null;
        Object[] param = pjd.getArgs();
        String[] paramNames = AopParams.getFieldsName(class_name, method_name);
        Map<String,Object> params = new HashMap<>();
        for (int i = 0; i < param.length; i++) {
            params.put(paramNames[i],param[i]);
        }
        if(params.get("requestFrom") != null && (int)params.get("requestFrom") == 3){
            return pjd.proceed(param);
        }
        if(params.get("liveId") == null || params.get("mac") == null){
            o = pjd.proceed(param);
        }else if(!params.get("mac").equals(redisUtils.get(String.valueOf(params.get("liveId"))))){
            List list = new ArrayList();
//            o = new MyHeader(ReCode.MAC_DONT_CONFORMITY,"MAC_DONT_CONFORMITY",list);
//            Account account = liveMapper.getLiveIdByliveId(String.valueOf(params.get("liveId")));
//            if(account == null){
//                return new MyHeader(ReCode.FAILURE,"DONT USER","");
//            }
            redisUtils.set(String.valueOf(params.get("liveId")),params.get("mac"));
        }
        if( o == null ){
            o = pjd.proceed(param);
        }
        return o;
    }
}
