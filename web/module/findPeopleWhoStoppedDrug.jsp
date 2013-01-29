<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>
<openmrs:htmlInclude file="/scripts/calendar/calendar.js" />



<openmrs:htmlInclude file="/moduleResources/drugorderexport/jquery.js" />

<openmrs:htmlInclude
	file="/moduleResources/drugorderexport/jquery.dataTables.js" />

<openmrs:htmlInclude
	file="/moduleResources/drugorderexport/demo_page.css" />

<openmrs:htmlInclude
	file="/moduleResources/drugorderexport/demo_table.css" />

<script type="text/javascript" charset="utf-8">
		$(document).ready(function() {
			$('#example').dataTable( {
				"sPaginationType": "full_numbers"
			} );
		} );
</script>



<openmrs:htmlInclude
	file="/moduleResources/drugorderexport/findpatientstopped.js" />

<openmrs:htmlInclude
	file="/moduleResources/drugorderexport/functions.js" />


<!-- top -->
<br />

<%@ include file="localHeader.jsp"%>

<i style="color: red"><spring:message code="drugorderexport.stoppedARVMsg" /></i>

<br/><br/>

<b class="boxHeader" style="width: 100%"><spring:message code="drugorderexport.patientwhostopped" /></b>


<div>
<div style="background: #E2E4FF; border: 1px #808080 solid; padding: 0.5em; margin-bottom: 0em">

<form action="" method="post" name="stoppedDrug"
	onsubmit="return checkform(this);"><input type="hidden"
	name="linkId" value="${linkId }" />

<table width="80%">
	<tr>
		<td valign="top">
		<table>

			<tr>
				<td colspan="2">
				<table
					style="background: #E2E4FF; -moz-border-radius: 4px; -webkit-border-radius: 4px;">
					<tr>
						<td colspan="2"><b><spring:message
							code="drugorderexport.optionalAttributes" /></b></td>
					</tr>
					<tr>
						<td><spring:message code="drugorderexport.gender" />:</td>
						<td><select name="gender">
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
							type="text" id="minAge" name="minAge" size="3"
							value="${param.minAge}" /> <spring:message
							code="drugorderexport.and" /> <input type="text" id="maxAge"
							name="maxAge" size="3" value="${param.maxAge}" /> <spring:message
							code="drugorderexport.years" /></td>
						<div id="check"></div>
					</tr>
					
			<tr>
				<td width="10%"><spring:message
					code="drugorderexport.inbetween" />:</td>
				<td width="40%"><input type="text" id="startdate" size="11"
					value="${param.startdate}" name="startdate"
					onclick="showCalendar(this)" /> <spring:message
					code="drugorderexport.and" /> <input type="text" id="enddate"
					size="11" value="${param.enddate}" name="enddate"
					onclick="showCalendar(this)" /><spring:message
					code="drugorderexport.optional" /><input type="hidden" name="all"
					value="all" /></td>
					
					
			</tr>

				</table>

				</td>
			</tr>

			
			<tr>
				<td colspan="2">
				<br/><br/>
				<table>
				<tr>
				<td width="40%" valign="top"><spring:message code="drugorderexport.reason" /></td>
				<td width="20%" valign="top"><spring:message code="drugorderexport.drugs" /></td>
				<td width="20%" valign="top"><spring:message code="drugorderexport.generic" /></td>
				</tr>
				
					<tr>
						<td width="60%"><select name="reasonsList"
							multiple="multiple" size="10">
							<c:forEach var="reason" items="${reasons}">
								<option value="${reason.conceptId}"
									<c:forEach var="reasonsIds" items="${reasonsIdback}">
							<c:if test='${reasonsIds==reason.conceptId}'>
							selected="selected"
							</c:if>
							</c:forEach>>${reason.name.name}</option>
							</c:forEach>
						</select></td>
						  
						  
						
						<td width="20%"><select name="drugList" multiple="multiple"
							size="10">
							<c:forEach var="drug" items="${drugs}">
								<option value="${drug.drugId}"
									<c:forEach var="drugIds" items="${drugIdback}">
							<c:if test='${drugIds==drug.drugId}'>
							selected="selected"
							</c:if>
							</c:forEach>>${drug.name}</option>
							</c:forEach>
						</select></td>
						
						<td width="20%"><select name="genericList"
							multiple="multiple" size="10">
							<c:forEach var="concept" items="${generics}">
								<option value="${concept.conceptId}"
									<c:forEach var="conceptIds" items="${conceptIdback}">
							<c:if test='${conceptIds==concept.conceptId}'>
							selected="selected"
							</c:if>
							</c:forEach>>${concept.name.name}</option>
							</c:forEach>
						</select></td>
					</tr>
				</table>
				</td>
			</tr>
		</table>
		</td>
	</tr>
</table>



<tr>
	<td colspan="2"><input type="hidden" name="formSubmitted"
		value="2" /> <input type="submit" id="myButton"
		value="<spring:message
			code="drugorderexport.searchSubmit" />" /></td>
</tr>

</form>
</div>
</div>
<br />
<c:if test="${fn:length(patientList)>0}">
	
	<div style="border: 1px #808080 solid; padding: 0em; margin: 0em;width:500"><!--(Start) This is repeated depending on the number of regimen of the patient -->
	<b class="boxHeader" style=" width:100% ;padding: 0em; margin-right: 0em;margin-left:0em ">


<div>
 Patients
 <c:if test="${gender=='Any'}">(<spring:message code="drugorderexport.anyGender" />) </c:if>
 <c:if test="${gender=='Male'}">(<spring:message code="drugorderexport.male" />) </c:if>
 <c:if test="${gender=='Female'}">(<spring:message code="drugorderexport.female" />) </c:if>
 
 <spring:message code="drugorderexport.whoStopped" />
 
 <c:if test="${drugList=='Any ARVs' }"><spring:message code="drugorderexport.anyARVs" /></c:if>
 <c:if test="${drugList!='Any ARVs' }">${drugList }</c:if>
 
 
 <c:if test="${!empty generic }"> <spring:message code="drugorderexport.formOf" /> ${generic}</c:if>
  <c:if test="${!empty reason }"> <spring:message code="drugorderexport.causeOf" /> ${reason}</c:if>
 
  <c:if test="${!empty startdate && !empty enddate}"><spring:message code="drugorderexport.inThePeriod" /> &nbsp;<spring:message code="drugorderexport.between" />&nbsp; ${startdate }&nbsp; <spring:message code="drugorderexport.and" /> &nbsp; ${enddate }&nbsp;</c:if>
 <c:if test="${!empty startdate && empty enddate}"> <spring:message code="drugorderexport.from" /> &nbsp; ${startdate }&nbsp; <spring:message code="drugorderexport.upToNow" /></c:if>
 <c:if test="${empty startdate && !empty enddate}"> <spring:message code="drugorderexport.before" /> &nbsp; ${enddate }&nbsp; </c:if>
 

  <c:if test="${!empty minAge && !empty maxAge}"><spring:message code="drugorderexport.between" />&nbsp;<spring:message code="drugorderexport.theAgeOf" /> &nbsp; ${minAge }&nbsp; <spring:message code="drugorderexport.and" /> &nbsp; ${maxAge }&nbsp;</c:if>
 <c:if test="${!empty minAge && empty maxAge}"> <spring:message code="drugorderexport.atLeast" />  &nbsp; ${minAge }&nbsp; <spring:message code="drugorderexport.yearsOld" /> </c:if>
 <c:if test="${empty minAge && !empty maxAge}"> <spring:message code="drugorderexport.upTo" /> &nbsp; ${maxAge }&nbsp; <spring:message code="drugorderexport.yearsOld" /></c:if>
 
 &nbsp;&nbsp;&nbsp;&nbsp;: ${listSize }
</div>

 </b>
	
	
	<div id="dt_example">
	<div id="container">
   <!-- -------------------------------export buttons----------------------------------- -->
	<div align="center" style="margin-top: 0px">
	
	
	<table><tr><td>
	<form action="pWhoStoppedDrug.form?export=excel" method="post" name="export"><input
				type="hidden" name="linkId" value="${linkId }" /> <select
				name="reasonsList" multiple="multiple" size="10"
				style="display: none;">
				<c:forEach var="reason" items="${reasons}">
					<option value="${reason.conceptId}"
						<c:forEach var="reasonsIds" items="${reasonsIdback}">
							<c:if test='${reasonsIds==reason.conceptId}'>
							selected="selected"
							</c:if>
							</c:forEach>>${reason.name.name}</option>
				</c:forEach>
			</select> <select name="drugList" multiple="multiple" style="display: none;">
				<c:forEach var="drug" items="${drugs}">
					<option value="${drug.drugId}"
						<c:forEach var="drugIds" items="${drugIdback}">
							<c:if test='${drugIds==drug.drugId}'>
							selected="selected"
							</c:if>
							</c:forEach>>
					${drug.name}</option>
				</c:forEach>
			</select> <select name="genericList" multiple="multiple" size="10"
				style="display: none;">
				<c:forEach var="concept" items="${generics}">
					<option value="${concept.conceptId}"
						<c:forEach var="conceptIds" items="${conceptIdback}">
							<c:if test='${conceptIds==concept.conceptId}'>
							selected="selected"
							</c:if>
							</c:forEach>>${concept.name.name}</option>
				</c:forEach>
			</select> <input type="hidden" name="gender" value="${param.gender}" />
			 <input type="hidden" name="minAge" value="${param.minAge}" /> <input
				type="hidden" name="maxAge" value="${param.maxAge}" /> <input type="submit"
				value="<spring:message code="drugorderexport.export" />" /></form>
	</td><td>
	
	<!--    export to pdf button   -->
	
	<form action="pWhoStoppedDrug.form?export=pdf" method="post" name="export"><input
				type="hidden" name="linkId" value="${linkId }" /> <select
				name="reasonsList" multiple="multiple" size="10"
				style="display: none;">
				<c:forEach var="reason" items="${reasons}">
					<option value="${reason.conceptId}"
						<c:forEach var="reasonsIds" items="${reasonsIdback}">
							<c:if test='${reasonsIds==reason.conceptId}'>
							selected="selected"
							</c:if>
							</c:forEach>>${reason.name.name}</option>
				</c:forEach>
			</select> <select name="drugList" multiple="multiple" style="display: none;">
				<c:forEach var="drug" items="${drugs}">
					<option value="${drug.drugId}"
						<c:forEach var="drugIds" items="${drugIdback}">
							<c:if test='${drugIds==drug.drugId}'>
							selected="selected"
							</c:if>
							</c:forEach>>
					${drug.name}</option>
				</c:forEach>
			</select> <select name="genericList" multiple="multiple" size="10"
				style="display: none;">
				<c:forEach var="concept" items="${generics}">
					<option value="${concept.conceptId}"
						<c:forEach var="conceptIds" items="${conceptIdback}">
							<c:if test='${conceptIds==concept.conceptId}'>
							selected="selected"
							</c:if>
							</c:forEach>>${concept.name.name}</option>
				</c:forEach>
			</select> <input type="hidden" name="gender" value="${param.gender}" />
			 <input type="hidden" name="minAge" value="${param.minAge}" /> <input
				type="hidden" name="maxAge" value="${param.maxAge}" /> <input type="submit"
				value="<spring:message code="drugorderexport.exportPdf" />" /></form></td>
				</tr></table>

	</div>
	<!-- ----------------------------end---export buttons----------------------------------- -->
	
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
				<th></th>
				<th></th>
			</tr>
		</thead>

		<tbody>
			<c:forEach var="patient" items="${patientList}" varStatus="status">
				<tr>
					<td>&nbsp; ${patient.patientIdentifier}</td>
					<openmrs:hasPrivilege privilege="View Patient Names">
					<td>&nbsp; ${patient.givenName}</td>
					<td>&nbsp; ${patient.familyName}</td>
					</openmrs:hasPrivilege>
					<td>&nbsp; ${patient.age}</td>
					<td>&nbsp; <img
						src="${pageContext.request.contextPath}/images/${patient.gender == 'M' ? 'male' : 'female'}.gif" /></td>
					<td>&nbsp; <a
						href="${pageContext.request.contextPath}/patientDashboard.form?patientId=${patient.patientId}"><spring:message
						code="drugorderexport.viewdashboard" /></a></td>
					<td>&nbsp; 
					<openmrs:hasPrivilege privilege="View Patient Regimens History">
					<a
						href="${pageContext.request.contextPath}/module/drugorderexport/showPatientRegimens.form?patient=${patient.patientId}"><spring:message
						code="drugorderexport.viewregimen" /></a>
						</openmrs:hasPrivilege>
					</td>
				</tr>
			</c:forEach>
		</tbody>

		<!-- End -->
	</table>
	</div>
	</div>
	</div>

</c:if>
<!-- down -->
<%@ include file="/WEB-INF/template/footer.jsp"%>