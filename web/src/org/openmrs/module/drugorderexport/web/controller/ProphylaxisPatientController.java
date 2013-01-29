package org.openmrs.module.drugorderexport.web.controller;

import java.text.SimpleDateFormat;
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
import org.openmrs.util.OpenmrsUtil;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

public class ProphylaxisPatientController extends ParameterizableViewController {
	
	protected final Log log = LogFactory.getLog(getClass());
	
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		Map<String, Object> map = new HashMap<String, Object>();
		List<Object[]> objectsList = new ArrayList<Object[]>();
		
		String checkedValueStr[] = (String[]) null;
		int checkedValue = 0;
		if (request.getParameterValues("checkValue") != null) {
			checkedValueStr = request.getParameterValues("checkValue");
			checkedValue = Integer.parseInt(checkedValueStr[0]);
		}
		if (request.getMethod().equalsIgnoreCase("post")) {
			Date startDate = null;
			Date endDate = null;
			DrugOrderService service = (DrugOrderService) Context.getService(DrugOrderService.class);
			String startD = request.getParameter("startdate");
			String endD = request.getParameter("enddate");
			String gender = request.getParameter("gender");
			String minAge = request.getParameter("minAge");
			String maxAge = request.getParameter("maxAge");
			String minBirthdate = request.getParameter("minBirthdate");
			String maxBirthdate = request.getParameter("maxBirthdate");
			
			SimpleDateFormat df = OpenmrsUtil.getDateFormat();
			Date mnBirthdate = null;
			Date mxBirthdate = null;
			Date mnAge = null;
			Date mxAge = null;
			if (startD != null && startD.length() != 0)
				startDate = df.parse(startD);
			if (endD != null && endD.length() != 0)
				endDate = df.parse(endD);
			if (minBirthdate != null && minBirthdate.length() != 0)
				mnBirthdate = df.parse(minBirthdate);
			if (maxBirthdate != null && maxBirthdate.length() != 0)
				mxBirthdate = df.parse(maxBirthdate);
			if (maxAge != null && maxAge.length() != 0)
				mxAge = service.getDateObjectFormAge(Integer.parseInt(maxAge));
			if (minAge != null && minAge.length() != 0)
				mnAge = service.getDateObjectFormAge(Integer.parseInt(minAge));
			
			
			
			List<Integer> patientsList= service.getAllProphylaxisPatients(startDate, endDate, gender, mnAge, mxAge, mnBirthdate, mxBirthdate);
			
			List<Integer> patients = new ArrayList<Integer>();
			
			for(Integer id:patientsList){
				if(service.isPatientOnProphylaxisOnly(id))
					patients.add(id);
			}
		
			if (checkedValue == 1) {
				patients = service.getActivePreARTPatients(patients, endDate);
				
			}
			
			List<Patient> patientList = new ArrayList<Patient>();
			
			for (Integer patientId : patients) {
				Patient patient = Context.getPatientService().getPatient(patientId);
//				if(service.isPatientOnProphylaxisOnly(patientId))
//					patient=Context.getPatientService().getPatient(patientId);
				
				Date lastEncounterDate = null;
				Date lastVisitDate = new Date();
				Date startProphylaxisDate = null;
				
				String lastEncounterDateStr = "";
				String lastVisitDateStr = "";
				String startProphylaxisDateStr = "";
				
				if (service.getPatientLastVisitDate(patientId) != null) {
					lastVisitDate = service.getPatientLastVisitDate(patient.getPatientId());
					if(lastVisitDate!=null)
					lastVisitDateStr = lastVisitDate.toString();
				}
				if (service.getPatientLastEncounterDate(patientId) != null) {
					lastEncounterDate = service.getPatientLastEncounterDate(patient.getPatientId());
					if(lastEncounterDate!=null)
					lastEncounterDateStr = lastEncounterDate.toString();
				}
				
				if (service.getWhenPatStartedXRegimen(patientId, DrugOrderExportUtil.getProphylaxisDrugIds()) != null) {
					startProphylaxisDate = service.getWhenPatStartedXRegimen(patientId, DrugOrderExportUtil.getProphylaxisDrugIds());
					if(startProphylaxisDate!=null)
					startProphylaxisDateStr = startProphylaxisDate.toString();
				}

				objectsList.add(new Object[] { Context.getPatientService().getPatient(patient.getPatientId()),startProphylaxisDateStr, lastEncounterDateStr, lastVisitDateStr });
		
			    patientList.add(Context.getPatientService().getPatient(patientId));
			}
					
			// for data exportation
			if (request.getParameter("export") != null && !request.getParameter("export").equals("")) {
	
					if (Context.getAuthenticatedUser().hasPrivilege("Export Collective Patient Data")) {
						if (request.getParameter("export").equals("excel"))
							DrugOrderExportUtil.exportData(request, response, patientList);
						if (request.getParameter("export").equals("pdf"))
							DrugOrderExportUtil.exportToPDF(request, response, patientList);
					}
					else
						map.put("msg", "Required Privilege : [Export Collective Patient Data]");
			}
			
			map.put("objectsList", objectsList);
			map.put("collectionSize", objectsList.size());
			map.put("checkedValueExport", checkedValue);
			
			Date now = new Date();
			if (startDate != null)
				map.put("startdate", df.format(startDate));
			if (endDate != null)
				map.put("enddate", df.format(endDate));
			if (gender.equals("f"))
				map.put("gender", "Female");
			else if (gender.equals(""))
				map.put("gender", "Any");
			else
				map.put("gender", "Male");
			if (mnAge != null)
				map.put("minAge", Integer.valueOf(now.getYear() - mnAge.getYear()));
			if (mxAge != null)
				map.put("maxAge", Integer.valueOf(now.getYear() - mxAge.getYear()));
			if (mnBirthdate != null)
				map.put("minBirthdate", df.format(mnBirthdate));
			if (mxBirthdate != null)
				map.put("maxBirthdate", df.format(mxBirthdate));
		}
		return new ModelAndView(getViewName(), map);
	}
	
}
