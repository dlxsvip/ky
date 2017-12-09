App.directive('tipImg', function() {
    return {
        restrict: 'A',
        replace: true,
        scope: {
            tipImg:'@'},
        link: function(scope, element, attrs) {
            $(element).mouseenter(function () {
                var value = $(this).attr("tip-img");
                var tooltip_class = '<img class="tip-img"/>';
                $(this).parent().append(tooltip_class);
                $(this).parent().find(".tip-img").attr('src', value);
                var tooltipHeight = $(".tip-img").height();
                $(".tip-img").css({
                    "top": $(this).position().top - tooltipHeight + $(this).height(),
                    "left": $(this).position().left + $(this).width() + 10,
                    "cursor": "text"
                });

                $(".tip-img").click(function(event){
                    event.preventDefault();
                    return false;
                });

            });

            $(element).mouseleave(function () {
                $(this).parent().find(".tip-img").remove();
            });
        }
    };
});