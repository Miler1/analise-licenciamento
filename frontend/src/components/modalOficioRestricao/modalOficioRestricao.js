var ModalOficioRestricao = {

    bindings: {

        resolve: '<',
        close: '&',
        dismiss: '&'
    },

    controller: function(documentoService) {

        var ctrl = this;

        ctrl.$onInit =  function() {

            ctrl.restricao = ctrl.resolve.restricao;
            console.log(ctrl.restricao.item);
        };

        ctrl.baixarDocumento = function (documento) {
            documentoService.download(documento.key, documento.nomeDoArquivo);
        };
    },
    templateUrl: 'components/modalOficioRestricao/modalOficioRestricao.html'
};

exports.directives.ModalOficioRestricao = ModalOficioRestricao;