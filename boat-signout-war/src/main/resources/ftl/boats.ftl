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

<div id="BoatTableContainer"></div>



<#include "inc/footer.ftl">

</div>

<#include "inc/_foot.ftl">
<script src="js/lib/jtable/jquery.jtable.js"></script>

<script type="text/javascript">
$(document).ready(function () {

    $('#BoatTableContainer').jtable({
        title: 'Boats',
        actions: {
            listAction: '/boats',
            createAction: '/boats/post',
            updateAction: '/boats/post',
            deleteAction: '/boats/delete'
        },
        fields: {
        	name: {
        		key: true,
            	create: true,
        		title: 'Boat name',
                width: '30%'                
            },
            displayName: {
                create: true,
                title: 'Display name',
                type: 'textarea',
                width: '30%'
            },
        	rowable: {
        		type: 'checkbox',
        		create: true,
        		default: 'true',
            	title: 'Status',
            	values: { 'false': 'not rowable', 'true': 'rowable' },
                width: '10%'                
            },
            event: {
                type: 'checkbox',
                create: true,
                default: 'false',
                title: 'Event/Boat',
                values: { 'false': 'boat', 'true': 'event' },
                width: '10%'
            },
        	warningMessage: {
        		create: true,
            	title: 'Warning message',
            	type: 'textarea',
                width: '30%'
            }
        }
    });

    $('#BoatTableContainer').jtable('load');
});

</script>
</body>
</html>
