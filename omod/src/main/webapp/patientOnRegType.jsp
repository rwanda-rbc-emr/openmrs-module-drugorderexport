<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>
<%@ taglib prefix="fn" uri="/WEB-INF/taglibs/fn.tld"%>

<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Map" %>
<%@ page import="org.openmrs.Drug" %>

<openmrs:htmlInclude file="/scripts/calendar/calendar.js" />

<openmrs:htmlInclude
	file="/moduleResources/drugorderexport/validatordrugorderview.js" />

<openmrs:htmlInclude
	file="/moduleResources/drugorderexport/jquery.dataTables.js" />
	

<openmrs:htmlInclude
	file="/moduleResources/drugorderexport/demo_page.css" />

<openmrs:htmlInclude
	file="/moduleResources/drugorderexport/demo_table.css" />

<openmrs:htmlInclude
	file="/moduleResources/drugorderexport/functions.js" />
	
<!--  the following includes are for the popup    -->
	<openmrs:htmlInclude file="/moduleResources/drugorderexport/basic.css" />
	<openmrs:htmlInclude file="/moduleResources/drugorderexport/demo.css" />
	<openmrs:htmlInclude file="/moduleResources/drugorderexport/basic.js" />
	<openmrs:htmlInclude file="/moduleResources/drugorderexport/jquery.simplemodal.js" />
	
<!-- for viewing the drugs categories  -->
<!-- openmrs:htmlInclude file="/moduleResources/drugorderexport/jquery-latest.js.js" /-->

<script type="text/javascript" charset="utf-8">
		var $ = jQuery;

		$(document).ready(function() {
			$('#example').dataTable( {
				"sPaginationType": "full_numbers"
			} );
	
		} );

		
</script>




<script type="text/javascript">
	var id_str =null;
	var $ = jQuery;
	
	$(document).ready(function() {
		$('.basic').click(function() {
			var id_str = this.id;
	
			displayDrugs(id_str);
			
		});
	
	
		function displayDrugs(typeId) {
			$.get("${pageContext.request.contextPath}/module/drugorderexport/patOnRegimenType.form",{
				drugTypeId:typeId
				}, function(data) {
					var drugs = $("<c:out value='${drugs}' />", data).text();
					var results = $("drugs", data).text();
				
				$("#toDisplay").html(drugs);
				}, "html"
				);
				
		}
	
		function viewDrugDetails(cat){
			$(document).ready(function() {
				$('#prophyDiv').hide();
				$('#arvDiv').hide();
				$('.basic').click(function() {
					var id_str = this.id;
					$("#prophyDiv").show();
					
				});
				} );
			
		}
	});
</script>

<script type="text/javascript">
var $ = jQuery;

$(document).ready(function() {
	/////////////////////////arv div//////////////////////////
	//var l=${catLink};
	//alert("catttttttttttttttttttttttttttttttt "+l);
  $('#arvDiv').hide();
  $('#arv_show').click(function() {
    $('#arvDiv').show('slow');
    return false;
  });

  
  $('#arv_hide').click(function() {
    $('#arvDiv').hide('fast');
    return false;
  });

	///////////////////prophylaxis div//////////////////////////
  $('#prophyDiv').hide();
  $('#prophy_show').click(function() {
    $('#prophyDiv').show('slow');
    return false;
  });

  
  $('#prophy-hide').click(function() {
    $('#prophyDiv').hide('fast');
    return false;
  });
  


});
	
</script>

<!-- top -->
<br />
<%@ include file="localHeader.jsp"%>

<b class="boxHeader" style="width: 100%"><spring:message
	code="drugorderexport.patientOnRegimenType" /></b>

<div
	style="background: #E2E4FF; border: 1px #808080 solid; padding: 0.5em; margin-bottom: 0em">

<div>
<form action="patOnRegimenType.form" method="post" name="specificDrug"
	onsubmit="return checkform(this);"><input type="hidden"
	name="linkId" value="${linkId}" />
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
						<td width="20%" valign="top"><strong><spring:message code="drugorderexport.regimenCategories" /></strong></td>

					</tr>
					
						<tr><td>-<spring:message code="drugorderexport.ARVs" /> </td><td><input type="checkbox" name="arv" value="arv"/></td></tr>
						<tr><td>-<spring:message code="drugorderexport.prophylaxis" /></td><td><input type="checkbox" name="prophylaxis" value="prophylaxis" /></td></tr>
						<tr><td>-<spring:message code="drugorderexport.firstReg" /></td><td><input type="checkbox" name="firstLine" value="firstLine" /></td></tr>
						<tr><td>-<spring:message code="drugorderexport.secondReg" /></td> <td><input type="checkbox"  name="secondLine" value="secondLine" /></td></tr>
						<tr><td>-<spring:message code="drugorderexport.thirdLineReg" /></td> <td><input type="checkbox"  name="thirdLine" value="thirdLine" /></td></tr>
					

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

<div id="regCategories">
<table>
 	<c:forEach var="cat" items="${categoryPatients}">
		<tr>
		<td>Patients on <a href="#?cat=${cat.key }"  title="${drugsMap[cat.key] }" id=arv_show <c:set var="catLink" value="${cat.key }"/> > ${cat.key }: 	
		  <a href="${pageContext.request.contextPath}/module/drugorderexport/viewPatOnRegimenCateg.form?viewCategory=${cat.key}">${fn:length(cat.value)}</a>  
		
		
		</td>
		</tr>
	</c:forEach>

</table>
</div>


<!--  pagination div   -->
<c:if test="${fn:length(exportPatients)>0}">   
<div style="display: block">
<b>Patient list</b>
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
				<th><spring:message code="drugorderexport.lastEncounterDate" /></th>
				<th><spring:message code="drugorderexport.lastVisitDate" /></th>
				<th></th>
				<th></th>
			</tr>
		</thead>
		<tbody>
			
			<c:forEach var="patient" items="${param.index}" varStatus="status">
				<tr>
					<td>&nbsp; ${patient.patientIdentifier}</td>
					<openmrs:hasPrivilege privilege="View Patient Names">
					<td>&nbsp; ${patient.givenName}</td>
					<td>&nbsp; ${ patient.familyName}</td>
					</openmrs:hasPrivilege>
					<td>&nbsp; ${patient.age}</td>
					<td>&nbsp; <img
						src="${pageContext.request.contextPath}/images/${patient.gender == 'M' ? 'male' : 'female'}.gif" /></td>
					<td></td>
					<td></td>
					<td>&nbsp;<a
						href="${pageContext.request.contextPath}/patientDashboard.form?patientId=${patient.patientId}">View
					Dashboard</a></td>
					<td>&nbsp; <a
						href="${pageContext.request.contextPath}/module/drugorderexport/showPatientRegimens.form?patient=${patient.patientId}"><spring:message
						code="drugorderexport.viewregimen" /></a></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</div>
</c:if> 



<br />



<div id="arvDiv">
<h3>${arv_title } </h3>

<hr/>
<a id="arv_hide" style="float: right;">hide</a>
<c:forEach var="d" items="${arvDrugs}" varStatus="status">
<table>
<tr> 
<td>${status.index+1}.</td>
<td><a href="/openmrs/admin/concepts/conceptDrug.form?drugId=${d.drugId }"> ${d.name}</a></td>
</tr>
</table>
</c:forEach>
</div>

<div id="prophyDiv">
<h3>${prophylaxis_title } </h3>

<hr/>

<c:forEach var="d" items="${prophylaxisDrugs}" varStatus="status">
<table>
<tr> 
<td>${status.index+1}.</td>
<td><a href="/openmrs/admin/concepts/conceptDrug.form?drugId=${d.drugId }"> ${d.name}</a></td>
</tr>
</table>
</c:forEach>
<a id="prophy_arv">hide</a>
</div>




<!-- down -->
<%@ include file="/WEB-INF/template/footer.jsp"%>
