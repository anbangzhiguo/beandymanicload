
define(function (require,exports,module) {
    require("components/menu.js");
    var router = new VueRouter({routes:[]});

    const app = new Vue({ router }).$mount('#app')

    $.getJSON("/getMenus", function(data){
        var routes = [];
        $.each(data,function (i,v) {
            var obj = {
                path:"/"+v.code,
                component:function (resolve) {
                    require.async(['modules/'+v.js+'.js'],resolve);
                }
            }
            routes.push(obj);
        })
        router.addRoutes(routes);
    });

});