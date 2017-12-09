App.controller('realtimeDataCtrl', ['$scope', '$location', 'formatTime', 'urlWs', 'urlService', '$websocket',
    function ($scope, $location, formatTime, urlWs, urlService, $websocket) {
        'use strict';

        var ws = $websocket(urlWs.getWsUrl());
        var resultData = [];

        ws.onOpen(function() {
            // Web Socket 已连接上，使用 send() 方法发送数据

            var date = {
                method:"keyList",
                param:{

                }
            };
            ws.send(date);
            console.log("ws连接已打开...");
        });

        ws.onMessage(function (evt) {
            //var received_msg = evt.data;
            var msg = JSON.parse(evt.data);
            var name = msg.name;
            if(msg.type == "word"){
                msg.type = "关键词";
                var reg = new RegExp(name, 'gi');
                msg.text = msg.text.replace(reg, "<span class='high-lighted'>"+name+"</span>");
            }else if(msg.type == "person") {
                msg.type = "关键人";
            }


            msg.startTime = formatTime.formatSeconds(msg.startTime);
            msg.endTime = formatTime.formatSeconds(msg.endTime);
            resultData.unshift(msg);
        });

        $scope.resultData = resultData;

        $scope.$on('$destroy', function(){
            ws.close({force: true})
        });

        // 导出实时数据
        $scope.exportRealTimeData = function () {
            var url = urlService.getFullUrl('/captureChannel/exportRealTimeData');
            if (navigator.userAgent.indexOf("Firefox") > -1) {
                window.open(url);
            } else {
                window.location.href = url;
            }
        }

        // 导出历史数据
        $scope.exportHistoryData = function () {
            var url = urlService.getFullUrl('/captureChannel/exportHistoryData');
            if (navigator.userAgent.indexOf("Firefox") > -1) {
                window.open(url);
            } else {
                window.location.href = url;
            }
        }

    }
]);