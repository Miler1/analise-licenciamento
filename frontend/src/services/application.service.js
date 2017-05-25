var ApplicationService = function(request, config) {

	this.findInfo = function(successCallback) {

		request.get(config.BASE_URL() + "aplicacao/info").then(successCallback);

	};

};

exports.services.ApplicationService = ApplicationService;
