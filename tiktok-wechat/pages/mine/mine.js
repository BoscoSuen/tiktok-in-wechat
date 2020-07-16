const app = getApp()

Page({
  data: {
    faceUrl: "../resource/images/noneface.png",
    isMe: true
  },

  onLoad: function(params) {
    var me = this;
    var user = app.userInfo;

    wx.showLoading({
      title: 'loading',
    })

    var serverUrl = app.serverUrl;

    // 调用后端
    wx.request({
      url: serverUrl + '/user/query?userId=' + user.id,
      method: "POST",
      header: {
        'content-type': 'application/json' // 默认值
      },
      success: function (res) {
        console.log(res.data);
        wx.hideLoading();
        if (res.data.status == 200) {
          var userInfo = res.data.data;
          var faceUrl = "../resource/images/noneface.png";
          if (userInfo.faceImage != null && userInfo.faceImage != '' && userInfo.faceImage != undefined) {
            faceUrl = serverUrl + userInfo.faceImage;
          }
          // console.log(userInfo);
          me.setData({
            faceUrl: faceUrl,
            fansCounts: userInfo.fansCounts,
            followCounts: userInfo.followCounts,
            receiveLikeCounts: userInfo.receiveLikeCounts,
            nickname: userInfo.nickname
          })
        }
      }
    }) 
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
  },

  changeFace: function () {
    // https://developers.weixin.qq.com/miniprogram/dev/api/media/image/wx.chooseImage.html
    // console.log("change face");
    var me = this;
    wx.chooseImage({
      count: 1,
      sizeType: ['compressed'],
      sourceType: ['album'],
      success (res) {
        // tempFilePath可以作为img标签的src属性显示图片
        const tempFilePaths = res.tempFilePaths;
        console.log(tempFilePaths);
        // https://developers.weixin.qq.com/miniprogram/dev/api/network/upload/wx.uploadFile.html

        wx.showLoading({
          title: '上传中...',
        })
        var serverUrl = app.serverUrl;

        wx.uploadFile({
          url: serverUrl + '/user/uploadFace?userId=' + app.userInfo.id, 
          filePath: tempFilePaths[0],
          name: 'file',
          header: {
            'content-type' : 'application/json'
          },
          success (res){
            const data = JSON.parse(res.data);
            wx.hideLoading();
            if(data.status == 200) {
              wx.showToast({
                title: '上传成功!',
                icon: "success"
              });

              var imageUrl = data.data;
              me.setData({
                faceUrl: serverUrl + imageUrl
              });

            } else if (data.status == 500) {
              wx.showToast({
                title: data.msg
              }); 
            }

          }
        })
      }
    })
  }, 
  uploadVideo: function() {
    var me = this;
    wx.chooseVideo({
      sourceType: ['album'],
      success: function(res) {
        // console.log(res);
        var duration = res.duration;
        var tmpHeight = res.height;
        var tmpWidth = res.width;
        var tmpVideoUrl = res.tempFilePath;
        var tmpCoverUrl = res.thumbTempFilePath;

        if (duration > 120) {
          // the video is too long
          wx.showToast({
            title: '视频长度不能超过120秒',
            icon: 'none',
            duration: 2500
          })
        } else {
          // 打开选择bgm的页面
          wx.navigateTo({
            url: '../chooseBgm/chooseBgm?duration=' + duration
            + "&tmpHeight=" + tmpHeight
            + "&tmpWidth=" + tmpWidth
            + "&tmpVideoUrl=" + tmpVideoUrl
            + "&tmpCoverUrl=" + tmpCoverUrl
            ,
          })
        }
      }
    })
  }

})
