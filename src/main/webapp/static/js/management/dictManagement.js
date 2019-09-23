var app = new Vue({
    el: '#app',
    data: {
        fullScreenLoading: false,
        urls: {
            getDictList: 'http://localhost:8444/api/sys/dict/selectDictListByPage',
            insertDict:'http://localhost:8444/api/sys/dict/insertOrUpdateDict',
            deleteListByIds:'http://localhost:8444/api/sys/dict/deleteDictByIds',
            deleteListById:'http://localhost:8444/api/sys/dict/deleteDictById',
            getDictType:"http://localhost:8444/api/sys/dict/getDictTypeList"

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
                dicProperty:"",
                dicValue:"",
                fatherProperty:[],
                sort:"",
                remarks:"",
                delFlag:"",
                dis:false
            }
        },
        options:[]
    },
    methods: {
        //刷新表格数据
        refreshTable: function () {
            let app = this;
            app.table.loading = true;
            let data = {
                page: app.table.props,
                dicProperty:app.table.data.dicProperty
            };
            ajaxPostJSON(this.urls.getDictList, data, function (d) {
                app.table.loading = false;
                app.table.data = d.data.resultList;
                app.table.props.total = d.data.total;

            })
            let dataAll = {
                page:{
                    searchKey: '',
                    pageIndex: 1,
                    pageSize: 99999,
                    pageSizes: [5, 10, 20, 40],
                    total: 0
                }
            };
            app.options=[];
            ajaxPostJSON(app.urls.getDictList,dataAll,function (d) {
                d.data.resultList.forEach(function (v) {
                    let pos=-1;
                    app.options.forEach(function (h) {
                        if(h["label"] === v["dicProperty"]){
                            pos=app.options.indexOf(h);
                        }
                    })
                    if(pos === -1 ){
                        app.options.push({
                            label:v["dicProperty"],
                            value:v["dicProperty"],
                            children:[{
                                label:v["dicValue"],
                                value:v["dicValue"]
                            }]
                        })
                    }else{
                        app.options[pos].children.push({
                            label:v["dicValue"],
                            value:v["dicValue"]
                        })
                    }

                })
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
            app.dialog.data.dicProperty=v;
        },
        handleSelectTypeChange2:function (v) {
            var app=this;
            app.table.props.searchKey=v;
            app.refreshTable();
        },
        handleSelectFatherTypeChange:function(v){
            var app=this;
            console.log(v);
            app.dialog.data.fatherProperty=v;
        },
        insertOrUpdateDict: function () {
            let app = this;
            app.dialog.loading = true;
            let data = {
                dicProperty:app.dialog.data.dicProperty,
                dicValue:app.dialog.data.dicValue,
                fatherProperty:app.dialog.data.fatherProperty.toString(),
                sort:app.dialog.data.sort,
                remark:app.dialog.data.remarks,
                id:app.dialog.data.id,
            };
            ajaxPostJSON(this.urls.insertDict, data, function (d) {
                app.dialog.loading = false;
                app.dialog.visible=false;
                app.$message({
                    message:"操作成功",
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
                    dicProperty:"",
                    dicValue:"",
                    fatherProperty:[],
                    sort:"",
                    remarks:"",
                    delFlag:"",
                    dis:false
                }
            }
        },
        updateDialog:function (v) {
            var app=this;
            app.dialog.data.id=v["id"];
            app.dialog.data.dicProperty=v["dicProperty"];
            app.dialog.data.dicValue=v["dicValue"];
            app.dialog.data.fatherProperty=v["fatherProperty"].split(",");
            app.dialog.data.sort=v["sort"];
            app.dialog.data.remarks=v["remark"];
            app.dialog.data.delFlag=v["delFlag"];
            app.dialog.data.dis=true;
            app.dialog.visible=true;

        },
        disable:function (v) {
            var app=this;
            let data={
                dicts:v["id"]
            }
            ajaxPost(app.urls.deleteListById,data, function (d) {
                app.refreshTable();
            })

        }

    },
    mounted: function(){
        this.getDictTypeData();
        this.refreshTable();
    }
});