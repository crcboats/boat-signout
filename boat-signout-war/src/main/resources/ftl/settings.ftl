<#assign style="mainstyle.css">
<!DOCTYPE html>
<html lang="en" class="shiro-none-active">
<head>
    <#include "inc/_head.ftl">
</head>

<body>

<div id="spinner" class="shiro-unset" style="position: absolute; top: 90px; left: 50%;">
</div>

<#include "inc/topbar.ftl">

<div class="container">


	            <div class="page-header">
	                <h1><small>Change your password</small></h1>
	            </div>
                <div class="row">
                   <div class="span12">
                        <form id="settingsForm" action="${userBaseUrl}/settings" method="post" autocomplete="off">
                            <fieldset>
                                <legend>Change your password</legend>
                                <div class="clearfix">
                                    <label for="email">Email Address</label>

                                    <div class="input">
                                        <input class="xlarge" id="email" name="email" value="${username!''}"
                                               size="30" type="text" disabled/>
                                    </div>
                                </div>
                                
                                <!-- /clearfix -->
                                <div class="clearfix">
                                    <label for="password">Password</label>

                                    <div class="input">
                                        <input class="xlarge" id="password" name="password" size="30" type="password" autocomplete="off" value=""/>
                                    </div>
                                </div>
                                <!-- /clearfix -->
                                <div class="clearfix">
                                    <label for="checkPassword">Retype Password</label>
                                    <div class="input">
                                        <input class="xlarge" id="checkPassword" name="checkPassword" size="30"
                                               type="password" autocomplete="off" value=""/>
                                    </div>
                                </div>
                                <!-- /clearfix -->
                            </fieldset>
                            <div class="actions">
                                <button type="submit" class="btn primary">Update</button>
                            </div>
                        </form>
                    </div>
                </div>
                <div class="row" id="stage3" style="display:none">
                    <div class="span12">
                      <p>Your settings have been saved</p>
                    </div>
                </div>
        </section>
    </div>

<#include "inc/footer.ftl">
</div>

<#include "inc/_foot.ftl">
<script>
$(document).ready(function() {
    
    
    $("#settingsForm").validate({
      rules: {
        password: {
            minlength: 6
        },
        checkPassword: {
          equalTo: "#password"
        }
      },
      errorPlacement: function(error, element) {
           error.insertAfter(element);
      }
    });
    $("#settingsForm").submit(function(e) {
        e.preventDefault();
        var form = $(this),
            username = $("#username").val(),
            password = $("#password").val();
            displayName = $("#displayName").val();
        if (form.valid()) {
            $.ajax(shiro.userBaseUrl+"/settings", {
                type: "POST",
                data: {
                    username : username,
                    displayName: displayName,
                    password: password
                },
                dataType: "json",
                success: function(data, status) {
                    if (status == 'success') {
                    	shiro.status.clearStatus();
                        $("#stage3").show();
                    }  else {
                        bootbox.alert("settings failed: " + data.message);
                    }
                },
                error: function(xhr) {
                    bootbox.alert("settings failed: " + xhr.responseText);
                }
            });
        }
        return false;
    });
});
</script>


</body>
</html>