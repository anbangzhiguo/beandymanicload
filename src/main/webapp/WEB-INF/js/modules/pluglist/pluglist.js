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

