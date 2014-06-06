<#assign style="mainstyle.css">
<!DOCTYPE html>
<html lang="en" class="shiro-none-active">
<head>
    <#include "inc/_head.ftl">
</head>


<div id="spinner" class="shiro-unset" style="position: absolute; top: 90px; left: 50%;">
</div>

<#include "inc/topbar.ftl">



<div class="container">

<#include "inc/footer.ftl">

</div>
</body>
<#include "inc/_foot.ftl">

<script>
function loginRedirect(){
	location.href = "monthview.ftl";
}
</script>

<body>
</html>
