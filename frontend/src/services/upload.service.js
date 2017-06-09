var UploadService = function(request, config, Upload) {

	this.save = function(file) {

		return Upload.upload({

            url: config.BASE_URL() + "upload/save",
            data: { file : file }
        });
	};

};

exports.services.UploadService = UploadService;