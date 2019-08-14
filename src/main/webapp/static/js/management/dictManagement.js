var app = new Vue({
    el: '#app',
    data: {
        fullScreenLoading: false,
        urls: {
            getDictList: 'http://localhost:8444/api/sys/dict/selectDictListByPage',
            getDictType:'http://localhost:8444/api/sys/dict/getDictTypeList',
            getFieldList:"http://localhost:8444/api/tool/excel/getColumnInTableAndExcel",
            insertDict:'http://localhost:8444/api/sys/dict/insertOrUpdateDict',
            deleteListByIds:'http://localhost:8444/api/sys/dict/deleteDictByIds',

        },
        table: {
            loading: false,
            selectionList: [],
            data: [],
            props: {
                searchKey: '',
                pageIndex: 1,
                pageSize: 10,
                pageSizes: [5, 10, 20, 40],
                total: 0
            },
            typeId:''
        },
        dictType:[],
        FieldList:[],
        dialog:{
            visible:false,
            loading:false,
            data:{
                id:"",
                typeId:"",
                typeName:"",
                tableName:"",
                dicProperty:"",
                dicValue:"",
                fatherId:"",
                enName:""
            }
        }
    },
    methods: {
        //刷新表格数据
        refreshTable: function () {
            let app = this;
            app.table.loading = true;
            let data = {
                page: app.table.props,
                typeId:app.table.typeId
            };
            ajaxPostJSON(this.urls.getDictList, data, function (d) {
                app.table.loading = false;
                app.table.data = d.data.resultList;
                app.table.props.total = d.data.total;
            })
        },
        //得到数据字典类型
        getDictTypeData:function(){
            let app= this;
            ajaxGet(app.urls.getDictType,null,function (d) {
                app.dictType=d.data;
            })
        },
        // 处理pageSize变化
        onPageSizeChange: function (newSize) {
            this.table.props.pageSize = newSize;
            this.refreshTable();
        },
        // 处理pageIndex变化
        onPageIndexChange: function (newIndex) {
            this.table.props.pageIndex = newIndex;
            this.refreshTable();
        },
        //打开修改弹窗
        openDialog_updateEntity: function (row) {
            this.dialog.updateEntity.visible = true;
            this.dialog.updateEntity.formData = JSON.parse(JSON.stringify(row));
        },
        // 处理选中的行变化
        onSelectionChange: function (val) {
            this.table.selectionList = val;
        },
        formatYear: function(timestamp){
            let date = new Date(timestamp);
            return date.Format("yyyy");
        },
        deleteByIds: function(fundList){
            if (fundList.length === 0) {
                app.$message({
                    message: "未选中任何项",
                    type:"danger"
                });
                return;
            }
            app.$confirm('确认删除选中的项', '警告', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                let data = fundList;
                let app = this;
                app.table.loading = true;
                ajaxPostJSON(this.urls.deleteListByIds, data, function (d) {
                    app.$message({
                        message: "删除成功",
                        type:"success"
                    });
                    app.refreshTable();
                })
            }).catch(() => {
                app.$message({
                    message: "取消删除",
                    type:"danger"
                });
            });
        },
        handleSelectTypeChange:function (v) {
            var app=this;
            console.log(v);
            app.dialog.data.tableName=v["tableName"];
            app.dialog.data.typeName=  v["typeName"];
            app.dialog.data.typeId= v["id"]

            let data={
                tableName:v["tableName"],
                ExcelName:null
            };
            ajaxPost(app.urls.getFieldList,data,function (result) {
                app.FieldList = result.data["tableColumnList"]
            })

        },
        insertOrUpdateDict: function () {
            let app = this;
            app.dialog.loading = true;
            let data = {
                typeId:app.dialog.data.typeId,
                typeName:app.dialog.data.typeName,
                dicProperty:app.dialog.data.dicProperty,
                dicValue:app.dialog.data.dicValue,
                fatherId:"",
                id:app.dialog.data.id,
                enName:app.dialog.data.enName
            };
            ajaxPostJSON(this.urls.insertDict, data, function (d) {
                app.dialog.loading = false;
                app.dialog.visible=false;
                app.$message({
                    message:"插入成功",
                    type:"success"
                })
                app.refreshTable();
            })
        },
        resetDialogData:function () {
            var app=this;
            app.dialog={
                visible:false,
                loading:false,
                data:{
                    id:"",
                    typeId:"",
                    typeName:"",
                    dicProperty:"",
                    dicValue:"",
                    fatherId:"",
                    enName:"",
                    tableName:""
                }
            }
        },
        updateDialog:function (v) {
            console.log(v);
            var app=this;
            app.dialog.data.id=v["id"];
            app.dialog.data.typeId=v["typeId"];
            app.dialog.data.typeName=v["typeName"];
            app.dialog.data.dicProperty=v["dicProperty"];
            app.dialog.data.dicValue=v["dicValue"];
            app.dialog.data.fatherId=v["fatherId"];
            //找到表名
            console.log(app.dictType);
            let tableName='';
            app.dictType.forEach(function (w) {
                if(w["typeName"] === v["typeName"]){
                    tableName=w["tableName"];
                }
            })
            let data={
                tableName:tableName,
                ExcelName:null
            };
            ajaxPost(app.urls.getFieldList,data,function (result) {
                app.FieldList = result.data["tableColumnList"]
            })
            app.dialog.visible=true;
        }

    },
    mounted: function(){
        this.getDictTypeData();
        this.refreshTable();
    }
});