/* jshint node:true, jquery:false */
'use strict';

var gulp = require('gulp');
var $$ = require('gulp-load-plugins')({lazy: true});
var eventStream = require('event-stream');
var del = require('del');
var path = require('path');
var runSequence = require('run-sequence');
var useref = require('gulp-useref');
var gulpif = require('gulp-if');
var inject = require('gulp-inject');
var uglify = require('gulp-uglify');
var cssnano = require('gulp-cssnano');
var concat = require('gulp-concat');
var ngHtml2Js = require("gulp-ng-html2js");

gulp.task('clean', function () {
    del(['dist/**', 'temp/**']);
});

gulp.task('analyze', function () {
    var basePath = path.resolve('./target/classes/js/');
    $$.util.log('Analyzing sources in ' + basePath);

    return gulp.src([basePath + '/**/*.js', '!./Content/bower_components/**/*'])
        .pipe($$.jshint())
        .pipe($$.jshint.reporter('jshint-stylish', { verbose: true }))
        .pipe($$.jshint.reporter('fail'));
});

gulp.task('build-dev', function() {
    gulp.src(['target/classes/images/*.png', 'target/classes/images/*.jpg', 'target/classes/images/*.gif'])
            .pipe(gulp.dest('target/dist/images'));

    gulp.src(['target/classes/partials/*.html'])
        .pipe($$.htmlhint({'doctype-first': false}))
        .pipe($$.htmlhint.reporter())
        .pipe(gulp.dest('target/dist/partials'));

    gulp.src(['target/classes/bower_components/**/*'])
        .pipe(gulp.dest('target/dist/bower_components'));

    gulp.src(['target/classes/js/**/*.js'])
        .pipe(gulp.dest('target/dist/js'));

    gulp.src(['target/classes/css/**/*.css'])
        .pipe(gulp.dest('target/dist/css'));

    return gulp.src('target/classes/index.html')
        .pipe($$.debug({title: 'Processed output File: '}))
        .pipe(gulp.dest('target/dist'));
});

gulp.task('release', function() {
    gulp.src(['target/classes/images/*.png', 'target/classes/images/*.jpg', 'target/classes/images/*.gif'])
        .pipe(gulp.dest('target/dist/images'));
    
    var partials = gulp.src(['target/classes/**/*.html', '!target/classes/index.html', '!target/classes/bower_components/**/*.html'])
        .pipe($$.htmlhint({'doctype-first': false}))
        .pipe($$.htmlhint.reporter())
        .pipe($$.htmlmin({ removeComments: true }))
        .pipe(ngHtml2Js({
            moduleName: 'recipesApp'
        }))
        .pipe($$.concat({path: 'template.js', cwd: ''}))
        //.pipe($$.rev())
        .pipe(gulp.dest('target/dist'));

    gulp.src(['target/classes/*.appcache'])
        .pipe(gulp.dest('target/dist'));

    var main = gulp.src('target/classes/*.html')
        .pipe($$.useref())
        //.pipe(gulpif('*.js', $$.uglify()))
        //.pipe(gulpif('*.js', $$.rev()))
        .pipe(gulpif('*.css', $$.cssnano()))
        //.pipe(gulpif('*.css', $$.rev()))
        .pipe($$.revReplace())
        .pipe(inject(partials, { ignorePath: ['target/dist/'], addRootSlash: false, starttag: '<!-- inject:template-js -->' }))
        .pipe(gulpif('*.html', $$.htmlmin({ removeComments: true })));
    
    return eventStream.merge(partials, main)
        .pipe($$.debug({title: 'Processed output File: '}))
        .pipe(gulp.dest('target/dist'));
});

//gulp.task('default', ['analyze', 'release']); // Dependencies are run in parallel, so an error in analyze does not stop the build
gulp.task('default', function(){
    runSequence('analyze', 'release');
});

gulp.task('watch', function () {
    gulp.watch(['./target/classes/index.html', './target/classes/**/*.js'], ['default']);
});
