<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt"%>

<!DOCTYPE html>
<html>
<head>
<title><fmt:message key="label.computerDatabase" /></title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta charset="utf-8">
<!-- Bootstrap -->
<link href="resources/css/bootstrap.min.css" rel="stylesheet" media="screen">
<link href="resources/css/font-awesome.css" rel="stylesheet" media="screen">
<link href="resources/css/main.css" rel="stylesheet" media="screen">
</head>
<body>
    <header class="navbar navbar-inverse navbar-fixed-top">
        <div class="container">
            <a class="navbar-brand" href="dashboard"><fmt:message key="label.title" /></a>
	        <div class="navbar-brand pull-right">
	        	<a href="?lang=fr">FR</a>
	            <a href="?lang=en">EN</a>
			</div>
        </div>
    </header>

    <section id="main">
        <div class="container">
            <h1 id="homeTitle">
                ${computerNumber} <fmt:message key="label.computersfound" />
            </h1>
            <div id="actions" class="form-horizontal">
                <div class="pull-left">
                    <form id="searchForm" action=# method="GET" class="form-inline">

                        <input type="search" id="searchbox" name="search" class="form-control" placeholder="<fmt:message key="label.search" />" value="${search}" />
                        <input type="submit" id="searchsubmit" value="<fmt:message key="label.filter" />"
                        class="btn btn-primary" />
                    </form>
                </div>
                <div class="pull-right">
                    <a class="btn btn-success" id="addComputer" href="addComputer"><fmt:message key="label.addComputer" /></a>
                    <a class="btn btn-default" id="editComputer" href="#" onclick="$.fn.toggleEditMode();"><fmt:message key="label.edit" /></a>
                </div>
            </div>
        </div>

        <form id="deleteForm" action="deleteComputer" method="POST">
            <input type="hidden" name="selection" value="">
        </form>

        <div class="container" style="margin-top: 10px;">
            <table class="table table-striped table-bordered">
                <thead>
                    <tr>
                        <!-- Variable declarations for passing labels as parameters -->
                        <!-- Table header for Computer Name -->

                        <th class="editMode" style="width: 60px; height: 22px;">
                            <input type="checkbox" id="selectall" /> 
                            <span style="vertical-align: top;">
                                 -  <a href="#" id="deleteSelected" onclick="$.fn.deleteSelected();">
                                        <i class="fa fa-trash-o fa-lg"></i>
                                    </a>
                            </span>
                        </th>
                        <th>
                        	<fmt:message key="label.computerName" />
                        </th>
                        <th>
                        	<fmt:message key="label.introducedDate" />
                        </th>
                        <!-- Table header for Discontinued Date -->
                        <th>
                        	<fmt:message key="label.discontinuedDate" />
                        </th>
                        <!-- Table header for Company -->
                        <th>
                        	<fmt:message key="label.company" />
                        </th>

                    </tr>
                </thead>
                <!-- Browse attribute computers -->
                <tbody id="results">
                	<c:forEach items="${computers}" var="computer">
	                    <tr>
	                        <td class="editMode">
	                            <input type="checkbox" name="cb" class="cb" value="${computer.id}">
	                        </td>
	                        <td>
	                            <a href="editComputer?id=${computer.id}" onclick="">${computer.name}</a>
	                        </td>
	                        <td>${computer.introducedDate}</td>
	                        <td>${computer.discontinuedDate}</td>
	                        <td>${computer.companyName}</td>
	                    </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </section>

    <footer class="navbar-fixed-bottom">
        <div class="container text-center">
            <ul class="pagination">
                <li>
                	<a href="dashboard?computersPerPage=${computersPerPage}&currentPage=1" aria-label="Previous">
                    	<span aria-hidden="true">&laquo;</span>
                	</a>
              	</li>
              	<c:forEach begin="${pageMin}" end="${pageMax}" var="page">
              		<li><a href="dashboard?computersPerPage=${computersPerPage}&currentPage=${page}">${page}</a></li>
              	</c:forEach>
              	<li>
                	<a href="dashboard?computersPerPage=${computersPerPage}&currentPage=${totalPage}" aria-label="Next">
                    	<span aria-hidden="true">&raquo;</span>
                	</a>
            	</li>
        	</ul>

	        <div class="btn-group btn-group-sm pull-right" role="group" >
	            <button type="button" class="btn btn-default" onclick="location.href='dashboard?computersPerPage=10&currentPage=1'">10</button>
	            <button type="button" class="btn btn-default" onclick="location.href='dashboard?computersPerPage=50&currentPage=1'">50</button>
	            <button type="button" class="btn btn-default" onclick="location.href='dashboard?computersPerPage=100&currentPage=1'">100</button>
	        </div>
	        
        </div>

    </footer>
<script src="resources/js/jquery.min.js"></script>
<script src="resources/js/bootstrap.min.js"></script>
<script src="resources/js/dashboard.js"></script>

</body>
</html>