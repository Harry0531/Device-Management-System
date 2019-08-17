let defaultFiltersCondition = {
    scope: [],
    use_situation: [],
    usage: [],
    secret_level: [],
    type: [],
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
        department_name: '',
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
        department_code: '',
        subject_code: ''
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

let app = new Vue({
    el: '#app',
    data: {
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
            }
        },

    },
    methods: {
        getSub() {
            ajaxPost(this.urls.getSub, {param: "scope"}, function (result) {
                result.forEach(function (r) {
                    app.filters.selectionList.scope.push({'value': r.id, 'label': r.dicValue});
                });
            });
            ajaxPost(this.urls.getSub, {param: "type"}, function (result) {
                result.forEach(function (r) {
                    app.filters.selectionList.type.push({'value': r.id, 'label': r.dicValue});
                });
            });
            ajaxPost(this.urls.getSub, {param: "secret_level"}, function (result) {
                result.forEach(function (r) {
                    app.filters.selectionList.secret_level.push({'value': r.id, 'label': r.dicValue});
                });
            });
            ajaxPost(this.urls.getSub, {param: "usage"}, function (result) {
                result.forEach(function (r) {
                    app.filters.selectionList.usage.push({'value': r.id, 'label': r.dicValue});
                });
            });
            ajaxPost(this.urls.getSub, {param: "use_situation"}, function (result) {
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
        refreshTable: function () {
            let app = this;
            app.table.loading = true;
            let data = {
                page: app.table.props,
                scope: this.filters.condition.scope[0],
                type: this.filters.condition.type[0],
                usage: this.filters.condition.usage[0],
                secret_level: this.filters.condition.secret_level[0],
                use_situation: this.filters.condition.use_situation[0],
                department_code: this.filters.condition.school,
                subject_code: this.filters.condition.subject,
                startTime: this.filters.condition.startTime,
                endTime: this.filters.condition.endTime
            };
            ajaxPostJSON(this.urls.getList, data, function (result) {
                app.table.loading = false;
                app.table.data = result.data.resultList;
                app.table.props.total = result.data.total;
            })
        },
        getDialogList: function () {
            ajaxPost(this.urls.getSub, {param: "scope"}, function (result) {
                app.dialog.selectionList.scope = [];
                result.forEach(function (r) {
                    app.dialog.selectionList.scope.push({'value': r.id, 'label': r.dicValue});
                });
            });
            ajaxPost(this.urls.getSub, {param: "type"}, function (result) {
                app.dialog.selectionList.type = [];
                result.forEach(function (r) {
                    app.dialog.selectionList.type.push({'value': r.id, 'label': r.dicValue});
                });
            });
            ajaxPost(this.urls.getSub, {param: "secret_level"}, function (result) {
                app.dialog.selectionList.secret_level = [];
                result.forEach(function (r) {
                    app.dialog.selectionList.secret_level.push({'value': r.id, 'label': r.dicValue});
                });
            });
            ajaxPost(this.urls.getSub, {param: "usage"}, function (result) {
                app.dialog.selectionList.usage = [];
                result.forEach(function (r) {
                    app.dialog.selectionList.usage.push({'value': r.id, 'label': r.dicValue});
                });
            });
            ajaxPost(this.urls.getSub, {param: "use_situation"}, function (result) {
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
            app.dialog.data.department_code = v.id;
            console.log("data", app.dialog.data);
            ajaxPost(this.urls.getDeptSub, {id: v.id}, function (result) {
                app.dialog.selectionList.subject = [];
                result.forEach(function (r) {
                    app.dialog.selectionList.subject.push(r);
                })
            });
            app.dialog.data.subject_name = '';
            app.dialog.data.subject_code = '';
        },
        handleSubjectChange: function (v) {
            app.dialog.data.subject_name = v.dept_name;
            app.dialog.data.subject_code = v.id;
        },
        insertOrUpdateStorage: function () {
            app.dialog.loading = true;
            let data = {
                id: app.dialog.data.id,
                department_name: app.dialog.data.department_name,
                department_code: app.dialog.data.department_code,
                subject_name: app.dialog.data.subject_name,
                subject_code: app.dialog.data.subject_code,
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
            })
        },
        getList: function () {
            app.table.props.pageIndex = 1;
            this.refreshTable();
        },
        updateDialog: function (v) {
            let app = this;
            app.getDialogList();
            app.dialog.data.id = v["id"];
            app.dialog.data.department_name = v["department_name"];
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
            app.dialog.data.department_code = v["department_code"];
            app.dialog.data.subject_code = v["subject_code"];
            app.dialog.visible = true;
        },
        resetDialogData: function () {
            app.dialog = defaultDialog;
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
                    app.refreshTable();
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
            this.refreshTable();
        },
        onPageIndexChange: function (newIndex) {
            this.table.props.pageIndex = newIndex;
            this.refreshTable();
        },
        scrap: function (v) {
            console.log(v);
            app.$confirm('确认报废', '警告', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                let data = {
                    id: v["id"],
                    department_name: v["department_name"],
                    subject_name: v["subject_name"],
                    secret_number: v["secret_number"],
                    type: v["type"],
                    model: v["model"],
                    person: v["person"],
                    secret_level: v["secret_level"],
                    serial_number: v["serial_number"],
                    place_location: v["place_location"],
                    usage: v["usage"],
                    scope: v["scope"],
                    enablation_time: v["enablation_time"],
                    use_situation: v["use_situation"],
                    remarks: v["remarks"],
                    department_code: v["department_code"],
                    subject_code: v["subject_code"],
                    delFlag: v["delFlag"]
                };
                ajaxPostJSON(app.urls.scrap, data, function (result) {
                    app.$message({
                        message: "报废成功",
                        type: "success"
                    });
                    app.table.props.pageIndex = 1;
                    app.refreshTable();
                });
            }).catch(() => {
                app.$message({
                    message: "取消操作",
                    type: "danger"
                });
            });
        }
    },
    mounted: function () {
        this.getSub();
        this.refreshTable();
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