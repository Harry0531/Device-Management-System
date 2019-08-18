

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
        // getTableNameByUrl:function (URL) {
        //     var args = URL.split("?");
        //     var result="";
        //     if(args[0] === URL){
        //         return result;
        //     }
        //     var str=args[1];
        //     args=str.split("&");
        //     for(var i =0 ;i<args.length;i++){
        //         str=args[i];
        //         var arg=str.split("=");
        //         if(arg.length <= 1) continue;
        //         if(arg[0] === "TableName") result =arg[1];
        //     }
        //     this.tableName=result;
        // },
        // getTableColumn:function () {
        //     var app=this;
        //     let data={
        //         tableName:app.exportInfo.tableName,
        //         ExcelName:null
        //     }
        //     ajaxPost(app.urls.getColumnInTableAndExcel,data,function (result) {
        //         result.data["tableColumnList"].forEach(function (v) {
        //             nameMap.forEach(function (w) {
        //                 if(w["value"] === v){
        //                     app.FieldList.push({
        //                         key:v,
        //                         label:w["label"]
        //                     })
        //                 }
        //             })
        //         })
        //     })
        // },
        getExportData:function () {
            let app=this;
           let con = getExportConditions();
           let data={
               fileName:con["fileName"],
               templateId:con["templateId"],
               fieldList:con["fieldList"],
               conditionsList:con["conditionsList"],
               idList:con["idList"],
               isScrapped:con["isScrapped"],
               tableName:con["tableName"]
           }
            app.exportInfo = data;
            app.FieldList=con["fieldList"];
        },
        startExport:function () {
            let app=this;
            var selectList=[];
            app.exportInfo["fieldList"].forEach(function (v) {
                if(app.ExportList.indexOf(v["fieldType"]) !== -1){
                    selectList.push(v["fieldName"]);
                }
            })
            let  data={
                    fileName:app.exportInfo["fileName"],
                    templateId:app.exportInfo["templateId"],
                    fieldName:selectList,
                    fieldType:app.ExportList,
                    conditionsList:app.exportInfo["conditionsList"],
                    idList:app.exportInfo["idList"],
                    isScrapped:app.exportInfo["isScrapped"],
                    tableName:app.exportInfo["tableName"]
            }

        postcall(app.urls.exportToExcel,data);
        },

    },
    mounted:function () {
        this.getExportData();
        // this.getTableColumn();
    }
})

function postcall(url, params, target) {
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
function getExportConditions() {
    let data={
        fileName:"导出测试1",
        templateId:"f45f9396b321488180cc7de2af8b535c",
        fieldList:[
            {
                fieldName:"单位",
                fieldType:"department"
            },{
                fieldName:"科室/课题组",
                fieldType:"subject"
            },{
                fieldName:"类型",
                fieldType:"type",
            },{
                fieldName:"保密编号",
                fieldType:"secret_number"
            },{
                fieldName:"固定资产编号",
                fieldType:"asset_number"
            },{
                fieldName:"负责人",
                fieldType:"person"
            },{
                fieldName:"密级",
                fieldType:"secret_level"
            },{
                fieldName:"品牌型号",
                fieldType:"model"
            },{
                fieldName:"操作系统版本",
                fieldType:"os_version"
            },{
                fieldName:"操作系统安装时间",
                fieldType:"os_install_time"
            },{
                fieldName:"硬盘序列号",
                fieldType:"serial_number"
            },{
                fieldName:"mac地址",
                fieldType:"mac_address"
            },{
                fieldName:"光驱",
                fieldType:"cd_drive"
            },{
                fieldName:"用途",
                fieldType:"usage"
            },{
                fieldName:"放置地点",
                fieldType:"place_location"
            },{
                fieldName:"启用时间",
                fieldType:"enablation_time"
            },{
                fieldName:"使用情况",
                fieldType:"use_situation"
            },{
                fieldName:"备注",
                fieldType:"remarks"
            }
        ],
        conditionsList:[],
        idList:[],
        isScrapped:false,
        tableName:"confidential_computer"
    }
    return data;
}