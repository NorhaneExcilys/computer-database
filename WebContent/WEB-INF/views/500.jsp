<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt"%>

<!DOCTYPE html>
<html>
<head>
	<title><fmt:message key="label.computerDatabase" /></title>
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<!-- Bootstrap -->
	<link href="resources/css/bootstrap.min.css" rel="stylesheet" media="screen">
	<link href="resources/css/font-awesome.css" rel="stylesheet" media="screen">
	<link href="resources/css/main.css" rel="stylesheet" media="screen">
</head>
<body>
	<header class="navbar navbar-inverse navbar-fixed-top">
		<div class="container">
			<a class="navbar-brand" href="dashboard"><fmt:message key="label.title" /></a>
		</div>
	</header>

	<section id="main">
		<div class="container">	
			<div class="alert alert-danger">
				<fmt:message key="label.error500" />
				<br/>
				<!-- stacktrace -->
			</div>
		</div>
	</section>

	<script src="resources/js/jquery.min.js"></script>
	<script src="resources/js/bootstrap.min.js"></script>
	<script src="resources/js/dashboard.js"></script>

</body>
</html>