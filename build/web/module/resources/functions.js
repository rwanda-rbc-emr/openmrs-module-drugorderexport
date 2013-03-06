//validates that the entry is a positive or negative number
function isNumber(elem) {
	var str = elem.value;
	//Regex regex = new Regex("^\d$");
	var re   = /^[0-99]+$/;
	str = str.toString();
	if (!str.match(re)) {
		return false;
	}
	return true;
}

function isDate(sDate) {
	var scratch = new Date(sDate);
	if (scratch.toString() != "NaN" || scratch.toString() != "Invalid Date") {
		return true;
	}
}

function dateFormat(date) {
	var str = date.value;
	var re = /^\d{1,2}\/\d{1,2}\/\d{4}$/;
	str = str.toString();
	if (str.match(re))
		return true;
}

function compareDates(maxDate, minDate) {
	var maxD;
	var minD;
	if (isDate(maxDate) && isDate(minDate)) {
		maxD = new Date(maxDate);
		minD = new Date(minDate);
		if (minD.getTime() < maxD.getTime() ) {
			return true;
		} 

	}
	else {
		return false;
	}
	return true;
}


function compareAges(minAge, maxAge) {
	if(isNumber(minAge)&&isNumber(maxAge)){
		if (minAge < maxAge)
			return true;
	}

}