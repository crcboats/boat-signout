
<#if (RequestParameters.forgot)??>
    <#assign header="<h1>Change your password</h1>">
<#else>
    <#assign header="<h1>Register your account</h1>">
</#if>
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

    <div class="content">
        <section>
            <div class="page-header">
                ${header}
            </div>
            <div class="row">
               <div class="span12">
                    <form id="registration-form" action="${userBaseUrl}/register" method="POST" autocomplete="off">
                        <fieldset>
                        <#if (RequestParameters.forgot)??>
                            <div class="clearfix">
                                <label for="username">Email Address</label>

                                <div class="input">
                                    <input class="required email xlarge" id="username" name="username"
                                           size="50" type="text" autocomplete="off" value=""/>
                                </div>
                            </div>
                        <#else>
                            <div class="clearfix">
                                <label for="displayName">Name</label>

                                <div class="input">
                                    <input class="required xlarge" id="displayName" name="displayName"
                                           size="50" type="text" autocomplete="off" value=""/>
                                </div>
                            </div>
                            <div class="clearfix">
                                <label for="username">Email Address</label>

                                <div class="input">
                                    <input class="required email xlarge" id="username" name="username"
                                           size="50" type="text" autocomplete="off" value=""/>
                                </div>
                            </div>
                            <div class="clearfix">
                                <label for="proofOfMembershipCode">Proof of Membership Code</label>
                                <div class="input">
                                    <input class="required xlarge" id="proofOfMembershipCode" name="proofOfMembershipCode"
                                           size="50" type="text" autocomplete="off" value=" "/>
                                </div>
                            </div>
                        </#if>
                        </fieldset>
                        <div id="stage1" class="actions">
                        <#if (RequestParameters.forgot)??>
                            <button id="register" type="submit" class="btn primary">Change Password</button>
                        <#else>
                            <button id="register" type="submit" class="btn primary">Register</button>
                        </#if>
                        </div>
                    </form>
                </div>
            </div>
            <div class="row" id="stage2" style="display:none">
                <div class="span12">
                    <p>Please wait for an email which will
                       provide you with a link. When you receive the email click on the link to complete your registration.</p>
                    <form id="passwordSetForm" action="${userBaseUrl}/confirm" method="POST" autocomplete="off" style="display:none">
                        <fieldset>
                            <legend>Please fill in the code, password and the retype field</legend>
                            <div class="clearfix">
                                <label for="confirmCode">Code received</label>

                                <div class="input">
                                    <input class="required xlarge" id="confirmCode" name="confirmCode"
                                           size="40" type="text" autocomplete="off" value=" "/>
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

</html>


<script>
     $.template("warning", $("#warning-template"));
</script>

<script src="js/register.js"></script>

<script>
    <#if (RequestParameters.forgot)??>
        shiro.forgot = true;
    <#else>
        shiro.forgot = false;
    </#if>
</script>


</body>
</html>
