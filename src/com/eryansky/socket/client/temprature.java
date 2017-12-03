package com.eryansky.socket.client;

public class temprature {
   private int maxtemp;
   private int mintemp;
   private int thishumidty;
   private float thispressure;
   private float thisvisibility;
   
   
   public void setHumidty(int temp)
   {
	   this.thishumidty=temp;
   }
   public int getHumidty()
   {
	   return this.thishumidty;
   }
   
   public void setPressure(float temp)
   {
	   this.thispressure=temp;
   }
   public float getPressure()
   {
	   return this.thispressure;
   }
   
   public void setVisibility(float temp)
   {
	   this.thisvisibility=temp;
	   
   }
   public float getVisibility()
   {   
	   return this.thisvisibility;
   }
   
   public void setMaxtemp(int temp)
   {
	   maxtemp=temp;
   }
   public int getMaxtemp()
   {
	   return maxtemp;
   }
   
   public void setMintemp(int temp)
   {
	   mintemp=temp;
   }
   public int getMintemp()
   {
	   return mintemp;
   }
   
}
