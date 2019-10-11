let app = new Vue({
    el: '#app',
    data: {
        showWindow: false,
        aaa: 0,
        fullScreenLoading: false,
        selectionType: [
            {value: 0, label: '学院/部门'},
            {value: 1, label: '科室/课题组'},
        ],
        urls: {
            getSchoolList: 'http://localhost:8444/api/sys/dept/getSchoolList',
            getList: 'http://localhost:8444/api/sys/dept/getList',
            insertOrUpdateDept: 'http://localhost:8444/api/sys/dept/insertOrUpdateDept',
            deleteListByIds: 'http://localhost:8444/api/sys/dept/deleteListByIds',//批量禁用
            changeDisable: 'http://localhost:8444/api/sys/dept/changeDisable'
        },
        table: {
            loading: false,
            selectionList: [],
            data: [],
            props: {
                searchKey: '',
                pageIndex: 1,
                pageSize: 10,
                pageSizes: [5, 10, 20, 40],
                total: 0
            },
            id: ''
        },
        schoolList: [],
        dialog: {
            visible: false,
            loading: false,
            data: {
                id: '',
                dept_name: '',
                dept_code: '',
                dept_attach: '',
                sort: '',
                dept_type: 0,
                _dept_type: '',
                _dept_attach: '',
                delFlag: ''
            }
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
                window.open("../login.html", "_self")
            }, 2000);
        },
        getSchoolList: function () {
            let app = this;
            ajaxGet(app.urls.getSchoolList, null, function (result) {
                app.schoolList = result.data;
            });
        },
        refreshTable: function () {
            console.log("refreshTable");
            let app = this;
            app.table.loading = true;
            let data = {
                page: app.table.props,
                id: app.table.id
            };
            ajaxPostJSON(this.urls.getList, data, function (result) {
                app.table.loading = false;
                app.table.data = result.data.resultList;
                app.table.props.total = result.data.total;
            })
        },
        // 处理pageSize变化
        onPageSizeChange: function (newSize) {
            this.table.props.pageSize = newSize;
            this.refreshTable();
        },
        // 处理pageIndex变化
        onPageIndexChange: function (newIndex) {
            this.table.props.pageIndex = newIndex;
            this.refreshTable();
        },
        // 处理选中的行变化
        onSelectionChange: function (val) {
            this.table.selectionList = val;
        },
        insertOrUpdateDept: function () {
            let app = this;
            app.dialog.loading = true;
            let data = {
                id: app.dialog.data.id,
                dept_type: app.dialog.data.dept_type,
                dept_attach: app.dialog.data.dept_attach,
                dept_name: app.dialog.data.dept_name,
                dept_code: app.dialog.data.dept_code,
                sort: app.dialog.data.sort == '' ? 0 : app.dialog.data.sort,
                delFlag: app.dialog.data.delFlag
            };
            if (app.dialog.data.dept_type == 0) {
                data.dept_attach = 0;
            }
            ajaxPostJSON(this.urls.insertOrUpdateDept, data, function (result) {
                app.dialog.loading = false;
                app.dialog.visible = false;
                if (result.code == "success") {
                    app.$message({
                        message: "操作成功",
                        type: "success"
                    });
                } else {
                    let msg = "操作失败" + result.data;
                    app.$message({
                        message: msg,
                        type: "error"
                    });
                }
                app.getSchoolList();
                app.refreshTable();
            })
        },
        deleteByIds: function (fundList) {
            if (fundList.length === 0) {
                app.$message({
                    message: "未选中任何项",
                    type: "danger"
                });
                return;
            }
            app.$confirm('确认禁用选中的项', '警告', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                let data = fundList;
                let app = this;
                app.table.loading = true;
                ajaxPostJSON(this.urls.deleteListByIds, data, function (d) {
                    app.$message({
                        message: "操作成功",
                        type: "success"
                    });
                    app.refreshTable();
                })
            }).catch(() => {
                app.$message({
                    message: "取消操作",
                    type: "danger"
                });
            });
        },
        resetDialogData: function () {
            let app = this;
            app.dialog = {
                visible: false,
                loading: false,
                data: {
                    id: '',
                    dept_name: '',
                    dept_code: '',
                    dept_attach: '',
                    dept_type: 0,
                    _dept_type: '',
                    _dept_attach: '',
                    delFlag: '',
                    sort: ''
                }
            }
        },
        updateDialog: function (v) {
            let app = this;
            app.dialog.data.id = v["id"];
            app.dialog.data.dept_name = v["dept_name"];
            app.dialog.data.dept_code = v["dept_code"];
            app.dialog.data.dept_type = v["dept_type"];
            app.dialog.data._dept_type = v["_dept_type"];
            app.dialog.data._dept_attach = v["_dept_attach"];
            app.dialog.data.dept_attach = v["dept_attach"];
            app.dialog.data.sort = v["sort"];
            app.dialog.data.delFlag = v["delFlag"];
            if (app.dialog.data.dept_attach == '0')
                app.dialog.data.dept_attach = '';
            app.dialog.visible = true;
        },
        handleDeptTypeChange: function (type) {
            if (type == 0) {
                this.dialog.data.dept_attach = '';
            }
        },
        disable: function (v) {
            // let flag = v.delFlag === true ? 1 : 0;
            let data = {
                id: v.id,
                flag: v.delFlag
                // flag: flag
            };
            ajaxPost(this.urls.changeDisable, data);
        }
    },
    mounted: function () {
        this.getSchoolList();
        this.refreshTable();
    },
    computed: {
        isAttachDisabled: function () {
            return (this.dialog.data.dept_type === 0)
                || (this.dialog.data.dept_type == null || this.dialog.data.dept_type === '')
        },
        isUpdateDisabled: function () {
            return !(this.dialog.data.dept_type != null
                && this.dialog.data.dept_name
                && this.dialog.data.dept_code
                && (this.dialog.data.dept_attach || this.isAttachDisabled));
        }
    }
});