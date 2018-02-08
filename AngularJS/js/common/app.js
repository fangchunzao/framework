var myApp = angular.module('myApp',['ngRoute','ngTable',
    'myApp.indexModule'
    ]);
myApp.run(['$rootScope', '$window', '$location', '$route', function ($rootScope, $window, $location, $route) {
    $rootScope.$on('$routeChangeStart', routeChangeStart);
    function routeChangeStart(event) {
    }
}]);
myApp
.config(['$routeProvider', function($routeProvider){
        //首页
        $routeProvider.when('/select',{templateUrl: 'template/select.html',controller:'selectController'});
        $routeProvider.when('/ngTable',{templateUrl: 'template/ngTable.html',controller:'tableController'});
    }])
.factory('dataService',function($http){
    var interFaceUrl = interFaceUrl_global;
    var popIndex = 0;
    return {
            /*
             *  创建接口对象
             */
             index:{

             },
             interfaces: {
                //登录接口
                index:interFaceUrl + '/index/index',

            },
            /*
             * 请求方法
             * */
             methods:{
                post: 'POST',
                get: 'GET',
                put: 'PUT',
                delete: 'DELETE'
            },

            /*
             * 全局请求
             * */
             go:function(url, type, param, succCallBack, errCallBack,isUpload){
                if(param == "") param = {};
                var loadIndex ;
                var token = this.storage.get("token")["token"]; 
                if(token && token!=""){
                    param.token = token;
                }
                var show = function() {
                    loadIndex = loadIndex > 0 ?  1:layer.load(1,{
                        shade: [0.3, '#000']
                    });
                };
                show();
                var hide = function() {
                    layer.close(loadIndex);
                    loadIndex = 0;
                };
                if (!url) {
                    alert('请求地址为空');
                    hide();
                    return false;
                }
                if (!type) {
                    alert('请求类型为空');
                    hide();
                    return false;
                }
                if (typeof succCallBack != 'function') {
                    hide();
                    succCallBack = function() {
                        App.alert('成功回调函数未定义');
                    }
                }
                var doSuccCallBack = function (res) {
                    hide();
                    if (!res) {
                        return false;
                    } else if (typeof(res) === 'string') {
                        res = JSON.parse(res);
                    }
                    if(res.responseCode == "0" || res.responseCode == "904" || res.responseCode == "901") {
                        succCallBack(res);
                    }else {
                        alertMsg(res.responseCode);
                    }
                };
                var doerrCallBack = function (res) {
                    hide();
                   
                    if (!res) {
                        console.log('返回为空');
                        return false;
                    } else if (typeof(res) === 'string') {
                        // res = JSON.parse(res);
                    }
                    errCallBack(res);
                };
                 
                var reqParam = {method:type,url:url};
                if(type == "GET") {
                    param.r = Math.random();
                    reqParam.params = param;
                }else {
                    reqParam.data = param;
                    //reqParam.headers = {'Content-Type':'application/json'};
                }
                if(isUpload) {
                    reqParam.headers = {'Content-Type':undefined};
                    reqParam.transformRequest = function(data,headersGetter){
                        var formData = new FormData();
                        angular.forEach(data, function (value, key) {
                            formData.append(key, value);
                        }); 
                        return formData;
                    };
                }

                $http(reqParam).then(function successCallback(response) {
                    // 请求成功执行代码
                    doSuccCallBack(response.data);
                }, function errorCallback(response) {
                    // 请求失败执行代码
                
                    layer.alert('请求失败，如需帮助请联系管理员！', {
                        icon: 0,
                        btn:['确定']
                    });
                    doerrCallBack(response.data);
                });
                
                var alertMsg = function(code) {
                    window.localStorage.removeItem('token');
                    window.sessionStorage.removeItem('token');  // 清除token
                    if(popIndex > 1) {
                        //已存在弹出框
                        return false;
                    }
                    if(code == "999"){
                        //处理返回值responseCode!=0的情况
                        popIndex = layer.alert('用户未登录，请登录后再进行操作！', {
                            icon: 0,
                            btn:['确定']
                        },function(){
                            window.location = "login.html";
                        });
                        return false;
                    }
                    if(code == "905"){
                        //处理返回值responseCode!=0的情况
                        popIndex = layer.alert('用户信息已过期，请重新登录。', {
                            icon: 0,
                            btn:['确定']
                        },function(){
                            window.location = "login.html";
                        });
                        return false;
                    }
                    if(code!=0){
                        //处理返回值responseCode!=0的情况
                        popIndex = layer.alert('请求失败，如需帮助请联系管理员！', {
                            icon: 0,
                            btn:['确定']
                        },function(index){
                            //popIndex = 0;
                            //layer.close(index);
                            window.location = "login.html";
                        });
                        return false;
                    }
                    return true;
                }

            },
            /*
             * 表格下载
             * */
            exportReport:function(url, param){
                var token = this.storage.get("token")["token"];
                if (!url) {
                    alert('请求地址为空');
                    return false;
                }
                url = url + "?token=" + token;
                for(var paramName in param) {
                    url += "&" + paramName + "=" + param[paramName];
                }
                window.open(url);
            },
            /*
             *    本地存储
             */
             storage:{
                set:function (key, jsonObj, scope) {
                    if (jsonObj != 0) {
                        if (jsonObj === 'undefined' || jsonObj === 'null' || !jsonObj) {
                            app.alert('JSON格式不对！');
                            return false;
                        }
                    }
                    if(scope == 'local') {
                        window.localStorage.setItem(key, JSON.stringify(jsonObj));
                    }
                    window.sessionStorage.setItem(key, JSON.stringify(jsonObj));
                },
                get: function(key, scope) {
                    var strObj = scope == 'local'?window.localStorage.getItem(key):window.sessionStorage.getItem(key);
                    if (strObj === 'undefined' || strObj === 'null' || !strObj) {
                        strObj = '{}';
                    }
                    return JSON.parse(strObj);
                },
                remove: function(key, scope) {
                    scope == 'local'?window.localStorage.removeItem(key):window.sessionStorage.removeItem(key);
                }
            },
            cookie: {

            },

            /*
             * 表格分页
             *
             * */
        getTable:function(param,succBack,pageCount){
                if(typeof(pageCount)=='undefined'){
                    pageCount=10;
                }
                var initialParams = {
                    count: pageCount
                };
                var initialSettings = {
                    counts: [],
                    paginationMaxBlocks: 5,
                    paginationMinBlocks: 2,
                    dataset: param
                };
                var array=[];
                array.push(initialParams);
                array.push(initialSettings);
                succBack(array);
            },
            /**
             * 特殊字符转义
             * @param  {[type]} string [description]
             * @return {[type]}        [description]
             */
             replaceSpecialChar: function (string) {
                string = string.replace(new RegExp(/(&)/g),'&amp;');
               // string = string.replace(new RegExp(/()/g),'&nbsp;');
               string = string.replace(new RegExp(/(<)/g),'&lt;');
               string = string.replace(new RegExp(/(>)/g),'&gt;');
               string = string.replace(new RegExp(/(")/g),'&quot;');
               string = string.replace(new RegExp(/(©)/g),'&reg;');
               string = string.replace(new RegExp(/(™)/g),'™');
               string = string.replace(new RegExp(/(×)/g),'&times;');
               string = string.replace(new RegExp(/(÷)/g),'&divide;');
               return string;
           },
        /**
         * select2 多选控件数据初始化
         * @param  {[type]} scopeConfig [select2 的config]
         * @param  {[type]} data        [数据源]
         * @param  {[type]} dataId      [对应select2数据的ID字段]
         * @param  {[type]} dataText    [对应select2数据的text字段]
         * @return {[type]}             [description]
         */
         selectInitData: function(scopeConfig, data,dataId, dataText) {
            for (var i = 0; i < data.length; i++) {
                var item = {'id': data[i][dataId], 'text': data[i][dataText]};
                scopeConfig.data[i] = item;
            }
        }

    }
})
.factory("app",function($http, $route, $location){
    return {
        /**
         * 弹出确认窗口
         */
         confirm:function ( title, message, execute, param, i){
            var btn = ['确定', '取消'];
            var set = {
                btn: btn,
                icon: i,
                title:title
            };
            layer.confirm(message, set, function() {
                layer.closeAll('dialog');
                execute(param);
            });
        },
        confirmLogout:function ( title, message, execute, param, i){
            var btn = ['确定'];
            var set = {
                btn: btn,
                icon: i,
                title:title
            };
            layer.confirm(message, set, function() {
                layer.closeAll('dialog');
                execute(param);
            });
        },
       /* confirm:function ( title, message, execute, param){
            $("#modal_confirm #myModalLabel").text(title);
            $("#modal_confirm #modalBody").text(message);
            $("#modal_confirm #confirmBtn").unbind().one("click",param,function(){
                execute(param);
            });
            $("#modal_confirm").modal("show");
        },*/
        /**
         * 定时提示窗口
         */
         alert:function(message){
            layer.alert(message, {
                icon: 2,
                time:2000,
                btn:[]
            });
        },
        success:function(message){
            layer.alert(message, {
                icon: 1,
                time:2000,
                btn:[]
            });
        },
        /**
         * 定时提示浮窗
         */
         systemAlert:function (type,message,callTitle,execute) {
            //type:success,error,warning
            $("#alert-pop").show();
            $("#hide").unbind().one("click", function () {
                $('#alert-pop').stop(true).animate({top:40,opacity:0 });
            })
            if(type == "success") {
                $("#alert-pop").removeClass("alert-danger alert-warning").addClass("alert-success");
            }
            if(type == "error") {
                $("#alert-pop").removeClass("alert-success alert-warning").addClass("alert-danger");
            }
            if(type == "warning") {
                $("#alert-pop").removeClass("alert-success alert-danger").addClass("alert-warning");
            }
            $("#alert-pop").animate({top:90,opacity:1 });
            $('#alert_message').text(message);
            $('#go_page_alert').text(callTitle).unbind().one("click", function() {
                execute();
            })
            if(callTitle == undefined || callTitle == '') {
                $("#stay_page_alert").remove();
            }
            if(execute == undefined || execute == '') {
                $("#stay_page_success").remove();
            }
            setTimeout("$('#alert-pop').stop(true).animate({top:40,opacity:0 });", 5000);
        },
        /**
         * 提示窗口,需确认
         */
         popup:function(message){
            layer.alert(message, {
                icon: 0,
                btn:['确定']
            });
        },

        reload:function(path) {
            if(path){
                $location.path(path);
            }
            $route.reload();
        }
    }
})
.factory('select2Query', function ($timeout) { // select2 Factory 
    return {
        testAJAX: function () {
            var config = {
                minimumInputLength: 1,
                ajax: {
                    url: "http://api.rottentomatoes.com/api/public/v1.0/movies.json",
                    dataType: 'jsonp',
                    data: function (term) {
                        return {
                            q: term,
                            page_limit: 10,
                            apikey: "ju6z9mjyajq2djue3gbvv26t"
                        };
                    },
                    results: function (data, page) {
                        return {results: data.movies};
                    }
                },
                formatResult: function (data) {
                    return data.title;
                },
                formatSelection: function (data) {
                    return data.title;
                }
            };

            return config;
        }
    }
})
.directive('select2', function (select2Query) { // select2 Directive 
    return {
        restrict: 'A',
        scope: {
            config: '=',
            ngModel: '=',
            select2Model: '='
        },
        link: function (scope, element, attrs) {
            // 初始化
            var tagName = element[0].tagName,
            config = {
                allowClear: true,
                multiple: !!attrs.multiple,
                    placeholder: attrs.placeholder || ' '   // 修复不出现删除按钮的情况
                };

            // 生成select
            if(tagName === 'SELECT') {
                // 初始化
                var $element = $(element);
                delete config.multiple;

                angular.extend(config, scope.config);
                $element
                .prepend('<option value=""></option>')
                .val('')
                .select2(config);

                // model - view
                scope.$watch('ngModel', function (newVal) {
                    setTimeout(function () {
                        $element.find('[value^="?"]').remove();    // 清除错误的数据
                        $element.select2('val', newVal);
                    },0);
                }, true);
                return false;
            }

            // 处理input
            if(tagName === 'INPUT') {
                // 初始化
                var $element = $(element);

                // 获取内置配置
                if(attrs.query) {
                    scope.config = select2Query[attrs.query]();
                }

                // 动态生成select2
                scope.$watch('config', function () {
                    angular.extend(config, scope.config);
                    $element.select2('destroy').select2(config);
                }, true);

                // view - model
                $element.on('change', function () {
                    scope.$apply(function () {
                        scope.select2Model = $element.select2('data');
                    });
                });

                // model - view
                scope.$watch('select2Model', function (newVal) {
                    $element.select2('data', newVal);
                }, true);

                // model - view
                scope.$watch('ngModel', function (newVal) {
                    // 跳过ajax方式以及多选情况
                    if(config.ajax || config.multiple) { return false }

                        $element.select2('val', newVal);
                }, true);
            }
        }
    }
})
.directive('onFinishRenderFilters', ['$timeout', function ($timeout) {
    return {
        restrict: 'A',
        link: function(scope,element,attr) {
            if (scope.$last === true) {
                $timeout(function() {   // 最后一条渲染结束
                    var finishFunc = scope.$parent[attr.onFinishRenderFilters]; // 要执行的方法在 controller中定义
                    if(finishFunc) {
                        finishFunc();
                    }
                });
            }
        }
    };
}])
.filter('trustHtml', function ($sce) { //解释符合条件的html标签
    return function (input) {
     return $sce.trustAsHtml(input);
 }
});




