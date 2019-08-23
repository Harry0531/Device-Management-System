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
        remarks: '',
        scrap_time: ''
    }
};

let app = new Vue({
    el: '#app',
    data: {
        urls: {
            getSub: 'http://localhost:8444/api/sys/product/scrapped/getSub',
            getDeptSub: 'http://localhost:8444/api/sys/product/scrapped/getDeptSub',
            getList: 'http://localhost:8444/api/sys/product/scrapped/getList',
            scrap: 'http://localhost:8444/api/sys/product/scrapped/scrap'
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
        dialog: defaultDialog
    },
    methods: {
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
        refreshTable: function () {
            let app = this;
            app.table.loading = true;
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
            })
        },
        scrap: function () {
            app.dialog.loading = true;
            let data = {
                id: app.dialog.data.id,
                remarks: app.dialog.data.remarks,
                scrap_time: app.dialog.data.scrap_time
            };
            ajaxPostJSON(this.urls.scrap, data, function (d) {
                app.dialog.loading = false;
                app.dialog.visible = false;
                app.$message({
                    message: "操作成功",
                    type: "success"
                });
                app.getList();
            })
        },
        getList: function (index) {
            app.table.props.pageIndex = 1;
            this.refreshTable();
        },
        showDialog: function (v) {
            let app = this;
            app.dialog.data.id = v["id"];
            app.dialog.data.remarks = v["remarks"];
            app.dialog.data.scrap_time = v["scrap_time"];
            app.dialog.visible = true;
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
        }
    },
    mounted: function () {
        this.getSub();
        this.refreshTable();
    },
    computed: {
        visitDate: function () {
            let nowDate = new Date();
            let year = nowDate.getFullYear();
            let month = nowDate.getMonth() + 1;
            let day = nowDate.getDate();
            return year + "-" + month + "-" + day;
        }
    }
});

function getExportConditions() {
    let ID = [];
    app.table.selectionList.forEach(function (v) {
        ID.push(v["id"]);
    });
    let data = {
        fileName: "报废安全保密产品",
        templateId: "ab81d835f0b146d98b4f5e06e0f651c0",//todo 编号！！！
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
        conditionsList: [],
        idList: ID,
        isScrapped: true,
        tableName: "security_products"
    };
    return data;
}