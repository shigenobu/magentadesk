$(function(){
    $('input[type=submit].btn-json').click(function (e) {
        e.preventDefault();

        const btn = $(this);
        const form = btn.parents("form");
        const url = form.attr('action') + '?' + form.serialize();

        let w = Math.floor(window.innerWidth * 0.5);
        let h = Math.floor(window.innerHeight * 0.8);

        $('#dialog-content').css("width", w * 0.9);
        $('#dialog-content').css("height", h * 0.9);
        $('#dialog-content').css("overflow", "auto");

        $('#dialog').dialog({
            title: 'JSON',
            open: function (event, ui) {
                $(".ui-widget-overlay").css({
                    opacity: 0.6,
                    backgroundColor: "black"
                });
                $(".ui-dialog-content").css({
                    overflow: "hidden"
                });
                $('#dialog-content').load(url);
            },
            modal: true,
            width: w,
            height: h
        });
    });
});