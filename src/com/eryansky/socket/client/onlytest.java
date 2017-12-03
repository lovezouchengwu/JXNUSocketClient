package com.eryansky.socket.client;

public class onlytest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	/*	WeatherReport info = new WeatherReport();
		temprature result = info.getMaxWeather("南昌");
		int min = result.getMintemp();
		int max = result.getMaxtemp();

		System.out.println(result.getMaxtemp());
		System.out.println(result.getMintemp());
	*/
		
		
//		GetWeatherInfo info = new GetWeatherInfo();
//        String weather = info.getWeather("南昌");
//        System.out.println(weather);
		GetWeatherInfo info=new GetWeatherInfo();
		
		temprature result = info.getWeather1("南昌");
		
        System.out.println(result.getMaxtemp());
		
		
	}

}
