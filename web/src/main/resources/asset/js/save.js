$(function() {
    $('input[type=submit].btn-insert, input[type=submit].btn-update').click(function (e) {
        e.preventDefault();
        e.target.form.querySelectorAll('input').forEach(e => e.setCustomValidity(""));

        const invalid = e.target.form.querySelectorAll('input:invalid');
        invalid.forEach(e => {
                e.setAttribute("style", "border: ridge medium red");
        });

        if (invalid.length > 0){
            return false;
        }

        const btn = $(this);
        const form = btn.parents("form");
        const url = form.attr('action');

        $('#loading').show();
        $.ajax({
            url: url,
            type: 'POST',
            data: form.serialize()
        }).
        done(function(data, status, xhr) {
            console.log(xhr.responseText)
            try {
                form.prepend('<div class="success">SAVED</div>');
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
