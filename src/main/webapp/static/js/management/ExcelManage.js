var app = new Vue({
    el:'#app',
    data:{
        activeTabName:"tabManager",
        tabList:[{
            src:"./excel/TemplateManager.html",
            label:"模版管理",
            name:"tabManager"
        },{
            src:"./excel/importData.html",
            label:"导入数据",
            name:"importData"
        }]
    } ,
    methods:{
        tabClick:function (tab) {
            if(tab.name === "importData" && document.getElementById('importData') != null){
                document.getElementById("importData").contentWindow.location.reload(true);
            }
        },

    }
})