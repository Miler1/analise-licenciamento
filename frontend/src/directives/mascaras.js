var Mascara = function() {

};

// Mascaras do sistema
Mascara.link = function(scope, element, attr, modelCtrl){

	/**
	 * As máscaras de datas precisam ser iniciadas junto com o elemento
	 */
	if(attr.mascara){
		if(attr.mascara === 'data'){

			$(element).mask("AB/CD/0000", {
				translation:{
					A: {pattern: /[0-3]/},
					B: {pattern: /[0-9]/},
					C: {pattern: /[0-1]/},
					D: {pattern: /[0-9]/},
				}
			});

		}

	}

	var fromUser = function(inputValue) {

		// this next if is necessary for when using ng-required on your input. 
		// In such cases, when a letter is typed first, this parser will be called
		// again, and the 2nd time, the value will be undefined
		if (!inputValue){
			return '';
		}

		var jqueryElement = $(element);

		switch (attr.mascara) {

			case 'data':

				jqueryElement.mask("AB/CD/0000", {
					translation:{
						A: {pattern: /[0-3]/},
						B: {pattern: /[0-9]/},
						C: {pattern: /[0-1]/},
						D: {pattern: /[0-9]/},
					}, 
					onKeyPress: function(a, b, c, d) {
						if (!a) return;
						var m = a.match(/(\d{2})/g);
						d.translation.B =  (m && m[0] && m[0].charAt(0)=='3') ? { pattern: /[0-1]/ } : { pattern: /[0-9]/ };
						d.translation.D = (m && m[1] && m[1].charAt(0)=='1') ? { pattern: /[0-2]/ } : { pattern: /[0-9]/ };
						c.mask("AB/CD/0000", d);
					}
				}).keyup();

				return jqueryElement[0].value.toDate();

		}

		var transformedInput = jqueryElement[0].value;

		return transformedInput;

	};

	modelCtrl.$parsers.push(fromUser);

};

exports.directives.Mascara = Mascara;