var filters = function (module) {

	var hub = {
		add: function (nome, filter) {
			
			module.filter(nome, filter);

			return hub;
		}
	};

	return hub;
};

exports.utils.filters = filters;