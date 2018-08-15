package cn.localhost.site.Dao;

import cn.localhost.site.Common.Exception.MySQLException;
import cn.localhost.site.Model.SQLModel.AddModel;
import cn.localhost.site.Model.SQLModel.QueryModel;
import cn.localhost.site.Model.SQLModel.UpDateModel;
import cn.localhost.site.Utils.CommonUtils;
import com.alibaba.fastjson.JSON;
import lombok.extern.log4j.Log4j2;
import org.apache.ibatis.jdbc.SQL;

import java.util.HashMap;
import java.util.Map;

@Log4j2
public class CommonSQL {
    public static String query(QueryModel queryModel){
        if(!queryModel.review()){
            throw new MySQLException("构造SQL时缺少主要参数");
        }
        log.info("SQL query table = "+queryModel.getTableName());
        String SQL = new SQL(){
            {
                switch (queryModel.getQueryType()){
                    case QueryModel.TRAVERSE:
                        SELECT("*");
                        log.info("遍历表");
                        break;
                    case QueryModel.TOTAL:
                        SELECT("count(1)");
                        log.info("获取总数");
                        break;
                }
                FROM(queryModel.getTableName());
                Map<String,Object> params =
                        JSON.parseObject(JSON.toJSONString(queryModel.getPojo()),HashMap.class);
                for(String key : params.keySet()){
                    Object value = params.get(key);
                    if(value.getClass() ==  String.class && value != null && !value.equals("")){
                        log.info("查询条件 "+ CommonUtils.HumpToUnderline(key)+"="+value);
                        WHERE(CommonUtils.HumpToUnderline(key)+"='"+value+"'");
                    }else if(value.getClass() == int.class || value.getClass() == Integer.class){
                        if((int)value != 0){
                            log.info("查询条件 "+CommonUtils.HumpToUnderline(key)+"="+value);
                            WHERE(CommonUtils.HumpToUnderline(key)+"="+value);
                        }
                    }
                }
                if(queryModel.getDate() != null && queryModel.getDateColumn() != null){
                    log.info("限定日期 date="+queryModel.getDate());
                    WHERE("( datediff ( "+queryModel.getDateColumn()+" , '"+
                            queryModel.getDate()+"' ) = 0 )");
                }
                if(queryModel.getStartDate() != null && queryModel.getEndDate() != null &&
                        queryModel.getDateColumn() != null){
                    log.info("限定日期范围 "+queryModel.getStartDate()+" ---- "+queryModel.getEndDate());
                    WHERE(queryModel.getStartDate()+"<="+queryModel.getDateColumn());
                    WHERE(queryModel.getEndDate()+">="+queryModel.getDateColumn());
                }
                if(queryModel.getOrderByColumn() != null){
                    ORDER_BY(queryModel.getOrderByColumn());
                }
                if(queryModel.getGroupByColumn() != null){
                    ORDER_BY(queryModel.getGroupByColumn());
                }
            }
        }.toString();
        if(queryModel.getPage() != null && queryModel.getLimit() != null ){
            SQL = SQL+" limit "+queryModel.getStart()+","+queryModel.getEnd();
        }
        log.info("SQL = "+SQL);
        return SQL;
    }

    public static String add(AddModel addModel){
        if(!addModel.review()){
            throw new MySQLException("构造SQL时缺少主要参数");
        }
        log.info("新增数据 tableName="+addModel.getTableName());
        return new SQL(){
            {
                INSERT_INTO(addModel.getTableName());
                Map<String,Object> params =  JSON.parseObject(JSON.toJSONString(addModel.getPojo()),HashMap.class);
                for(String key : params.keySet()){
                    Object value = params.get(key);
                    key = CommonUtils.HumpToUnderline(key);
                    if(value.getClass() == String.class){
                        String v = String.valueOf(value);
                        if(v != null && !v.isEmpty()){
                            log.info("查询数据 key="+key+" ---- value="+value);
                            VALUES(key,"'"+v+"'");
                        }
                    }
                    if(value.getClass() == int.class || value.getClass() == Integer.class){
                        String v = String.valueOf(value);
                        if(!v.equals("0")){
                            log.info("查询数据 key="+key+" ---- value="+value);
                            VALUES(key,v);
                        }
                    }
                }
            }
        }.toString();
    }

    public static String upDateMapper(UpDateModel upDateModel){
        if(!upDateModel.review()){
            throw new MySQLException("构造SQL时缺少主要参数");
        }
        return new SQL(){
            {
                log.info("更新数据 tableName="+upDateModel.getTableName());
                UPDATE(upDateModel.getTableName());
                Map<String,Object> params =
                        JSON.parseObject(JSON.toJSONString(upDateModel.getPojo()),HashMap.class);
                WHERE("id="+params.get("id"));
                params.remove("id");
                for(String key : params.keySet()){
                    Object value = params.get(key);
                    key = CommonUtils.HumpToUnderline(key);
                    if(value.getClass() == String.class){
                        String v = String.valueOf(value);
                        if(v != null && !v.isEmpty()){
                            log.info("key="+key+" ---- value="+v);
                            SET(key+"='"+v+"'");
                        }
                    }
                    if(value.getClass() == int.class || value.getClass() == Integer.class){
                        String v = String.valueOf(value);
                        if(!v.equals("0")){
                            log.info("key="+key+" ---- value="+v);
                            SET(key+"="+v);
                        }
                    }
                }
            }
        }.toString();
    }
}
