var app = new Vue({
    el: '#app',
    data: {
        fullScreenLoading: false,
        urls: {
            getSchoolList:'http://localhost:8444/api/sys/dept/getSchoolList',
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
        schoolList:[],
        dialog:{
            visible:false,
            loading:false,
            data:{
                id:"",
                typeId:"",
                typeName:"",
                dicProperty:"",
                dicValue:"",
                fatherId:""
            }
        }
    },
    methods: {
        getSchoolList:function(){
            let app= this;
            ajaxGet(app.urls.getSchoolList,null,function (d) {
                console.log(d);
                app.schoolList=d.data;
            });
            console.log(app.schoolList);
        },
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
            this.dialog.updateEntity.formData = copy(row);
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
            app.dialog.data.typeName=  v["typeName"];
            app.dialog.data.typeId= v["id"]
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
                id:app.dialog.data.id
            };
            ajaxPostJSON(this.urls.insertDict, data, function (d) {
                app.dialog.loading = false;
                app.dialog.visible=false;
                app.$message({
                    message:"插入成功",
                    type:"success"
                });
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
                    fatherId:""
                }
            }
        },
        updateDialog:function (v) {
            var app=this;
            app.dialog.data.id=v["id"];
            app.dialog.data.typeId=v["typeId"];
            app.dialog.data.typeName=v["typeName"];
            app.dialog.data.dicProperty=v["dicProperty"];
            app.dialog.data.dicValue=v["dicValue"];
            app.dialog.data.fatherId=v["fatherId"];
            app.dialog.visible=true;
        }

    },
    mounted: function(){
        this.getSchoolList();
        //this.refreshTable();
    }
});