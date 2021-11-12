$(function() {
    $('a.btn-delete').click(function (e) {
        e.preventDefault();

        const btn = $(this);
        const form = btn.parents("form");
        const url = btn.attr('href');

        $('#loading').show();
        $.ajax({
            url: url,
            type: 'DELETE'
        }).
        done(function(data, status, xhr) {
            console.log(xhr.responseText)
            try {
                form.prepend('<div class="success">DELETED</div>');
                let obj = JSON.parse(xhr.responseText);
                if (obj.hasOwnProperty('location') && obj.location != '') {
                    btn.parent().hide();
                    btn.delay(1500).queue(function () {
                        location.href = obj.location;
                    });
                }
            } catch (e) {
                console.log(e)
                alert("error")
            }
        }).
        fail(function (xhr, status, err) {
            console.log(xhr.responseText);
            try {
                let obj = JSON.parse(xhr.responseText);
                if (obj.hasOwnProperty('message') && obj.message != '') {
                    alert(obj.message);
                }
            } catch (e) {
                console.log(e)
                alert("error")
            }
        }).
        always(function () {
            $('#loading').hide();
            $("html,body").animate({scrollTop:0}, "300");
        });
    });
});