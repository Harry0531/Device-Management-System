//数量统计：允许分别按单位、类型、密级、用途、使用范围、使用情况等统计数量
let select1 = [
    {value: 'dept', label: '单位'},
    {value: 'type', label: '类型'},
    {value: 'secret_level', label: '密级'},
    {value: 'usage', label: '用途'},
    {value: 'scope', label: '使用范围'},
    {value: 'search', label: '搜索'}
];

let app = new Vue({
    el: '#app',
    data: {
        urls: {
            getSub: 'http://localhost:8444/api/sys/storage/scrapped/getSub',
            getDeptSub: 'http://localhost:8444/api/sys/storage/scrapped/getDeptSub',
            getList: 'http://localhost:8444/api/sys/storage/scrapped/getList',
            scrap: 'http://localhost:8444/api/sys/storage/scrapped/scrap'
        },
        select1: select1,
        select2: [],
        select3: [],
        select4: [],
        loading: false,
        filters: {
            value1: '',
            value2: '',
            value3: '',
            value4: ''
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
                remarks: '',
                scrap_time: ''
            }
        }
    },
    methods: {
        getSub(prov) {
            app.select2 = [];
            app.filters.value2 = '';
            app.select3 = [];
            app.filters.value3 = '';
            app.select4 = [];
            app.filters.value4 = '';
            if (prov == 'search')
                return;
            let data = {
                param: prov
            };
            ajaxPost(this.urls.getSub, data, function (result) {
                if (prov === 'dept') {
                    result.forEach(function (r) {
                        app.select3.push(r);
                    });
                } else {
                    result.forEach(function (r) {
                        app.select2.push({'value': r.id, 'label': r.dicValue});
                    });
                }
            });
            if (app.filters.value1 == '') {
                app.table.type = '';
                app.table.props.pageIndex=1;
                app.refreshTable();
            }
        },
        getDeptSub: function (index) {
            app.select4 = [];
            app.filters.value4 = '';
            ajaxPost(this.urls.getDeptSub, {id: index}, function (result) {
                result.forEach(function (r) {
                    app.select4.push(r);
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
                app.resetDialogData();
                app.getList();
                app.filters.value1 = '';
                app.filters.value2 = '';
            })
        },
        getList: function (index) {
            this.table.type = index;
            if (app.filters.value4 == '' && app.filters.value3 != '')
                this.table.type = app.filters.value3;
            app.table.props.pageIndex=1;
            this.refreshTable();
        },
        showDialog: function (v) {
            let app = this;
            app.dialog.data.id = v["id"];
            app.dialog.data.remarks = v["remarks"];
            app.dialog.data.scrap_time = v["scrap_time"];
            app.dialog.visible = true;
        },
        resetDialogData: function () {
            app.dialog = {
                visible: false,
                loading: false,
                data: {
                    id: '',
                    remarks: '',
                    scrap_time: ''
                }
            }
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