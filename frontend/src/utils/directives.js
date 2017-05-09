var directives = function (module) {

	var hub = {
		add: function (nome, controller, config) {
			
			module.directive(nome, function () {
				return angular.extend({
					restrict: 'A'
				}, config || {}, {
					controller: controller,
					controllerAs: nome
				});
			});

			return hub;
		}
	};

	return hub;
};

app.utils.directives = directives;