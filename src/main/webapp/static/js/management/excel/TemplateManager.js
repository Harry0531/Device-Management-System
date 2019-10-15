var app = new Vue({
    el: "#app",
    data: {
        showWindow: false,
        urls: {
            getAllTemplate: "http://localhost:8444/api/tool/excel/selectAllTemplate",
            deleteExcelTemplate:"http://localhost:8444/api/tool/excel/deleteExcelTemplate"
        },
        fullScreenLoading: false,
        insertOrUpdateDialog: {
            title: "模版设置",
            visible: false
        },
        table: {
            loading: false,
            data: []
        },
        selections:[]
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
        refreshTable: function () {
            let app = this;
            let data = {};
            ajaxPostJSON(app.urls.getAllTemplate, data, function (v) {
                app.table.data = v["data"];
                console.log(app.table.data);
            })
        },
        handleDialogClose: function () {
            this.insertOrUpdateDialog.visible = false;
        },
        handleSelectionChange:function (val) {
            this.selections=[];
            let app=this;
            val.forEach(function (v) {
                app.selections.push(v["id"]);
            })
        },
        deleteTemplateByIds:function () {
            let app = this;
            let data = app.selections;
            if(data.length === 0 ) {
                app.$message({
                    type:"warning",
                    message: "请至少选择一项"
                })
                return ;
            }

            ajaxPostJSON(app.urls.deleteExcelTemplate, data, function (v) {
                app.$message({
                    type:"success",
                    message: "删除成功"
                })
                app.refreshTable();
            },function (v) {
                app.$message({
                    type:"error",
                    message: "删除失败"
                })
            })
        }
    },
    mounted: function () {
        this.refreshTable();
    }
})

function closeIframe() {
    app.insertOrUpdateDialog.visible = false;
}