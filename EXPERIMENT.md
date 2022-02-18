# Запуск на SSD диске компьютера

При запуске на локальном SSD диске были получены следующие результаты:

```
Test data with total size of 256 MB was split into 1 file(s)
Read speed is 412 MB/s and write speed is 344 MB/s

Test data with total size of 256 MB was split into 2 file(s)
Read speed is 606 MB/s and write speed is 178 MB/s

Test data with total size of 256 MB was split into 3 file(s)
Read speed is 540 MB/s and write speed is 178 MB/s

Test data with total size of 256 MB was split into 4 file(s)
Read speed is 479 MB/s and write speed is 169 MB/s

Test data with total size of 256 MB was split into 5 file(s)
Read speed is 511 MB/s and write speed is 169 MB/s

Test data with total size of 256 MB was split into 10 file(s)
Read speed is 504 MB/s and write speed is 171 MB/s

Test data with total size of 256 MB was split into 20 file(s)
Read speed is 504 MB/s and write speed is 170 MB/s

Test data with total size of 256 MB was split into 50 file(s)
Read speed is 535 MB/s and write speed is 146 MB/s

Test data with total size of 256 MB was split into 100 file(s)
Read speed is 582 MB/s and write speed is 110 MB/s
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