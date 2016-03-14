<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>
<%@ taglib prefix="fn" uri="/WEB-INF/taglibs/fn.tld"%>

<openmrs:require privilege="View HIV Drugs Reporting" otherwise="/login.htm" redirect="/module/@MODULE_ID@/drugorderView.form" />


<openmrs:htmlInclude file="/scripts/calendar/calendar.js" />

<openmrs:htmlInclude
	file="/moduleResources/drugorderexport/validatordrugorderview.js" />

<openmrs:htmlInclude file="/moduleResources/drugorderexport/jquery.js" />

<openmrs:htmlInclude
	file="/moduleResources/drugorderexport/jquery.dataTables.js" />

<openmrs:htmlInclude
	file="/moduleResources/drugorderexport/demo_page.css" />

<openmrs:htmlInclude
	file="/moduleResources/drugorderexport/demo_table.css" />

<openmrs:htmlInclude
	file="/moduleResources/drugorderexport/functions.js" />
	

<script type="text/javascript" charset="utf-8">
		$(document).ready(function() {
			$('#example').dataTable( {
				"sPaginationType": "full_numbers"
			} );
		    $('#drugsId').show();
			$('#conceptsId').hide();
			
			$("input[name=rdBtn]").click(function() {
				var rdBtn = $('input[name=rdBtn]:checked').val();
				
				if(rdBtn == 'drugs') {
					$("#drugsId").show();
					$("#conceptsId").hide();
				}
				if(rdBtn == 'concepts') {
					$("#drugsId").hide();
					$("#conceptsId").show();
				}
			});
			
		} );
</script>


 
<%@ include file="localHeader.jsp"%>



<b class="boxHeader" style="width: 100%"><spring:message
	code="drugorderexport.patienttakingspecificdrugs" /></b>

<div
	style="background: #E2E4FF; border: 1px #808080 solid; padding: 0.5em; margin-bottom: 0em">

<div>
<form action="drugorderView.form" method="post" name="specificDrug"
	onsubmit="return checkform(this);">
<table>
	<tr>
		<td></td>
		<td>
		<table valign="top">
			<tr>
			</tr>
			<tr>
				<td>
				<table width="500" align="left" cellspacing="0" cellpadding="2">
					<tr>
						<td><select name="anyOrAll">
							<option value="any" ${param.anyOrAll==
								"any" ? 'selected="selected"':''}><spring:message
								code="drugorderexport.any" /></option>
							<option value="all" ${param.anyOrAll==
								"all" ? 'selected="selected"':''}><spring:message
								code="drugorderexport.all" /></option>
							<option value="none" ${param.anyOrAll==
								"none" ? 'selected="selected"':''}><spring:message
								code="drugorderexport.none" /></option>
						</select></td>
						<td><spring:message code="Of the following" /><br/>
						
						</td>
					</tr>

					<tr>
					<!-- <td width="20%" valign="top"><spring:message code="drugorderexport.drugs" />:</td>  -->
					<td width="20%" valign="top">
					
					  <b>Drugs: <input type="radio" name="rdBtn" value="drugs" checked="checked"/></b>
					  
					  <b>Concepts: <input type="radio" name="rdBtn" value="concepts" /></b>
					</td>
						<td>
						<div id="drugsId">
							<select name="drugList" multiple="multiple"
								size="10">
								<c:forEach var="drug" items="${drugs}">
									<option value="${drug.drugId}"
										<c:forEach var="drugId" items="${drugIdback}">
								<c:if test='${drugId==drug.drugId}'>
								selected="selected"
								</c:if>
								</c:forEach>>
									${drug.name}</option>
								</c:forEach>
							</select>
						</div>
						
						<div id="conceptsId">
							<select name="drugConceptsList" multiple="multiple" size="10">
					<c:forEach var="drugsObj" items="${hivDrugsObj}">
					 <option value="${drugsObj[0]}"
						<c:forEach var="conceptId" items="${conceptIdsback}">
							<c:if test='${conceptId==drugsObj[0]}'>
							selected="selected"
							</c:if>	
						</c:forEach>>
						${drugsObj[1]}</option>
					</c:forEach>
			</select>
						</div>
						</td>
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
							code="drugorderexport.optional" /><input type="hidden"
							name="all" value="all" /></td>
					</tr>



				</table>
				</td>
			</tr>
		</table>
		</td>
		<td valign="top">
		<table
			style="background: #E2E4FF; -moz-border-radius: 4px; -webkit-border-radius: 4px;">
			<tr>
				<td colspan="2"><b><spring:message
					code="drugorderexport.optionalAttributes" /></b></td>
			</tr>
			<tr>
				<td><spring:message code="drugorderexport.gender" />:</td>
				<td><select name="gender">
					<option value="" ${param.gender== " " ? 'selected="selected"':''}><spring:message
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
					code="drugorderexport.and" /> <input type="text" name="maxAge"
					size="3" value="${param.maxAge}" /> <spring:message
					code="drugorderexport.years" /></td>
			</tr>

			<tr>
				<td><spring:message code="drugorderexport.birthday" />:</td>
				<td><spring:message code="drugorderexport.between" /> <input
					type="text" id="minBirthdate" size="11"
					value="${param.minBirthdate}" name="minBirthdate"
					onclick="showCalendar(this)" /> <spring:message
					code="drugorderexport.and" /> <input type="text" id="maxBirthdate"
					size="11" value="${param.maxBirthdate}" name="maxBirthdate"
					onclick="showCalendar(this)" /></td>
			</tr>

		</table>
		</td>
	</tr>
	<tr>

		<td colspan="2"><input type="hidden" name="formSubmitted"
			value="1" /> <input type="submit"
			value="<spring:message
			code="drugorderexport.searchSubmit"/>" />
			
			<spring:message code="drugorderexport.activeOnly" /><input type="checkbox" name="checkValue" value="1"/>
			</td>
			
	</tr>
	
</table>

</form>
</div>
</div>

<br />



<c:if test="${fn:length(objectsList)>0}">

	<br>
	<br>

	<div
		style="border: 1px #808080 solid; padding: 0em; margin: 0em; width: 500"><!--(Start) This is repeated depending on the number of regimen of the patient -->
	<b class="boxHeader"
		style="width: 100%; padding: 0em; margin-right: 0em; margin-left: 0em">



	<div>Patients <c:if test="${gender=='Any'}">(<spring:message
			code="drugorderexport.anyGender" />) </c:if> <c:if test="${gender=='Male'}">(<spring:message
			code="drugorderexport.male" />)</c:if> <c:if test="${gender=='Female'}">(<spring:message
			code="drugorderexport.female" />) </c:if> <label><spring:message
		code="drugorderexport.action" /></label> <c:if test="${anyOrAll=='any'}">
		<spring:message code="drugorderexport.anyOf" />&nbsp; </c:if> <c:if
		test="${anyOrAll=='all'}">
		<spring:message code="drugorderexport.allOf" />&nbsp;  </c:if> <c:if
		test="${anyOrAll=='none'}">
		<spring:message code="drugorderexport.noneOf" />&nbsp;  </c:if> ( ${drugList
	} ) <c:if test="${!empty startdate && !empty enddate}">
		<spring:message code="drugorderexport.inThePeriod" /> &nbsp;<spring:message
			code="drugorderexport.between" />&nbsp; ${startdate }&nbsp; <spring:message
			code="drugorderexport.and" /> &nbsp; ${enddate }&nbsp;</c:if> <c:if
		test="${!empty startdate && empty enddate}">
		<spring:message code="drugorderexport.from" /> &nbsp; ${startdate }&nbsp; <spring:message
			code="drugorderexport.upToNow" />
	</c:if> <c:if test="${empty startdate && !empty enddate}">
		<spring:message code="drugorderexport.before" /> &nbsp; ${enddate }&nbsp; </c:if>


	<c:if test="${!empty minAge && !empty maxAge}">
		<spring:message code="drugorderexport.between" />&nbsp;<spring:message
			code="drugorderexport.theAgeOf" /> &nbsp; ${minAge }&nbsp; <spring:message
			code="drugorderexport.and" /> &nbsp; ${maxAge }&nbsp;</c:if> <c:if
		test="${!empty minAge && empty maxAge}">
		<spring:message code="drugorderexport.atLeast" />  &nbsp; ${minAge }&nbsp; <spring:message
			code="drugorderexport.yearsOld" />
	</c:if> <c:if test="${empty minAge && !empty maxAge}">
		<spring:message code="drugorderexport.upTo" /> &nbsp; ${maxAge }&nbsp; <spring:message
			code="drugorderexport.yearsOld" />
	</c:if> <c:if test="${!empty minBirthdate && !empty maxBirthdate}">
		<spring:message code="drugorderexport.dob" />&nbsp;<spring:message
			code="drugorderexport.between" />&nbsp; ${minBirthdate }&nbsp; <spring:message
			code="drugorderexport.and" /> &nbsp; ${maxBirthdate }&nbsp;</c:if> <c:if
		test="${!empty minBirthdate && empty maxBirthdate}">
		<spring:message code="drugorderexport.dob" />&nbsp;<spring:message
			code="drugorderexport.from" />  &nbsp; ${minBirthdate }&nbsp; </c:if> <c:if
		test="${empty minBirthdate && !empty maxBirthdate}">
		<spring:message code="drugorderexport.dob" />&nbsp;<spring:message
			code="drugorderexport.before" /> &nbsp; ${maxBirthdate }&nbsp; </c:if>

	&nbsp;&nbsp;&nbsp;&nbsp;: ${listSize }</div>

	<!--    end report div --> </b>


	<div>
	<div id="dt_example">
	<div id="container">


	<div align="center">

	<table>
		<tr>
			<td>
			<form action="drugorderView.form?export=excel" method="post">
			 <input type="hidden" id="startdate" value="${param.startdate}"	name="startdate" /> 
			 <input type="hidden" id="enddate"
				value="${param.enddate}" name="enddate" /> <input type="hidden"
				name="anyOrAll" value="${param.anyOrAll}" /><input type="hidden"
				name="gender" value="${param.gender}" /> <input type="hidden"
				name="minAge" value="${param.minAge}" /> <input type="hidden"
				name="maxAge" value="${param.maxAge}" /> <input type="hidden"
				name="minBirthdate" value="${param.minBirthdate}" /> <input
				type="hidden" name="maxBirthdate" value="${param.maxBirthdate}" />
				
				<input type="hidden" name="checkValue" value="${checkedValueExport}"/>
				
				<input type="hidden" name="rdBtn" value="${rdBtn }" />
			
                
			<select name="drugList" multiple="multiple" style="display: none">
				<c:forEach var="drug" items="${drugs}">
					<option value="${drug.drugId}"
						<c:forEach var="drugIds" items="${drugIdback}">
							<c:if test='${drugIds==drug.drugId}'>
							selected="selected"
							</c:if>
							</c:forEach>>
					${drug.name}</option>
				</c:forEach>
			</select> 
			
			<select name="drugConceptsList" multiple="multiple" style="display: none">
					<c:forEach var="drugsObj" items="${hivDrugsObj}">
					 <option value="${drugsObj[0]}"
						<c:forEach var="conceptIds" items="${conceptIdsback}">
							<c:if test='${conceptIds==drugsObj[0]}'>
							selected="selected"
							</c:if>	
						</c:forEach>>
						${drugsObj[1]}</option>
					</c:forEach>
			</select>
		
			
			
			<input type="submit" value="<spring:message code="drugorderexport.export" />" />
			</form>
			</td>
			<td>
			
			<!-- other form for pdf exportation    -->
			<form action="drugorderView.form?export=pdf" method="post"> <input
				type="hidden" id="startdate" value="${param.startdate}"
				name="startdate" /> <input type="hidden" id="enddate"
				value="${param.enddate}" name="enddate" /> <input type="hidden"
				name="anyOrAll" value="${param.anyOrAll}" /><input type="hidden"
				name="gender" value="${param.gender}" /> <input type="hidden"
				name="minAge" value="${param.minAge}" /> <input type="hidden"
				name="maxAge" value="${param.maxAge}" /> <input type="hidden"
				name="minBirthdate" value="${param.minBirthdate}" /> <input
				type="hidden" name="maxBirthdate" value="${param.maxBirthdate}" />

			<input type="hidden" name="checkedValueExport" value="${checkedValueExport}"/>
			
			<input type="hidden" name="rdBtn" value="${rdBtn }"/>

			<select name="drugList" multiple="multiple" style="display: none;">
				<c:forEach var="drug" items="${drugs}">
					<option value="${drug.drugId}"
						<c:forEach var="drugIds" items="${drugIdback}">
							<c:if test='${drugIds==drug.drugId}'>
							selected="selected"
							</c:if>
							</c:forEach>>
					${drug.name}</option>
				</c:forEach>
			</select> 
			
			
			
			<select name="drugConceptsList" multiple="multiple" style="display: none">
					<c:forEach var="drugsObj" items="${hivDrugsObj}">
					 <option value="${drugsObj[0]}"
						<c:forEach var="conceptIds" items="${conceptIdsback}">
							<c:if test='${conceptIds==drugsObj[0]}'>
							selected="selected"
							</c:if>	
						</c:forEach>>
						${drugsObj[1]}</option>
					</c:forEach>
			</select>
			
			
			<input type="submit" value="<spring:message code="drugorderexport.exportPdf" />" /></form>
			</td>
		</tr>
	</table>
	<!--   div containing reports --></div>

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
						<td>&nbsp;${list[1]}</td>
						<td>&nbsp;${list[2]}</td>
						<td>&nbsp;${list[3]}</td>
						
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
		<!-- End -->
	</table>
	</div>
	</div>
	</div>
	</div>
</c:if>
<!-- down -->
<%@ include file="/WEB-INF/template/footer.jsp"%>