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
package org.openmrs.module.drugorderexport;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.openmrs.Concept;
import org.openmrs.Drug;
import org.openmrs.Patient;
import org.openmrs.PatientProgram;
import org.openmrs.api.context.Context;
import org.openmrs.module.drugorderexport.service.DrugOrderService;
import org.openmrs.module.regimenhistory.Regimen;
import org.openmrs.module.regimenhistory.RegimenComponent;
import org.openmrs.module.regimenhistory.RegimenHistory;
import org.openmrs.module.regimenhistory.RegimenUtils;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

/**
 *
 */
public class DrugOrderExportUtil {
	
	/** Logger for this class and subclasses */
	protected static final Log log = LogFactory.getLog(DrugOrderExportUtil.class);
	
	private SessionFactory sessionFactory;
	
	
	//==========================================================================================================================================
	
	public String getReplaceString(String str, String toBeReplaced) {
		if (str.contains(toBeReplaced)) {
			if (toBeReplaced.equalsIgnoreCase("00:00:00.0") || toBeReplaced.equalsIgnoreCase("[")
			        || toBeReplaced.equalsIgnoreCase("]")) {
				str = str.replace(toBeReplaced, "");
			}
			if (toBeReplaced.equalsIgnoreCase(",")) {
				str = str.replace(toBeReplaced, " ");
			}
		}
		return str;
	}
	
	//==========================================================================================================================================
	
	public static List<Drug> getDrugFromIds(List<Integer> drugIds) {
		List<Drug> drugs = new ArrayList<Drug>();
		for (Integer id : drugIds) {
			drugs.add(Context.getConceptService().getDrug(id));
		}
		return drugs;
	}
	//==========================================================================================================================================
	
	public static List<String> getDrugNames(List<Integer> drugIds) {
		List<String> drugNames = new ArrayList<String>();
		for (Integer id : drugIds) {
			Drug d = Context.getConceptService().getDrug(id);
			drugNames.add(d.getName());
		}
		return drugNames;
	}
	
	//==========================================================================================================================================
	
	/**
	 * global properties for prophylaxis drugs
	 * 
	 * @return string
	 */
	public static String getProphylaxisDrugIds() {
		return Context.getAdministrationService().getGlobalProperty("drugorderexport.prophylaxisDrugIds");
	}
	
	//==========================================================================================================================================
	
	/**
	 * prophylaxis concept ids
	 * 
	 * @return string
	 */
	public static String getProphylaxisDrugConceptIdsStr() {
		return Context.getAdministrationService().getGlobalProperty("drugorderexport.prophylaxisDrugConceptIds");
	}
	
	//==========================================================================================================================================
	
	/**
	 * prophylaxis and tb drug ids
	 * 
	 * @return string
	 */
	public static String getProphylaxisAndTbDrugIds() {
		return Context.getAdministrationService().getGlobalProperty("drugorderexport.tbAndProphylaxisDrugIds");
	}
	//==========================================================================================================================================
	
		/**
		 * prophylaxis and tb concept ids
		 * 
		 * @return string
		 */
		public static String getProphylaxisAndTbDrugConceptIds() {
			return Context.getAdministrationService().getGlobalProperty("drugorderexport.tbAndProphylaxisDrugConceptIds");
		}
	
	//==========================================================================================================================================
	
	/**
	 * Second Line concept ids
	 * 
	 * @return string
	 */
	public static String getSecondLineDrugConceptIds() {
		return Context.getAdministrationService().getGlobalProperty("drugorderexport.secondLineDrugConceptIds");
	}
	
	//==========================================================================================================================================
	
		/**
		 * Third Line concept ids
		 * 
		 * @return string
		 */
		public static String getThirdLineDrugConceptIds() {
			return Context.getAdministrationService().getGlobalProperty("drugorderexport.thirdLineDrugConceptIds");
		}
	

	//==========================================================================================================================================
	
	public static String getProphylaxisDrugConceptIds() {
		return Context.getAdministrationService().getGlobalProperty("drugorderexport.prophylaxisDrugConceptIds");
	}

	//==========================================================================================================================================
	
	/**
	 * gets the list of ids and split them
	 * 
	 * @return list
	 */
	public static List<String> gpGetProphylaxisAsList() {
		ArrayList<String> list = new ArrayList<String>();
		StringTokenizer tokenizer = new StringTokenizer(getProphylaxisDrugIds(), ",");
		while (tokenizer.hasMoreTokens()) {
			String id = (String) tokenizer.nextToken();
			list.add(id);
		}
		//		log.debug("getProphylaxisAsList " +list);
		return list;
	}
	
	//==========================================================================================================================================
	
	/**
	 * get prophylaxis ids as integers
	 * 
	 * @return
	 */
	public static List<Integer> gpGetProphylaxisAsIntegers() {
		ArrayList<Integer> list = new ArrayList<Integer>();
		StringTokenizer tokenizer = new StringTokenizer(getProphylaxisDrugIds(), ",");
		while (tokenizer.hasMoreTokens()) {
			Integer id = Integer.parseInt(tokenizer.nextToken());
			list.add(id);
		}
		return list;
	}
	//==========================================================================================================================================
	
		/**
		 * get prophylaxis ids as integers
		 * 
		 * @return
		 */
		public static List<Integer> gpGetProphylaxisDrugConceptIds() {
			ArrayList<Integer> list = new ArrayList<Integer>();
			StringTokenizer tokenizer = new StringTokenizer(getProphylaxisDrugConceptIds(), ",");
			while (tokenizer.hasMoreTokens()) {
				Integer id = Integer.parseInt(tokenizer.nextToken());
				list.add(id);
			}
			return list;
		}
	//==========================================================================================================================================
	
	/**
	 * get prophylaxis and tb ids as integers
	 * 
	 * @return
	 */
	public static List<Integer> gpGetProphylaxisAndTbDrugIds() {
		ArrayList<Integer> list = new ArrayList<Integer>();
		StringTokenizer tokenizer = new StringTokenizer(getProphylaxisAndTbDrugIds(), ",");
		while (tokenizer.hasMoreTokens()) {
			Integer id = Integer.parseInt(tokenizer.nextToken());
			list.add(id);
		}
		return list;
	}
	//==========================================================================================================================================
	
		/**
		 * get prophylaxis and tb drug concept ids as integers
		 * 
		 * @return
		 */
		public static List<Integer> gpGetProphylaxisAndTbDrugConceptIds() {
			ArrayList<Integer> list = new ArrayList<Integer>();
			StringTokenizer tokenizer = new StringTokenizer(getProphylaxisAndTbDrugConceptIds(), ",");
			while (tokenizer.hasMoreTokens()) {
				Integer id = Integer.parseInt(tokenizer.nextToken());
				list.add(id);
			}
			return list;
		}
	//==========================================================================================================================================
	/**
	 * get prophylaxis and tb ids as integers
	 * 
	 * @return
	 */
	public static List<Integer> gpGetProphylaxisAndTbIds() {
		ArrayList<Integer> list = new ArrayList<Integer>();
		StringTokenizer tokenizer = new StringTokenizer(getProphylaxisAndTbDrugIds(), ",");
		while (tokenizer.hasMoreTokens()) {
			Integer id = Integer.parseInt(tokenizer.nextToken());
			list.add(id);
		}
		return list;
	}

	//==========================================================================================================================================
	
	public static String getSecondLineDrugIds() {
		return Context.getAdministrationService().getGlobalProperty("drugorderexport.secondLineDrugIds");
	}
	
	//==========================================================================================================================================
	
	public static List<Integer> gpGetSecondLineDrugsAsList() {
		ArrayList<Integer> list = new ArrayList<Integer>();
		StringTokenizer tokenizer = new StringTokenizer(getSecondLineDrugIds(), ",");
		while (tokenizer.hasMoreTokens()) {
			Integer id = Integer.parseInt(tokenizer.nextToken());
			list.add(id);
		}
		return list;
	}
	//==========================================================================================================================================
	
		public static List<Integer> gpGetSecondLineDrugConceptIds() {
			ArrayList<Integer> list = new ArrayList<Integer>();
			StringTokenizer tokenizer = new StringTokenizer(DrugOrderExportUtil.getSecondLineDrugConceptIds(), ",");
			while (tokenizer.hasMoreTokens()) {
				Integer id = Integer.parseInt(tokenizer.nextToken());
				list.add(id);
			}
			return list;
		}
	
	//==========================================================================================================================================
	
	public static String gpGetFirstLineDrugIds() {
		return Context.getAdministrationService().getGlobalProperty("drugorderexport.firstLineDrugsIds");
	}
	//==========================================================================================================================================
	
		public static String gpGetFirstLineDrugConceptIds() {
			return Context.getAdministrationService().getGlobalProperty("drugorderexport.firstLineDrugConceptIds");
		}
	//==========================================================================================================================================
	
	public static List<Integer> gpGetFirstLineDrugsAsList() {
		ArrayList<Integer> list = new ArrayList<Integer>();
		StringTokenizer tokenizer = new StringTokenizer(gpGetFirstLineDrugIds(), ",");
		while (tokenizer.hasMoreTokens()) {
			Integer id = Integer.parseInt(tokenizer.nextToken());
			list.add(id);
		}
		return list;
	}
	//==========================================================================================================================================
	
		public static List<Integer> gpGetFirstLineDrugConceptIdsList() {
			ArrayList<Integer> list = new ArrayList<Integer>();
			StringTokenizer tokenizer = new StringTokenizer(gpGetFirstLineDrugConceptIds(), ",");
			while (tokenizer.hasMoreTokens()) {
				Integer id = Integer.parseInt(tokenizer.nextToken());
				list.add(id);
			}
			return list;
		}
		
	//==========================================================================================================================================
	
	public static String gpGetARVConceptIds() {
		return Context.getAdministrationService().getGlobalProperty("drugorderexport.arvConceptIds");
	}
	
	//==========================================================================================================================================
	
	public static String gpGetARVDrugsIds() {
		return Context.getAdministrationService().getGlobalProperty("drugorderexport.arvDrugsIds");
	}
	//==========================================================================================================================================
	
		public static String gpGetTbDrugsConceptsIds() {
			return Context.getAdministrationService().getGlobalProperty("drugorderexport.tbDrugIds");
		}
		//==========================================================================================================================================
		
		public static List<Integer> gpGetTBDrugsConceptsIds() {
			ArrayList<Integer> tbConceptsIds = new ArrayList<Integer>();
			StringTokenizer tokenizer = new StringTokenizer(gpGetTbDrugsConceptsIds(), ",");
			while (tokenizer.hasMoreTokens()) {
				Integer id = Integer.parseInt(tokenizer.nextToken());
				tbConceptsIds.add(id);
			}
			return tbConceptsIds;
		}
	//==========================================================================================================================================
	
	public static List<Integer> gpGetARVDrugIds() {
		ArrayList<Integer> arvIds = new ArrayList<Integer>();
		StringTokenizer tokenizer = new StringTokenizer(gpGetARVDrugsIds(), ",");
		while (tokenizer.hasMoreTokens()) {
			Integer id = Integer.parseInt(tokenizer.nextToken());
			arvIds.add(id);
		}
		return arvIds;
	}
	
	//==========================================================================================================================================
	
	/**
	 * exports data to csv file
	 * 
	 * @param request
	 * @param response
	 * @param patients patient object
	 * @param filename file name
	 * @param title file title
	 * @throws IOException
	 * @throws ParseException
	 */
	
	public static void exportData(HttpServletRequest request, HttpServletResponse response, List<Patient> patients)throws Exception,
	                                                                                                               Exception {
	                                                                                                               
		DrugOrderService service = (DrugOrderService) Context.getService(DrugOrderService.class);
		SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");
		//		
		//		service.exportData(request, response, patients, "patientDrugOrder.csv");
		PrintWriter op = response.getWriter();
		
		List<Object[]> listPatientHistory = new ArrayList<Object[]>();
		List<Regimen> regimens = new ArrayList<Regimen>();
		List<PatientProgram> patientPrograms = new ArrayList<PatientProgram>();
		Patient patientsObj = new Patient();
		
		response.setContentType("text/plain");
		response.setHeader("Content-Disposition", "attachment; filename=\"drugorder.csv\"");
		
		if(Context.hasPrivilege("View Patient Names"))
		op.print("TRACnet ID,COHORT ID,FAMILY NAME,GIVEN NAME,GENDER,AGE,PROGRAM(Enrollment Date-dd/mm/yyyy),Status");
		else
			op.print("TRACnet ID,COHORT ID,GENDER,AGE,PROGRAM(Enrollment Date-dd/mm/yyyy),Program Status");
		
		HashMap<Patient, List<Regimen>> patientRegLists = new HashMap<Patient, List<Regimen>>();
		int maxRegHistorySize = 1;
		
		Concept CD4CountConcept = Context.getConceptService().getConcept(5497);
		Concept hivViralLoad = Context.getConceptService().getConcept(856);
		Concept weightConcept = Context.getConceptService().getConcept(5089);
		
		int patientMaxCD4Count = 0;
		int patientMaxVL = 0;
		
//		log.info("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx_patients "+patients.size());
		
		for (Patient patient : patients) {
			RegimenHistory history = RegimenUtils.getRegimenHistory(patient);
			List<Regimen> regimenList = history.getRegimenList();
			patientRegLists.put(patient, regimenList);
			if (regimenList.size() > maxRegHistorySize)
				maxRegHistorySize = regimenList.size();
			
			List<String> patientCD4Count = service.getAllPatientObsList(patient, CD4CountConcept);
			List<String> patientVL = service.getAllPatientObsList(patient, hivViralLoad);
			
			if (patientCD4Count.size() > patientMaxCD4Count) {
				patientMaxCD4Count = patientCD4Count.size();
			}
			if (patientVL.size() > patientMaxVL) {
				patientMaxVL = patientVL.size();
			}
			
		}
		op.print(",FIRST VIRAL LOAD,LAST VIRAL LOAD");
		
		op.print(",FIRST WEIGHT(kg),LAST WEIGHT(kg)");
		
		for (int i = 0; i < patientMaxCD4Count - 1; i++) {
			op.print(",CD4 COUNT,Obs_Datetime ");
		}
		
		if (maxRegHistorySize != 1) {
			op.print(",REGIMEN START DATE,REGIMEN END DATE");
		}
		
		// regimen row (display regimen in excel sheet)
		
		int maxValue = patientMaxCD4Count * 2 + 15;
		
		op.println();
		Date date=null;
		for (Patient patient : patients) {
			String progAndDate ="";
			Collection<PatientProgram> patPrograms = Context.getProgramWorkflowService().getPatientPrograms(patient, null, null, null, null, null, false);

				for (PatientProgram pg : patPrograms) {
					date = pg.getDateCompleted();
					if(date==null)
					progAndDate+=pg.getProgram().getName()+"(Still Enrolled) ";
					else if(date!=null)
						progAndDate+=pg.getProgram().getName()+" (Completed on "+f.format(pg.getDateCompleted())+") ";
				}
				
			listPatientHistory.add(new Object[] { patient,
			        Context.getProgramWorkflowService().getPatientPrograms(patient, null, null, null, null, null, false),
			        patientRegLists.get(patient), service.getAllPatientObs(patient, CD4CountConcept),
			        service.getAllPatientObsList(patient, hivViralLoad),
			        service.getAllPatientObsList(patient, weightConcept), patient.getPatientIdentifier(4),
			        progAndDate
			        }
			);
			
		}
		
		if (listPatientHistory.size() > 0) {
			for (Object objects[] : listPatientHistory) {
				patientsObj = (Patient) objects[0];
				if(Context.hasPrivilege("View Patient Names")){
				op.print(patientsObj.getPatientIdentifier() + "," + patientsObj.getPatientIdentifier(4) + ","
				        + patientsObj.getGivenName() + "," + patientsObj.getFamilyName() + "," + patientsObj.getGender()
				        + "," + patientsObj.getAge());
				}
				else{
					op.print(patientsObj.getPatientIdentifier() + "," + patientsObj.getPatientIdentifier(4) + ","
				        + patientsObj.getGender()
				        + "," + patientsObj.getAge());
				}
				
				String patientProgramsAsString = "";
				ArrayList<String> pPrograms = new ArrayList<String>();
				patientPrograms = (List<PatientProgram>) objects[1];
				
				
				
				for (PatientProgram patientProgram : patientPrograms) {
					
					if (patientProgram.getDateEnrolled() != null) {
						pPrograms.add(patientProgram.getProgram().getName() + " ("+ service.getDateFormated(patientProgram.getDateEnrolled()) + ")");
					} else {
						pPrograms.add(patientProgram.getProgram().getName());
					}
					patientProgramsAsString = service.getStringFromArrayListOfString(pPrograms);
					patientProgramsAsString = patientProgramsAsString.substring(0, patientProgramsAsString.lastIndexOf(";"));
				}
				
				op.print("," + patientProgramsAsString);
				
				String progAndDateStr = objects[7].toString();
				op.print(","+progAndDateStr);
				
				String CD4CountAndDate = (String) objects[3];
				ArrayList<String> HIViralLoads = (ArrayList<String>) objects[4];
				ArrayList<String> patientWeights = (ArrayList<String>) objects[5];
				
				String firstVL = null;
				String lastVL = null;
				if (HIViralLoads != null && HIViralLoads.size() != 0) {
					firstVL = HIViralLoads.get(0).toString();
					lastVL = HIViralLoads.get(HIViralLoads.size() - 1).toString();
				} else {
					firstVL = "-";
					lastVL = "-";
				}
				
				String firstWeight = null;
				String lastWeight = null;
				
				if (patientWeights != null && patientWeights.size() != 0) {
					firstWeight = patientWeights.get(0).toString();
					lastWeight = patientWeights.get(patientWeights.size() - 1).toString();
				} else {
					firstWeight = "-";
					lastWeight = "-";
				}
				
				op.print("," + firstVL + "," + lastVL);
				op.print("," + firstWeight + "," + lastWeight);
				
				// patient CD4 COUNT
				if (CD4CountAndDate.length() != 0) {
					op.print("," + CD4CountAndDate);
					
				}
				op.println();
				
				//empty string before displaying regimens
				String emptyStr = new String();
				for (int i = 1; i < maxValue - 1; i++) {
					emptyStr += ",";
				}
				
				Set<RegimenComponent> regimenComponent = new HashSet<RegimenComponent>();
				
				String patRegimens = "";
				
				Set<RegimenComponent> componentsStopped = new HashSet<RegimenComponent>();
				
				RegimenHistory history = RegimenUtils.getRegimenHistory(patientsObj);
				regimens = history.getRegimenList();
				
				for (Regimen r : regimens) {
					regimenComponent = r.getComponents();
					
					if (r.getEndDate() == null) {
						r.setEndDate(new Date());
					}
					for (RegimenComponent rc : regimenComponent) {
						if (rc.getStopDate() != null)
							if (rc.getStopDate().getTime() <= r.getStartDate().getTime()) {
								componentsStopped.add(rc);
								
							}
					}
					
					regimenComponent.removeAll(componentsStopped);
					
					patRegimens = service.getRegimensAsString(regimenComponent);
					
					
					if (!patRegimens.toString().equals("")) {
						op.print(emptyStr + f.format(r.getStartDate()) + "," + f.format(r.getEndDate()) + "," + patRegimens);
						op.println();
					} else {
						op.print(emptyStr + f.format(r.getStartDate()) + "," + f.format(r.getEndDate()) + "," + "-");
						op.println();
					}
					
				}
				
				op.println();
				
			}
			
		}
		
		op.flush();
		op.close();
		
	}
	
	//==========================================================================================================================================
	
	/**
	 * allows to export patient list to pdf 
	 * 
	 * @param patients
	 * @param request
	 * @param response
	 * @param filename
	 * @param title
	 * @throws DocumentException
	 * @throws FileNotFoundException
	 */
	public static void exportToPDF(HttpServletRequest request, HttpServletResponse response, List<Patient> patients)
	                             throws Exception,Exception {
		
		DrugOrderService service = (DrugOrderService) Context.getService(DrugOrderService.class);
		//		
		//		service.exportToPDF(request, response, patients, "patientDrugOrder.csv");
		Document document = new Document();
		
		response.setContentType("application/pdf");
		response.setHeader("Content-Disposition", "attachment; filename=\"patientDrugOrder.pdf\"");
		
		PdfWriter.getInstance(document, response.getOutputStream());
		
		document.open();
		
		float[] colsWidth = { 3f, 7f, 8f, 8f, 4f, 4f, 16f };
		float[] colsWidth1 = { 3f, 5f,6f, 6f, 16f };
		
		PdfPTable table = null;
		
		if(Context.hasPrivilege("View Patient Names")){
			 table = new PdfPTable(colsWidth);
		}
		else{
			 table = new PdfPTable(colsWidth1);
		}
		
		document.add(new Paragraph("  "));
		
		
		document.add(new Paragraph("                                           List of Patients And Their Regimens"));
		document.add(new Paragraph("  "));
		document.add(new Paragraph("  "));
		
		table.addCell("#");
		table.addCell("Patient Id");
		if(Context.hasPrivilege("View Patient Names")){
		table.addCell("FAMILY NAME");
		table.addCell("GIVEN NAME");
		}

		table.addCell("AGE");
		table.addCell("SEX");
		table.addCell("REGIMENS");
		
		log.info("iiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiipatients "+patients.size());
		
		for (int i = 0; i < patients.size(); i++) {
			
			table.addCell(i + 1 + "");
			if (patients.get(i).getPatientIdentifier() != null) {
				table.addCell(patients.get(i).getPatientIdentifier() + "");
			} else if (patients.get(i).getPatientIdentifier() == null) {
				table.addCell("-");
			}
			if(Context.hasPrivilege("View Patient Names")){
			table.addCell(patients.get(i).getFamilyName() + "");
			table.addCell(patients.get(i).getGivenName() + "");
			}

			table.addCell(patients.get(i).getAge() + "");
			table.addCell(patients.get(i).getGender() + "");
			
			RegimenHistory history = RegimenUtils.getRegimenHistory(patients.get(i));
			List<Regimen> regimens = history.getRegimenList();
			
			
			Set<RegimenComponent> regimenComponents = new HashSet<RegimenComponent>();
			
			String patRegimens = "";
			
			//Set<RegimenComponent> componentsStopped = new HashSet<RegimenComponent>();
			for (Regimen r : regimens) {
				regimenComponents = r.getComponents();
				patRegimens = service.getRegimensAsString(regimenComponents);
			}
			table.addCell(patRegimens);
			
		}
		
		document.add(table);
		document.getJavaScript_onLoad();
		document.close();
		
	}
	
	
	//==========================================================================================================================================

}
