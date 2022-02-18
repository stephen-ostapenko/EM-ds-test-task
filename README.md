# EM data structures test task

Автор: Степан Остапенко.

#### Использование

Проект запускается с помощью gradle.

Для замеров необходимо производить операции с файлами, поэтому для работы программе
необходимо знать путь до папки, расположенной на диске, и в которой можно свободно
производить стандартные операции с файлами, например, создание файла, чтение содержимого,
изменение и удаление файла. \
По желанию можно указать размер тестовых данных и количество проводимых тестов.

Запуск из командной строки: \
`gradle run --args="<путь до вышеописанной папки> [размер тестовых данных в мегабайтах] [количество тестов]"`