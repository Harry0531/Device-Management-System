
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