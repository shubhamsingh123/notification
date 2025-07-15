#!/bin/bash

# Set variables
PROJECT_ID="telusrecruitai"
REGION="asia-south1"  # Default region, can be changed
IMAGE_NAME="notification-service"
SERVICE_NAME="notification-service"

# Build the Docker image
docker build -t $IMAGE_NAME .

# Tag the image for Google Container Registry
docker tag $IMAGE_NAME gcr.io/$PROJECT_ID/$IMAGE_NAME

# Push the image to Google Container Registry
docker push gcr.io/$PROJECT_ID/$IMAGE_NAME

# Deploy to Cloud Run
gcloud run deploy $SERVICE_NAME \
  --image gcr.io/$PROJECT_ID/$IMAGE_NAME \
  --platform managed \
  --region $REGION \
  --project $PROJECT_ID \
  --allow-unauthenticated
