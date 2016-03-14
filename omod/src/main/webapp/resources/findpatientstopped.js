function checkform(form) {
	var message = new String();
	// age fields
	if (form.minAge.value != "" || form.maxAge.value != "") {

		if (form.minAge.value != "" && !isNumber(form.minAge)) {
			message += "* Minimum Age must be numeric.\n";
		}
		if (form.maxAge.value != "" && !isNumber(form.maxAge)) {
			message += "* Maximum Age must be numeric.\n";
		}

		if (form.minAge.value < 0 || form.maxAge.value < 0) {
			message += "* Age can't be negative.\n";
		}

		if (form.minAge.value != "" && form.maxAge.value != "" ) {
//			if(isNumber(form.minAge) && isNumber(form.maxAge) )
//			if (!compareAges(form.minAge.value, form.maxAge.value)) {
//				message += "* Minimum Age, it can't be greater than maximum Age\n";
//			}

			if (form.minAge.value > 120 || form.maxAge.value > 120)
				message += "* Age is to high can't be greater than 120.\n";

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

	// start and end date field
	if (form.startdate.value != "" || form.enddate.value != "") {
		if (form.startdate.value != "" && form.enddate.value != "") {
			if (dateFormat(form.startdate) && dateFormat(form.enddate)) {
				if (!compareDates(form.enddate.value, form.startdate.value))
					message += "* Start Date has to be less than the End Date.\n";
			} else {
				message += "* Invalid date (Start Date or End Date).\n";
			}
		}
		if (form.startdate.value != "" && form.enddate.value == "") {
			if (!dateFormat(form.startdate))
				message += "* Invalid date (Start Date).\n";
		}
		if (form.startdate.value == "" && form.enddate.value != "") {
			if (!dateFormat(form.enddate))
				message += "* Invalid date (End Date).\n";
		}
	}

	// after checking
	if (message.length > 0) {
		alert(message);
		return false;
	}
	return true;

}