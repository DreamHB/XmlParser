package neoworkstudio.com.xmlparserdemo;

/**
 * Created by dreamaomao on 7/5/15.
 */
public class Staff {
    private String name;
    private String gender;
    private int age;

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public void setGender(String gender){
        this.gender = gender;
    }

    public String getGender(){
        return this.gender;
    }

    public void setAge(int age){
        this.age = age;
    }

    public int getAge(){
        return age;
    }

    @Override
    public String toString() {
        return "name = " + name + " gender = " + gender + " age = " + age;
    }
}
