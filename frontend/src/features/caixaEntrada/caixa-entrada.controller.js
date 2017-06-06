var CaixaEntradaController = function($scope, $rootScope, $location) {

	var caixaEntrada = this;

	caixaEntrada.getDiasRestantes = getDiasRestantes;
	caixaEntrada.isPrazoMinimoAvisoAnalise = isPrazoMinimoAvisoAnalise;
	caixaEntrada.iniciarAnalise = iniciarAnalise;
	caixaEntrada.perfis = app.utils.Perfis;

	function calcularDiasRestantes(stringDate){

		return moment(stringDate, 'DD/MM/yyyy').startOf('day')
			.diff(moment(Date.now()).startOf('day'), 'days');		
	}

	function getDiasRestantes(dataVencimento){

		var diasRestantes = calcularDiasRestantes(dataVencimento);

		return diasRestantes >=0 ? diasRestantes : Math.abs(diasRestantes) + ' dia(s) atraso';
	}

	function isPrazoMinimoAvisoAnalise(dataVencimento, prazoMinimo) {

		return calcularDiasRestantes(dataVencimento) <= prazoMinimo; 
	}	

	function iniciarAnalise(idProcesso) {

		$location.path('/analise-juridica/' + idProcesso.toString());
	}

};

exports.controllers.CaixaEntradaController = CaixaEntradaController;