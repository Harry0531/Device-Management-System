var app = new Vue({
    el:'#app',
    data:{
        activeTabName:"importData",
        tabList:[{
                src:"./excel/importData.html",
                label:"导入数据",
                name:"importData"
            },
            {
            src:"./excel/TemplateManager.html",
            label:"模版管理",
            name:"tabManager"
        }]
    } ,
    methods:{
        tabClick:function (tab) {
            if(tab.name === "tabManager" && document.getElementById('tabManager') != null){
                document.getElementById("tabManager").contentWindow.location.reload(true);
            }
        },

    }
})