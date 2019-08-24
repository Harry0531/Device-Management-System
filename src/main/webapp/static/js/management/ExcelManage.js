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
        },
            {
                src:"./excel/Recover.html",
                label:"数据备份",
                name:"recover"
            }]
    } ,
    methods:{
        tabClick:function (tab) {
            if(tab.name === "tabManager" && document.getElementById('tabManager') != null){
                document.getElementById("tabManager").contentWindow.location.reload(true);
            }
            if(tab.name === "recover" && document.getElementById('recover') != null){
                document.getElementById("recover").contentWindow.location.reload(true);
            }
        },

    }
})