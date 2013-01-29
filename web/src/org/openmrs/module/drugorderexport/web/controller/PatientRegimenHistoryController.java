package org.openmrs.module.drugorderexport.web.controller;

import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Drug;
import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.module.drugorderexport.DrugOrderExportUtil;
import org.openmrs.module.drugorderexport.service.DrugOrderService;
import org.openmrs.util.OpenmrsUtil;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

public class PatientRegimenHistoryController extends ParameterizableViewController {
	
	protected final Log log = LogFactory.getLog(getClass());
	
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<Integer> allPatientsIds = null;
		List<Object[]> listPatientHistory = new ArrayList<Object[]>();
		Map<String, Object> map = new HashMap<String, Object>();
		
		String checkedValueStr[] = (String[]) null;
		int checkedValue = 0;
		if (request.getParameterValues("checkValue") != null) {
			checkedValueStr = request.getParameterValues("checkValue");
			checkedValue = Integer.parseInt(checkedValueStr[0]);
		}
		if (request.getMethod().equalsIgnoreCase("post")) {
			String gender = request.getParameter("gender");
			String mnAge = request.getParameter("minAge");
			String mxAge = request.getParameter("maxAge");
			String mnBirthdate = request.getParameter("minBirthdate");
			String mxBirthdate = request.getParameter("maxBirthdate");
			
			DrugOrderService service = (DrugOrderService) Context.getService(DrugOrderService.class);
			
			Date minBirthdate = null;
			Date maxBirthdate = null;
			Date minAge = null;
			Date maxAge = null;
			
			SimpleDateFormat df = OpenmrsUtil.getDateFormat();
			if (mnBirthdate != null && mnBirthdate.length() != 0)
				minBirthdate = df.parse(mnBirthdate);
			if (mxBirthdate != null && mxBirthdate.length() != 0)
				maxBirthdate = df.parse(mxBirthdate);
			if (mxAge != null && mxAge.length() != 0)
				maxAge = service.getDateObjectFormAge(Integer.parseInt(mxAge));
			if (mnAge != null && mnAge.length() != 0)
				minAge = service.getDateObjectFormAge(Integer.parseInt(mnAge));
			
			List<Drug> drugs = Context.getConceptService().getAllDrugs();
			List<Integer> drugIds = new ArrayList<Integer>();
			for (Drug drug : drugs) {
				drugIds.add(drug.getDrugId());
			}
			
			allPatientsIds = service.patientsWhoHasDrugOrder(gender, minAge, maxAge, minBirthdate, maxBirthdate);
			Date endDate = new Date();
			
			if (checkedValue == 1)
				allPatientsIds = service.getActivePatients(service.patientsWhoHasDrugOrder(gender, minAge, maxAge,
				    minBirthdate, maxBirthdate), endDate);
			
			List<Patient> patients = new ArrayList<Patient>();
			for (Integer patientId : allPatientsIds) {
				Patient patient = new Patient();
				
				patient = Context.getPatientService().getPatient(patientId);
				
				if (!patient.getVoided()) {
					patients.add(patient);
					
					Date startTreatmentDate = null;
					Date lastEncounterDate = null;
					Date lastVisitDate = null;
					String startTreatmentDateStr = "";
					String lastEncounterDateStr = "";
					String lastVisitDateStr = "";
					
					startTreatmentDate = service.getWhenPatientStarted(patient);
					lastEncounterDate = service.getPatientLastEncounterDate(patientId);
					
					if (service.getPatientLastVisitDate(patientId) != null) {
						lastVisitDate = service.getPatientLastVisitDate(patientId);
						lastVisitDateStr = lastVisitDate.toString();
					}
					if (service.getWhenPatStartedXRegimen(patientId, DrugOrderExportUtil.gpGetARVConceptIds()) != null) {
						startTreatmentDate = service.getWhenPatientStarted(patient);
						startTreatmentDateStr = startTreatmentDate.toString();
					}
					if (service.getPatientLastEncounterDate(patientId) != null) {
						lastEncounterDate = service.getPatientLastEncounterDate(patientId);
						lastEncounterDateStr = lastEncounterDate.toString();
					}
					
					listPatientHistory.add(new Object[] { Context.getPersonService().getPerson(patient.getPatientId()),
					        Context.getProgramWorkflowService().getPatientPrograms(patient), startTreatmentDateStr,
					        lastEncounterDateStr, lastVisitDateStr });
					
				}
				
			}
			
			// for data exportation
			if (request.getParameter("export") != null && !request.getParameter("export").equals("")) {
				
			
					if (Context.getAuthenticatedUser().hasPrivilege("Export Collective Patient Data")) {
						if (request.getParameter("export").equals("excel"))
							DrugOrderExportUtil.exportData(request, response, patients);
						if (request.getParameter("export").equals("pdf"))
							DrugOrderExportUtil.exportToPDF(request, response, patients);
					}
					else
						map.put("msg", "Required Privilege : [Export Collective Patient Data]");
				
			}
			
			Date now = new Date();
			if (gender.equals("f"))
				map.put("gender", "Female");
			else if (gender.equals(""))
				map.put("gender", "Any");
			else
				map.put("gender", "Male");
			if (minAge != null)
				map.put("minAge", Integer.valueOf(now.getYear() - minAge.getYear()));
			if (maxAge != null)
				map.put("maxAge", Integer.valueOf(now.getYear() - maxAge.getYear()));
			if (minBirthdate != null)
				map.put("minBirthdate", df.format(minBirthdate));
			if (maxBirthdate != null)
				map.put("maxBirthdate", df.format(maxBirthdate));
		}
		map.put("listPatientHistory", listPatientHistory);
		map.put("collectionSize", listPatientHistory.size());
		map.put("checkedValueExport", checkedValue);
		
		return new ModelAndView(getViewName(), map);
	}
	
}
