let defaultFiltersCondition = {
    use_situation: 'f84169416c2f43de9ca5f17e90396379',
    usage: '',
    secret_level: '',
    type: '',
    school: '',
    subject: '',
    startTime: '',
    endTime: '',
    cd_drive: '',
    os_version: '',
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
        enablation_time: '',
        use_situation: 'f84169416c2f43de9ca5f17e90396379',
        remarks: '',
        _type: '',
        _secret_level: '',
        _usage: '',
        _scope: '',
        _use_situation: '报废',
        asset_number: '',
        os_version: ``,
        os_install_time: ``,
        mac_address: ``,
        cd_drive: '',
        _cd_drive: '',
        _os_version: '',
        scrap_time: ''
    },
    selectionList: {
        type: [],
        secret_level: [],
        usage: [],
        use_situation: [{'value': 'f84169416c2f43de9ca5f17e90396379', 'label': '报废'}],
        subject: [],
        department: [],
        os_version: [],
        cd_drive: []
    },
    exportData: {
        visible: false,
        src: "../management/excel/ExportData.html"
    }
};

let app = new Vue({
    el: '#app',
    data: {
        showWindow: false,
        loading: false,
        urls: {
            getSub: 'http://localhost:8444//api/computer/scrapped/getSub',
            getDeptSub: 'http://localhost:8444/api/computer/scrapped/getDeptSub',
            getList: 'http://localhost:8444/api/computer/scrapped/getList',
            insertOrUpdateComputer: 'http://localhost:8444/api/computer/scrapped/insertOrUpdateComputer',
            deleteListByIds: 'http://localhost:8444/api/computer/scrapped/deleteListByIds',
            deleteAll: 'http://localhost:8444/api/computer/scrapped/deleteAll',
            scrap: 'http://localhost:8444/api/computer/scrapped/scrap'
        },
        activeNames: [],
        filters: {
            selectionList: {
                use_situation: [],
                usage: [],
                secret_level: [],
                type: [],
                school: [],
                subject: [],
                os_version: [],
                cd_drive: []
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
            ajaxPost(this.urls.getSub, {param: "光驱"}, function (result) {
                app.filters.selectionList.cd_drive.push({'value': '', 'label': '全选'});
                result.forEach(function (r) {
                    app.filters.selectionList.cd_drive.push({'value': r.id, 'label': r.dicValue});
                });
            });
            ajaxPost(this.urls.getSub, {param: "操作系统版本"}, function (result) {
                app.filters.selectionList.os_version.push({'value': '', 'label': '全选'});
                result.forEach(function (r) {
                    app.filters.selectionList.os_version.push({'value': r.id, 'label': r.dicValue});
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
                type: this.filters.condition.type,
                usage: this.filters.condition.usage,
                secret_level: this.filters.condition.secret_level,
                use_situation: this.filters.condition.use_situation,
                department_code: this.filters.condition.school,
                subject_code: this.filters.condition.subject,
                startTime: this.filters.condition.startTime,
                endTime: this.filters.condition.endTime,
                os_version: this.filters.condition.os_version,
                cd_drive: this.filters.condition.cd_drive,
                searchKey: this.filters.condition.searchKey
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

            ajaxPost(this.urls.getSub, {param: "光驱"}, function (result) {
                app.dialog.selectionList.cd_drive = [];
                result.forEach(function (r) {
                    app.dialog.selectionList.cd_drive.push({'value': r.id, 'label': r.dicValue});
                });
            });
            ajaxPost(this.urls.getSub, {param: "操作系统版本"}, function (result) {
                app.dialog.selectionList.os_version = [];
                result.forEach(function (r) {
                    app.dialog.selectionList.os_version.push({'value': r.id, 'label': r.dicValue});
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
        handleSituationChange: function (v) {
            app.dialog.data.use_situation = v;
        },
        handleOsVersionChange: function (v) {
            app.dialog.data.os_version = v;
        },
        handleCdDriveChange: function (v) {
            app.dialog.data.cd_drive = v;
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
                enablation_time: app.dialog.data.enablation_time,
                use_situation: app.dialog.data.use_situation,
                remarks: app.dialog.data.remarks,
                delFlag: 0,
                asset_number: app.dialog.data.asset_number,
                os_version: app.dialog.data.os_version,
                os_install_time: app.dialog.data.os_install_time,
                mac_address: app.dialog.data.mac_address,
                cd_drive: app.dialog.data.cd_drive,
                scrap_time: app.dialog.data.scrap_time
            };
            console.log("insert", data);
            ajaxPostJSON(this.urls.insertOrUpdateComputer, data, function (d) {
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
            app.dialog.data.secret_number = v["secret_number"];
            app.dialog.data.type = v["type"];
            app.dialog.data.model = v["model"];
            app.dialog.data.person = v["person"];
            app.dialog.data.secret_level = v["secret_level"];
            app.dialog.data.serial_number = v["serial_number"];
            app.dialog.data.place_location = v["place_location"];
            app.dialog.data.usage = v["usage"];
            app.dialog.data.enablation_time = v["enablation_time"];
            app.dialog.data.use_situation = v["use_situation"];
            app.dialog.data.remarks = v["remarks"];
            app.dialog.data._type = v["_type"];
            app.dialog.data._secret_level = v["_secret_level"];
            app.dialog.data._usage = v["_scope"];
            app.dialog.data._use_situation = v["_use_situation"];
            app.dialog.data.asset_number = v["asset_number"];
            app.dialog.data.os_version = v["os_version"];
            app.dialog.data.os_install_time = v["os_install_time"];
            app.dialog.data.mac_address = v["mac_address"];
            app.dialog.data.cd_drive = v["cd_drive"];
            app.dialog.data._cd_drive = v["_cd_drive"];
            app.dialog.data._os_version = v["_os_version"];
            app.dialog.data.scrap_time = v["scrap_time"]
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
            app.dialog.data.enablation_time = '';
            app.dialog.data.use_situation = '';
            app.dialog.data.remarks = '';
            app.dialog.data._type = '';
            app.dialog.data._secret_level = '';
            app.dialog.data._usage = '';
            app.dialog.data._use_situation = '';
            app.dialog.data.asset_number = '';
            app.dialog.data.os_version = '';
            app.dialog.data.os_install_time = '';
            app.dialog.data.mac_address = '';
            app.dialog.data.cd_drive = '';
            app.dialog.data._cd_drive = '';
            app.dialog.data._os_version = '';
            app.dialog.data.scrap_time = ''
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
                && app.dialog.data.enablation_time
                && app.dialog.data.use_situation
                && app.dialog.data.asset_number
                && app.dialog.data.os_version
                && app.dialog.data.os_install_time
                && app.dialog.data.mac_address
                && app.dialog.data.cd_drive
                && app.dialog.data.scrap_time
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
        fileName: "报废计算机",
        templateId: "e5aba220d7e54016bb82d901ba6be78d",
        fieldList: [
            {
                fieldName: "单位",
                fieldType: "department"
            }, {
                fieldName: "科室/课题组",
                fieldType: "subject"
            }, {
                fieldName: "类型",
                fieldType: "type",
            }, {
                fieldName: "保密编号",
                fieldType: "secret_number"
            }, {
                fieldName: "固定资产编号",
                fieldType: "asset_number"
            }, {
                fieldName: "负责人",
                fieldType: "person"
            }, {
                fieldName: "密级",
                fieldType: "secret_level"
            }, {
                fieldName: "品牌型号",
                fieldType: "model"
            }, {
                fieldName: "操作系统版本",
                fieldType: "os_version"
            }, {
                fieldName: "操作系统安装时间",
                fieldType: "os_install_time"
            }, {
                fieldName: "硬盘序列号",
                fieldType: "serial_number"
            }, {
                fieldName: "mac地址",
                fieldType: "mac_address"
            }, {
                fieldName: "光驱",
                fieldType: "cd_drive"
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
                fieldName: "报废时间",
                fieldType: "scrap_time"
            }, {
                fieldName: "备注",
                fieldType: "remarks"
            }
        ],
        conditionsList: [],
        idList: ID,
        isScrapped: true,
        tableName: "confidential_computer"
    }
    return data;
}