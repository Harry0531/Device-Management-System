var app = new Vue({
    el: '#app',
    data: {
        fullScreenLoading: false,
        urls: {
            getDictList: 'http://localhost:8444/api/sys/dict/selectDictListByPage',
            getDictType:'http://localhost:8444/api/sys/dict/getDictTypeList',
            insertDict:'http://localhost:8444/api/sys/dict/insertOrUpdateDict',

            deleteListByIds:'/api/doc/fund/deleteByIds',
            deleteAll:'/api/doc/fund/deleteAll'
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
        dialog:{
            visible:false,
            loading:false,
            data:{
                typeId:"",
                typeName:"",
                dicProperty:"",
                dicValue:"",
                fatherId:""
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
                window.parent.app.showMessage('提示：未选中任何项', 'warning');
                return;
            }
            window.parent.app.$confirm('确认删除选中的项', '警告', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                let data = fundList;
                let app = this;
                app.table.loading = true;
                ajaxPostJSON(this.urls.deleteListByIds, data, function (d) {
                    window.parent.app.showMessage('删除成功！', 'success');
                    app.refreshTable();
                })
            }).catch(() => {
                window.parent.app.showMessage('已取消删除', 'warning');
            });
        },
        deleteAll:function () {
            window.parent.app.$confirm('确认全部删除？','警告',{
                confirmButtonText:'确定',
                cancelButtonText: "取消",
                type:'warning'
            }).then(()=>{
                let app = this;
                ajaxPostJSON(this.urls.deleteAll,null,function (v) {
                    window.parent.app.showMessage("删除成功","success");
                    app.refreshTable();
                })
            }).catch(()=>{
                window.parent.app.showMessage('已取消删除', 'warning');
            })
        },
        handleSelectTypeChange:function (v) {
            var app=this;
            app.dialog.data.typeName=  v["typeName"];
            app.dialog.data.typeId= v["id"]
        },
        insertDict: function () {
            let app = this;
            app.dialog.loading = true;
            let data = {
                typeId:app.dialog.data.typeId,
                typeName:app.dialog.data.typeName,
                dicProperty:app.dialog.data.dicProperty,
                dicValue:app.dialog.data.dicValue,
                fatherId:""
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
        }

    },
    mounted: function(){
        this.getDictTypeData();
        this.refreshTable();
    }
});