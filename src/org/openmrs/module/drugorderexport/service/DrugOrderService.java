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
package org.openmrs.module.drugorderexport.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.openmrs.Concept;
import org.openmrs.Drug;
import org.openmrs.Patient;
import org.openmrs.module.regimenhistory.RegimenComponent;

/**
 *
 */
public interface DrugOrderService {
	
	public Drug getDrugById(int drugId);
	
	public List<Drug> getListOfDrugsByIds(List<Integer> drugIdsList);
	
	public List<Integer> searchDrugOrderByDrug(String baseCriteria, Date startdate, Date enddate, List<Drug> drugs,
	                                           String gender, Date minAge, Date maxAge, Date minBirthdate, Date maxBirthdate);
	
	public List<Integer> searchDrugOrderByStopReason(Date startdate, Date enddate, List<Integer> drugs,
	                                                 List<Integer> stopReason, List<Integer> gererics, String gender,
	                                                 Date minAge, Date maxAge);
	
	public String checkInputDate(Date startDate, Date endDate);
	
	public String patientAttributeStr(String gender, Date minAge, Date maxAge, Date minBirthdate, Date maxBirthdate);
	
	
	public List<Integer> patientsWhoHasDrugOrder(String gender, Date minAge, Date maxAge, Date minBirthdate,
	                                             Date maxBirthdate);
	
	public Double getPatientObsValue(Patient patient, Concept concept, Date obsStartDate, Date obsEndDate);
	
	public Date getMinStartDate(List<Date> startDateList);
	
	public List<Integer> getPatientWhoStartedOnDate(Date startDate, Date endDate, String gender, Date minAge, Date maxAge,
	                                                Date minBirthdate, Date maxBirthdate);
	
	public List<Integer> getPatientsWhoChangedRegimen(Date startDate, Date endDate, String gender, Date minAge, Date maxAge,
	                                                  Date minBirthdate, Date maxBirthdate);
	
	public Date getWhenPatientStarted(Patient patient);
	
	public List<Integer> getPatientsOnProphylaxis(List<Integer> prophylaxisDrugIds, List<Integer> patientIds);
	
	public List<Integer> getPatientBasedOnAttributes(String gender, Date minAge, Date maxAge, Date minBirthdate,
	                                                 Date maxBirthdate);
	
	public List<Integer> getPatientsTakingDrugsInThePeriod(Date startDate, Date endDate, String gender, Date minAge,
	                                                       Date maxAge, Date minBirthdate, Date maxBirthdate);
	
	public List<Integer> getPatientsExitedInThePeriod(Date startDate, Date endDate);
	
	public List<Integer> getPatientsVoidedInThePeriod(Date startDate, Date endDate);
	
	
	public String getAllPatientObs(Patient p, Concept c);
	
	public List<String> getAllPatientObsList(Patient p, Concept c);
	
	
	public List<Integer> getPatientsExitedFromCare(Date endDate);
	
	public List<Integer> getProphylaxisDrugsIds();
	
	
	public List<Integer> getAllProphylaxisPatients(Date startDate, Date endDate, String gender, Date minAge, Date maxAge,
	                                               Date minBirthdate, Date maxBirthdate);
	
	public List<Integer> getNewPatientsOnProphylaxis(Date startDate, Date endDate, String gender, Date minAge, Date maxAge,
        Date minBirthdate, Date maxBirthdate);
	
	
	public List<Integer> getActivePatients(List<Integer> patients, Date endDate);
	
	public Date getDateObjectFormAge(int age);
	
	public String getRegimensAsString(Set<RegimenComponent> regimens);
	
	public String getStringFromArrayListOfString(ArrayList<String> listOfIds) ;
	
	public String getDateFormated(Date dateObject);
	
	public Date getPatientLastEncounterDate(Integer patientId);
	
	public Date getPatientLastVisitDate(Integer patientId);
	
	public Date getWhenPatStartedXRegimen(Integer patientId,String gpDrugs);

	 public List<Integer> getActivePreARTPatients(List<Integer> patients, Date endDate);

	 public List<Integer> getPatientsOnFirstLineReg(Date startdate, Date enddate, String gender, Date minAge, Date maxAge,
         Date minBirthdate, Date maxBirthdate);
	 
	 public List<Integer> getPatientsOnRegimenCategory(String categoryConceptId, Date startDate, Date endDate,
         String gender, Date minAge, Date maxAge, Date minBirthdate,
         Date maxBirthdate);
	
	 public List<Drug> getArvDrugs();
	 
	 public List<Object[]> getArvDrugsByConcepts();
	 
	 public List<Integer> searchDrugOrderByDrugsConcepts(String anyOrAll, Date startdate, Date enddate, List<Drug> drugs,
             String gender, Date minAge, Date maxAge, Date minBirthdate, Date maxBirthdate) ;
	 
	 public Drug getDrugByConceptId(int conceptId);
	 
	 public List<Drug> getListOfDrugsByConceptsIds(List<Integer> drugConceptsIdsList);
	 public List<Integer> getPatientOnAllDrugsByConceptIds(	List<Integer> listDrugIdsByConcept, List<Integer> patientIds);
	 
	 public Boolean isLastRegimenProphy(Patient p);
	 
	 public boolean isPatientOnProphylaxisOnlyBeforePeriod(Integer patientId,Date enddate);
	 
	 public List<Integer> getPatientsOnRegimenCategoryActive(String categoryConceptId, Date startDate, Date endDate,
	         String gender, Date minAge, Date maxAge, Date minBirthdate,
	         Date maxBirthdate);
	 
	 public List<Integer> searchDrugOrderByDrugActive(String anyOrAll, Date startdate, Date enddate, List<Drug> drugs,
             String gender, Date minAge, Date maxAge, Date minBirthdate, Date maxBirthdate);
	 
	 public List<Integer> getActiveOnDrugsPatients(List<Integer> patients,String list,Date endDate);
	 
	 public List<Integer> getFirstLinePatientsBeforeDate(Date startdate, Date enddate, String gender, Date minAge, Date maxAge,
	            Date minBirthdate, Date maxBirthdate);
	 
	 public List<Integer> getSecondLinePatientsBeforeDate(Date startdate, Date enddate, String gender, Date minAge, Date maxAge,
	            Date minBirthdate, Date maxBirthdate);
	 
	 public List<Integer> getThirdLinePatientsBeforeDate(Date startdate, Date enddate, String gender, Date minAge, Date maxAge,
	            Date minBirthdate, Date maxBirthdate);
	
}
