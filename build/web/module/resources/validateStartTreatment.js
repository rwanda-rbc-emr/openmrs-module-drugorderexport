var message = new String();
function checkform(form) {
	var message = new String();

	// age fields
	if (form.minAge.value != "" || form.maxAge.value != "") {

		if (form.minAge.value != "" && !isNumber(form.minAge)) {
			message += "* Minimum Age is not numeric.\n";
		}
		if (form.maxAge.value != "" && !isNumber(form.maxAge)) {
			message += "* Maximum Age is not numeric.\n";
		}

		if (form.minAge.value < 0 || form.maxAge.value < 0) {
			message += "* Age can't be negative.\n";
		}

		if (form.minAge.value != "" && form.maxAge.value != "") {
			if (isNumber(form.minAge) && isNumber(form.maxAge)) {
//				if (form.minAge.value > form.maxAge.value) {
//					message += "* Minimum Age can't be greater than maximum Age\n";
//				}

				if (form.minAge.value > 120 || form.maxAge.value > 120) {
					message += "* Age is to high can't be greater than 120.\n";

				}
			}
		}
		if ((form.minAge.value != "" && isNumber(form.minAge))
				&& form.maxAge.value == "") {
			if (form.minAge.value > 120)
				message += "* Age is to high can't be greater than 120.\n";
		}
		if ((form.maxAge.value != "" && isNumber(form.maxAge))
				&& form.minAge.value == "") {
			if (form.maxAge.value > 120)
				message += "* Age is to high can't be greater than 120.\n";
		}
	}

	// dob fields
	if (form.minBirthdate.value != "" || form.maxBirthdate.value != "") {
		if (form.minBirthdate.value != "" && form.maxBirthdate.value != "") {
			if (dateFormat(form.minBirthdate) && dateFormat(form.maxBirthdate)) {
				if (!compareDates(form.maxBirthdate.value,
						form.minBirthdate.value))
					message += "* Minimum DOB has to be less than the Maximum DOB.\n";
			} else {
				message += "* An invalid date format(min and max dob)\n";
			}
		}
		if (form.minBirthdate.value != "" && form.maxBirthdate.value == "") {
			if (!dateFormat(form.minBirthdate))
				message += "* An invalid date(dob minimum).\n";
		}
		if (form.minBirthdate.value == "" && form.maxBirthdate.value != "") {
			if (!dateFormat(form.maxBirthdate))
				message += "* An invalid date(dob max) .\n";
		}

	}
	// start and end dates fields
	if (form.startdate.value != "" || form.enddate.value != "") {
		
		if (form.startdate.value != "" && form.enddate.value == "") {
			if (!dateFormat(form.startdate))
				message += "* An invalid date(start date).\n";
		}
		if (form.startdate.value == "" && form.enddate.value != "") {
			if (!dateFormat(form.enddate))
				message += "* An invalid date(end date) .\n";
		}
		
		if (form.startdate.value != "" && form.enddate.value != "") {
			if (dateFormat(form.startdate) && dateFormat(form.enddate)) {
				if (!compareDates(form.enddate.value, form.startdate.value)) {
					message += "* Invalid dates range(First date must be greater than the second) .\n";
				}
			} else {
				message += "* An invalid range(start and end date)\n";
			}
		}
		

	}

	// after checking
	if (message.length > 0) {
		alert(message);
		return false;
	}
	return true;

}
