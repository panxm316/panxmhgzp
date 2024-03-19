/**
 * 项目名称：cpsn
 * 类描述： 大文件上传插件，返回StoryFileModel类型的数组
 * 创建人：suny
 * 创建时间：2017年10月25日 下午4:18:59
 * 1、修改人：
 *    修改时间：
 *    修改备注：
 */

import {
  getToken
} from '@/utils/auth'
import WebUploader from 'webuploader'
// import '@/assets/vendor/webuploader/webuploader'
import 'webuploader/css/webuploader.css'
import '@/assets/vendor/webuploader/radialindicator.css'
import '@/assets/vendor/webuploader/hgwebuploader.css'
// import '@/assets/vendor/webuploader/radialIndicator.js'

/**
 * 项目名称：cpsn
 * 类描述： 大文件上传插件，返回StoryFileModel类型的数组
 * 创建人：suny
 * 创建时间：2017年10月25日 下午4:18:59
 * 1、修改人：
 *    修改时间：
 *    修改备注：
 */

(function ($, window) {
  var applicationPath = window.applicationPath === '' ? '' : window.applicationPath || '../..'

  function SuiJiNum() {
    return (((1 + Math.random()) * 0x10000) | 0).toString(16).substring(1)
  }

  var gridData = [] //上传成功后的文件列表
  var storytypes = [] //稿件文件类型
  var uploader
  var filecount = 0
  var fileerr = false

  function initWebUpload(item, options) {

    // if (!WebUploader.Uploader.support()) {
    //     var error = "上传控件不支持您的浏览器！请尝试升级flash版本或者使用Chrome引擎的浏览器。<a target='_blank' href='http://se.360.cn'>下载页面</a>";
    //     if (window.console) {
    //         window.console.log(error);
    //     }
    //     $(item).text(error);
    //     return;
    // }

    var fileMd5
    var bflagfile = true //分块文件夹是否存在
    //是否上传分片验证
    WebUploader.Uploader.register({
      //'before-send-file': 'beforeSendFile', //整个文件上传前
      "before-send": "beforeSend", //每个分片上传前
      //'after-send-file': 'afterSendFile'  //分片上传完毕
    }, {
      //            //时间点1：所有分块进行上传之前调用此函数
      //            beforeSendFile: function (file) {
      //                var self = this;
      //                var deferred = WebUploader.Deferred();
      //                //1、计算文件的唯一标记fileMd5，用于断点续传  如果.md5File(file)方法里只写一个file参数则计算MD5值会很慢 所以加了后面的参数：10*1024*1024
      //                (new WebUploader.Uploader()).md5File(file, 0, 10 * 1024 * 1024).progress(function (percentage) {
      //                        $('#' + file.id).find('p.state').text('正在读取文件信息...');
      //                    })
      //                    .then(function (val) {
      //                        $('#' + file.id).find("p.state").text("成功获取文件信息...");
      //                        fileMd5 = val;
      //                        self.owner.options.formData.uid = fileMd5;
      //                        //获取文件信息后进入下一步
      //                        deferred.resolve();
      //                    });
      //
      //                fileName = file.name; //为自定义参数文件名赋值
      //                return deferred.promise();
      //            }

      beforeSend: function (block) {
        var deferred = WebUploader.Deferred()
        var _this = this
        this.owner.md5File(block.blob)
          .progress(function (percentage) {
            // ignore
          })
          .then(function (val) {
            // 扩展分片上传请求参数
            _this.owner.options.formData = {
              ..._this.owner.options.formData,
              md5: val
            }
            deferred.resolve()
          })
        return deferred.promise()
      },
      // afterSendFile: function(file) {
      //   //第一步：先检查文件路径下是否存在该文件，如果存在则修改旧文件名称和文件状态
      //   var deferred = WebUploader.Base.Deferred();
      //   mergeFile(file, null, null, deferred);
      //   return deferred.promise();
      // }

    })

    //创建默认参数
    var defaults = {
      auto: true,
      hiddenInputId: 'uploadifyHiddenInputId', // input hidden id
      onAllComplete: function (event) {
      }, // 当所有file都上传后执行的回调函数
      onComplete: function (event) {
      }, // 每上传一个file的回调函数
      innerOptions: {},
      fileNumLimit: undefined, //验证文件总数量, 超出则不允许加入队列
      fileSizeLimit: undefined, //验证文件总大小是否超出限制, 超出则不允许加入队列。按字节计算
      fileSingleSizeLimit: undefined, //验证单个文件大小是否超出限制, 超出则不允许加入队列
      PostbackHold: false
    }
    var opts = $.extend(defaults, options)
    var hdFileData = $('#' + opts.hiddenInputId)
    var target = $(item) //容器
    var pickerid = ''
    if (typeof guidGenerator36 != 'undefined') //给一个唯一ID
    {
      pickerid = guidGenerator36()
    } else {
      pickerid = (((1 + Math.random()) * 0x10000) | 0).toString(16).substring(1)
    }

    var uploaderStrdiv = '<div class="webuploader">'

    var multiple = opts.multiple == undefined ? true : opts.multiple //控制单选chixq
    if (opts.auto) {
      var str = ''
      if (opts.btnid == undefined) { //如果自定义上传按钮为空
        if (opts.btn) { //显示按钮chixq
          var btn = '选择文件'
          if (CPSN.contextmenu) {
            btn = '选择图片'
          }
          str = '<div id="Uploadthelist" class="el-upload uploader-list"></div><div id="' + pickerid + '" class="choosefile">' + btn + '</div>'
          multiple = false
        } else { //显示为“+”图标
          str = '<div id="Uploadthelist" class="el-upload uploader-list"></div><div id="' + pickerid + '" class="el-upload el-upload--picture-card">' +
            '<div class="el-upload el-upload--picture-card"><i class="el-icon-plus"></i></div></div>'
        }
      } else { //自定义按钮id名称
        str = '<div id="Uploadthelist" class="el-upload uploader-list"></div>'
        pickerid = opts.btnid
      }
      uploaderStrdiv = '<div class="btns">' + str
      // /*'<div id="' + pickerid + '">选择文件</div>' +*/
    } else {
      var str = ''
      if (opts.btnid == undefined) { //如果自定义上传按钮为空
        if (opts.btn) { //显示按钮chixq
          str = '<div id="' + pickerid + '" class="choosefile">选择文件</div>'
          multiple = false
        } else { //显示为“+”图标
          str = '<div id="' + pickerid + '" class="el-upload el-upload--picture-card">' +
            '<div class="el-upload el-upload--picture-card"><i class="el-icon-plus"></i></div></div>'
        }
      } else { //自定义按钮id名称
        pickerid = opts.btnid
      }
      uploaderStrdiv = '<div id="Uploadthelist" class="uploader-list"></div><div class="btns">' + str
    }
    //<button class="webuploadbtn">开始上传</button>
    //        uploaderStrdiv += '<div style="display:none" class="UploadhiddenInput" >\
    //                         </div>'
    uploaderStrdiv += '</div>'
    target.append(uploaderStrdiv)

    var $list = target.find('.uploader-list'),
      $btn = target.find('.icon-zanting'), //手动上传按钮备用
      state = 'pending' //,
    //             $hiddenInput = target.find('.UploadhiddenInput'),
    //uploader;
    var jsonData = {
      fileList: []
    }
    var radialObj
    var fileid = ''
    var webuploaderoptions = $.extend({
        headers: {
          'Authorization': 'Bearer ' + getToken()
        }, //muyn221024增加存储token用于视频稿件上传插件
        auto: opts.auto,
        // swf文件路径
        swf: applicationPath + 'Uploader.swf',
        // 选择文件的按钮。可选。
        // 内部根据当前运行是创建，可能是input元素，也可能是flash.
        pick: {
          id: '#' + pickerid,
          multiple: multiple
        },
        fileVal: 'multiFile',
        timeout: 0,
        accept: opts.accept,
        //不压缩image, 默认如果是jpeg，文件上传前会压缩一把再上传！
        // server: import.meta.env.VITE_APP_BASE_API + '/storycapture/storyupfile/doup', //opts.server,
        server: opts.server,
        duplicate: true, //同一文件是否可重复选择
        resize: false,
        formData: {
          'status': 'multi',
          'model': 'counsel',
          'mtype': '',
          'uid': '',
          'storytype': '', //稿件类型VR视频稿件mp4文件不需要转码
          'isattachment': opts.isattachment // 是否附件方式参数 true为直接上传统一文件
        },
        compress: null, //图片不压缩
        chunked: true, //分片
        chunkSize: 1 * 1024 * 1024, //每片5M
        chunkRetry: false, //如果失败，则不重试
        threads: 1, //上传并发数。允许同时最大上传进程数。
        // 禁掉全局的拖拽功能。这样不会出现图片拖进页面的时候，把图片打开。
        disableGlobalDnd: true,

        //---------------------------------
        fileNumLimit: opts.fileNumLimit,
        fileSizeLimit: opts.fileSizeLimit,
        fileSingleSizeLimit: opts.fileSingleSizeLimit
      },
      opts.innerOptions)
    uploader = WebUploader.create(webuploaderoptions)
    setTimeout(function () {
      //	        if($(":file").parent().attr("style").indexOf("width: 1px;")>=0){
      if ($(':file').parent().css('width') != $('.el-upload--picture-card').css('width')) {
        $(':file').parent().css('width', $('.el-upload--picture-card').css('width'))
        $(':file').parent().css('height', $('.el-upload--picture-card').css('height'))
      }
    }, 50)

    //回发时还原hiddenfiled的保持数据
    var fileDataStr = hdFileData.val()
    if (fileDataStr && opts.PostbackHold) {
      jsonData = JSON.parse(fileDataStr)
      $.each(jsonData.fileList, function (index, fileData) {
        var newid = SuiJiNum()
        fileData.queueId = newid
        $list.append('<div id="' + newid + '" class="item">' +
          '<div class="info">' + fileData.name + '</div>' +
          '<div class="state">已上传</div>' +
          '<div class="del"></div>' +
          '</div>')
      })
      hdFileData.val(JSON.stringify(jsonData))
    }

    uploader.on('beforeFileQueued', function (file) {
      if (typeof opts.getFileCount === 'function') {
        //peij 20230220 获取相关业务中的文件数
        const fileCountObj = opts.getFileCount()
        const isMaxFileNum = fileCountObj.fileCount + uploader.getStats().queueNum - uploader.getStats().cancelNum >= fileCountObj.fileNumLimit
        if (multiple && isMaxFileNum) {
          if (!fileerr) {
            alert('文件超出个数！最多支持个数：' + fileCountObj.fileNumLimit)
            fileerr = true
          }
          return false
        }
      } else {
        //20180903 suny  计算文件个数
        if (multiple && opts.fileNumLimit != undefined && (filecount + uploader.getStats().successNum + uploader.getStats().queueNum - uploader.getStats().cancelNum) >= opts.fileNumLimit) {
          if (!fileerr) {
            alert('文件超出个数！最多支持个数：' + opts.fileNumLimit)
            fileerr = true
          }
          return false
        }
      }

      var ext = file.ext.toLowerCase()
      var flagadd = false
      var flagtype = false //是否已设置文件类型
      //peij 20221103 获取内容的文件不分片上传
      if ("txt,doc,docx,wps".indexOf(ext.toLowerCase()) > -1) {
        uploader.options.chunked = false
      }

      if (opts.accept) {
        if (opts.accept.title && opts.accept.extensions) {
          flagtype = true
          if (opts.accept.extensions.toLowerCase().indexOf(ext) >= 0) {
            file.mtype = opts.accept.title
            flagadd = true
          }
        }
      }
      if (opts.storytype) {
        file.storytype = opts.storytype
      }
      if (!flagtype) {
        if (storytypes && storytypes.length > 0) {
          flagadd = false
          storytypes.forEach(function (ftype) {
            var fileformat = ftype.sfileformat.split(',')
            var formatindex = fileformat.findIndex( (f) => f.toLowerCase() == ext)
            if (formatindex >= 0 && !flagadd) {
              file.mtype = ftype.sguid
              flagadd = true
            }
            // if (ftype.sfileformat.toLowerCase().indexOf(ext) >= 0 && !flagadd) {
            //     file.mtype = ftype.sguid;
            //     flagadd = true;
            // }
          })
        } else {
          //peij 20221104 opt.typelimt:true 不限文件类型
          if (!opts.typelimit) {
            if ('jpg,gif,jpeg,exif,fpx,png,svg,tif,tiff,bmp'.indexOf(ext) >= 0) {
              file.mtype = 'Photo'
              flagadd = true
            }
          } else {
            file.mtype = 'files'
            flagadd = true
          }
        }
      }
      if (!flagadd) {
        window.console.log('不支持' + ext + '格式文件上传!')
        alert('不支持' + ext + '格式文件上传!')
        uploader.removeFile(file, true)
        return false
      }
    })

    if (opts.auto) {

      uploader.on('fileQueued', function (file) {
        file.uid = file.source.uid

        $list.append('<div class="prg-cont rad-prg upkongzhi" id="indicatorContainer' + file.id + '"><div class="kongzhi"><i class="webuploadDelbtn el-icon-delete2" id="del_' + file.id + '"></i></div></div>')
        //<i class=\"icon iconfont icon-zanting1\" id=\"stop_"+file.id+"\"></i>
        //<label class=\"img-label_file del\" ><i class=\"el-icon-delete\"></i></label>
        //peij 自定义进度
        if (opts.uploading) {
          emitter.emit('uploading', true)
        } else {
          emitter.emit('uploading', true)
          $('#indicatorContainer' + file.id).radialIndicator({
            barColor: '#87CEEB',
            barWidth: 10,
            initValue: 0,
            roundCorner: true,
            percentage: true
          })
        }
        $('#indicatorContainer' + file.id).attr('style', 'display:none')
        $list.show()
        uploader.upload()
      })
    } else {
      uploader.on('fileQueued', function (file) { //队列事件
        var ext = file.name.substring(file.name.lastIndexOf('.') + 1).toLowerCase()
        var flagadd = false
        if (storytypes && storytypes.length > 0) {
          storytypes.forEach(function (ftype) {
            if (ftype.sfileformat.toLowerCase().indexOf(ext) >= 0) {
              file.mtype = ftype.sguid
              flagadd = true
            }
          })
        }
        if (!flagadd) {
          window.console.log('不支持' + ext + '格式文件上传!')
          alert('不支持' + ext + '格式文件上传!')
          uploader.removeFile(file, true)
          return false
        } else {
          $list.append('<div class="prg-cont rad-prg el-upload el-upload--picture-card upkongzhi" id="indicatorContainer' + file.id + '"><div class="kongzhi"><i class="webuploadDelbtn el-icon-delete2" id="del_' + file.id + '"></i></div></div>')
          //<i class=\"icon iconfont icon-zanting1\" id=\"stop_"+file.id+"\"></i>
          //<label class=\"img-label_file del\" ><i class=\"el-icon-delete\"></i></label>
          $('#indicatorContainer' + file.id).radialIndicator({
            barColor: '#87CEEB',
            barWidth: 10,
            initValue: 0,
            roundCorner: true,
            percentage: true
          })
          $('#indicatorContainer' + file.id).attr('style', 'display:none')
          $list.show()
        }
      })
    }

    uploader.on('uploadProgress', function (file, percentage) { //进度条事件
      if (fileid != file.id) {
        fileid = file.id

        radialObj = $('#indicatorContainer' + file.id).data('radialIndicator')
        $('#indicatorContainer' + file.id).attr('style', '')
      }
      if (radialObj == null || radialObj == undefined) {
        radialObj = $('#indicatorContainer' + file.id).data('radialIndicator')
      }
      //peij 自定义进度
      if (opts.uploading == undefined) {
        radialObj.animate(percentage * 99)
      } else {
        emitter.emit('uploadProgress', percentage * 99)
      }

    })
    uploader.on('uploadStart', function (file, Object) { //上传成功事件
      uploader.options.formData.mtype = file.mtype
      uploader.options.formData.uid = file.uid
      uploader.options.formData.storytype = file.storytype ? file.storytype : ''
    })
    uploader.on('uploadSuccess', function (file, Object) { //上传成功事件
      //            debugger
      if (Object.success) {
        // Object.obj.showflag = false
        // if (Object.obj.sfiletype == 'Video') {
        //     if (Object.obj.url == undefined || Object.obj.url == '') {
        //         Object.obj.url = process.env.BASE_URL + 'static/styles/images/defaultcover.jpg'
        //     }
        //     if (Object.obj.durl == '') {
        //         Object.obj.durl = process.env.BASE_URL + 'static/styles/images/default.mp4'
        //     }
        //
        // } else {
        //     if (Object.obj.url == undefined || Object.obj.url == '') {
        //         Object.obj.url = process.env.BASE_URL + 'static/styles/images/' +
        //             Object.obj.sfiletype + '.png'
        //     } else {
        //         var sfilesize = '?m=fit'
        //         if (opts.width != null && opts.width != undefined &&
        //             opts.width != '') {
        //             sfilesize += '&w=' + opts.width
        //         } else {
        //             sfilesize += '&w=200'
        //         }
        //         if (opts.height != null && opts.height != undefined &&
        //             opts.height != '') {
        //             sfilesize += '&h=' + opts.height
        //         } else {
        //             sfilesize += '&h=200'
        //         }
        //
        //         Object.obj.url = Object.obj.url + sfilesize
        //     }
        // }

        if (!multiple) {
          uploader.removeFile(file, true)
        }
        //peij 上传成功
        if (opts.uploading) {
          emitter.emit('uploadSuccess', Object)
          // uploader.removeFile(file);
        } else {
          //peij 20201214 文件重复
          var fileindex = gridData.findIndex(function (f) {
            return f.sfileid == Object.obj.sfileid
          })

          if (fileindex >= 0) {
            alert(Object.obj.soriginalfile + ',文件重复')
            return
          }
          gridData.push(Object.obj)
          emitter.emit('getUploadGridData', gridData)
        }
        $('#indicatorContainer' + file.id).remove()
      } else {
        //emitter.emit('uploadSuccess', Object.obj);
        emitter.emit('uploadSuccess', Object)
        uploader.removeFile(file)
        $('#indicatorContainer' + file.id).remove()
        alert(Object.msg)
      }
    })

    uploader.on('error', function (err, filemaxcount, file) {
      if (err == 'Q_EXCEED_NUM_LIMIT') {
        alert('文件超出个数！最多支持个数：' + filemaxcount) // opts.fileNumLimit);
        //                uploader.removeFile(file);

      } else if (err == 'Q_TYPE_DENIED') {
        var name = file == undefined ? filemaxcount.name : file.name
        alert(name + '可能是空文件，空文件类型不支持')
        //                uploader.removeFile(file);
      } else if (err == 'F_EXCEED_SIZE') { //20180914  by suny
        var name = file == undefined ? filemaxcount.name : file.name
        var isize = filemaxcount / (1024 * 1024)
        alert(name + '文件超过限制，最大支持 ' + isize + ' M')
        //                uploader.removeFile(file);
      }
      $('#indicatorContainer' + file.id).remove()
    })

    //全部上传完成
    uploader.on('uploadFinished', function () {
      fileerr = false
      //peij 自定义进度
      if (opts.uploading) {
        emitter.emit('uploading', false)
      } else {
        if (radialObj) {
          radialObj.animate(100)
        }
      }
      $list.hide()
    })

    uploader.on('uploadError', function (file) {
      //20180914  by suny
      var name = file == undefined ? '' : file.name
      if (file.statusText == 'timeout') {
        alert(name + '文件上传超时')
      } else {
        alert(name + '文件上传失败')
      }
      uploader.removeFile(file)
      $('#indicatorContainer' + file.id).remove()
      //            target.find('#' + $(item)[0].id + file.id).find('span.webuploadstate').html('上传出错');
      //        	$( "#"+file.id ).find("p.state").text("上传出错");
      //              //多个文件
      //              var fileArray = uploader.getFiles();
      //              for(var i = 0 ;i<fileArray.length;i++){
      //                  //取消文件上传
      //                   uploader.cancelFile(fileArray[i]);
      //                   //从队列中移除掉
      //                   uploader.removeFile(fileArray[i],true);
      //             }
    })

    uploader.on('uploadComplete', function (file) { //全部完成事件
      $('#indicatorContainer' + file.id).remove()
      //            target.find('#' + $(item)[0].id + file.id).find('.progress').fadeOut();

    })

    //删除时执行的方法
    uploader.on('fileDequeued', function (file) {
      //            debugger
      // $.post(
      //     import.meta.env.VITE_APP_BASE_API + '/storycapture/storyupfile/dodel', {
      //         'uid': file.source.ruid
      //     },
      //     function(data) {
      //         //                    alert(data.msg);
      //     })

    })
    $('.webuploader-pick').removeClass('webuploader-pick')

    $list.on('click', '.icon-zanting1', function () {
      var $ele = $(this)
      var id = $ele.attr('id')
      var id = id.replace('stop_', '')

      radialObj = null
      $ele.removeClass('icon-zanting1').addClass('icon-kaishi')
      var file = uploader.getFile(id)
      uploader.stop(true)
    })

    $list.on('click', '.icon-kaishi', function () {
      var $ele = $(this)
      var id = $ele.attr('id')
      var id = id.replace('stop_', '')

      $ele.removeClass('icon-kaishi').addClass('icon-zanting1')
      radialObj = $('#indicatorContainer' + id).data('radialIndicator')
      //          var file = uploader.getFile(id);
      uploader.upload()
    })
    //删除
    $list.on('click', '.webuploadDelbtn', function () {
      //            debugger
      var $ele = $(this)
      var id = $ele.attr('id')
      var id = id.replace('del_', '')

      var file = uploader.getFile(id)
      $('#indicatorContainer' + id).remove()
      uploader.removeFile(file)
    })

  }

  /**
   * 返回已上传的文件列表
   */
  $.fn.GetFilesAddress = function (grid) {
    if (grid != undefined && grid != null && grid.length > 0) {
      gridData = grid
      uploader.reset() //20180903 suny
    } else {
      gridData = []
    }
    filecount = gridData.length //20180903 suny  计算文件个数
    return gridData
  }

  /**
   * 初始化
   */
  $.fn.powerWebUpload = function (options, types) {
    var ele = this
    if (typeof WebUploader == 'undefined') {
      alert('请检查webuploader的路径是否正确!')
    } else {
      initWebUpload(ele, options)
      storytypes = types
      filecount = 0
    }
  }

  $.fn.refreshWebUpload = function () {
    if (uploader) {
      uploader.refresh()
    }
  }
  /*销毁上传*/
  $.fn.destroyWebUpload = function () {
    this.empty()
    if (uploader) {
      uploader.destroy()
    }
  }
})(jQuery, window);
