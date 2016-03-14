<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>
<openmrs:htmlInclude file="/scripts/calendar/calendar.js" />

<openmrs:htmlInclude
file="/moduleResources/drugorderexport/jquery.dataTables.js" />  

<openmrs:htmlInclude
file="/moduleResources/drugorderexport/demo_page.css" />  

<openmrs:htmlInclude
file="/moduleResources/drugorderexport/demo_table.css" />  
<script type="text/javascript" charset="utf-8">
		var $ = jQuery;

		$(document).ready(function() {
			$('#example').dataTable( {
				"sPaginationType": "full_numbers"
			} );
		} );
</script>



<openmrs:htmlInclude
	file="/moduleResources/drugorderexport/patienthistories.js" />

<openmrs:htmlInclude
	file="/moduleResources/drugorderexport/functions.js" />	
	

<br />
	
<%@ include file="localHeader.jsp"%>

<b class="boxHeader" style="width: 100%"><spring:message code="drugorderexport.patientHistory" /></b>

<!-- copy of form to get values/fields while export=====>fields are hidden -->
<div
	style="background: #E2E4FF; border: 1px #808080 solid; padding: 0.5em; margin-bottom: 0em">
<form action="patientHistory.form" method="post"
	name="patientHistory" onsubmit="return checkform(this);">
	
	<input type="hidden" name="linkId" value="${linkId}"/>
	
<table style="background: #E2E4FF; -moz-border-radius:4px; -webkit-border-radius:4px;">
	<tr>
		<td colspan="2"><b><spring:message
			code="drugorderexport.optionalAttributes" /></b></td>
	</tr>
	<tr>
		<td><spring:message code="drugorderexport.gender" />:</td>
		<td>
		<select name="gender">
			<option value="" ${param.gender== "" ? 'selected="selected"':''}><spring:message
				code="drugorderexport.any" /></option>
			<option value="m" ${param.gender== "m" ? 'selected="selected"':''}><spring:message
				code="drugorderexport.male" /></option>
			<option value="f" ${param.gender== "f" ? 'selected="selected"':''}><spring:message
				code="drugorderexport.female" /></option>
		</select></td>

	</tr>
	<tr>
		<td><spring:message code="drugorderexport.age" />:</td>
		<td><spring:message code="drugorderexport.between" /> <input
			type="text" name="minAge" size="3" value="${param.minAge}" /> <spring:message
			code="drugorderexport.and" /> <input type="text" name="maxAge" size="3"
			value="${param.maxAge}" /> <spring:message code="drugorderexport.years" /></td>
	</tr>

	<tr>
		<td><spring:message code="drugorderexport.birthday" />:</td>
		<td><spring:message code="drugorderexport.between" /> <input
			type="text" id="minBirthdate" size="11" value="${param.minBirthdate}"
			name="minBirthdate" onclick="showCalendar(this)" /> <spring:message
			code="drugorderexport.and" /> <input type="text" id="maxBirthdate"
			size="11" value="${param.maxBirthdate}" name="maxBirthdate"
			onclick="showCalendar(this)" /></td>
	</tr>

	<tr>
		<td><input type="submit"
			value="<spring:message code="drugorderexport.searchSubmit" />" align="right"/>
			
			<spring:message code="drugorderexport.activeOnly" /><input type="checkbox" name="checkValue" value="1"/>
			</td>
	</tr>
</table>

</form>
</div>


<br/>

<c:if test="${fn:length(listPatientHistory)>0}">
	
 <div style="border: 1px #808080 solid; padding: 0em; margin: 0em;width:500"><!--(Start) This is repeated depending on the number of regimen of the patient -->
	<b class="boxHeader" style=" width:100% ;padding: 0em; margin-right: 0em;margin-left:0em ">
	
	<div>
	<spring:message code="drugorderexport.histories" />&nbsp;  
    Patients
    <c:if test="${gender=='Any'}">(<spring:message code="drugorderexport.anyGender" />) </c:if>
    <c:if test="${gender=='Male'}">(<spring:message code="drugorderexport.male" />) </c:if>
    <c:if test="${gender=='Female'}">(<spring:message code="drugorderexport.female" />) </c:if>


  <c:if test="${!empty minAge && !empty maxAge}"><spring:message code="drugorderexport.between" />&nbsp;<spring:message code="drugorderexport.theAgeOf" /> &nbsp; ${minAge }&nbsp; <spring:message code="drugorderexport.and" /> &nbsp; ${maxAge }&nbsp;</c:if>
 <c:if test="${!empty minAge && empty maxAge}"> <spring:message code="drugorderexport.atLeast" />  &nbsp; ${minAge }&nbsp; <spring:message code="drugorderexport.yearsOld" /> </c:if>
 <c:if test="${empty minAge && !empty maxAge}"> <spring:message code="drugorderexport.upTo" /> &nbsp; ${maxAge }&nbsp; <spring:message code="drugorderexport.yearsOld" /></c:if>
 
 <c:if test="${!empty minBirthdate && !empty maxBirthdate}"> <spring:message code="drugorderexport.dob" />&nbsp;<spring:message code="drugorderexport.between" />&nbsp; ${minBirthdate }&nbsp; <spring:message code="drugorderexport.and" /> &nbsp; ${maxBirthdate }&nbsp;</c:if>
 <c:if test="${!empty minBirthdate && empty maxBirthdate}"> <spring:message code="drugorderexport.dob" />&nbsp;<spring:message code="drugorderexport.from" />  &nbsp; ${minBirthdate }&nbsp; </c:if>
 <c:if test="${empty minBirthdate && !empty maxBirthdate}"> <spring:message code="drugorderexport.dob" />&nbsp;<spring:message code="drugorderexport.before" /> &nbsp; ${maxBirthdate }&nbsp; </c:if>

 &nbsp;&nbsp;&nbsp;&nbsp; :: ${collectionSize }
</div>
	
	</b>
	
	<div id="dt_example">
	<div id="container">
	 
	<!-- --------------------export button------------------------------------------------ -->
	<div align="center">
	
	<table><tr><td> <form action="patientHistory.form?export=excel" method="post">
	<input type="hidden" name="linkId" value="${linkId }"/>
	<input type="hidden" name="startdate" value="${param.startdate}"/> 
	<input type="hidden" name="enddate" value="${param.enddate}" /> 
	<input type="hidden" name="anyOrAll" value="${param.anyOrAll}" />
	<input type="hidden" name="gender" value="${param.gender}" />
	<input type="hidden" name="minAge" value="${param.minAge}" /> 
	<input type="hidden" name="maxAge" value="${param.maxAge}" /> 
	<input type="hidden" name="minBirthdate" value="${param.minBirthdate}" /> 
	<input type="hidden" name="maxBirthdate" value="${param.maxBirthdate}" />
	
	<input	type="submit" value="<spring:message code="drugorderexport.export" />"/>
	
	<input type="hidden" name="checkValue" value="${checkedValueExport}"/>
	
	</form>
	</td>
	<td><form action="patientHistory.form?export=pdf" method="post">
	<input type="hidden" name="linkId" value="${linkId }"/>
	<input type="hidden" name="startdate" value="${param.startdate}"/> 
	<input type="hidden" name="enddate" value="${param.enddate}" /> 
	<input type="hidden" name="anyOrAll" value="${param.anyOrAll}" />
	<input type="hidden" name="gender" value="${param.gender}" />
	<input type="hidden" name="minAge" value="${param.minAge}" /> 
	<input type="hidden" name="maxAge" value="${param.maxAge}" /> 
	<input type="hidden" name="minBirthdate" value="${param.minBirthdate}" /> 
	<input type="hidden" name="maxBirthdate" value="${param.maxBirthdate}" />
	
	<input	type="submit" value="<spring:message code="drugorderexport.exportPdf" />"/>
	
	<input type="hidden" name="checkValue" value="${checkedValueExport}"/>
	
	</form>	</td></tr></table>

	
	</div>

	<!-- ------------end--------export button------------------------------------------------ -->
	
	<table cellpadding="0" cellspacing="0" border="0" class="display" id="example">
		<thead>
		<tr>
		   <th><spring:message code="drugorderexport.patientId" /></th>
		   <openmrs:hasPrivilege privilege="View Patient Names">
			<th><spring:message code="drugorderexport.givenname" /></th>
			<th><spring:message code="drugorderexport.familyname" /></th>
			</openmrs:hasPrivilege>
			<th><spring:message code="drugorderexport.gender" /></th>
			<th><spring:message code="drugorderexport.age" /></th>
			<th><spring:message code="drugorderexport.programs" /></th>
			<th><spring:message code="drugorderexport.startTreatment" /></th>
			<th><spring:message code="drugorderexport.lastEncounterDate" /></th>
			<th><spring:message code="drugorderexport.lastVisitDate" /></th>
			<th></th>
			<th></th>
		</tr>
		</thead>
		<tbody>
		
		<c:forEach var="list" items="${listPatientHistory}" varStatus="i">
			<tr>
			    <td>&nbsp; ${list[0].patientIdentifier}</td>
			    <openmrs:hasPrivilege privilege="View Patient Names">
				<td>&nbsp; ${list[0].givenName} </td>
				<td>&nbsp; ${list[0].familyName}</td>
				</openmrs:hasPrivilege>
				<td>&nbsp;<img
					src="${pageContext.request.contextPath}/images/${list[0].gender == 'M' ? 'male' : 'female'}.gif" /></td>
				<td>&nbsp; ${list[0].age}</td>
				<td>&nbsp; <c:forEach var="program" items="${list[1]}">${program.program.name}   </c:forEach></td>

				<td>&nbsp; ${list[2]}</td>
				<td>&nbsp; ${list[3]}</td>
				<td>&nbsp; ${list[4]}</td>
				<td>&nbsp; 
				<openmrs:hasPrivilege privilege="View Patient Regimens History">
				<a
					href="${pageContext.request.contextPath}/module/drugorderexport/showPatientRegimens.form?patient=${list[0].patientId}"><spring:message
					code="drugorderexport.viewregimen" /></a>
					</openmrs:hasPrivilege>
					</td>
					
					<td>&nbsp; <a
					href="${pageContext.request.contextPath}/patientDashboard.form?patientId=${list[0].patientId}">View
				Dashboard</a></td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	</div></div>

</c:if>
<%@ include file="/WEB-INF/template/footer.jsp"%>