var app =new Vue({
    el:'#app',
    data:{
        tableName:"",
        urls:{
            getColumnInTableAndExcel:"http://localhost:8444/api/tool/excel/getColumnInTableAndExcel"
        }
    },
    methods:{
        //根据网页地址获得导出表名
        getTableNameByUrl:function (URL) {
            var args = URL.split("?");
            var result="";
            if(args[0] === URL){
                return result;
            }
            var str=args[1];
            args=str.split("&");
            for(var i =0 ;i<args.length;i++){
                str=args[i];
                var arg=str.split("=");
                if(arg.length <= 1) continue;
                if(arg[0] === "TableName") result =arg[1];
            }
            this.tableName=result;
        },
        getTableColumn:function () {
            var app=this;
            let data={
                tableName:app.tableName,
                ExcelName:null
            }
            ajaxPost(app.urls.getColumnInTableAndExcel,data,function (result) {
                console.log(result.data);
            })
        }
    },
    mounted:function () {
        this.getTableNameByUrl(window.location.search);
        this.getTableColumn();
    }
});