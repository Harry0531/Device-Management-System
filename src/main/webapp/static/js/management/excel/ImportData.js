let app = new Vue({
    el: '#app',
    data: {
        showWindow: false,
        formData: { // 与后端ExcelTemplate相对应
            id: '',
            excelName: '',      // excel模板文件的名字(默认存放/WEB-INF/excelTemplate)
            excelDataName: '',  // 存放数据的excel文件的名字(默认存放在/WEB-INF/temp)
            typeId: ''
        },
        urls: {
            selectAllExcelTemplate: 'http://localhost:8444/api/tool/excel/selectAllTemplate',
            downloadExcelTemplate: 'http://localhost:8444/api/tool/excel/downloadExcelTemplate',
            importExcelToTable: 'http://localhost:8444/api/tool/excel/importExcelToTable'
        },
        excelTemplateList: [],
        defaultFileList: [],
        tmpFileName: '', // 当前上传的文件存储在服务器的临时文件夹上的名字
        loading: {
            importing: false
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
        // 上传模板前调用
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
        // get all templates those has been enabled
        selectExcelTemplateByEnabled: function () {
            let excelTemplate = {
                enable: true
            };
            this.fullScreenLoading = true;
            let app = this;
            ajaxPostJSON(this.urls.selectAllExcelTemplate, excelTemplate, function (d) {
                app.fullScreenLoading = false;
                app.excelTemplateList = d.data;
            })
        },
        onSelectChange: function (value) {
            let flag = false;
            this.excelTemplateList.forEach(item => {
                if (flag) return;
                if (item.id === value) {
                    this.formData = JSON.parse(JSON.stringify(item));
                    flag = true;
                }
            });
        },
        downloadExcelTemplate: function () {
            if (this.formData.id == null || this.formData.id === '') {
                window.parent.parent.app.showMessage('请先选择模板!', 'warning');
                return;
            }
            let excelName = this.formData.excelName;
            let downloadName = this.formData.templateName;
            let requestUrl = this.urls.downloadExcelTemplate +
                "?excelName=" + excelName + "&downloadName=" + downloadName;
            // location.href = requestUrl;
            window.open(requestUrl);
        },
        startImport: function () {
            if (this.formData.id == null || this.formData.id === '') {
                // app.showMessage('请先选择模板!', 'warning');
                this.$message.warning('请先选择模板!');
                return;
            }
            if (this.tmpFileName == null || this.tmpFileName === '') {
                // app.showMessage('请先上传数据文件!', 'warning');
                this.$message.warning('请先上传数据文件!');
                return;
            }
            this.formData.excelDataName = this.tmpFileName;
            this.loading.importing = true;
            let app = this;
            console.log(this.formData);
            ajaxPostJSON(this.urls.importExcelToTable, this.formData, async function (d) {
                app.loading.importing = false;
                console.log(d)
                let success = d.data.successUpdate + d.data.successInsert
                await app.$notify({
                    title: '成功',
                    type: 'success',
                    message: "成功导入数据" + success + "条<br>" +
                        "成功插入数据" + d.data.successInsert + "条<br>" +
                        "成功更新数据" + d.data.successUpdate + "条",
                    dangerouslyUseHTMLString: true,
                    duration: 0
                });

                if(d.data.failInsert + d.data.failUpdate > 0) {
                    let failMessage = "";
                    for(let i in d.data.failMessage){
                        failMessage += d.data.failMessage[i] + "<br>"
                    }
                    failMessage = "插入失败" + d.data.failInsert + "条<br>" +
                        "更新失败" + d.data.failUpdate + "条<br>" + failMessage;
                    await app.$notify({
                        title: '失败',
                        type: 'error',
                        message: "<div class='errorBox'>" + failMessage + "</div>",
                        dangerouslyUseHTMLString: true,
                        duration: 0
                    });
                }

            }, function (d) {
                app.loading.importing = false;
                app.$message({
                    message: "导入失败",
                    type: "error"
                });
            })
        }
    },
    mounted: function () {
        this.selectExcelTemplateByEnabled();
    }
});