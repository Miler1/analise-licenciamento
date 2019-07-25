var TextEditor = function($scope, $timeout) {

	this.$scope = $scope;

	this.identificador = this.$scope.identificador;

	var that = this;
	$timeout(function(){
		tinymce.init({ 
			selector: '#' + that.identificador,
			language: 'pt_BR',
			language_url: 'public/js/utils/tinymce/pt_BR.js', // jshint ignore:line
			menubar: false,
			plugins: 'table lists',
			visual_table_class: 'table-editor', // jshint ignore:line
			toolbar:'undo redo | styleselect | bold italic | alignleft aligncenter alignright alignjustify | bullist numlist | outdent indent | link image | formatselect fontselect fontsizeselect | table',
			content_css : [ // jshint ignore:line
				'public/css/tinymce.css'
			],
			table_appearance_options: false,// jshint ignore:line
			table_toolbar: 'tabledelete | tableinsertrowbefore tableinsertrowafter tabledeleterow | tableinsertcolbefore tableinsertcolafter tabledeletecol'// jshint ignore:line
		});
	});
	
};

TextEditor.config = {

	restrict: 'E',
	templateUrl: 'layout/components/textEditor.html',
	scope :{
		identificador: '='
	}
};
exports.directives.TextEditor = TextEditor;
