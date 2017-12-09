App.directive('maxLenHidden', function() {
  return {
    restrict: 'A',
    replace: true,
    scope: { maxLenHidden: '@',
              tipValue:'@'},
    link: function(scope, element, attrs) {
      //scope.$watch('maxLenHidden', function(value) {
      //  console.log(value);
      //  if (parseInt(value, 10) < 18) {
      //    //element.html('child');
      //  }
      //});
      //计算字符串总长度（汉字、全角算两个字符）
      function getStrLength(str) {
        var cArr = str.match(/[^\x00-\xff]/ig);
        if(null == cArr){
          var cArr_length = 0;
        }else{
          var cArr_length = cArr.length;
        }
        var strLength = str.length + (cArr == null ? 0 : cArr.length);
        return strLength;
      }
      //计算截取字符串截止的index
      function getShowIndex(str,maxLength) {
        var totalStrNum = 0;
        var aStr = str.split("");
        for(var i = 0; i < aStr.length; i++){
          if(totalStrNum < maxLength) {
            if (aStr[i].match(/[^\x00-\xff]/ig) == null) {
              totalStrNum++;
            } else {
              totalStrNum += 2;
            }
          }else{
            return i;
          }
        }
      }
      var maxLengthtemp = parseInt(attrs.maxLenHidden, 10);
      if (maxLengthtemp < getStrLength(attrs.tipValue.toString())) {
        $(element).text(attrs.tipValue.toString().substr(0, getShowIndex(attrs.tipValue.toString(),maxLengthtemp)) + "...");
        $(element).mouseenter(function () {
          var value = $(this).attr("tip-value");
          var tooltip_class = '<div class="tooltip-self right in">  <div class="tooltip-self-arrow"></div> <div class="tooltip-self-inner"></div></div>';
          $(this).append(tooltip_class);
          $(this).find(".tooltip-self-inner").text(value);
          var tooltipHeight = $(".tooltip-self", this).height();
          $(".tooltip-self", this).css({
            "top": $(this).position().top - (tooltipHeight / 2) + ($(this).height() / 2),
            "left": $(this).position().left + $(this).width(),
            "cursor": "text"
          });

          $(".tooltip-self", this).click(function(event){
            event.preventDefault();
            return false;
          });

          var href = "";
          $(".tooltip-self").mouseleave(function () {
            $(this).find(".tooltip-self").remove();
            if ($(this).parent().is("a")) {
              $(this).parent().prop("href", href);
            }
          }).mouseenter(function(){
            $tempLen = $(this).parent();
            if ($tempLen.is("a")) {
              href = $tempLen.prop("href");
              $tempLen.removeAttr("href");
            }
          });
        });

        $(element).mouseleave(function () {
          $(this).find(".tooltip-self").remove();
        });
      }
      scope.$watch('tipValue',function(){
        if (maxLengthtemp < getStrLength(attrs.tipValue.toString())){
          $(element).text(attrs.tipValue.toString().substr(0, getShowIndex(attrs.tipValue.toString(),maxLengthtemp)) + "...");
          $(element).mouseenter(function () {
            var value = $(this).attr("tip-value");
            var tooltip_class = '<div class="tooltip-self right in">  <div class="tooltip-self-arrow"></div> <div class="tooltip-self-inner"></div></div>';
            $(this).append(tooltip_class);
            $(this).find(".tooltip-self-inner").text(value);
            var tooltipHeight = $(".tooltip-self", this).height();
            $(".tooltip-self", this).css({
              "top": $(this).position().top - (tooltipHeight / 2) + ($(this).height() / 2),
              "left": $(this).position().left + $(this).width(),
              "cursor": "text"
            });

            $(".tooltip-self", this).click(function(event){
              event.preventDefault();
              return false;
            });

            var href = "";
            $(".tooltip-self").mouseleave(function () {
              $(this).find(".tooltip-self").remove();
              if ($(this).parent().is("a")) {
                $(this).parent().prop("href", href);
              }
            }).mouseenter(function(){
              $tempLen = $(this).parent();
              if ($tempLen.is("a")) {
                href = $tempLen.prop("href");
                $tempLen.removeAttr("href");
              }
            });
          });

          $(element).mouseleave(function () {
            $(this).find(".tooltip-self").remove();
          });
        }else {
          $(element).text(attrs.tipValue.toString());
        }
      });
    }
  };
});

App.directive('tipCopySelf', function() {
  return {
    restrict: 'A',
    replace: true,
    scope: { maxLenHidden: '@' },
    link: function(scope, element, attrs) {
      //scope.$watch('maxLenHidden', function(value) {
      //  console.log(value);
      //  if (parseInt(value, 10) < 18) {
      //    //element.html('child');
      //  }
      //});
      //var maxLengthtemp = parseInt(attrs.maxLenHidden, 10);
      //if (maxLengthtemp < attrs.tipValue.toString().length) {
        //$(element).text(attrs.tipValue.toString().substr(0, maxLengthtemp) + "...");
        $(element).mouseenter(function () {
          var value = $(this).attr("tip-copy-self");
          var tooltip_class = '<div class="tooltip-self right in">  <div class="tooltip-self-arrow"></div> <div class="tooltip-self-inner"></div></div>';
          $(this).append(tooltip_class);
          $(this).find(tooltip-self-inner).text(value);
          var tooltipHeight = $(".tooltip-self", this).height();
          $(".tooltip-self", this).css({
            "top": $(this).position().top - (tooltipHeight / 2) + ($(this).height() / 2),
            "left": $(this).position().left + $(this).width(),
            "cursor": "text"
          });

          $(".tooltip-self", this).click(function(event){
            event.preventDefault();
            return false;
          });

          var href = "";
          $(".tooltip-self").mouseleave(function () {
            $(this).find(".tooltip-self").remove();
            if ($(this).parent().is("a")) {
              $(this).parent().prop("href", href);
            }
          }).mouseenter(function(){
            $tempLen = $(this).parent();
            if ($tempLen.is("a")) {
              href = $tempLen.prop("href");
              $tempLen.removeAttr("href");
            }
          });
        });

        $(element).mouseleave(function () {
          $(this).find(".tooltip-self").remove();
        });
      }
    //}
  };
});

App.directive('modalMaxLenHidden', function() {
  return {
    restrict: 'A',
    replace: true,
    scope: { modalMaxLenHidden: '@' },
    link: function(scope, element, attrs) {
      //scope.$watch('maxLenHidden', function(value) {
      //  console.log(value);
      //  if (parseInt(value, 10) < 18) {
      //    //element.html('child');
      //  }
      //});
      /*判断字符串的长度，中文算2个，英文算1个*/
      function getStrLength(str) {
        var cArr = str.match(/[^\x00-\xff]/ig);
        if(null == cArr){
          cArr_length = 0;
        }else{
          cArr_length = cArr.length;
        }
        var strLengthInfo = {
          Strlength : str.length + (cArr == null ? 0 : cArr.length),
          cArrLength : cArr_length
        }
        return strLengthInfo;
      }
      /*截取maxLength长度的字符串，中文算两个，英文算一个*/
      function getElementText(subStr,maxLength){
        var templength = maxLength;
        if(subStr.length < maxLength){
          templength = subStr.length;
        }
        while(getStrLength(subStr).Strlength > maxLength){
          templength = templength-(getStrLength(subStr).Strlength - maxLength)/2-1;
          subStr = subStr.substr(0,templength);
        }
        return subStr;
      }

      var maxLengthtemp = parseInt(attrs.modalMaxLenHidden, 10);
      if (maxLengthtemp < getStrLength(attrs.tipValue.toString()).Strlength) {
        var subStr = attrs.tipValue.toString().substr(0,maxLengthtemp);
        $(element).text(getElementText(subStr,maxLengthtemp) + "...");
        $(element).mouseenter(function () {
          var value = $(this).attr("tip-value");
          var tooltip_class = '<div class="tooltip-self bottom in">  <div class="tooltip-self-arrow"></div> <div class="tooltip-self-inner"></div></div>';
          $(this).append(tooltip_class);
          $(this).find(".tooltip-self-inner").text(value);
          var tooltipHeight = $(".tooltip-self", this).height();
          $(".tooltip-self", this).css({
            "top": $(this).position().top + $(this).height()/2,
            "left": $(this).position().left,
            "cursor": "text"
          });

          $(".tooltip-self-inner",this).css({
            "max-width": "350px"
          });

          $(".tooltip-self", this).click(function(event){
            event.preventDefault();
            return false;
          });

          var href = "";
          $(".tooltip-self").mouseleave(function () {
            $(this).find(".tooltip-self").remove();
            if ($(this).parent().is("a")) {
              $(this).parent().prop("href", href);
            }
          }).mouseenter(function(){
            $tempLen = $(this).parent();
            if ($tempLen.is("a")) {
              href = $tempLen.prop("href");
              $tempLen.removeAttr("href");
            }
          });
        });

        $(element).mouseleave(function () {
          $(this).find(".tooltip-self").remove();
        });
      }
    }
  };
});