var services = function (module) {

	var hub = {
		add: function (nome, service) {
			
			module.service(nome, service);

			return hub;
		}
	};

	return hub;
};

exports.utils.services = services;