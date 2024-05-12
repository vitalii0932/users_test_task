# Halaiko Vitalii test task

## _System for storing and working with user data_

___

### Requirements:

1. It has the following fields:

   1.1. Email (required). Add validation against email pattern

   1.2. First name (required)

   1.3. Last name (required)

   1.4. Birth date (required). Value must be earlier than current date

   1.5. Address (optional)

   1.6. Phone number (optional)

2. It has the following functionality:

   2.1. Create user. It allows to register users who are more than [18] years old. The value [18] should be taken from
   properties file.

   2.2. Update one/some user fields

   2.3. Update all user fields

   2.4. Delete user

   2.5. Search for users by birth date range. Add the validation which checks that “From” is less than “To”. Should
   return a list of objects
3. Code is covered by unit tests using Spring
4. Code has error handling for REST
5. API responses are in JSON format

### Loading

1. Clone the project from GitHub:`git clone https://github.com/vitalii0932/users_test_task`
2. `mvn clean install`.
3. Run the project

### Usage

1. Save the user POST in url `/api/v1/users/register`.
    ```json
        {
          "id": "any number, it will be replaced",
          "email": "email@gmail.com",
          "firstName": "first name",
          "lastName": "last name",
          "dateOfBirth": "YYYY-MM-DD",
          "address": "address",
          "phoneNumber": "1234567890"
        }
   ```
2. Update some user POST in url `/api/v1/users/update`.
    ```json
        {
          "id": "user id",
          "email": "email@gmail.com",
          "firstName": "first name",
          "lastName": "last name",
          "dateOfBirth": "YYYY-MM-DD",
          "address": "address",
          "phoneNumber": "1234567890"
        }
   ```
3. Update some user fields POST in url `/api/v1/users/update_fields`.
    ```json
        {
          "id": "user id",
          "field1": "value1",
          "field2": "value2"
        }
   ```
   User fields to update:
   - email (only email, unique, required)
   - firstName (required)
   - lastName (required)
   - dateOfBirth (date only in the past, over 18 years old, required)
   - address (optional)
   - phoneNumber (empty, or only 10 digits)
4. Delete some user Get in url `/api/v1/users/delete`.
   Params:
   - id (some user id)
5. Get user from some date to some date Get in url `/api/v1/users/get_users_by_dates`.
   Params:
   - from (from date in format YYYY-MM-DD)
   - to (to date in format YYYY-MM-DD)
6. Access to swagger `/swagger-ui/index.html`. On the Swagger page, you can also test all the features.

___

## Author

- [GitHub](https://github.com/vitalii0932)
- [Telegram](https://t.me/VitaliiGalayko)
- [LinkedIn](https://www.linkedin.com/in/vitalii-halaiko-199337281/)
- halaikovitalii@ukr.net