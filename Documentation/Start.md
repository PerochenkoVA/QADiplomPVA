
Для запуска автотестов требуется: 
Предварительно выключить запущенные сервера на порту 3306.

1. Запустить Is the docker daemon.
2. Запустить через терминал Docker: 
docker-compose up -d
3. Запустить приложение aqa-shop.jar: 
java -Dspring.datasource.url=jdbc:mysql://localhost:3306/app -jar aqa-shop.jar
4. Проверить доступность приложения в браузере по адресу:
http://localhost:8080/
5. Запустить тесты с отчетом Allure командой:
   ./gradlew clean test allureReport
6. Для просмотра отчета о тестировании открыть с помощью браузера фаил: 
 % build/reports/testes/test/index.html
