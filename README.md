# java-kanban
Repository for homework project.
если тесты не запускаются _ удалить директории out/production/java-kanban и out/test(если ошибка  про версии class-файлов — rm -rf out/production/java-kanban out/test)
Запустить все тесты java -jar lib/junit-platform-console-standalone-1.9.3.jar --class-path out/test:out/production/java-kanban --scan-classpath
Запустить какой то тест java -jar lib/junit-platform-console-standalone-1.9.3.jar --class-path out/test:out/production/java-kanban --select-class service.FileBackedTaskManagerTest