var UploadService = function(request, config, Upload) {

	this.save = function(file) {

		return Upload.upload({

            url: config.BASE_URL() + "upload/save",
            data: { file : file }
        });
    };

    this.saveShape = function(file) {

        return Upload.upload({

            url: config.BASE_URL() + 'upload/shape',
            data: { file: file }
        });
    };

	this.removeShape = function(path) {

		return request
			.delete(config.BASE_URL() + "upload/shape", path);
	};

};

exports.services.UploadService = UploadService;