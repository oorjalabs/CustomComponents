let gulp = require('gulp');
let markdown = require('gulp-markdown-it');
var concat = require('gulp-concat');

gulp.task('updateNotesMarkdown', gulp.series(() => {
    return gulp.src('Others/md/updatenotes.md')
        .pipe(markdown())
        .pipe(gulp.dest("Others/md/out"));
}));

gulp.task('justUpdateNotes', gulp.series('updateNotesMarkdown', () => {
    return gulp.src(['Others/md/header.html', 'Others/md/out/updatenotes.html', 'Others/md/footer.html'])
        .pipe(concat("updatenotes.html"))
        .pipe(gulp.dest("app/src/main/res/raw"));
}));

gulp.task('default', gulp.series('justUpdateNotes', () => {
    gulp.watch('Others/md/*.md', gulp.series('justUpdateNotes'));
}));
