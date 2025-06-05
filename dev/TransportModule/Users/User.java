package TransportModule.Users;

import java.util.Objects;

public abstract class User {
    protected static int id = 0;
    protected   int code;
    protected String username;

    public User(String username){
        this.username = username;
        this.code = ++id;

    }



    public void setUsername( String newusername){
        this.username = newusername;

    }

    public String getUsername(){
        return username;
    }

    @Override
    public boolean equals(Object other){

        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        return  Objects.equals(((User) other).username, username);
    }

    @Override
    public final int hashCode() {
        return username .hashCode();
    }

    public abstract void showMenu();
}