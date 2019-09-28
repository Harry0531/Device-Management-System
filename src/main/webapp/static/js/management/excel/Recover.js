let app = new Vue({
    el: '#app',
    data: {
        showWindow: false,
        loading: false,
        tmpFileName: "",
        defaultFileList: [],
        department: [],
        selectDep: "",
        urls: {
            getSub: 'http://localhost:8444/api/sys/info/confidential/getSub',
        },
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
        beforeUpload: function (file) {
            let suffix = file.name.split('.').pop();
            if (suffix !== 'xlsx') {
                app.$message({message: '仅支持xlsx文件', type: 'error'});
                return false;
            }
        },
        // 上传完成后调用
        onUploadSuccess: function (res, file) {
            this.tmpFileName = res.data;
            this.defaultFileList[0] = file;
        },
        recover: function () {
            let app = this;
            if (this.tmpFileName === "") {
                app.$message({message: '请上传文件', type: 'error'});
                return;
            }
            this.loading = true;
            let data = {
                fileName: this.tmpFileName
            }
            ajaxPost("http://localhost:8444/api/tool/backup/recover", data, function () {
                app.$message({message: '数据更新成功', type: 'success'});
                app.loading = false;
            }, function () {
                app.loading = false;
                app.$message({message: '更新失败', type: 'error'});
            })
        },
        backup: function () {
            window.open("http://localhost:8444/api/tool/backup/backup" + "?department=" + app.selectDep);
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
            })
        })
    }
});