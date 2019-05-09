var ApplicationService = function(request, config) {

	this.findInfo = function(successCallback) {

		request.get("http://localhost:9011" + "aplicacao/info").then(successCallback);

	};

	this.login = function(successCallback) {

		request.get("http://localhost:9011" + "/authenticate").then(successCallback);

	};

	this.auth = function(successCallback) {

		request.get("http://localhost:9011" + "login/getAuthenticatedUser").then(successCallback);

	};

};

exports.services.ApplicationService = ApplicationService;
