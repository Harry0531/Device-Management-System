let urls = [
    ['单位管理', 'management/deptManagement.html'],
    ['字典项管理', 'management/dictManagement.html'],
    ['数据管理', 'management/ExcelManage.html'],
    ['涉密计算机', 'computer/confidentialComputer.html'],
    ['非涉密中间机', 'computer/nonConfidentialIntermediary.html'],
    ['非涉密计算机', 'computer/nonConfidentialComputer.html'],
    ['报废计算机', 'computer/scrappedComputer.html'],
    ['涉密信息设备', 'informationDevice/confidentialInfoDevice.html'],
    ['非涉密信息设备', 'informationDevice/nonConfidentialInfoDevice.html'],
    ['报废信息设备', 'informationDevice/scrappedInfoDevice.html'],
    ['涉密存储介质', 'storage/confidentialStorage.html'],
    ['非涉密存储介质', 'storage/nonConfidentialStorage.html'],
    ['报废涉密存储介质', 'storage/scrappedStorage.html'],
    ['安全保密产品', 'securityProduct/securityProducts.html'],
    ['报废安全保密产品', 'securityProduct/scrappedSecurityProducts.html'],
    ['USB Key', 'usb/usb.html'],
    ['报废USB Key', 'usb/scrappedUSB.html']
];

let app = new Vue({
    el: '#app',
    data: {

    },
    created: function () {
        this.checkStatus();
        this.showWindow = true;
    },
    methods: {
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
                window.open("login.html", "_self")
            }, 2000);
        },
        addTab(){
            parent.addTab(1,1);
        }
    }
})