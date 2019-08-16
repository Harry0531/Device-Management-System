let nameMap = [
    {value: 'dept', label: '单位'},
    {value: 'type', label: '类型'},
    {value: 'secret_level', label: '密级'},
    {value: 'usage', label: '用途'},
    {value: 'scope', label: '使用范围'},
    {value: 'use_situation', label: '使用情况'},
    {value:'name',label:'姓名'},
    {value:'year',label:'年龄'},
    {value:'location',label:'所在地'}
];

var ex = new Vue({
    el:'#app',
    data:{
        urls:{
            getColumnInTableAndExcel:"http://localhost:8444/api/tool/excel/getColumnInTableAndExcel",
            exportToExcel:"http://localhost:8444/api/tool/excel/exportDataToExcel"
        },
        FieldList:[],
        ExportList:[],
        titles:["请选择导出列","已选中"],
        exportInfo:{}
    },
    methods:{
        //根据网页地址获得导出表名
        getTableNameByUrl:function (URL) {
            var args = URL.split("?");
            var result="";
            if(args[0] === URL){
                return result;
            }
            var str=args[1];
            args=str.split("&");
            for(var i =0 ;i<args.length;i++){
                str=args[i];
                var arg=str.split("=");
                if(arg.length <= 1) continue;
                if(arg[0] === "TableName") result =arg[1];
            }
            this.tableName=result;
        },
        getTableColumn:function () {
            var app=this;
            let data={
                tableName:app.exportInfo.tableName,
                ExcelName:null
            }
            ajaxPost(app.urls.getColumnInTableAndExcel,data,function (result) {
                result.data["tableColumnList"].forEach(function (v) {
                    nameMap.forEach(function (w) {
                        if(w["value"] === v){
                            app.FieldList.push({
                                key:v,
                                label:w["label"]
                            })
                        }
                    })
                })
            })
        },
        getExportData:function () {
            let app=this;
           let con =  window.parent.getExportConditions();
           let data={
               fileName:con["fileName"],
               tableName:con["tableName"],
               conditionsList:con["conditionsList"],
               idList:con["idList"]
           }
            app.exportInfo = data;
        },
        startExport:function () {
            let app=this;
            let  data={
                    fileName:app.exportInfo["fileName"],
                    tableName:app.exportInfo["tableName"],
                    fieldList:app.ExportList,
                    conditionsList:app.exportInfo["conditionsList"],
                    idList:app.exportInfo["idList"]
            }

        postcall(app.urls.exportToExcel,data);
        },

    },
    mounted:function () {
        this.getExportData();
        this.getTableColumn();
    }
})
function postcall( url, params, target){
    var tempform = document.createElement("form");
    tempform.id = "export"
    tempform.action = url;
    tempform.method = "post";
    tempform.style.display="none"
    if(target) {
        tempform.target = target;
    }

    for (var x in params) {
        var opt = document.createElement("input");
        opt.setAttribute("type","hidden");
        opt.setAttribute("name",x);
        opt.setAttribute("value",params[x]);
        tempform.appendChild(opt);
    }

    var opt = document.createElement("input");
    opt.type = "submit";
    tempform.appendChild(opt);
    document.body.appendChild(tempform);

    var options = {
            type: 'POST',
            url: url,
            success:function () {
                document.body.removeChild(tempform);
                tryAgain(url,params,target);
                setTimeout(2000);
                ex.$message({
                    message:"导出成功",
                    type:"success"
                })
            },
            // dataType: 'json',
            error : function(xhr, status, err) {
                console.log(err);
            }
        };
       $("#export").ajaxSubmit(options);





}
/**
 * 保存后，执行回调
 * @param responseText
 * @param statusText
 * @param xhr
 * @param $form
 */
function showResponse(responseText, statusText, xhr, $form){
    if(responseText.status === "0"){
        /**
         * 请求成功后的操作
         */
        alert(responseText.msg);
    } else {
        alert(responseText.msg);
    }
}
/**
 * 保存操作
 */
function toSave(){
    $("#Form名称").submit();
}

function tryAgain(url, params, target) {
    var tempform = document.createElement("form");
    tempform.id = "export"
    tempform.action = url;
    tempform.method = "post";
    tempform.style.display="none"
    if(target) {
        tempform.target = target;
    }

    for (var x in params) {
        var opt = document.createElement("input");
        opt.setAttribute("type","hidden");
        opt.setAttribute("name",x);
        opt.setAttribute("value",params[x]);
        tempform.appendChild(opt);
    }

    var opt = document.createElement("input");
    opt.type = "submit";
    tempform.appendChild(opt);
    document.body.appendChild(tempform);

     tempform.submit();
    document.body.removeChild(tempform);
}