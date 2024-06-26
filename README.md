# Scrabble Score Backend

A backend system for managing and calculating Scrabble scores, implemented with Spring Boot, Kotlin, and PostgreSQL.

## Features

- Scrabble score calculation
- Player score submission
- Top scores leaderboard
- RESTful API with validation

## Requirements

- Java 17
- Maven
- PostgreSQL

###  Set Up PostgreSQL Database
```
CREATE DATABASE scrabble_score;
```

## API Documentation

### Base URL
`http://localhost:8080/api`

### Endpoints


1. **Calculate Score**
   - **Method:** `POST`
   - **Endpoint:** `/scores/calculate`
   - **Description:** Calculates the Scrabble score for a given word.

   **Request:**
   ```json
   {
     "word": "hello"
   }
   ```

   **Response:**
   ```json
   8
   ```

   **Error Responses:**
   - **400 (Bad Request):** Word input contains non-alphabetical characters or is empty.
   ```json
   {
     "word": "Word must contain only alphabetical characters"
   }
   ```

2. **Submit Score**
   - **Method:** `POST`
   - **Endpoint:** `/scores/submit`
   - **Description:** Submits the scores for a player, closing the session afterward.

   **Request:**
   ```json
   {
     "playerName": "Player1",
     "scores": [
       {
         "word": "HELLO",
         "score": 8
       },
       {
         "word": "WORLD",
         "score": 12
       }
     ]
   }
   ```

   **Response:**
   ```json
   "Scores submitted successfully"
   ```

   **Error Responses:**
   - **400 (Bad Request):** Player name or word is empty, or word input contains non-alphabetical characters.
   ```json
   {
     "playerName": "Player name cannot be empty",
     "word": "Word must contain only alphabetical characters"
   }
   ```

3. **Get Top Scores**
   - **Method:** `GET`
   - **Endpoint:** `/scores/top`
   - **Description:** Retrieves the top 10 player scores sorted by score in descending order.

   **Response:**
   ```json
   [
     {
       "sessionId": "f47ac10b-58cc-4372-a567-0e02b2c3d479",
       "startTime": "2024-04-25T10:00:00",
       "endTime": "2024-04-25T10:30:00",
       "totalScore": 100,
       "playerName": "Player1",
       "scores": [
         {
           "word": "HELLO",
           "wordScore": 8
         },
         {
           "word": "WORLD",
           "wordScore": 12
         }
       ]
     }
   ]
   ```

## Postman Collection
Here's a JSON collection that could be imported into Postman for testing the API endpoints.

```json
{
	"info": {
		"_postman_id": "bfe26480-6b64-42ed-88b7-c7a577d443dd",
		"name": "Scrabble Score Backend",
		"description": "A Postman collection for testing the Scrabble Score Backend endpoints.",
		"schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json",
		"_exporter_id": "6728125"
	},
	"item": [
		{
			"name": "Calculate Score",
			"request": {
				"method": "POST",
				"header": [
					{
						"key": "Content-Type",
						"value": "application/json"
					}
				],
				"body": {
					"mode": "raw",
					"raw": "{\"word\": \"hello\"}"
				},
				"url": {
					"raw": "http://localhost:8080/api/scores/calculate",
					"protocol": "http",
					"host": [
						"localhost"
					],
					"port": "8080",
					"path": [
						"api",
						"scores",
						"calculate"
					]
				}
			},
			"response": []
		},
		{
			"name": "Submit Score",
			"request": {
				"method": "POST",
				"header": [
					{
						"key": "Content-Type",
						"value": "application/json"
					}
				],
				"body": {
					"mode": "raw",
					"raw": "{\"playerName\": \"Player1\",\"scores\": [{\"word\": \"HELLO\",\"score\": 8},{\"word\": \"WORLD\",\"score\": 12}]}"
				},
				"url": {
					"raw": "http://localhost:8080/api/scores/submit",
					"protocol": "http",
					"host": [
						"localhost"
					],
					"port": "8080",
					"path": [
						"api",
						"scores",
						"submit"
					]
				}
			},
			"response": []
		},
		{
			"name": "Get Top Scores",
			"request": {
				"method": "GET",
				"header": [],
				"url": {
					"raw": "http://localhost:8080/api/scores/top",
					"protocol": "http",
					"host": [
						"localhost"
					],
					"port": "8080",
					"path": [
						"api",
						"scores",
						"top"
					]
				}
			},
			"response": []
		}
	]
}
```

