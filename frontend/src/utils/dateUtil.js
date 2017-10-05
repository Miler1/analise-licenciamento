DateUtil = {

	calcularDiasRestantes: function (stringDate){

        return moment(stringDate, 'DD/MM/yyyy').startOf('day')
            .diff(moment(Date.now()).startOf('day'), 'days');		
    },

	getDiasRestantes: function(dataVencimento){

		var diasRestantes = this.calcularDiasRestantes(dataVencimento);

		return diasRestantes >=0 ? diasRestantes : Math.abs(diasRestantes) + ' dia(s) atraso';
	},

	isPrazoMinimoAvisoAnalise: function(dataVencimento, prazoMinimo) {

		return this.calcularDiasRestantes(dataVencimento) <= prazoMinimo; 
	},

	calcularDias: function(stringInicialDate, stringFinalDate) {

		if (stringFinalDate) {

			return moment(stringFinalDate, 'DD/MM/yyyy').startOf('day')
				.diff(moment(stringInicialDate, 'DD/MM/yyyy').startOf('day'), 'days');
		}

		return this.calcularDiasRestantes(stringInicialDate)*-1;
	},

	getContaDiasRestantes: function(dias, prazo) {

		if(dias == null){
			return ' - ';
		}

		var diasRestantes = prazo - dias ;
		return diasRestantes >= 0 ? diasRestantes : Math.abs(diasRestantes) + ' dia(s) atraso';
	},

	verificaPrazoMinimo: function (dias, prazoMinimo) {

		if(dias >= prazoMinimo)
			return true;
		else
			return false;
	},

	getTodaysStringDate: function(){

		return moment(new Date()).format('DD/MM/YYYY');
	},
	
	formatarData : function(data){

		return moment(data, 'DD/MM/YYYY').format('DD/MM/YYYY');
	},

	calcularPrazoEmAnos: function(dataInicio, dataVencimento) {

		var a = moment(dataVencimento,'DD/MM/YYYY');
		var b = moment(dataInicio, 'DD/MM/YYYY');
		
		return a.diff(b, 'year');
	}
};

exports.utils.DateUtil = DateUtil;