
/**
 * 通用Get方法
 * @param url 后端接口地址
 * @param data 传送的数据
 * @param successCallback 成功回调函数（可选）
 * @param errorCallBack 失败回调函数（可选）
 */
function ajaxGet(url, data, successCallback, errorCallback) {
    $.ajax({
        type: 'GET',
        url: url,
        data: data,
        cache: false,
        success: successCallback,
        error: errorCallback
    })
}

/**
 * 通用POST方法
 * @param url 后端接口地址
 * @param data 传送的数据
 * @param successCallback 成功回调函数（可选）
 * @param errorCallback 失败回调函数（可选）
 */
function ajaxPost(url, data, successCallback, errorCallback, async = true) {
    $.ajax({
        type: 'POST',
        url: url,
        data: data,
        cache: false,
        success: successCallback,
        error: errorCallback,
        async: async
    })
}

/**
 * 通用POST方法，data会被自动转化为json格式的字符串
 * @param url 后端接口地址
 * @param data 传送的数据
 * @param successCallback 成功回调函数（可选）
 * @param errorCallback 失败回调函数（可选）
 */
function ajaxPostJSON(url, data, successCallback, errorCallback, async = true) {
    $.ajax({
        type: 'POST',
        url: url,
        data: JSON.stringify(data),
        cache: false,
        success: successCallback,
        error: errorCallback,
        dataType: 'json',
        contentType: 'application/json;charset=utf-8',
        async: async
    })
}

/**
 * 设置cookie
 * @param name cookie的名称
 * @param value cookie的值
 * @param day cookie的过期时间
 */
var setCookie = function (name, value, day) {
    if(day !== 0){     //当设置的时间等于0时，不设置expires属性，cookie在浏览器关闭后删除
        var expires = day * 24 * 60 * 60 * 1000;
        var date = new Date(+new Date()+expires);
        document.cookie = name + "=" + escape(value) + ";expires=" + date.toUTCString();
    }else{
        document.cookie = name + "=" + escape(value);
    }
};

/**
 * 获取对应名称的cookie
 * @param name cookie的名称
 * @returns {null} 不存在时，返回null
 */
var getCookie = function (name) {
    var arr;
    var reg = new RegExp("(^| )" + name + "=([^;]*)(;|$)");
    if (arr = document.cookie.match(reg))
        return unescape(arr[2]);
    else
        return null;
};

/**
 * 删除cookie
 * @param name cookie的名称
 */
var delCookie = function (name) {
    setCookie(name, '', -1);
};