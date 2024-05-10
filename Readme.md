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

#### Возможние команды:
- help
Usage: help
Помощь
- add_category
Usage:  add_category arg0 arg1 ...
Добавить категорию трат
      <arg0>      Название категории
      <arg1>...   MCC коды
- add_group_to_category
Usage:  add_group_to_category arg0 arg1 ...
Добавить группу категорий в категорию
      <arg0>      Название категории
      <arg1>...   Категории для добавления
- add_mcc_to_category
Usage:  add_mcc_to_category arg0 arg1 ...
Изменить категорию трат
      <arg0>      Название категории
      <arg1> ...   MCC коды
- add_transaction
Usage: add_transaction arg0 arg1 arg2 [arg3]
Добавить трату
      <arg0>     Название траты
      <arg1>     Значение траты
      <arg2>     Месяц
      [<arg3>]   MCC (необязательно)
- delete_category
Usage: delete_category arg0
Удалить категорию трат
      <arg0>   Название категории
- remove_transaction
Usage: remove_transaction arg0 arg1 arg2
Удалить трату
      <arg0>   Название транзакции
      <arg1>   Значение транзакции
      <arg2>   Месяц транзакции
- show_categories 
Usage: show_categories
Показать список категорий
- show_all_by_month
Usage: show_all_by_month arg0
Показать список категорий с суммой потраченных средств в выбранный месяц
      <arg0>   Месяц
- show_all_by_month
Usage: show_all_by_month arg0
Показать список категорий с суммой потраченных средств в выбранный месяц
      <arg0>   Месяц
- show_category_by_monthes
Usage: show_category_by_monthes arg0
Траты в категории по месяцам (сумма за месяц)
      <arg0>   Название категории