<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>SYS</title>
    <style>
        #app{
            display: flex;
            flex-direction: row;

        }
        #menu{
            flex: 1;
            background-color: #b3d4db;
            align-items: center;
        }
        #content{
            flex: 6;
        }


    </style>
</head>
<body>

    <div id="app">
        <div id="menu"><mymenu></mymenu></div>
        <div id="content"><router-view></router-view></div>
    </div>
    <script src="/js/lib/jquery-1.11.2.js"></script>
    <script src="/js/lib/sea.js"></script>
    <script src="https://unpkg.com/vue/dist/vue.js"></script>
    <script src="https://unpkg.com/vue-router/dist/vue-router.js"></script>
    <script>
        var gMain = {
            verson:"1.0"
            ,basePath:"/vue+seajs/"
        };
        seajs.config({
            base: "/js"
            ,charset: 'utf-8'
            ,map: [[ /^(.*\.(?:css|js|html))(.*)$/i, '$1?v=_VERSION_'+'-'+gMain.verson]]
            ,paths:{

            }
        });

        $(function () {
            seajs.use(["src/app.js"]);
        });
    </script>
</body>
</html>