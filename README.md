# Tick Sightings Analysis API

A Java Spring Boot REST API that retrieves tick sighting data from the Elanco API and provides filtering, pagination, and statistical reports. This is only the backend part.

## Features

- Retrieve all tick sightings.
- Retrieve an individual sighting by ID.
- Filter sightings by city or species.
- Filter sightings between a date range.
- Combine city and date filters.
- Paginate tick sightings.
- Create regional sighting counts.
- Create weekly and monthly sighting reports.
- Store API responses as cache for five minutes.
- Exclude duplicate records using sighting IDs.
- Handle invalid requests and upstream API failures.

## Technologies Used

- Java
- Spring Boot
- Maven
- Spring Web / RestClient
- REST API
- Postman

## Data Source

Data is retrieved from the Elanco public API:

https://dev-task.elancoapps.com/data

The application retrieves the sighting dataset and performs filtering and reporting locally.

## Running the Application

### Prerequisites

- Java JDK 25 or compatible version
- Maven (or the included Maven Wrapper)

### Instructions

1. Clone or download this repository.
2. Open a terminal in the project root directory.
3. Run:

```bash
./mvnw spring-boot:run
```

On Windows PowerShell, use:

```powershell
.\mvnw.cmd spring-boot:run
```

4. The application will start at:

`http://localhost:8080`

## API Endpoints

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/sightings` | Retrieve all sightings |
| GET | `/api/sightings/{id}` | Retrieve a sighting by ID |
| GET | `/api/sightings/city/{city}` | Filter by city |
| GET | `/api/sightings/species/{species}` | Filter by species |
| GET | `/api/sightings/date-range?from=...&to=...` | Filter by date range |
| GET | `/api/sightings/filter?city=...&from=...&to=...` | Filter by city and date |
| GET | `/api/sightings/page?page=0&size=20` | Retrieve paginated results |
| GET | `/api/reports/regional` | Counts by location |
| GET | `/api/reports/monthly` | Monthly sighting counts |
| GET | `/api/reports/weekly` | Weekly sighting counts |

### Date Format

Date-time query parameters use the ISO format:

`yyyy-MM-ddTHH:mm:ss`

Example:

`2022-08-01T00:00:00`

### Pagination

- Page numbering starts at `0`.
- Default page size is `20`.
- Maximum page size is `100`.

## Error Handling

The application handles:

- Invalid input with HTTP 400.
- Missing sightings with HTTP 404.
- Upstream API errors.
- Connection failures with HTTP 503.

These were tested and verified via Postman.

## Notes

- The application uses an in-memory cache with a five-minute duration.
- Duplicate sightings are removed using their IDs.
- Records with missing IDs are excluded.
- The application retrieves data from an external API and does not modify the source dataset.