# clojure-spanish-date-parser

## Clojure

Read a date in spanish and parse it to the desired format.

Lee una fecha en español y conviertela al formato que desees.

Default format is dd/MM/yyyy.

If it cannot be parser same string is returned.

## Examples
```
(date-parser "2 de enero del 2023")
=> "02/01/2023"

(date-parser "2/1/2023")
=> "02/01/2023"

(date-parser "2023-12-01")
=> "01/12/2023"

(date-parser "2023-12-01" "dd-MM-yyyy")
=> "01-12-2023"

(date-parser "01-ene-2023" "dd-MM-yyyy")
=> "01-01-2023"

(date-parser "01 enero 2023" "dd-MM-yyyy")
=> "01-01-2023"

(date-parser "blablabla")
=> "blablabla"
```




# Author
- [Jon Ramos](https://github.com/jramosg)
