package cn.localhost.site.Dao;

import cn.localhost.site.Model.SQLModel.AddModel;
import cn.localhost.site.Model.SQLModel.QueryModel;
import cn.localhost.site.Model.SQLModel.UpDateModel;
import org.springframework.stereotype.Component;

@Component
public class AccountDao {
    public String queryTotal(QueryModel queryModel){
        return CommonSQL.query(queryModel);
    }

    public String query(QueryModel queryModel){
        return CommonSQL.query(queryModel);
    }

    public String add(AddModel addModel){
        return CommonSQL.add(addModel);
    }

    public String update(UpDateModel upDateModel){
        return CommonSQL.upDateMapper(upDateModel);
    }
}
