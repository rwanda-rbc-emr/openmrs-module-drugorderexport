
	
<h2><spring:message code="drugorderexport.title" /></h2>



<ul id="menu">

		<li
			class="first<c:if test='<%= request.getRequestURI().contains("drugorderView") %>'> active</c:if>">
		<a
	href="${pageContext.request.contextPath}/module/drugorderexport/drugorderView.form"><spring:message
	code="drugorderexport.patienttakingspecificdrugs" /></a>
	</li>

	
		<li <c:if test='<%= request.getRequestURI().contains("pWhoStoppedDrug") %>'>class="active"</c:if>>
		<a 	href="${pageContext.request.contextPath}/module/drugorderexport/pWhoStoppedDrug.form"><spring:message 
		code="drugorderexport.pwhostoppedrug" /></a>
		</li>
	
		<li <c:if test='<%= request.getRequestURI().contains("patientHistory") %>'>class="active"</c:if>>
		<a
			href="${pageContext.request.contextPath}/module/drugorderexport/patientHistory.form"><spring:message
			code="drugorderexport.patientHistory" /></a>
		</li>
		
		<li <c:if test='<%= request.getRequestURI().contains("startTreatment") %>'>class="active"</c:if>>
		<a 	href="${pageContext.request.contextPath}/module/drugorderexport/startTreatment.form"><spring:message
			code="drugorderexport.firstRegimen" /></a>
		</li>
		
		<li <c:if test='<%= request.getRequestURI().contains("patientWhoHaveChangedRegimen") %>'>class="active"</c:if>>
		<a
	href="${pageContext.request.contextPath}/module/drugorderexport/patientWhoHaveChangedRegimen.form">
	<spring:message code="drugorderexport.changedRegimen" /></a>
		</li>
		
		<li <c:if test='<%= request.getRequestURI().contains("patientsOnProphylaxis") %>'>class="active"</c:if>>
		<a
	href="${pageContext.request.contextPath}/module/drugorderexport/patientsOnProphylaxis.form"><spring:message
	code="drugorderexport.prophyPatients" /></a>
		</li>
		
		
		<li <c:if test='<%= request.getRequestURI().contains("newOnProphylaxis") %>'>class="active"</c:if>>
		<a
	href="${pageContext.request.contextPath}/module/drugorderexport/newOnProphylaxis.form"><spring:message
	code="drugorderexport.prophyNew" /></a>
		</li>
		
		
		<li <c:if test='<%= request.getRequestURI().contains("patOnRegimenType") %>'>class="active"</c:if>>
		<a 	href="${pageContext.request.contextPath}/module/drugorderexport/patOnRegimenType.form"><spring:message
	        code="drugorderexport.patientOnRegimenType" /></a>
		</li>
	
	
	
</ul>
