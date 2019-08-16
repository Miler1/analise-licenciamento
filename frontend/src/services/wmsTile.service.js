var WMSTileService = function($rootScope, config) {

    this.novoTile = function(satelite, layer, minZoom, maxZoom, filtro, styles, noCache) {

        var geoserver = config.BASE_URL_GEOSERVER + '/wms';

        var layers = satelite ? layer + ',' + satelite + '_grade' : layer;

        var tile = new L.tileLayer.wms(geoserver, {
            layers: layers,
            format: 'image/png',
            transparent: true,
            version: '1.3.0',
            tiled: true,
            minZoom: minZoom,
            maxZoom: maxZoom,
            maxNativeZoom: maxZoom,
            detectRetina:true
        });

        if(filtro) {

            tile.setParams({CQL_FILTER:filtro});
        }

        if(styles) {

            tile.setParams({styles: styles});
        }

        var imgLegenda = geoserver +
            encodeURI('?REQUEST=GetLegendGraphic&' +
                'VERSION=1.3.0&FORMAT=image/png&transparent=true' +
                '&WIDTH=12&HEIGHT=12&SCALE=4752970&LAYER=' + tile.options.layers);

        tile.legend = $('<img/>', {
            'src': imgLegenda
        });

        if(noCache) {

            tile.on('tileloadstart', function (e) {

                if (e.target && e.target.wmsParams) {
                    e.target.wmsParams.tick = new Date().getTime();
                }
            });
        }

        return tile;
    };
};

exports.services.WMSTileService = WMSTileService;