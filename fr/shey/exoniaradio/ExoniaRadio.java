package fr.shey.exoniaradio;

import java.net.URL;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.Port;

import javazoom.jl.player.JavaSoundAudioDevice;
import javazoom.jl.player.JavaSoundAudioDeviceFactory;
import javazoom.jl.player.Player;
import net.minecraft.client.Minecraft;

public class ExoniaRadio
{
  private String streamURL = "http://streaming.radio.funradio.fr/fun-1-48-192";
  private static Player player;
  private static float volume;

  public ExoniaRadio(float vol)
  {
	System.out.println("Initializing radio..");
    this.volume = vol;
  }
  
  public void startPlayer()
  {
	  try
	  {
	        Thread t = new Thread()
	        {
	          public void run()
	          {
	        	try
	        	{
	        		player = new Player(new URL(streamURL).openStream());
	        		Minecraft.getMinecraft().isRadioPlaying = true;
	        		JavaSoundAudioDevice.radioVolume = volume;
	        		player.play();
	        	}
	        	catch(Exception e)
	        	{
	        		e.printStackTrace();
	        	}
	          }
	        };
	        t.start();
	    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  public void stop()
  {
    if ((this.player != null) && (isPlaying()))
    {
      this.volume = 0.0F;
      JavaSoundAudioDevice.radioVolume = -1500.0F;
      ExoniaSlider.field_146134_p = 0.0F;
      this.player.close();
      Minecraft.getMinecraft().isRadioPlaying = false;
    }
  }
  
  public boolean isPlaying()
  {
    return Minecraft.getMinecraft().isRadioPlaying;
  }
  
  public static void setVolume(float f)
  {
    if (player != null)
    {
      volume = f;
      JavaSoundAudioDevice.radioVolume = f * 46.0F - 40.0F;
      if(f <= 0)
          JavaSoundAudioDevice.radioVolume = -1500.0F;
    }
  }
  
  public float getVolume()
  {
    return JavaSoundAudioDevice.radioVolume;
  }

}
