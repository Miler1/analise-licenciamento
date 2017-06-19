var FichaImovel = {

	bindings: {
		imovel: '='
	},

	controller: function(imovelService, $scope, $timeout) {

		var ctrl = this;

		ctrl.openedAccordion = true;

		$timeout(function () {

			imovelService.getImovelDadosCadastrante(ctrl.imovel.idCar)
				.then(function(response){


					$('#ficha-imovel-cadastrante').html(response.data);

				});

			imovelService.getImovelDadosImovel(ctrl.imovel.idCar)
				.then(function(response){


					$('#ficha-imovel-imovel').html(response.data);

				});

			imovelService.getImovelDadosDominio(ctrl.imovel.idCar)
				.then(function(response){


					$('#ficha-imovel-dominio').html(response.data);

				});

			imovelService.getImovelDadosGeo(ctrl.imovel.idCar)
				.then(function(response){

					ctrl.fichaImovelGeo = response.data;

					if(!$scope.$$phase) {

						$scope.$apply();

					}

				}.bind(this));


			imovelService.getImovelDemonstrativo(ctrl.imovel.codigo)
				.then(function(response){


					$('#ficha-imovel-demonstrativo')
						.html(response.data);

				});

		});



		this.baixarDemonstrativo = function(){

			imovelService.baixarDemonstrativo(ctrl.imovel.codigo);

		};

		this.baixarShapefile = function(){

			imovelService.baixarShapefile(ctrl.imovel.codigo);

		};


		this.initMap = function(){

			$timeout(function () {

				$('#ficha-imovel-geo').html(this.fichaImovelGeo);

			}.bind(this), 70);

		};
	},

	templateUrl: 'components/fichaImovel/fichaImovel.html'

};

exports.directives.FichaImovel = FichaImovel;

