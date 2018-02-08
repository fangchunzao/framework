angular.module('myApp.indexModule',[])
    .controller('indexController',['$scope','$http', '$location','$window','$interval','NgTableParams','dataService','app', function ($scope,$http,$location,$window,$interval,NgTableParams,dataService,app) {
        /*init start*/

    }])
    .controller('selectController',['$scope','$http', '$location','$window','$interval','dataService','app', function ($scope,$http,$location,$window,$interval,dataService,app) {
        /*init start*/

        $scope.selectInit = function () {
            $scope.selectConfig = {
                data: [],
                placeholder: '多选加载'
            };
            $scope.loadSelect();
        };

        $scope.loadSelect = function () {
            var successCallBack = function() {
                var res = [{id:1,text:'选项1'},{id:2,text:'选项2'},{id:3,text:'选项3'},{id:4,text:'选项4'}];
                dataService.selectInitData($scope.selectConfig, res, "id", 'text');
            };
            successCallBack();
            //dataService.go(dataService.interfaces, dataService.methods.get, param, successCallBack);
        };

        $scope.showSelect = function () {
           console.log($scope.selectModel);
           var param = {};
        };


    }])
    .controller('tableController',['$scope','$http', '$location','$window','$interval','dataService','NgTableParams','app', function ($scope,$http,$location,$window,$interval,dataService,NgTableParams,app) {
        $scope.ngTableInit = function () {
            var data = [
                {value1:'value1',value2:'value2'},
                {value1:'value1',value2:'value2'},
                {value1:'value1',value2:'value2'}
            ];
            var succBack = function(res) {
                $scope.tableParams = new NgTableParams(res[0], res[1]);
            };
            dataService.getTable(data, succBack);
         /*   var successCallBack = function() {
                var data = [{value1:'value1',value2:'value2'},{value1:'value1',value2:'value2'},{value1:'value1',value2:'value2'}];
                var succBack = function(res) {
                    $scope.ngTable = new NgTableParams(res[0], res[1]);
                };
                dataService.getTable(data, succBack);
            };
            successCallBack();*/
            //dataService.go(dataService.interfaces, dataService.methods.get, param, successCallBack);
        }
    }]);

