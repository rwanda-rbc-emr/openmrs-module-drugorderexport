package org.openmrs.module.drugorderexport.web.controller;

import java.text.SimpleDateFormat;
import java.util.*;

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

public class NewOnProphylaxisController extends ParameterizableViewController {
	
	protected final Log log = LogFactory.getLog(getClass());
	
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<Integer> patients = null;
		Map<String, Object> map = new HashMap<String, Object>();
		List<Object[]> objectsList = new ArrayList<Object[]>();
		
		String checkedValueStr[] = null;
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
			
			List<Patient> patientsFromIds = new ArrayList<Patient>();
			
			patients = service.getNewPatientsOnProphylaxis(startDate, endDate, gender, mnAge, mxAge, mnBirthdate,
			    mxBirthdate);
			String pophylaxisDrugIds = DrugOrderExportUtil.getProphylaxisDrugConceptIds();
			if (checkedValue == 1){
//				patients = service.getActivePreARTPatients(patients, endDate);
				List<Integer> patients1 = service.getNewPatientsOnProphylaxis(startDate, endDate, gender, mnAge, mxAge, mnBirthdate,
					    mxBirthdate);
			patients=service.getActiveOnDrugsPatients(patients1, pophylaxisDrugIds, endDate);
			}
			
			for (Integer id : patients) {
				if (!Context.getPatientService().getPatient(id).getVoided())
				patientsFromIds.add(Context.getPatientService().getPatient(id));
				
				Patient patient = new Patient();
				
				patient = Context.getPatientService().getPatient(id);

					
					Date lastEncounterDate = null;
					Date lastVisitDate = null;
					Date startProphylaxis=null;
					String lastEncounterDateStr = "";
					String lastVisitDateStr = "";
					String startProphylaxisStr="";
					
					if (service.getPatientLastVisitDate(id) != null) {
						lastVisitDate = service.getPatientLastVisitDate(id);
						lastVisitDateStr = lastVisitDate.toString();
					}

					if (service.getPatientLastEncounterDate(id) != null) {
						lastEncounterDate = service.getPatientLastEncounterDate(id);
						lastEncounterDateStr = lastEncounterDate.toString();
					}
					if(service.getWhenPatStartedXRegimen(id, DrugOrderExportUtil.getProphylaxisDrugIds())!=null){
						startProphylaxis =service.getWhenPatStartedXRegimen(id, DrugOrderExportUtil.getProphylaxisDrugIds());
						startProphylaxisStr = startProphylaxis.toString();
					}
					
					objectsList.add(new Object[] { Context.getPersonService().getPerson(patient.getPatientId()),
							startProphylaxisStr,lastEncounterDateStr, lastVisitDateStr });
			}
			
			// for data exportation
			if (request.getParameter("export") != null && !request.getParameter("export").equals("")) {
			
					if (Context.getAuthenticatedUser().hasPrivilege("Export Collective Patient Data")) {
						if (request.getParameter("export").equals("excel"))
							DrugOrderExportUtil.exportData(request, response, patientsFromIds);
						if (request.getParameter("export").equals("pdf"))
							DrugOrderExportUtil.exportToPDF(request, response, patientsFromIds);
					}
					else
						map.put("msg", "Required Privilege : [Export Collective Patient Data]");
				
			}
		

			map.put("objectsList", objectsList);
			map.put("patients1", patientsFromIds);
			map.put("collectionSize", patientsFromIds.size());
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
