Стек нагрузки: gatling + java

При нагрузочном тестировании запуск приложения производился следующей командой: `docker run -d -p 8080:4242 --cpus=2 -m 2048m todo-app:latest`

Повышение нагрузки происходило ступеньчато от 0 до 900 rps. В начале происходило наращивание нагрузки в течение 30 секунд,
после чего в течение 120 секунд подавалась постоянная нагрузка (всего было 6 ступеней - 150, 300 и т д)

В целом метод POST /todos хорошо выдерживает нагрузку и даже по 99 перцентилю время ответа в среднем не превышает 5мс,
но при 900 rps начинаются непродолжительные всплески, когда время ответа может увеличиться вплоть до 100 мс

При дальнейшем росте нагрузки начинается резкий рост потребление CPU 