
define(function (require,exports,module) {

    Vue.component('mymenu', Vue.extend({
        template:"<p><template v-for=\"item in aMenu\"><a :href=\"'#/'+item.code\">{{item.describe}}</a><br></template></p>",
        data:function(){
            return {
                aMenu:[]
            };
        },
        methods:{
            freshdata:function () {
                var self = this;
                $.get("/getMenus", function(data){
                    self.aMenu = data;
                });
            }
        },
        mounted: function () {
            var self = this;
            $.get("/getMenus", function(data){
                self.aMenu = data;
            });
        }
    }));

});
