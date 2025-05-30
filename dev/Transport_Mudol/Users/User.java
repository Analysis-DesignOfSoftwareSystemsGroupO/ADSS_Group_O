package Users;

import java.util.Objects;

public abstract class User {
    protected static int id = 0;
    protected   int code;
    protected String username;
    protected String password;

    public User(String username, String password){
        this.password = password;
        this.username = username;
        this.code = ++id;

    }

    public void setPassword(String oldpassword, String newpassword) {
        if(Objects.equals(oldpassword, password)) {
            this.password = newpassword;
        }
    }

    public void setUsername(String password, String newusername){
        if(Objects.equals(this.password, password)) {
            this.username = newusername;
        }
    }

    public String getUsername(){
        return username;
    }
    public boolean comparePassord(String password){
        return  Objects.equals(this.password, password);
    }

    @Override
    public boolean equals(Object other){

        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        return Objects.equals(((User) other).password, password) &&  Objects.equals(((User) other).username, username);
    }

    public abstract void showMenu();
}
