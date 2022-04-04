import java.io.*;
import java.net.URL;
import javax.sound.sampled.*;

/**
 * <code>Music</code> is a child class of Thread and Its main function is to
 * play musics.
 * 
 * @author Nuo Chen
 * @author Yanbo Wang
 */
public class Music extends Thread {
   private Clip clip;
   /**
    * If the music need loop playback
    */
   private boolean loop = false;

   /**
    * The constructor of <code>Music</code>.
    * 
    * @param name The music file name.
    * @param loop If it need loop playback.
    */
   public Music(String name, boolean loop) {
      this.loop = loop;
      try {
         // Open an audio input stream.
         URL url = this.getClass().getClassLoader().getResource(name);
         AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
         // Get a sound clip resource.
         clip = AudioSystem.getClip();
         // Open audio clip and load samples from the audio input stream.
         clip.open(audioIn);
      } catch (UnsupportedAudioFileException e) {
         e.printStackTrace();
      } catch (IOException e) {
         e.printStackTrace();
      } catch (LineUnavailableException e) {
         e.printStackTrace();
      }

   }

   @Override
   public void run() {
      if (!loop) {
         clip.start();
      }
      if (loop) {
         clip.loop(10);
      }
   }

   /**
    * Get the whole time of music
    * 
    * @return The time of music.
    */
   public long getWholeTime() {
      return clip.getMicrosecondLength();
   }

   /**
    * Set the time of music
    * 
    * @param microseconds The time of music.
    */
   public void setTime(long microseconds) {
      clip.setMicrosecondPosition(microseconds);
   }

   /**
    * Stop the music
    */
   public void pause() {
      clip.stop();
   }

   /**
    * Get the time of music this moment.
    * 
    * @return The time of music this moment.
    */
   public long getTime() {
      return clip.getMicrosecondPosition();
   }
}
