import com.codingame.gameengine.runner.SoloGameRunner;

public class SkeletonMain {
    public static void main(String[] args) {

        SoloGameRunner gameRunner = new SoloGameRunner();
        gameRunner.setAgent("mono /home/eulerschezahl/Documents/Programming/C#/TestProject/bin/Debug/TestProject.exe");
        gameRunner.setTestCase("test1.json");

        gameRunner.start();
    }
}
