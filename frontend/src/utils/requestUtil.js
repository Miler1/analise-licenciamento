var Request = function($http, animacaoLoader) {

	this._$http = $http;

	this.requests = [];
	this.animacao = animacaoLoader;
};
var i = 0;

Request.prototype._finally = function(http, url) {

	var that = this;

	http['finally'](function(){
		that._unBlock(url);
	});

};

Request.prototype._block = function(requestLoader) {

	if (i === 0) {
		
		this.preloader = new this.animacao.GSPreloader({
			parent: requestLoader.elemento,
		});
		
		requestLoader.preloader = this.preloader;
		
		this.requests.unshift(requestLoader);
		
		this.animacao.openPreloader(requestLoader);
	}
	i++;
};


Request.prototype._unBlock = function(url) {
	i--;
	
	if (i === 0){
		var elemPreloader = this.requests[0];

		this.animacao.closePreloader(elemPreloader);

		this.requests = _.pull(this.requests, elemPreloader);
	}


};

Request.prototype.load = function(url, elem) {

	var elemento = elem ? elem[0] : $('#loader')[0];

	if(elemento){

		var requestLoader = {
			elemento: elemento,
			url: url
		};

		this._block(requestLoader);

	}

};

Request.prototype.get = function(url, params, elem, comLoad) {

	if(comLoad === null || comLoad === undefined)
		comLoad = true;

	var http = this._$http({
		url: url,
		method: 'GET',
		params: params
	});

	if(comLoad) {
		this.load(url, elem);
		this._finally(http, url);
	}

	return http;

};

Request.prototype.getWithCache = function(url, params, elem, comLoad) {

	if(comLoad === null || comLoad === undefined)
		comLoad = true;

	var http =	this._$http({
		url: url,
		method: 'GET',
		cache: true,
		params: params
	});

	if(comLoad) {
		this.load(url, elem);
		this._finally(http, url);
	}

	return http;

};

Request.prototype.getWithoutBlock = function(url, params) {

	var http =	this._$http({
		url: url,
		method: 'GET',
		cache: true,
		params: params
	});

	this._finally(http);

	return http;

};

Request.prototype.post = function(url, params, elem, comLoad) {

	if(comLoad === null || comLoad === undefined)
		comLoad = true;

	var http = this._$http({
		url:  url,
		method: 'POST',
		cache: false,
		data: params,
		headers: {'Content-Type': 'application/json'}
	});

	if(comLoad) {
		this.load(url, elem);
		this._finally(http, url);
	}

	return http;

};

Request.prototype.postAsUrlEncoded = function(url, params, elem, comLoad) {

	if(comLoad === null || comLoad === undefined)
		comLoad = true;

	var http = this._$http({
		url:  url,
		method: 'POST',
		cache: false,
		data: $.param(params),
		headers: {'Content-Type': 'application/x-www-form-urlencoded'}
	});

	if(comLoad) {
		this.load(url, elem);
		this._finally(http, url);
	}

	return http;

};


Request.prototype.delete = function(url, params, elem, comLoad) {

	if(comLoad === null || comLoad === undefined)
		comLoad = true;

	var http = this._$http({
		url:  url,
		method: 'DELETE',
		cache: false,
		data: params
	});

	if(comLoad) {
		this.load(url, elem);
		this._finally(http, url);
	}

	return http;

};

Request.prototype.put = function(url, params, elem, comLoad) {

	if(comLoad === null || comLoad === undefined)
		comLoad = true;

	var http = this._$http({
		url:  url,
		method: 'PUT',
		cache: false,
		data: params,
		headers: {'Content-Type': 'application/json'}
	});

	if(comLoad) {
		this.load(url, elem);
		this._finally(http, url);
	}

	return http;

};

exports.services.Request = Request;