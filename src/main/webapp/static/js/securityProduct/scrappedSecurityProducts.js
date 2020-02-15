let defaultFiltersCondition = {
    use_situation: '',
    scope: '',
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
        asset_number: '',
        type: '',
        product_version: '',
        manufacturer: '',
        certificate_number: '',
        certificate_validity: '',
        serial_number: '',
        secret_level: 'a434016d651e4f29a176f271fa4f04e1',
        person: '',
        place_location: '',
        scope: '',
        buy_time: '',
        enablation_time: '',
        use_situation: '756dc2b343ee42ab9a20d34eebc38c5d',
        scrap_time: '',
        remarks: '',
        _type: '',
        _secret_level: '涉密专用',
        _scope: '',
        _use_situation: '报废',
    },
    selectionList: {
        type: [],
        secret_level: [],
        scope: [],
        use_situation: [{value: '756dc2b343ee42ab9a20d34eebc38c5d', label: '报废'}],
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
    use_situation: '',
    scope: '',
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
        urls: {
            getSub: 'http://localhost:8444/api/sys/product/scrapped/getSub',
            getDeptSub: 'http://localhost:8444/api/sys/product/scrapped/getDeptSub',
            getList: 'http://localhost:8444/api/sys/product/scrapped/getList',
            deleteListByIds: 'http://localhost:8444/api/sys/product/scrapped/deleteListByIds',
            scrap: 'http://localhost:8444/api/sys/product/scrapped/scrap',
            insertOrUpdateProduct: 'http://localhost:8444/api/sys/product/product/insertOrUpdateProduct',
        },
        loading: false,
        activeNames: [],
        filters: {
            selectionList: {
                use_situation: [],
                scope: [],
                secret_level: [],
                type: [],
                school: [],
                subject: []
            },
            condition: defaultFiltersCondition
        },
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
        dialog: defaultDialog,
        scrapDialog: defaultScrapDialog,
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
        handleScopeChange: function (v) {
            app.dialog.data.scope = v;
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
        insertOrUpdateProduct: function () {
            app.dialog.loading = true;
            let data = {
                id: app.dialog.data.id,
                department: app.dialog.data.department,
                subject: app.dialog.data.subject,
                secret_number: app.dialog.data.secret_number,
                asset_number: app.dialog.data.asset_number,
                type: app.dialog.data.type,
                product_version: app.dialog.data.product_version,
                manufacturer: app.dialog.data.manufacturer,
                certificate_number: app.dialog.data.certificate_number,
                certificate_validity: app.dialog.data.certificate_validity,
                serial_number: app.dialog.data.serial_number,
                secret_level: app.dialog.data.secret_level,
                person: app.dialog.data.person,
                place_location: app.dialog.data.place_location,
                scope: app.dialog.data.scope,
                buy_time: app.dialog.data.buy_time,
                enablation_time: app.dialog.data.enablation_time,
                use_situation: app.dialog.data.use_situation,
                scrap_time: app.dialog.data.scrap_time,
                remarks: app.dialog.data.remarks,
                scrapped_flag: 1,
                delFlag: 0
            };
            ajaxPostJSON(this.urls.insertOrUpdateProduct, data, function (d) {
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
        getList: function (index) {
            app.table.props.pageIndex = 1;
            this.refreshTable(false);
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
        resetDialogData: function () {
            app.dialog.data.id = '';
            app.dialog.data.department = '';
            app.dialog.data.department_name = '';
            app.dialog.data.subject = '';
            app.dialog.data.subject_name = '';
            app.dialog.data.secret_number = '';
            app.dialog.data.asset_number = '';
            app.dialog.data.type = '';
            app.dialog.data.product_version = '';
            app.dialog.data.manufacturer = '';
            app.dialog.data.certificate_number = '';
            app.dialog.data.certificate_validity = '';
            app.dialog.data.serial_number = '';
            app.dialog.data.person = '';
            app.dialog.data.place_location = '';
            app.dialog.data.scope = '';
            app.dialog.data.buy_time = '';
            app.dialog.data.enablation_time = '';
            app.dialog.data.remarks = '';
            app.dialog.data._type = '';
            app.dialog.data._scope = '';
            app.dialog.data.scrap_time = '';
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
        showDialog: function (v) {
            let app = this;
            app.dialog.data.id = v["id"];
            app.dialog.data.remarks = v["remarks"];
            app.dialog.data.scrap_time = v["scrap_time"];
            app.dialog.visible = true;
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
            app.scrapDialog.data.scrap_time = v["scrap_time"];
            app.scrapDialog.data.remarks = v["remarks"];
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
        visitDate: function () {
            let nowDate = new Date();
            let year = nowDate.getFullYear();
            let month = nowDate.getMonth() + 1;
            let day = nowDate.getDate();
            return year + "-" + month + "-" + day;
        },
        isInsertProductDisable: function () {
            let app = this;
            return !(
                app.dialog.data.department_name
                && app.dialog.data.subject_name
                && app.dialog.data.secret_number
                && app.dialog.data.asset_number
                && app.dialog.data.type
                && app.dialog.data.product_version
                && app.dialog.data.manufacturer
                && app.dialog.data.certificate_validity
                && app.dialog.data.certificate_number
                && app.dialog.data.person
                && app.dialog.data.secret_level
                && app.dialog.data.serial_number
                && app.dialog.data.place_location
                && app.dialog.data.scope
                && app.dialog.data.buy_time
                && app.dialog.data.enablation_time
                && app.dialog.data.use_situation
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
    let timeList = [];
    timeList.push(app.filters.condition.startTime, app.filters.condition.endTime);
    let data = {
        fileName: "报废安全保密产品",
        templateId: "f40edb34f32343e5b7488beb5498d8a1",
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
                fieldName: "固定资产编号",
                fieldType: "asset_number"
            }, {
                fieldName: "类型",
                fieldType: "type",
            }, {
                fieldName: "产品名称及版本号",
                fieldType: "product_version"
            }, {
                fieldName: "生产厂家",
                fieldType: "manufacturer"
            }, {
                fieldName: "检测证书编号",
                fieldType: "certificate_number"
            }, {
                fieldName: "检测证书有效期",
                fieldType: "certificate_validity"
            }, {
                fieldName: "序列号",
                fieldType: "serial_number"
            }, {
                fieldName: "密级",
                fieldType: "secret_level"
            }, {
                fieldName: "责任人",
                fieldType: "person"
            }, {
                fieldName: "放置地点",
                fieldType: "place_location"
            }, {
                fieldName: "使用范围",
                fieldType: "scope"
            }, {
                fieldName: "购置时间",
                fieldType: "buy_time"
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
        conditionsList: timeList,
        idList: ID,
        isScrapped: true,
        tableName: "security_products"
    };
    return data;
}