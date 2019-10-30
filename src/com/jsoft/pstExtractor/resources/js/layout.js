$(document).ready(
	function(event)
	{
		setVerticalSpacerHeight();
	});

function setVerticalSpacerHeight()
{
	var hContent =  $('#heading').outerHeight(true) + $('#content-container').outerHeight(true) + $('#footer-container').outerHeight(true);
	$('#v-spacer').height($('html').height() - hContent);
}