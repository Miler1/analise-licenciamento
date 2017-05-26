var CaixaEntradaController = function($scope, $rootScope) {

	var caixaEntrada = this;

	caixaEntrada.getDiasRestantes = getDiasRestantes;
	caixaEntrada.isPrazoMinimoAvisoAnalise = isPrazoMinimoAvisoAnalise;
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

};

exports.controllers.CaixaEntradaController = CaixaEntradaController;