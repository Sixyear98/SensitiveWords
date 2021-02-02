call gradle clean

call gradle --info --stacktrace jar

call gradle --info --stacktrace --daemon --system-prop=windows publish

pause