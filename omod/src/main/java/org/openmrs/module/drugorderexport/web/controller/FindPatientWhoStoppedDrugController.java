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
import org.openmrs.Concept;
import org.openmrs.ConceptAnswer;
import org.openmrs.ConceptName;
import org.openmrs.Drug;
import org.openmrs.Patient;
import org.openmrs.api.ConceptService;
import org.openmrs.api.context.Context;
import org.openmrs.module.drugorderexport.DrugOrderExportUtil;
import org.openmrs.module.drugorderexport.service.DrugOrderService;
import org.openmrs.util.OpenmrsUtil;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

public class FindPatientWhoStoppedDrugController extends ParameterizableViewController {
	
	protected final Log log = LogFactory.getLog(getClass());
	
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		
//		Collection<Drug> drugs = Context.getConceptService().getAllDrugs();
		DrugOrderService service = Context.getService(DrugOrderService.class);
		
		Collection<Drug> drugs = service.getArvDrugs();
		
		List<Concept> genericDrugs = Context.getConceptService().getConceptsWithDrugsInFormulary();
		ConceptService cs = Context.getConceptService();
		List<Concept> orderStopReasons = new ArrayList<Concept>();
		Concept c = cs.getConcept(1812);
		
		if (c != null)
			orderStopReasons.addAll(cs.getConceptsByConceptSet(c));
		
		if (c != null && c.getAnswers() != null) {
			for (ConceptAnswer ca : c.getAnswers())
				orderStopReasons.add(ca.getAnswerConcept());
			
		}
		map.put("drugs", drugs);
		map.put("generics", genericDrugs);
		map.put("reasons", orderStopReasons);
		if (request.getMethod().equalsIgnoreCase("post"))
			doSearch(map, request, response);
		return new ModelAndView(getViewName(), map);
	}
	
	protected void doSearch(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response)
	                                                                                                            throws Exception {
		
		DrugOrderService service = (DrugOrderService) Context.getService(DrugOrderService.class);
		
		String gender = request.getParameter("gender");
		String mnAge = request.getParameter("minAge");
		String mxAge = request.getParameter("maxAge");
		
		String startD = request.getParameter("startdate");
		String endD = request.getParameter("enddate");
		log.info("jjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjendD "+endD);
		String drugs[] = request.getParameterValues("drugList");
		String genericId[] = request.getParameterValues("genericList");
		String reasonId[] = request.getParameterValues("reasonsList");
		
		List<Integer> drugIds = new ArrayList<Integer>();
		List<Integer> genericIds = new ArrayList<Integer>();
		List<Integer> reasonIds = new ArrayList<Integer>();
		List<String> drugNames = new ArrayList<String>();
		if (drugs != null) {
			for (int i = 0; i < drugs.length; i++) {
				drugIds.add(Integer.parseInt(drugs[i]));
				drugNames.add(Context.getConceptService().getDrug(drugs[i]).getName());
			}
			
		}
		List<ConceptName> genericNames = new ArrayList<ConceptName>();
		if (genericId != null) {
			for (int k = 0; k < genericId.length; k++) {
				genericIds.add(Integer.parseInt(genericId[k]));
				genericNames.add(Context.getConceptService().getConcept(Integer.parseInt(genericId[k])).getName());
			}
			
		}
		List<ConceptName> reasonNames = new ArrayList<ConceptName>();
		if (reasonId != null) {
			for (int i1 = 0; i1 < reasonId.length; i1++) {
				reasonIds.add(Integer.parseInt(reasonId[i1]));
				reasonNames.add(Context.getConceptService().getConcept(Integer.parseInt(reasonId[i1])).getName());
			}
			
		}
		Date minAge = null;
		Date maxAge = null;
		Date startDate = null;
		Date endDate = null;
		SimpleDateFormat df = OpenmrsUtil.getDateFormat();
		
		if (startD != null && startD.length() != 0)
			startDate = df.parse(startD);
		if (endD != null && endD.length() != 0)
			endDate = df.parse(endD);
		if (mxAge != null && mxAge.length() != 0)
			maxAge = service.getDateObjectFormAge(Integer.parseInt(mxAge));
		if (mnAge != null && mnAge.length() != 0)
			minAge = service.getDateObjectFormAge(Integer.parseInt(mnAge));
		
		List<Patient> patients = new ArrayList<Patient>();
		List<Integer> patientIds = new ArrayList<Integer>();
		
		
		
//		if(drugIds!=null)
		patientIds = service.searchDrugOrderByStopReason(startDate, endDate, drugIds, reasonIds, genericIds, gender, minAge,maxAge);
		
	     if(patientIds==null)
			patientIds = service.searchDrugOrderByStopReason(startDate, endDate, null, reasonIds, genericIds, gender, minAge,maxAge);
	     
		
	     //prophylaxis patients
	     List<Integer> prophylaxisPatient = service.getAllProphylaxisPatients(startDate, endDate, gender, minAge, maxAge, null, null);
	     
	     
		for (Integer id : patientIds) {
			Patient patient = Context.getPatientService().getPatient(id);
			if (!patient.getVoided().booleanValue())
				patients.add(patient);
		}
		
		// for data exportation
		if (request.getParameter("export") != null && !request.getParameter("export").equals("")) {
		
				if (Context.getAuthenticatedUser().hasPrivilege("Export Collective Patient Data")){
					if (request.getParameter("export").equals("excel"))
						DrugOrderExportUtil.exportData(request, response, patients);
					if (request.getParameter("export").equals("pdf"))
						DrugOrderExportUtil.exportToPDF(request, response, patients);
				}
				else
					model.put("msg", "Required Privilege : [Export Collective Patient Data]");
			
		} 
		model.put("patientList", patients);
		model.put("listSize", patients.size());
		model.put("drugIdback", drugIds);
		model.put("reasonsIdback", reasonIds);
		model.put("conceptIdback", genericIds);
		
		Date now = new Date();
		if (gender.equals("f"))
			model.put("gender", "Female");
		else if (gender.equals(""))
			model.put("gender", "Any");
		else
			model.put("gender", "Male");
		if (minAge != null)
			model.put("minAge", Integer.valueOf(now.getYear() - minAge.getYear()));
		if (maxAge != null)
			model.put("maxAge", Integer.valueOf(now.getYear() - maxAge.getYear()));
		if (startDate != null)
			model.put("startdate", df.format(startDate));
		if (endDate != null)
			model.put("enddate", df.format(endDate));
		if (reasonNames != null && reasonNames.size() != 0)
			model.put("reason", reasonNames.toString().substring(1, reasonNames.toString().indexOf(']')));
		if (drugNames != null && drugNames.size() != 0)
			model.put("drugList", drugNames.toString().substring(1, drugNames.toString().indexOf(']')));
		else
			model.put("drugList", "Any ARVs");
		model.put("generic", genericNames);
	}
	
}
