var app=new Vue({
    el:'#app',
    data:{
        StepCount:1,
        TableList:[],
        urls:{
            getTableList:"http://localhost:8444/api/tool/excel/getTableList",
            getDictType:'http://localhost:8444/api/tool/excel/getTemplateTypeList',
            getDict:'http://localhost:8444/api/sys/dict/getDictTypeList',

            uploadFile:"http://localhost:8444/api/tool/file/uploadFile",
            getColumnInTableAndExcel:"http://localhost:8444/api/tool/excel/getColumnInTableAndExcel",
            insertExcelTemplate:"http://localhost:8444/api/tool/excel/insertExcelTemplate"
        },
        loading:{
            step1:false,
            step2:false
        },
        templateEntity:{
            templateName:"",       //模版名
            tableName:"",       //对应表名
            filePath:"" ,  //文件名
            TypeId:"" ,  //对应数据字典类型ID
            TypeName:""
        },
        ColumnList:{
            ExcelColumnList:[],
            TableColumnList:[],
            mapList:[],
        },
        dictType:[],
        Dict:[],
    },
    methods: {
        // 上传模板前调用
        beforeUpload: function (file) {
            let suffix = file.name.split('.').pop();
            if (suffix !== 'xlsx') {
                app.$message({
                    message: "仅支持xlsx文件",
                    type: "error"
                });
                return false;
            }
        },
        //上传完成调用
        SuccessUpload: function (res) {
            this.templateEntity.filePath = res.data;
        },
        nextStep: function () {
            var app = this;
            app.loading.step1 = true;
            let data = {
                tableName: app.templateEntity.tableName,
                ExcelName: app.templateEntity.filePath
            };
            ajaxGet(app.urls.getDict, null, function (v) {
                app.Dict = v.data;
                app.Dict.push("部门");
                console.log(app.Dict)
            })
            ajaxPost(app.urls.getColumnInTableAndExcel, data, function (result) {
                app.ColumnList.ExcelColumnList = result.data["excelColumnList"];
                app.ColumnList.TableColumnList = result.data["tableColumnList"];

                app.ColumnList.TableColumnList.forEach(function (v) {
                    app.ColumnList.mapList.push({
                        tableColumnName: v,
                        ExcelColumnIndex: -1,
                        isDict:false,
                        dict:null,
                        columnIndex:null,
                        columnName:null
                    })
                });
                app.loading.step1 = false;
                app.StepCount = 2;
            })
        },
        beforeStep: function () {
            var app = this;
            app.StepCount = 1;

        },
        submit: function () {
            console.log(this.ColumnList.mapList);
            let app = this;
            let data = {
                templateName: app.templateEntity.templateName,
                tableName: app.templateEntity.tableName,
                filePath: app.templateEntity.filePath,
                typeId: app.templateEntity.typeId,
                columnMapFieldList: app.ColumnList.mapList
            }
            ajaxPostJSON(app.urls.insertExcelTemplate, data, function (v) {
                app.$message({
                    message: "新建模版成功！",
                    type: "success"
                })
                //清空数据
                app.ColumnList = {
                    ExcelColumnList: [],
                    TableColumnList: [],
                    mapList: []
                }
                window.setTimeout(function (v) {
                    window.parent.app.insertOrUpdateDialog.visible=false;
                },1000);

            })
        },
        setColumnName($event, row) {
            for (let i = 0; i < this.ColumnList.ExcelColumnList.length; i++) {
                if (this.ColumnList.ExcelColumnList[i].columnIndex === $event) {
                    row.columnName = this.ColumnList.ExcelColumnList[i].columnName;
                    return;
                }
            }
        },
        //获取数据库表名列表
        getTableList: function () {
            let app = this;
            ajaxGet(app.urls.getTableList, null, function (v) {
                app.TableList = v.data;
            })
        },
        handleSelectTypeChange: function (v) {
            var app = this;
            app.templateEntity.TypeId = v;
            console.log(app.dictType);
            app.dictType.forEach(function (w) {
                if (v["id"] === w["id"]) {
                    app.templateEntity.tableName = w["tableName"]
                    app.templateEntity.typeName = w["typeName"]
                }
            })
        },
        getDictTypeData: function () {
            let app = this;
            ajaxGet(app.urls.getDictType, null, function (d) {
                app.dictType = d.data;
            })
        },

    },
    mounted:function () {
        this.getTableList();
        this.getDictTypeData();
    }
})