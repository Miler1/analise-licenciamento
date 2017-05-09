var BreadcrumbController = function($rootScope) {

	var breadcrumb = this;

	breadcrumb.itens = [];

	$rootScope.$watch('itensBreadcrumb', function(itens) {
		breadcrumb.itens = itens;
	});

};

exports.controllers.BreadcrumbController = BreadcrumbController;