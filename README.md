# Task №3

### 1. Собрать проект
	mvn clean package

### 2. Запустить проект 
	java -jar target/spring-weather-service-0.0.1-SNAPSHOT.jar

После запуска:

### 3. Получить прогноз погоды на графике
	curl "http://localhost:8080/weather?city=Moscow"

PNG-график температуры сохраняется в папку ./charts.

### 4. Если необходимо сразу получить файл, то необходимо после получения прогноза вписать следующий запрос:
	curl -o chart.png "http://localhost:8080/chart?path={chartPath}"
Где {chartPath} это путь до сохраненного графика из полученного JSON-ответа
