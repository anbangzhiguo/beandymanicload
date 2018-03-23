define(function (require,exports,module) {
    var sTpl = require("modules/pluglist/pluglist.html");
    require("modules/pluglist/pluglist.css");
    var VueComponent = Vue.extend({
        template: sTpl,
        methods:{
            testClick:function(unit,method){
                $.ajax({
                    url:"/excute/"+unit+"/"+method,
                    contentType:"application/json",
                    type:"POST",
                    data:this.param||'{}',
                    success:function (res) {
                        alert(res);
                    }
                });
            },
            tirggerFile:function (e) {
                var self = this;
                var files = e.target.files;
                var form = new FormData(),
                    url = '/uploadPlug', //服务器上传地址
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
                        $.get("/getPlugs", function(res){
                            self.pluglist = res;
                        });
                    }
                });
                xhr.send(form); //开始上传
            }
        },
        data:function(){
            return {
                pluglist:[],
                param:""
            }
        },
        mounted: function () {
            var self = this;
            $.get("/getPlugs", function(res){
                self.pluglist = res;
            });
        }
    });

    module.exports = VueComponent;
});

