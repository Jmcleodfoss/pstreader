$(document).ready(
	function(event)
	{
		setButtonsEnabled(false);
	});

function setResetButtonEnabled(fEnabled)
{
	$("main.uploadPSTForm.#uploadPSTForm.reset").prop("disabled", !fEnabled);
}

function setSubmitButtonEnabled(fEnabled)
{
	$("#main.ploadPSTForm.uploadPSTForm.submit").prop("disabled", !fEnabled);
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
