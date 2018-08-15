package cn.localhost.site.Model.SQLModel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QueryModel extends SQLBaseModel {
    public static final int TRAVERSE = 1;

    public static final int TOTAL = 2;

    private Integer limit;

    private Integer page;

    private String date;

    private String dateColumn;

    private Integer queryType;

    private String startDate;

    private String endDate;

    private String orderByColumn;

    private String groupByColumn;

    public boolean review(){
        if(this.queryType == 0 || pojo == null || tableName.equals("")){
            return false;
        }
        return true;
    }

    public String getStart(){
        return String.valueOf((this.page - 1) * limit);
    }

    public String getEnd(){
        return  String.valueOf(limit);
    }
}
