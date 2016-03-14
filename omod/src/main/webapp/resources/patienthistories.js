function checkform(form) {
	var message = new String();

	// age fields
	if (form.minAge.value != "" || form.maxAge.value != "") {

		if (form.minAge.value != "" && !isNumber(form.minAge)) {
			message += "* Minimum Age must be numeric .\n";
		}
		if (form.maxAge.value != "" && !isNumber(form.maxAge)) {
			message += "* Maximum Age must be numeric .\n";
		}

		if (form.minAge.value < 0 || form.maxAge.value < 0) {
			message += "* Age can't be negative .\n";
		}

		if (form.minAge.value > 120 || form.maxAge.value > 120) {
			message += "* Age is to high, it can't be greater than 120.\n";
		}

//		if (form.minAge.value != "" && form.maxAge.value != "") {
//			if(form.minAge.value>form.maxAge.value){
//				message += "* Minimum Age, it can't be greater than maximum Age\n";
//			}
//
//		}

	}

	// dob fields
	if (form.minBirthdate.value != "" || form.maxBirthdate.value != "") {
		// if(form.minBirthdate.value>today || form.maxBirthdate.value()>today){
		// message += "* An invalid date(The date cant be greater than the
		// current date) .\n";
		// }
		
		
		if (form.minBirthdate.value != "" && form.maxBirthdate.value != "") {
			if (dateFormat(form.minBirthdate) && dateFormat(form.maxBirthdate)) {
				if (!compareDates(form.maxBirthdate.value,
						form.minBirthdate.value))
					message += "* Minimum DOB has to be less than the Maximum DOB.\n";
			} else {
				message += "* An invalid date format(min and/or max dob)\n";
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
	// after checking
	if (message.length > 0) {
		alert(message);
		return false;
	}
	return true;

}
