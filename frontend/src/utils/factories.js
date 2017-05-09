var factories = function (module) {

	var hub = {
		add: function (nome, factory) {
			
			module.factory(nome, factory);

			return hub;
		}
	};

	return hub;
};

exports.utils.factories = factories;