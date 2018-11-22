var shapefile = function(mensagem) {


	this.shapefileToGeojson = function(uploadedFile, objetoSaida, callback) {

		var fileType = ['zip', 'gpx', 'kml'],
			fileExtension = this.getFileExtension(uploadedFile.name),
			fileReader = new FileReader(),
			extensionIsValid = false;

		for(var i = 0; i < fileType.length; i++) {

			if (fileType[i] === fileExtension) {

				extensionIsValid = true;
			}
		}

		if (!extensionIsValid) {

			mensagem.error('Esta funcionalidade suporta apenas arquivos do tipo .ZIP, .KML e .GPX');
			return false;
		}

		if(fileExtension === 'zip') {

			fileReader.readAsArrayBuffer(uploadedFile);

		} else {

			fileReader.readAsText(uploadedFile);
		}

		fileReader.onload = function() {

			switch(fileExtension) {

				case 'kml':

					callback(app.utils.toGeoJSON.kml($.parseXML(fileReader.result)), objetoSaida);
					break;

				case 'gpx':

					callback(app.utils.toGeoJSON.gpx($.parseXML(fileReader.result)), objetoSaida);
					break;

				case 'zip':

					shp(fileReader.result).then(function(data) {

						if(data instanceof Array) {

							console.log('erro');
							return false;
						}

						callback(data, objetoSaida);

					}.bind(this));
					break;
			}

		}.bind(this);

	};


	this.geojsonToGeometryCollection = function(geojson) {

		var geometryCollection = {type: "GeometryCollection", geometries: []};

		for (var j = 0; j < geojson.features.length; j++) {

			var feature = geojson.features[j];

			geometryCollection.geometries.push(feature.geometry);
		}

		return geometryCollection;
	};

	this.geojsonToArcGIS = function(geojson) {

		return ArcgisToGeojsonUtils.geojsonToArcGIS(geojson);
	};


	this.getFileExtension = function(fileName) {

		var ext = fileName.split('.').pop();

		if(ext === fileName) {

			return '';
		}

		return ext;
	};

};
exports.utils.shapefile = shapefile;