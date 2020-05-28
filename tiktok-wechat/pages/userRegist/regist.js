const app = getApp()

Page({
    data: {
      
    },
    doRegist: function(e) {
        var formObject = e.detail.value;
        var username = formObject.username;
        var password = formObject.password;

        // 验证
        if (username.length == 0 || password.length == 0) {
            wx.showToast({
              title: '用户名或密码不能为空',
              icon: 'none',
              duration: 1500
            })
        } else {
            var serverUrl = app.serverUrl;
            wx.showLoading({
              title: '请等待...',
            });
            wx.request({
              url: serverUrl + '/regist',
              method: "POST",
              data: {
                username: username,
                password: password
              },
              header: {
                'content-type': 'application/json' // 默认值
              },
              success: function(res) {
                // 200 为成功
                console.log(res.data);
                wx.hideLoading();
                var status = res.data.status;
                if (status == 200) {
                  wx.showToast({
                    title: '用户名注册成功!',
                    icon: 'none',
                    duration: 1500
                  }),
                  app.userInfo = res.data.data;
                } else {
                  wx.showToast({
                    title: res.data.msg,
                    icon: 'none',
                    duration: 1500
                  })
                }
              }
            })
        }
    },

    goLoginPage: function() {
      wx.redirectTo({
        url: '../userLogin/login',
      })
    }
})