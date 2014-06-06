
shiro.forgot = false;

$(document).ready(function() {

    $("#registration-form").validate({
      errorPlacement: function(error, element) {
           error.insertAfter(element);
      }    });

    $("#passwordSetForm").validate({
      errorPlacement: function(error, element) {
           error.insertAfter(element);
      },
      rules: {
          password: {
              minlength: 6
          },
          checkPassword: {
              equalTo: "#password"
          }
      }
    });

    // stage1
    $("#registration-form").submit(function(e) {
        e.preventDefault();
        var form = $(this),
        username = form.find("input[name='username']").val(),
        displayName = form.find("input[name='displayName']").val(),
        proofOfMembershipCode = form.find("input[name='proofOfMembershipCode']").val();
        if (form.valid()) {
            $.ajax(form.attr( 'action' ), {
                type: "POST",
                data : {
                    username: username,
                    displayName: displayName,
                    proofOfMembershipCode: proofOfMembershipCode,
                    forgot : shiro.forgot
                },
                dataType: "json",
                success: function(data, status) {
                    if (status == 'success') {
                        $("#username").attr("disabled", true);
                        $("#stage1").hide();
                        $("#stage2").show();
                    } else {
                    	 bootbox.alert("register failed: " + data.message);
                        reset();
                    }
                },
                error: function(xhr) {
                	 bootbox.alert("submission failed: " + xhr.responseJSON.message);
                }
            });
        }
        return false;
    });
    // stage2
    $("#passwordSetForm").submit(function(e) {
        e.preventDefault();
        var form = $(this),
            username = $("#username").val(),
            code = form.find("#confirmCode").val().trim(),
            password = form.find("#password").val();
        if (form.valid()) {
            $.ajax(form.attr("action"), {
                type: "POST",
                data: {
                    code: code,
                    username : username,
                    password: password,
                    forgot : shiro.forgot
                },
                dataType: "json",
                success: function(data, status) {
                    if (status == 'success') {
                        $("#stage3").show();
                        $("#resetButton").hide();
                        bootbox.alert("Confirmation was successful", function(){
                        	location.href = "/";
                        });
                    }  else {
                    	 bootbox.alert("confirm failed: " + data.message);
                        reset();
                    }
                },
                error: function(xhr) {
                	 bootbox.alert("submission failed: " + xhr.responseText);
                }
            });
        }
        return false;
    });
    function reset() {
        $("#stage2").hide();
        $("#resetButton").show();
        $("#stage3").hide();
        $("#username").attr("disabled", false);
    }
});