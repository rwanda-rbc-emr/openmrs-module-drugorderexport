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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Concept;
import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.module.drugorderexport.service.DrugOrderService;
import org.openmrs.module.regimenhistory.Regimen;
import org.openmrs.module.regimenhistory.RegimenComponent;
import org.openmrs.module.regimenhistory.RegimenHistory;
import org.openmrs.module.regimenhistory.RegimenUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

/**
 *
 */
public class ViewPatientRegimenController extends SimpleFormController {
	
	/** Logger for this class and subclasses */
	protected final Log log = LogFactory.getLog(getClass());
	
	@SuppressWarnings("static-access")
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		ModelAndView mav = new ModelAndView();
		
		List<Object[]> patientHistory = new ArrayList<Object[]>();
		
		DrugOrderService service = Context.getService(DrugOrderService.class);
		
		String linkStr = request.getParameter("linkId");
		Integer linkId = 1;
		if (linkStr != null && !linkStr.equals("")) {
			linkId = Integer.parseInt(linkStr);
		}
		
		int patientId = 0;
		try {
			patientId = Integer.parseInt(request.getParameter("patient"));
		}
		catch (Exception e) {
			e.printStackTrace();
			mav.addObject("msg", "There is no patient with id= " + request.getParameter("patient"));
		}
		patientHistory.clear();
		
		Patient patient = new Patient();
		RegimenUtils regimenHistory = new RegimenUtils();
		
		if (patientId != 0) {
			patient = Context.getPatientService().getPatient(patientId);
			RegimenHistory history = regimenHistory.getRegimenHistory(patient);
			List<Regimen> regimens = history.getRegimenList();

			Set<RegimenComponent> regimenComponents = new HashSet<RegimenComponent>();
			Set<RegimenComponent> componentsStopped = new HashSet<RegimenComponent>();
			
			for (Regimen r : regimens) {
				regimenComponents = r.getComponents();
				
				if (r.getEndDate() == null) {
					r.setEndDate(new Date());
				}
				for (RegimenComponent rc : regimenComponents) {
					if (rc.getStopDate() != null)
						if (rc.getStopDate().getTime() <= r.getStartDate().getTime()) {
							componentsStopped.add(rc);
							
						}

				}
				if (componentsStopped != null)
					regimenComponents.removeAll(componentsStopped);
				
			}
			
			Concept weightConcept = Context.getConceptService().getConcept(5089);
			
			Concept CD4CountConcept = Context.getConceptService().getConcept(5497);
			
			Concept hivViralLoad = Context.getConceptService().getConcept(856);
			
			if (patient != null)
				
				for (Regimen regimen : regimens) {					
					String viralLoad ="";
					if(service.getPatientObsValue(patient, hivViralLoad, regimen.getStartDate(), regimen.getEndDate())!=null){
						viralLoad = service.getPatientObsValue(patient, hivViralLoad, regimen.getStartDate(),
						    regimen.getEndDate()).toString();
					}
					else {
						viralLoad="-";
					}
					
					
					if (service.getPatientObsValue(patient, CD4CountConcept, regimen.getStartDate(), regimen.getEndDate()) == null) {
						patientHistory.add(new Object[] {
						        regimen,
						        " No Test In This Period",
						        service.getPatientObsValue(patient, weightConcept, regimen.getStartDate(), regimen
						                .getEndDate())
						                + " (Kg)",viralLoad });
					}
					if (service.getPatientObsValue(patient, weightConcept, regimen.getStartDate(), regimen.getEndDate()) == null) {
						patientHistory.add(new Object[] {
						        regimen,
						        service.getPatientObsValue(patient, CD4CountConcept, regimen.getStartDate(), regimen
						                .getEndDate()),
						        "No Test In This Period",
						        viralLoad });
						
					}
					if (service.getPatientObsValue(patient, CD4CountConcept, regimen.getStartDate(), regimen.getEndDate()) != null
					        && service.getPatientObsValue(patient, weightConcept, regimen.getStartDate(), regimen.getEndDate()) != null) {
						patientHistory.add(new Object[] {
						        regimen,
						        service.getPatientObsValue(patient, CD4CountConcept, regimen.getStartDate(), regimen
						                .getEndDate()),
						        service.getPatientObsValue(patient, weightConcept, regimen.getStartDate(), regimen
						                .getEndDate())
						                + " (Kg)",
						                viralLoad });						
						
					}
					
				}

		}
		
		mav.addObject("patient", patient);
		mav.addObject("program", Context.getProgramWorkflowService().getPatientPrograms(patient, null, null, null, null,null, false));
		mav.addObject("listPatientHistory", patientHistory);
		mav.setViewName("/module/drugorderexport/showpatientregimens");
		mav.addObject("linkId", linkId);
		return mav;
	}
	
}
