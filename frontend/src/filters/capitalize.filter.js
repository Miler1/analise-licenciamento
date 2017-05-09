var Capitalize = function(){
	
	return function(input) {
		return (!!input) ? input.charAt(0).toUpperCase() + input.substr(1).toLowerCase() : '';
	};

};

exports.filters.Capitalize = Capitalize;