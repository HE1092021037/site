package cn.localhost.site.Mapper;


import cn.localhost.site.Dao.AccountDao;
import cn.localhost.site.Model.Account;
import cn.localhost.site.Model.SQLModel.AddModel;
import cn.localhost.site.Model.SQLModel.QueryModel;
import cn.localhost.site.Model.SQLModel.UpDateModel;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

public interface AccountMapper {
    @SelectProvider(type = AccountDao.class, method = "queryTotal")
    int queryTotal(QueryModel queryModel);

    @SelectProvider(type = AccountDao.class, method = "query")
    @Results(id = "account",value = {
            @Result(property = "nikeName", column = "nike_name"),
            @Result(property = "userName", column = "user_name"),
            @Result(property = "passWord",column = "pass_word"),
            @Result(property = "createTime",column = "create_time"),
            @Result(property = "lastLoginTime",column = "last_login_time"),
            @Result(property = "lastLoginIP",column = "last_login_iP"),
            @Result(property = "containsInfo",column = "contains_info"),
            @Result(property = "accountId",column = "account_id")
    })
    List<Account> query(QueryModel queryModel);

    @InsertProvider(type = AccountDao.class, method = "add")
    int add(AddModel addModel);

    @InsertProvider(type = AccountDao.class, method = "update")
    int update(UpDateModel upDateModel);

}
