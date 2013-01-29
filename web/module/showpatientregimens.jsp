<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>

<openmrs:htmlInclude file="/moduleResources/drugorderexport/jquery.js" />
<openmrs:htmlInclude
	file="/moduleResources/drugorderexport/jquery.PrintArea.js" />

<openmrs:htmlInclude
	file="/moduleResources/drugorderexport/validatorform.js" />




<br />

<%@ include file="localHeader.jsp"%>

<c:if test="${fn:length(listPatientHistory)>0}">
	<!-- top -->

	<b class="boxHeader" style="width: 100%"><spring:message
		code="drugorderexport.patientregimenhistory" /></b>

	<div class="box" style="width: 100%"><!--(Start) This is repeated depending on the number of patient in the patient -->
	<table width="100%">
		<tr>

			<div style="border: 1px #E2E4FF;">
			<table width="100%" border="0">
				<tr
					style="border: #e7e6e2 solid 0px; background-color: #E2E4FF; color: #333; font-weight: bold">
					<td width="100%"><a
						href="${pageContext.request.contextPath}/patientDashboard.form?patientId=${patient.patientId}">
					<spring:message code="drugorderexport.viewdashboard" /></a> <br />
					<br />
				
					<spring:message code="drugorderexport.names" />:
					${patient.givenName} ${patient.familyName} <br />
					<spring:message code="drugorderexport.gender" />:<img
						src="/openmrs/images/${patient.gender == 'M' ? 'male' : 'female'}.gif" />
					<spring:message code="drugorderexport.age" />: ${patient.age} <br />
					<spring:message code="drugorderexport.programs" />: <c:forEach
						var="program" items="${program}">${program.program.name}  </c:forEach></td>
				</tr>

			</table>
			</div>

			<div style="border: 1px #E2E4FF;">
			<table width="100%" border="0" cellspacing="0" cellpadding="2">
				<tr
					style="background-color: #8fabc7; color: #fff; font-weight: bold;">
					<td width="25%"><spring:message code="drugorderexport.drugs" /></td>
					<td width="25%"><spring:message
						code="drugorderexport.startdate" /></td>
					<td width="25%"><spring:message code="drugorderexport.enddate" /></td>
					<td width="25%"><spring:message code="drugorderexport.reason" /></td>
				</tr>

			</table>
			</div>

			<td><c:forEach var="list" items="${listPatientHistory}">
				<div style="border: 1px #E2E4FF;"><!--(Start) This is repeated depending on the number of regimen of the patient -->
				<table width="100%" border="0" cellspacing="0" cellpadding="2">
					<tr>
						<td colspan="4" style="background-color: #f9f9f9; color: #666;">
						
						<label style="font-weight: bold;">From</label>  <openmrs:formatDate date="${list[0].startDate}" type="medium" /> 
						<label style="font-weight: bold;">To </label> <openmrs:formatDate   date="${list[0].endDate}" type="medium" />

						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
						
						<label style="font-weight: bold;">CD4 Count :</label>${list[1]} 
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
						<label 	style="font-weight: bold;">WEIGHT : </label> ${list[2]}
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
						<label 	style="font-weight: bold;">HIV VIRAL LOAD : </label> ${list[3]}
						
						
						</td>


					</tr>

					<!--(Start) This is reapeted depending on the number of drugs by regimen -->
					<c:forEach var="drugs" items="${list[0].components}">

						<c:if
							test="${empty drugs.stopDate || drugs.stopDate>list[0].startDate}">

							<tr>
							   
								<td width="25%">
								        <c:if test="${!empty drugs.drug.name}">
								            ${drugs.drug.name}
								        </c:if>
								        <c:if test="${empty drugs.drug.name}">
								            ${drugs.generic.name}
								        </c:if>
								</td>
								
								
								<td width="25%"><openmrs:formatDate
									date="${drugs.startDate}" type="medium" /></td>
								<td width="25%"><c:if
									test="${drugs.stopDate<=list[0].endDate}">
									<openmrs:formatDate date="${drugs.stopDate}" type="medium" />
									<td width="25%">${drugs.stopReason.name}</td>
								</c:if></td>
								
							</tr>

						</c:if>
						
					</c:forEach>

				</table>
				</div>
			</c:forEach></td>
		</tr>
	</table>

	</div>

</c:if>
<%@ include file="/WEB-INF/template/footer.jsp"%>