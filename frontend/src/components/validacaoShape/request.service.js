var blocks = 0;

var Request = function($http, Upload, $rootScope) {

	this._$http = $http;
	this._Upload = Upload;
	this._Uploading = null;

	if($rootScope && $rootScope.urlBase) {
        this._urlBase = $rootScope.urlBase;
    }
    else {
        this._urlBase = '';
	}

    $.blockUI.defaults.baseZ = 4000;
};

Request.prototype._block = function() {

	$.blockUI({
		message: '<div class="loading"> <div class="loading-ring"></div><div class="logo"></div><div class="logo-fill"></div></div>',
		css: {
			padding:	0,
			margin:		0,
			top:		'0',
			left:		'0',
			textAlign:	'center',
			color:		'#000',
			border:		'none',
			backgroundColor:'transparent',
			cursor:		'wait'
		}
	});

	blocks++;
};

Request.prototype._unblock = function() {

	blocks--;

	if(blocks <= 0){
		$.unblockUI();
	}

};

Request.prototype.get = function(url, params) {

	this._block();

	var http =	this._$http({
		url: this._urlBase + url,
		method: 'GET',
		params: params
	});

	http['finally'](this._unblock);

	return http;

};

Request.prototype.getWithCache = function(url, params) {

	this._block();

	var http =	this._$http({
		url: this._urlBase + url,
		method: 'GET',
		cache: true,
		params: params,
	});

	http['finally'](this._unblock);

	return http;

};

Request.prototype.getWithoutBlock = function(url, params, cache) {

	var http =	this._$http({
		url: this._urlBase + url,
		method: 'GET',
		cache: (cache !== undefined ? cache : true),
		params: params,
	});

	return http;

};

Request.prototype.post = function(url, params) {

	this._block();

	var http = this._$http({

		url:  this._urlBase + url,
		method: 'POST',
		cache: false,
		data: $.param(params),
		headers: {'Content-Type': 'application/x-www-form-urlencoded'}
	});

	http['finally'](this._unblock);
	
	return http;

};

Request.prototype.postWithoutBlock = function(url, params) {

	var http = this._$http({

		url:  this._urlBase + url,
		method: 'POST',
		cache: false,
		data: $.param(params),
		headers: {'Content-Type': 'application/x-www-form-urlencoded'}
	});

	return http;

};


Request.prototype.postAsJson = function(url, params) {

	this._block();

	var http = this._$http({

		url:  this._urlBase + url,
		method: 'POST',
		cache: false,
		data: params,
		headers: {'Content-Type': 'application/json'}
	});

	http['finally'](this._unblock);

	return http;

};

Request.prototype.uploadWithBlock = function(arquivo, url){

    this._block();

    var upload = this._Upload.upload({
        url: this._urlBase + url,
        data: {file: arquivo}
    });

    upload['finally'](this._unblock);

    return upload;
};

Request.prototype.upload = function(arquivo, url){


    var upload = this._Upload.upload({
        url: this._urlBase + url,
        data: {file: arquivo}
    });

	this._Uploading = upload;

    return upload;
};

Request.prototype.abortUpload = function() {

	if(this._Uploading) {
        this._Uploading.abort();
    }
};

exports.utils.Request = Request;