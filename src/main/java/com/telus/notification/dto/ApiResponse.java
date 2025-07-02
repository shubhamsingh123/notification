package com.telus.notification.dto;

import java.util.List;
import java.util.stream.Collectors;
import java.time.format.DateTimeFormatter;
import com.telus.notification.entity.Notification;

public class ApiResponse {
    private boolean success;
    private Data data;
    private String message;
    private int statusCode;
    private Object errors;
    private long timestamp;

    public static class Data {
        private String message;
        private int count;
        private List<NotificationDto> notifications;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public List<NotificationDto> getNotifications() {
            return notifications;
        }

        public void setNotifications(List<NotificationDto> notifications) {
            this.notifications = notifications;
        }
    }

    public static class NotificationDto {
        private Integer id;
        private String message;
        private String created_at;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public Object getErrors() {
        return errors;
    }

    public void setErrors(Object errors) {
        this.errors = errors;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public static ApiResponse success(List<Notification> notifications) {
        ApiResponse response = new ApiResponse();
        response.setSuccess(true);
        response.setMessage("Notification loaded successfully");
        response.setStatusCode(200);
        response.setErrors(null);
        response.setTimestamp(System.currentTimeMillis());

        Data data = new Data();
        data.setMessage("Notification loaded successfully");
        data.setCount(notifications.size());
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        List<NotificationDto> notificationDtos = notifications.stream()
            .map(n -> {
                NotificationDto dto = new NotificationDto();
                dto.setId(n.getNotificationId());
                dto.setMessage(n.getType());
                dto.setCreated_at(n.getCreatedAt().format(formatter));
                return dto;
            })
            .collect(Collectors.toList());
        
        data.setNotifications(notificationDtos);
        response.setData(data);

        return response;
    }
}
