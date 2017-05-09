var TextoTruncado = function() {
	
	return function(texto, tamanhoInicio, tamanhoFinal) {

		if (!texto){
			return "";
		}

		var inicio = (tamanhoInicio) ? parseInt(tamanhoInicio, 10) : 0;
		var fim = (tamanhoFinal) ? parseInt(tamanhoFinal, 10) : 0;

		var textoInicio = texto.substring(0, inicio);
		var textoFim = texto.substring( texto.length - fim, texto.length );

		if (texto.length === textoInicio.length)
			return textoInicio;
		else
			return textoInicio + " ... " + textoFim;
	};

};

exports.filters.TextoTruncado = TextoTruncado;
