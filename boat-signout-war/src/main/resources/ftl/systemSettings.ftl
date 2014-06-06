<#assign style="mainstyle.css">
<!DOCTYPE html>
<html lang="en" class="shiro-none-active">
<head>
    <#include "inc/_head.ftl">
    <link href="js/lib/jtable/themes/metro/darkgray/jtable.min.css" rel="stylesheet" type="text/css" />
    
</head>

<body>

<div id="spinner" class="shiro-unset" style="position: absolute; top: 90px; left: 50%;">
</div>

<#include "inc/topbar.ftl">


<div class="container">

<div id="SettingsTableContainer"></div>



<#include "inc/footer.ftl">

</div>

<#include "inc/_foot_min.ftl">
<script src="js/lib/jtable/jquery.jtable.js"></script>

<script type="text/javascript">
$(document).ready(function () {

    $('#SettingsTableContainer').jtable({
        title: 'Settings',
        actions: {
            listAction: '/systemSettings',
            createAction: '/systemSettings/post',
            updateAction: '/systemSettings/post',
            deleteAction: '/systemSettings/delete'
        },
        fields: {
        	key: {
        		key: true,
        		create: true,
            	title: 'Key',
                width: '100%'                
            },
        	value: {
        		create: true,
            	title: 'Value',
                width: '100%'                
            }
        }
    });

    $('#SettingsTableContainer').jtable('load');
});

</script>
</body>
</html>
