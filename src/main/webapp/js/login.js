/*
    https://www.sitepoint.com/9-javascript-libraries-working-with-local-storage/
*/
	$(function() {
	    $("#loginForm").submit(function(event) {
	        event.preventDefault();

	        $.ajax({
	            type: "POST",
	            url: "/web/oauth/token",
	            beforeSend: function (xhr) {
                    xhr.setRequestHeader ("Authorization", "Basic " + btoa("trusted-app:secret"));
                },
	            data: {
                      username: $('#username').val(),
                      password: $('#password').val(),
                      expires_in: 0,
                      grant_type: "password"
                },
                async: true,
	            dataType: "json",
	            success: function(json, textStatus, jqXHR) {
                    Lockr.set('access_token', json.access_token);
                    Lockr.set('refresh_token', json.refresh_token);
                    window.location.href = "/web/secure/view/batchschedule?access_token=" + json.access_token;
	            },
	            error: function(jqXHR, textStatus, errorThrown) {
	                var responseText = jQuery.parseJSON(jqXHR.responseText);
	                alert(responseText.message);
	                console.log(responseText);
	            }
	        });
	    });
	});