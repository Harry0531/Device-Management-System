let app = new Vue({
    el: '#app',
    data: {
        showWindow: false,
        loading: false,
        tmpFileName: "",
        defaultFileList: [],
        department: [],
        wordDepartment: [],
        selectDep: "",
        wordDep: "",
        selectModel: '',
        urls: {
            getSub: 'http://localhost:8444/api/sys/info/confidential/getSub',
        },
        dataType: [{
            label: '涉密信息设备和存储设备台账',
            value: 'secret'
        }, {
            label: '非涉密信息设备和存储设备台账',
            value: 'noneSecret'
        }, {
            label: '报废涉密信息设备和存储设备台账',
            value: 'scrapped'
        }]
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
        toword: function () {
            if(this.selectModel === ''){
                this.$message.warning('请选择台账类型');
                return;
            }
            let filename;
            for(let i in app.department){
                if(app.department[i].id === app.selectDep){
                    filename = app.department[i].dept_name;
                    break;
                }
            }
            window.open("http://localhost:8444/api/tool/toword/toword" + "?department=" + app.selectDep + "&depName=" + filename + "&model=" + this.selectModel);
        }
    },
    mounted: function () {
        ajaxPost(this.urls.getSub, {param: "dept"}, function (result) {
            app.department = [{
                id: "",
                dept_name: "全部"
            }];
            result.forEach(function (r) {
                app.department.push(r);
                app.wordDepartment.push(r);
            })
        })
    }
});