define(function (require,exports,module) {
    var sTpl = require("modules/menulist/menulist.html");
    require("modules/menulist/menulist.css");
    var VueComponent = Vue.extend({
        template: sTpl,
        methods:{
            addMenu:function () {
                var _self = this;
                $.ajax({
                    url:"/addMenus",
                    contentType:"application/json",
                    type:"POST",
                    data:JSON.stringify({code:_self.code,js:_self.js,describe:_self.describe}),
                    success:function (res) {
                        gMain.freshRoute();
                        gMain.app.$refs.freshdata.$options.methods.freshdata.bind(gMain.app.$refs.freshdata)();
                    }
                });
            }
        },
        data:function(){
            return {
                code:"",
                describe:"",
                js:"",
                menulist:[]
            }
        }
    });

    module.exports = VueComponent;
});

