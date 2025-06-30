# Notification Service

This service handles notifications for user events using Google Cloud PubSub.

## Environment Variables

The following environment variables need to be set:

### Google Cloud Configuration
- `GCP_PROJECT_ID`: Your Google Cloud project ID
- `GCP_CREDENTIALS_BASE64`: Base64 encoded Google Cloud service account key
- `PUBSUB_TOPIC_USER_REGISTRATION`: PubSub topic name for user registration events
- `PUBSUB_SUBSCRIPTION_USER_REGISTRATION`: PubSub subscription name for user registration events

### Database Configuration
- `SPRING_DATASOURCE_URL`: MySQL database URL
- `SPRING_DATASOURCE_USERNAME`: Database username
- `SPRING_DATASOURCE_PASSWORD`: Database password

### Email Configuration
- `SPRING_MAIL_USERNAME`: Gmail account username
- `SPRING_MAIL_PASSWORD`: Gmail app password

## Setup Instructions

1. Create a Google Cloud service account and download the key file
2. Convert the key file to base64:
   ```bash
   # On Linux/Mac:
   base64 -i path/to/service-account-key.json
   
   # On Windows PowerShell:
   [Convert]::ToBase64String([System.IO.File]::ReadAllBytes("path\to\service-account-key.json"))
   ```
3. Set the environment variables with the appropriate values
4. Run the application:
   ```bash
   mvn spring-boot:run
   ```

## Security Note

- Never commit sensitive credentials to version control
- The `src/main/resources/telus-koodo-key.example.json` file provides a template for the required Google Cloud service account structure
- Always use environment variables or a secure secrets management service for sensitive information
