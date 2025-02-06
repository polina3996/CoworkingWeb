package coworking.dto;


public class RegisterRequest {
    private String username;
    private String password;
    private String role;

    public String getPassword(){
        return this.password;
    }
    public String getRole(){
        return this.role;
    }
    public String getUsername(){
        return this.username;
    }
    public void setUsername(String newName){
        this.username = newName;
    }
    public void setPassword(String newPassword){
        this.password = newPassword;
    }
    public void setRole(String newRole){
        this.role = newRole;
    }

}

