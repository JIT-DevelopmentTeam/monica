<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
    <title>莫尔卡·移动应用平台</title>
    <meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=0" />
    <script src="${ctxStatic}/common/vue/js/vue.js"></script>
    <script src="${ctxStatic}/common/vue/js/vue-router.min.js"></script>
</head>
<body>
<div id="app">
    <p>{{massage}}</p>

</div>
<script>
    function test() {
        alert('${ctxStatic}');
    }


    var router = new VueRouter({

    })

    var vue = new Vue({
        el: '#app',
        router,
        data: {
            massage:"跳转页面"
        },
        methods:{
            test() {
                alert('${ctxStatic}');
            },
            goBD(){
                this.$router.replace('http://www.baidu.com');
            }
        }
    })
</script>
</body>
</html>
