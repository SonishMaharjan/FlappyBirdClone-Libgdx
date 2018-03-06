package helpers;

public class GameManager {

    private String[] birds = {"Blue","Green","Red"};

    private int index = 0;


    private GameManager() {
    }

    public void incrementIndex()
    {
        index++;

        if(index == birds.length)
        {
            index = 0;
        }
    }

    public String getBird()
    {
         return birds[index];

    }

    private static GameManager ourInstance = new GameManager();

    public static GameManager getInstance() {
        return ourInstance;
    }

}
