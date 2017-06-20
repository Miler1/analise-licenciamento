var LegislacaoController = function($scope, $rootScope) {

	$scope.leis = [];
	$scope.leiSelecionada = "";

	$('#modalLegislacao').on('shown.bs.modal', function(){
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
			var divLei = $('#conteudoLei');
			
			divLei.html('');

			var htmlLei = $("div[title='" + lei.titulo + "']").html();

			// htmlLei = htmlLei.replace(new RegExp($scope.criteria.normalizeText(), 'gi'), '<criteria>'+$scope.criteria+'</criteria>');

			divLei.html( htmlLei );

			$('#modalLegislacao').animate({scrollTop:$('#visualizarLei').position(0).top}, 'slow');

		}

		window.find($scope.criteria);

	};

	$scope.voltarAoTopo = function(){

		$('#modalLegislacao').animate({scrollTop:$('#modalLegislacao').position(0).top}, 'slow');
		
	};

};

exports.controllers.LegislacaoController = LegislacaoController;