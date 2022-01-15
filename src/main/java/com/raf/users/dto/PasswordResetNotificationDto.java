package com.raf.users.dto;

public class PasswordResetNotificationDto {

    private Long id;
    private String email;
    private NotificationType type = NotificationType.NOTIFICATION_RESET_PASSWORD;

    public PasswordResetNotificationDto() {
    }

    public PasswordResetNotificationDto(Long id, NotificationType type) {
        this.id = id;
        this.type = type;
    }

    public PasswordResetNotificationDto(Long id, String email, NotificationType type) {
        this.id = id;
        this.email = email;
        this.type = type;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }
}
