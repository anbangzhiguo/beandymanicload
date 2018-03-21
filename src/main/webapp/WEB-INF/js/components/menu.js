
define(function (require,exports,module) {

    Vue.component('mymenu', Vue.extend({
        template:"<p><template v-for=\"item in aMenu\"><a :href=\"'#/'+item.js\">{{item.describe}}</a><br></template></p>",
        data:function(){
            return {
                aMenu:[]
            };
        },
        mounted: function () {
            var self = this;
            $.get("/getMenus", function(data){
                self.aMenu = data;
            });
        }
    }));

});
