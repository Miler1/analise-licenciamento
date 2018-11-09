var UploadService = function(request, config, Upload) {

	this.save = function(file) {

		return Upload.upload({

            url: config.BASE_URL() + "upload/save",
            data: { file : file }
        });
    };

    this.saveShape = function(file) {

        return request.upload(config.BASE_URL() + 'upload/shape', file, Upload);
    };

	this.removeShape = function(nameFile) {

		return request
			.delete(config.BASE_URL() + "upload/shape/" + nameFile);
	};

};

exports.services.UploadService = UploadService;