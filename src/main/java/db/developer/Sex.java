package db.developer;

public enum Sex {
    MALE("male"),
    FEMALE("female"),
    UNKNOWN("unknown");

    private final String sexName;

    Sex(String sexName)
    {
        this.sexName = sexName;
    }
    public String getSexName (){
        return sexName;
    }

}
