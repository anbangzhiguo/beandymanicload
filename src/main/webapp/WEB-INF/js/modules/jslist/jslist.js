define(function (require,exports,module) {
    var sTpl = require("modules/jslist/jslist.html");
    require("modules/jslist/jslist.css");
    var VueComponent = Vue.extend({
        template: sTpl,
        methods:{

        },
        data:function(){
            return {
            }
        }
    });

    module.exports = VueComponent;
});

