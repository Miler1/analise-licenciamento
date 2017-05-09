var components = function (module) {

	var hub = {
		add: function (nome, component) {
			
			module.component(nome, component);

			return hub;
		}
	};

	return hub;
};

exports.utils.components = components;