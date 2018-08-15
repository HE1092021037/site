package cn.localhost.site.Model;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseModel {
    protected Long id;
    public boolean reviewId(){
        if(this.id == 0){
            return false;
        }
        return true;
    }
}
