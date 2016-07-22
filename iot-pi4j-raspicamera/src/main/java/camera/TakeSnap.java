package camera;

public class TakeSnap
{
  
  public static void main(String[] args) throws Exception
  {
    Runtime rt = Runtime.getRuntime();
    System.out.println("Raspberry Camera is going to take 10 snaps, be ready...");
    System.out.println("Started taking the snaps...");
    for (int i=1; i<=10; i++)
    {
      long before = System.currentTimeMillis();
      //The below raspistill command will take photos with width as 200, height as 150, timeout of 1 millsec, image name as "snap<i>.jpg" (where <i> is the counter) with nopreview 
      //Process snap = rt.exec("raspistill --width 200 --height 150 --timeout 1 --output snap" + i + ".jpg --nopreview");
      //Process snap = rt.exec("raspistill --width 200 --height 150 --timeout 1 --output snap" + i + ".jpg --nopreview");
      Process snap = rt.exec("raspistill --timeout 5 --output snap" + i + ".jpg --nopreview");
      snap.waitFor(); // Sync
      long after = System.currentTimeMillis();
      System.out.println("Snapshot Take #" + i + " is complete in " + Long.toString(after - before) + " ms.");
    }
    System.out.println("Completed taking the snaps. You can view the snap (files ending with .jpg) in the project root folder i.e inside \\iot-pi4j-raspicamera ");
  }
}