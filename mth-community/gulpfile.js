var gulp = require('gulp');

var $ = require('gulp-load-plugins')();

gulp.task('build', function() {
    return gulp.src('public/js/community/*.js')
        .pipe($.babel({
            presets: ['es2015']
        }))
        .pipe($.uglify())
        .pipe(gulp.dest('public/build/js/community/'));
});

gulp.task('watch', function() {
	gulp.watch('public/js/community/*.js', ['build']);
});