$(document).ready(
	function(event)
	{
		setButtonsEnabled(false);
	});

function setResetButtonEnabled(fEnabled)
{
	document.uploadPSTForm["uploadPSTForm:button-reset"].disabled = !fEnabled;
}

function setSubmitButtonEnabled(fEnabled)
{
	document.uploadPSTForm["uploadPSTForm:button-submit"].disabled = !fEnabled;
}

function setButtonsEnabled(fEnabled)
{
	setSubmitButtonEnabled(fEnabled);
	setResetButtonEnabled(fEnabled);
}

function inputFileChange()
{
	var inp = document.uploadPSTForm["uploadPSTForm:file"];
	setButtonsEnabled(inp.value != "");
}
