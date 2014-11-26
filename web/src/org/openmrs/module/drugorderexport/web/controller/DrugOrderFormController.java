/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.drugorderexport.web.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Drug;
import org.openmrs.Patient;
import org.openmrs.PatientProgram;
import org.openmrs.api.context.Context;
import org.openmrs.module.drugorderexport.DrugOrderControllerMessage;
import org.openmrs.module.drugorderexport.DrugOrderExportUtil;
import org.openmrs.module.drugorderexport.service.DrugOrderService;
import org.openmrs.util.OpenmrsUtil;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

/**
 * This controller backs the /web/module/basicmoduleForm.jsp page. This controller is tied to that
 * jsp page in the /metadata/moduleApplicationContext.xml file
 */
public class DrugOrderFormController extends ParameterizableViewController {
	
	/** Logger for this class and subclasses */
	protected final Log log = LogFactory.getLog(getClass());
	
	/**
	 * Returns any extra data in a key-->value pair kind of way
	 * 
	 * @see org.springframework.web.servlet.mvc.SimpleFormController#referenceData(javax.servlet.http.HttpServletRequest,
	 *      java.lang.Object, org.springframework.validation.Errors)
	 */
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		DrugOrderService service = Context.getService(DrugOrderService.class);
		
		Map<String, Object> map = new HashMap<String, Object>();
		
//		Collection<Drug> drugs = Context.getConceptService().getAllDrugs();
		
		Collection<Drug> drugs = service.getArvDrugs();
		
//		List<Concept> genericDrugs = Context.getConceptService().getConceptsWithDrugsInFormulary();
		
		List<Object[]> hivDrugsObj = service.getArvDrugsByConcepts();
		
		
//		ConceptService cs = Context.getConceptService();
//		
//		List<Concept> orderStopReasons = new ArrayList<Concept>();
//		
//		Concept c = cs.getConceptByName("REASON ORDER STOPPED");
//		if (c != null)
//			orderStopReasons.addAll(cs.getConceptsByConceptSet(c));
//		if ((c != null) && (c.getAnswers() != null)) {
//			for (ConceptAnswer ca : c.getAnswers())
//				orderStopReasons.add(ca.getAnswerConcept());
//		}
		
//		log.info("gggggggggggggggggggggggggggggggggggggggg "+genericDrugs);
		
		map.put("drugs", drugs);
//		map.put("generics", genericDrugs);
//		map.put("reasons", orderStopReasons);
		map.put("hivDrugsObj", hivDrugsObj);
		
		if (request.getMethod().equalsIgnoreCase("post")) {
			doSearch(map, request, response);
		}
		
		return new ModelAndView(getViewName(), map);
	}
	
	/**
	 * @see org.springframework.web.servlet.mvc.SimpleFormController#onSubmit(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse, java.lang.Object,
	 *      org.springframework.validation.BindException)
	 */
	
	@SuppressWarnings("unchecked")
	protected void doSearch(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response)
	                                                                                                            throws Exception {
		
		List<Integer> patientIds = null;
		List<Patient> patients = new ArrayList<Patient>();
		List<Object[]> objectsList = new ArrayList<Object[]>();
		List<Integer> drugIds = new ArrayList<Integer>();
		List<Integer> drugsByConceptsIds = new ArrayList<Integer>();
		
		String checkedValueStr[] = null;
		int checkedValue = 0;
		
		String rdBtn=request.getParameter("rdBtn");
		DrugOrderService service = Context.getService(DrugOrderService.class);
		
			if (request.getParameterValues("checkValue") != null) {
				checkedValueStr = request.getParameterValues("checkValue");
				//log.info(">>>>>>>>>>>>>>>>>>checkedValue>>>>>>>>>>>>>>>>>> " + checkedValueStr[0]);
				checkedValue = Integer.parseInt(checkedValueStr[0]);
			}
			
			
			String gender = request.getParameter("gender"), mnAge = request.getParameter("minAge"), mxAge = request
			        .getParameter("maxAge"), mnBirthdate = request.getParameter("minBirthdate"), mxBirthdate = request
			        .getParameter("maxBirthdate"), anyOrAll = request.getParameter("anyOrAll"), startD = request
			        .getParameter("startdate"), endD = request.getParameter("enddate");
			
			String[] drugs = request.getParameterValues("drugList");
			String[] drugsByConcepts = request.getParameterValues("drugConceptsList"); 
			
			
			/**********************************************************************************/
			// get drug by id
			if (drugs != null) {
				for (String drug : drugs) {
					drugIds.add(Integer.parseInt(drug));
				}
			}
			else{
				for (Drug d : service.getArvDrugs()) {
					drugIds.add(d.getDrugId());
				}
			}
			/**********************************************************************************/
			// get drug by concept id
			if (drugsByConcepts != null) {
				for (String conceptStr : drugsByConcepts) {
					drugsByConceptsIds.add(Integer.parseInt(conceptStr));
				}
			}
			else{
				for (Object obj[] : service.getArvDrugsByConcepts()) {
					drugsByConceptsIds.add((Integer) obj[0]);
				}
			}
			
			
			Date minBirthdate = null;
			Date maxBirthdate = null;
			Date minAge = null;
			Date maxAge = null;
			Date startDate = null;
			Date endDate = null;
			
			DrugOrderControllerMessage message = new DrugOrderControllerMessage();
			String arvConceptIds = DrugOrderExportUtil.gpGetARVConceptIds();
			String arvDrugIds = DrugOrderExportUtil.gpGetARVDrugsIds();
			/*
			 * Data validation
			 */
			SimpleDateFormat df = OpenmrsUtil.getDateFormat();
			if (startD != null && startD.length() != 0) {
				startDate = df.parse(startD);
			}
			if (endD != null && endD.length() != 0) {
				endDate = df.parse(endD);
			}
			
			if (mnBirthdate != null && mnBirthdate.length() != 0) {
				minBirthdate = df.parse(mnBirthdate);
			}
			if (mxBirthdate != null && mxBirthdate.length() != 0) {
				maxBirthdate = df.parse(mxBirthdate);
			}
			
			if (mxAge != null && mxAge.length() != 0) {
				maxAge = service.getDateObjectFormAge(Integer.parseInt(mxAge));
			}
			if (mnAge != null && mnAge.length() != 0) {
				minAge = service.getDateObjectFormAge(Integer.parseInt(mnAge));
			}
			
			
			if(rdBtn.equals("drugs")){
				patientIds = service.searchDrugOrderByDrug(anyOrAll, startDate, endDate, service.getListOfDrugsByIds(drugIds),
					    gender, minAge, maxAge, minBirthdate, maxBirthdate);
					
					if (checkedValue == 1){
						List<Integer> allPatients = service.searchDrugOrderByDrug(anyOrAll, startDate, endDate, service.getListOfDrugsByIds(drugIds),
							    gender, minAge, maxAge, minBirthdate, maxBirthdate);
						
						patientIds = service.getActiveOnDrugsPatients(allPatients,arvDrugIds, endDate);

					}
			}
			else if(rdBtn.equals("concepts")){
				patientIds = service.searchDrugOrderByDrugsConcepts(anyOrAll, startDate, endDate, service.getListOfDrugsByConceptsIds(drugsByConceptsIds),
					    gender, minAge, maxAge, minBirthdate, maxBirthdate);

					if (checkedValue == 1)
						patientIds=service.searchDrugOrderByDrugActive(anyOrAll, startDate, endDate, service.getListOfDrugsByConceptsIds(drugsByConceptsIds), gender, minAge, maxAge, minBirthdate, maxBirthdate);
					
			}
			
			for (Integer patientId : patientIds) {
				Patient patient = new Patient();
				
				patient = Context.getPatientService().getPatient(patientId);
				
				if (!patient.getVoided())
					patients.add(patient);
				
				Date startTreatmentDate = null;
				Date lastEncounterDate = null;
				Date lastVisitDate = null;
				String startTreatmentDateStr = "";
				String lastEncounterDateStr = "";
				String lastVisitDateStr = "";
				
				if (service.getPatientLastVisitDate(patientId) != null) {
					lastVisitDate = service.getPatientLastVisitDate(patientId);
					lastVisitDateStr = lastVisitDate.toString();
				}
				if (service.getWhenPatStartedXRegimen(patientId, DrugOrderExportUtil.gpGetARVDrugsIds()) != null) {
					startTreatmentDate = service.getWhenPatientStarted(patient);
					startTreatmentDateStr = startTreatmentDate.toString();
				}
				if (service.getPatientLastEncounterDate(patientId) != null) {
					lastEncounterDate = service.getPatientLastEncounterDate(patientId);
					lastEncounterDateStr = lastEncounterDate.toString();
				}

				List<String> programs = new ArrayList<String>();
				String progAndDate ="";
				Collection<PatientProgram> patientPrograms = Context.getProgramWorkflowService().getPatientPrograms(patient, null, null, null, null, null, false);
				Date date=null;
				if(endDate==null)
					endDate=new Date();
				for (PatientProgram pg : patientPrograms) {
					date = pg.getDateCompleted();
					if(date==null)
					progAndDate+=pg.getProgram().getName()+"(Still Enrolled),";
					else if(date!=null && date.getTime()<=endDate.getTime())
						progAndDate+=pg.getProgram().getName()+" (Completed on "+df.format(pg.getDateCompleted())+"),";
				}
			objectsList.add(new Object[] { Context.getPersonService().getPerson(patientId), startTreatmentDateStr,
			        lastEncounterDateStr, lastVisitDateStr,progAndDate });
			}
			
			Date now = new Date();
			
			List<String> drugNames = new ArrayList<String>();
			String anyARVs = " ARVs";
			
			if(rdBtn.equals("drugs")){
			for (Integer id : drugIds) {
				drugNames.add(Context.getConceptService().getDrug(id).getName());
			}
			}
			
			if(rdBtn.equals("concepts")){
				
				for (Integer conceptId : drugsByConceptsIds) {
//					if(Context.getConceptService().getDrug(conceptId)!=null)
					drugNames.add(Context.getConceptService().getConcept(conceptId).getName().toString());
				}
			}

			model.put("anyOrAll", anyOrAll);
			if (drugNames != null && drugNames.size() != 0) {
				model.put("drugList", drugNames.toString().substring(1, drugNames.toString().indexOf(']')));
				message.setDrugIds(drugNames.toString().substring(1, drugNames.toString().indexOf(']')));
			} 
			else {
				model.put("drugList", anyARVs);
				message.setDrugIds(anyARVs);
			}
			
			
			
			if (startDate != null) {
				model.put("startdate", df.format(startDate));
				message.setStartDate(df.format(startDate) + "");
			}
			
			if (endDate != null) {
				model.put("enddate", df.format(endDate));
				message.setEndDate(df.format(endDate) + "");
			}
			
			if (gender.equals("f")) {
				model.put("gender", "Female");
				message.setGender("Female");
			} else if (gender.equals("")) {
				model.put("gender", "Any");
				message.setGender("Any");
			} else if (!gender.equals("m")) {
				model.put("gender", "Male");
				message.setGender("Male");
			}
			if (minAge != null) {
				model.put("minAge", now.getYear() - minAge.getYear());
				message.setMinAge(now.getYear() - minAge.getYear() + "");
			}
			if (maxAge != null) {
				model.put("maxAge", now.getYear() - maxAge.getYear());
				message.setMaxAge(now.getYear() - maxAge.getYear() + "");
			}
			if (minBirthdate != null) {
				model.put("minBirthdate", df.format(minBirthdate));
				message.setMinBirthdate(df.format(minBirthdate) + "");
			}
			if (maxBirthdate != null) {
				model.put("maxBirthdate", df.format(maxBirthdate));
				message.setMaxBirthdate(df.format(maxBirthdate) + "");
			}
			
			
			// for data exportation
			if (request.getParameter("export") != null && !request.getParameter("export").equals("")) {
					if (Context.getAuthenticatedUser().hasPrivilege("Export Collective Patient Data")) {
						if (request.getParameter("export").equals("excel"))
							DrugOrderExportUtil.exportData(request, response, patients);
						if (request.getParameter("export").equals("pdf"))
							DrugOrderExportUtil.exportToPDF(request, response, patients);
					}
					else{
						model.put("msg", "Required Privilege: [Export Collective Patient Data]");
					}
			}
	
		
		model.put("objectsList", objectsList);
		model.put("drugIdback", drugIds); 
		model.put("conceptIdsback", drugsByConceptsIds);
		model.put("listSize", objectsList.size());
		model.put("checkedValueExport", checkedValue);
		model.put("rdBtn",rdBtn );
		
	}
	
}
