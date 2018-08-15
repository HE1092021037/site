var time = 180;
$('#get_code').on('click',function () {
    var phoneNumber = $('#phoneNumber').val();
    if(phoneNumber == ''){
        layer.tips('手机号码为空', '#phoneNumber',{tips: [1, '#3595CC'], time: 2000});
    }
    if(!isPoneAvailable(phoneNumber)){
        layer.tips('不是有效的号码呢', '#phoneNumber',{tips: [1, '#3595CC'], time: 2000});
    }
    $.ajax({
        type:"post",
        url:"/regist/ajax/registGetCode",
        dataType:"json",
        data:JSON.stringify({phone:phoneNumber}),
        contentType:"application/json",
        async:false,
        success:function (data) {
            if(data.code == OK){
                layer.msg(data.msg,{anim: 5,time: 1000},function(){
                    $('#get_code').text(time)
                    $('#get_code').attr('disabled',true);
                    var get_code = $('#get_code');
                    var timeId = setInterval(function () {
                        time -= 1;
                        if(time == 0){
                            $(get_code).removeAttr('disabled');
                            time = 180;
                        }
                        $(get_code).text(time);
                    },1000);
                    setTimeout( function(){
                        window.clearInterval(timeId);
                    },60*3*1000);
                });
            }else{
                layer.msg(data.msg,{anim: 5},function () {
                });
            }
        },
        error:function (err) {
            layer.msg('验证发送失败,请联系管理员');
        }
    });
});

$('#regist_button').on('click',function () {
    if(!regist_formNotNull()){
        return false;
    }
    $('#passWord').val(encryptByDES($('#passWord').val(),Key));
    $.ajax({
        type:"post",
        url:"/account/regist",
        dataType:"json",
        data:JSON.stringify($('#regist_form').serializeObject()),
        contentType:"application/json",
        async:false,
        success:function (data) {
            console.log(data);
            if(data.code == OK){
                layer.msg(data.msg,{anim: 5,time: 500},function(){
                    window.location.href="/";
                });
            }else{
                layer.msg(data.msg,{anim: 5});
            }
        },
        error:function (err) {
            layer.msg('注册出错,请联系管理员');
        }
    });
    return false;
});

$('#userName').change(function () {
    var userName = $('#userName').val();
    $.ajax({
        type:"post",
        url:"/regist/ajax/registCheckUser",
        dataType:"json",
        data:JSON.stringify({userName:userName}),
        contentType:"application/json",
        async:false,
        success:function (data) {
            if(data.code == OK){
                // layer.tips('用户名没有被注册~', '#userName',{tips: [1, '#3595CC'], time: 2000});
            }else{
                layer.tips(data.msg, '#userName',{tips: [1, '#3595CC'], time: 2000});
            }
        },
        error:function (err) {
            layer.msg('用户名检测失败,请联系管理员');
        }
    });
});


$('#phoneNumber').change(function () {
    var phoneNumber = $('#phoneNumber').val();
    $.ajax({
        type:"post",
        url:"/regist/ajax/registCheckMobile",
        dataType:"json",
        data:JSON.stringify({userName:phoneNumber}),
        contentType:"application/json",
        async:false,
        success:function (data) {
            if(data.code == OK){
                // layer.tips('用户名没有被注册~', '#userName',{tips: [1, '#3595CC'], time: 2000});
            }else{
                layer.tips(data.msg, '#phoneNumber',{tips: [1, '#3595CC'], time: 2000});
            }
        },
        error:function (err) {
            layer.msg('手机号码检测失败,请联系管理员');
        }
    });
});


function regist_formNotNull(){
    $("#regist form input").each(function(){
        if($(this).val() == ''){
            layer.tips('这里还是空的哦', this,{tips: [1, '#3595CC'], time: 2000});
            return false;
        }
    });
    return true;
}
