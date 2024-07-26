function getUrlVar(key){
	var result = new RegExp(key + "=([^&]*)", "i").exec(window.location.search); 
	return result && unescape(result[1]) || ""; 
}

(function($) {

	"use strict";
	
	
	var datePicker = $('#reserveDate').datetimepicker({
		format: 'YYYY-MM-DD',
		pickTime: false
	});
	
	var timePicker = $('#reserveTime').datetimepicker({
		format: 'h:mm a',
		pickDate: false
	});

	var dt = getUrlVar('dt');
	dt = dt ? dt  :moment().format('YYYY-MM-DD');
	var options = {
			events_source: 'reserve',
			view: 'day',
			tmpl_path: 'tmpls/',
			tmpl_cache: false,
			day: dt,
			onAfterViewLoad: function(view) {
				$('.page-header h3').text(this.getTitle());
				$('.btn-group button').removeClass('active');
				$('button[data-calendar-view="' + view + '"]').addClass('active');
				
				// If the view date is different than the datetimepicker then update the 
				// datetimepicker to match the view and default the time to 6:00 am.
				var dp = $('#reserveDate').data("DateTimePicker");
				var tp = $('#reserveTime').data("DateTimePicker");
				tp.setValue(localStorage.getItem("time-input"));
				$('#duration').val(localStorage.getItem("duration"));
				$('#unit').val(localStorage.getItem("unit"));
				var viewDate = moment(this.getStartDate()).format('YYYY-MM-DD');
				if(!$("#date-input").val() || viewDate != dp.getDate().format('YYYY-MM-DD')){
					dp.setValue(viewDate);
				}
				
				$(".glyphicon-trash").click(function(e){
					bootbox.confirm(
							"Are you sure you want to delete this entry?<br>" + $(e.target).data("event"),
							function(ok) {
								if(ok){
									var spin = shiro.spin.start($("#spinner"));
									$.ajax({
										type: "DELETE",
										url: "reserve/" + $(e.target).data("id"),
										dataType: "json",
										success: function(data)
										{
											spin.stop();
											if (data.Result != "ERROR") {
												calendar.view();
											} else {
												bootbox.alert(data.Message);
											}
										},
										error: function(data)
										{
											spin.stop();
											bootbox.alert(data);
										}
									});
								}
							}			
						);
				});

			},
			classes: {
				months: {
					general: 'label'
				}
			}
	};

	var calendar = $('#calendar').calendar(options);
	datePicker.on('dp.change', function(e){
		$('#reserveForm').bootstrapValidator('resetForm');
		var newDate = e.date.format('YYYY-MM-DD');
		calendar.options.day = newDate;
		calendar.view();
		location.href = "dayview.ftl?dt=" + newDate;
	});

	$('.btn-group button[data-calendar-nav]').each(function() {
		var $this = $(this);
		$this.click(function() {
			calendar.navigate($this.data('calendar-nav'));
		});
	});

	$('.btn-group button[data-calendar-view]').each(function() {
		var $this = $(this);
		$this.click(function() {
			calendar.view($this.data('calendar-view'));
		});
	});
	
	var updateWarning = function(){
		var boat = $("#boat").select2('data');
		var msg = '';
		if(!boat.rowable){
			msg += "<h4>" + boat.id + " is  not rowable</h4>";
		}
		if(boat.warningMessage){
			msg += "<h4>" + boat.warningMessage + "</h4>";
		}
		$('#warningMessage').html(msg);
		if(boat.warningMessage || !boat.rowable){
			$('#warningMessage').show();
		}else{
			$('#warningMessage').hide();
		}
	};

	$.getJSON( "boats", function( data ) {
		var boatData = [];
		$.each( data.Records, function( i, val ) {
			boatData.push( {
				id: val.name, 
				text: val.displayName,
				rowable: val.rowable, 
				warningMessage: val.warningMessage 
			});
		});
		$("#boat").select2({ data: boatData});
		var previous = localStorage.getItem("boat");
		if(previous){
			$("#boat").select2("val", previous);
			updateWarning();
		}
	});

	$("#boat").on("change", function(e){
		$('#reserveForm').bootstrapValidator('resetForm');
		calendar.options.boat = e.val;
		calendar.view();
		localStorage.setItem("boat", e.val);
		updateWarning();		
	});
	
	var submitHandler = function(validator, form, submitButton, allowConflicts) {
		if(!$("#boat").select2('data').rowable){
			bootbox.alert($("#boat").select2('data').text + " is not rowable<br>" + $("#boat").select2('data').warningMessage);
			return false;
		}
		if($("#boat").select2('data').event){
			allowConflicts = true;
		}
		var spin = shiro.spin.start($("#spinner"));
		localStorage.setItem("time-input", $("#time-input").val());
		localStorage.setItem("boat", $("#boat").val());
		localStorage.setItem("duration", $("#duration").val());
		localStorage.setItem("units", $("#units").val());
		$.ajax({
			type: "POST",
			url: "reserve",
			dataType: "json",
			data: $("#reserveForm").serialize() + "&allowConflicts=" + allowConflicts, 
			success: function(data)
			{
				spin.stop();
				if (data.Result != "ERROR") {
					location.reload();
				} else {
					if(data.ErrorType){
						if (data.ErrorType === "CONFLICT"){
							bootbox.confirm(
								"The time conflicts with the following existing reservations: <br><br>" +  data.Message +" <br><br>Do you want to save anyway?",
								function(ok) {
									if(ok){
										submitHandler(validator, form, submitButton, true);
									}
								}
							);
						}
						if (data.ErrorType === "RIVER_NOT_ROWABLE"){
							bootbox.confirm(
								data.Message +" <br><br>Do you want to save anyway?",
								function(ok) {
									if(ok){
										submitHandler(validator, form, submitButton, true);
									}
								}
							);
						}
					} else {
						bootbox.alert(data.Message);
					}
				}
			},
			error: function(data)
			{
				spin.stop();
				location.href = "/";
			}
		});

	};
	
	$('#reserveForm').bootstrapValidator({
		message: 'This value is not valid',
		excluded: [],
		fields: {
			boat: {
				selector: "#boat",
				validators: {
					notEmpty: {
						message: 'Please select a boat'
					}
				}
			},
			reserveDate: {
				validators: {
					notEmpty: {
						message: 'Please select a date'
					}
				}
			},
			reserveTime: {
				validators: {
					notEmpty: {
						message: 'Please select a time'
					}
				}
			},
			duration: {
				validators: {
					notEmpty: {
						message: 'Please select a duration'
					}
				}
			}
		},
		submitHandler: submitHandler
	});
	

	
}(jQuery));