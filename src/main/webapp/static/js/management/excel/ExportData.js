var ex = new Vue({
    el: '#app',
    data: {
        showWindow: false,
        urls: {
            getColumnInTableAndExcel: "http://localhost:8444/api/tool/excel/getColumnInTableAndExcel",
            exportToExcel: "http://localhost:8444/api/tool/excel/exportDataToExcel"
        },
        FieldList: [],
        ExportList: [],
        titles: ["未选中", "已选中"],
        exportInfo: {},
        defaultChecked:[]
    },
    created: function () {
        this.checkStatus();
    },
    methods: {
        //判断登录状态
        checkStatus() {
            if (getCookie("name") != null) {
                this.showWindow = true;
                return;
            }
            this.$message({
                message: "请登录",
                type: 'error'
            });
            setTimeout(function () {
                window.open("../../login.html", "_self")
            }, 2000);
        },
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
        getExportData: function () {
            let app = this;
            let con = window.parent.getExportConditions();
            let data = {
                fileName: con["fileName"],
                templateId: con["templateId"],
                fieldList: con["fieldList"],
                conditionsList: con["conditionsList"],
                idList: con["idList"],
                isScrapped: con["isScrapped"],
                tableName: con["tableName"]
            }
            app.exportInfo = data;
            app.FieldList = con["fieldList"];
            app.FieldList.forEach(function (v) {
                app.defaultChecked.push(v["fieldType"])
                app.ExportList.push(v["fieldType"])
            })
        },
        startExport: function () {
            let app = this;

            app.getExportData();
            var selectList = [];
            app.exportInfo["fieldList"].forEach(function (v) {
                if (app.ExportList.indexOf(v["fieldType"]) !== -1) {
                    selectList.push(v["fieldName"]);
                }
            })
            let data = {
                fileName: app.exportInfo["fileName"],
                templateId: app.exportInfo["templateId"],
                fieldName: selectList,
                fieldType: app.ExportList,
                conditionsList: app.exportInfo["conditionsList"],
                idList: app.exportInfo["idList"],
                isScrapped: app.exportInfo["isScrapped"],
                tableName: app.exportInfo["tableName"]
            }

            postcall(app.urls.exportToExcel, data);
            app.ExportList = []
        },

    },
    mounted: function () {
        this.getExportData();

    }
})

function postcall(url, params, target) {
    var tempform = document.createElement("form");
    tempform.id = "export"
    tempform.action = url;
    tempform.method = "post";
    tempform.style.display = "none"
    if (target) {
        tempform.target = target;
    }

    for (var x in params) {
        var opt = document.createElement("input");
        opt.setAttribute("type", "hidden");
        opt.setAttribute("name", x);
        opt.setAttribute("value", params[x]);
        tempform.appendChild(opt);
    }

    var opt = document.createElement("input");
    opt.type = "submit";
    tempform.appendChild(opt);
    document.body.appendChild(tempform);

    tempform.submit();
    document.body.removeChild(tempform);
}
