if (window.console) {
  console.log("Welcome to Employees sample with Scala/Play/ScalikeJDBC!");
}

var app = angular.module('app', [], function(){});

app.directive('focusOn', function() {
    return function(scope, elem, attr) {
        scope.$on('focusOn', function(e, name) {
            if(name === attr.focusOn) {
                elem[0].focus();
            }
        });
    };
});

app.factory('focus', function ($rootScope, $timeout) {
    return function(name) {
        $timeout(function (){
            $rootScope.$broadcast('focusOn', name);
        });
    }
});

app.controller('appController', ['$scope', '$http', 'focus', function($scope, $http, focus) {
    $scope.page = "";
    $scope.data = [];
    $scope.newRow = -1;
    $scope.depts = [];

    $scope.load = function(page) {
        $scope.page = page;
        if (_INIT_DATA) {
            $scope.data = _INIT_DATA;
            if (page == "emp") {
                $scope.depts = _INIT_DATA_DEPTS;
                $scope.titles = _INIT_DATA_TITLES;
            }
        } else {
            $scope.data = [];
        }
    }

    $scope.add = function() {
        if ($scope.newRow >= 0) {
            alert("please save current row");
            focus("name");
            return;
        }
        $scope.data.push({id: 0, editable: true});
        $scope.newRow = $scope.data.length -1;
        focus("name");
    }

    $scope.delete = function(idx) {
        var e = $scope.data[idx];
        if (e.editable == true) {
            e.editable = false;
            if (!e.id || e.id <= 0) {
                $scope.data.splice(idx, 1);
                $scope.newRow = -1;
            }
            return;
        } else if (e.id > 0) {
            if (!confirm("Do you want to delete " + $scope.page + "?")) {
                return
            }
            $http.delete('/' + $scope.page + '/' + e.id, {}).then(
                function () {
                    $scope.data.splice(idx, 1);
                },
                function () {
                    alert($scope.page + " could not be deleted");
                }
            );
        }
    };

    $scope.edit = function(idx) {
        $scope.data[idx].editable = true;
        focus("name")
    }

    $scope.save = function(idx) {
        var e = $scope.data[idx];
        if (e.id > 0) {
            $http.put('/' + $scope.page  + '/' + e.id, e).then(
                function (resp) {
                    $scope.data[idx] = resp.data;
                },
                function (resp) {
                    alert($scope.page + " could not be updated")
                }
            );
        } else {
            $http.post('/' + $scope.page, e).then(
                function(resp){
                    $scope.data[idx] = resp.data;
                    $scope.newRow = -1;
                },
                function(resp){
                    alert($scope.page + " could not be created")
                }
            );
        };
    };

    $scope.getDepts = function() {
        $http.get('/dept/lookup', {}).then(
            function(resp) {
                $scope.depts = resp.data;
            },
            function(resp) {}
        );
    };

    $scope.trStyleClass = function(e) {
        return (e.editable == true) ? "edit-row" : "";
    }
}]);
