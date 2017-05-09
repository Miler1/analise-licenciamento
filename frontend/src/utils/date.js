/**
 * Sobrecarregando método toString do tipo Date para que o Play Framework faça o bind de datas corretamente
 * @returns {string}
 */
Date.prototype.toString = function() {
	return this.toLocaleString('pt-br');
};

Date.prototype.toISOString = function() {
	return this.toString();
};