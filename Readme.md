## Пробелмы с выводом русских символов в терминале Windows.
- Если вы используете IntellijIdea то данной проблемы у вас не будет
- Если вы используете терминал Windows или VSCode (На Windows) у вас может быть проблем с выводом русских строк (он не отображает их в выводе), поэтому при использованя приложения рекомендуется использовать латиницу. 

## Запуск приложения
#### 2 способа
- Используя скрипт запуска в зависимости от вашей операционной системы
- Командой java -jar .\target\spending-notebook-1.0-jar-with-dependencies.jar

#### В обоих случаях необходимо указать либо 0 либо 4 аргумента для доступа к базе данных. Примеры запуска
- .\AppStartScript.bat jdbc:postgresql://localhost:5432/ spending_notebook_db_test postgres postgres 
- .\AppStartScript.bat
- java -jar .\target\spending-notebook-1.0-jar-with-dependencies.jar jdbc:postgresql://localhost:5432/ spending_notebook_db_test postgres postgres
- java -jar .\target\spending-notebook-1.0-jar-with-dependencies.jar

#### Аргументы идут в следующем порядке: 
- URL к которому нужно подключаться
- название базы данных (Если не существет, то создастся автоматически) 
- логин от postgres
- пароль от postgres.

## Использование приложения
#### Spending notebook консольное приложение, поддерживающие определенные команды. Узнать весь список команд можно введя команду help. К каждой команде прилагается описание аргументов и описание.