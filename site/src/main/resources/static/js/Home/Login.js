$("#login_button").on("click",function(){
    if($('#userName').val() == ''){
        layer.tips('没有输入账号', '#userName',{tips: [1, '#3595CC'], time: 2000});
        return false;
    }
    if($('#passWord').val() == ''){
        layer.tips('密码为空', '#passWord',{tips: [1, '#3595CC'], time: 2000});
        return false;
    }
    $('#passWord').val(encryptByDES($('#passWord').val(),Key));
    getIp();
    $.ajax({
        type:"post",
        url:"/account/login",
        dataType:"json",
        data:JSON.stringify($('#login_form').serializeObject()),
        contentType:"application/json",
        async:false,
        success:function (data) {
            if(data.code == OK){
                layer.msg(data.msg,{anim: 5,time: 500},function(){
                    clearForm();
                    window.location.href="/";
                });
            }else{
                layer.msg(data.msg,{anim: 5},function () {
                    if(data.code == PASSWORD_ERROR){
                        $('#passWord').val('');
                    }else if(data.code == NOUSER){
                        clearForm();
                    }
                });
            }
        },
        error:function (err) {
            layer.msg('登录出错,请联系管理员');
            clearForm();
        }
    });
    return false;
});
function clearForm(){
    $("#login form input").each(function(){
        $(this).val('');
    });
    $("#login form select").each(function(){
        $(this).val('');
    });
}

function getIp(){
    var nowIP = returnCitySN.cip;
    $('#lastLoginIP').val(nowIP);
}
