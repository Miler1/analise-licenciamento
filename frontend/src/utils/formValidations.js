var FormValidations = function($scope, name) {

    this.formName = name || "form";
    this.$scope = $scope;
};

var isAngularProperty = function(prop) {

    return prop.startsWith("\$");
};

var isField = function(obj) {

    return obj && obj.hasOwnProperty("$viewValue");
};

FormValidations.prototype.validate = function() {

    this._eachField(function(field){

        field.$setViewValue(field.$viewValue);
    });

    var form = this.$scope[this.formName];

    return form && form.$valid;
};

FormValidations.prototype.clear = function() {

    this._eachField(function(field){

        field.$setPristine();
    });
};

FormValidations.prototype._eachField = function(fn, formObj) {
    
    var form = formObj || this.$scope[this.formName];

    if (!fn || !form)
        return;

    for (var prop in form) {

        if (!isAngularProperty(prop) && form[prop]) {

            var field = form[prop];

            if (isField(field)) {
            
                fn(field);
            
            } else {

                this._eachField(fn, field);
            }
        }
    }
};

exports.utils.FormValidations = FormValidations;