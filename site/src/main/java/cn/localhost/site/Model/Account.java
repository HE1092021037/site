package cn.localhost.site.Model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Account extends BaseModel{

    public static int ORDINARY = 1;

    public static int secondaryadmin = 2;

    public static int SENIORADMIN= 3;

    public static int SUPERADMIN = 4;

    private Integer role;

    private String phone;

    private String nikeName;

    private String userName;

    private String passWord;

    private String createTime;

    private String lastLoginTime;

    private String lastLoginIP;

    private String containsInfo;

    private String accountId;

    private String profilePhoto;
}
