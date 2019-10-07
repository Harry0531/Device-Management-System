let defaultFiltersCondition = {
    use_situation: '',
    type: '',
    device_name: '',
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
        number: '',
        asset_number: '',
        type: '',
        device_name: '',
        person: '',
        model: '',
        device_number: '',
        disk_number: '',
        secret_level:'c60de690d93c4e18b369e1adff7dcb84',
        usage:'51cbcfd540e847e2b2e35b5cdbbeaa22',
        place_location: '',
        enablation_time: '',
        use_situation: '',
        remarks: '',
        _type: '',
        _device_name: '',
        _use_situation: '',
    },
    selectionList: {
        type: [],
        device_name: [],
        use_situation: [],
        subject: [],
        department: [],
        secret_level:[{value:'c60de690d93c4e18b369e1adff7dcb84',label:'非密'}],
        usage:[{value:'51cbcfd540e847e2b2e35b5cdbbeaa22',label:'办公/科研'}]
    }
};

let app = new Vue({
    el: '#app',
    data: {
        showWindow: false,
        loading: false,
        urls: {
            getSub: 'http://localhost:8444/api/sys/info/nonConfidential/getSub',
            getDeviceName: 'http://localhost:8444/api/sys/info/nonConfidential/getDeviceName',
            getDeptSub: 'http://localhost:8444/api/sys/info/nonConfidential/getDeptSub',
            getList: 'http://localhost:8444/api/sys/info/nonConfidential/getList',
            insertOrUpdateInfo: 'http://localhost:8444/api/sys/info/nonConfidential/insertOrUpdateInfo',
            deleteListByIds: 'http://localhost:8444/api/sys/info/nonConfidential/deleteListByIds',
            deleteAll: 'http://localhost:8444/api/sys/info/nonConfidential/deleteAll'
        },
        activeNames: [],
        filters: {
            selectionList: {
                use_situation: [],
                type: [],
                school: [],
                subject: [],
                device_name: []
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
                pageSizes: [5, 10, 20, 40, 99999],
                total: 0
            }
        },
        exportData: {
            visible: false,
            src: "../management/excel/ExportData.html"
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
        getSub() {
            ajaxPost(this.urls.getSub, {param: "设备名称"}, function (result) {
                app.filters.selectionList.device_name.push({'value': '', 'label': '全选'});
                result.forEach(function (r) {
                    app.filters.selectionList.device_name.push({'value': r.id, 'label': r.dicValue});
                });
            });
            ajaxPost(this.urls.getSub, {param: "类型"}, function (result) {
                app.filters.selectionList.type.push({'value': '', 'label': '全选'});
                result.forEach(function (r) {
                    app.filters.selectionList.type.push({'value': r.id, 'label': r.dicValue});
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
        refreshTable: function () {
            let app = this;
            app.table.loading = true;
            let data = {
                page: app.table.props,
                device_name: this.filters.condition.device_name,
                type: this.filters.condition.type,
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
            })
        },
        getDialogList: function () {
            ajaxPost(this.urls.getSub, {param: "类型"}, function (result) {
                app.dialog.selectionList.type = [];
                result.forEach(function (r) {
                    app.dialog.selectionList.type.push({'value': r.id, 'label': r.dicValue});
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
            ajaxPost(this.urls.getDeviceName, {type: v}, function (result) {
                app.dialog.selectionList.device_name = [];
                result.forEach(function (r) {
                    app.dialog.selectionList.device_name.push({'value': r.id, 'label': r.dicValue});
                });
            });
            app.dialog.data.device_name = '';
        },
        handleDeviceChange: function (v) {
            app.dialog.data.device_name = v;
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
        insertOrUpdateInfo: function () {
            app.dialog.loading = true;
            let data = {
                id: app.dialog.data.id,
                department: app.dialog.data.department,
                subject: app.dialog.data.subject,
                number: app.dialog.data.number,
                asset_number: app.dialog.data.asset_number,
                type: app.dialog.data.type,
                device_name: app.dialog.data.device_name,
                person: app.dialog.data.person,
                secret_level: 'c60de690d93c4e18b369e1adff7dcb84',
                model: app.dialog.data.model,
                device_number: app.dialog.data.device_number,
                disk_number: app.dialog.data.disk_number,
                usage: '51cbcfd540e847e2b2e35b5cdbbeaa22',
                place_location: app.dialog.data.place_location,
                use_situation: app.dialog.data.use_situation,
                enablation_time: app.dialog.data.enablation_time,
                remarks: app.dialog.data.remarks,
                delFlag: 0
            };
            ajaxPostJSON(this.urls.insertOrUpdateInfo, data, function (d) {
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
            app.dialog.data.id = v["id"];
            app.dialog.data.department = v["department"];
            app.dialog.data.department_name = v["department_name"];
            app.dialog.data.subject = v["subject"];
            app.dialog.data.subject_name = v["subject_name"];
            app.dialog.data.number = v["number"];
            app.dialog.data.asset_number = v["asset_number"];
            app.dialog.data.type = v["type"];
            app.dialog.data.device_name = v["device_name"];
            app.dialog.data.person = v["person"];
            app.dialog.data.secret_level = v["secret_level"];
            app.dialog.data.model = v["model"];
            app.dialog.data.device_number = v["device_number"];
            app.dialog.data.disk_number = v["disk_number"];
            app.dialog.data.place_location = v["place_location"];
            app.dialog.data.use_situation = v["use_situation"];
            app.dialog.data.enablation_time = v["enablation_time"];
            app.dialog.data.remarks = v["remarks"];
            app.dialog.data._type = v["_type"];
            app.dialog.data._secret_level = v["_secret_level"];
            app.dialog.data._device_name = v["_device_name"];
            app.dialog.data._use_situation = v["_use_situation"];
            ajaxPost(this.urls.getDeptSub, {id: app.dialog.data.department}, function (result) {
                app.dialog.selectionList.subject = [];
                result.forEach(function (r) {
                    app.dialog.selectionList.subject.push(r);
                })
            });
            ajaxPost(this.urls.getDeviceName, {type: app.dialog.data.type}, function (result) {
                app.dialog.selectionList.device_name = [];
                result.forEach(function (r) {
                    app.dialog.selectionList.device_name.push({'value': r.id, 'label': r.dicValue});
                });
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
            app.dialog.data.number = '';
            app.dialog.data.asset_number = '';
            app.dialog.data.type = '';
            app.dialog.data.device_name = '';
            app.dialog.data.person = '';
            app.dialog.data.model = '';
            app.dialog.data.device_number = '';
            app.dialog.data.disk_number = '';
            app.dialog.data.place_location = '';
            app.dialog.data.use_situation = '';
            app.dialog.data.enablation_time = '';
            app.dialog.data.remarks = '';
            app.dialog.data._type = '';
            app.dialog.data._secret_level = '';
            app.dialog.data._device_name = '';
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
    },
    mounted: function () {
        this.getSub();
        this.refreshTable();
    },
    computed: {
        isInsertInfoDisable: function () {
            let app = this;
            return !(
                app.dialog.data.department_name
                && app.dialog.data.subject_name
                && app.dialog.data.number
                && app.dialog.data.asset_number
                && app.dialog.data.type
                && app.dialog.data.device_name
                && app.dialog.data.person
                && app.dialog.data.model
                && app.dialog.data.device_number
                && app.dialog.data.disk_number
                && app.dialog.data.place_location
                && app.dialog.data.use_situation
                && app.dialog.data.enablation_time
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
        fileName: "涉密信息设备",
        templateId: "ab81d835f0b146d98b4f5e06e0f651c0",//todo 编号！！！
        fieldList: [
            {
                fieldName: "单位",
                fieldType: "department"
            }, {
                fieldName: "科室/课题组",
                fieldType: "subject"
            }, {
                fieldName: "编号",
                fieldType: "number"
            }, {
                fieldName: "固定资产编号",
                fieldType: "asset_number",
            }, {
                fieldName: "类型",
                fieldType: "type",
            }, {
                fieldName: "设备名称",
                fieldType: "device_name",
            }, {
                fieldName: "责任人",
                fieldType: "person"
            }, {
                fieldName: "密级",
                fieldType: "secret_level"
            }, {
                fieldName: "产品型号",
                fieldType: "model"
            }, {
                fieldName: "设备序列号",
                fieldType: "device_number"
            }, {
                fieldName: "硬盘序列号",
                fieldType: "disk_number"
            }, {
                fieldName: "用途",
                fieldType: "usage"
            }, {
                fieldName: "放置地点",
                fieldType: "place_location"
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
        tableName: "non_confidential_information_device"
    };
    return data;
}