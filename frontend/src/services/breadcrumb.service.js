var BreadcrumbService = function($rootScope) {

	this.set = function(itens) {
		$rootScope.itensBreadcrumb = itens;
	};

};

exports.services.BreadcrumbService = BreadcrumbService;