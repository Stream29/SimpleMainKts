cd /d %~dp0
chcp 65001
java -jar -Dkotlin.script.classpath=app-all.jar -Dfile.encoding=UTF-8 -Dsun.stdout.encoding=UTF-8 -Dsun.stderr.encoding=UTF-8 -Dsun.stdin.encoding=UTF-8 app-all.jar %1
PAUSE