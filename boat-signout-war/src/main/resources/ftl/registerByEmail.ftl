<#assign style="mainstyle.css">
<#assign header="<h1>Register</h1>">
<#assign style="substyle.css">
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

    <div class="content">
        <section>
            <div class="page-header">
                ${header}
            </div>
            <div class="row">
                <div class="span12">
                    <form id="passwordSetForm" action="${userBaseUrl}/confirm" method="POST" autocomplete="off">
                        <fieldset>
                            <div class="input">
                                <input id="username" name="username" value="${(RequestParameters.username)!}" type="hidden"/>
                            </div>
                            <legend>Please fill in the password and the retype field</legend>
                            <div class="clearfix">
                                <label for="confirmCode">Code received</label>

                                <div class="input">
                                    <input class="required xlarge" id="confirmCode" name="confirmCode"
                                           size="40" type="text" value="${(RequestParameters.code)!}" disabled/>
                                </div>
                            </div>
                            <div class="clearfix">
                                <label for="password">Password</label>
                                <div class="input">
                                    <input class="required xlarge" id="password" name="password" size="30" type="password" autocomplete="off" value=""/>
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
                        <div class="actions" id="resetButton">
                            <#if (RequestParameters.forgot)??>
                              <button id="register" type="submit" class="btn primary">Reset</button>
                             <#else>
                               <button id="register" type="submit" class="btn primary">Register</button>
                            </#if>
                        </div>
                    </form>

                </div>
            </div>
            <div class="row" id="stage3" style="display:none">
                <div class="span12">
                <#if (RequestParameters.forgot)??>
                  <p>Your password has been reset</p>
                 <#else>
                   <p>You have been registered</p>
                </#if>
                </div>
            </div>
        </section>
    </div>
<#include "inc/footer.ftl">
</div>
</body>
<#include "inc/_foot_min.ftl">


<script src="js/register.js"></script>

<script>
    shiro.forgot = false;
    $(document).ready(function() {
        $("#passwordSetForm").submit(function() {
            shiro.status.clearStatus();
        });    
    });
</script>


</body>
</html>
