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

			<div class="pull-right form-inline">
				<div class="btn-group">
					<form id="monthViewButtonForm" action="monthview.ftl">
						<button type="submit" class="btn btn-success" id="month-view-button">Month View</button>
					</form>
				</div>
				<div class="btn-group">
					<button class="btn btn-info" data-calendar-nav="prev">&lt;&lt; Prev</button>
					<button class="btn btn-default" data-calendar-nav="today">Today</button>
					<button class="btn btn-info" data-calendar-nav="next">Next &gt;&gt;</button>
				</div>
			</div>

			<h3></h3>
		</div>

		<div class="row">
			<div class="col-md-3">
				<form id="reserveForm" role="form">
						<div class="sticky">
							<fieldset>

								<!-- Select Boat -->
								<div class="row">
									<div class="form-group col-xs-8">
										<label class="control-label" for="boat">Boat/Event</label><br>
										<input name="boat" id="boat" style="width: 200px"  />
									</div>
								</div>
								<!-- Date-->
								<div class="row">
									<div class="form-group col-xs-8">
										<label class="control-label" for="reserveDate">Date</label>
										<div class="input-group date" id="reserveDate" style="width: 200px" >
											<input id='date-input' name="reserveDate" type="text" class="form-control" />
											<span class="input-group-addon">
												<span class="glyphicon glyphicon-calendar"></span> 
											</span>
										</div>
									</div>
								</div>
								<!-- Time-->
								<div class="row">
									<div class="form-group col-xs-8">
										<label class="control-label" for="reserveTime">Time</label>
										<div class="input-group date" id="reserveTime" style="width: 200px" >
											<input id='time-input' name="reserveTime" type="text" class="form-control" />
											<span class="input-group-addon">
												<span class="glyphicon glyphicon-clock"></span> 
											</span>
										</div>
									</div>
								</div>

								<!-- Duration-->
								<div class="row">
									<div class="form-group col-xs-8" >
										<label class="control-label" for="duration">Duration</label>
										<div class="form-inline">
											<input id="duration" name="duration"  class="form-control" style="width: 50px" value="60"> <select id="units"
												name="units" style="width: 100px" class="form-control">
												<option selected="selected">Minutes</option>
												<option>Hours</option>
												<option>Days</option>
											</select>
										</div>
									</div>
								</div>
								<!-- Time -->

							</fieldset>
							<div class="row">
								<div class="col-xs-4">
									<button id="reserve" type="submit" class="btn btn-primary form-control">Save</button>
								</div>
							</div>
						</div>
					</form>
					<br>
			</div>
			<div class="col-md-6">
				<div id="warningMessage" class="bs-callout bs-callout-danger" style="display: none;"></div>
				<div id="calendar"></div>
			</div>


		</div>

		<div class="clearfix"></div>
		<br> <br>


<#include "inc/footer.ftl">
</div>
<#include "inc/_foot.ftl">
<script type="text/javascript" src="js/reserve.js"></script>

</body>
</html>
