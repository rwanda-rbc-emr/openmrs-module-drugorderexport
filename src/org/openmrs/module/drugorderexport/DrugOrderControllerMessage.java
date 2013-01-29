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


/**
 *
 */
public class DrugOrderControllerMessage {

	protected String anyAllNone;
	protected String drugIds = "";
	protected String gender;
	protected String minBirthdate = null;
	protected String maxBirthdate = null;
	protected String minAge = null;
	protected String maxAge = null;
	protected String startDate = null;
	protected String endDate = null;
	protected String checkedValue="";
	
    public String getAnyAllNone() {
    	return anyAllNone;
    }
	
    public void setAnyAllNone(String anyAllNone) {
    	this.anyAllNone = anyAllNone;
    }
	
    public String getDrugIds() {
    	return drugIds;
    }
	
    public void setDrugIds(String drugIds) {
    	this.drugIds = drugIds;
    }
	
    public String getGender() {
    	return gender;
    }
	
    public void setGender(String gender) {
    	this.gender = gender;
    }
	
    public String getMinBirthdate() {
    	return minBirthdate;
    }
	
    public void setMinBirthdate(String minBirthdate) {
    	this.minBirthdate = minBirthdate;
    }
	
    public String getMaxBirthdate() {
    	return maxBirthdate;
    }
	
    public void setMaxBirthdate(String maxBirthdate) {
    	this.maxBirthdate = maxBirthdate;
    }
	
    public String getMinAge() {
    	return minAge;
    }
	
    public void setMinAge(String minAge) {
    	this.minAge = minAge;
    }
	
    public String getMaxAge() {
    	return maxAge;
    }
	
    public void setMaxAge(String maxAge) {
    	this.maxAge = maxAge;
    }
	
    public String getStartDate() {
    	return startDate;
    }
	
    public void setStartDate(String startDate) {
    	this.startDate = startDate;
    }
	
    public String getEndDate() {
    	return endDate;
    }
	
    public void setEndDate(String endDate) {
    	this.endDate = endDate;
    }
	
    public String getCheckedValue() {
    	return checkedValue;
    }
	
    public void setCheckedValue(String checkedValue) {
    	this.checkedValue = checkedValue;
    }
	
   
	
	
}
