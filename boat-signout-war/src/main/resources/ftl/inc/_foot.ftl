
<script src="${staticBaseUrl}js/lib/jquery/jquery.min.js"></script>
<script>
	$.ajaxSetup({ cache: false });
</script>
<script src="${staticBaseUrl}js/lib/jquery/jquery-ui-min.js"></script>

<script src="${staticBaseUrl}js/lib/jquery.tmpl.min.js"></script>

<script src="${staticBaseUrl}js/lib/jquery.validate.min.js"></script>
<script src="${staticBaseUrl}js/lib/spin.min.js"></script>
<script src="${staticBaseUrl}js/lib/jquery.busy.min.js"></script>

<script src="${staticBaseUrl}assets/js/google-code-prettify/prettify.js"></script>

<script src="${staticBaseUrl}js/lib/underscore/underscore-min.js"></script>
<script src="${staticBaseUrl}js/lib/bootstrap3/js/bootstrap.min.js"></script>
<script src="${staticBaseUrl}js/lib/jstimezonedetect/jstz.min.js"></script>
<script src="${staticBaseUrl}js/lib/moment.js"></script>
<script src="${staticBaseUrl}js/lib/hammer.js"></script>
<script src="${staticBaseUrl}js/lib/datetime/js/bootstrap-datetimepicker.js"></script>
<script src="${staticBaseUrl}js/lib/validator/js/bootstrapValidator.js"></script>
<script src="${staticBaseUrl}js/lib/bootbox.min.js"></script>
<script src="${staticBaseUrl}js/lib/select2.js"></script>
<script  src="${staticBaseUrl}js/lib/calendar.js"></script>
<script  src="${staticBaseUrl}js/lib/jquery.cookie.js"></script>

<script src="${staticBaseUrl}js/init.js"></script>
<script src="${staticBaseUrl}js/spin.js"></script>
<script src="${staticBaseUrl}js/log.js"></script>
<script src="${staticBaseUrl}js/status.js"></script>
<script src="${staticBaseUrl}js/login.js"></script>

<script>
	$(document).ready(function() {
		$('#logout').click( function() { 
			shiro.status.clearStatus(); 
			location.href = "/"; 
		} );
	});
</script>
