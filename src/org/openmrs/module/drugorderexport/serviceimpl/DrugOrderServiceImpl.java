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
package org.openmrs.module.drugorderexport.serviceimpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.openmrs.Concept;
import org.openmrs.Drug;
import org.openmrs.Patient;
import org.openmrs.module.drugorderexport.db.dao.DrugOrderExportDao;
import org.openmrs.module.drugorderexport.service.DrugOrderService;
import org.openmrs.module.regimenhistory.RegimenComponent;

/**
 *
 */
public class DrugOrderServiceImpl implements DrugOrderService {
	
	private DrugOrderExportDao drugorderDAO;
	/**
	 * 
	 * @see org.openmrs.module.drugorderexport.service.DrugOrderService#getListOfDrugsByIds(java.util.List)
	 */
    public List<Drug> getListOfDrugsByIds(List<Integer> drugIdsList){
    	return drugorderDAO.getListOfDrugsByIds(drugIdsList);
    }
	/**
	 * @see org.openmrs.module.drugorderexport.service.DrugOrderService#searchDrugOrderByDrug(java.lang.String,
	 *      java.util.Date, java.util.Date, java.util.List)
	 */
	public List<Integer> searchDrugOrderByDrug(String baseCriteria, Date startdate, Date enddate, List<Drug> drugs,
	                                      String gender, Date minAge, Date maxAge,
	                                      Date minBirthdate, Date maxBirthdate) {
		return drugorderDAO.searchDrugOrderByDrug(baseCriteria, startdate, enddate, drugs,
			gender, minAge, maxAge, minBirthdate, maxBirthdate);
		
	}
	
	/**
	 * @see org.openmrs.module.drugorderexport.service.DrugOrderService#searchDrugOrderByStopReason(java.util.Date,
	 *      java.util.Date, java.util.List, java.lang.String, org.openmrs.Concept)
	 */
	public List<Integer> searchDrugOrderByStopReason(Date startdate, Date enddate, List<Integer> drugs,
	                                                       List<Integer> stopReason, List<Integer> gererics,
	                                                       String gender, Date minAge,
	                                                       Date maxAge) {
		return  drugorderDAO.searchDrugOrderByStopReason(startdate, enddate, drugs, stopReason, gererics,
			gender, minAge, maxAge);
		
	}
	
	/**
	 * @param drugorderDAO the drugorderDAO to set
	 */
	public void setDrugorderDAO(DrugOrderExportDao drugorderDAO) {
		this.drugorderDAO = drugorderDAO;
	}
	
	/**
	 * @return the drugorderDAO
	 */
	public DrugOrderExportDao getDrugorderDAO() {
		return drugorderDAO;
	}
		
	public String checkInputDate(Date startDate, Date endDate) {
		return drugorderDAO.checkInputDate(startDate, endDate);
	}
	
	/**
	 * @see org.openmrs.module.drugorderexport.service.DrugOrderService#patientAttributeStr(java.lang.String,
	 *      int, int, java.lang.String, java.lang.String, java.lang.Boolean, java.lang.Boolean)
	 */
	public String patientAttributeStr(String gender, Date minAge, Date maxAge, Date minBirthdate, Date maxBirthdate) {
		return drugorderDAO.patientAttributeStr(gender, minAge, maxAge, minBirthdate, maxBirthdate);
	}	
	/**
	 * @see org.openmrs.module.drugorderexport.service.DrugOrderService#patientsWhoHasDrugOrder(int,
	 *      java.lang.String, java.util.Date, java.util.Date, java.util.Date, java.util.Date,
	 *      boolean, boolean)
	 */
	public List<Integer> patientsWhoHasDrugOrder(String gender, Date minAge, Date maxAge,
	                                        Date minBirthdate, Date maxBirthdate) {
		
		return drugorderDAO.patientsWhoHasDrugOrder(gender, minAge, maxAge, minBirthdate, maxBirthdate);
	}


	/**
     * @see org.openmrs.module.drugorderexport.service.DrugOrderService#getDrugById(int)
     */
    public Drug getDrugById(int drugId) {
	    return drugorderDAO.getDrugById(drugId);
    }
		

    public Double getPatientObsValue(Patient patient,Concept concept, Date obsStartDate, Date obsEndDate){
    	return drugorderDAO.getPatientObsValue(patient,concept, obsStartDate, obsEndDate);
    }
	/**
     * @see org.openmrs.module.drugorderexport.service.DrugOrderService#getPatientWhoStartedOnDate(java.util.Date)
     */
    public List<Integer> getPatientWhoStartedOnDate(Date startDate, Date endDate,String gender, Date minAge, Date maxAge, Date minBirthdate, Date maxBirthdate) {
    	return drugorderDAO.getPatientWhoStartedOnDate(startDate, endDate,gender,minAge,maxAge,minBirthdate,maxBirthdate);
    }
    public Date getMinStartDate(List<Date> startDateList){
    	return drugorderDAO.getMinStartDate(startDateList);
    }
    
    public List<Integer> getPatientsWhoChangedRegimen(Date startDate, Date endDate, String gender, Date minAge, Date maxAge,
        Date minBirthdate, Date maxBirthdate){
    	return drugorderDAO.getPatientsWhoChangedRegimen(startDate, endDate, gender, minAge, maxAge, minBirthdate, maxBirthdate);
    }
    
    public Date getWhenPatientStarted(Patient patient){
    	return drugorderDAO.getWhenPatientStarted(patient);
    }
	/**
     * @see org.openmrs.module.drugorderexport.service.DrugOrderService#getPatientsOnProphylaxis(java.util.List, java.util.List)
     */
    @Override
    public List<Integer> getPatientsOnProphylaxis(List<Integer> prophylaxisDrugIds, List<Integer> patientIds) {
	    // TODO Auto-generated method stub
	    return drugorderDAO.getPatientsOnProphylaxis(prophylaxisDrugIds, patientIds);
    }
	/**
     * @see org.openmrs.module.drugorderexport.service.DrugOrderService#getPatientBasedOnAttributes(java.lang.String, java.util.Date, java.util.Date, java.util.Date, java.util.Date)
     */
    @Override
    public List<Integer> getPatientBasedOnAttributes(String gender, Date minAge, Date maxAge, Date minBirthdate,
                                                     Date maxBirthdate) {
	    // TODO Auto-generated method stub
	    return drugorderDAO.getPatientBasedOnAttributes(gender, minAge, maxAge, minBirthdate, maxBirthdate);
    }
	/**
     * @see org.openmrs.module.drugorderexport.service.DrugOrderService#getPatientsTakingDrugsInThePeriod(java.util.Date, java.util.Date)
     */
    @Override
    public List<Integer> getPatientsTakingDrugsInThePeriod(Date startDate, Date endDate,String gender,Date minAge,Date maxAge,Date minBirthdate,Date maxBirthdate) {
	    // TODO Auto-generated method stub
	    return drugorderDAO.getPatientsTakingDrugsInThePeriod(startDate, endDate,gender,minAge,maxAge,minBirthdate,maxBirthdate);
    }
	/**
     * @see org.openmrs.module.drugorderexport.service.DrugOrderService#getPatientsExitedInThePeriod(java.util.Date, java.util.Date)
     */
    @Override
    public List<Integer> getPatientsExitedInThePeriod(Date startDate, Date endDate) {
	    return drugorderDAO.getPatientsExitedInThePeriod(startDate, endDate);
    }
	/**
     * @see org.openmrs.module.drugorderexport.service.DrugOrderService#getPatientsVoidedInThePeriod(java.util.Date, java.util.Date)
     */
    @Override
    public List<Integer> getPatientsVoidedInThePeriod(Date startDate, Date endDate) {
	    // TODO Auto-generated method stub
	    return drugorderDAO.getPatientsVoidedInThePeriod(startDate, endDate);
    }
	/**
     * @see org.openmrs.module.drugorderexport.service.DrugOrderService#getAllPatientObs(org.openmrs.Patient, org.openmrs.Concept)
     */
    @Override
    public String getAllPatientObs(Patient p, Concept c) {
	    // TODO Auto-generated method stub
	    return drugorderDAO.getAllPatientObs(p, c);
    }
    public List<String> getAllPatientObsList(Patient p, Concept c){
    	return drugorderDAO.getAllPatientObsList(p, c);
    }

	/**
     * @see org.openmrs.module.drugorderexport.service.DrugOrderService#getPatientsExitedFromCare()
     */
    @Override
    public List<Integer> getPatientsExitedFromCare() {
	    // TODO Auto-generated method stub
	    return drugorderDAO.getPatientsExitedFromCare();
    }
	/**
     * @see org.openmrs.module.drugorderexport.service.DrugOrderService#getProphylaxisDrugsIds()
     */
    @Override
    public List<Integer> getProphylaxisDrugsIds() {
	    // TODO Auto-generated method stub
	    return drugorderDAO.getProphylaxisDrugsIds();
    }


	/**
     * @see org.openmrs.module.drugorderexport.service.DrugOrderService#getAllProphylaxisPatients(java.util.Date, java.util.Date, java.lang.String, java.util.Date, java.util.Date, java.util.Date, java.util.Date)
     */
    @Override
    public List<Integer> getAllProphylaxisPatients(Date startDate, Date endDate, String gender, Date minAge, Date maxAge,
                                                   Date minBirthdate, Date maxBirthdate) {
	    // TODO Auto-generated method stub
	    return drugorderDAO.getAllProphylaxisPatients(startDate, endDate, gender, minAge, maxAge, minBirthdate, maxBirthdate);
    }
	/**
     * @see org.openmrs.module.drugorderexport.service.DrugOrderService#getNewPatientsOnProphylaxis(java.util.Date, java.util.Date, java.lang.String, java.util.Date, java.util.Date, java.util.Date, java.util.Date)
     */
    @Override
    public List<Integer> getNewPatientsOnProphylaxis(Date startDate, Date endDate, String gender, Date minAge, Date maxAge,
                                                     Date minBirthdate, Date maxBirthdate) {
	    // TODO Auto-generated method stub
	    return drugorderDAO.getNewPatientsOnProphylaxis(startDate, endDate, gender, minAge, maxAge, minBirthdate, maxBirthdate);
    }
	/**
     * @see org.openmrs.module.drugorderexport.service.DrugOrderService#getActivePatients(java.util.List, java.util.Date)
     */
    @Override
    public List<Integer> getActivePatients(List<Integer> patients, Date endDate) {
	    // TODO Auto-generated method stub
	    return drugorderDAO.getActivePatients(patients, endDate);
    }
	/**
	 * @see org.openmrs.module.drugorderexport.service.DrugOrderService#getDateObjectFormAge(int)
	 */
	@Override
	public Date getDateObjectFormAge(int age) {
		// TODO Auto-generated method stub
		return drugorderDAO.getDateObjectFormAge(age);
	}
	/**
     * @see org.openmrs.module.drugorderexport.service.DrugOrderService#getRegimensAsString(java.util.Set)
     */
    @Override
    public String getRegimensAsString(Set<RegimenComponent> regimens) {
	    return drugorderDAO.getRegimensAsString(regimens);
    }
	/**
     * @see org.openmrs.module.drugorderexport.service.DrugOrderService#getStringFromArrayListOfString(java.util.ArrayList)
     */
    @Override
    public String getStringFromArrayListOfString(ArrayList<String> listOfIds) {
	    return drugorderDAO.getStringFromArrayListOfString(listOfIds);
    }
	/**
     * @see org.openmrs.module.drugorderexport.service.DrugOrderService#getDateFormated(java.util.Date)
     */
    @Override
    public String getDateFormated(Date dateObject) {
	    return drugorderDAO.getDateFormated(dateObject);
    }
	/**
     * @see org.openmrs.module.drugorderexport.service.DrugOrderService#getPatientLastEncounterDate(java.lang.Integer)
     */
    @Override
    public Date getPatientLastEncounterDate(Integer patientId) {
	    // TODO Auto-generated method stub
	    return drugorderDAO.getPatientLastEncounterDate(patientId);
    }
	/**
     * @see org.openmrs.module.drugorderexport.service.DrugOrderService#getPatientLastVisitDate(java.lang.Integer)
     */
    @Override
    public Date getPatientLastVisitDate(Integer patientId) {
	    // TODO Auto-generated method stub
	    return drugorderDAO.getPatientLastVisitDate(patientId);
    }
	/**
     * @see org.openmrs.module.drugorderexport.service.DrugOrderService#getWhenPatStartedXRegimen(java.lang.Integer, java.lang.String)
     */
    @Override
    public Date getWhenPatStartedXRegimen(Integer patientId, String gpDrugs) {
	    return drugorderDAO.getWhenPatStartedXRegimen(patientId, gpDrugs);
    }
	/**
     * @see org.openmrs.module.drugorderexport.service.DrugOrderService#getActivePreARTPatients(java.util.List, java.util.Date)
     */
    @Override
    public List<Integer> getActivePreARTPatients(List<Integer> patients, Date endDate) {
	    // TODO Auto-generated method stub
	    return drugorderDAO.getActivePreARTPatients(patients, endDate);
    }
	/**
     * @see org.openmrs.module.drugorderexport.service.DrugOrderService#patientOnFirstLineReg(java.util.Date, java.util.Date, java.lang.String, java.util.Date, java.util.Date, java.util.Date, java.util.Date)
     */
    @Override
    public List<Integer> getPatientsOnFirstLineReg(Date startdate, Date enddate, String gender, Date minAge, Date maxAge,
                                               Date minBirthdate, Date maxBirthdate) {
	    // TODO Auto-generated method stub
	    return drugorderDAO.getPatientsOnFirstLineReg(startdate, enddate, gender, minAge, maxAge, minBirthdate, maxBirthdate);
    }
	/**
     * @see org.openmrs.module.drugorderexport.service.DrugOrderService#getPatientsOnRegimenCategory(java.lang.String, java.util.Date, java.util.Date, java.lang.String, java.util.Date, java.util.Date, java.util.Date, java.util.Date)
     */
    @Override
    public List<Integer> getPatientsOnRegimenCategory(String categoryConceptId, Date startDate, Date endDate,
                                                      String gender, Date minAge, Date maxAge, Date minBirthdate,
                                                      Date maxBirthdate) {
	   
	    return drugorderDAO.getPatientsOnRegimenCategory(categoryConceptId,  startDate,  endDate, gender, minAge, maxAge, minBirthdate, maxBirthdate);
    }
	@Override
	public List<Drug> getArvDrugs() {
		return drugorderDAO.getArvDrugs();
	}
	
	
	@Override
	public List<Object[]> getArvDrugsByConcepts() {
		return drugorderDAO.getArvDrugsByConcepts();
	}
	
	
	@Override
	public List<Integer> searchDrugOrderByDrugsConcepts(String anyOrAll,
			Date startdate, Date enddate, List<Drug> drugs, String gender,
			Date minAge, Date maxAge, Date minBirthdate, Date maxBirthdate) {
		return drugorderDAO.searchDrugOrderByDrugsConcepts(anyOrAll, startdate, enddate, drugs, gender, minAge, maxAge, minBirthdate, maxBirthdate);
	}
	
	
	@Override
	public Drug getDrugByConceptId(int conceptId) {
		return drugorderDAO.getDrugByConceptId(conceptId);
	}

	@Override
	public List<Drug> getListOfDrugsByConceptsIds(
			List<Integer> drugConceptsIdsList) {
		return drugorderDAO.getListOfDrugsByConceptsIds(drugConceptsIdsList);
	}
	@Override
	public List<Integer> getPatientOnAllDrugsByConceptIds(
			List<Integer> listDrugIdsByConcept, List<Integer> patientIds) {
		return drugorderDAO.getPatientOnAllDrugsByConceptIds(listDrugIdsByConcept, patientIds);
	}
	@Override
	public Boolean isLastRegimenProphy(Patient p) {
		// TODO Auto-generated method stub
		return drugorderDAO.isLastRegimenProphy(p);
	}
	@Override
	public boolean isPatientOnProphylaxisOnlyBeforePeriod(Integer patientId,Date enddate) {
		return drugorderDAO.isPatientOnProphylaxisOnlyBeforePeriod(patientId,enddate);
	}
	

   

	

}
