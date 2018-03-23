define(function (require,exports,module) {
    var sTpl = require("modules/jslist/jslist.html");
    require("modules/jslist/jslist.css");
    var VueComponent = Vue.extend({
        template: sTpl,
        methods:{
            tirggerFile:function (e) {
                var self = this;
                var files = e.target.files;
                var form = new FormData(),
                    url = '/uploadJs', //服务器上传地址
                    file = files[0];
                form.append('file', file);

                var xhr = new XMLHttpRequest();
                xhr.open("post", url, true);
                xhr.upload.addEventListener("progress", function(result) {
                    if (result.lengthComputable) {
                        var percent = (result.loaded / result.total * 100).toFixed(2);
                    }
                }, false);

                xhr.addEventListener("readystatechange", function() {
                    var result = xhr;
                    if (result.status != 200) { //error
                        console.log('上传失败', result.status, result.statusText, result.response);
                    }
                    else if (result.readyState == 4) { //finished
                        console.log('上传成功', result);
                        alert('上传成功');
                    }
                });
                xhr.send(form); //开始上传
            }
        },
        data:function(){
            return {
            }
        }
    });

    module.exports = VueComponent;
});

