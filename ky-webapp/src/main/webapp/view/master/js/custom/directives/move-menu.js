App.directive("dtDeptSelect",function(){
    return {
        restrict:'EA',
        template:"<div class='wrapper'><a class='tb-left-disabled'><span><</span></a><a class='tb-right-disabled'><span>></span></a><div class='content'><ul class='td-content'><li></li></ul></div>",
        replace:true,
        scope:{
            output:'=',
            isRemoveAllDepartment: '=',
            departmentId:'=',
            departmentTree:'=',
            selectChange:'&',
            selectLoadSuccess:'&',
            timeSlot:'@'
        },
        link: function(scope, element, attrs) {
            moveImages();

            function moveImages() {
                var $as = $('.wrapper a');
                var $backwardA = $as.first();
                var $forwardA = $as.last();
                var $listUl = $('#vedio .td-content');
                var $ulWrapper = $('#vedio .content');

                //设置$listUl的宽度
                var imgCount = $listUl.children().length;
                $listUl.css('width', 80*imgCount);
                var listUlWidthStr = $listUl.css('width');
                var ulWrapperWidthStr = $ulWrapper.css('width');
                var listUlWidth = listUlWidthStr.substring(0, listUlWidthStr.indexOf('px')-1) * 1;
                var ulWrapperWidth = ulWrapperWidthStr.substring(0, ulWrapperWidthStr.indexOf('px')-1) * 1;
                if(listUlWidth > ulWrapperWidth) {
                    $forwardA.attr('class', 'tb-right');
                }

                var moveCount = 0;
                //绑定监听
                $forwardA.click(function () {
                    //到最右边后不能再向左移动
                    if(this.className==='tb-right-disabled') {
                        return;
                    }
                    moveCount++;
                    $listUl.css('left', -80*moveCount);
                    var rightStr = $listUl.css('right');
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
                    $listUl.css('left', -80*moveCount);
                    var leftStr = $listUl.css('left');
                    var left = leftStr.substring(0, leftStr.indexOf('px')-1) * 1;
                    if(left >= 0) {
                        this.className = 'tb-left-disabled';
                    }

                    $forwardA.attr('class', 'tb-right');
                });


            }
        }
    }
});