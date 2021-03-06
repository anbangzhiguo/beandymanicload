
define(function (require,exports,module) {
    require("components/menu.js");
    var router = new VueRouter({routes:[]});
    const app = new Vue({ router:router }).$mount('#app')

    function freshRoutes() {
        $.getJSON("/getMenus", function(data){
            var routes = [];
            $.each(data,function (i,v) {
                var obj = {
                    path:"/"+v.code,
                    component:function (resolve) {
                        require.async(['modules/'+v.js+'/'+v.js+'.js'],resolve);
                    }
                }
                routes.push(obj);
            })
            router.addRoutes(routes);
        });
    }

    freshRoutes();

    exports.freshRoutes = freshRoutes;
    exports.app = app;

});