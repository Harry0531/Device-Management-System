let app = new Vue({
    el: '#app',
    data: {
        passwordData: {
            password: '',
            newPassword: '',
            newPasswordConfirm: ''
        },
        fullScreenLoading: false,
        isLogin: true,
        urls: {
            checkPassword: 'http://localhost:8444/api/sys/user/login',
            changePassword: 'http://localhost:8444/api/sys/user/changePassword'
        }
    },
    methods: {
        login: function () {
            let app = this;
            let data = {
                data: app.passwordData.password
            };
            app.fullScreenLoading = true;
            ajaxPost(app.urls.checkPassword, data, function (d) {
                app.fullScreenLoading = false;
                if (d.code === 'success') {
                    app.$message({
                        message: '登录成功',
                        type: 'success'
                    });
                    delCookie("name");
                    setCookie("name", "value", 0);
                    setTimeout(function () {
                        window.open("frame.html", "_self");
                    }, 2000);
                } else {
                    app.$message({
                        message: '登录失败',
                        type: 'error'
                    });
                    app.passwordData.password = '';
                }
            })
        },
        changePassword: function () {
            let app = this;
            if (app.passwordData.newPassword !== app.passwordData.newPasswordConfirm) {
                app.$message({
                    message: '两次输入不一致',
                    type: 'error'
                });
                app.passwordData.password = '';
                app.passwordData.newPassword = '';
                app.passwordData.newPasswordConfirm = '';
                return;
            }

            let data = {
                data: app.passwordData.password
            };
            app.fullScreenLoading = true;
            ajaxPost(app.urls.checkPassword, data, function (d) {
                if (d.code === 'success') {
                    let pw = {
                        data: app.passwordData.newPassword
                    };
                    ajaxPost(app.urls.changePassword, pw, function (r) {
                        app.fullScreenLoading = false;
                        if (r.code === 'success') {
                            app.$message({
                                message: '修改成功',
                                type: 'success'
                            });
                            app.passwordData.password = '';
                            app.passwordData.newPassword = '';
                            app.passwordData.newPasswordConfirm = '';
                            app.isLogin = true;
                        } else {
                            app.$message({
                                message: '修改失败，请重试',
                                type: 'error'
                            });
                            app.passwordData.password = '';
                            app.passwordData.newPassword = '';
                            app.passwordData.newPasswordConfirm = '';
                        }
                    })
                } else {
                    app.$message({
                        message: '原密码有误',
                        type: 'error'
                    });
                    app.fullScreenLoading = false;
                    app.passwordData.password = '';
                    app.passwordData.newPassword = '';
                    app.passwordData.newPasswordConfirm = '';
                }
            })
        }
    }
});


// window.onload = function () {
//     setCookie("name", "value", 0);
//     window.open("frame.html", "_self")
// };