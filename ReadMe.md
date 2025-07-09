A service for receiving cars from various sites and posting them to a TG group, as well as a search with filtering in the TG bot.

### Presets.

### Create a database:
1. create a Postgres database called `gur_car_bot`
2. the name of the database is contained in the section: `spring.datasource.url: jdbc:postgresql://localhost:5432/gur_car_bot`

### Create a bot and a group:
1. Go to the telegram channel https://t.me/BotFather
2. In the menu section, select `/newbot`
3. Follow the instructions and create a bot and group
4. Copy the token and paste it into `application-local.yaml` in the section: `application.telegram.bot.token = xxx`
5. The group identification must start with the minus `group-id: -00000000`
6. In the group, you need to create several subgroups and specify their ID in the # id section of the subgroups for posting

### Test Load:
At the first load, the flyway will load the initial data into the database, as if the parser has already received data on several cars, but has not yet parsed it.
The `vehicles` table will not be filled in completely. Primary data include:
`auction_id`, `vin`, `auction`, `status`, `link`, `auction_data`
`status` will be equal to `AWAIT_CALC`, i.e. waiting to be calculated

### Work of the TG group:
In 1 minute, the calculation service starts. It will take a pack of cars and try to parse the JSON obtained at the previous stage with the full data about the car. If it fails, it will change the status of the record.

After successful JSON parsing, the data found in JSON will appear in the `vehicles` table and the status will change to `WEB`, i.e. the record is ready for use in posting to the group and on the site. The `pictures` table will also be populated with links to the car images found in the JSON. The main picture of each car is of type `FIRST`, all the rest are `SECOND`.

At intervals, a service will be launched, which selects from the available cars relevant for posting to the telegram group and forms a table for posting, taking into account which group in which subgroup the posting will be made.
>Why was a separate service made? To parallelize actions. Cars (in the future) can be sent not only to a telegram group, but also to e-mail or directly to a personal chat to the user (who subscribed to the mailing list). For this purpose, the `posts` table has the `user_type`, `group_channel_id` fields.

Then the "Poster" starts. It reads the `posts` table, picks up a certain number of posts to send in the `CREATED` status and sends them to the specified addresses. In the current implementation, only the telegram group (subgroups).
Sending is carried out in synchronous mode, because telegram does not allow multiple calls. If the number of calls is more than the number allotted to the service, telegram blocks the download and gives an error with the specified time until the end of the blocking. The posting service takes this into account and falls asleep for the specified time in order to avoid DDOS and subsequent ban.
After posting, write down the message identifiers in the TG in the `posts` table (usually one post is several messages) to be able to remove it from the TG in the future. We also change the status of the record to `POSTED`.

The service for deleting records in the TG channel is also periodically launched. It reads a batch of records from the `posts` table, compares the deletion date and if it is suitable, it deletes the record from the TG group, indicating the `DELETED` status in the record. Sometimes the TG cannot delete a record for some internal reasons, in which case it will return an error and the `DELETED_ERROR` status will be recorded in the table. Such recordings are considered deleted so as not to try to delete them again and not to clog the channel. (Feature of TG)

## Service values

**parser:**
- set the `True` flag to load the list of found cars from the site for the first time (it does not work without login/password):<br>
  `application.parser.auctionspark.seed.enabled = true`
- set the `true` flag to start downloading parts for each car from the site (does not work without login/password):<br>
  `application.parser.auctionspark.details.enabled = true`
- set the `true` flag to parse each downloaded car and calculate the data (filling in the table for uploading to the bot):<br>
  `application.parser.auctionspark.calculation.enabled = true`

**publisher:**
- check the `true` flag to calculate which cars will be sent to the bot (bots):<br>
  `application.publisher.seed.enabled: true`
- set the `true` flag when posting a car with a set of images to the bot:<br>
  `application.publisher.post.enabled: true`
- set the `true` flag to remove irrelevant (already played) cars from the bot:<br>
  `application.publisher.delete.enabled: true`

## Working of the TG bot (basics)

In the registered bot, it becomes possible to search for entries from the `vehicle` table in the `WEB` status by various filters.
When you send the `/start` command to the bot, the initial data (depending on the user's language), the selection of settings, filters, company data, etc. are loaded.
Any user can set up a search filter that will be saved for them in the `filters` table. <br>
When searching by filter, the user is given several posts with the calculation of how many posts have been found and where it is located, with an offer to continue viewing further. When clicking on the "continue viewing" button, the user is downloaded another batch of posts (pagination). <br>
The user can also view information about the company, ask a question, and order the car he likes. <br>
If the user wants to ask a question, the program enters the TG listening mode and waits for the user to write a question and send it. If this does not happen, the program will reset the settings, and at any other step (except for sending a question) it will reset the question mode. If a question is asked, it will be saved to the `messages` table. <br> <br>
Also, the order will be saved there if the user clicks "order". We will show the user a funny picture and say that the order has been accepted, wait.

## Smartphone app operation

The service is paired with an Android application.<br>
The application scans the VIN number, takes photos of the car and sends them via the REST API `/v1/file/upload`.<br>
Security is accomplished by passing the `X-API-Key` header from the application to the service. The resulting photos are saved on the server. In the local version, in the `images` folder.<br>
The REST API can be seen in Swagger at http://localhost:5000/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config.<br>
In the future, the application will be able to send car data such as price, mileage, etc. to the service via the `/v1/send_data` REST API.
