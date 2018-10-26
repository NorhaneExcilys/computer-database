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
                    <div class="label label-default pull-right">
                        id: ${id}
                    </div>
                    <h1><fmt:message key="label.editComputer" /></h1>
                    <div class="alert alert-danger" role="alert" style="display:none;">
  						The computer name must be completed.
					</div>
                    <form action="editComputer" method="POST">
                        <input type="hidden" name="id" value="${id}" id="id"/>
                        <fieldset>
                            <div class="form-group">
                                <label for="computerName"><fmt:message key="label.computerName" /></label>
                                <input type="text" class="form-control" id="computerName" name="name" placeholder="<fmt:message key="label.computerName" />" value="${name}">
                            </div>
                            <div class="form-group">
                                <label for="introduced"><fmt:message key="label.introducedDate" /></label>
                                <input type="date" class="form-control" id="introduced" name="introduced" placeholder="<fmt:message key="label.introducedDate" />" value="${introduced}">
                            </div>
                            <div class="form-group">
                                <label for="discontinued"><fmt:message key="label.discontinuedDate" /></label>
                                <input type="date" class="form-control" id="discontinued" name="discontinued" placeholder="<fmt:message key="label.discontinuedDate" />" value="${discontinued}">
                            </div>
                            <div class="form-group">
                                <label for="companyId"><fmt:message key="label.company" /></label>
                                <select class="form-control" id="companyId" name="companyId" >
                                	<option disabled selected value> -- <fmt:message key="label.companySelect" /> -- </option>

                                    
                                    <c:forEach items="${companies}" var="company">
										<c:choose>
											<c:when test="${companyId == company.id}">
												<option value="${company.id}" selected>${company.name}</option>
											</c:when>
											<c:otherwise>
												<option value="${company.id}">${company.name}</option>
											</c:otherwise>
										</c:choose>
                                	</c:forEach>
                                </select>
                            </div>            
                        </fieldset>
                        <div class="actions pull-right">
                            <input type="submit" value="<fmt:message key="label.edit" />" class="btn btn-primary">
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