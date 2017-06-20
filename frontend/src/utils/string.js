String.prototype.deixarSomenteNumeros = function() {
	
	return this.toString().replace(/[-_./]/g,"");
};

String.prototype.isPartOfCpfCnpj = function() {

	var regex = /^[0-9./-]+$/;

	return regex.test(this.toString());
};

String.prototype.isCPF = function(){

	var cpf = this.toString().replace(/[-_./]/g,"");
	var soma;
	var resto;
	var i;
	if ((!cpf) || (cpf.length !== 11) ||
		(cpf == "00000000000") || (cpf == "11111111111") ||
		(cpf == "22222222222") || (cpf == "33333333333") ||
		(cpf == "44444444444") || (cpf == "55555555555") ||
		(cpf == "66666666666") || (cpf == "77777777777") ||
		(cpf == "88888888888") || (cpf == "99999999999") ) {
		return false;
	}
	soma = 0;
	for (i = 1; i <= 9; i++) {
		soma += Math.floor(cpf.charAt(i-1)) * (11 - i);
	}
	resto = 11 - (soma - (Math.floor(soma / 11) * 11));
	if ((resto == 10) || (resto == 11)) {
		resto = 0;
	}
	if (resto != Math.floor(cpf.charAt(9))) {
		return false;
	}
	soma = 0;

	for (i = 1; i<=10; i++) {
		soma += cpf.charAt(i-1) * (12 - i);
	}
	resto = 11 - (soma - (Math.floor(soma / 11) * 11));
	if ((resto === 10) || (resto === 11)) {
		resto = 0;
	}
	if (resto != Math.floor(cpf.charAt(10))) {
		return false;
	}
	return true;

};

String.prototype.isCNPJ = function () {

	var cnpj = this.toString().replace(/[-_./]/g,"");
	if (cnpj === null || cnpj === undefined || cnpj.length != 14)
		return false;

	var i;
	var c = cnpj.substr(0,12);
	var dv = cnpj.substr(12,2);
	var d1 = 0;

	for (i = 0; i < 12; i++) {
		d1 += c.charAt(11-i)*(2+(i % 8));
	}

	if (d1 === 0) return false;

	d1 = 11 - (d1 % 11);

	if (d1 > 9) d1 = 0;
	if (dv.charAt(0) != d1) {
		return false;
	}

	d1 *= 2;

	for (i = 0; i < 12; i++) {
		d1 += c.charAt(11-i)*(2+((i+1) % 8));
	}

	d1 = 11 - (d1 % 11);

	if (d1 > 9) d1 = 0;
	if (dv.charAt(1) != d1){
		return false;
	}

	return true;

};

String.prototype.toDate = function() {

	var dateParts = this.split("/");

	return new Date(dateParts[2], dateParts[1] - 1, dateParts[0]);
};

String.prototype.zeroEsquerda = function(length) {

	var numberString = '' + this;
	while (numberString.length < length) {
		numberString = '0' + numberString;
	}

	return numberString;

};

String.prototype.reduceWhiteSpace = function() {
	return this.replace(/\s+/g, ' ');
};

String.prototype.purifyString = function() {
	var r = [];
	for (var i = 0, length = this.length; i < length; i++) {
		r.push(replaceCharacter(this.charAt(i)));
	}
	return r.join("");
};

String.prototype.normalizeText = function() {
	return this.purifyString().reduceWhiteSpace().toLowerCase();
};

function replaceCharacter(character) {
	switch (character) {
		case '\r': return " ";
		case '\n': return " ";
		case '\t': return " ";
		case '\f': return " ";
		case '\v': return " ";
		case '`': return "'";
		case '€': return "_";
		case '‚': return ",";
		case 'ƒ': return "f";
		case '„': return "\"";
		case '…': return "...";
		case '†': return "_";
		case '‡': return "_";
		case 'ˆ': return "^";
		case '‰': return "%";
		case 'Š': return "S";
		case '‹': return "<";
		case 'Œ': return "CE";
		case 'Ž': return "Z";
		case '‘': return "'";
		case '’': return "'";
		case '“': return "\"";
		case '”': return "\"";
		case '•': return "-";
		case '–': return "-";
		case '—': return "-";
		case '˜': return "~";
		case '™': return "(tm)";
		case 'š': return "s";
		case '›': return ">";
		case 'œ': return "ce";
		case 'ž': return "z";
		case 'Ÿ': return "Y";
		case '¡': return "i";
		case '¥': return "Y";
		case '¦': return "|";
		case 'ª': return "a";
		case '¬': return "-";
		case '¯': return "-";
		case '²': return "2";
		case '³': return "3";
		case '´': return "'";
		case '¸': return ",";
		case '¹': return "1";
		case 'º': return "0";
		case '¼': return "1/4";
		case '½': return "1/2";
		case '¾': return "3/4";
		case '¿': return "?";
		case 'À': return "A";
		case 'Á': return "A";
		case 'Â': return "A";
		case 'Ã': return "A";
		case 'Ä': return "A";
		case 'Å': return "A";
		case 'Æ': return "AE";
		case 'Ç': return "C";
		case 'È': return "E";
		case 'É': return "E";
		case 'Ê': return "E";
		case 'Ë': return "E";
		case 'Ì': return "I";
		case 'Í': return "I";
		case 'Î': return "I";
		case 'Ï': return "I";
		case 'Ð': return "D";
		case 'Ñ': return "N";
		case 'Ò': return "O";
		case 'Ó': return "O";
		case 'Ô': return "O";
		case 'Õ': return "O";
		case 'Ö': return "O";
		case '×': return "x";
		case 'Ø': return "O";
		case 'Ù': return "U";
		case 'Ú': return "U";
		case 'Û': return "U";
		case 'Ü': return "U";
		case 'Ý': return "Y";
		case 'ß': return "B";
		case 'à': return "a";
		case 'á': return "a";
		case 'â': return "a";
		case 'ã': return "a";
		case 'ä': return "a";
		case 'å': return "a";
		case 'æ': return "ae";
		case 'ç': return "c";
		case 'è': return "e";
		case 'é': return "e";
		case 'ê': return "e";
		case 'ë': return "e";
		case 'ì': return "i";
		case 'í': return "i";
		case 'î': return "i";
		case 'ï': return "i";
		case 'ñ': return "n";
		case 'ò': return "o";
		case 'ó': return "o";
		case 'ô': return "o";
		case 'õ': return "o";
		case 'ö': return "o";
		case '÷': return "/";
		case 'ø': return "o";
		case 'ù': return "u";
		case 'ú': return "u";
		case 'û': return "u";
		case 'ü': return "u";
		case 'ý': return "y";
		case 'ÿ': return "y";
		case '©': return "(c)";
		case '®': return "(r)";
		default: return character;
	}
}