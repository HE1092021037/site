package cn.localhost.site.Common.Exception;

/**
 * MyBatis SQL构造器自定义异常 通常为参数异常
 */
public class MySQLException extends NullPointerException{

    public MySQLException() {
        super();
    }

    public MySQLException(String message)
    {
        super(message);
    }
}
