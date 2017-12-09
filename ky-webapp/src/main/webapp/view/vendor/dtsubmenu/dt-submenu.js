$(document).on("click", ".dtsubmenu-btn", function() {
    if ($(this).hasClass("hide")) {
        $(this).show();
    } else {
        $(this).hide();
    }

    $(this).parent(".dt-submenu").addClass("dt-dropdown");
})

$(document).on("mouseleave", ".menu-top", function() {
    $(this).parent(".dt-submenu").removeClass("dt-dropdown");
    $(this).parent(".dt-submenu").find(".dtsubmenu-btn").show();
})


