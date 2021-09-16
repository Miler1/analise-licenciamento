var ApplicationService = function(request, config) {

	this.findInfo = function(successCallback) {

		request.get("https://www.sema.ap.gov.br/" + "aplicacao/info").then(successCallback);

	};

	this.login = function(successCallback) {

		request.get("https://www.sema.ap.gov.br" + "/authenticate").then(successCallback);

	};

	this.auth = function(successCallback) {

		request.get("https://www.sema.ap.gov.br/" + "login/getAuthenticatedUser").then(successCallback);

	};

};

exports.services.ApplicationService = ApplicationService;
