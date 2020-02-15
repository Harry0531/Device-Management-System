let defaultFiltersCondition = {
    scope: '',
    use_situation: '',
    usage: '',
    secret_level: '',
    type: '',
    school: '',
    subject: '',
    startTime: '',
    endTime: ''
};

let defaultDialog = {
    visible: false,
    loading: false,
    data: {
        id: '',
        department: '',
        department_code: '',
        department_name: '',
        subject: '',
        subject_code: '',
        subject_name: '',
        secret_number: '',
        type: '',
        model: '',
        person: '',
        secret_level: '',
        serial_number: '',
        place_location: '',
        usage: '',
        scope: '',
        enablation_time: '',
        use_situation: '',
        remarks: '',
        _type: '',
        _secret_level: '',
        _usage: '',
        _scope: '',
        _use_situation: '',
    },
    selectionList: {
        type: [],
        secret_level: [],
        usage: [],
        scope: [],
        use_situation: [],
        subject: [],
        department: []
    }
};

let defaultScrapDialog = {
    visible: false,
    loading: false,
    data: {
        id: '',
        scrap_time: '',
        remarks: ''
    }
};

let prevFilter = {
    scope: '',
    use_situation: '',
    usage: '',
    secret_level: '',
    type: '',
    school: '',
    subject: '',
    startTime: '',
    endTime: ''
};

let app = new Vue({
    el: '#app',
    data: {
        showWindow: false,
        loading: false,
        urls: {
            getSub: 'http://localhost:8444/api/sys/storage/confidential/getSub',
            getDeptSub: 'http://localhost:8444/api/sys/storage/confidential/getDeptSub',
            getList: 'http://localhost:8444/api/sys/storage/confidential/getList',
            insertOrUpdateStorage: 'http://localhost:8444/api/sys/storage/confidential/insertOrUpdateStorage',
            deleteListByIds: 'http://localhost:8444/api/sys/storage/confidential/deleteListByIds',
            deleteAll: 'http://localhost:8444/api/sys/storage/confidential/deleteAll',
            scrap: 'http://localhost:8444/api/sys/storage/confidential/scrap'
        },
        activeNames: [],
        filters: {
            selectionList: {
                scope: [],
                use_situation: [],
                usage: [],
                secret_level: [],
                type: [],
                school: [],
                subject: []
            },
            condition: defaultFiltersCondition
        },
        dialog: defaultDialog,
        scrapDialog: defaultScrapDialog,
        table: {
            loading: false,
            selectionList: [],
            data: [],
            props: {
                searchKey: '',
                pageIndex: 1,
                pageSize: 10,
                pageSizes: [5, 10, 20, 40, 99999],
                total: 0
            }
        },
        exportData: {
            visible: false,
            src: "../management/excel/ExportData.html"
        },
        visitDate: ''
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
        getSub() {
            ajaxPost(this.urls.getSub, {param: "使用范围"}, function (result) {
                app.filters.selectionList.scope.push({'value': '', 'label': '全选'});
                result.forEach(function (r) {
                    app.filters.selectionList.scope.push({'value': r.id, 'label': r.dicValue});
                });
            });
            ajaxPost(this.urls.getSub, {param: "类型"}, function (result) {
                app.filters.selectionList.type.push({'value': '', 'label': '全选'});
                result.forEach(function (r) {
                    app.filters.selectionList.type.push({'value': r.id, 'label': r.dicValue});
                });
            });
            ajaxPost(this.urls.getSub, {param: "密级"}, function (result) {
                app.filters.selectionList.secret_level.push({'value': '', 'label': '全选'});
                result.forEach(function (r) {
                    app.filters.selectionList.secret_level.push({'value': r.id, 'label': r.dicValue});
                });
            });
            ajaxPost(this.urls.getSub, {param: "用途"}, function (result) {
                app.filters.selectionList.usage.push({'value': '', 'label': '全选'});
                result.forEach(function (r) {
                    app.filters.selectionList.usage.push({'value': r.id, 'label': r.dicValue});
                });
            });
            ajaxPost(this.urls.getSub, {param: "使用情况"}, function (result) {
                app.filters.selectionList.use_situation.push({'value': '', 'label': '全选'});
                result.forEach(function (r) {
                    app.filters.selectionList.use_situation.push({'value': r.id, 'label': r.dicValue});
                });
            });
            ajaxPost(this.urls.getSub, {param: "dept"}, function (result) {
                result.forEach(function (r) {
                    app.filters.selectionList.school.push(r);
                })
            })
        },
        getDeptSub: function (index) {
            app.filters.selectionList.subject = [];
            app.filters.subject = '';
            ajaxPost(this.urls.getDeptSub, {id: index}, function (result) {
                result.forEach(function (r) {
                    app.filters.selectionList.subject.push(r);
                });
            })
        },
        refreshTable: function (usingPrevFilter) {
            let app = this;
            app.table.loading = true;
            if (usingPrevFilter) {
                Object.assign(this.filters.condition, prevFilter);
            }
            let data = {
                page: app.table.props,
                scope: this.filters.condition.scope,
                type: this.filters.condition.type,
                usage: this.filters.condition.usage,
                secret_level: this.filters.condition.secret_level,
                use_situation: this.filters.condition.use_situation,
                department_code: this.filters.condition.school,
                subject_code: this.filters.condition.subject,
                startTime: this.filters.condition.startTime,
                endTime: this.filters.condition.endTime
            };
            ajaxPostJSON(this.urls.getList, data, function (result) {
                app.table.loading = false;
                app.table.data = result.data.resultList;
                app.table.props.total = result.data.total;
                Object.assign(prevFilter, app.filters.condition);
            })
        },
        getDialogList: function () {
            ajaxPost(this.urls.getSub, {param: "使用范围"}, function (result) {
                app.dialog.selectionList.scope = [];
                result.forEach(function (r) {
                    app.dialog.selectionList.scope.push({'value': r.id, 'label': r.dicValue});
                });
            });
            ajaxPost(this.urls.getSub, {param: "类型"}, function (result) {
                app.dialog.selectionList.type = [];
                result.forEach(function (r) {
                    app.dialog.selectionList.type.push({'value': r.id, 'label': r.dicValue});
                });
            });
            ajaxPost(this.urls.getSub, {param: "密级"}, function (result) {
                app.dialog.selectionList.secret_level = [];
                result.forEach(function (r) {
                    app.dialog.selectionList.secret_level.push({'value': r.id, 'label': r.dicValue});
                });
            });
            ajaxPost(this.urls.getSub, {param: "用途"}, function (result) {
                app.dialog.selectionList.usage = [];
                result.forEach(function (r) {
                    app.dialog.selectionList.usage.push({'value': r.id, 'label': r.dicValue});
                });
            });
            ajaxPost(this.urls.getSub, {param: "使用情况"}, function (result) {
                app.dialog.selectionList.use_situation = [];
                result.forEach(function (r) {
                    app.dialog.selectionList.use_situation.push({'value': r.id, 'label': r.dicValue});
                });
            });
            ajaxPost(this.urls.getSub, {param: "dept"}, function (result) {
                app.dialog.selectionList.department = [];
                result.forEach(function (r) {
                    app.dialog.selectionList.department.push(r);
                })
            })
        },
        handleTypeChange: function (v) {
            app.dialog.data.type = v;
        },
        handleLevelChange: function (v) {
            app.dialog.data.secret_level = v;
        },
        handleUsageChange: function (v) {
            app.dialog.data.usage = v;
        },
        handleScopeChange: function (v) {
            app.dialog.data.scope = v;
        },
        handleSituationChange: function (v) {
            app.dialog.data.use_situation = v;
        },
        handleSchoolChange: function (v) {
            app.dialog.data.department_name = v.dept_name;
            app.dialog.data.department = v.id;
            console.log("data", app.dialog.data);
            ajaxPost(this.urls.getDeptSub, {id: v.id}, function (result) {
                app.dialog.selectionList.subject = [];
                result.forEach(function (r) {
                    app.dialog.selectionList.subject.push(r);
                })
            });
            app.dialog.data.subject = '';
            app.dialog.data.subject_name = '';
            app.dialog.data.subject_code = '';
        },
        handleSubjectChange: function (v) {
            app.dialog.data.subject_name = v.dept_name;
            app.dialog.data.subject = v.id;
        },
        insertOrUpdateStorage: function () {
            app.dialog.loading = true;
            let data = {
                id: app.dialog.data.id,
                department: app.dialog.data.department,
                subject: app.dialog.data.subject,
                secret_number: app.dialog.data.secret_number,
                type: app.dialog.data.type,
                model: app.dialog.data.model,
                person: app.dialog.data.person,
                secret_level: app.dialog.data.secret_level,
                serial_number: app.dialog.data.serial_number,
                place_location: app.dialog.data.place_location,
                usage: app.dialog.data.usage,
                scope: app.dialog.data.scope,
                enablation_time: app.dialog.data.enablation_time,
                use_situation: app.dialog.data.use_situation,
                remarks: app.dialog.data.remarks,
                delFlag: 0
            };
            ajaxPostJSON(this.urls.insertOrUpdateStorage, data, function (d) {
                app.dialog.loading = false;
                app.dialog.visible = false;
                app.$message({
                    message: "操作成功",
                    type: "success"
                });
                app.resetDialogData();
                app.getList();
            }, (res) => {
                app.dialog.loading = false;
                app.dialog.visible = false;
                app.$message({
                    message: "系统出错或保密编号已存在",
                    type: "error"
                });
                app.resetDialogData();
            })
        },
        getList: function () {
            app.table.props.pageIndex = 1;
            this.refreshTable(false);
        },
        updateDialog: function (v) {
            let app = this;
            app.dialog.data.id = v["id"];
            app.dialog.data.department = v["department"];
            app.dialog.data.department_name = v["department_name"];
            app.dialog.data.subject = v["subject"];
            app.dialog.data.subject_name = v["subject_name"];
            app.dialog.data.secret_number = v["secret_number"];
            app.dialog.data.type = v["type"];
            app.dialog.data.model = v["model"];
            app.dialog.data.person = v["person"];
            app.dialog.data.secret_level = v["secret_level"];
            app.dialog.data.serial_number = v["serial_number"];
            app.dialog.data.place_location = v["place_location"];
            app.dialog.data.usage = v["usage"];
            app.dialog.data.scope = v["scope"];
            app.dialog.data.enablation_time = v["enablation_time"];
            app.dialog.data.use_situation = v["use_situation"];
            app.dialog.data.remarks = v["remarks"];
            app.dialog.data._type = v["_type"];
            app.dialog.data._secret_level = v["_secret_level"];
            app.dialog.data._usage = v["_usage"];
            app.dialog.data._scope = v["_scope"];
            app.dialog.data._use_situation = v["_use_situation"];
            ajaxPost(this.urls.getDeptSub, {id: app.dialog.data.department}, function (result) {
                app.dialog.selectionList.subject = [];
                result.forEach(function (r) {
                    app.dialog.selectionList.subject.push(r);
                })
            });
            app.getDialogList();
            app.dialog.visible = true;
        },
        resetDialogData: function () {
            app.dialog.data.id = '';
            app.dialog.data.department = '';
            app.dialog.data.department_name = '';
            app.dialog.data.subject = '';
            app.dialog.data.subject_name = '';
            app.dialog.data.secret_number = '';
            app.dialog.data.type = '';
            app.dialog.data.model = '';
            app.dialog.data.person = '';
            app.dialog.data.secret_level = '';
            app.dialog.data.serial_number = '';
            app.dialog.data.place_location = '';
            app.dialog.data.usage = '';
            app.dialog.data.scope = '';
            app.dialog.data.enablation_time = '';
            app.dialog.data.use_situation = '';
            app.dialog.data.remarks = '';
            app.dialog.data._type = '';
            app.dialog.data._secret_level = '';
            app.dialog.data._usage = '';
            app.dialog.data._scope = '';
            app.dialog.data._use_situation = '';
        },
        deleteByIds: function (list) {
            if (list.length === 0) {
                app.$message({
                    message: "未选中任何项",
                    type: "danger"
                });
                return;
            }
            app.$confirm('确认删除选中的项', '警告', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                let data = list;
                let app = this;
                app.table.loading = true;
                ajaxPostJSON(app.urls.deleteListByIds, data, function (d) {
                    app.$message({
                        message: "删除成功",
                        type: "success"
                    });
                    app.table.props.pageIndex = 1;
                    app.refreshTable(true);
                })
            }).catch(() => {
                app.$message({
                    message: "取消删除",
                    type: "danger"
                });
            });
        },
        onSelectionChange: function (val) {
            this.table.selectionList = val;
        },
        onPageSizeChange: function (newSize) {
            this.table.props.pageSize = newSize;
            this.refreshTable(true);
        },
        onPageIndexChange: function (newIndex) {
            this.table.props.pageIndex = newIndex;
            this.refreshTable(true);
        },
        scrap: function () {
            app.scrapDialog.loading = true;
            if (app.scrapDialog.data.scrap_time === "" || app.scrapDialog.data.scrap_time == null) {
                app.$message({
                    type: "error",
                    message: "未选择报废时间"
                });
                app.scrapDialog.loading = false;
                return;
            }
            let data = {
                id: app.scrapDialog.data.id,
                scrap_time: app.scrapDialog.data.scrap_time,
                remarks: app.scrapDialog.data.remarks
            };
            ajaxPost(app.urls.scrap, data, function (result) {
                app.$message({
                    message: "报废成功",
                    type: "success"
                });
                app.table.props.pageIndex = 1;
                app.refreshTable(true);
                app.scrapDialog.loading = false;
                app.scrapDialog.visible = false;
            });

        },
        showScrapDialog: function (v) {
            app.scrapDialog.data.id = v["id"];
            app.scrapDialog.data.scrap_time = '';
            app.scrapDialog.data.remarks = '';
            app.scrapDialog.visible = true;
        },
        resetScrapDialog: function () {
            app.scrapDialog.data.id = '';
            app.scrapDialog.data.scrap_time = '';
            app.scrapDialog.data.remarks = '';
        }
    },
    mounted: function () {
        this.getSub();
        this.refreshTable(false);
    },
    computed: {
        isInsertStorageDisable: function () {
            let app = this;
            return !(
                app.dialog.data.department_name
                && app.dialog.data.subject_name
                && app.dialog.data.secret_number
                && app.dialog.data.type
                && app.dialog.data.model
                && app.dialog.data.person
                && app.dialog.data.secret_level
                && app.dialog.data.serial_number
                && app.dialog.data.place_location
                && app.dialog.data.usage
                && app.dialog.data.scope
                && app.dialog.data.enablation_time
                && app.dialog.data.use_situation
            )
        }
    }
});

function getExportConditions() {
    let ID = [];
    app.table.selectionList.forEach(function (v) {
        ID.push(v["id"]);
    });
    let data = {
        fileName: "涉密存储介质",
        templateId: "dd149cedfd1547ff86c450071339d71d",
        fieldList: [
            {
                fieldName: "单位",
                fieldType: "department"
            }, {
                fieldName: "科室/课题组",
                fieldType: "subject"
            }, {
                fieldName: "保密编号",
                fieldType: "secret_number"
            }, {
                fieldName: "类型",
                fieldType: "type",
            }, {
                fieldName: "型号",
                fieldType: "model"
            }, {
                fieldName: "责任人",
                fieldType: "person"
            }, {
                fieldName: "密级",
                fieldType: "secret_level"
            }, {
                fieldName: "序列号",
                fieldType: "serial_number"
            }, {
                fieldName: "放置地点",
                fieldType: "place_location"
            }, {
                fieldName: "用途",
                fieldType: "usage"
            }, {
                fieldName: "使用范围",
                fieldType: "scope"
            }, {
                fieldName: "启用时间",
                fieldType: "enablation_time"
            }, {
                fieldName: "使用情况",
                fieldType: "use_situation"
            }, {
                fieldName: "备注",
                fieldType: "remarks"
            }
        ],
        conditionsList: [],
        idList: ID,
        isScrapped: false,
        tableName: "confidential_storage_device"
    };
    return data;
}