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

	getDataFormatada: function(data) {
		
		var dateParts = data.split("/");

		dateParts[2] = dateParts[2].split(' ')[0];

		var dateObject = new Date(dateParts[2], dateParts[1] - 1, dateParts[0]);

		return dateObject;

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
	},

	getContaDiasRestantesData:  function(data) { 

		var diferencaTempo = this.getDataFormatada(data).getTime() - new Date().getTime();
		var diasRestantes = (diferencaTempo / (1000 * 3600 * 24));

		if(diasRestantes < 0 && diasRestantes > -1) {
			diasRestantes = 0;
			return diasRestantes.toString();
		} else if(diasRestantes >= 0) {
			diasRestantes++;
		}

		return diasRestantes.toString().substring(0, diasRestantes.toString().indexOf('.'));

	},

	verificaPrazoMinimoData: function (data) {

		var diferencaTempo = this.getDataFormatada(data).getTime() - new Date().getTime();

		return (diferencaTempo / (1000 * 3600 * 24)) <= -1;

	}
};

exports.utils.DateUtil = DateUtil;