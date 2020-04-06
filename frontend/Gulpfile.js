var gulp = require('gulp');
var jshint = require('gulp-jshint');
var uglify = require('gulp-uglify');
var concat = require('gulp-concat');
var rename = require('gulp-rename');
var less = require('gulp-less');
var pug = require('gulp-pug');
var ngAnnotate = require('gulp-ng-annotate');
var clean = require('gulp-rimraf');
var cleanCSS = require('gulp-clean-css')
var concatCSS = require('gulp-concat-css');
var gutil = require('gulp-util');
var wrapper = require('gulp-wrapper');
var order = require('gulp-order');
var bower = require('gulp-bower');

var DIST_FOLDER = "../backend/public/app",
	BACKEND_FOLDER = "../backend";


var config = {

	src: {
		all:  "src/**/*",
		imgs: "src/images/**",
		less: "src/styles/main.less",
		fonts: "src/fonts/**",
		pdf:  "src/pdf/**/*",
		summernote: "src/summernote/**/*",
		summernoteFonts: "src/summernote/font/*"
	},

	libs: {

		arcGisToGeoJsonUtil: "./node_modules/arcgis-to-geojson-utils/dist/arcgis-to-geojson.js",
		bootstrap: "./bower_components/bootstrap/dist/**",
		angular: [
			"./bower_components/angular/angular.min.js",
			"./bower_components/angular-route/angular-route.min.js",
			"./bower_components/angular-i18n/angular-locale_pt-br.js",
			"./bower_components/angular-bootstrap/ui-bootstrap-tpls.min.js",
			"./bower_components/angular-messages/angular-messages.min.js",
			"./bower_components/angular-br-filters/release/angular-br-filters.min.js",
			"./bower_components/angular-sanitize/angular-sanitize.min.js",
			"./bower_components/angular-ui-select/dist/select.js",
			"./bower_components/angular-ui-select/dist/select.min.css",
			"./bower_components/angular-bootstrap/ui-bootstrap-csp.css"
		],
		jquery: [
			"./bower_components/jquery/dist/jquery.min.js",
			"./bower_components/jquery-mask-plugin/dist/jquery.mask.min.js"
		],
		greensock: "./bower_components/gsap/src/minified/TweenMax.min.js",
		growl: [
			"./bower_components/angular-growl-v2/build/angular-growl.min.js",
			"./bower_components/angular-growl-v2/build/angular-growl.min.css"
		],
		fontawesome: {
			css: "./bower_components/components-font-awesome/css/font-awesome.min.css",
			fonts: "./bower_components/components-font-awesome/fonts/**"
		},
		lodash: "./bower_components/lodash/dist/lodash.min.js",
		blockUI: "./bower_components/blockui/jquery.blockUI.js",
		inputMasks: "./bower_components/angular-input-masks/angular-input-masks-standalone.min.js",
		moment: [
				"./bower_components/moment/min/moment-with-locales.min.js",
				"./bower_components/angular-moment/angular-moment.min.js"
		],
		leaflet: {
			root: [
				"./bower_components/leaflet/dist/leaflet.*",
				"./bower_components/Leaflet.EasyButton/src/easy-button.*",
				"./bower_components/Leaflet.fullscreen/dist/Leaflet.fullscreen.min.js",
				"./bower_components/Leaflet.fullscreen/dist/leaflet.fullscreen.css",
				"./bower_components/Leaflet.fullscreen/dist/fullscreen.png",
				"./bower_components/Leaflet.fullscreen/dist/fullscreen@2x.png",
				"./bower_components/leaflet-graphicscale/dist/Leaflet.GraphicScale.min.*",
				"./bower_components/shp/dist/shp.min.js",
				"./bower_components/leaflet-measure/dist/leaflet-measure.css",
				"./bower_components/leaflet-measure/dist/leaflet-measure.min.js",
				"./bower_components/leaflet.markercluster/dist/leaflet.markercluster.js",
				"./bower_components/leaflet.markercluster/dist/MarkerCluster.css",
				"./bower_components/leaflet.markercluster/dist/MarkerCluster.Default.css"
			],
			images: [
				"./bower_components/leaflet/dist/images/**",
				"./bower_components/leaflet-draw/dist/images/**",
				"./bower_components/leaflet-measure/dist/images/**"
			]
		},
		"ng-file-upload": [
			"./bower_components/ng-file-upload/ng-file-upload.min.js",
			"./bower_components/ng-file-upload/ng-file-upload-shim.min.js"
		],
		textAngular: [

			"./bower_components/textAngular/dist/textAngular-rangy.min.js",
			"./bower_components/textAngular/dist/textAngular-sanitize.min.js",
			"./bower_components/textAngular/dist/textAngular.min.js",
			"./bower_components/textAngular/dist/textAngular.css"
		],
		ngSortable: [

			"./bower_components/ng-sortable/dist/ng-sortable.min.js",
			"./bower_components/ng-sortable/dist/ng-sortable.style.css"
		],

		sideBar: [

			"./bower_components/sidebar-v2/js/leaflet-sidebar.min.js",
			"./bower_components/sidebar-v2/css/leaflet-sidebar.min.css"
		],
		summernote:[
			"./bower_components/angular-summernote/dist/angular-summernote.min.js"
		],
		ngBootbox:[
			"./bower_components/ngBootbox/dist/ngBootbox.min.js"
		],
		bootbox:[
			"./bower_components/bootbox/dist/bootbox.all.min.js",
			"./bower_components/bootbox/dist/bootbox.locales.min.js",
			"./bower_components/bootbox/dist/bootbox.min.js"
		]
	},

	dist: {
		jsMinFile:  "scripts.min.js",
		cssMinFile: "styles.min.css",
		pdfPath:  BACKEND_FOLDER + "/app/views/templates/pdf/",

		rootPath: DIST_FOLDER + "/",
		jsPath:   DIST_FOLDER + "/js",
		htmlPath: DIST_FOLDER + "/",
		cssPath:  DIST_FOLDER + "/css",
		libsPath: DIST_FOLDER + "/libs",

		imgsPath: DIST_FOLDER + "/images",
		fontsPath: DIST_FOLDER + "/fonts",
		summernotePath: DIST_FOLDER + "/summernote",
		summernoteFontsPath: DIST_FOLDER + "/summernote/font"
	}

};

gulp.task('clean-dist', function () {

	return gulp.src(config.dist.rootPath, { read: false })
		.pipe(clean({ force: true }));
});


gulp.task('lint', function() {

	return gulp.src([config.src.all + ".js", "!" + config.src.summernote + ".js"])
		.pipe(jshint())
		.pipe(jshint.reporter('default'))
		.pipe(jshint.reporter('fail'));
});


gulp.task("pug", function() {

	return gulp.src(config.src.all + ".pug")
		.pipe(pug())
		.pipe(gulp.dest(config.dist.htmlPath));

});

gulp.task("summernote", function() {

	gulp.src(config.src.summernote + ".js")
		.pipe(gulp.dest(config.dist.summernotePath));

	gulp.src(config.src.summernote + ".css")
		.pipe(gulp.dest(config.dist.summernotePath))

	gulp.src(config.src.summernoteFonts)
		.pipe(gulp.dest(config.dist.summernoteFontsPath))

});

gulp.task("pdf", function() {

	gulp
		.src(config.src.pdf + ".pug")
		.pipe(pug())
		.pipe(gulp.dest(config.dist.pdfPath));

	gulp.src(config.src.pdf + ".css")
		.pipe(gulp.dest(config.dist.pdfPath))

	return gulp.src(config.src.pdf + ".less")
		.pipe(less())
		.pipe(gulp.dest(config.dist.pdfPath));

});

gulp.task("images", function() {

	return gulp.src(config.src.imgs)
		.pipe(gulp.dest(config.dist.imgsPath));
});

gulp.task("fonts", function() {

	return gulp.src(config.src.fonts)
		.pipe(gulp.dest(config.dist.fontsPath));
});

gulp.task("fonts-bootstrap", function() {

	return gulp.src('./bower_components/bootstrap/fonts/**')
		.pipe(gulp.dest(config.dist.fontsPath));
});

gulp.task('less', function() {

	return gulp.src(config.src.less)
		.pipe(less())
		.pipe(concatCSS(config.dist.cssMinFile))
		.pipe(cleanCSS({compatibility: 'ie8'}))
		.pipe(gulp.dest(config.dist.cssPath));
});


gulp.task('concat', function () {

	return gulp.src('assets/**/*.css')
		.pipe(concatCss("styles/bundle.css"))
		.pipe(gulp.dest('out/'));
});


gulp.task("js", function() {

	return gulp.src([
			config.src.all.slice(0,-1) + "!(*.module|app).js",
			config.src.all + ".module.js",
			"src/app.js"
		])
		.pipe(wrapper({
			header: '!function(exports) {\n/* @ngInject */\n',
			footer: '\n}(function() { this.app = this.app || {controllers:{}, directives:{}, services:{}, factories: {}, filters:{}, utils: {}}; return this.app;}());'
		}))
		.pipe(concat(config.dist.jsPath))
		.pipe(rename(config.dist.jsMinFile))
		.pipe(gulp.dest(config.dist.jsPath));

});

gulp.task("js-dev", function() {

	return gulp.src([
			config.src.all.slice(0,-1) + "!(*.module|app).js",
			config.src.all + ".module.js",
			"src/app.js"
		])
		.pipe(wrapper({
			header: '!function(exports) {\n/* @ngInject */\n',
			footer: '\n}(function() { this.app = this.app || {controllers:{}, directives:{}, services:{}, factories: {}, filters:{}, utils: {}}; return this.app;}());'
		}))
		.pipe(concat(config.dist.jsPath))
		.pipe(rename(config.dist.jsMinFile))
		.pipe(gulp.dest(config.dist.jsPath));
});

gulp.task('bower', function() {
	return bower();
});

var configureLib = function (configValue, distPath) {

	for (var lib in configValue) {

		var libConfig = configValue[lib];

		if (lib === "root") {

			gulp.src(libConfig)
				.pipe(gulp.dest(distPath));

		} else if (libConfig instanceof Array || typeof libConfig === "string") {

			gulp.src(libConfig)
				.pipe(gulp.dest(distPath + "/" + lib));

		} else if (typeof libConfig === "object") {

			configureLib(libConfig, distPath + "/" + lib);
		}
	}
};

gulp.task("libs", function() {

	configureLib(config.libs, config.dist.libsPath);
});


gulp.task('dist',['clean-dist'], function() {

	return gulp.run("bower", "libs", "images", "pug", "less", "js", "fonts", "fonts-bootstrap", "pdf", "summernote");
});

gulp.task('dev', function() {

	return gulp.run("bower", "libs", "images", "pug", "less", "lint", "js-dev", "fonts", "fonts-bootstrap", "pdf", "summernote");
});

gulp.task('default', function() {

	gulp.run('dev');
	return gulp.watch(config.src.all, ['dev']);
});

gulp.task('teste', function () {
	return gulp.run('dist');
});