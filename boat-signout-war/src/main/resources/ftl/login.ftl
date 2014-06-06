<#assign title="Shiro on GAE Login">
<#assign style="substyle.css">

<!DOCTYPE html>
<html lang="en">
<head>
    <#include "inc/_head.ftl">
</head>

<body>


<div class="container">

    <div class="content">
        <section>
            <div class="page-header">
                <h1>Login</h1>
            </div>
            <div class="row">
               <div class="span12">
                   <#include "inc/login-template.ftl">
                </div>
            </div>
        </section>
    </div>

<#include "inc/footer.ftl">

</div>

<#include "inc/_foot.ftl">

<script>
$(document).ready(function() {
    $("#login-form").validate({
      errorPlacement: function(error, element) {
           error.insertAfter(element);
      }
    });

});
</script>

</body>
</html>
