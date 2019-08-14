//数量统计：允许分别按单位、类型、密级、用途、使用范围、使用情况等统计数量
let select1 = [
    {value: 'dept', label: '单位（暂不可用）', disabled: true},//todo 待单位表确定
    {value: '存储介质类型', label: '类型'},
    {value: '存储介质密级', label: '密级'},
    {value: '存储介质用途', label: '用途'},
    {value: '存储介质使用范围', label: '使用范围'},
    {value: '存储介质使用情况', label: '使用情况'}
];

let app = new Vue({
    el: '#app',
    data: {
        urls: {
            getSub: 'http://localhost:8444/api/sys/storage/getSub',
            getList: 'http://localhost:8444/api/sys/storage/getList',
            insertOrUpdateStorage: 'http://localhost:8444/api/sys/storage/insertOrUpdateStorage',
            deleteListByIds: 'http://localhost:8444/api/sys/storage/deleteListByIds',
            deleteAll: 'http://localhost:8444/api/sys/storage/deleteAll'
        },
        select1: select1,
        select2: [],
        loading: false,
        filters: {
            value1: '',
            value2: ''
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
            type: ''
        },
        dialog: {
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
            },
            selectionList: {
                type: [],
                secret_level: [],
                usage: [],
                scope: [],
                use_situation: []
            }
        }
    },
    methods: {
        getSub(prov) {
            app.select2 = [];
            app.filters.value2 = '';
            let data = {
                param: prov
            };
            console.log(prov);
            ajaxPost(this.urls.getSub, data, function (result) {
                result.forEach(function (r) {
                    app.select2.push({'value': r.id, 'label': r.dicValue});
                });
            })
        },
        refreshTable: function () {
            let app = this;
            app.table.loading = true;
            let data = {
                page: app.table.props,
                type: app.table.type
            };
            ajaxPostJSON(this.urls.getList, data, function (result) {
                app.table.loading = false;
                app.table.data = result.data.resultList;
                app.table.props.total = result.data.total;
            })
        },
        getDialogList: function () {
            ajaxPost(this.urls.getSub, {param: "存储介质使用范围"}, function (result) {
                app.dialog.selectionList.scope = [];
                result.forEach(function (r) {
                    app.dialog.selectionList.scope.push({'value': r.id, 'label': r.dicValue});
                });
            });
            ajaxPost(this.urls.getSub, {param: "存储介质类型"}, function (result) {
                app.dialog.selectionList.type = [];
                result.forEach(function (r) {
                    app.dialog.selectionList.type.push({'value': r.id, 'label': r.dicValue});
                });
            });
            ajaxPost(this.urls.getSub, {param: "存储介质密级"}, function (result) {
                app.dialog.selectionList.secret_level = [];
                result.forEach(function (r) {
                    app.dialog.selectionList.secret_level.push({'value': r.id, 'label': r.dicValue});
                });
            });
            ajaxPost(this.urls.getSub, {param: "存储介质用途"}, function (result) {
                app.dialog.selectionList.usage = [];
                result.forEach(function (r) {
                    app.dialog.selectionList.usage.push({'value': r.id, 'label': r.dicValue});
                });
            });
            ajaxPost(this.urls.getSub, {param: "存储介质使用情况"}, function (result) {
                app.dialog.selectionList.use_situation = [];
                result.forEach(function (r) {
                    app.dialog.selectionList.use_situation.push({'value': r.id, 'label': r.dicValue});
                });
            });
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
        insertOrUpdateStorage: function () {
            app.dialog.loading = true;
            let data = {
                id: app.dialog.data.id,
                department_name: app.dialog.data.department_name,
                subject_name: app.dialog.data.subject_name,
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
                remarks: app.dialog.data.remarks
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
                app.filters.value1='';
                app.filters.value2='';
            })
        },
        getList: function (index) {
            this.table.type = index;
            this.refreshTable();
        },
        updateDialog: function (v) {
            let app = this;
            app.getDialogList();
            console.log("v", v);
            app.dialog.data.id = v["id"];
            console.log("id", app.dialog.data.id);
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
            console.log("*********");
            console.log(app.dialog.data);
            app.dialog.visible = true;
        },
        resetDialogData: function () {
            app.dialog = {
                visible: false,
                loading: false,
                data: {
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
                    remarks: ''
                },
                selectionList: {
                    type: [],
                    secret_level: [],
                    usage: [],
                    scope: [],
                    use_situation: []
                }
            }
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
        }
        ,
        onPageIndexChange: function (newIndex) {
            this.table.props.pageIndex = newIndex;
            this.refreshTable();
        }
    },
    mounted: function () {
        this.refreshTable();
    },
    computed: {
        isInsertStorageDisable: function () {
            let app = this;
            return !(
                app.dialog.data.department_name &&
                app.dialog.data.subject_name &&
                app.dialog.data.secret_number &&
                app.dialog.data.type &&
                app.dialog.data.model &&
                app.dialog.data.person &&
                app.dialog.data.secret_level &&
                app.dialog.data.serial_number &&
                app.dialog.data.place_location &&
                app.dialog.data.usage &&
                app.dialog.data.scope &&
                app.dialog.data.enablation_time &&
                app.dialog.data.use_situation
            )
        }
    }
});