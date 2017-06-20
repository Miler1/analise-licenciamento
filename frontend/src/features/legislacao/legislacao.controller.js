var LegislacaoController = function($scope, $rootScope) {

	$scope.leis = [];
	$scope.leiSelecionada = "";

	$('#modalLegislacao').on('show.bs.modal', function(){
		$scope.modalAberta = true;
		carregarLeis();
		$scope.$apply();
	});

	$('#modalLegislacao').on('hide.bs.modal', function(){
		$scope.leis = [];
		$scope.criteria = undefined;
		$scope.leiSelecionada = "";
		$scope.modalAberta = false;
		$scope.$apply();
	});

	function carregarLeis() {
		var leisDivs = $('#leis').find("div[type='doc']");

		for (var i=0; i < leisDivs.length; i++) {

			var div = $(leisDivs[i]);

			var lei = {
				id: i,
				titulo: div.attr('title'),
				texto: div.text().normalizeText()
			};

			$scope.leis.push(lei);
		}
	}

	$scope.verLei = function(lei) {

		if($scope.leiSelecionada == lei){
			
			$scope.leiSelecionada = undefined;

		}else{

			$scope.leiSelecionada = lei;
			$scope.pesquisarTermo(lei);

			$('#modalLegislacao').animate({scrollTop:$('#visualizarLei').position(0).top}, 'slow');

		}

	};

	$scope.voltarAoTopo = function(){

		$('#modalLegislacao').animate({scrollTop:$('#modalLegislacao').position(0).top}, 'slow');
		
	};

	$scope.pesquisarTermo = function(lei) {

		var divLei = $('#conteudoLei');
		var htmlLei;
		
		if(!lei && divLei.html().length <= 0) {
			return;
		} else if(!lei && divLei.html().length > 0) {
			htmlLei = divLei.html();
		} else {
			htmlLei = $("div[title='" + lei.titulo + "']").html();
		}

		if($scope.criteria && $scope.criteria.length > 3) {
			htmlLei = htmlLei.replace('<highlighted>', '');
			htmlLei = htmlLei.replace('</highlighted>', '');
			htmlLei = htmlLei.replace(new RegExp($scope.criteria, 'gi'), '<highlighted>' + $scope.criteria + '</highlighted>');
		} else {
			htmlLei = htmlLei.replace('<highlighted>', '');
			htmlLei = htmlLei.replace('</highlighted>', '');
		}

		divLei.html('');
		divLei.html(htmlLei);

	};

};

exports.controllers.LegislacaoController = LegislacaoController;