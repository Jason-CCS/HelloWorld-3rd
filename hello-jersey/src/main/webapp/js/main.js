var href = window.location.href;
var port = window.location.port;

function homePage() {
	var path = window.location.pathname.replace("login", "");
	var home = window.location.origin + path;
	window.location = home;
	// window.location=window.location.origin;
}

function check(acc, pwd) {
	if ("" == acc.value) {
		alert("Please Enter Account ! ");
		return false;
	} else {
		if ("" == pwd.value) {
			alert("Please Enter Password !");
			return false;
		}
	}
	return true;
}

function actionURL(form) {
	var action = href + "login";
	form.form.action = action;
	form.form.submit();
}
