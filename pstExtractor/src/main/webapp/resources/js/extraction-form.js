$(document).ready(
	function(event)
	{
//		setButtonsEnabled(false);
//		$("#main\\:uploadPSTForm\\:reset").attr("onclick", "$('#main').trigger('reset')");
//		$("#main\\:uploadPSTForm\\:reset").prop("hash", "sha256-aHhwgjrHMMf/7S7t93sveOaeQi7/lKrgGHc432rJGf4=");
	});

function setResetButtonEnabled(fEnabled)
{
	$("#main\\:uploadPSTForm\\:reset").prop("disabled", !fEnabled);
}

function setSubmitButtonEnabled(fEnabled)
{
	$("#main\\:uploadPSTForm\\:submit").prop("disabled", !fEnabled);
}

function setButtonsEnabled(fEnabled)
{
	setSubmitButtonEnabled(fEnabled);
	setResetButtonEnabled(fEnabled);
}

function inputFileChange()
{
	var inp = document.uploadPSTForm["main:uploadPSTForm:file"];
	setButtonsEnabled(inp.value != "");
}
