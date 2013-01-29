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
package org.openmrs.module.drugorderexport.db.dao;

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
public interface DrugOrderExportDao {
	
	/**
	 * gives the drug having the given id 
	 * 
	 * @param drugId
	 * @return drug
	 */
	public Drug getDrugById(int drugId);
	
	/**
	 * Auto generated method comment takes a list of ids and returns the corresponding drugs
	 * 
	 * @param drugIdsList
	 * @return list of drug
	 */
	public List<Drug> getListOfDrugsByIds(List<Integer> drugIdsList);
	
	/**
	 * gives the concept having the given id
	 * 
	 * @param conceptId
	 * @return
	 */
	public Concept getConceptById(int conceptId);
	
	/**
	 * Searches (any,all,none) patients who are (or not) taking the provided set(list) of drugs in
	 * the specific period of time if these criteria are specified
	 * 
	 * @param anyOrAll
	 * @param startdate
	 * @param enddate
	 * @param drugs
	 * @return list Patient Object
	 */
	public List<Integer> searchDrugOrderByDrug(String anyOrAll, Date startdate, Date enddate, List<Drug> drugs,
	                                           String gender, Date minAge, Date maxAge, Date minBirthdate, Date maxBirthdate);
	
	
	/**
	 * Searches (any,all,none) patients who stopped or changed the provided set(list) drugs and it
	 * generic in the specific period of time and for given reason if these criteria are specified
	 * 
	 * @param startdate
	 * @param enddate
	 * @param drugs
	 * @param stopReason
	 * @param gererics
	 * @return list Patient Object
	 */
	
	public List<Integer> searchDrugOrderByStopReason(Date startdate, Date enddate, List<Integer> drugs,
	                                                 List<Integer> stopReason, List<Integer> gererics, String gender,
	                                                 Date minAge, Date maxAge);
	
	/**
	 * helps to filter information using the given period
	 * 
	 * @param startDate
	 * @param endDate
	 * @return string
	 */
	public String checkInputDate(Date startDate, Date endDate);
	
	/**
	 * helps to use the patient attributes gives a all given patient attributes as a string which is
	 * used in mysql query
	 * 
	 * @param gender
	 * @param minAge
	 * @param maxAge
	 * @param minBirthdate
	 * @param maxBirthdate
	 * @param aliveOnly
	 * @param deadOnly
	 * @return string
	 */
	public String patientAttributeStr(String gender, Date minAge, Date maxAge, Date minBirthdate, Date maxBirthdate);
	
	/**
	 * gives the patient histories
	 * 
	 * @param gender
	 * @param minAge
	 * @param maxAge
	 * @param minBirthdate
	 * @param maxBirthdate
	 * @return patient ids
	 */
	public List<Integer> patientsWhoHasDrugOrder(String gender, Date minAge, Date maxAge, Date minBirthdate,
	                                             Date maxBirthdate);
	
	/**
	 * gives observations in the period
	 * 
	 * @param concept
	 * @return list of patients
	 */
	public Double getPatientObsValue(Patient patient, Concept concept, Date obsStartDate, Date obsEndDate);
	
	/**
	 * comment gives all IDs patient who started the treatment for the first
	 * time on a given date
	 * 
	 * @param startDate
	 * @param endDate
	 * @param gender
	 * @param minAge
	 * @param maxAge
	 * @param minBirthdate
	 * @param maxBirthdate
	 * @param aliveOnly
	 * @param deadOnly
	 * @return Integer
	 */
	public List<Integer> getPatientWhoStartedOnDate(Date startDate, Date endDate, String gender, Date minAge, Date maxAge,
	                                                Date minBirthdate, Date maxBirthdate);
	
	/**
	 *helps to get when the patient have started the treatment for
	 * first time
	 * 
	 * @param startDateList
	 * @return Date
	 */
	public Date getMinStartDate(List<Date> startDateList);
	
	/**
	 *helps to get patients who have drugs they were changing on the
	 * provided date
	 * 
	 * @param startDate
	 * @param endDate
	 * @param gender
	 * @param minAge
	 * @param maxAge
	 * @param minBirthdate
	 * @param maxBirthdate
	 * @param aliveOnly
	 * @param deadOnly
	 * @return Integer
	 */
	
	public List<Integer> getPatientsWhoChangedRegimen(Date startDate, Date endDate, String gender, Date minAge, Date maxAge,
	                                                  Date minBirthdate, Date maxBirthdate);
	
	/**
	 * gives when patient started treatment for the first time using query
	 * 
	 * @param patient
	 * @return date
	 */
	
	public Date getWhenPatientStarted(Patient patient);
	
	/**
	 * gives patient taking only prophylaxis
	 */
	public List<Integer> getPatientsOnProphylaxis(List<Integer> prophylaxisDrugIds, List<Integer> patientIds);
	
	/**
	 * gives patients based on optional attributes
	 * 
	 * @param gender
	 * @param minAge
	 * @param maxAge
	 * @param minBirthdate
	 * @param maxBirthdate
	 * @return patient ids
	 */
	
	public List<Integer> getPatientBasedOnAttributes(String gender, Date minAge, Date maxAge, Date minBirthdate,
	                                                 Date maxBirthdate);
	
	/**
	 * gives patients taking drugs in a given period
	 * 
	 * @param startDate
	 * @param endDate
	 * @param gender
	 * @param minAge
	 * @param maxAge
	 * @param minBirthdate
	 * @param maxBirthdate
	 * @return patient Ids
	 */
	public List<Integer> getPatientsTakingDrugsInThePeriod(Date startDate, Date endDate, String gender, Date minAge,
	                                                       Date maxAge, Date minBirthdate, Date maxBirthdate);
	
	/**
	 * gives patients exited from care in a given period
	 * 
	 * @param startDate
	 * @param endDate
	 * @return patient Ids
	 */
	public List<Integer> getPatientsExitedInThePeriod(Date startDate, Date endDate);
	
	/**
	 * gives voided patients in a given period
	 * 
	 * @param startDate
	 * @param endDate
	 * @return patient Ids
	 */
	public List<Integer> getPatientsVoidedInThePeriod(Date startDate, Date endDate);
	
	
	/**
	 * allows to get all patient observation's values and the date of the event 
	 * 
	 * @param p
	 * @param c
	 * @return list of the objects
	 */
	public String getAllPatientObs(Patient p, Concept c);
	
	/**
	 * does the same thing with getAllPatientObs(Patient p, Concept c) but its used to know the size
	 * of a list
	 * 
	 * @param p
	 * @param c
	 * @return list of concept value and its obs datetime
	 */
	public List<String> getAllPatientObsList(Patient p, Concept c);
	
	/**
	 * gives patients exited from care
	 */
	public List<Integer> getPatientsExitedFromCare();
	
	/**
	 * gives prophylaxis ids
	 * 
	 * @return patient ids list
	 */
	public List<Integer> getProphylaxisDrugsIds();
	
	/**
	 * gives all patients on prophylaxis
	 * 
	 * @param drugs list
	 * @param startDate
	 * @param endDate
	 * @param gender
	 * @param minAge
	 * @param maxAge
	 * @param minBirthdate
	 * @param maxBirthdate
	 * @return ids of patients
	 */
	public List<Integer> getAllProphylaxisPatients(Date startDate, Date endDate, String gender, Date minAge, Date maxAge,
	                                               Date minBirthdate, Date maxBirthdate);
	
	/**
	 * returns cumulative new patients on prophylaxis
	 * 
	 * @param startDate
	 * @param endDate
	 * @param gender
	 * @param minAge
	 * @param maxAge
	 * @param minBirthdate
	 * @param maxBirthdate
	 * @return patient's ids
	 */
	public List<Integer> getNewPatientsOnProphylaxis(Date startDate, Date endDate, String gender, Date minAge, Date maxAge,
	                                                 Date minBirthdate, Date maxBirthdate);
	
	/***
	 * retrieves ids of active patients ids
	 * 
	 * @param patients
	 * @param endDate
	 * @return ids
	 */
	public List<Integer> getActivePatients(List<Integer> patients, Date endDate);
	
	/**
	 * forms a date from age 
	 * 
	 * @param age
	 * @return date
	 */
	public Date getDateObjectFormAge(int age);
	
	/**
	 * helps to display patient regimen
	 * 
	 * @param regimens
	 * @return
	 */
	public String getRegimensAsString(Set<RegimenComponent> regimens);
	
	/*****************************************************************************************************************************************/
	/**
	 * takes an arrayList and form a string to be well displayed in csv
	 * 
	 * @param listOfIds
	 * @return String
	 */
	public String getStringFromArrayListOfString(ArrayList<String> listOfIds);
	
	/********************************************************************************************************************************/
	/**
	 * allows to format a date
	 */
	public String getDateFormated(Date dateObject) ;
	

	/**
	 * gives patient last encounter
	 * 
	 * @param patientId
	 * @return
	 */
	public Date getPatientLastEncounterDate(Integer patientId);
	
	/**
	 * 
	 * gives patient last visit date
	 * 
	 * @param patientId
	 * @return
	 */
	public Date getPatientLastVisitDate(Integer patientId);
	
	
	/**
	 * 
	 *helps to know when patient started the given regimen category
	 * 
	 * @param patientId
	 * @return
	 */
	public Date getWhenPatStartedXRegimen(Integer patientId,String gpDrugs);
	
	/**
	 * returns active preART patients
	 * 
	 * @param patients
	 * @param endDate
	 * @return List<Integer>
	 */
	public List<Integer> getActivePreARTPatients(List<Integer> patients, Date endDate);
	
	/**
	 * 
	 * gives patients on first line
	 * 
	 * @param startdate
	 * @param enddate
	 * @param gender
	 * @param minAge
	 * @param maxAge
	 * @param minBirthdate
	 * @param maxBirthdate
	 * @return ids list
	 */
	public List<Integer> getPatientsOnFirstLineReg(Date startdate, Date enddate,
        String gender, Date minAge, Date maxAge, Date minBirthdate, Date maxBirthdate);
	
	 public List<Integer> getPatientsOnRegimenCategory(String categoryConceptId, Date startDate, Date endDate,
         String gender, Date minAge, Date maxAge, Date minBirthdate,
         Date maxBirthdate);
	
	 /**
	  * 
	  * gets HIV Drugs
	  * 
	  * @return HIV Drugs list
	  */
	 public List<Drug> getArvDrugs();
	 
	 /**
	  * 
	  * get ARV drugs by concepts
	  * 
	  * @return
	  */
	 public List<Object[]> getArvDrugsByConcepts();
	 
	 public List<Integer> searchDrugOrderByDrugsConcepts(String anyOrAll, Date startdate, Date enddate, List<Drug> drugs,
             String gender, Date minAge, Date maxAge, Date minBirthdate, Date maxBirthdate) ;
	
	 public Drug getDrugByConceptId(int conceptId);
	 
	 public List<Drug> getListOfDrugsByConceptsIds(List<Integer> drugConceptsIdsList);
	 
	 public List<Integer> getPatientOnAllDrugsByConceptIds(List<Integer> listDrugIdsByConcept, List<Integer> patientIds);
	 
	 public Boolean isLastRegimenProphy(Patient p);
	 
	 public boolean isPatientOnProphylaxisOnly(Integer patientId);
}
