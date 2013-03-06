
//	$(document).ready(function(){
//	$("dd:not(:first)").hide();
//	$("dt a").click(function(){
//	$("dd:visible").slideUp("slow");
//	//$(this).parent().next().slideDown("slow");
//	$("dd:hidden").slideDown("slow");
//	return false;
//	});
//	
//	});

////execute only when the whole document is ready
//$(document).ready(function() {
//	
//	// hide all sub heading lists
//	$('#testnav li ul').hide();
//	
//	// add a click handler to the heading links
//	$('#testnav li a').click(function(){
//		// if the current sub heading list is already open
//		if($(this).next('ul:visible').length) {
//			// close the sub heading list
//			$(this).next('ul:visible').slideUp();
//		} else {
//			// close all open sub heading lists
//			$('#testnav li ul:visible').slideUp();
//			// slide open the next list
//			$(this).next('ul').slideToggle('normal');
//		}
//	
//	// return false to stop link following the href
//	return false;
//	});
//});

$(document).ready(function () {
  $('#testnav > li > a').click(function(){
    if ($(this).attr('class') != 'active'){
      $('#testnav li ul').slideUp();
      $(this).next().slideToggle();
      $('#testnav li a').removeClass('active');
      $(this).addClass('active');
    }
  });
});
