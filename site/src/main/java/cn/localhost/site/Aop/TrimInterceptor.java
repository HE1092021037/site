package cn.localhost.site.Aop;


import cn.localhost.site.Model.MyHeader;
import cn.localhost.site.Utils.Aop.AopParams;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Log4j2
public class TrimInterceptor {


    @Pointcut("@annotation(Trim)")
    public void paramsTrim(){

    }


    @Around("paramsTrim()")
    public Object filter(ProceedingJoinPoint pjd) throws Throwable {
        String class_name = pjd.getTarget().getClass().getName();
        String method_name = pjd.getSignature().getName();
        Object o = null;
        Object[] param = pjd.getArgs();
        for(int i=0;i<param.length;i++){
            if(param[i].getClass() == String.class){
                if(param[i].equals("\"\"")){
                    String[] paramNames = AopParams.getFieldsName(class_name, method_name);
                    log.error(paramNames[i].toUpperCase()+" Is Null");
                    return new MyHeader(1,paramNames[i].toUpperCase()+" Is Null","");
                }
                param[i] =  String.valueOf(param[i]).trim();
            }
        }
        o = pjd.proceed(param);
        return o;
    }



}
