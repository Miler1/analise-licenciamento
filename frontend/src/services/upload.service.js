var UploadService = function(request, config, Upload) {

	this.save = function(file) {

		return Upload.upload({

            url: config.BASE_URL() + "upload/save",
            data: { file : file }
        });
    };

    this.uploadShape = function(file) {

        return request.upload(config.BASE_URL() + 'upload/shape', file, Upload);
    };

	this.deleteShape = function(nameFile) {

		return request
			.delete(config.BASE_URL() + "delete/shape", { key: nameFile }, 'documentacao', true);
	};

};

exports.services.UploadService = UploadService;