var MenuPrincipal = {

    bindings: {

        itens: '<'
    },
    controller: function() {

        var ctrl = this;

        this.$onInit = function() {

            ctrl.itens = [{

                titulo: 'Caixa de entrada (novos processos)',
                icone: 'glyphicon glyphicon-inbox',
                url: ''
            }, {

                titulo: 'Aguardando validação',
                icone: 'glyphicon glyphicon-check',
                url: ''
                
            }, {
                titulo: 'Consultar processo',
                icone: 'glyphicon glyphicon-search',
                url: ''                
            }];
        };

        this.irPara = function(url) {

            alert(url);
        };
    },
    templateUrl: 'components/menuPrincipal/menuPrincipal.html'
};

exports.directives.MenuPrincipal = MenuPrincipal;