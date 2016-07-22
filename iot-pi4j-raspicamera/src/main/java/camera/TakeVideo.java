package camera;

public class TakeVideo
{
  
  public static void main(String[] args) throws Exception
  {
    Runtime rt = Runtime.getRuntime();
    System.out.println("Raspberry Camera is going to take two videos of 10 seconds each, be ready..");
    System.out.println("Started capturing video..");
    for (int i=1; i<=2; i++)
    {
      long before = System.currentTimeMillis();
      //The below raspivid command will take videos of 1000 millseconds (i.e 10 seconds) with the output file name as "video<i>.h264" (where <i> is the counter)
      Process snap = rt.exec("raspivid --timeout 10000 --output video" + i + ".h264");
      snap.waitFor(); // Sync
      long after = System.currentTimeMillis();
      System.out.println("Video Take #" + i + " is complete in " + Long.toString(after - before) + " ms.");
    }
    System.out.println("Video capturing is complete. You can view the videos (files ending with .h264) in the project root folder i.e inside \\iot-pi4j-raspicamera ");
  }
}