package coworking.dto;


public class RegisterRequest {
    private String username;
    private String password;
    private String role;

//    // Default constructor (needed for Spring deserialization)
//    public RegisterRequest() {
//    }
    //No-argument Constructor: A no-argument constructor is required
    // so that Spring can instantiate the RegisterRequest object when deserializing
    // the JSON request body.

    //Getters and Setters: These allow Spring to populate the fields of
    // RegisterRequest when it receives the HTTP request body.

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

