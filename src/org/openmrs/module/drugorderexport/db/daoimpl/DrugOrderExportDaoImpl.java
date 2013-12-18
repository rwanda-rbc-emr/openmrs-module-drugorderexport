package org.openmrs.module.drugorderexport.db.daoimpl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.openmrs.Concept;
import org.openmrs.Drug;
import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.module.drugorderexport.DrugOrderExportUtil;
import org.openmrs.module.drugorderexport.db.dao.DrugOrderExportDao;
import org.openmrs.module.drugorderexport.service.DrugOrderService;


import org.openmrs.module.regimenhistory.Regimen;
import org.openmrs.module.regimenhistory.RegimenComponent;
import org.openmrs.module.regimenhistory.RegimenUtils;


public class DrugOrderExportDaoImpl implements DrugOrderExportDao {
	
	/** Logger for this class and subclasses */
	protected final Log log = LogFactory.getLog(getClass());
	
	private SessionFactory sessionFactory;
	
	
	
	//==========================================================================================================================================
	
	/**
	 * @param sessionFactory the sessionFactory to set
	 */
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	//==========================================================================================================================================
	/**
	 * @return the sessionFactory
	 */
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	
	//==========================================================================================================================================
	
	//using global properties to get second line drug ids
	public String gpGetSecondLineQueryPortion() {
		String secondLineDrugIds = DrugOrderExportUtil.getSecondLineDrugIds();
		return secondLineDrugIds;
		
	}
	//==========================================================================================================================================
		public String gpGetSecondLineDrugConceptIds() {
			String secondLineDrugIds = DrugOrderExportUtil.getSecondLineDrugConceptIds();
			return secondLineDrugIds;
			
		}
	//==========================================================================================================================================
	
	public String getQueryPortionForProphylaxis() {
		List<String> gpProphylaxisDrugIds = DrugOrderExportUtil.gpGetProphylaxisAsList();
		String quaryPortion = "";
		for (String id : gpProphylaxisDrugIds) {
			quaryPortion = quaryPortion + " AND o.concept_id <> " + id;
		}
		
		return quaryPortion;
	}
	//==========================================================================================================================================
	
	public String getProphylaxisOrders() {
		List<String> gpProphylaxisDrugIds = DrugOrderExportUtil.gpGetProphylaxisAsList();
		String quaryPortion = "";
		for (String id : gpProphylaxisDrugIds) {
			quaryPortion = quaryPortion + " AND o.concept_id = " + id;
		}
		
		return quaryPortion;
	}
	//==========================================================================================================================================
	
	/**
	 * @see org.openmrs.module.drugorderexport.db.dao.DrugOrderExportDao#checkInputDate(java.util.Date,
	 *      java.util.Date)
	 */
	@Override
	public String checkInputDate(Date startDate, Date endDate) {
		if ((startDate != null) && (endDate != null))
			return "AND (o.start_date >= '" + getDateFormated(startDate) + "' AND o.start_date <= '" + getDateFormated(endDate) + "' )";
		if ((endDate == null) && (startDate != null))
			return "AND (o.start_date >= '" + getDateFormated(startDate) + "' )";
		if ((startDate == null) && (endDate != null))
			return "AND (o.start_date <= '" + getDateFormated(endDate) + "' )";
		if ((startDate == null) && (endDate == null)) {
			endDate = new Date();
			return "AND (o.start_date <= '" + getDateFormated(endDate) + "' )";
		}
		return "";
	}
	
	//==========================================================================================================================================
	
	/**
	 * @see org.openmrs.module.drugorderexport.db.dao.DrugOrderExportDao#getActivePatients(java.util.List,
	 *      java.util.Date)
	 */
	@Override
	public List<Integer> getActivePatients(List<Integer> patients, Date endDate) {
		List<Integer> activePatients = new ArrayList<Integer>();
		
		Session session = getSessionFactory().getCurrentSession();
		
		
		if (endDate == null) {
			endDate = new Date();
		}
//		Date threeMonthsBeforeEndDate = getTreeMonthBefore(endDate);
		
		
//		SQLQuery patsCompletedHIVProgram =  session.createSQLQuery(
//			" SELECT patient_id FROM patient_program where program_id=2 and CAST(date_completed as DATE) is not null "
//			+ " group by patient_id "
//			+" order by CAST(date_completed as DATE) asc ");
//		
//
//		List<Integer> patsCompletedHIVProgramList = patsCompletedHIVProgram.list();

		
		for (Integer patientId : patients) {
			
//			StringBuffer maxEncounterQuery = new StringBuffer();
//			StringBuffer maxReturnVisitQuery = new StringBuffer();
//			
//			maxEncounterQuery.append("select cast(max(encounter_datetime)as DATE) from encounter where ");
//			maxEncounterQuery.append(" (select(cast(max(encounter_datetime)as Date))) <=  ");
//			maxEncounterQuery.append(" '" + getDateFormated(endDate) + "' and patient_id = " + patientId);
//			
//			
//			SQLQuery maxEncounter = session.createSQLQuery(maxEncounterQuery.toString());			
//			
//			Date maxEncounterDateTime = (Date) maxEncounter.uniqueResult();
//			
//			maxReturnVisitQuery.append(" select cast(max(value_datetime) as DATE ) from obs where ");
//			maxReturnVisitQuery.append(" (select(cast(max(value_datetime)as Date))) <=  ");
//			maxReturnVisitQuery.append(" '" + getDateFormated(endDate) + "' ");
//			maxReturnVisitQuery.append(" AND concept_id = 5096 and person_id = " + patientId);
//			
//			SQLQuery maxReturnVisit = session.createSQLQuery(maxReturnVisitQuery.toString());
//			
//			Date maxReturnVisitDay = (Date) maxReturnVisit.uniqueResult();
			
//			if ((maxReturnVisitDay != null) && (maxEncounterDateTime != null)) {
//				
//				if (((maxEncounterDateTime.getTime()) >= threeMonthsBeforeEndDate.getTime() && (maxEncounterDateTime
//				        .getTime()) <= endDate.getTime())
//				        || ((maxReturnVisitDay.getTime()) >= threeMonthsBeforeEndDate.getTime() && (maxReturnVisitDay
//				                .getTime()) <= endDate.getTime())) {

					if (!getPatientsExitedFromCare(endDate).contains(patientId)){
//						if(!isAllDrugsStopped(Context.getPatientService().getPatient(patientId)))
//							if(!isPatientOnProphylaxisOnlyBeforePeriod(patientId,endDate))
//								if(!patsCompletedHIVProgramList.contains(patientId))
						activePatients.add(patientId);
					
				}
//			}

//			else if (maxReturnVisitDay == null && maxEncounterDateTime != null) {
//				if (maxEncounterDateTime.getTime() >= threeMonthsBeforeEndDate.getTime() && maxEncounterDateTime.getTime()<= endDate.getTime()) {
//					if (!getPatientsExitedFromCare().contains(patientId))
//						if(!isAllDrugsStopped(Context.getPatientService().getPatient(patientId)))
//							if(!isPatientOnProphylaxisOnly(patientId))
////								if(!patsCompletedHIVProgramList.contains(patientId))
//						activePatients.add(patientId);
//					
//				}
//			} else if (maxReturnVisitDay != null && maxReturnVisitDay.getTime() > endDate.getTime()) {
//				if (!getPatientsExitedFromCare().contains(patientId))
//					if(!isAllDrugsStopped(Context.getPatientService().getPatient(patientId)))
//						if(!isPatientOnProphylaxisOnly(patientId))
////							if(!patsCompletedHIVProgramList.contains(patientId))
//					activePatients.add(patientId);
//				
//			}
			
		}
		return activePatients;
	}
	
	//==========================================================================================================================================
	
	/**
	 * @see org.openmrs.module.drugorderexport.db.dao.DrugOrderExportDao#getAllPatientObs(org.openmrs.Patient,
	 *      org.openmrs.Concept)
	 */
	@Override
	public String getAllPatientObs(Patient p, Concept c) {
		
		SQLQuery query = null;
		
		SortedMap<Date, Double> cd4CountAndDateSorted = new TreeMap<Date, Double>();
		
		Date minDate = getWhenPatientStarted(p);
		
		String values = new String();
		
		if (c != null && p != null)
			query = sessionFactory.getCurrentSession().createSQLQuery(
			    "(select obs_datetime,value_numeric from obs where  person_id= " + p.getPatientId() + " and concept_id= "
			            + c.getConceptId() + ")ORDER BY obs_datetime asc");
		
		List<Object[]> obj = query.list();
		
		for (Object[] ob : obj) {
			Date date = (Date) ob[0];
			
			Double conceptValue = (Double) ob[1];
			cd4CountAndDateSorted.put(date, conceptValue);
		}
		cd4CountAndDateSorted = getSubMap(cd4CountAndDateSorted, minDate);
		for (Date d : cd4CountAndDateSorted.keySet()) {
			values += cd4CountAndDateSorted.get(d) + "," + getDateFormated(d) + ",";
		}
		
		return values;
	}
	
	//==========================================================================================================================================
	
	public SortedMap<Date, Double> getSubMap(SortedMap<Date, Double> map, Date dateToSearch) {
		
		SortedMap<Date, Double> subMap = new TreeMap<Date, Double>();
		
		//		try {
		Iterator<Date> it = map.keySet().iterator();
		List<Date> dates = new ArrayList<Date>();
		
		while (it.hasNext()) {
			dates.add((Date) it.next());
			
		}
		int index = 0;
		for (int i = 0; i < dates.size(); i++) {
			index = i;
			if (dates != null && dateToSearch != null)
				if (dates.get(i).getTime() == dateToSearch.getTime() || dates.get(i).getTime() > dateToSearch.getTime()) {
					break;
				}
		}
		if (dates.size() != 0 && dates != null) {
			// sub map starts with the first concept value(ex:cd4count) 
			//up to the last value (or last cd4 cout) i.e the cd4 count he has now
			
			// submap(firstkey,laskey) 
			if (index != 0)
				subMap = map.subMap(dates.get(index - 1), new Date());
			else if (index == 0) {
				subMap = map.subMap(dates.get(index), new Date());
			}
		}
		
		//        }
		//        catch (Exception e) {
		//	       log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>> error generated "+e.getMessage());
		//        }
		
		return subMap;
		
	}
	
	//==========================================================================================================================================
	
	/**
	 * helps to know if all drugs the patient was taking have been stopped
	 */
	public Boolean isAllDrugsStopped(Patient patient) {		
		
		Boolean isStopped = false;
		SQLQuery query = null;
		
		StringBuffer strb = new StringBuffer();
		
//		try {
			strb.append("SELECT DISTINCT o.patient_id , ");
			strb.append("IF((select o.discontinued='1') , 1 , 0)");
			strb.append("FROM drug_order d ");
			strb.append("INNER JOIN orders o on o.order_id=d.order_id ");
//			strb.append("INNER JOIN drug g on g.drug_id=d.drug_inventory_id ");
			strb.append(" AND o.patient_id= "+patient.getPatientId());
			strb.append(" AND o.concept_id IN ("+DrugOrderExportUtil.gpGetARVConceptIds()+")");

			List<Integer> values = new ArrayList<Integer>();
			
			query = sessionFactory.getCurrentSession().createSQLQuery(strb.toString());
			
			List<Object[]> records = query.list();
			
			
			for (Object[] obj : records) {
				String booleanValue = obj[1].toString() ;
				
				values.add(Integer.parseInt(booleanValue));
			}
			
					
			if(!values.contains(0)){
				isStopped=true;
			}
				
//        }
//        catch (Exception e) {
//	        // TODO: handle exception
//        }
		
		
		return isStopped;
		
	}
	
	//==========================================================================================================================================
	
	/**
	 * @see org.openmrs.module.drugorderexport.db.dao.DrugOrderExportDao#getAllPatientObsList(org.openmrs.Patient,
	 *      org.openmrs.Concept)
	 */
	@Override
	public List<String> getAllPatientObsList(Patient p, Concept c) {
		
		SQLQuery query = null;
		
		SortedMap<Date, Double> cd4CountAndDateSorted = new TreeMap<Date, Double>();
		
		Date whenPatientHasStarted = getWhenPatientStarted(p);
		
		if (c != null && p != null) {
			query = sessionFactory.getCurrentSession().createSQLQuery(
			    "(select obs_datetime,value_numeric from obs where  person_id= " + p.getPatientId() + " and concept_id= "
			            + c.getConceptId() + " )ORDER BY obs_datetime asc");
		}
		List<Object[]> obj = query.list();
		
		List<String> conceptValueAndDate = new ArrayList<String>();
		
		for (Object[] ob : obj) {
			Date date = (Date) ob[0];
			
			Double conceptValue = (Double) ob[1];
			
			cd4CountAndDateSorted.put(date, conceptValue);
			
			conceptValueAndDate.add(cd4CountAndDateSorted.get(date) + "(" + getDateFormated(date) + ")");
		}
		
		cd4CountAndDateSorted = getSubMap(cd4CountAndDateSorted, whenPatientHasStarted);
		return conceptValueAndDate;
	}
	
	//==========================================================================================================================================
	
	/**
	 * @see org.openmrs.module.drugorderexport.db.dao.DrugOrderExportDao#getAllProphylaxisPatients(java.util.Date,
	 *      java.util.Date, java.lang.String, java.util.Date, java.util.Date, java.util.Date,
	 *      java.util.Date)
	 */
	@Override
	public List<Integer> getAllProphylaxisPatients(Date startDate, Date endDate, String gender, Date minAge, Date maxAge,
	                                               Date minBirthdate, Date maxBirthdate) {
		SQLQuery query = null;
		
		StringBuffer b = new StringBuffer();
		
		if (endDate == null) {
			endDate = new Date();
		}
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		StringBuffer portion = new StringBuffer();
		portion.append(" select distinct pa.patient_id from patient_program pg  ");
		portion.append(" inner join person p on pg.patient_id = p.person_id ");
		portion.append(" inner join patient pa on pg.patient_id = pa.patient_id ");
		portion.append(" inner join orders o on pg.patient_id = o.patient_id  ");
		portion.append(" where ((pg.date_completed is null) or(cast(pg.date_completed as DATE)> ' " + df.format(endDate) + " ')) ");
		portion.append(" and o.concept_id IN ("+DrugOrderExportUtil.getProphylaxisDrugConceptIdsStr()+") " );
//		portion.append(" AND (o.concept_id IN("+DrugOrderExportUtil.gpGetARVConceptIds()+ ") OR o.concept_id NOT IN("+DrugOrderExportUtil.gpGetARVConceptIds()+"))");
		portion.append(" and (cast(o.start_date as DATE)) <= '");
		portion.append(getDateFormated(endDate) + "' ");
		portion.append(" and pg.program_id= 2 and pg.voided = 0 and p.voided = 0 and o.voided = 0 ");
		portion.append(" and pg.date_enrolled <= '" + df.format(endDate) +"'");
		portion.append(checkInputDate(startDate, endDate));
		
		
		if (gender.equals("") && minAge == null && maxAge == null && minBirthdate == null
		        && maxBirthdate == null) {
			b.append(portion);

		} else {
			b.append(portion);
			b.append(" AND " + patientAttributeStr(gender, minAge, maxAge, minBirthdate, maxBirthdate));
		}
		query = sessionFactory.getCurrentSession().createSQLQuery(b.toString());
		
		List<Integer> patientIds1 = query.list();
		
		List<Integer> result = new ArrayList<Integer>();
		List<Integer> patientIds2 = new ArrayList<Integer>();
//		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		for (Integer patientId : patientIds1) {
//			SQLQuery query2 = this.sessionFactory
//			        .getCurrentSession()
//			        .createSQLQuery(
//			            "select distinct pa.patient_id from patient pa " +
//			            "inner join patient_program pg on pg.patient_id = pa.patient_id " +
//			            "inner join person pe on pa.patient_id = pe.person_id " +
//			            "inner join orders o on pa.patient_id = o.patient_id " +
////			            "inner join drug_order dor on o.order_id = dor.order_id " +
//			            " and o.concept_id IN ("+ DrugOrderExportUtil.gpGetARVConceptIds()
//			                    + ") and pg.program_id= 2 "
//			                    + " and pa.patient_id = " + patientId);
			SQLQuery query2 = sessionFactory.getCurrentSession().createSQLQuery("select distinct pg.patient_id from patient_program pg "
					+ "inner join person pe on pg.patient_id = pe.person_id "
					+ "inner join patient pa on pg.patient_id = pa.patient_id "
					+ "inner join orders ord on pg.patient_id = ord.patient_id "
					+ "where ord.concept_id IN ("
					+ DrugOrderExportUtil.gpGetARVConceptIds()
					+ ") and pg.voided = 0 and pe.voided = 0 and ord.voided = 0 "
					+ "and pa.voided = 0 and (cast(ord.start_date as DATE)) <= '"
					+ df.format(endDate)
					+ "'"
					+ " and pg.patient_id="
					+ patientId);
			
			log.info("gggggggggggggggggggggggggg "+query2.toString());
			patientIds2 = query2.list();
			
	
			
			if (patientIds2.size() == 0) {
				result.add(patientId);
			}
		}
		
//		for (Integer patientId : patientIds1) {	
////		if (isPatientOnProphylaxisOnly(patientId)) {
//			result.add(patientId);
////		}
//	    }
//		log.info("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaresult "+result.size());
		return result;
	}
	
	//==========================================================================================================================================
	/**
	 * @see org.openmrs.module.drugorderexport.db.dao.DrugOrderExportDao#getConceptById(int)
	 */
	@Override
	public Concept getConceptById(int conceptId) {
		Session session = getSessionFactory().getCurrentSession();
		return (Concept) session.load(Concept.class, conceptId);
	}
	
	//==========================================================================================================================================
	/**
	 * @see org.openmrs.module.drugorderexport.db.dao.DrugOrderExportDao#getDrugById(int)
	 */
	@Override
	public Drug getDrugById(int drugId) {
		Session session = getSessionFactory().getCurrentSession();
		return (Drug) session.load(Drug.class, drugId);
	}
	
	//==========================================================================================================================================
	/**
	 * @see org.openmrs.module.drugorderexport.db.dao.DrugOrderExportDao#getListOfDrugsByIds(java.util.List)
	 */
	@Override
	public List<Drug> getListOfDrugsByIds(List<Integer> drugIdsList) { 
		List<Drug> drugList = new ArrayList<Drug>();
		for (Integer drugId : drugIdsList) {
			drugList.add(getDrugById(drugId));
		}
		return drugList;
	}
	
	//==========================================================================================================================================
	
	/**
	 * @see org.openmrs.module.drugorderexport.db.dao.DrugOrderExportDao#getMinStartDate(java.util.List)
	 */
	@Override
	public Date getMinStartDate(List<Date> startDateList) {
		
		Date minStartDate = null;
		for (int i = 0; i < startDateList.size(); i++) {
			minStartDate = startDateList.get(0);
			if (startDateList.get(i).before(minStartDate)) {
				minStartDate = startDateList.get(i);
			}
		}
		return minStartDate;
	}
	
	//==========================================================================================================================================
	
	/**
	 * @see org.openmrs.module.drugorderexport.db.dao.DrugOrderExportDao#getNewPatientsOnProphylaxis(java.util.Date,
	 *      java.util.Date, java.lang.String, java.util.Date, java.util.Date, java.util.Date,
	 *      java.util.Date)
	 */
	@Override
	public List<Integer> getNewPatientsOnProphylaxis(Date startDate, Date endDate, String gender, Date minAge, Date maxAge,
	                                                 Date minBirthdate, Date maxBirthdate) {
		Map<String, Integer> patientStartedWhenMap = new HashMap<String, Integer>();
		StringBuffer strBuffer = new StringBuffer();
		
		SQLQuery query = null;
		
		StringBuffer portionBuffer = new StringBuffer();
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		
		portionBuffer.append(" FROM orders o  ");
		portionBuffer.append(" INNER JOIN patient pa on pa.patient_id=o.patient_id  ");
		portionBuffer
		        .append(" INNER JOIN patient_program pg on pg.patient_id=pa.patient_id AND pg.program_id=2   ");
		portionBuffer.append(" INNER JOIN drug_order dro on dro.order_id=o.order_id ");
		portionBuffer.append(" AND o.concept_id IN (" + DrugOrderExportUtil.getProphylaxisDrugConceptIdsStr() + ") ");
		portionBuffer.append(" and o.discontinued=0 and pg.voided = 0 and o.voided = 0 and pg.date_enrolled <= '" + df.format(endDate) + "'"); 
//		portionBuffer.append(" AND (o.concept_id = 916 or o.concept_id = 747 or o.concept_id = 92 ) ");
//		portionBuffer.append(" AND o.voided=0 ");
		
		StringBuffer attribStr = new StringBuffer();
		
		if (!gender.equals("") || minAge != null || maxAge != null || minBirthdate != null || maxBirthdate != null) {
			attribStr.append(" INNER JOIN person p on o.patient_id = p.person_id AND ");
			attribStr.append(patientAttributeStr(gender, minAge, maxAge, minBirthdate, maxBirthdate));
			attribStr.append(" group by o.patient_id ");
		}
		
		if ((!(gender.equals(""))) || (minAge != null) || (maxAge != null) || (minBirthdate != null)    || (maxBirthdate != null)) {
			if (startDate != null && endDate == null) {
				
				endDate = new Date();
				
				strBuffer.append("SELECT DISTINCT o.patient_id , ");
				strBuffer.append("IF((select min(o.start_date)) >= ");
				strBuffer.append("'" + getDateFormated(startDate) + "'" + " , " + " true " + " ," + "false) ");
				strBuffer.append(portionBuffer);
				strBuffer.append(attribStr);

			}
			if ((startDate == null) && (endDate != null)) {
				strBuffer.append("SELECT DISTINCT o.patient_id , ");
				strBuffer.append("IF((select min(o.start_date)) <= ");
				strBuffer.append("'" + getDateFormated(endDate) + "'" + " , " + " true" + " ," + "false) ");
				strBuffer.append(portionBuffer);
				strBuffer.append(attribStr);

			}
			
			if ((startDate != null) && (endDate != null)) {
				strBuffer.append("SELECT DISTINCT o.patient_id , ");
				strBuffer.append("IF((select min(o.start_date)) >= ");
				strBuffer.append("'" + getDateFormated(startDate) + "'" + " AND  (select min(o.start_date)) <= '" + getDateFormated(endDate) + "'"
				        + " , " + " true" + " , " + " false " + ") ");
				strBuffer.append(portionBuffer);
				strBuffer.append(attribStr);

			}
			if ((startDate == null) && (endDate == null)) {
				endDate = new Date();
				
				strBuffer.append("SELECT DISTINCT o.patient_id , ");
				strBuffer.append("IF((select min(o.start_date)) <= ");
				strBuffer.append("'" + getDateFormated(endDate) + "'" + " , " + " true" + " ," + "false) ");
				strBuffer.append(portionBuffer);
				strBuffer.append(attribStr);

			}
		} else {
			if ((startDate != null) && (endDate == null)) {
				
				endDate = new Date();
				
				strBuffer.append("SELECT DISTINCT o.patient_id , ");
				strBuffer.append("IF((select min(o.start_date)) >= ");
				strBuffer.append("'" + getDateFormated(startDate) + "'" + " , " + " true" + " ," + " false) ");
				strBuffer.append(portionBuffer);
				strBuffer.append(" group by o.patient_id");
	
			}
			if ((startDate == null) && (endDate != null)) {
				strBuffer.append("SELECT DISTINCT o.patient_id , ");
				strBuffer.append("IF((select min(o.start_date)) <= ");
				strBuffer.append("'" + getDateFormated(endDate) + "'" + " , " + " true" + " ," + " false)");
				strBuffer.append(portionBuffer);
				strBuffer.append(" group by o.patient_id");

			}
			if ((startDate != null) && (endDate != null)) {
				strBuffer.append("SELECT DISTINCT o.patient_id , ");
				strBuffer.append("IF((select min(o.start_date)) >= ");
				strBuffer.append("'" + getDateFormated(startDate) + "'" + " AND (select min(o.start_date))<='" + getDateFormated(endDate) + "'"
				        + " ," + "true" + " ," + "false)");
				strBuffer.append(portionBuffer);
				strBuffer.append("group by o.patient_id");

			}
			
			if (startDate == null && (endDate == null)) {
				endDate = new Date();
				strBuffer.append("SELECT DISTINCT o.patient_id , ");
				strBuffer.append("IF((select min(o.start_date)) <= ");
				strBuffer.append("'" + getDateFormated(endDate) + "'" + " , " + " true" + " ," + " false)");
				strBuffer.append(portionBuffer);
				strBuffer.append(" group by o.patient_id");
				
			}
		}
		
//		log.info("qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq "+strBuffer.toString());
		query = sessionFactory.getCurrentSession().createSQLQuery(strBuffer.toString());
		
		List<Object[]> records = query.list();
		
		int add = 0;
		Integer id;
		for (Object[] obj : records) {
			++add;
			
			id = (Integer) obj[0];
			String booleanValue = obj[1].toString() + add;
			
			patientStartedWhenMap.put(booleanValue, id);
		}
		List<Integer> patientIds = new ArrayList<Integer>();

		for (String key : patientStartedWhenMap.keySet()) {
			if (key.charAt(0) == '1') {
				Patient patient = Context.getPatientService().getPatient(patientStartedWhenMap.get(key));
//				if (!patient.getVoided())
					patientIds.add(patient.getPatientId());
			}
		}
		List<Integer> newOnProphylaxisList=new ArrayList<Integer>();
		for (Integer patientId : patientIds) {
			
			SQLQuery query2 = sessionFactory.getCurrentSession().createSQLQuery(
					"select distinct pg.patient_id from patient_program pg "
			                + "inner join person pe on pg.patient_id = pe.person_id "
			                + "inner join patient pa on pg.patient_id = pa.patient_id "
			                + "inner join orders ord on pg.patient_id = ord.patient_id "
			                + " and ord.concept_id IN ("
			                + DrugOrderExportUtil.gpGetARVConceptIds() + ") "
			                + "and ord.discontinued=0 and pg.voided = 0 and pe.voided = 0 and ord.voided = 0 "
			                + "and pa.voided = 0 and (cast(ord.start_date as DATE)) >= '" + df.format(startDate)
			                + "' and (cast(ord.start_date as DATE)) <= '" + df.format(endDate) + "' and pg.program_id=2 "
			                + " and pg.date_enrolled <= '" + df.format(endDate) + "' and pg.patient_id="
			                + patientId);
			
			List<Integer> patientIds2 = query2.list();
			
			if (patientIds2.size() == 0)
				newOnProphylaxisList.add(patientId);
		}
		return newOnProphylaxisList;
	}
	
	//==========================================================================================================================================
	
	/**
	 * @see org.openmrs.module.drugorderexport.db.dao.DrugOrderExportDao#getPatientBasedOnAttributes(java.lang.String,
	 *      java.util.Date, java.util.Date, java.util.Date, java.util.Date)
	 */
	@Override
	public List<Integer> getPatientBasedOnAttributes(String gender, Date minAge, Date maxAge, Date minBirthdate,
	                                                 Date maxBirthdate) {
		SQLQuery patientWithAttributeQuery = null;
		List<Integer> patientWithAttributeList = new ArrayList<Integer>();
		patientWithAttributeQuery = sessionFactory.getCurrentSession().createSQLQuery(
		    "select  DISTINCT p.patient_id from patient p WHERE p.patient_id IN(select p.person_id from person p WHERE "
		            + patientAttributeStr(gender, minAge, maxAge, minBirthdate, maxBirthdate) + ")");
		
		patientWithAttributeList = patientWithAttributeQuery.list();
		return patientWithAttributeList;
		
	}
	
	//==========================================================================================================================================
	
	/**
	 * @see org.openmrs.module.drugorderexport.db.dao.DrugOrderExportDao#getPatientObsValue(org.openmrs.Patient,
	 *      org.openmrs.Concept, java.util.Date, java.util.Date)
	 */
	@Override
	public Double getPatientObsValue(Patient patient, Concept concept, Date obsStartDate, Date obsEndDate) {
		Session session = sessionFactory.getCurrentSession();
		
		// if patient has been tested in the period:
		// search all dates when he has been tested
		// search max among those
		// and finally search the corresponding value
		
		SQLQuery obsDateMaxQuery = session.createSQLQuery("SELECT DISTINCT max(ob.obs_datetime) FROM obs ob WHERE "
		        + "ob.concept_id = " + concept.getConceptId() + " AND ob.voided = 0 "
		        + checkObsDate(obsStartDate, obsEndDate) + " AND person_id = " + patient.getPatientId());
		
		List<Date> maxObsDates = obsDateMaxQuery.list();
		
		Date obsMaxDate = null;
		Double obsNumericValue = null;
		
		if (maxObsDates.size() != 0 && maxObsDates != null)
			obsMaxDate = maxObsDates.get(0);
		
		SQLQuery obsMaxDateValue = null;
		if (obsMaxDate != null) {
			obsMaxDateValue = session.createSQLQuery("select value_numeric from obs where obs_datetime= " + "'"
			        + getDateFormated(obsMaxDate) + "'" + " and person_id= " + patient.getPatientId() + " and concept_id = "
			        + concept.getConceptId());
			
		}
		
		List<Double> obsValuesAtMaxDate = null;
		if (obsMaxDateValue != null) {
			obsValuesAtMaxDate = obsMaxDateValue.list();
			if (obsValuesAtMaxDate.size() != 0)
				obsNumericValue = obsValuesAtMaxDate.get(0);
		}
		
		if (obsValuesAtMaxDate != null && obsValuesAtMaxDate.size() != 0)
			obsNumericValue = obsValuesAtMaxDate.get(0);
		///////////////////////////////////else retain the previous value////////////////////
		
		else if (obsValuesAtMaxDate == null) {
			SQLQuery queryWhenValueIsNullInThePeriod = session
			        .createSQLQuery("SELECT DISTINCT MAX(ob.obs_datetime) FROM obs ob WHERE ob.concept_id = "
			                + concept.getConceptId() + " AND ob.voided = 0 " + " AND ob.obs_datetime <= " + "'"
			                + getDateFormated(obsStartDate) + "'" + " AND  person_id = " + patient.getPatientId());
			
			List<Date> obsDates1 = queryWhenValueIsNullInThePeriod.list();
			
			Date obsDate1 = obsDates1.get(0);
			
			SQLQuery obsMaxDateValue1Q = null;
			
			List<Double> obsMaxDateValues1 = null;
			
			if (obsDate1 != null) {
				obsMaxDateValue1Q = session.createSQLQuery("select value_numeric from obs where obs_datetime = " + "'"
				        + getDateFormated(obsDate1) + "'" + " and person_id= " + patient.getPatientId()
				        + " and concept_id = " + concept.getConceptId());
				
				obsMaxDateValues1 = obsMaxDateValue1Q.list();
				
				if (obsMaxDateValues1 != null && obsMaxDateValues1.size() != 0)
					obsNumericValue = obsMaxDateValues1.get(0);
			}
		}
		
		return obsNumericValue;
		
	}
	
	//==========================================================================================================================================
	
	/**
	 * helps to check when the observation occurred
	 * 
	 * @param startDate
	 * @param endDate
	 * @return String
	 */
	public String checkObsDate(Date startDate, Date endDate) {
		if (startDate != null && endDate != null) {
			return " AND (ob.obs_datetime >= '" + getDateFormated(startDate) + "' AND ob.obs_datetime<='" + getDateFormated(endDate)
			        + "' )";
		} else if (endDate == null && startDate != null) {
			return " AND (ob.obs_datetime >= '" + getDateFormated(startDate) + "' )";
		} else if (startDate == null && endDate != null) {
			return " AND (ob.obs_datetime <= '" + getDateFormated(endDate) + "' )";
		} else if (startDate == null && endDate == null) {
			endDate = new Date();
			return " AND (ob.obs_datetime <= '" + getDateFormated(endDate) + "' )";
		}
		return "";
	}
	
	//==========================================================================================================================================
	
	/**
	 * @see org.openmrs.module.drugorderexport.db.dao.DrugOrderExportDao#getPatientWhoStartedOnDate(java.util.Date,
	 *      java.util.Date, java.lang.String, java.util.Date, java.util.Date, java.util.Date,
	 *      java.util.Date)
	 */
	@Override
	public List<Integer> getPatientWhoStartedOnDate(Date startDate, Date endDate, String gender, Date minAge, Date maxAge,
	                                                Date minBirthdate, Date maxBirthdate) {
		
		Map<String, Integer> patientStartedWhenMap = new HashMap<String, Integer>();
		StringBuffer strBuffer = new StringBuffer();
		
		SQLQuery query = null;
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		

		if (!gender.equals("") || minAge != null || maxAge != null || minBirthdate != null || maxBirthdate != null) {
			if (startDate != null && endDate == null) {
				endDate=new Date();
				strBuffer.append("SELECT DISTINCT o.patient_id , ");
				strBuffer.append("IF((select min(o.start_date)) <= ");
				strBuffer.append("'" + getDateFormated(startDate) + "'" + " , " + " true " + " ," + "false)");
				strBuffer.append(" FROM orders o  ");
				strBuffer.append(" INNER JOIN patient pa on pa.patient_id=o.patient_id ");
				strBuffer.append(" INNER JOIN patient_program pg on pg.patient_id=pa.patient_id  ");
				strBuffer.append(" INNER JOIN program pr on pr.program_id=pg.program_id AND pr.program_id = 2 ");
				strBuffer.append(" INNER JOIN drug_order dro on dro.order_id=o.order_id ");
//				strBuffer.append(" INNER JOIN drug drug on drug.drug_id=dro.drug_inventory_id  ");
				strBuffer.append(" AND o.concept_id in ("+DrugOrderExportUtil.gpGetARVConceptIds()+") ");
//				strBuffer.append(getQueryPortionForProphylaxis());
				strBuffer.append(" AND o.voided=0 AND pa.voided=0 AND pg.voided=0 and pg.date_enrolled <= '" + df.format(endDate) + "'  ");
				strBuffer.append(" INNER JOIN person p on o.patient_id = p.person_id AND  ");
				strBuffer.append(patientAttributeStr(gender, minAge, maxAge, minBirthdate, maxBirthdate));
				strBuffer.append(" group by o.patient_id ");
				
//				 query = sessionFactory.getCurrentSession().createSQLQuery("select distinct pg.patient_id from patient_program pg "
//				        + "inner join person p on pg.patient_id = p.person_id "
//				        + "inner join patient pa on pg.patient_id = pa.patient_id "
//				        + "inner join orders ord on pg.patient_id = ord.patient_id "		
//				        + "where ((pg.date_completed is null) or(cast(pg.date_completed as DATE)> ' " + df.format(endDate)  + " ')) "
//				        + " and ord.concept_id IN (" + DrugOrderExportUtil.gpGetARVConceptIds() + ") "
//				        + " and pg.voided = 0 and p.voided = 0 and ord.voided = 0 "
//				        + "and pa.voided = 0 and (cast(ord.start_date as DATE)) >= '" + df.format(startDate)
//				        + "' and (cast(ord.start_date as DATE)) <= '" + df.format(endDate) + "' and pg.program_id= 2" 
//				        + " and pg.date_enrolled <= '" + df.format(endDate) + "' "
//				        + patientAttributeStr(gender, minAge, maxAge, minBirthdate, maxBirthdate)
//				        ); 
//				
				query = sessionFactory.getCurrentSession().createSQLQuery(strBuffer.toString());
			}
			if (startDate == null && endDate != null) {
				strBuffer.append("SELECT DISTINCT o.patient_id , ");
				strBuffer.append("IF((select min(o.start_date)) <= ");
				strBuffer.append("'" + getDateFormated(endDate) + "'" + " , " + " true" + " ," + "false)");
				strBuffer.append(" FROM orders o ");
				strBuffer.append(" INNER JOIN patient pa on pa.patient_id=o.patient_id ");
				strBuffer
				        .append(" INNER JOIN patient_program pg on pg.patient_id=pa.patient_id  ");
				strBuffer.append(" INNER JOIN program pr on pr.program_id=pg.program_id AND pr.program_id = 2 ");
				strBuffer.append(" INNER JOIN drug_order dro on dro.order_id=o.order_id ");
//				strBuffer.append(getQueryPortionForProphylaxis());
//				strBuffer.append(" INNER JOIN drug drug on drug.drug_id=dro.drug_inventory_id ");
				strBuffer.append(" AND o.concept_id in ("+DrugOrderExportUtil.gpGetARVConceptIds()+") ");
				strBuffer.append(" AND o.voided=0 AND pa.voided=0 AND pg.voided=0  and pg.date_enrolled <= '" + df.format(endDate) + "'");
				strBuffer.append(" INNER JOIN person p on o.patient_id = p.person_id AND  ");
				strBuffer.append(patientAttributeStr(gender, minAge, maxAge, minBirthdate, maxBirthdate));
				strBuffer.append(" group by o.patient_id ");
				
				query = sessionFactory.getCurrentSession().createSQLQuery(strBuffer.toString());
				
//				 query = sessionFactory.getCurrentSession().createSQLQuery("select distinct pg.patient_id from patient_program pg "
//					        + "inner join person p on pg.patient_id = p.person_id "
//					        + "inner join patient pa on pg.patient_id = pa.patient_id "
//					        + "inner join orders ord on pg.patient_id = ord.patient_id "		
//					        + "where ((pg.date_completed is null) or(cast(pg.date_completed as DATE)> ' " + df.format(endDate)  + " ')) "
//					        + " and ord.concept_id IN (" + DrugOrderExportUtil.gpGetARVConceptIds() + ") "
//					        + " and pg.voided = 0 and p.voided = 0 and ord.voided = 0 "
//					        + "and pa.voided = 0 and (cast(ord.start_date as DATE)) <= '" + df.format(endDate)
//					        + "' and (cast(ord.start_date as DATE)) <= '" + df.format(endDate) + "' and pg.program_id= 2" 
//					        + " and pg.date_enrolled <= '" + df.format(endDate) + "' "
//					        + " AND "+patientAttributeStr(gender, minAge, maxAge, minBirthdate, maxBirthdate)
//					        );
				
			}
			if (startDate != null && endDate != null) {
				strBuffer.append("SELECT DISTINCT o.patient_id , ");
				strBuffer.append("IF((select min(o.start_date)) >= ");
				strBuffer.append("'" + getDateFormated(startDate) + "'" + " AND (select min(o.start_date)) <='" + getDateFormated(endDate) + "'"
				        + " , " + " true" + " , " + " false " + ")");
				strBuffer.append(" FROM orders o ");
				strBuffer.append(" INNER JOIN patient pa on pa.patient_id=o.patient_id ");
				strBuffer.append(" INNER JOIN patient_program pg on pg.patient_id=pa.patient_id ");
				strBuffer.append(" INNER JOIN program pr on pr.program_id=pg.program_id AND pr.program_id = 2 ");
				strBuffer.append(" INNER JOIN drug_order dro on dro.order_id=o.order_id ");
//				strBuffer.append(getQueryPortionForProphylaxis());
//				strBuffer.append(" INNER JOIN drug drug on drug.drug_id=dro.drug_inventory_id ");
				strBuffer.append(" AND o.concept_id in ("+DrugOrderExportUtil.gpGetARVConceptIds()+") ");
				strBuffer.append(" AND o.voided=0 AND pa.voided=0 AND pg.voided=0  and pg.date_enrolled <= '" + df.format(endDate) + "' ");
				strBuffer.append(" INNER JOIN person p on o.patient_id = p.person_id AND  ");
				strBuffer.append(patientAttributeStr(gender, minAge, maxAge, minBirthdate, maxBirthdate));
				strBuffer.append(" group by o.patient_id ");
				
				query = sessionFactory.getCurrentSession().createSQLQuery(strBuffer.toString());
//			
//			 query = sessionFactory.getCurrentSession().createSQLQuery("select distinct pg.patient_id from patient_program pg "
//				        + "inner join person p on pg.patient_id = p.person_id "
//				        + "inner join patient pa on pg.patient_id = pa.patient_id "
//				        + "inner join orders ord on pg.patient_id = ord.patient_id "		
//				        + "where ((pg.date_completed is null) or(cast(pg.date_completed as DATE)> ' " + df.format(endDate)  + " ')) "
//				        + " and ord.concept_id IN (" + DrugOrderExportUtil.gpGetARVConceptIds() + ") "
//				        + " and pg.voided = 0 and p.voided = 0 and ord.voided = 0 "
//				        + "and pa.voided = 0 and (cast(ord.start_date as DATE)) BETWEEN '" + df.format(startDate)+"' AND '"+df.format(endDate)+"'"
//				        + " and (cast(ord.start_date as DATE)) <= '" + df.format(endDate) + "' and pg.program_id= 2" 
//				        + " and pg.date_enrolled <= '" + df.format(endDate) + "' "
//				        + patientAttributeStr(gender, minAge, maxAge, minBirthdate, maxBirthdate)
//				        );
			}
			if (startDate == null && endDate == null) {
				endDate=new Date();
				strBuffer.append("SELECT DISTINCT o.patient_id , ");
				strBuffer.append("IF((select min(o.start_date)) <= ");
				strBuffer.append("'" + getDateFormated(endDate) + "'" + " , " + " true" + " ," + "false)");
				strBuffer.append(" FROM orders o ");
				strBuffer.append(" INNER JOIN patient pa on pa.patient_id=o.patient_id ");
				strBuffer
				        .append(" INNER JOIN patient_program pg on pg.patient_id=pa.patient_id ");
				strBuffer.append(" INNER JOIN program pr on pr.program_id=pg.program_id AND pr.program_id = 2 ");
				strBuffer.append(" INNER JOIN drug_order dro on dro.order_id=o.order_id ");
//				strBuffer.append(getQueryPortionForProphylaxis());
//				strBuffer.append(" INNER JOIN drug drug on drug.drug_id=dro.drug_inventory_id ");
				strBuffer.append(" AND o.concept_id in ("+DrugOrderExportUtil.gpGetARVConceptIds()+") ");
				strBuffer.append(" AND o.voided=0 AND pa.voided=0 AND pg.voided=0 and pg.date_enrolled <= '" + df.format(endDate) + "' ");
				strBuffer.append(" INNER JOIN person p on o.patient_id = p.person_id AND  ");
				strBuffer.append(patientAttributeStr(gender, minAge, maxAge, minBirthdate, maxBirthdate));
				strBuffer.append(" group by o.patient_id ");
				
				query = sessionFactory.getCurrentSession().createSQLQuery(strBuffer.toString());
				
//				 query = sessionFactory.getCurrentSession().createSQLQuery("select distinct pg.patient_id from patient_program pg "
//					        + "inner join person p on pg.patient_id = p.person_id "
//					        + "inner join patient pa on pg.patient_id = pa.patient_id "
//					        + "inner join orders ord on pg.patient_id = ord.patient_id "		
//					        + "where ((pg.date_completed is null) or(cast(pg.date_completed as DATE)> ' " + df.format(endDate)  + " ')) "
//					        + " and ord.concept_id IN (" + DrugOrderExportUtil.gpGetARVConceptIds() + ") "
//					        + " and pg.voided = 0 and p.voided = 0 and ord.voided = 0 "
//					        + "and pa.voided = 0 and (cast(ord.start_date as DATE)) <= '" + df.format(endDate)
//					        + "' and (cast(ord.start_date as DATE)) <= '" + df.format(endDate) + "' and pg.program_id= 2" 
//					        + " and pg.date_enrolled <= '" + df.format(endDate) + "' "
//					        + patientAttributeStr(gender, minAge, maxAge, minBirthdate, maxBirthdate)
//					        );
			}
		} else {
			if (startDate != null && endDate == null) {
				endDate=new Date();
				strBuffer.append("SELECT DISTINCT o.patient_id , ");
				strBuffer.append("IF((select min(o.start_date)) <= ");
				strBuffer.append("'" + getDateFormated(startDate) + "'" + " , " + " true" + " ," + " false)");
				strBuffer.append(" FROM orders o ");
				strBuffer.append(" INNER JOIN patient pa on pa.patient_id=o.patient_id ");
				strBuffer
				        .append(" INNER JOIN patient_program pg on pg.patient_id=pa.patient_id ");
				strBuffer.append(" INNER JOIN program pr on pr.program_id=pg.program_id AND pr.program_id = 2 ");
				strBuffer.append(" INNER JOIN drug_order dro on dro.order_id=o.order_id ");
//				strBuffer.append(getQueryPortionForProphylaxis());
//				strBuffer.append(" INNER JOIN drug drug on drug.drug_id=dro.drug_inventory_id ");
				strBuffer.append(" AND o.concept_id in ("+DrugOrderExportUtil.gpGetARVConceptIds()+") ");
				strBuffer.append(" AND o.voided=0 AND pa.voided=0 AND pg.voided=0 and pg.date_enrolled <= '" + df.format(endDate) + "'  ");
				strBuffer.append(" group by o.patient_id");
				
				query = sessionFactory.getCurrentSession().createSQLQuery(strBuffer.toString());
				
//				 query = sessionFactory.getCurrentSession().createSQLQuery("select distinct pg.patient_id from patient_program pg "
//					        + "inner join patient pa on pg.patient_id = pa.patient_id "
//					        + "inner join orders ord on pg.patient_id = ord.patient_id "		
//					        + "where ((pg.date_completed is null) or(cast(pg.date_completed as DATE)> ' " + df.format(endDate)  + " ')) "
//					        + " and ord.concept_id IN (" + DrugOrderExportUtil.gpGetARVConceptIds() + ") "
//					        + " and pg.voided = 0 and ord.voided = 0 "
//					        + "and pa.voided = 0 and (cast(ord.start_date as DATE)) >= '" + df.format(startDate)
//					        + "' and (cast(ord.start_date as DATE)) <= '" + df.format(endDate) + "' and pg.program_id= 2" 
//					        + " and pg.date_enrolled <= '" + df.format(endDate) + "' "
//					        );
			}
			if (startDate == null && endDate != null) {
				strBuffer.append("SELECT DISTINCT o.patient_id , ");
				strBuffer.append("IF((select min(o.start_date)) <= ");
				strBuffer.append("'" + getDateFormated(endDate) + "'" + " , " + " true" + " ," + " false)");
				strBuffer.append(" FROM orders o ");
				strBuffer.append(" INNER JOIN patient pa on pa.patient_id=o.patient_id ");
				strBuffer
				        .append(" INNER JOIN patient_program pg on pg.patient_id=pa.patient_id ");
				strBuffer.append(" INNER JOIN program pr on pr.program_id=pg.program_id AND pr.program_id = 2 ");
				strBuffer.append(" INNER JOIN drug_order dro on dro.order_id=o.order_id ");
//				strBuffer.append(getQueryPortionForProphylaxis());
//				strBuffer.append(" INNER JOIN drug drug on drug.drug_id=dro.drug_inventory_id ");
				strBuffer.append(" AND o.concept_id in ("+DrugOrderExportUtil.gpGetARVConceptIds()+") ");
				strBuffer.append(" AND o.voided=0 AND pa.voided=0 AND pg.voided=0 and pg.date_enrolled <= '" + df.format(endDate) + "' ");
				strBuffer.append(" group by o.patient_id");
				
				query = sessionFactory.getCurrentSession().createSQLQuery(strBuffer.toString());
				
//				 query = sessionFactory.getCurrentSession().createSQLQuery("select distinct pg.patient_id from patient_program pg "
//					        + "inner join patient pa on pg.patient_id = pa.patient_id "
//					        + "inner join orders ord on pg.patient_id = ord.patient_id "		
//					        + "where ((pg.date_completed is null) or(cast(pg.date_completed as DATE)> ' " + df.format(endDate)  + " ')) "
//					        + " and ord.concept_id IN (" + DrugOrderExportUtil.gpGetARVConceptIds() + ") "
//					        + " and pg.voided = 0 and ord.voided = 0 "
//					        + "and pa.voided = 0 and (cast(ord.start_date as DATE)) <= '" + df.format(endDate)
//					        + "' and (cast(ord.start_date as DATE)) <= '" + df.format(endDate) + "' and pg.program_id= 2" 
//					        + " and pg.date_enrolled <= '" + df.format(endDate) + "' "
//					        );
			}
			if (startDate != null && endDate != null) {
				strBuffer.append("SELECT DISTINCT o.patient_id , ");
				strBuffer.append("IF((select min(o.start_date)) >= ");
				strBuffer.append("'" + getDateFormated(startDate) + "'" + " AND (select min(o.start_date))<= '" + getDateFormated(endDate) + "'"
				        + " ," + "true" + " ," + "false)");
				strBuffer.append(" FROM orders o ");
				strBuffer.append(" INNER JOIN patient pa on pa.patient_id=o.patient_id ");
				strBuffer
				        .append(" INNER JOIN patient_program pg on pg.patient_id=pa.patient_id ");
				strBuffer.append(" INNER JOIN program pr on pr.program_id=pg.program_id AND pr.program_id = 2 ");
				strBuffer.append(" INNER JOIN drug_order dro on dro.order_id=o.order_id ");
//				strBuffer.append(getQueryPortionForProphylaxis());
//				strBuffer.append(" INNER JOIN drug drug on drug.drug_id=dro.drug_inventory_id ");
				strBuffer.append(" AND o.concept_id in ("+DrugOrderExportUtil.gpGetARVConceptIds()+") ");
				strBuffer.append(" AND o.voided=0 AND pa.voided=0 AND pg.voided=0 and pg.date_enrolled <= '" + df.format(endDate) + "' ");
				strBuffer.append("group by o.patient_id");
				
				query = sessionFactory.getCurrentSession().createSQLQuery(strBuffer.toString());
				
				
//				 query = sessionFactory.getCurrentSession().createSQLQuery("select distinct pg.patient_id from patient_program pg "
//					        + "inner join patient pa on pg.patient_id = pa.patient_id "
//					        + "inner join orders ord on pg.patient_id = ord.patient_id "		
//					        + "where ((pg.date_completed is null) or(cast(pg.date_completed as DATE)> ' " + df.format(endDate)  + " ')) "
//					        + " and ord.concept_id IN (" + DrugOrderExportUtil.gpGetARVConceptIds() + ") "
//					        + " and pg.voided = 0 and ord.voided = 0 "
//					        + "and pa.voided = 0 and (cast(ord.start_date as DATE)) BETWEEN '" + df.format(startDate)+"' AND '"+df.format(endDate)+"'"
//					        + " and (cast(ord.start_date as DATE)) <= '" + df.format(endDate) + "' and pg.program_id= 2" 
//					        + " and pg.date_enrolled <= '" + df.format(endDate) + "' "
//					        );
				
			}
			if (startDate == null && endDate == null) {
				endDate=new Date();
				strBuffer.append("SELECT DISTINCT o.patient_id , ");
				strBuffer.append("IF((select min(o.start_date)) <= ");
				strBuffer.append("'" + getDateFormated(endDate) + "'" + " , " + " true" + " ," + " false)");
				strBuffer.append(" FROM orders o ");
				strBuffer.append(" INNER JOIN patient pa on pa.patient_id=o.patient_id ");
				strBuffer.append(" INNER JOIN patient_program pg on pg.patient_id=pa.patient_id  ");
				strBuffer.append(" INNER JOIN program pr on pr.program_id=pg.program_id AND pr.program_id = 2 ");
				strBuffer.append(" INNER JOIN drug_order dro on dro.order_id=o.order_id ");
//				strBuffer.append(getQueryPortionForProphylaxis());
//				strBuffer.append(" INNER JOIN drug drug on drug.drug_id=dro.drug_inventory_id ");
				strBuffer.append(" AND o.concept_id in ("+DrugOrderExportUtil.gpGetARVConceptIds()+") ");
				strBuffer.append(" AND o.voided=0 AND pa.voided=0 AND pg.voided=0 and pg.date_enrolled <= '" + df.format(endDate) + "' ");
				strBuffer.append(" group by o.patient_id");
				
				query = sessionFactory.getCurrentSession().createSQLQuery(strBuffer.toString());
				
//				 query = sessionFactory.getCurrentSession().createSQLQuery("select distinct pg.patient_id from patient_program pg "
//					        + "inner join person p on pg.patient_id = p.person_id "
//					        + "inner join patient pa on pg.patient_id = pa.patient_id "
//					        + "inner join orders ord on pg.patient_id = ord.patient_id "		
//					        + "where ((pg.date_completed is null) or(cast(pg.date_completed as DATE)> ' " + df.format(endDate)  + " ')) "
//					        + " and ord.concept_id IN (" + DrugOrderExportUtil.gpGetARVConceptIds() + ") "
//					        + " and pg.voided = 0 and ord.voided = 0 "
//					        + "and pa.voided = 0 and (cast(ord.start_date as DATE)) <= '" + df.format(endDate)
//					        + "' and (cast(ord.start_date as DATE)) <= '" + df.format(endDate) + "' and pg.program_id= 2" 
//					        + " and pg.date_enrolled <= '" + df.format(endDate) + "' AND "
//					        + patientAttributeStr(gender, minAge, maxAge, minBirthdate, maxBirthdate)
//					        );
			}
			
		}
		

		
		List<Object[]> records = query.list();
		
		int add = 0;
		
		for (Object[] obj : records) {
			add++;
			
			Integer id = (Integer) obj[0];
			String booleanValue = obj[1].toString() + add + "";
			
			patientStartedWhenMap.put(booleanValue, id);
		}
		List<Integer> patientIds = new ArrayList<Integer>();
		
		for (String key : patientStartedWhenMap.keySet()) {
			if (key.charAt(0) == '1') {
				Patient patient = Context.getPatientService().getPatient(patientStartedWhenMap.get(key));
					patientIds.add(patient.getPatientId());
			}
			
		}
//		List<Integer> patientIds = query.list();

	
		return patientIds;
	}
	
	//==========================================================================================================================================
	
	/**
	 * @see org.openmrs.module.drugorderexport.db.dao.DrugOrderExportDao#getPatientsExitedFromCare()
	 */
	@SuppressWarnings("unchecked")
    @Override
	public List<Integer> getPatientsExitedFromCare(Date endDate) {
		List<Integer> patientsExitedIds = null;
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		
		StringBuffer queryPortion = new StringBuffer();
		
		if(endDate==null)
			endDate=new Date();
		
//		try {
			Session session = sessionFactory.getCurrentSession();
			SQLQuery patientsExitedFromCare = session.createSQLQuery("SELECT distinct pa.patient_id FROM patient pa "
			        + "INNER JOIN person pe ON pa.patient_id = pe.person_id "
					+" INNER JOIN patient_program pg ON pg.patient_id = pa.patient_id   "
			        + "INNER JOIN obs ob ON ob.person_id = pe.person_id WHERE ob.concept_id = 1811 and ob.obs_datetime< '"+df.format(endDate)+"' and ob.voided=0 and pg.voided=0 and pe.voided=0 and ob.voided=0 and pa.voided=0 "
			        + " AND ((pg.date_completed is null) or(cast(pg.date_completed as DATE)< ' " + df.format(endDate) + " ')) "
			        );
			

    		
    		SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery(queryPortion.toString());
    		
			patientsExitedIds = patientsExitedFromCare.list();
			
//		}
//		catch (Exception e) {
//
//		}
		
		return patientsExitedIds;
	}
	
	//==========================================================================================================================================
	
	@SuppressWarnings("unchecked")
    /**
	 * @see org.openmrs.module.drugorderexport.db.dao.DrugOrderExportDao#getPatientsExitedInThePeriod(java.util.Date,
	 *      java.util.Date)
	 */
	@Override
	public List<Integer> getPatientsExitedInThePeriod(Date startDate, Date endDate) {
		SQLQuery patientsExitedInThePeriodQuery = null;
		List<Integer> patientsExitedInThePeriod = new ArrayList<Integer>();
		
		patientsExitedInThePeriodQuery = sessionFactory.getCurrentSession().createSQLQuery(
		    "SELECT DISTINCT pa.patient_id FROM patient pa INNER JOIN person pe ON pa.patient_id = pe.person_id "
		            + "INNER JOIN obs ob ON ob.person_id = pe.person_id WHERE ob.concept_id = 1811 AND ob.voided=0 "
		            + checkObsDate(startDate, endDate));
		patientsExitedInThePeriod = patientsExitedInThePeriodQuery.list();
		return patientsExitedInThePeriod;
	}
	
	//==========================================================================================================================================
	
	/**
	 * @see org.openmrs.module.drugorderexport.db.dao.DrugOrderExportDao#getPatientsOnProphylaxis(java.util.List,
	 *      java.util.List)
	 */
	@Override
	public List<Integer> getPatientsOnProphylaxis(List<Integer> prophylaxisDrugIds, List<Integer> patientIds) {
		List<Integer> patientOnProphylaxis = new ArrayList<Integer>();
		
		for (Integer patientId : patientIds) {
			Patient p = Context.getPatientService().getPatient(patientId);
			
//			if (isPatientOnProphylaxisOnly(patientId)) {
				patientOnProphylaxis.add(p.getPatientId());
//			}
		}
		return patientOnProphylaxis;
	}
	
	//==========================================================================================================================================
	
	public boolean isPatientOnProphylaxisOnlyBeforePeriod(Integer patientId,Date enddate) {
	
		Patient p=Context.getPatientService().getPatient(patientId);
		Set<RegimenComponent> components = new HashSet<RegimenComponent>();
		
		if(enddate==null)
			enddate=new Date();
		
		int regimenSize = 1;
		List<Regimen> regimens = new ArrayList<Regimen>();
		
		regimens = RegimenUtils.getRegimenHistory(p).getRegimenList();
		
		regimenSize = regimens.size() - 1;
		components = regimens.get(regimenSize).getComponents();
		
		for (Regimen r : regimens) { 
			components = r.getComponents();
		}
		List<Integer> regimenDrugs = new ArrayList<Integer>();

		for (RegimenComponent rc : components) {
			if (!rc.getDrugOrder().getDiscontinued() && (rc.getDrugOrder().getStartDate()!=null && rc.getDrugOrder().getStartDate().getTime()<= enddate.getTime())){
			if(rc.getDrug()!=null)
				regimenDrugs.add(rc.getDrug().getConcept().getConceptId());
				else
				regimenDrugs.add(rc.getGeneric().getConceptId());
		   }
		}
		boolean found = false;
		if (DrugOrderExportUtil.gpGetProphylaxisDrugConceptIds().size() >= regimenDrugs.size() && DrugOrderExportUtil.gpGetProphylaxisDrugConceptIds().containsAll(regimenDrugs)) {
			for (Integer r : regimenDrugs) {
				if (DrugOrderExportUtil.gpGetProphylaxisDrugConceptIds().contains(r)) {
					found = true;
				} else {
					found = false;
					break;
				}
			}
		}
		
		return found;
	}
	

	//==========================================================================================================================================
	/**
	 * @see org.openmrs.module.drugorderexport.db.dao.DrugOrderExportDao#getPatientsTakingDrugsInThePeriod(java.util.Date,
	 *      java.util.Date, java.lang.String, java.util.Date, java.util.Date, java.util.Date,
	 *      java.util.Date)
	 */
	@Override
	public List<Integer> getPatientsTakingDrugsInThePeriod(Date startDate, Date endDate, String gender, Date minAge,
	                                                       Date maxAge, Date minBirthdate, Date maxBirthdate) {
		SQLQuery patientsUnderDrugsQuery = null;
		List<Integer> patientsUnderDrugs = new ArrayList<Integer>();
		
		StringBuffer b = new StringBuffer();
		
		if (endDate == null) {
			endDate = new Date();
		}
		
		if (!gender.equals("") || minAge != null || maxAge != null || minBirthdate != null || maxBirthdate != null) {
			
			b.append(" SELECT DISTINCT o.patient_id FROM orders o ");
			b.append(" INNER JOIN patient pa on pa.patient_id=o.patient_id ");
			b
			        .append(" INNER JOIN patient_program pm on pm.patient_id=pa.patient_id AND pm.program_id=2 AND pm.date_completed is null AND pm.date_enrolled  <= '"
			                + getDateFormated(endDate) + "'  ");
			b.append(" inner join person p on p.person_id=pa.patient_id ");
			b.append(" inner join drug_order dor on dor.order_id=o.order_id ");
			b.append(checkInputDate(startDate, endDate));
			b.append("  and  ");
			b.append(patientAttributeStr(gender, minAge, maxAge, minBirthdate, maxBirthdate));
			
			patientsUnderDrugsQuery = sessionFactory.getCurrentSession().createSQLQuery(b.toString());
			
		} else {
			
			b.append(" SELECT DISTINCT o.patient_id FROM orders o ");
			b.append(" INNER JOIN patient pa on pa.patient_id=o.patient_id ");
			b
			        .append(" INNER JOIN patient_program pm on pm.patient_id=pa.patient_id AND pm.program_id=2 AND pm.date_completed is null AND pm.date_enrolled <= '"
			                + getDateFormated(endDate) + "'  ");
			b.append(" inner join drug_order dor on dor.order_id=o.order_id ");
			b.append(checkInputDate(startDate, endDate));
			
			patientsUnderDrugsQuery = sessionFactory.getCurrentSession().createSQLQuery(b.toString());
		}
		
		patientsUnderDrugs = patientsUnderDrugsQuery.list();
		return patientsUnderDrugs;
		
	}
	
	//==========================================================================================================================================
	
	/**
	 * @see org.openmrs.module.drugorderexport.db.dao.DrugOrderExportDao#getPatientsVoidedInThePeriod(java.util.Date,
	 *      java.util.Date)
	 */
	@Override
	public List<Integer> getPatientsVoidedInThePeriod(Date startDate, Date endDate) {
		SQLQuery patientsVoidedInThePeriodQuery = null;
		List<Integer> patientsVoidedInThePeriod = new ArrayList<Integer>();
		patientsVoidedInThePeriodQuery = sessionFactory.getCurrentSession().createSQLQuery(
		    "SELECT o.patient_id FROM patient o where o.date_voided is not null AND " + checkDateVoided(startDate, endDate));
		
		return patientsVoidedInThePeriodQuery.list();
		
	}
	
	//==========================================================================================================================================
	/**
	 * helps to check when patient has been voided
	 */
	public String checkDateVoided(Date startDate, Date endDate) {
		if (startDate != null && endDate != null) {
			return " (o.date_voided >= '" + getDateFormated(startDate) + "' AND o.date_voided <='" + getDateFormated(endDate) + "' )";
		} else if (endDate == null && startDate != null) {
			return " (o.date_voided >= '" + getDateFormated(startDate) + "' )";
		} else if (startDate == null && endDate != null) {
			return " (o.date_voided <= '" + getDateFormated(endDate) + "' )";
		} else if (startDate == null && endDate == null) {
			endDate = new Date();
			return " (o.date_voided <= '" + getDateFormated(endDate) + "' )";
		}
		return "";
	}
	
	//==========================================================================================================================================
	
	/**
	 * @see org.openmrs.module.drugorderexport.db.dao.DrugOrderExportDao#getPatientsWhoChangedRegimen(java.util.Date,
	 *      java.util.Date, java.lang.String, java.util.Date, java.util.Date, java.util.Date,
	 *      java.util.Date)
	 */
	@Override
	public List<Integer> getPatientsWhoChangedRegimen(Date startDate, Date endDate, String gender, Date minAge, Date maxAge,
	                                                  Date minBirthdate, Date maxBirthdate) {
		SQLQuery query = null;
		
		StringBuffer strBuffer = new StringBuffer();
		Map<String, Integer> patientStartedWhenMap = new HashMap<String, Integer>();
		
		//			Date minDiscontinuedDate = getWhenPatientStarted(patient);
		
		if (!gender.equals("") || minAge != null || maxAge != null || minBirthdate != null || maxBirthdate != null) {
			if (startDate != null && endDate == null) {
				strBuffer.append(" SELECT DISTINCT o.patient_id ,  ");
				strBuffer.append(" IF((select min(o.start_date)) >=' " + getDateFormated(startDate) + "',true,false)  ");
				strBuffer.append(" FROM orders o  ");
				strBuffer.append(" INNER JOIN drug_order dro on dro.order_id=o.order_id  ");
//				strBuffer.append(" INNER JOIN drug g on g.drug_id=dro.drug_inventory_id   ");
				strBuffer.append(" and o.concept_id IN(" + gpGetSecondLineDrugConceptIds() + ") ");
				strBuffer.append(" and o.voided=0   ");
				strBuffer.append("  INNER JOIN person p on p.person_id=o.patient_id AND  ");
				strBuffer.append(patientAttributeStr(gender, minAge, maxAge, minBirthdate, maxBirthdate));
				strBuffer.append(" group by o.patient_id ");
				
				query = sessionFactory.getCurrentSession().createSQLQuery(strBuffer.toString());
			}
			if (startDate == null && endDate != null) {
				strBuffer.append(" SELECT DISTINCT o.patient_id ,  ");
				strBuffer.append(" IF((select min(o.start_date)) <=' " + getDateFormated(endDate) + "',true,false)  ");
				strBuffer.append(" FROM orders o  ");
				strBuffer.append(" INNER JOIN drug_order dro on dro.order_id=o.order_id  ");
//				strBuffer.append(" INNER JOIN drug g on g.drug_id=dro.drug_inventory_id   ");
				strBuffer.append(" and o.concept_id IN(" + gpGetSecondLineDrugConceptIds() + ") ");
				strBuffer.append(" and o.voided=0   ");
				strBuffer.append("  INNER JOIN person p on p.person_id=o.patient_id AND  ");
				strBuffer.append(patientAttributeStr(gender, minAge, maxAge, minBirthdate, maxBirthdate));
				strBuffer.append(" group by o.patient_id ");
				
				query = sessionFactory.getCurrentSession().createSQLQuery(strBuffer.toString());
				
			}
			if (startDate != null && endDate != null) {
				strBuffer.append(" SELECT DISTINCT o.patient_id ,  ");
				strBuffer.append(" IF((select min(o.start_date)) >=' " + getDateFormated(startDate)
				        + "' AND (select min(o.start_date))<= '" + getDateFormated(endDate) + "',true,false)  ");
				strBuffer.append(" FROM orders o  ");
				strBuffer.append(" INNER JOIN drug_order dro on dro.order_id=o.order_id  ");
//				strBuffer.append(" INNER JOIN drug g on g.drug_id=dro.drug_inventory_id   ");
				strBuffer.append(" and o.concept_id IN(" + gpGetSecondLineDrugConceptIds() + ") ");
				strBuffer.append(" and o.voided=0  ");
				strBuffer.append("  INNER JOIN person p on p.person_id=o.patient_id AND  ");
				strBuffer.append(patientAttributeStr(gender, minAge, maxAge, minBirthdate, maxBirthdate));
				strBuffer.append(" group by o.patient_id ");
				
				query = sessionFactory.getCurrentSession().createSQLQuery(strBuffer.toString());
				
			}
			if (startDate == null && endDate == null) {
				endDate = new Date();
				strBuffer.append(" SELECT DISTINCT o.patient_id ,  ");
				strBuffer.append(" IF((select min(o.start_date)) <=' " + getDateFormated(endDate) + "',true,false)  ");
				strBuffer.append(" FROM orders o  ");
				strBuffer.append(" INNER JOIN drug_order dro on dro.order_id=o.order_id  ");
//				strBuffer.append(" INNER JOIN drug g on g.drug_id=dro.drug_inventory_id   ");
				strBuffer.append(" and o.concept_id IN(" + gpGetSecondLineDrugConceptIds() + ") ");
				strBuffer.append(" and o.voided=0  ");
				strBuffer.append("  INNER JOIN person p on p.person_id=o.patient_id AND  ");
				strBuffer.append(patientAttributeStr(gender, minAge, maxAge, minBirthdate, maxBirthdate));
				strBuffer.append(" group by o.patient_id ");
				
				query = sessionFactory.getCurrentSession().createSQLQuery(strBuffer.toString());
			}
			
		} else {
			if (startDate != null && endDate == null) {
				strBuffer.append(" SELECT DISTINCT o.patient_id ,  ");
				strBuffer.append(" IF((select min(o.start_date)) >=' " + getDateFormated(startDate) + "',true,false)  ");
				strBuffer.append(" FROM orders o  ");
				strBuffer.append(" INNER JOIN drug_order dro on dro.order_id=o.order_id  ");
//				strBuffer.append(" INNER JOIN drug g on g.drug_id=dro.drug_inventory_id   ");
				strBuffer.append(" and o.concept_id IN(" + gpGetSecondLineDrugConceptIds() + ") ");
				strBuffer.append(" and o.voided=0  ");
				strBuffer.append(" group by o.patient_id ");
				
				query = sessionFactory.getCurrentSession().createSQLQuery(strBuffer.toString());
			}
			if (startDate == null && endDate != null) {
				strBuffer.append(" SELECT DISTINCT o.patient_id ,  ");
				strBuffer.append(" IF((select min(o.start_date)) <=' " + getDateFormated(endDate) + "',true,false)  ");
				strBuffer.append(" FROM orders o  ");
				strBuffer.append(" INNER JOIN drug_order dro on dro.order_id=o.order_id  ");
//				strBuffer.append(" INNER JOIN drug g on g.drug_id=dro.drug_inventory_id   ");
				strBuffer.append(" and o.concept_id IN(" + gpGetSecondLineDrugConceptIds() + ") ");
				strBuffer.append(" and o.voided=0  ");
				strBuffer.append(" group by o.patient_id ");
				
				query = sessionFactory.getCurrentSession().createSQLQuery(strBuffer.toString());
			}
			if (startDate != null && endDate != null) {
				strBuffer.append(" SELECT DISTINCT o.patient_id ,  ");
				strBuffer.append(" IF((select min(o.start_date)) >=' " + getDateFormated(startDate)
				        + "' AND (select min(o.start_date))<= '" + getDateFormated(endDate) + "',true,false)  ");
				strBuffer.append(" FROM orders o  ");
				strBuffer.append(" INNER JOIN drug_order dro on dro.order_id=o.order_id  ");
//				strBuffer.append(" INNER JOIN drug g on g.drug_id=dro.drug_inventory_id   ");
				strBuffer.append(" and o.concept_id IN(" + gpGetSecondLineDrugConceptIds() + ") ");
				strBuffer.append(" and o.voided=0  ");
				strBuffer.append(" group by o.patient_id ");
				
				query = sessionFactory.getCurrentSession().createSQLQuery(strBuffer.toString());
			}
			if (startDate == null && endDate == null) {
				endDate = new Date();
				strBuffer.append(" SELECT DISTINCT o.patient_id ,  ");
				strBuffer.append(" IF((select min(o.start_date)) <=' " + getDateFormated(endDate) + "',true,false)  ");
				strBuffer.append(" FROM orders o  ");
				strBuffer.append(" INNER JOIN drug_order dro on dro.order_id=o.order_id  ");
//				strBuffer.append(" INNER JOIN drug g on g.drug_id=dro.drug_inventory_id    ");
				strBuffer.append(" and o.concept_id IN(" + gpGetSecondLineDrugConceptIds() + ") ");
				strBuffer.append(" and o.voided=0  ");
				strBuffer.append(" group by o.patient_id ");
				
				query = sessionFactory.getCurrentSession().createSQLQuery(strBuffer.toString());
				
			}
		}
		
//		log.info("kkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk "+strBuffer.toString());
		
		List<Object[]> records = query.list();
		
		int add = 0;
		
		for (Object[] obj : records) {
			add++;
			
			Integer id = (Integer) obj[0];
			String booleanValue = obj[1].toString() + add + "";
			
			patientStartedWhenMap.put(booleanValue, id);
		}
		List<Integer> patientWhoChangedFromFirstToSecondLine = new ArrayList<Integer>();
		
		for (String key : patientStartedWhenMap.keySet()) {
			if (key.charAt(0) == '1') {
				Integer patientId = patientStartedWhenMap.get(key);
				Patient patient = Context.getPatientService().getPatient(patientId);
				// because of some reason the patient start by second line
				if (!getIsFirstRegimenSecondLineOrProphylaxis(patient.getPatientId()) && !patient.getVoided()) {
					patientWhoChangedFromFirstToSecondLine.add(patientStartedWhenMap.get(key));
				}
				
			}
		}
		
		return patientWhoChangedFromFirstToSecondLine;
	}
	
	//==========================================================================================================================================
	
	/**
	 * @see org.openmrs.module.drugorderexport.db.dao.DrugOrderExportDao#getProphylaxisDrugsIds()
	 */
	@Override
	public List<Integer> getProphylaxisDrugsIds() {
		List<Integer> prophylaxisDrugs = DrugOrderExportUtil.gpGetProphylaxisAsIntegers();
		return prophylaxisDrugs;
	}
	//==========================================================================================================================================
	public List<Integer> getProphylaxisAndTbDrugsIds() {
		List<Integer> prophylaxisAndTBDrugs = DrugOrderExportUtil.gpGetProphylaxisAndTbIds();
		return prophylaxisAndTBDrugs;
	}
	//==========================================================================================================================================
		public List<Integer> getProphylaxisAndTbDrugConceptIds() {
			List<Integer> prophylaxisAndTBDrugConceptIds = DrugOrderExportUtil.gpGetProphylaxisAndTbDrugConceptIds();
			return prophylaxisAndTBDrugConceptIds;
		}
	
		
	/**
	 * allows to know if patient first regimen is of type second line or prophylaxis
	 * 
	 * @param patientId
	 * @return true or false
	 */
	
	public boolean getIsFirstRegimenSecondLineOrProphylaxis(Integer patientId) {
		
		Patient p = Context.getPatientService().getPatient(patientId);
		
		List<Regimen> regimens = RegimenUtils.getRegimenHistory(p).getRegimenList();
		
		Set<RegimenComponent> components = new HashSet<RegimenComponent>();
		
		boolean isOnSecondLine = true;
		
		Set<RegimenComponent> seeRegimen = new HashSet<RegimenComponent>();
		List<Integer> seeDrugs = new ArrayList<Integer>();
		
		Regimen firstRegimen = null;
		for (int i = 0; i < regimens.size(); i++) {
			seeRegimen = regimens.get(i).getComponents();
			
			for (RegimenComponent rc : seeRegimen) {
				if(rc.getDrug()!=null)
				seeDrugs.add(rc.getDrug().getConcept().getConceptId());
			}
			// take regimen other than one made by prophylaxis drugs
			// if the first is prophylaxis drugs take the following until 
			// it reaches the first arv regimens
			if (!getPatientsOnAll(seeDrugs,DrugOrderExportUtil.gpGetProphylaxisDrugConceptIds()) && !isRegimenSecondLine(seeDrugs)) {
				//	                	firstRegimen = regimens.get(i);
				isOnSecondLine = false;
				break;
				
			}
			
		}
		//		if (firstRegimen != null)
		//			components = firstRegimen.getComponents();
		//		
		//		List<Integer> regimenDrugs = new ArrayList<Integer>();
		//		
		//		for (RegimenComponent rc : components) {
		//			regimenDrugs.add(rc.getDrug().getDrugId());
		//		}
		//		
		//		List<Integer> gpGetSecondLineDrugIds = DrugOrderExportUtil.gpGetSecondLineDrugsAsList();
		//		for (Integer id : gpGetSecondLineDrugIds) {
		//			if (regimenDrugs.contains(id)) {
		//				isOnSecondLine = true;
		//			} else {
		//				isOnSecondLine = false;
		//				break;
		//			}
		//		}
		
		return isOnSecondLine;
	}
	
	//==========================================================================================================================================
	
	public boolean isRegimenSecondLine(List<Integer> regimenDrugIds) {
//		List<Integer> gpGetSecondLineDrugIds = DrugOrderExportUtil.gpGetSecondLineDrugsAsList();
		List<Integer> gpGetSecondLineDrugConceptIds = DrugOrderExportUtil.gpGetSecondLineDrugConceptIds();
		boolean confirm = false;
		for (Integer id : gpGetSecondLineDrugConceptIds) {
			if (regimenDrugIds.contains(id)) {
				confirm = true;
				break;
			}
		}
		return confirm;
		
	}
	
	//==========================================================================================================================================
	
	/**
	 * allows to compare two arraylists all of type integer
	 * 
	 * @param drugSelected
	 * @param regimenDrugs
	 * @return boolean
	 */
	public boolean getPatientsOnAll(List<Integer> drugSelected, List<Integer> regimenDrugs) {
		boolean found = false;
		
		//////////prophylaxis drugs Ids////////////////////
		//22:cotrimo(960)
		//27:cotrimo(480)
		//37:cotrimo sirop
		//23:Diflucon(200)
		//24:Dapsone(100)
		
		List<Integer> prophylaxisIds = DrugOrderExportUtil.gpGetProphylaxisAsIntegers();
		
		if (regimenDrugs.size() >= drugSelected.size() && regimenDrugs.containsAll(drugSelected)) {
			for (Integer r : regimenDrugs) {
				if (drugSelected.contains(r) || prophylaxisIds.contains(r)) {
					found = true;
				} else {
					found = false;
					break;
				}
			}
		}
		
		return found;
	}
	
	//==========================================================================================================================================
	
	/**
	 * @see org.openmrs.module.drugorderexport.db.dao.DrugOrderExportDao#getWhenPatientStarted(org.openmrs.Patient)
	 */
	@Override
	public Date getWhenPatientStarted(Patient patient) {
		SQLQuery query = null;
		Session session = sessionFactory.getCurrentSession();
		
		StringBuffer strbuf = new StringBuffer();
		
		strbuf.append("SELECT cast(min(o.start_date) as DATE ) FROM orders o  ");
		strbuf.append("INNER JOIN drug_order dro on dro.order_id = o.order_id  ");
		strbuf.append(getQueryPortionForProphylaxis());
		strbuf.append(" AND o.patient_id = ");
		strbuf.append(patient.getPatientId());
		
		query = session.createSQLQuery(strbuf.toString());
		
		List<Date> dates = query.list();
		Date whenPatientStarted = dates.get(0);
		
		return whenPatientStarted;
	}
	

	//==========================================================================================================================================
	
	/**
	 * @see org.openmrs.module.drugorderexport.db.dao.DrugOrderExportDao#patientAttributeStr(java.lang.String,
	 *      java.util.Date, java.util.Date, java.util.Date, java.util.Date)
	 */
	@Override
	public String patientAttributeStr(String gender, Date minAge, Date maxAge, Date minBirthdate, Date maxBirthdate) {
		StringBuffer attributeQPortion = new StringBuffer();
		if (!gender.equals("")) {
			attributeQPortion.append("AND gender='" + gender + "' ");
			
		}
		
		if (maxAge != null || minAge != null) {
			if (minAge != null && maxAge == null) {
				attributeQPortion.append("AND '" + getDateFormated(minAge) + "'>= CAST(p.birthdate as DATE) ");
			} else if (maxAge != null && minAge == null) {
				attributeQPortion.append("AND '" + getDateFormated(maxAge) + "'<= CAST(p.birthdate as DATE) ");
			} else {
				attributeQPortion.append("AND (CAST(p.birthdate as DATE)  >= '" + getDateFormated(maxAge) + "' AND CAST(p.birthdate as DATE) <= '"
				        + getDateFormated(minAge) + "')");
			}
		}
		
		if (minBirthdate != null || maxBirthdate != null) {
			if (minBirthdate != null && maxBirthdate == null) {
				attributeQPortion.append("AND CAST(p.birthdate as DATE) >= '" + getDateFormated(minBirthdate) + "' ");
			} else if (maxBirthdate != null && minBirthdate == null) {
				attributeQPortion.append("AND CAST(p.birthdate as DATE) <= '" + getDateFormated(maxBirthdate) + "' ");
			} else if(maxBirthdate!=null && minBirthdate!=null) {
				attributeQPortion.append("AND (CAST(p.birthdate as DATE)  >= '" + getDateFormated(minBirthdate) + "' AND CAST(p.birthdate as DATE) <= '"
				        + getDateFormated(maxBirthdate) + "') ");
			}
		}
		
		return attributeQPortion.toString().substring(3);
	}
	
	//==========================================================================================================================================
	
	/**
	 * @see org.openmrs.module.drugorderexport.db.dao.DrugOrderExportDao#patientsWhoHasDrugOrder(java.lang.String,
	 *      java.util.Date, java.util.Date, java.util.Date, java.util.Date)
	 */
	@Override
	public List<Integer> patientsWhoHasDrugOrder(String gender, Date minAge, Date maxAge, Date minBirthdate,
	                                             Date maxBirthdate) {
		SQLQuery query = null;
		
		StringBuffer strbuf = new StringBuffer();
		
		Date today = new Date();
		
		if (!gender.equals("") || minAge != null || maxAge != null || minBirthdate != null || maxBirthdate != null) {
			
			strbuf.append(" select distinct pg.patient_id from patient_program pg ");
			strbuf.append(" inner join patient pa on pa.patient_id = pg.patient_id ");
			strbuf.append(" AND pg.program_id=2 AND cast(pg.date_completed as Date) is null AND cast(pg.date_enrolled as Date) <= ' ");
			strbuf.append(getDateFormated(today) + "' ");
			strbuf.append(" inner join person p on pa.patient_id = p.person_id ");
			strbuf.append(" inner join obs obs on obs.person_id= p.person_id ");
			strbuf.append(" inner join orders o on pa.patient_id = o.patient_id ");
			strbuf.append(" inner join drug_order dor on dor.order_id = o.order_id ");
//			strbuf.append(" inner join drug d on dor.drug_inventory_id = d.drug_id AND ");
			strbuf.append(" AND "+patientAttributeStr(gender, minAge, maxAge, minBirthdate, maxBirthdate));
			strbuf.append(" and o.concept_id IN ("+DrugOrderExportUtil.gpGetARVConceptIds()+")");
//			strbuf.append(getStringFromIds(drugIds) + ") ");
		
			query = sessionFactory.getCurrentSession().createSQLQuery(strbuf.toString());
			
		}

		else {
			
			strbuf.append(" select distinct pg.patient_id from patient_program pg ");
			strbuf.append(" inner join patient pa on pa.patient_id = pg.patient_id ");
			strbuf.append(" AND pg.program_id=2 AND cast(pg.date_completed as Date) is null AND cast(pg.date_enrolled as Date) <= ' ");
			strbuf.append(getDateFormated(today) + "' ");
			strbuf.append(" inner join person p on pa.patient_id = p.person_id ");
			strbuf.append(" inner join obs obs on obs.person_id= p.person_id ");
			strbuf.append(" inner join orders o on pa.patient_id = o.patient_id ");
			strbuf.append(" inner join drug_order dor on dor.order_id = o.order_id ");
//			strbuf.append(" inner join drug d on dor.drug_inventory_id = d.drug_id  ");
			strbuf.append(" and o.concept_id IN ("+DrugOrderExportUtil.gpGetARVConceptIds()+")");
//			strbuf.append(" and d.drug_id IN (");
//			strbuf.append(getStringFromIds(drugIds) + ") ");
			
			query = sessionFactory.getCurrentSession().createSQLQuery(strbuf.toString());
			
		}
		
		List<Integer> patientIdsFromQuery = new ArrayList<Integer>();
		patientIdsFromQuery = query.list();
		
		return patientIdsFromQuery;
	}
	
	//==========================================================================================================================================
	/**
	 * Return a string of Ids concatenated one after another and separated by a comma
	 * 
	 *@param listOfIds
	 *@return stringOfIds
	 */
	@SuppressWarnings("unchecked")
	public String getStringFromIds(ArrayList<Integer> listOfIds) {
		String listVal = "";
		Iterator itr = listOfIds.iterator();
		while (itr.hasNext()) {
			listVal += itr.next().toString() + ",";
		}
		String listValSubStr = "";
		if (listVal.length() > 0)
			listValSubStr = listVal.substring(0, listVal.length() - 1);
		return listValSubStr;
	}
	
	//==========================================================================================================================================
	
	/**
	 * @see org.openmrs.module.drugorderexport.db.dao.DrugOrderExportDao#searchDrugOrderByDrug(java.lang.String,
	 *      java.util.Date, java.util.Date, java.util.List, java.lang.String, java.util.Date,
	 *      java.util.Date, java.util.Date, java.util.Date)
	 */
	@Override
	public List<Integer> searchDrugOrderByDrug(String anyOrAll, Date startdate, Date enddate, List<Drug> drugs,
	                                           String gender, Date minAge, Date maxAge, Date minBirthdate, Date maxBirthdate) {
		List<Integer> patientOnDrugs = null;
		
		List<Drug> allDrugs = new ArrayList<Drug>();
		
		ArrayList<Integer> drugIdList = new ArrayList<Integer>();
		
		if (enddate == null) {
			enddate = new Date();
		}
		for (Drug drug : drugs) {
			drugIdList.add(drug.getDrugId());
		}
		
		if (drugIdList.size() != 0) {
			if (anyOrAll.equals("any")) {
				patientOnDrugs = getQueryResult(drugIdList, "any", startdate, enddate, gender, minAge, maxAge, minBirthdate,
				    maxBirthdate);

			}
			if (anyOrAll.equals("none")) {
				patientOnDrugs = getQueryResult(drugIdList, "none", startdate, enddate, gender, minAge, maxAge,
				    minBirthdate, maxBirthdate);

			}
			if (anyOrAll.equals("all")) {
				patientOnDrugs = getQueryResult(drugIdList, "all", startdate, enddate, gender, minAge, maxAge, minBirthdate,
				    maxBirthdate);

				
			}
		} // else select any patient on any of drugs
		else {
//			allDrugs = Context.getConceptService().getAllDrugs();
			Concept concept = Context.getConceptService().getConcept(1085);
			allDrugs = Context.getConceptService().getDrugsByConcept(concept);
			for (Drug d : allDrugs) {
				drugIdList.add(d.getDrugId());
			}
//			drugIdList = (ArrayList<Integer>) DrugOrderExportUtil.gpGetARVDrugIds();
			if(anyOrAll.equals("any"))
			patientOnDrugs = getQueryResult(drugIdList, "any", startdate, enddate, gender, minAge, maxAge, minBirthdate,
			    maxBirthdate);
			else if(anyOrAll.equals("all"))
				patientOnDrugs = getQueryResult(drugIdList, "all", startdate, enddate, gender, minAge, maxAge, minBirthdate,
				    maxBirthdate);
			else if(anyOrAll.equals("none"))
				patientOnDrugs = getQueryResult(drugIdList, "none", startdate, enddate, gender, minAge, maxAge, minBirthdate,
				    maxBirthdate);
		}

		return patientOnDrugs;
	}
	//==========================================================================================================================================
	
		/**
		 * @see org.openmrs.module.drugorderexport.db.dao.DrugOrderExportDao#searchDrugOrderByDrug(java.lang.String,
		 *      java.util.Date, java.util.Date, java.util.List, java.lang.String, java.util.Date,
		 *      java.util.Date, java.util.Date, java.util.Date)
		 */
		@Override
		public List<Integer> searchDrugOrderByDrugsConcepts(String anyOrAll, Date startdate, Date enddate, List<Drug> drugs,
		                                           String gender, Date minAge, Date maxAge, Date minBirthdate, Date maxBirthdate) {
			List<Integer> patientOnDrugs = null;
			
			List<Drug> allDrugs = new ArrayList<Drug>();
			List<Object[]> allDrugsObj = new ArrayList<Object[]>();
			
			ArrayList<Integer> drugsConceptsId = new ArrayList<Integer>();
			DrugOrderService service = Context.getService(DrugOrderService.class);
			
			if (enddate == null) {
				enddate = new Date();
			}
			
			for (Drug drug : drugs) {
				drugsConceptsId.add(drug.getConcept().getConceptId());
			}
			if (drugsConceptsId.size() != 0) {
				if (anyOrAll.equals("any")) {
					patientOnDrugs = getQueryResultByConcept(drugsConceptsId, "any", startdate, enddate, gender, minAge, maxAge, minBirthdate,
					    maxBirthdate);

				}
				if (anyOrAll.equals("none")) {
					patientOnDrugs = getQueryResultByConcept(drugsConceptsId, "none", startdate, enddate, gender, minAge, maxAge,
					    minBirthdate, maxBirthdate);

				}
				if (anyOrAll.equals("all")) {
					patientOnDrugs = getQueryResultByConcept(drugsConceptsId, "all", startdate, enddate, gender, minAge, maxAge, minBirthdate,
					    maxBirthdate);

					
				}
			} // else select any patient on any of HIV drugs
			else {
				allDrugsObj = service.getArvDrugsByConcepts();
				for (Object[] obj : allDrugsObj) {
					allDrugs.add(Context.getConceptService().getDrug((Integer) obj[0]));
				}
				for (Drug d : allDrugs) {
					if(d!=null)
					drugsConceptsId.add(d.getConcept().getConceptId());
				}
//				drugIdList = (ArrayList<Integer>) DrugOrderExportUtil.gpGetARVDrugIds();
				if(anyOrAll.equals("any"))
				patientOnDrugs = getQueryResultByConcept(drugsConceptsId, "any", startdate, enddate, gender, minAge, maxAge, minBirthdate,
				    maxBirthdate);
				else if(anyOrAll.equals("all"))
					patientOnDrugs = getQueryResultByConcept(drugsConceptsId, "all", startdate, enddate, gender, minAge, maxAge, minBirthdate,
					    maxBirthdate);
				else if(anyOrAll.equals("none"))
					patientOnDrugs = getQueryResultByConcept(drugsConceptsId, "none", startdate, enddate, gender, minAge, maxAge, minBirthdate,
					    maxBirthdate);
			}

			return patientOnDrugs;
		}
	//==========================================================================================================================================
	
	//==========OPTION I. Consider drug id=====================
	/**
	 * Return a list of patientId
	 * 
	 * @param listDugIds
	 * @param val(None,Any or All)
	 * @param startDate
	 * @param endDate
	 * @return patientIds
	 */
	
	public List<Integer> getQueryResult(ArrayList<Integer> listDugIds, String val, Date startDate, Date endDate,
	                                    String gender, Date minAge, Date maxAge, Date minBirthdate, Date maxBirthdate) {
		SQLQuery query = null;
		
		List<Integer> records = null;
		StringBuffer strBuffer = new StringBuffer();
		
		if (val.equals("any")) {
			if (gender.equals("") && minAge == null && maxAge == null && minBirthdate == null && maxBirthdate == null) {
				
				strBuffer.append("SELECT DISTINCT o.patient_id FROM orders o   ");
				strBuffer.append(" INNER JOIN patient pat on pat.patient_id=o.patient_id   ");
				strBuffer.append(" INNER JOIN patient_program pg on pg.patient_id=pat.patient_id  ");
				strBuffer.append(" INNER JOIN program gr on gr.program_id=pg.program_id and gr.program_id=2 ");
				strBuffer.append(" AND o.voided=0 AND pat.voided=0 AND pg.voided=0 ");
				strBuffer.append("INNER JOIN drug_order dr ON dr.order_id = o.order_id "
			        + checkInputDate(startDate, endDate));
				strBuffer.append(" AND dr.drug_inventory_id IN (" + getStringFromIds(listDugIds) + ")");
				strBuffer.append(" AND o.concept_id IN (" + DrugOrderExportUtil.gpGetARVConceptIds() + ")");
				

			} else {
				
				strBuffer.append("SELECT DISTINCT o.patient_id FROM orders o   ");
				strBuffer.append(" INNER JOIN patient t on t.patient_id=o.patient_id   ");
				strBuffer.append(" INNER JOIN person p on p.person_id=t.patient_id   ");
				strBuffer.append(" INNER JOIN patient_program pg on pg.patient_id=t.patient_id");
				strBuffer.append(" INNER JOIN program gr on gr.program_id=pg.program_id and gr.program_id=2  ");
				strBuffer.append(" AND o.voided=0 AND t.voided=0 AND pg.voided=0 ");
				strBuffer.append("INNER JOIN drug_order dr ON dr.order_id = o.order_id "
			        + checkInputDate(startDate, endDate));
				strBuffer.append(" AND o.concept_id IN (" + DrugOrderExportUtil.gpGetARVConceptIds() + ")");
				strBuffer.append(" AND dr.drug_inventory_id IN (" + getStringFromIds(listDugIds) + ") AND ");
				strBuffer.append(patientAttributeStr(gender, minAge, maxAge, minBirthdate, maxBirthdate));
				
			}
			
			
			query = sessionFactory.getCurrentSession().createSQLQuery(strBuffer.toString());
			
			records = query.list();
			
		} else if (val.equals("none")) {
			if (gender.equals("") && minAge == null && maxAge == null && minBirthdate == null && maxBirthdate == null) {
				
				strBuffer.append("SELECT DISTINCT pt.patient_id from patient pt "
				        + " INNER JOIN orders os on os.patient_id = pt.patient_id and pt.voided=0 and pt.patient_id NOT IN "
				        + "(SELECT DISTINCT p.patient_id FROM patient p ");
				strBuffer.append(" INNER JOIN program gr INNER JOIN patient_program pg on pg.patient_id=p.patient_id and gr.program_id=2   ");
				strBuffer.append("INNER JOIN orders o ON p.patient_id = o.patient_id AND o.voided=0  ");
				strBuffer.append(" INNER JOIN drug_order dr ON dr.order_id = o.order_id   "
				        + checkInputDate(startDate, endDate));
				strBuffer.append(" AND o.concept_id IN (" + DrugOrderExportUtil.gpGetARVConceptIds() + ")");
				strBuffer.append(" AND dr.drug_inventory_id IN (" + getStringFromIds(listDugIds) + ")" + ")");
				
				query = sessionFactory.getCurrentSession().createSQLQuery(strBuffer.toString());
				
			} else {
				strBuffer.append("SELECT DISTINCT pt.patient_id from patient pt "
				        + " INNER JOIN orders os on os.patient_id = pt.patient_id and pt.voided=0 and pt.patient_id NOT IN "
				        + "(SELECT DISTINCT p.patient_id FROM patient p ");
				strBuffer.append(" INNER JOIN program gr INNER JOIN patient_program pg on pg.patient_id=p.patient_id and gr.program_id=2   ");
				strBuffer.append("INNER JOIN orders o ON p.patient_id = o.patient_id AND o.voided=0  ");
				strBuffer.append(" INNER JOIN drug_order dr ON dr.order_id = o.order_id   "
				        + checkInputDate(startDate, endDate));
				strBuffer.append(" AND o.concept_id IN (" + DrugOrderExportUtil.gpGetARVConceptIds() + ")");
				strBuffer.append(" AND dr.drug_inventory_id IN (" + getStringFromIds(listDugIds) + "))");
				strBuffer.append(" AND pt.patient_id IN(SELECT p.person_id FROM person p WHERE ");
				strBuffer.append(patientAttributeStr(gender, minAge, maxAge, minBirthdate, maxBirthdate) + ")");
				
				query = sessionFactory.getCurrentSession().createSQLQuery(strBuffer.toString());
				
			}
			
			records = query.list();
			
		} else if (val.equals("all")) {
			if (gender.equals("") && minAge == null && maxAge == null && minBirthdate == null && maxBirthdate == null) {
				strBuffer.append("SELECT DISTINCT o.patient_id FROM orders o WHERE o.patient_id in(SELECT DISTINCT p.patient_id FROM patient p ");
				strBuffer.append(" INNER JOIN program gr INNER JOIN patient_program pg on pg.patient_id=p.patient_id and gr.program_id=2   ");
				strBuffer.append("INNER JOIN orders o ON p.patient_id = o.patient_id AND o.voided=0 AND p.voided=0 ");
				strBuffer.append("INNER JOIN drug_order dr ON dr.order_id = o.order_id "
				        + checkInputDate(startDate, endDate));
				strBuffer.append(" AND o.concept_id IN (" + DrugOrderExportUtil.gpGetARVConceptIds() + ")");
				strBuffer.append(" AND dr.drug_inventory_id IN (" + getStringFromIds(listDugIds) + "))");
				
				query = sessionFactory.getCurrentSession().createSQLQuery(strBuffer.toString());
				
			} else {
				
				strBuffer
				        .append("SELECT DISTINCT o.patient_id FROM orders o WHERE o.patient_id IN(SELECT DISTINCT p.patient_id FROM patient p ");
				strBuffer.append(" INNER JOIN program gr INNER JOIN patient_program pg on pg.patient_id=p.patient_id and gr.program_id=2   ");
				strBuffer.append(" INNER JOIN orders o ON p.patient_id = o.patient_id AND o.voided=0 AND p.voided=0  ");
				strBuffer.append("INNER JOIN drug_order dr ON dr.order_id = o.order_id "
				        + checkInputDate(startDate, endDate));
				strBuffer.append(" AND o.concept_id IN (" + DrugOrderExportUtil.gpGetARVConceptIds() + ")");
				strBuffer.append(" AND dr.drug_inventory_id IN (" + getStringFromIds(listDugIds) + "))");
				strBuffer.append(" AND o.patient_id in(SELECT  DISTINCT p.patient_id FROM patient p WHERE p.patient_id  ");
				strBuffer.append("IN(SELECT p.person_id FROM person p WHERE ");
				strBuffer.append(patientAttributeStr(gender, minAge, maxAge, minBirthdate, maxBirthdate) + "))");
				
				query = sessionFactory.getCurrentSession().createSQLQuery(strBuffer.toString());
			}
			
			
			
			List<Integer> patientIds = query.list();
			
			records = getPatientOnAllDrugs(listDugIds, patientIds);
			
			
		}
		
		return records;
		
	}
	
	//==========================================================================================================================================
	//==========OPTION II. Consider drugs concepts ids=====================
	/**
	 * 
	 * gives patients on hiv drugs by their concepts
	 * 
	 * @param listDrugsConceptsIds
	 * @param val
	 * @param startDate
	 * @param endDate
	 * @param gender
	 * @param minAge
	 * @param maxAge
	 * @param minBirthdate
	 * @param maxBirthdate
	 * @return
	 */
	
	public List<Integer> getQueryResultByConcept(ArrayList<Integer> listDrugsConceptsIds, String val, Date startDate, Date endDate,
	                                    String gender, Date minAge, Date maxAge, Date minBirthdate, Date maxBirthdate) {
		SQLQuery query = null;
		
		List<Integer> records = null;
		StringBuffer strBuffer = new StringBuffer();
		
		if (val.equals("any")) {
			if (gender.equals("") && minAge == null && maxAge == null && minBirthdate == null && maxBirthdate == null) {
				 
				strBuffer.append("SELECT DISTINCT o.patient_id FROM orders o   ");
				strBuffer.append(" INNER JOIN patient pat on pat.patient_id=o.patient_id   ");
				strBuffer.append(" INNER JOIN patient_program pg on pg.patient_id=pat.patient_id  ");
				strBuffer.append(" INNER JOIN program gr on gr.program_id=pg.program_id and gr.program_id=2 ");
				strBuffer.append(" AND o.voided=0 AND pat.voided=0 AND pg.voided=0 ");
				strBuffer.append( checkInputDate(startDate, endDate));
				strBuffer.append(" AND o.concept_id IN (" + getStringFromIds(listDrugsConceptsIds) + ")");
				strBuffer.append(" AND o.concept_id IN (" + DrugOrderExportUtil.gpGetARVConceptIds() + ")");
				
				
			} else {
				
				strBuffer.append("SELECT DISTINCT o.patient_id FROM orders o   ");
				strBuffer.append(" INNER JOIN patient t on t.patient_id=o.patient_id   ");
				strBuffer.append(" INNER JOIN person p on p.person_id=t.patient_id   ");
				strBuffer.append(" INNER JOIN patient_program pg on pg.patient_id=t.patient_id");
				strBuffer.append(" INNER JOIN program gr on gr.program_id=pg.program_id and gr.program_id=2  ");
				strBuffer.append(" AND o.voided=0 AND t.voided=0 AND pg.voided=0 ");
				strBuffer.append( checkInputDate(startDate, endDate));
				strBuffer.append(" AND o.concept_id IN (" + getStringFromIds(listDrugsConceptsIds) + ")");
				strBuffer.append(" AND o.concept_id IN (" + DrugOrderExportUtil.gpGetARVConceptIds() + ") AND ");
				strBuffer.append(patientAttributeStr(gender, minAge, maxAge, minBirthdate, maxBirthdate));
				
				
			}
			
			
			query = sessionFactory.getCurrentSession().createSQLQuery(strBuffer.toString());
			
			records = query.list();
			
		} else if (val.equals("none")) {
			if (gender.equals("") && minAge == null && maxAge == null && minBirthdate == null && maxBirthdate == null) {
				
				strBuffer.append("SELECT DISTINCT pt.patient_id from patient pt "
				        + " INNER JOIN orders os on os.patient_id = pt.patient_id and pt.voided=0 and pt.patient_id NOT IN "
				        + "(SELECT DISTINCT p.patient_id FROM patient p ");
				strBuffer.append(" INNER JOIN program gr INNER JOIN patient_program pg on pg.patient_id=p.patient_id and gr.program_id=2   ");
				strBuffer.append("INNER JOIN orders o ON p.patient_id = o.patient_id AND o.voided=0  ");
				strBuffer.append(" AND o.concept_id IN (" + getStringFromIds(listDrugsConceptsIds) + ")");
				strBuffer.append(" AND o.concept_id IN (" + DrugOrderExportUtil.gpGetARVConceptIds() + "))");
				
				
				query = sessionFactory.getCurrentSession().createSQLQuery(strBuffer.toString());
				
			} else {
				strBuffer.append("SELECT DISTINCT pt.patient_id from patient pt "
				        + " INNER JOIN orders os on os.patient_id = pt.patient_id and pt.voided=0 and pt.patient_id NOT IN "
				        + "(SELECT DISTINCT p.patient_id FROM patient p ");
				strBuffer.append(" INNER JOIN program gr INNER JOIN patient_program pg on pg.patient_id=p.patient_id and gr.program_id=2   ");
				strBuffer.append("INNER JOIN orders o ON p.patient_id = o.patient_id AND o.voided=0  ");
				strBuffer.append( checkInputDate(startDate, endDate));
				strBuffer.append(" AND o.concept_id IN (" + getStringFromIds(listDrugsConceptsIds) + ")");
				strBuffer.append(" AND o.concept_id IN (" + DrugOrderExportUtil.gpGetARVConceptIds() + ")");
				strBuffer.append(" AND pt.patient_id IN(SELECT p.person_id FROM person p WHERE ");
				strBuffer.append(patientAttributeStr(gender, minAge, maxAge, minBirthdate, maxBirthdate) + "))");
				
				
				query = sessionFactory.getCurrentSession().createSQLQuery(strBuffer.toString());
				
			}
			
			records = query.list();
			
		} else if (val.equals("all")) {
			if (gender.equals("") && minAge == null && maxAge == null && minBirthdate == null && maxBirthdate == null) {
				strBuffer.append("SELECT DISTINCT o.patient_id FROM orders o WHERE o.patient_id in(SELECT DISTINCT p.patient_id FROM patient p ");
				strBuffer.append(" INNER JOIN program gr INNER JOIN patient_program pg on pg.patient_id=p.patient_id and gr.program_id=2   ");
				strBuffer.append("INNER JOIN orders o ON p.patient_id = o.patient_id AND o.voided=0 AND p.voided=0 ");
				strBuffer.append( checkInputDate(startDate, endDate));
				strBuffer.append(" AND o.concept_id IN (" + getStringFromIds(listDrugsConceptsIds) + ")");
				strBuffer.append(" AND o.concept_id IN (" + DrugOrderExportUtil.gpGetARVConceptIds() + "))");
				
				query = sessionFactory.getCurrentSession().createSQLQuery(strBuffer.toString());
				
			} else {
				
				strBuffer.append("SELECT DISTINCT o.patient_id FROM orders o WHERE o.patient_id IN(SELECT DISTINCT p.patient_id FROM patient p ");
				strBuffer.append(" INNER JOIN program gr INNER JOIN patient_program pg on pg.patient_id=p.patient_id and gr.program_id=2   ");
				strBuffer.append(" INNER JOIN orders o ON p.patient_id = o.patient_id AND o.voided=0 AND p.voided=0  ");
				strBuffer.append( checkInputDate(startDate, endDate));
				strBuffer.append(" AND o.concept_id IN (" + getStringFromIds(listDrugsConceptsIds) + ")");
				strBuffer.append(" AND o.concept_id IN (" + DrugOrderExportUtil.gpGetARVConceptIds() + ")");
				strBuffer.append(" AND o.patient_id in(SELECT  DISTINCT p.patient_id FROM patient p WHERE p.patient_id  ");
				strBuffer.append("IN(SELECT p.person_id FROM person p WHERE ");
				strBuffer.append(patientAttributeStr(gender, minAge, maxAge, minBirthdate, maxBirthdate) + ")))");
				
				
				query = sessionFactory.getCurrentSession().createSQLQuery(strBuffer.toString());
			}
			
			
			List<Integer> patientIds = query.list();
			
			records = getPatientOnAllDrugsByConceptIds(listDrugsConceptsIds, patientIds);
			
		}
		
		return records;
		
	}
       
	
	
	//==========================================================================================================================================
	/**
	 * gives patients currently taking all selected drugs
	 * 
	 * @param listDrugIds
	 * @param patientIds
	 * @return patient ids
	 */
	public List<Integer> getPatientOnAllDrugs(List<Integer> listDrugIds, List<Integer> patientIds) {
		
		List<Integer> patientOnDrugs = new ArrayList<Integer>();
		
		int regimenSize = 1;
		
		for (Integer patientId : patientIds) {
			Patient p = Context.getPatientService().getPatient(patientId);
			List<Regimen> patientRegimens = new ArrayList<Regimen>();
			
			patientRegimens = RegimenUtils.getRegimenHistory(p).getRegimenList();
			
			Set<RegimenComponent> regimenComponents = new HashSet<RegimenComponent>();
			
			regimenSize = patientRegimens.size() - 1;
			
			Set<RegimenComponent> componentsStopped = new HashSet<RegimenComponent>();
			
			regimenComponents = patientRegimens.get(regimenSize).getComponents();
			
			Date today = new Date();
			
			if (patientRegimens.get(regimenSize).getEndDate() == null) {
				patientRegimens.get(regimenSize).setEndDate(today);
			}
			for (RegimenComponent rc : regimenComponents) {
				if (rc.getStopDate() != null)
					if (rc.getStopDate().getTime() <= patientRegimens.get(regimenSize).getStartDate().getTime()) {
						componentsStopped.add(rc);
						
					}
			}
			
			if (componentsStopped != null)
				regimenComponents.removeAll(componentsStopped);
			
			List<Integer> regimenDrugs = new ArrayList<Integer>();
			
			for (RegimenComponent rc : regimenComponents) {
				regimenDrugs.add(rc.getDrug().getDrugId());
			}
			
			if (getPatientsOnAll(listDrugIds, regimenDrugs)) {
				patientOnDrugs.add(p.getPatientId());
				
			}
			
		}
		return patientOnDrugs;
		
	}
	
	//==========================================================================================================================================
	
	/**
	 * @see org.openmrs.module.drugorderexport.db.dao.DrugOrderExportDao#searchDrugOrderByStopReason(java.util.Date,
	 *      java.util.Date, java.util.List, java.util.List, java.util.List, java.lang.String,
	 *      java.util.Date, java.util.Date)
	 */
	@Override
	public List<Integer> searchDrugOrderByStopReason(Date startdate, Date enddate, List<Integer> drugs,
	                                                 List<Integer> stopReason, List<Integer> gererics, String gender,
	                                                 Date minAge, Date maxAge) {
		
		
		SQLQuery queryStopReason = null;
		List<Integer> patientsWhoStopped = new ArrayList<Integer>();
		List<Concept> stopReasons = new ArrayList<Concept>();
		List<Concept> generics = new ArrayList<Concept>();
		List<Drug> drugss = new ArrayList<Drug>();
		
		DrugOrderService service = (DrugOrderService) Context.getService(DrugOrderService.class);
		
//		if (enddate == null) {
//			enddate = new Date();
//		}
		
		for (Integer conceptId : stopReason) {
			stopReasons.add(Context.getConceptService().getConcept(conceptId));
		}
		for (Integer conceptId : gererics) {
			generics.add(Context.getConceptService().getConcept(conceptId));
		}
		for (Integer drugId : drugs) {
			drugss.add(getDrugById(drugId));
		}
		List<Integer> patientWhoStoppedList = new ArrayList<Integer>();
		
		if (startdate != null || enddate != null || drugs.size() != 0 || generics.size() == 0 || stopReason.size() != 0) {
			queryStopReason = sessionFactory.getCurrentSession().createSQLQuery(
			    "select DISTINCT prt.patient_id from patient prt WHERE prt.patient_id IN "
			            + "(select DISTINCT p.patient_id from patient p "
			            + "INNER JOIN orders o on o.patient_id = p.patient_id AND o.discontinued='1' and o.voided=0 AND p.voided=0  "
			            + " AND o.concept_id IN ("+DrugOrderExportUtil.gpGetARVConceptIds()+") "
			            + getQueryPortionForSearchStopDrug(startdate, enddate, drugss, generics, stopReasons) + " ) "

			);
			 
		} else {
			queryStopReason = sessionFactory.getCurrentSession().createSQLQuery(
			    "select DISTINCT p.patient_id from patient p INNER JOIN orders o on o.patient_id = p.patient_id "
			            + "AND o.discontinued='1' AND o.voided=0 AND p.voided=0 "
			            + " AND o.concept_id IN ("+DrugOrderExportUtil.gpGetARVConceptIds()+") "
			    		);
		}
		
		
		patientWhoStoppedList = queryStopReason.list();
		
		if (gender.equals("") && minAge == null && maxAge == null) {
			for (Integer i : patientWhoStoppedList) {
				Patient patient = Context.getPatientService().getPatient(i);  
				if (isAllDrugsStopped(patient) )  
				  if(!isLastRegimenProphy(patient)) 
					patientsWhoStopped.add(patient.getPatientId());
			}
		}

		else if(!gender.equals("") && minAge != null && maxAge != null){
			//////////////////////////patient with attrib/////////////////////////////////////////////////////
			SQLQuery patientWithAttributeQuery = null;
			List<Integer> patientWithAttributeList = new ArrayList<Integer>();
			patientWithAttributeQuery = sessionFactory.getCurrentSession().createSQLQuery(
			    "select  DISTINCT p.patient_id from patient p WHERE p.patient_id IN(select p.person_id from person p WHERE "
			            + patientAttributeStr(gender, minAge, maxAge, null, null) + ")");
			
			patientWithAttributeList = patientWithAttributeQuery.list();
			
			/////////////////////////End/patient with attrib//////////////////////////////////////////////////////
			
			for (Integer patientId : patientWithAttributeList) {
				Patient patient = Context.getPatientService().getPatient(patientId);
				if (patientWhoStoppedList.contains(patientId)) {
					if (isAllDrugsStopped(patient))
						if(!isLastRegimenProphy(patient)) 
//						if (!getPatientsExitedInThePeriod(startdate, enddate).contains(patient))
							patientsWhoStopped.add(patient.getPatientId());
				}
			}
			
		}
		return patientsWhoStopped;
	}
	
	//==========================================================================================================================================
	/**
	 * Return a portion of the query (String) depending on the availability of the provided
	 * parameter
	 * 
	 * @param startDate
	 * @param endDate
	 * @param drugs
	 * @param generics
	 * @param stopReason
	 * @return portion of the query
	 */
	public String getQueryPortionForSearchStopDrug(Date startDate, Date endDate, List<Drug> drugs, List<Concept> generics,
	                                               List<Concept> stopReason) {
		StringBuffer queryPortion = new StringBuffer();
		ArrayList<Integer> drugIdList = new ArrayList<Integer>();
		ArrayList<Integer> stopReasonIdList = new ArrayList<Integer>();
		ArrayList<Integer> genericIdList = new ArrayList<Integer>();
		
		/*=============================================================================================*/
		if (startDate != null && endDate != null) {
			queryPortion.append(" AND (o.discontinued_date  >= '" + getDateFormated(startDate) + "' AND o.discontinued_date AND '"
			        + getDateFormated(endDate) + "')");
		}
		/*============================================================================================*/
		if (startDate != null && endDate == null) {
			queryPortion.append(" AND (o.discontinued_date  >= '" + getDateFormated(startDate) + "')");
		}
		/*============================================================================================*/
		if (startDate == null && endDate != null) {
			queryPortion.append(" AND (o.discontinued_date  <= '" + getDateFormated(endDate) + "')");
		}
		/*============================================================================================*/
		if (startDate == null && endDate == null) {
			endDate = new Date();
			queryPortion.append(" AND (o.discontinued_date  <= '" + getDateFormated(endDate) + "')");
		}
		/*============================================================================================*/
//		if (drugs.size() != 0 || generics.size() != 0 || stopReason.size() != 0) {
			queryPortion.append(" INNER JOIN drug_order dr ON dr.order_id = o.order_id");
		
			if (!drugs.isEmpty()  && !generics.isEmpty()) {
				for (Drug drug : drugs) {
					drugIdList.add(drug.getDrugId());
				}
				for (Concept genericId : generics) {
					genericIdList.add(genericId.getConceptId());
				}
				queryPortion.append(" INNER JOIN drug d ON d.drug_id = dr.drug_inventory_id AND d.drug_id IN ("
				        + getStringFromIds(drugIdList) + ") AND o.concept_id IN (" + getStringFromIds(genericIdList) + ")");
				
			}
			
			if (drugs!=null && generics==null) {
				for (Drug drug : drugs) {
					drugIdList.add(drug.getDrugId());
				}
				queryPortion.append(" INNER JOIN drug d ON d.drug_id = dr.drug_inventory_id AND d.drug_id IN ("
				        + getStringFromIds(drugIdList) + ")");
			}
			if (drugs.isEmpty() && !generics.isEmpty()) {
				
				for (Concept genericId : generics) {
					genericIdList.add(genericId.getConceptId());
				}
				queryPortion.append(" AND o.concept_id IN ("
				        + getStringFromIds(genericIdList) + ")");
			}
			
			if (stopReason.size() != 0) {
				for (Concept reasonId : stopReason) {
					stopReasonIdList.add(reasonId.getConceptId());
				}
				queryPortion.append(" INNER JOIN concept c ON c.concept_id = o.discontinued_reason AND c.concept_id IN ("
				        + getStringFromIds(stopReasonIdList) + ")");
			}
			
//		}
		
		return queryPortion.toString();
	}
	//==========================================================================================================================================
		/**
		 * Return a portion of the query (String) depending on the availability of the provided
		 * parameter
		 * 
		 * @param startDate
		 * @param endDate
		 * @param drugs
		 * @param generics
		 * @param stopReason
		 * @return portion of the query
		 */
		public String getQueryPortionForSearchStopDrugByConcept(Date startDate, Date endDate, List<Concept> generics,
		                                               List<Concept> stopReason) {
			StringBuffer queryPortion = new StringBuffer();

			ArrayList<Integer> stopReasonIdList = new ArrayList<Integer>();
			ArrayList<Integer> genericIdList = new ArrayList<Integer>();
			/*=============================================================================================*/
			if (startDate != null && endDate != null) {
				queryPortion.append(" AND ( CAST(o.discontinued_date AS DATE)  >= '" + getDateFormated(startDate) + "' AND  CAST(o.discontinued_date AS DATE)  AND '"
				        + getDateFormated(endDate) + "')");
			}
			/*============================================================================================*/
			if (startDate != null && endDate == null) {
				queryPortion.append(" AND ( CAST(o.discontinued_date AS DATE)   >= '" + getDateFormated(startDate) + "')");
			}
			/*============================================================================================*/
			if (startDate == null && endDate != null) {
				queryPortion.append(" AND ( CAST(o.discontinued_date AS DATE)   <= '" + getDateFormated(endDate) + "')");
			}
			/*============================================================================================*/
			if (startDate == null && endDate == null) {
				endDate = new Date();
				queryPortion.append(" AND ( CAST(o.discontinued_date AS DATE)   <= '" + getDateFormated(endDate) + "')");
			}
			/*============================================================================================*/
			if (generics.size() != 0 || stopReason.size() != 0) {
				queryPortion.append(" INNER JOIN drug_order dr ON dr.order_id = o.order_id");
				
				if (generics.size() != 0) {
					
					for (Concept genericId : generics) {
						genericIdList.add(genericId.getConceptId());
					}
					queryPortion.append(" INNER JOIN drug d ON d.drug_id = dr.drug_inventory_id AND d.concept_id IN (" + getStringFromIds(genericIdList) + ")");
					
				}
				
				if (generics.size() != 0) {
					
					for (Concept genericId : generics) {
						genericIdList.add(genericId.getConceptId());
					}
					queryPortion.append(" INNER JOIN drug d ON d.drug_id = dr.drug_inventory_id AND d.concept_id IN ("
					        + getStringFromIds(genericIdList) + ")");
				}
				
				if (stopReason.size() != 0) {
					for (Concept reasonId : stopReason) {
						stopReasonIdList.add(reasonId.getConceptId());
					}
					queryPortion.append(" INNER JOIN concept c ON c.concept_id = o.discontinued_reason AND c.concept_id IN ("
					        + getStringFromIds(stopReasonIdList) + ")");
				}
				
			}
			
			return queryPortion.toString();
		}
		
	//==========================================================================================================================================
	
	public Date getTreeMonthBefore(Date date) {
		Session session = getSessionFactory().getCurrentSession();
		SQLQuery query = session.createSQLQuery("SELECT DATE_SUB(CAST('" + getDateFormated(date)
		        + "' AS DATE), INTERVAL 3 MONTH)");
		
		return ((Date) query.uniqueResult());
	}
	
	//==========================================================================================================================================
	
	/**
	 * @see org.openmrs.module.drugorderexport.db.dao.DrugOrderExportDao#getDateObjectFormAge(int)
	 */
	@Override
	public Date getDateObjectFormAge(int age) {
		Calendar cal = new GregorianCalendar();
		cal.add(Calendar.YEAR, -age);
		return cal.getTime();
	}
	
	//==========================================================================================================================================
	
	/**
	 * @see org.openmrs.module.drugorderexport.db.dao.DrugOrderExportDao#getRegimensAsString(java.util.Set)
	 */
	@Override
	public String getRegimensAsString(Set<RegimenComponent> regimens) {
		StringBuffer sb = new StringBuffer();
		RegimenComponent components[] = regimens.toArray(new RegimenComponent[0]);
		for (int r = 0; r < components.length; r++) {
			RegimenComponent reg = components[r];
			RegimenComponent nextReg = r >= components.length - 1 ? null : components[r + 1];
			if (nextReg == null || !reg.getStartDate().equals(nextReg.getStartDate()))
				sb.append((new StringBuilder(String.valueOf(reg.toString()))).append(" ").toString());
			
			else{
			if(reg.getDrug()!=null)
				sb.append((new StringBuilder(String.valueOf(reg.getDrug().getName()))).append("-").toString());
			else
				sb.append((new StringBuilder(String.valueOf(reg.getGeneric().getName()))).append("-").toString());
			}
		}
		
		return sb.toString();
	}
	
	//==========================================================================================================================================
	
	/**
	 * @see org.openmrs.module.drugorderexport.db.dao.DrugOrderExportDao#getStringFromArrayListOfString(java.util.ArrayList)
	 */
	@Override
	public String getStringFromArrayListOfString(ArrayList<String> listOfIds) {
		String listVal = "";
		Iterator<String> itr = listOfIds.iterator();
		while (itr.hasNext()) {
			listVal += itr.next().toString() + ";";
		}
		return listVal;
	}
	//==========================================================================================================================================
	
	/**
	 * @see org.openmrs.module.drugorderexport.db.dao.DrugOrderExportDao#getDateFormated(java.util.Date)
	 */
	@Override
	public String getDateFormated(Date dateObject) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return df.format(dateObject);
	}
	
	//==========================================================================================================================================
	
	/**
	 * @see org.openmrs.module.drugorderexport.db.dao.DrugOrderExportDao#getPatientLastEnconterDate(java.lang.Integer)
	 */
	@Override
	public Date getPatientLastEncounterDate(Integer patientId) {
		Session session = getSessionFactory().getCurrentSession();
		SQLQuery query = session
		        .createSQLQuery("SELECT cast(max(encounter_datetime) as DATE ) FROM encounter where patient_id = "
		                + patientId);
		return (Date) query.uniqueResult();
	}
	
	//==========================================================================================================================================
	/**
	 * @see org.openmrs.module.drugorderexport.db.dao.DrugOrderExportDao#getPatientLastVisitDate(java.lang.Integer)
	 */
	@Override
	public Date getPatientLastVisitDate(Integer patientId) {
		Session session = getSessionFactory().getCurrentSession();
		SQLQuery query = session
		        .createSQLQuery("SELECT cast(max(value_datetime) as DATE ) FROM obs where concept_id=5096 and person_id = "
		                + patientId);
		return (Date) query.uniqueResult();
	}
	
	//==========================================================================================================================================
	
	/**
	 * @see org.openmrs.module.drugorderexport.db.dao.DrugOrderExportDao#getWhenPatStartedXRegimen(java.lang.Integer,
	 *      java.lang.String)
	 */
	@Override
	public Date getWhenPatStartedXRegimen(Integer patientId, String gpDrugs) {
		SQLQuery query = null;
		Session session = sessionFactory.getCurrentSession();
		
		StringBuffer strbuf = new StringBuffer();
		strbuf.append("SELECT cast(min(o.start_date) as DATE ) FROM orders o  ");
		strbuf.append("INNER JOIN drug_order dro on dro.order_id = o.order_id  ");
		strbuf.append(" AND o.concept_id IN (" + gpDrugs + ")");
		strbuf.append(" AND o.patient_id = " + patientId);
		strbuf.append(" AND o.voided = 0 ");
		
		
		query = session.createSQLQuery(strbuf.toString());
		Date date = (Date) query.uniqueResult();
		
		return date;
	}
	
	//==========================================================================================================================================
	
	/**
     * @see org.openmrs.module.drugorderexport.db.dao.DrugOrderExportDao#getActivePreARTPatients(java.util.List, java.util.Date)
     */
    @Override
    public List<Integer> getActivePreARTPatients(List<Integer> patients, Date endDate) {
    	List<Integer> activePreARTPatients = new ArrayList<Integer>();
		
		Session session = getSessionFactory().getCurrentSession();
		
		
		if (endDate == null) {
			endDate = new Date();
		}
//		Date threeMonthsBeforeEndDate = getTreeMonthBefore(endDate);
		
		for (Integer patientId : patients) {
//			StringBuffer maxEncounterQuery = new StringBuffer();
//			StringBuffer maxReturnVisitQuery = new StringBuffer();
//			
//			maxEncounterQuery.append("select cast(max(encounter_datetime)as DATE) from encounter where ");
//			maxEncounterQuery.append(" (select(cast(max(encounter_datetime)as Date))) <=  ");
//			maxEncounterQuery.append(" '" + getDateFormated(endDate) + "' and patient_id = " + patientId);
//			
//			
//			SQLQuery maxEncounter = session.createSQLQuery(maxEncounterQuery.toString());			
//			
//			Date maxEncounterDateTime = (Date) maxEncounter.uniqueResult();
//			
//			maxReturnVisitQuery.append(" select cast(max(value_datetime) as DATE ) from obs where ");
//			maxReturnVisitQuery.append(" (select(cast(max(value_datetime)as Date))) <=  ");
//			maxReturnVisitQuery.append(" '" + getDateFormated(endDate) + "' ");
//			maxReturnVisitQuery.append(" AND concept_id = 5096 and person_id = " + patientId);
//			
//			SQLQuery maxReturnVisit = session.createSQLQuery(maxReturnVisitQuery.toString());
//			
//			Date maxReturnVisitDay = (Date) maxReturnVisit.uniqueResult();
			
//			if ((maxReturnVisitDay != null) && (maxEncounterDateTime != null)) {
//				
//				if (((maxEncounterDateTime.getTime()) >= threeMonthsBeforeEndDate.getTime() && (maxEncounterDateTime
//				        .getTime()) <= endDate.getTime())
//				        || ((maxReturnVisitDay.getTime()) >= threeMonthsBeforeEndDate.getTime() && (maxReturnVisitDay
//				                .getTime()) <= endDate.getTime())) {
					
					if (!getPatientsExitedFromCare(endDate).contains(patientId)){
//					if(!isAllDrugsStopped(Context.getPatientService().getPatient(patientId)))
//								if(!patsCompletedHIVProgramList.contains(patientId))
								activePreARTPatients.add(patientId);
					}
					
//				}
			}

//			else if (maxReturnVisitDay == null && maxEncounterDateTime != null) {
//				if (maxEncounterDateTime.getTime() >= threeMonthsBeforeEndDate.getTime() && maxEncounterDateTime.getTime()<= endDate.getTime()) {
////					if (!getPatientsExitedFromCare().contains(patientId))
////						if(!isAllDrugsStopped(Context.getPatientService().getPatient(patientId)))
////								if(!patsCompletedHIVProgramList.contains(patientId))
//								activePreARTPatients.add(patientId);
//					
//				}
//			} else if (maxReturnVisitDay != null && maxReturnVisitDay.getTime() > endDate.getTime()) {
////				if (!getPatientsExitedFromCare().contains(patientId))
////					if(!isAllDrugsStopped(Context.getPatientService().getPatient(patientId)))
////							if(!patsCompletedHIVProgramList.contains(patientId))
//							activePreARTPatients.add(patientId);
//				
//			}
			
		//}
		return activePreARTPatients;
    }
    
    //==========================================================================================================================================
	
    /**
     * @see org.openmrs.module.drugorderexport.db.dao.DrugOrderExportDao#patientOnFirstLineReg(java.util.Date, java.util.Date, java.lang.String, java.util.Date, java.util.Date, java.util.Date, java.util.Date)
     */
    @Override
    public List<Integer> getPatientsOnFirstLineReg(Date startdate, Date enddate, String gender, Date minAge, Date maxAge,
                                               Date minBirthdate, Date maxBirthdate) {

    	StringBuffer s = new StringBuffer();
    	if (gender.equals("") && minAge == null && maxAge == null && minBirthdate == null && maxBirthdate == null) {
    	s.append("select DISTINCT pa.patient_id from patient pa ");
    	s.append("inner join person p on pa.patient_id = p.person_id ");
    	s.append("INNER JOIN patient_program pg on pg.patient_id=pa.patient_id and pg.program_id=2 ");
    	s.append("INNER JOIN orders o ON pa.patient_id = o.patient_id AND o.voided=0 AND pa.voided=0 ");
    	s.append("INNER JOIN drug_order dr ON dr.order_id = o.order_id "+ checkInputDate(startdate, enddate));
//    	s.append("INNER JOIN drug d ON dr.drug_inventory_id = d.drug_id ");
    	s.append(" AND o.concept_id IN ("+DrugOrderExportUtil.gpGetFirstLineDrugConceptIds()+") ");
//    	s.append("AND dr.drug_inventory_id NOT IN("+DrugOrderExportUtil.getSecondLineDrugIds()+")  ");
    	}
    	else{
    		s.append("select DISTINCT pa.patient_id from patient pa ");
        	s.append("inner join person p on pa.patient_id = p.person_id ");
        	s.append("INNER JOIN patient_program pg on pg.patient_id=pa.patient_id and pg.program_id=2 ");
        	s.append("INNER JOIN orders o ON pa.patient_id = o.patient_id AND o.voided=0 AND pa.voided=0 ");
        	s.append("INNER JOIN drug_order dr ON dr.order_id = o.order_id "+ checkInputDate(startdate, enddate));
//        	s.append("INNER JOIN drug d ON dr.drug_inventory_id = d.drug_id ");
//        	s.append(" AND d.concept_id IN ("+DrugOrderExportUtil.gpGetARVConceptIds()+") ");
        	s.append("AND o.concept_id IN("+DrugOrderExportUtil.gpGetFirstLineDrugConceptIds()+") AND ");
        	s.append(patientAttributeStr(gender, minAge, maxAge, minBirthdate, maxBirthdate));
    	}
    	SQLQuery query = null;
    	
    	query = sessionFactory.getCurrentSession().createSQLQuery(s.toString());
    	
    	List<Integer> firstLinePatients = query.list();
    	
//    	StringBuffer s1 = new StringBuffer();
//    	s1.append("select DISTINCT pa.patient_id from patient pa ");
//    	s1.append("inner join person p on pa.patient_id = p.person_id ");
//    	s1.append("INNER JOIN patient_program pg on pg.patient_id=pa.patient_id and pg.program_id=2 ");
//    	s1.append("INNER JOIN orders o ON pa.patient_id = o.patient_id AND o.voided=0 AND pa.voided=0 ");
//    	s1.append("INNER JOIN drug_order dr ON dr.order_id = o.order_id "+ checkInputDate(startdate, enddate));
//    	s1.append("AND dr.drug_inventory_id IN("+DrugOrderExportUtil.getSecondLineDrugIds()+") ");
//
//    	List<Integer> secondLinePatients = sessionFactory.getCurrentSession().createSQLQuery(s1.toString()).list();
//    	
//    	List<Integer> list = new ArrayList<Integer>();
//    	
//    	for (Integer id : firstLinePatients) {
//	        if(!secondLinePatients.contains(id))
//	        	list.add(id);
//        }
    	
    	
	    return firstLinePatients;
    }
    
    
  //==========================================================================================================================================
	
    
    /**
     * @see org.openmrs.module.drugorderexport.db.dao.DrugOrderExportDao#getPatientsOnRegimenCategory(java.lang.String, java.util.Date, java.util.Date, java.lang.String, java.util.Date, java.util.Date, java.util.Date, java.util.Date)
     */
    @Override
    public List<Integer> getPatientsOnRegimenCategory(String categoryConceptId, Date startDate, Date endDate,
                                                      String gender, Date minAge, Date maxAge, Date minBirthdate,
                                                      Date maxBirthdate) {

    	StringBuffer queryPortion = new StringBuffer();
    	
    	List<Integer> patientIds = new ArrayList<Integer>();

    	DrugOrderService service = Context.getService(DrugOrderService.class);
    	
    	if (gender.equals("") && minAge == null && maxAge == null && minBirthdate == null && maxBirthdate == null){
    		queryPortion.append(" SELECT DISTINCT o.patient_id FROM orders o ");
//    		queryPortion.append(" INNER JOIN drug_order dro on dro.order_id=o.order_id  ");
    		queryPortion.append(" INNER JOIN patient pat on pat.patient_id=o.patient_id ");
    		queryPortion.append( service.checkInputDate(startDate, endDate));
    		queryPortion.append(" INNER JOIN patient_program pg on pat.patient_id=pg.patient_id ");
    		queryPortion.append(" INNER JOIN program gr on gr.program_id=pg.program_id AND gr.program_id = 2 ");
    		queryPortion.append(" AND o.concept_id in( "+categoryConceptId+") ");
//    		queryPortion.append(" AND o.voided=0 AND pat.voided=0 AND pg.voided=0 ");
    	}
    	else{
    		queryPortion.append(" SELECT DISTINCT o.patient_id FROM orders o ");
//    		queryPortion.append(" INNER JOIN drug_order dro on dro.order_id=o.order_id  ");
    		queryPortion.append(" INNER JOIN patient pat on pat.patient_id=o.patient_id ");
    		queryPortion.append(service.checkInputDate(startDate, endDate));
    		queryPortion.append(" INNER JOIN patient_program pg on pat.patient_id=pg.patient_id ");
    		queryPortion.append(" INNER JOIN program gr on gr.program_id=pg.program_id AND gr.program_id =2 ");
    		queryPortion.append(" AND o.concept_id in( "+categoryConceptId+") ");
//    		queryPortion.append(" AND o.voided=0 AND pat.voided=0 AND pg.voided=0 ");
    		queryPortion.append(" INNER JOIN person p on o.patient_id = p.person_id AND  ");
    		queryPortion.append(patientAttributeStr(gender, minAge, maxAge, minBirthdate, maxBirthdate));
    	}

    		SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery(queryPortion.toString());
    			
    		patientIds = query.list();

    		return patientIds;
    }
    
    
  //==========================================================================================================================================
	
    
    @Override
	public List<Drug> getArvDrugs() {
		SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery("select d.drug_id from drug d " +
				" INNER JOIN concept c on c.concept_id=d.concept_id " +
				" and d.concept_id IN(633,814,796,628,749,794,795,635,631,5424,625,792,802,5811,797,630,2833,2203,1613) " +
				" and c.retired=0");
		List<Integer> hivDrugIds = query.list();
		List<Drug> hivDrugs = new ArrayList<Drug>();
		
		for (Integer id : hivDrugIds) {
			hivDrugs.add(Context.getConceptService().getDrug(id));
		}
		return hivDrugs;
	}
    //==========================================================================================================================================
    
	@Override
	public List<Object[]> getArvDrugsByConcepts() {
		SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery("SELECT distinct d.concept_id,cn.name FROM drug d  " +
				" INNER JOIN concept_name cn on cn.concept_id=d.concept_id "+
				" INNER JOIN concept c on c.concept_id=d.concept_id " +
				" and d.concept_id IN(633,814,796,628,749,794,795,635,631,5424,625,792,802,5811,797,630,2833,2203,1613) " +
				" and c.retired=0 group by d.drug_id ");
		
		List<Object[]> hivDrugObj = query.list();
	
		return hivDrugObj;
	}
	
	//==========================================================================================================================================
	@Override
	public Drug getDrugByConceptId(int conceptId) {
//		Session session = getSessionFactory().getCurrentSession();
//		return (Drug) session.load(Drug.class, conceptId);
		
		Concept c = Context.getConceptService().getConcept(conceptId);
		Drug d = Context.getConceptService().getDrugsByConcept(c).get(0);
		
		return d;
	}
	
	//==========================================================================================================================================
	@Override
	public List<Drug> getListOfDrugsByConceptsIds(List<Integer> drugConceptsIdsList) {
		List<Drug> drugList = new ArrayList<Drug>();
		for (Integer conceptId : drugConceptsIdsList) {
			drugList.add(getDrugByConceptId(conceptId));
		}
		return drugList;
	}
	//==========================================================================================================================================
	@Override
	public List<Integer> getPatientOnAllDrugsByConceptIds(	List<Integer> listDrugIdsByConcept, List<Integer> patientIds) {
        List<Integer> patientOnDrugs = new ArrayList<Integer>();
		
		int regimenSize = 1;
		
		for (Integer patientId : patientIds) {
			Patient p = Context.getPatientService().getPatient(patientId);
			List<Regimen> patientRegimens = new ArrayList<Regimen>();
			
			patientRegimens = RegimenUtils.getRegimenHistory(p).getRegimenList();
			
			Set<RegimenComponent> regimenComponents = new HashSet<RegimenComponent>();
			
			regimenSize = patientRegimens.size() - 1;
			
			Set<RegimenComponent> componentsStopped = new HashSet<RegimenComponent>();
			
			regimenComponents = patientRegimens.get(regimenSize).getComponents();
			
			Date today = new Date();
			
			if (patientRegimens.get(regimenSize).getEndDate() == null) {
				patientRegimens.get(regimenSize).setEndDate(today);
			}
			for (RegimenComponent rc : regimenComponents) {
				if (rc.getStopDate() != null)
					if (rc.getStopDate().getTime() <= patientRegimens.get(regimenSize).getStartDate().getTime()) {
						componentsStopped.add(rc);
						
					}
			}
			
			if (componentsStopped != null)
				regimenComponents.removeAll(componentsStopped);
			
			List<Integer> regimenDrugsConceptsIds = new ArrayList<Integer>();

			for (RegimenComponent rc : regimenComponents) {
//				if(rc.getDrug()!=null)
				regimenDrugsConceptsIds.add(rc.getDrugOrder().getConcept().getConceptId());
			}
			
			if (getPatientsOnAllByConceptIds(listDrugIdsByConcept, regimenDrugsConceptsIds)) {
				patientOnDrugs.add(p.getPatientId());
				
			}
			
		}
		
		return patientOnDrugs;
	}
	
	//==========================================================================================================================================
	/**
	 * allows to compare two arraylists of drug concept ids
	 * 
	 * @param drugSelected
	 * @param regimenDrugs
	 * @return boolean value
	 */
	public boolean getPatientsOnAllByConceptIds(List<Integer> drugSelectedByConcept,List<Integer> regimenDrugsByConcept) {
		boolean found = false;
		if (regimenDrugsByConcept.size() >= drugSelectedByConcept.size()&& regimenDrugsByConcept.containsAll(drugSelectedByConcept)) {
			for (Integer r : regimenDrugsByConcept) {
				if (drugSelectedByConcept.contains(r) || DrugOrderExportUtil.gpGetProphylaxisDrugConceptIds()
								.contains(r)||DrugOrderExportUtil.gpGetTBDrugsConceptsIds().contains(r)) {
					found = true;
				} else {
					found = false;
					break;
				}
			}
		}

		return found;
	}
	//==========================================================================================================================================
	@Override
	public Boolean isLastRegimenProphy(Patient p) {
		List<Regimen> regimens = RegimenUtils.getRegimenHistory(p).getRegimenList();
				Set<RegimenComponent> components = new HashSet<RegimenComponent>();
				
				if(regimens.size()!=0)
				components = regimens.get(regimens.size()-1).getComponents();
				
				List<Integer> regimenDrugs = new ArrayList<Integer>();
				
				for (RegimenComponent rc : components) {
					if (!rc.getDrugOrder().getDiscontinued())
                         if(rc.getDrug()!=null)
						regimenDrugs.add(rc.getDrug().getConcept().getConceptId());
//					regimenDrugs.add(rc.getGeneric().getConceptId());
				}
				
				boolean found = false;
				List<Integer> prophylaxisIds = DrugOrderExportUtil.gpGetProphylaxisDrugConceptIds();
				if (prophylaxisIds.size() >= regimenDrugs.size()&& prophylaxisIds.containsAll(regimenDrugs)) {
					for (Integer r : regimenDrugs) {
						if (prophylaxisIds.contains(r)) {
							found = true;
						} else {
							found = false;
							break;
						}
					}
		}
				return found;
	}

	@Override
	public List<Integer> getPatientsOnRegimenCategoryActive(
			String categoryConceptId, Date startDate, Date endDate,
			String gender, Date minAge, Date maxAge, Date minBirthdate,
			Date maxBirthdate) {
		
		StringBuffer queryPortion = new StringBuffer();
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    	
    	List<Integer> patientIds = new ArrayList<Integer>();

    	DrugOrderService service = Context.getService(DrugOrderService.class);
    	
    	if (gender.equals("") && minAge == null && maxAge == null && minBirthdate == null && maxBirthdate == null){
//    		queryPortion.append(" SELECT DISTINCT o.patient_id FROM orders o ");
////    		queryPortion.append(" INNER JOIN drug_order dro on dro.order_id=o.order_id  ");
//    		queryPortion.append(" INNER JOIN patient pat on pat.patient_id=o.patient_id ");
//    		queryPortion.append( service.checkInputDate(startDate, endDate));
//    		queryPortion.append(" INNER JOIN patient_program pg on pat.patient_id=pg.patient_id ");
//    		queryPortion.append(" INNER JOIN program gr on gr.program_id=pg.program_id AND gr.program_id = 2 ");
//    		queryPortion.append(" AND o.concept_id in( "+categoryConceptId+") ");
////    		queryPortion.append(" AND o.voided=0 AND pat.voided=0 AND pg.voided=0 ");
    		
    		queryPortion.append(" select distinct pg.patient_id from patient_program pg ");
    		queryPortion.append( "inner join person pe on pg.patient_id = pe.person_id ");
    		queryPortion.append( "inner join patient pa on pg.patient_id = pa.patient_id ");
    		queryPortion.append("inner join orders ord on pg.patient_id = ord.patient_id ");
    		queryPortion.append( "where ((pg.date_completed is null) or(cast(pg.date_completed as DATE)> ' " + df.format(endDate) + " ')) "	);	      
	       
    		queryPortion.append( " and ord.concept_id IN (" + categoryConceptId);
    		queryPortion.append( ") and pg.voided = 0 and pe.voided = 0 and ord.voided = 0 ");
    		queryPortion.append("and pa.voided = 0 and (cast(ord.start_date as DATE)) <= '" + df.format(endDate) + "' and pg.program_id=2 ");
    		queryPortion.append( " and pg.date_enrolled <= '" + df.format(endDate) + "'");
    	}
    	else{
//    		queryPortion.append(" SELECT DISTINCT o.patient_id FROM orders o ");
////    		queryPortion.append(" INNER JOIN drug_order dro on dro.order_id=o.order_id  ");
//    		queryPortion.append(" INNER JOIN patient pat on pat.patient_id=o.patient_id ");
//    		queryPortion.append(service.checkInputDate(startDate, endDate));
//    		queryPortion.append(" INNER JOIN patient_program pg on pat.patient_id=pg.patient_id ");
//    		queryPortion.append(" INNER JOIN program gr on gr.program_id=pg.program_id AND gr.program_id =2 ");
//    		queryPortion.append(" AND o.concept_id in( "+categoryConceptId+") ");
////    		queryPortion.append(" AND o.voided=0 AND pat.voided=0 AND pg.voided=0 ");
//    		queryPortion.append(" INNER JOIN person p on o.patient_id = p.person_id AND  ");
//    		queryPortion.append(patientAttributeStr(gender, minAge, maxAge, minBirthdate, maxBirthdate));
//    		
    		queryPortion.append(" select distinct pg.patient_id from patient_program pg ");
    		queryPortion.append( "inner join person pe on pg.patient_id = pe.person_id ");
    		queryPortion.append( "inner join patient pa on pg.patient_id = pa.patient_id ");
    		queryPortion.append("inner join orders ord on pg.patient_id = ord.patient_id ");
	        // + "inner join drug_order do on ord.order_id = do.order_id "
	        //+ "inner join drug d on do.drug_inventory_id = d.drug_id "
    		queryPortion.append( "where ((pg.date_completed is null) or(cast(pg.date_completed as DATE)> ' " + df.format(endDate) + " ')) "	);	      
    		queryPortion.append( ") " + " and ord.concept_id IN (" + categoryConceptId);
    		queryPortion.append( ") and pg.voided = 0 and pe.voided = 0 and ord.voided = 0 ");
    		queryPortion.append("and pa.voided = 0 and (cast(ord.start_date as DATE)) <= categoryConceptId'" + df.format(endDate) + "' and pg.program_id= 2");
    		queryPortion.append( " and pg.date_enrolled <= '" + df.format(endDate) + "'");
    		queryPortion.append(patientAttributeStr(gender, minAge, maxAge, minBirthdate, maxBirthdate));
    	}

    		SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery(queryPortion.toString());
    		
//    		log.info("wwwwwwwwwwwwwwwwwwwwwwwwwwwwwwww "+query.toString());
    			
    		patientIds = query.list();

    		return patientIds;
	}

	@Override
	
	public List<Integer> searchDrugOrderByDrugActive(String anyOrAll, 
			Date startdate, Date enddate, List<Drug> drugs, String gender,
			Date minAge, Date maxAge, Date minBirthdate, Date maxBirthdate) {
		List<Integer> patientOnDrugs = null;
		
		List<Drug> allDrugs = new ArrayList<Drug>();
		List<Object[]> allDrugsObj = new ArrayList<Object[]>();
		
		ArrayList<Integer> drugsConceptsId = new ArrayList<Integer>();
		DrugOrderService service = Context.getService(DrugOrderService.class);
		
		if (enddate == null) {
			enddate = new Date();
		}
		
		for (Drug drug : drugs) {
			drugsConceptsId.add(drug.getConcept().getConceptId());
		}
		
		if (drugsConceptsId.size() != 0) {
			if (anyOrAll.equals("any")) {
				patientOnDrugs = getQueryResultByConcept(drugsConceptsId, "any", startdate, enddate, gender, minAge, maxAge, minBirthdate,
				    maxBirthdate);

			}
			if (anyOrAll.equals("none")) {
				patientOnDrugs = getQueryResultByConcept(drugsConceptsId, "none", startdate, enddate, gender, minAge, maxAge,
				    minBirthdate, maxBirthdate);

			}
			if (anyOrAll.equals("all")) {
				patientOnDrugs = getQueryResultByConcept(drugsConceptsId, "all", startdate, enddate, gender, minAge, maxAge, minBirthdate,
				    maxBirthdate);

				
			}
		} // else select any patient on any of HIV drugs
		else {
			allDrugsObj = service.getArvDrugsByConcepts();
			for (Object[] obj : allDrugsObj) {
				allDrugs.add(Context.getConceptService().getDrug((Integer) obj[0]));
			}
			for (Drug d : allDrugs) {
				if(d!=null)
				drugsConceptsId.add(d.getConcept().getConceptId());
			}
//			drugIdList = (ArrayList<Integer>) DrugOrderExportUtil.gpGetARVDrugIds();
			if(anyOrAll.equals("any"))
			patientOnDrugs = getQueryResultByConcept(drugsConceptsId, "any", startdate, enddate, gender, minAge, maxAge, minBirthdate,
			    maxBirthdate);
			else if(anyOrAll.equals("all"))
				patientOnDrugs = getQueryResultByConcept(drugsConceptsId, "all", startdate, enddate, gender, minAge, maxAge, minBirthdate,
				    maxBirthdate);
			else if(anyOrAll.equals("none"))
				patientOnDrugs = getQueryResultByConcept(drugsConceptsId, "none", startdate, enddate, gender, minAge, maxAge, minBirthdate,
				    maxBirthdate);
		}

		return patientOnDrugs;
	}

	
	   public List<Integer> getQueryResultByConceptActive(ArrayList<Integer> listDrugsConceptsIds, String val, Date startDate, Date endDate,
			           String gender, Date minAge, Date maxAge, Date minBirthdate, Date maxBirthdate) {
			SQLQuery query = null;
			
			List<Integer> records = null;
			StringBuffer strBuffer = new StringBuffer();
			
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			
			if (val.equals("any")) {
			if (gender.equals("") && minAge == null && maxAge == null && minBirthdate == null && maxBirthdate == null) {
			
//			strBuffer.append("SELECT DISTINCT o.patient_id FROM orders o   ");
//			strBuffer.append(" INNER JOIN patient pat on pat.patient_id=o.patient_id   ");
//			strBuffer.append(" INNER JOIN patient_program pg on pg.patient_id=pat.patient_id  ");
//			strBuffer.append(" INNER JOIN program gr on gr.program_id=pg.program_id and gr.program_id=2 ");
//			strBuffer.append(" AND o.voided=0 AND pat.voided=0 AND pg.voided=0 ");
//			strBuffer.append( checkInputDate(startDate, endDate));  
//			strBuffer.append(" AND o.concept_id IN (" + getStringFromIds(listDrugsConceptsIds) + ")");
//			strBuffer.append(" AND o.concept_id IN (" + DrugOrderExportUtil.gpGetARVConceptIds() + ")");
			
			
			
			strBuffer.append(" select distinct pg.patient_id from patient_program pg ");
			strBuffer.append( "inner join person pe on pg.patient_id = pe.person_id ");
			strBuffer.append( "inner join patient pa on pg.patient_id = pa.patient_id ");
			strBuffer.append("inner join orders ord on pg.patient_id = ord.patient_id ");
			strBuffer.append( "where ((pg.date_completed is null) or(cast(pg.date_completed as DATE)> ' " + df.format(endDate) + " ')) "	);	      
	       
    		strBuffer.append(" AND o.concept_id IN (" + getStringFromIds(listDrugsConceptsIds) + ")");
    		strBuffer.append(" AND o.concept_id IN (" + DrugOrderExportUtil.gpGetARVConceptIds() + ")");
    		strBuffer.append( " and pg.voided = 0 and pe.voided = 0 and ord.voided = 0 ");
    		strBuffer.append("and pa.voided = 0 and (cast(ord.start_date as DATE)) <= '" + df.format(endDate) + "' and pg.program_id=2 ");
    		strBuffer.append( " and pg.date_enrolled <= '" + df.format(endDate) + "'");
			
			} else {
			
//			strBuffer.append("SELECT DISTINCT o.patient_id FROM orders o   ");
//			strBuffer.append(" INNER JOIN patient t on t.patient_id=o.patient_id   ");
//			strBuffer.append(" INNER JOIN person p on p.person_id=t.patient_id   ");
//			strBuffer.append(" INNER JOIN patient_program pg on pg.patient_id=t.patient_id");
//			strBuffer.append(" INNER JOIN program gr on gr.program_id=pg.program_id and gr.program_id=2  ");
//			strBuffer.append(" AND o.voided=0 AND t.voided=0 AND pg.voided=0 ");
//			strBuffer.append( checkInputDate(startDate, endDate));
//			strBuffer.append(" AND o.concept_id IN (" + getStringFromIds(listDrugsConceptsIds) + ")");
//			strBuffer.append(" AND o.concept_id IN (" + DrugOrderExportUtil.gpGetARVConceptIds() + ") AND ");
//			strBuffer.append(patientAttributeStr(gender, minAge, maxAge, minBirthdate, maxBirthdate));
			
				strBuffer.append(" select distinct pg.patient_id from patient_program pg ");
				strBuffer.append( "inner join person pe on pg.patient_id = pe.person_id ");
				strBuffer.append( "inner join patient pa on pg.patient_id = pa.patient_id ");
				strBuffer.append("inner join orders ord on pg.patient_id = ord.patient_id ");
				strBuffer.append( "where ((pg.date_completed is null) or(cast(pg.date_completed as DATE)> ' " + df.format(endDate) + " ')) "	);	     
	    		strBuffer.append(" AND o.concept_id IN (" + getStringFromIds(listDrugsConceptsIds) + ")");
	    		strBuffer.append(" AND o.concept_id IN (" + DrugOrderExportUtil.gpGetARVConceptIds() + ")");
	    		strBuffer.append( " and pg.voided = 0 and pe.voided = 0 and ord.voided = 0 ");
	    		strBuffer.append("and pa.voided = 0 and (cast(ord.start_date as DATE)) <= '" + df.format(endDate) + "' and pg.program_id=2 ");
	    		strBuffer.append( " and pg.date_enrolled <= '" + df.format(endDate) + "' ");
	    		strBuffer.append(patientAttributeStr(gender, minAge, maxAge, minBirthdate, maxBirthdate));
			
			}
			
			
			query = sessionFactory.getCurrentSession().createSQLQuery(strBuffer.toString());
			
			records = query.list();
			
			} else if (val.equals("none")) {
			if (gender.equals("") && minAge == null && maxAge == null && minBirthdate == null && maxBirthdate == null) {
			
			strBuffer.append("SELECT DISTINCT pt.patient_id from patient pt ");
			strBuffer.append(" INNER JOIN orders os on os.patient_id = pt.patient_id and pt.voided=0 and pt.patient_id NOT IN( ");
			strBuffer.append(" select distinct pg.patient_id from patient_program pg ");
			strBuffer.append( "inner join person pe on pg.patient_id = pe.person_id ");
			strBuffer.append( "inner join patient pa on pg.patient_id = pa.patient_id ");
			strBuffer.append("inner join orders ord on pg.patient_id = ord.patient_id ");
			strBuffer.append( "where ((pg.date_completed is null) or(cast(pg.date_completed as DATE)> ' " + df.format(endDate) + " ')) "	);	      
	       
    		strBuffer.append(" AND o.concept_id IN (" + getStringFromIds(listDrugsConceptsIds) + ")");
    		strBuffer.append(" AND o.concept_id IN (" + DrugOrderExportUtil.gpGetARVConceptIds() + ")");
    		strBuffer.append( " and pg.voided = 0 and pe.voided = 0 and ord.voided = 0 ");
    		strBuffer.append("and pa.voided = 0 and (cast(ord.start_date as DATE)) <= '" + df.format(endDate) + "' and pg.program_id=2 ");
    		strBuffer.append( " and pg.date_enrolled <= '" + df.format(endDate) + "'");
			strBuffer.append(")");
			
			
			query = sessionFactory.getCurrentSession().createSQLQuery(strBuffer.toString());
			
			} else {
			strBuffer.append("SELECT DISTINCT pt.patient_id from patient pt ");
			strBuffer.append(" INNER JOIN orders os on os.patient_id = pt.patient_id and pt.voided=0 and pt.patient_id NOT IN( ");
			strBuffer.append(" select distinct pg.patient_id from patient_program pg ");
			strBuffer.append( "inner join person pe on pg.patient_id = pe.person_id ");
			strBuffer.append( "inner join patient pa on pg.patient_id = pa.patient_id ");
			strBuffer.append("inner join orders ord on pg.patient_id = ord.patient_id ");
			strBuffer.append( "where ((pg.date_completed is null) or(cast(pg.date_completed as DATE)> ' " + df.format(endDate) + " ')) "	);	      
	       
    		strBuffer.append(" AND o.concept_id IN (" + getStringFromIds(listDrugsConceptsIds) + ")");
    		strBuffer.append(" AND o.concept_id IN (" + DrugOrderExportUtil.gpGetARVConceptIds() + ")");
    		strBuffer.append( " and pg.voided = 0 and pe.voided = 0 and ord.voided = 0 ");
    		strBuffer.append("and pa.voided = 0 and (cast(ord.start_date as DATE)) <= '" + df.format(endDate) + "' and pg.program_id=2 ");
    		strBuffer.append( " and pg.date_enrolled <= '" + df.format(endDate) + "'");
			strBuffer.append(" AND pt.patient_id IN(SELECT p.person_id FROM person p WHERE ");
			strBuffer.append(patientAttributeStr(gender, minAge, maxAge, minBirthdate, maxBirthdate) + ")" );
			strBuffer.append(")");
			
			
			query = sessionFactory.getCurrentSession().createSQLQuery(strBuffer.toString());
			
			}
			
			records = query.list();
			
			} else if (val.equals("all")) {
			if (gender.equals("") && minAge == null && maxAge == null && minBirthdate == null && maxBirthdate == null) {
//			strBuffer.append("SELECT DISTINCT o.patient_id FROM orders o WHERE o.patient_id in(" );
//			strBuffer.append("SELECT DISTINCT p.patient_id FROM patient p ");
//			strBuffer.append(" INNER JOIN program gr INNER JOIN patient_program pg on pg.patient_id=p.patient_id and gr.program_id=2 ");
//			strBuffer.append("INNER JOIN orders o ON p.patient_id = o.patient_id AND o.voided=0 AND p.voided=0 ");
//			strBuffer.append( checkInputDate(startDate, endDate));
//			strBuffer.append(" AND o.concept_id IN (" + getStringFromIds(listDrugsConceptsIds) + ")");
//			strBuffer.append(" AND o.concept_id IN (" + DrugOrderExportUtil.gpGetARVConceptIds() + "))");
				
				strBuffer.append(" select distinct pg.patient_id from patient_program pg ");
				strBuffer.append( "inner join person pe on pg.patient_id = pe.person_id ");
				strBuffer.append( "inner join patient pa on pg.patient_id = pa.patient_id ");
				strBuffer.append("inner join orders ord on pg.patient_id = ord.patient_id ");
				strBuffer.append( "where ((pg.date_completed is null) or(cast(pg.date_completed as DATE)> ' " + df.format(endDate) + " ')) "	);	      
		       
	    		strBuffer.append(" AND o.concept_id IN (" + getStringFromIds(listDrugsConceptsIds) + ")");
	    		strBuffer.append(" AND o.concept_id IN (" + DrugOrderExportUtil.gpGetARVConceptIds() + ")");
	    		strBuffer.append( " and pg.voided = 0 and pe.voided = 0 and ord.voided = 0 ");
	    		strBuffer.append("and pa.voided = 0 and (cast(ord.start_date as DATE)) <= '" + df.format(endDate) + "' and pg.program_id=2 ");
	    		strBuffer.append( " and pg.date_enrolled <= '" + df.format(endDate) + "'");
			
			query = sessionFactory.getCurrentSession().createSQLQuery(strBuffer.toString());
			
			} else {
			
//			strBuffer.append("SELECT DISTINCT o.patient_id FROM orders o WHERE o.patient_id IN(" );
//			strBuffer.append(" SELECT DISTINCT p.patient_id FROM patient p ");
//			strBuffer.append(" INNER JOIN program gr INNER JOIN patient_program pg on pg.patient_id=p.patient_id and gr.program_id=2   ");
//			strBuffer.append(" INNER JOIN orders o ON p.patient_id = o.patient_id AND o.voided=0 AND p.voided=0  ");
//			strBuffer.append( checkInputDate(startDate, endDate));
//			strBuffer.append(" AND o.concept_id IN (" + getStringFromIds(listDrugsConceptsIds) + ")");
//			strBuffer.append(" AND o.concept_id IN (" + DrugOrderExportUtil.gpGetARVConceptIds() + ")");
//			strBuffer.append(" AND o.patient_id in(SELECT  DISTINCT p.patient_id FROM patient p WHERE p.patient_id  ");
//			strBuffer.append("IN(SELECT p.person_id FROM person p WHERE ");
//			strBuffer.append(patientAttributeStr(gender, minAge, maxAge, minBirthdate, maxBirthdate) + ")))");
				
				strBuffer.append(" select distinct pg.patient_id from patient_program pg ");
				strBuffer.append( "inner join person pe on pg.patient_id = pe.person_id ");
				strBuffer.append( "inner join patient pa on pg.patient_id = pa.patient_id ");
				strBuffer.append("inner join orders ord on pg.patient_id = ord.patient_id ");
				strBuffer.append( "where ((pg.date_completed is null) or(cast(pg.date_completed as DATE)> ' " + df.format(endDate) + " ')) "	);	      
		       
	    		strBuffer.append(" AND o.concept_id IN (" + getStringFromIds(listDrugsConceptsIds) + ")");
	    		strBuffer.append(" AND o.concept_id IN (" + DrugOrderExportUtil.gpGetARVConceptIds() + ")");
	    		strBuffer.append( " and pg.voided = 0 and pe.voided = 0 and ord.voided = 0 ");
	    		strBuffer.append("and pa.voided = 0 and (cast(ord.start_date as DATE)) <= '" + df.format(endDate) + "' and pg.program_id=2 ");
	    		strBuffer.append( " and pg.date_enrolled <= '" + df.format(endDate) + "'");
	    		strBuffer.append(" AND o.patient_id in(SELECT  DISTINCT p.patient_id FROM patient p WHERE p.patient_id  ");
				strBuffer.append("IN(SELECT p.person_id FROM person p WHERE ");
				strBuffer.append(patientAttributeStr(gender, minAge, maxAge, minBirthdate, maxBirthdate) + ")))");
			
			
			query = sessionFactory.getCurrentSession().createSQLQuery(strBuffer.toString());
			}
			
			
			List<Integer> patientIds = query.list();
			
			records = getPatientOnAllDrugsByConceptIds(listDrugsConceptsIds, patientIds);
			
			}
			
			return records;
			
			}


	@Override
	public List<Integer> getActiveOnDrugsPatients(List<Integer> patients,String list,Date endDate) {
		Session session = getSessionFactory().getCurrentSession();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		SQLQuery query = session.createSQLQuery("select distinct pg.patient_id from patient_program pg "
				+ "inner join person pe on pg.patient_id = pe.person_id "
				+ "inner join patient pa on pg.patient_id = pa.patient_id "
				+ "inner join orders ord on pg.patient_id = ord.patient_id "
				+ "where ((pg.date_completed is null) or (pg.date_completed > '" + df.format(endDate)	+ "'))"
		        + " and ord.concept_id IN ("
		        + list
		        + ") and pg.voided = 0 and pe.voided = 0 and ord.voided = 0 "
		        + " and pg.date_enrolled <= '" + df.format(endDate)
		        + "' and pa.voided = 0 and (cast(ord.start_date as DATE)) <= '" + df.format(endDate)
		        + "' and pg.program_id= 2 ");
		List<Integer> activePatients = query.list();
		List<Integer> res = new ArrayList<Integer>();
		for (Integer id : activePatients) {
			if(patients.contains(id))
				res.add(id);
		}
		return res;
	}

	@Override
	public List<Integer> getPatientsOnFirstLineRegActive(Date startdate,
			Date enddate, String gender, Date minAge, Date maxAge,
			Date minBirthdate, Date maxBirthdate) {
		// TODO Auto-generated method stub
		return null;
	}


	
	
}
