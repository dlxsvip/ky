App.directive('pieceTimeFlash', function($timeout, urlService, toaster, $http, formatTime) {
    return {
        restrict: 'EA',
        template: "<div class='listWrapper'>" +
        "<span>时间段</span>\n"+
        "<div class='timeWrapper'>\n" +
        "<ul class='td-content'>\n" +
        "<li ng-repeat=\"time in timeList track by $index\"  ng-click=\"limitPlay(time, $index)\">{{time.timePiece}}</li>\n" +
        "</ul>\n" +
        "</div>\n"+
        "</div>",
        transclude: true,
        replace: true,
        scope: {
            timeList: '=',
            vedioText: '=',
            vedioKeyword:"=",
            videoId:'=',
            typeRequest:'&'
        },
        link: function(scope, element, attrs) {

            var myVideo = $(element[0]).parent().siblings().children();
            var vedio = document.getElementById(attrs.typeRequest);

            var vedioHeight = myVideo.height();
            $('.timeWrapper').css('height', vedioHeight - 30);
            $('.vedioInfo').css('height', vedioHeight);

            vedio.addEventListener("loadedmetadata", function () {
                var height = $(this).parent().height();
                $('.timeWrapper').css('height', height - 34);
                $('.vedioInfo').css('height', height - 5);
            });


            $(window).resize(function() {
                var height = $(vedio).parent().height();
                $('.timeWrapper').css('height', height - 34);
                $('.vedioInfo').css('height', height - 5);
            });


            //播放
            function play(){
                vedio.play();
            }

            //暂停
            function pause(){
                vedio.pause();
            }


            //设置播放点
            function playBySeconds(num){
                vedio.currentTime = num;
                play();
            }

            var videoId;

            scope.$watch("videoId", function(value){
                if(value !== undefined){
                    videoId = value;
                }
            })

            scope.limitPlay = function(time, index){
                $timeout.cancel(scope.timer);

                $('.timeWrapper li').removeClass('time-active');
                $($('.timeWrapper li')[index]).addClass('time-active');
                var aTime = time.timePiece.split('-');
                var timeStrFirst = formatTime.formatToSeconds(aTime[0]);
                var timeStrLast = formatTime.formatToSeconds(aTime[1]);
                var timeQuantum = ((timeStrLast-timeStrFirst) *1 +1)*1000;

                playBySeconds(timeStrFirst);
                scope.timer = $timeout(function(){
                    pause();
                },timeQuantum);

                var getUrl;
                if(attrs.typeRequest === 'keyword'){
                    getUrl = urlService.getFullUrl('/keywordAnalysis/queryTimeBucketText?analysisId=' + time.id);
                }else if(attrs.typeRequest === 'keyPerson'){
                    getUrl = urlService.getFullUrl('/keyPersonAnalysis/queryTimeBucketText?videoId=' + videoId + '&analysisId=' + time.id);
                }

                $http.get(getUrl).success(function(data) {
                    if(data.result == "success"){
                        var vedioText = {
                            text:data.data.text
                        }
                        if(data.data.keywords.length != 0){
                            var vedioKeyword = data.data.keywords;
                            for(item in vedioKeyword){
                                var sKey = vedioKeyword[item];
                                var reg = new RegExp(sKey, 'gi');
                                vedioText.text = vedioText.text.replace(reg, "<span class='high-lighted'>"+sKey+"</span>");
                            }
                            scope.vedioKeyword = data.data.keywords.join(",");
                        }else{
                            scope.vedioKeyword = '';
                        }
                        scope.vedioText = vedioText.text;
                    } else{
                        toaster.pop('error','失败',data.errorMsg,data.errorDetail,error_timeout);
                    }
                }).error(function() {
                    toaster.pop('error','失败',data.errorMsg,data.errorDetail,error_timeout);
                });
            }


            scroll()
            //moveTime();

            function scroll(){

                var $listUl = $('.timeWrapper .td-content');
                var timeCount;
                scope.$watch("timeList", function(value){
                    if(value !== undefined){
                        timeCount = value.length;
                        setClickable(timeCount);
                    }
                })

                function setClickable(timeCount){
                    $listUl.css('height', 40*timeCount);
                }
            }

            function moveTime() {
                var $as = $('.timeWrapper a');
                var $backwardA = $as.first();
                var $forwardA = $as.last();
                var $listUl = $('.timeWrapper .td-content');
                var $ulWrapper = $('.timeWrapper .content');

                //设置$listUl的宽度
                var timeCount;
                scope.$watch("timeList", function(value){
                    if(value !== undefined){
                        timeCount = value.length;
                        setClickable(timeCount);
                    }
                })

                function setClickable(timeCount){
                    $listUl.css('height', 40*timeCount);
                    var listUlHeightStr = $listUl.css('height');
                    var ulWrapperHeightStr = $ulWrapper.css('height');
                    var listUlHeight = listUlHeightStr.substring(0, listUlHeightStr.indexOf('px')) * 1;
                    var ulWrapperHeight = ulWrapperHeightStr.substring(0, ulWrapperHeightStr.indexOf('px')) * 1;
                    if(listUlHeight > ulWrapperHeight) {
                        $forwardA.attr('class', 'tb-right');
                    }
                }

                var moveCount = 0;
                //绑定监听
                $forwardA.click(function () {
                    //到最右边后不能再向左移动
                    if(this.className==='tb-right-disabled') {
                        return;
                    }
                    moveCount++;
                    $listUl.css('top', -40*moveCount);
                    var rightStr = $listUl.css('bottom');
                    var right = rightStr.substring(0, rightStr.indexOf('px')-1) * 1;
                    if(right >= 0) {
                        $forwardA.attr('class', 'tb-right-disabled');
                    }

                    $backwardA.attr('class', 'tb-left');
                });

                $backwardA.click(function () {
                    if(this.className==='tb-left-disabled') {
                        return;
                    }
                    moveCount--;
                    $listUl.css('top', -40*moveCount);
                    var leftStr = $listUl.css('top');
                    var left = leftStr.substring(0, leftStr.indexOf('px')-1) * 1;
                    if(left >= 0) {
                        this.className = 'tb-left-disabled';
                    }

                    $forwardA.attr('class', 'tb-right');
                });


            }
        }
    };
});

