var MenuPrincipal = {

    bindings: {

        itens: '<'
    },
    controller: function($location) {

        var ctrl = this;

        this.$onInit = function() {

        };

        this.irPara = function(url) {

            $location.path(url);
        };
    },
    templateUrl: 'components/menuPrincipal/menuPrincipal.html'
};

exports.directives.MenuPrincipal = MenuPrincipal;