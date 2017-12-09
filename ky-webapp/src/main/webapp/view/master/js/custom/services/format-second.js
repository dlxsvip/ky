App.service('formatTime', ['$http', '$location', function ($http, $location) {

    var formatTime = {}
    function formatSeconds(value) {
        var secondTime = parseInt(value);// 秒
        var minuteTime = 0;// 分
        var hourTime = 0;// 小时
        if(secondTime > 60) {
            minuteTime = parseInt(secondTime/60);
            secondTime = secondTime%60;
            if(minuteTime > 60) {
                hourTime = parseInt(minuteTime/60);
                minuteTime = minuteTime%60;
            }
        }
        secondTime = secondTime>9?secondTime:"0"+secondTime;
        var result = ""+secondTime;

        minuteTime = minuteTime>9?minuteTime:("0"+minuteTime);
        result = minuteTime+":"+result;

        hourTime = hourTime>9?hourTime:("0"+hourTime);
        result = ""+hourTime+":"+result;

        return result;
    }
    function formatToSeconds(value) {
        var time = value.split(':');
        var result = '';

        if(time[0].substring(0,1) == 0){
            result = time[0].substring(1) * 3600;
        }else{
            result = time[0] * 3600
        }

        if(time[1].substring(0,1) == 0){
            result = result + time[1].substring(1) * 60;
        }else{
            result = result + time[1] * 60
        }

        if(time[2].substring(0,1) == 0){
            result = result + time[2].substring(1) * 1;
        }else{
            result = result + time[2] * 1
        }


        return result;
    }

    function formatSecondsList (timeList){
        for(var i = 0; i < timeList.length; i++){
            var startTime = formatTime.formatSeconds(timeList[i].startTime);
            var endTime = formatTime.formatSeconds(timeList[i].endTime);
            var timeStr = startTime + "-" + endTime;
            timeList[i].timePiece = timeStr;
        }
        return timeList;
    }

    formatTime.formatSeconds = formatSeconds;
    formatTime.formatToSeconds = formatToSeconds;
    formatTime.formatSecondsList = formatSecondsList;

    return formatTime;
}]);