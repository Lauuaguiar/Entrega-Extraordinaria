# Project: Hotel Information and Weather Forecast in the Canary Islands
## Course: Data Science Applications Development
## Year: 2024
## Degree: Bachelor's in Data Science and Engineering
## School: School of Computer Engineering and Mathematics
## University: University of Las Palmas de Gran Canaria
---

## Functionality Overview
This project aims to provide detailed information about hotels and weather forecasts across the Canary Islands. It utilizes two main APIs: OpenWeatherMap for weather information and Amadeus for Developers for hotel information.

### Modules

1. **Prediction Provider**:
   - This module accesses the OpenWeatherMap API to fetch detailed weather data.
   - It uses a CSV file with locations of the 8 Canary Islands to fetch specific information such as temperature, precipitation, clouds, and wind.
   - Data updates every 6 hours and is converted into JSON objects published to the 'prediction.weather' topic on the ActiveMQ broker.

   #### Technical Details
   - Requires the OpenWeatherMap `apikey` for execution.
   - Data is pushed to the ActiveMQ topic for distribution.

   #### Class Diagram Prediction Provider
   ![Class Diagram Prediction Provider](https://github.com/Lauuaguiar/Entrega-Extraordinaria/assets/145450311/1cd41eeb-8d3e-4c36-97e4-c68722d8febd)


2. **Hotel Provider**:
   - This module connects to the Amadeus for Developers API to retrieve detailed hotel information in the Canary Islands.
   - It uses the `locations.csv` file to access data such as hotel names, IDs, and star ratings.
   - Information updates every 6 hours and is converted into JSON objects published to the 'location.hotels' topic on the ActiveMQ broker.

   #### Technical Details
   - Requires the `apikey` and `apisecret` from Amadeus for Developers for execution.
   - Data is published to the ActiveMQ topic for distribution.

   #### Class Diagram Hotel Provider
   ![Class Diagram Hotel Provider](https://github.com/Lauuaguiar/Entrega-Extraordinaria/assets/145450311/a512d1ec-c2ea-46dd-b942-4d7308fc6ea9)

3. **Datalake Builder**:
   - This module stores the collected data from other modules into a datalake.
   - It subscribes to topics where data is found and saves it into files with a specific directory structure within the datalake.

   #### Technical Details
   - Requires the path where the datalake should be stored as an argument.
   - Data is organized into the structure `datalake/eventstore/{topic}/{ss}/{YYYYMMDD}.events`.

   #### Class Diagram Datalake Builder
   ![Class Diagram Datalake Builder](https://github.com/Lauuaguiar/Entrega-Extraordinaria/assets/145450311/7ae19c40-0b3d-420a-a377-316e837e8ed4)


4. **Business Unit**:
   - This module serves as the user interface to query information collected by other modules.
   - Allows the user to select an island and provides information about available hotels based on retrieved data.
   - Also offers weather forecasts upon user request.

   #### Technical Details
   - Requires the path where databases will be stored as an argument.
   - Accesses data stored locally if no new real-time data is available.

   #### Class Diagram Business Unit
   ![Class Diagram Business Unit](https://github.com/Lauuaguiar/Entrega-Extraordinaria/assets/145450311/bb1f8103-d77c-41fd-8e37-932d7de75766)



## Design Patterns Used
### MVC (Model-View-Controller)
The project follows the MVC architectural pattern to separate business logic, presentation, and data management into distinct components:

- **Model**: Represents data and application logic. In this project, models include structures for weather, hotels, and data stored in the datalake.

- **View**: The user interface enabling user interaction with the system. In this project, the View is implemented in the Business Unit module through the user interface presenting hotel and weather information.

- **Controller**: Manages interactions between the Model and View. In this project, the Controller is implemented in the Business Unit module to coordinate user requests and data retrieval from the Model (either real-time or from the datalake).

### Kappa Design
The Kappa design refers to a real-time data processing architecture that simplifies the infrastructure required for streaming data analysis:

- **Prediction and Hotel Providers**: These modules collect real-time data from respective APIs (OpenWeatherMap and Amadeus for Developers). This data is processed and sent directly to a broker (ActiveMQ) for distribution.

- **Datalake Builder**: Although not strictly following the Kappa design, this module stores data in a datalake after it has been initially collected and processed. This allows durable storage and the ability to query historical data for subsequent analysis.

---

## Execution Requirements
### Required Arguments
- **Business Unit**:
  - Path where databases will be stored.

  
- **Hotel Provider**:
  - `apikey` and `apisecret` from your Amadeus for Developers account.

  
- **Prediction Provider**:
  - `apikey` from your OpenWeatherMap account.

  
- **Datalake Builder**:
  - Path where the datalake should be stored.
  
### Running the Project
To run the project, follow these steps:

1. **Execute Prediction Provider and Hotel Provider simultaneously**:
   - Both modules will execute and display a message indicating they have completed their function.

2. **Run Datalake Builder**:
   - Execute Datalake Builder after Prediction Provider and Hotel Provider have finished. It will display a message confirming completion.

3. **Run Business Unit**:
   - Once the above steps are complete, execute Business Unit. It will display a message explaining no messages are in the topic and accessing the datalake to extract data. It will then begin querying the user based on available options.

---

## Dependencies
The project requires the following dependencies:

- **jsoup**: Used for HTML data parsing and manipulation.
- **gson**: Used for Java object to JSON serialization and deserialization.
- **activemq-client**: Provides necessary functionality to interact with the ActiveMQ broker.
- **slf4j-api**: Provides a logging API for various logging implementations.
- **spring-context**: Part of the Spring framework, used for dependency management and dependency injection.
- **okhttp**: Efficient HTTP client for communication with APIs.
- **jakarta.jms-api**: Provides necessary classes and interfaces for Java Message Service (JMS) programming.
- **sqlite-jdbc**: JDBC driver for connecting to SQLite databases.

```
