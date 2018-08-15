package cn.localhost.site.Model.SQLModel;


import cn.localhost.site.Model.BaseModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SQLBaseModel {
    protected BaseModel pojo;

    protected String tableName;

    public boolean review(){
        if(this.pojo == null || this.tableName.equals("")){
            return false;
        }
        return true;
    }
}
