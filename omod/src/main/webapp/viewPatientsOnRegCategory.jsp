<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>
<%@ taglib prefix="fn" uri="/WEB-INF/taglibs/fn.tld"%>

<script type="text/javascript" charset="utf-8">
		var $ = jQuery;
		
		$(document).ready(function() {
			$('#example').dataTable( {
				"sPaginationType": "full_numbers"
			} );


			
			
		} );
</script>

<openmrs:htmlInclude
	file="/moduleResources/drugorderexport/jquery.dataTables.js" />

<openmrs:htmlInclude
	file="/moduleResources/drugorderexport/demo_page.css" />

<openmrs:htmlInclude
	file="/moduleResources/drugorderexport/demo_table.css" />
	<br />
	
	
	<div id="openmrs_msg" >${title}: <b>${patientsSize}</b></div>
	<br />

	
	<form action="viewPatOnRegimenCateg.form?export=excel" method="post">
	<input type="submit" value="Excel"/>
	
	<input type="hidden" name="startdate" value="${param.startdate}" /> 
	<input type="hidden" name="enddate" value="${param.enddate}" /> 
	<input type="hidden" name="gender" value="${param.gender}" /> 
	<input type="hidden" name="minAge" value="${param.minAge}" /> 
	<input type="hidden" name="maxAge" value="${param.maxAge}" /> 
	<input type="hidden" name="minBirthdate" value="${param.minBirthdate}" /> 
	<input type="hidden" name="maxBirthdate" value="${param.maxBirthdate}" />
	
	<input type="hidden" name="viewCategory" value="${viewCategory }"/>
				
	</form>
	
	<table cellpadding="0" cellspacing="0" border="0" class="display"
		id="example">
		<thead>

			<tr>
				 <th><spring:message code="drugorderexport.patientId" /></th>
				 <openmrs:hasPrivilege privilege="View Patient Names">
				<th><spring:message code="drugorderexport.givenname" /></th>
				<th><spring:message code="drugorderexport.familyname" /></th>
				</openmrs:hasPrivilege>
				<th><spring:message code="drugorderexport.age" /></th>
				<th><spring:message code="drugorderexport.gender" /></th>
				<th><spring:message code="drugorderexport.startTreatment" /></th>
				<th><spring:message code="drugorderexport.lastEncounterDate" /></th>
				<th><spring:message code="drugorderexport.lastVisitDate" /></th>
				<openmrs:hasPrivilege privilege="View Patient Regimens History">
				<th></th>
				</openmrs:hasPrivilege>
				<th></th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="list" items="${objectsList}" varStatus="status">
				<tr>
					<td>&nbsp; ${list[0].patientIdentifier}</td>
					<openmrs:hasPrivilege privilege="View Patient Names">
					<td>&nbsp; ${list[0].givenName}</td>
					<td>&nbsp; ${ list[0].familyName}</td>
					</openmrs:hasPrivilege>
					<td>&nbsp; ${list[0].age}</td>
					<td>&nbsp; <img
						src="${pageContext.request.contextPath}/images/${list[0].gender == 'M' ? 'male' : 'female'}.gif" /></td>
						<td>&nbsp; ${list[1]}</td>
						<td>&nbsp; ${list[2]}</td>
						<td>&nbsp; ${list[3]}</td>
						<openmrs:hasPrivilege privilege="View Patient Regimens History">
						<td>&nbsp;
						
						<a
						href="${pageContext.request.contextPath}/module/drugorderexport/showPatientRegimens.form?patient=${list[0].patientId}"><spring:message
						code="drugorderexport.viewregimen" /></a>
						
						</td>
						</openmrs:hasPrivilege>
					<td>&nbsp;<a
						href="${pageContext.request.contextPath}/patientDashboard.form?patientId=${list[0].patientId}">View
					Dashboard</a></td>
					
				</tr>
			</c:forEach>
		</tbody>
	</table>

<%@ include file="/WEB-INF/template/footer.jsp"%>
