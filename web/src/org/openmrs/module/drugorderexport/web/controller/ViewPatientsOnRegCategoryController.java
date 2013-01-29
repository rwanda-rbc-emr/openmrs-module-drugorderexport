package org.openmrs.module.drugorderexport.web.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.module.drugorderexport.DrugOrderExportUtil;
import org.openmrs.module.drugorderexport.service.DrugOrderService;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

public class ViewPatientsOnRegCategoryController extends ParameterizableViewController {
	
	protected final Log log = LogFactory.getLog(getClass());
	
	@SuppressWarnings("unchecked")
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<Integer> patients = null;
		Map<String, List<Integer>> mapFromController = new HashMap<String, List<Integer>>();
		mapFromController = (Map<String, List<Integer>>) request.getSession().getAttribute("mapObjects");
		
		List<Object[]> objectsList = new ArrayList<Object[]>();
		
		String viewCategory = null;
		
		if (request.getParameter("viewCategory") != "" && request.getParameter("viewCategory") != null)
			viewCategory = request.getParameter("viewCategory");
		
		String title = "";
		if (viewCategory.equals("ARV")) {
			patients = mapFromController.get("ARV ");
			title = "Patients on ARV Drugs";
		}
		if (viewCategory.equals("First Line")) {
			patients = mapFromController.get("First Line ");
			title = "Patients on First-Line Drugs";
		}
		if (viewCategory.equals("Second Line")) {
			patients = mapFromController.get("Second Line ");
			title = "Patients on Second-Line Drugs";
		}
		if (viewCategory.equals("Prophylaxis")) {
			patients = mapFromController.get("Prophylaxis ");
			title = "Patients on Prophylaxis";
		}
		DrugOrderService service = (DrugOrderService) Context.getService(DrugOrderService.class);
		List<Patient> patientsFromId = new ArrayList<Patient>();
		if (patients != null)
			//        	for (Integer id : patients) {
			//	            patientsFromId.add(Context.getPatientService().getPatient(id));
			//            }
			for (Integer patientId : patients) {
				Patient patient = new Patient();
				if (!Context.getPatientService().getPatient(patientId).getVoided()) {
					patient = Context.getPatientService().getPatient(patientId);
					patientsFromId.add(patient);
				}
				
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
				
				objectsList.add(new Object[] { Context.getPersonService().getPerson(patientId), startTreatmentDateStr,
				        lastEncounterDateStr, lastVisitDateStr });
			}
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("objectsList", objectsList);
		map.put("patientsSize", objectsList.size());
		
		map.put("title", title);
		map.put("viewCategory", viewCategory);
		
		if (request.getParameter("export") != null && !request.getParameter("export").equals("")) {
	
				if (Context.getAuthenticatedUser().hasPrivilege("Export Collective Patient Data")) {
					if (request.getParameter("export").equals("excel"))
						DrugOrderExportUtil.exportData(request, response, patientsFromId);
				}
				else
					map.put("msg", "Required Privilege : [Export Collective Patient Data]");
			
		}
		
		return new ModelAndView(getViewName(), map);
	}
	
}
