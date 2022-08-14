package db.skill;

public enum Level {
    JUNIOR("Junior"),
    MIDDLE("Middle"),
    SENIOR("Senior");

    private final String levelName;

    Level(String levelName)
    {
        this.levelName = levelName;
    }

    public String getLevelName (){
        return levelName;
    }
}
