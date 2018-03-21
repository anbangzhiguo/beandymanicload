define(function (require,exports,module) {
    var sTpl = require("templates/pluglist.html");
    require("modules/pluglist.css");
    require("lib/layer/layer.js");
    layer.config({
        path: './js/lib/layer/'
    });
    
    var VueComponent = Vue.extend({
        template: sTpl
        ,methods:{
            click1:function(){
                this.msg = "zhang";
                layer.msg("我是Bar页面");
            }
        }
        ,data:function(){
            return {
                msg:"Bar页面"
            }
        }
    });

    module.exports = VueComponent;
});

