const app = getApp()

Page({
  data: {
    faceUrl: "../resource/images/noneface.png",
  },

  onLoad: function(params) {

  },

  logout: function() {
    var user = app.userInfo;
    var serverUrl = app.serverUrl;
    // 调用后端
    wx.request({
      url: serverUrl + '/logout?userId=' + user.id,
      method: "POST",
      header: {
        'content-type': 'application/json' // 默认值
      },
      success: function (res) {
        console.log(res.data);
        wx.hideLoading();
        if (res.data.status == 200) {
          // 成功跳转 
          wx.showToast({
            title: '注销成功',
            icon: 'success',
            duration: 2000
          });
          // fixme 修改原有的全局对象为本地缓存
          app.userInfo = null;
          // 页面跳转
          wx.navigateTo({
            url: '../userLogin/login',
          })
        } 
      }
    })
  }

})
