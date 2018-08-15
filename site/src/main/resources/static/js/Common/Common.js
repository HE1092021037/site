const Key = "hetianqi";

const  OK = 10000;

const  FAILURE = 10001;

/**没有数据*/
const  NODATA = 10002;

/**用户已存在*/
const  USER_EXIST = 10003;

/**手机号码已注册*/
const MOBILE_EXIST = 10004;

/**没有这个用户*/
const  NOUSER = 10005;

/**密码错误*/
const  PASSWORD_ERROR = 10006;

/**验证码错误*/
const CODE_ERROR = 10007;

/**短信验证码发送失败*/
const SEND_CODE_ERROR = 10008;

/**获取验证码时间CD*/
const SEND_CODE_TIME_ERROR = 10009;


/**
 * layui导航菜单悬浮二级菜单
 */
layui.use('element', function(){
    var element = layui.element; //导航的hover效果、二级菜单等功能，需要依赖element模块
    //监听导航点击
    element.on('nav(demo)', function(elem){
        //console.log(elem)
        layer.msg(elem.text());
    });
});

/**
 * form表单序列 用于post请求
 */
$.fn.serializeObject = function() {
    var o = {};
    var a = this.serializeArray();
    $.each(a, function () {
        if (o[this.name]) {
            if (!o[this.name].push) {
                o[this.name] = [o[this.name]];
            }
            o[this.name].push(this.value || '');
        } else {
            o[this.name] = this.value || '';
        }
    });
    return o;
}

/**
 * 手机号码正则
 * @param str
 * @returns {boolean}
 */
function isPoneAvailable(str) {
    var myreg=/^[1][3,4,5,7,8][0-9]{9}$/;
    if (!myreg.test(str)) {
        return false;
    } else {
        return true;
    }
}
console.log($('#header_a').val());
if($('#header_a').text() == '请登录'){
    $('.layui-nav-child').append('<dd><a href="/login">登陆</a></dd>');
}else{
    $('.layui-nav-child').append('<dd><a href="javascript:;">个人中心</a></dd>');
    $('.layui-nav-child').append('<dd><a href="javascript:;">注销</a></dd>');
}
