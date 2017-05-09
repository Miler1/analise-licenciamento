var Enter = function() {

};

Enter.link = function(scope, element, attrs) {

    element.bind("keydown keypress", function(event) {

        if (event.which === 13) {

            scope.$apply(function() {
                scope.$eval(attrs.enter);
            });

            event.preventDefault();
        }
    });
};

exports.directives.Enter = Enter;