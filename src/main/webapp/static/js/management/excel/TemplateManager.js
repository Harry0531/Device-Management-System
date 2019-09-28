var app = new Vue({
    el: "#app",
    data: {
        showWindow: false,
        urls: {
            getAllTemplate: "http://localhost:8444/api/tool/excel/selectAllTemplate"
        },
        fullScreenLoading: false,
        insertOrUpdateDialog: {
            title: "模版设置",
            visible: false
        },
        table: {
            loading: false,
            data: []
        }
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
        }
    },
    mounted: function () {
        this.refreshTable();
    }
})

function closeIframe() {
    app.insertOrUpdateDialog.visible = false;
}