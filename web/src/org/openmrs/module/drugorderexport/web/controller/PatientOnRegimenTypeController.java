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
import org.openmrs.Drug;
import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.module.drugorderexport.DrugOrderExportUtil;
import org.openmrs.module.drugorderexport.service.DrugOrderService;
import org.openmrs.util.OpenmrsUtil;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

public class PatientOnRegimenTypeController extends ParameterizableViewController {
	
	protected final Log log = LogFactory.getLog(getClass());
	
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView mav = new ModelAndView();
		Map<String, List<Integer>> categoryPatients = new HashMap<String, List<Integer>>();
		List<Integer> exportPatientIds = new ArrayList<Integer>();
		Map<String, Object> map = new HashMap<String, Object>();
		String checkedValueStr[] = null;
		
		Map<String, List<String>> drugsMap = new HashMap<String, List<String>>();
		
		int checkedValue = 0;
		if (request.getParameterValues("checkValue") != null) {
			checkedValueStr = request.getParameterValues("checkValue");
			checkedValue = Integer.parseInt(checkedValueStr[0]);
		}
		List<String> regCategories = new ArrayList<String>();
		String arvValueStr[] = null;
		String prophylaxisValueStr[] = null;
		String firstLineValueStr[] = null;
		String secondLineValueStr[] = null;
		
		List<Integer> arvPatientIds = new ArrayList<Integer>();
		List<Integer> prophylaxisPatientsIds = new ArrayList<Integer>();
		List<Integer> firstLinePatients = new ArrayList<Integer>();
		List<Integer> secondLinePatientIds = new ArrayList<Integer>();
		List<Integer> thirdLinePatientIds = new ArrayList<Integer>();
		
		DrugOrderService service = (DrugOrderService) Context.getService(DrugOrderService.class);
		String viewCategory = "";
		
		List<Integer> arvDrugIds = DrugOrderExportUtil.gpGetARVDrugIds();
		
		List<Drug> arvDrugs = DrugOrderExportUtil.getDrugFromIds(arvDrugIds);
		List<Integer> firstLinedrugsIds = DrugOrderExportUtil.gpGetFirstLineDrugsAsList();
		List<Drug> firstLineDrugs = DrugOrderExportUtil.getDrugFromIds(firstLinedrugsIds);
		List<Integer> secondLinedrugsIds = DrugOrderExportUtil.gpGetSecondLineDrugsAsList();
		List<Drug> secondLineDrugs = DrugOrderExportUtil.getDrugFromIds(secondLinedrugsIds);
		List<Integer> prophylaxisDrugsIds = DrugOrderExportUtil.gpGetProphylaxisAsIntegers();
		List<Drug> prophylaxisDrugs = DrugOrderExportUtil.getDrugFromIds(prophylaxisDrugsIds);
		
		if (request.getParameter("viewCategory") != null) {
			viewCategory = request.getParameter("viewCategory");
			String cats[] = viewCategory.split("],");
			List<String> unorderedList = new ArrayList<String>();
			for (int i = 0; i < cats.length; i++)
				unorderedList.add(cats[i]);
			
			Map<String, String> strList = new HashMap<String, String>();
			for (int i = 0; i < unorderedList.size() - 1; i++) {
				String keyVal[] = ((String) unorderedList.get(i)).toString().split("=");
				if (keyVal[0].toString().contains("{"))
					keyVal[0] = keyVal[0].toString().replace("{", "");
				if (keyVal[1].toString().contains("[") || keyVal[1].toString().contains("}")
				        || keyVal[1].toString().contains("]")) {
					keyVal[1] = keyVal[1].toString().replace("[", "0");
					keyVal[1] = keyVal[1].toString().replace("]", "0");
					keyVal[1] = keyVal[1].toString().replace("}", "0");
				}
				strList.put(keyVal[0], keyVal[1]);
			}
			
			List<Integer> patIds;
			for (String key : strList.keySet()) {
				patIds = new ArrayList<Integer>();
				String ids[] = ((String) strList.get(key)).split(", ");
				for (int i = 0; i < ids.length; i++)
					patIds.add(Integer.parseInt(ids[i]));
				
			}
			
//			String index = null;
//			index = request.getParameter("index");
//			exportPatientIds = (List<Integer>) categoryPatients.get(categoryPatients.get(index));
		}
		if (request.getMethod().equalsIgnoreCase("post")
		        || (request.getParameter("post") != null && !request.getParameter("post").equals(""))) {
			Date startDate = null;
			Date endDate = null;
			
			String startD = request.getParameter("startdate");
			String endD = request.getParameter("enddate");
			String gender = request.getParameter("gender");
			String minAge = request.getParameter("minAge");
			String maxAge = request.getParameter("maxAge");
			String minBirthdate = request.getParameter("minBirthdate");
			String maxBirthdate = request.getParameter("maxBirthdate");
			
			SimpleDateFormat df = OpenmrsUtil.getDateFormat();
			
//			String patientsOnCateg = request.getParameter("viewCategory");
//			log.info("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!pats on link " + request.getParameter("viewCategory"));
			
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
			
			if (request.getParameterValues("arv") != null) {
				arvValueStr = request.getParameterValues("arv");
				regCategories.add(arvValueStr[0]);
//				arvPatientIds = service.searchDrugOrderByDrug("any", startDate, endDate, arvDrugs, gender, mnAge, mxAge,
//				    mnBirthdate, mxBirthdate);
				String arvConceptIds = DrugOrderExportUtil.gpGetARVConceptIds();
			
				arvPatientIds = service.getPatientsOnRegimenCategory(arvConceptIds, startDate, endDate, gender, mnAge, mxAge,
				    mnBirthdate, mxBirthdate);
				
				
				if (checkedValue == 1){
//					arvPatientIds = service.getActivePatients(arvPatientIds, endDate);
					List<Integer> listPatients = service.getPatientsOnRegimenCategory(arvConceptIds, startDate, endDate, gender, mnAge, mxAge,
						    mnBirthdate, mxBirthdate);
				
					arvPatientIds = service.getActiveOnDrugsPatients(listPatients, arvConceptIds, endDate);
					categoryPatients.put("ARV ", arvPatientIds);
				
					exportPatientIds = arvPatientIds;
				}
					
			}
			if (request.getParameterValues("prophylaxis") != null) {
				prophylaxisValueStr = request.getParameterValues("prophylaxis");
				regCategories.add(prophylaxisValueStr[0]);
				prophylaxisPatientsIds = service.getAllProphylaxisPatients(startDate, endDate, gender, mnAge, mxAge,
				    mnBirthdate, mxBirthdate);
				String prophylaxisConceptIds = DrugOrderExportUtil.getProphylaxisDrugConceptIds();
//				prophylaxisPatientsIds = service.getPatientsOnRegimenCategory(prophylaxisConceptIds, startDate, endDate, gender, mnAge, mxAge,
//				    mnBirthdate, mxBirthdate);
				if (checkedValue == 1)
					prophylaxisPatientsIds = service.getActivePreARTPatients(prophylaxisPatientsIds, endDate);
					categoryPatients.put("Prophylaxis ", prophylaxisPatientsIds);
				
					exportPatientIds = prophylaxisPatientsIds;
			}
			if (request.getParameterValues("secondLine") != null) {
				secondLineValueStr = request.getParameterValues("secondLine");
				regCategories.add(secondLineValueStr[0]);
				
				String secondLineConceptIds = DrugOrderExportUtil.getSecondLineDrugConceptIds();
//				secondLinePatientIds = service.getPatientsOnRegimenCategory(secondLineConceptIds, startDate, endDate, gender, mnAge, mxAge,
//				    mnBirthdate, mxBirthdate);
				secondLinePatientIds = service.getSecondLinePatientsBeforeDate(startDate, endDate, gender, mnAge, mxAge, mnBirthdate, mxBirthdate);
				if (checkedValue == 1) {
//					secondLinePatientIds = service.getActivePatients(secondLinePatientIds, endDate);
					secondLinePatientIds = service.getActiveOnDrugsPatients(secondLinePatientIds, secondLineConceptIds, endDate);
				}
				categoryPatients.put("Second Line ", secondLinePatientIds);
				exportPatientIds = secondLinePatientIds;
			}
			if (request.getParameterValues("firstLine") != null) {
//				List<Integer> firstLinePatientIds = service.getPatientsOnFirstLineReg(startDate, endDate, gender, mnAge, mxAge, mnBirthdate, mxBirthdate);
								
				String firstLineConceptIds = DrugOrderExportUtil.gpGetFirstLineDrugConceptIds();
//				List<Integer> secLinePatientIds = service.getPatientsOnRegimenCategory(secondLineConceptIds, startDate, endDate, gender, mnAge, mxAge,
//				    mnBirthdate, mxBirthdate);
				
				//exclude secondline patients from those on first line regimen
//				List<Integer> newFirstLinePatientIds=new ArrayList<Integer>();
//				for (Integer d : firstLinePatientIds) {
//					if(!secLinePatientIds.contains(d))
//						newFirstLinePatientIds.add(d);
//				}
				
				firstLinePatients = service.getFirstLinePatientsBeforeDate(startDate, endDate, gender, mnAge, mxAge, mnBirthdate, mxBirthdate);
				
				if (checkedValue == 1){
//					newFirstLinePatientIds = service.getActivePatients(newFirstLinePatientIds, endDate);
					List<Integer> cumulativeList = service.getFirstLinePatientsBeforeDate(startDate, endDate, gender, mnAge, mxAge, mnBirthdate, mxBirthdate);
					firstLinePatients = service.getActiveOnDrugsPatients(cumulativeList, firstLineConceptIds, endDate);
				}
				
				categoryPatients.put("First Line ", firstLinePatients);
				exportPatientIds = firstLinePatients;
			}
			if (request.getParameterValues("thirdLine") != null) {
		
				String thirdLineConceptIds = DrugOrderExportUtil.getThirdLineDrugConceptIds();
				thirdLinePatientIds = service.getThirdLinePatientsBeforeDate(startDate, endDate, gender, mnAge, mxAge,
				    mnBirthdate, mxBirthdate);
				
				if (checkedValue == 1)
					thirdLinePatientIds = service.getActiveOnDrugsPatients(thirdLinePatientIds, thirdLineConceptIds, endDate);
				
				categoryPatients.put("Third Line ", thirdLinePatientIds);
				exportPatientIds = thirdLinePatientIds;
			}
			
			
			
			List<Integer> patients = new ArrayList<Integer>();
			List<Patient> exportPatients = new ArrayList<Patient>();
			
			for (Integer id : exportPatientIds)
				exportPatients.add(Context.getPatientService().getPatient(id));
			
			//            log.info("_______exported patients_____________________________   ids "+exportPatients.size());
			
			
			map.put("checkedValueExport", checkedValue);
			map.put("exportPatients", exportPatients);
			map.put("categoryPatients", categoryPatients);
			
			drugsMap.put("ARV ", DrugOrderExportUtil.getDrugNames(arvDrugIds));
			drugsMap.put("First Line ", DrugOrderExportUtil.getDrugNames(firstLinedrugsIds));
			drugsMap.put("Second Line ", DrugOrderExportUtil.getDrugNames(secondLinedrugsIds));
			drugsMap.put("Prophylaxis ", DrugOrderExportUtil.getDrugNames(prophylaxisDrugsIds));
			
			
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
			request.getSession().setAttribute("mapObjects", categoryPatients);
		}
		mav.addObject("arv_title", "Antiretroviral Drugs");
		mav.addObject("firstLine_title", "First Line Drugs");
		mav.addObject("secondLine_title", "Second Line Drugs");
		mav.addObject("prophylaxis_title", "Prophylaxis Drugs");
		
		mav.addObject("categoryPatients", categoryPatients);
		mav.addObject("drugsMap", drugsMap);
		mav.addObject("arvDrugs", arvDrugs);
		mav.addObject("prophylaxisDrugs", prophylaxisDrugs);
		mav.addObject("map", map);
		
		
		
//		mav.addObject("arv", map.get("ARV"));
//		mav.addObject("firstLine", map.get("firstLine"));
//		mav.addObject("secondLine", map.get("secondLine"));
//		mav.addObject("prophylaxis", map.get("prophylaxis"));
		
		if (request.getParameter("drugTypeId") != null && !request.getParameter("drugTypeId").equals("")) {
			if (request.getParameter("drugTypeId").equals("ARV")) {
				mav.addObject("drugs", arvDrugs.toString());
				
			}
			if (request.getParameter("drugTypeId").equals("First Line")) {
				mav.addObject("drugs", firstLineDrugs.toString());
			}
			if (request.getParameter("drugTypeId").equals("Second Line")) {
				mav.addObject("drugs", secondLineDrugs.toString());
			}
			if (request.getParameter("drugTypeId").equals("Prophylaxis")) {
				mav.addObject("drugs", prophylaxisDrugs.toString());
			}
		}
		
		mav.setViewName(getViewName());
		return mav;
	}
	
}
