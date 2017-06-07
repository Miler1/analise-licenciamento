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
	}
};

exports.utils.DateUtil = DateUtil;