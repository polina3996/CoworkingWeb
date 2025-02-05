package coworking.controller;

public class AuthResponse {
    //(Handles login response) Represents login response (returns JWT token).
    private String token;

    public AuthResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
