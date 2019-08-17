var app = new Vue({
    el:"#app",
    data:{
        urls:{
            getAllTemplate:"http://localhost:8444/api/tool/excel/selectAllTemplate"
        },
        fullScreenLoading:false,
        insertOrUpdateDialog:{
            title:"模版设置",
            visible:false
        },
        table:{
            loading:false,
            data:[]
        }
    },
    methods:{
        refreshTable:function () {
            let app=this;
            let data={};
            ajaxPostJSON(app.urls.getAllTemplate,data,function (v) {
                app.table.data=v["data"];
                console.log(app.table.data);
            })
        },
        handleDialogClose:function () {
            this.insertOrUpdateDialog.visible=false;
        }
    },
    mounted:function () {
        this.refreshTable();
    }
})

function closeIframe() {
    app.insertOrUpdateDialog.visible=false;
}