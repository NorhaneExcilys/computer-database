<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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
            <div class="row">
                <div class="col-xs-8 col-xs-offset-2 box">
                    <h1><fmt:message key="label.addComputer" /></h1>
                    <form action="addComputer" method="POST">
                        <fieldset>
                            <div class="form-group">
                                <label for="computerName"><fmt:message key="label.computerName" /></label>
                                <input type="text" class="form-control" id="computerName" name="computerName" placeholder="<fmt:message key="label.computerName" />" required="required">
                            </div>
                            <div class="form-group">
                                <label for="introduced"><fmt:message key="label.introducedDate" /></label>
                                <input type="date" class="form-control" id="introduced" name="introduced" placeholder="<fmt:message key="label.introducedDate" />">
                            </div>
                            <div class="form-group">
                                <label for="discontinued"><fmt:message key="label.discontinuedDate" /></label>
                                <input type="date" class="form-control" id="discontinued" name="discontinued" placeholder="<fmt:message key="label.discontinuedDate" />">
                            </div>
                            <div class="form-group">
                                <label for="companyId"><fmt:message key="label.company" /></label>
                                <select class="form-control" id="companyId" name="companyId" >
                                	<option disabled selected> -- <fmt:message key="label.companySelect" /> -- </option>
                                	<c:forEach items="${companies}" var="company">
                                    	<option value="${company.id}">${company.name}</option>
                                	</c:forEach>
                                </select>
                            </div>                  
                        </fieldset>
                        <div class="actions pull-right">
                            <input type="submit" value="<fmt:message key="label.add" />" class="btn btn-primary">
                            <fmt:message key="label.or" />
                            <a href="dashboard" class="btn btn-default"><fmt:message key="label.cancel" /></a>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </section>
</body>
</html>