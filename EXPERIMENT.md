# Запуск на SSD диске компьютера

При запуске на локальном SSD диске были получены следующие результаты:

```
Test data with total size of 256 MB was split into 1 file(s)
Read speed is 657 MB/s and write speed is 475 MB/s

Test data with total size of 256 MB was split into 2 file(s)
Read speed is 719 MB/s and write speed is 268 MB/s

Test data with total size of 256 MB was split into 3 file(s)
Read speed is 685 MB/s and write speed is 192 MB/s

Test data with total size of 256 MB was split into 4 file(s)
Read speed is 648 MB/s and write speed is 199 MB/s

Test data with total size of 256 MB was split into 5 file(s)
Read speed is 665 MB/s and write speed is 181 MB/s

Test data with total size of 256 MB was split into 10 file(s)
Read speed is 650 MB/s and write speed is 165 MB/s

Test data with total size of 256 MB was split into 20 file(s)
Read speed is 633 MB/s and write speed is 140 MB/s

Test data with total size of 256 MB was split into 50 file(s)
Read speed is 659 MB/s and write speed is 115 MB/s

Test data with total size of 256 MB was split into 100 file(s)
Read speed is 487 MB/s and write speed is 90 MB/s
```

Можно видеть, что скорость записи значительно меньше скорости чтения, а также то, что
скорость записи уменьшается с увеличением количества файлов, в то время как скорость чтения
не меняется.

Первый факт я не могу самостоятельно обосновать. Скажу лишь, что, действительно, при тестах
скорости SSD диска скорость чтения получается примерно в два раза выше скорости записи.

Второй факт можно обосновать тем, что для записи одного файла могут потребоваться некоторые
накладные расходы, помимо расходов на саму запись. В связи с этим, выгоднее записывать на
диск один большой файл, а не много маленьких файлов с суммарным размером равным размеру
большого файла. При этом, на чтение файлов дополнительных накладных расходов почти не
требуется, поэтому чтение одного большого файла занимает столько времени, сколько и чтение
большого числа маленьких файлов. 