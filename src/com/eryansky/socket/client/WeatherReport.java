package com.eryansky.socket.client;

import java.io.File;import java.io.FileOutputStream;import java.io.IOException;import java.io.InputStream;import java.net.URL;import java.net.URLConnection;import java.util.HashMap;import javax.xml.parsers.DocumentBuilder;import javax.xml.parsers.DocumentBuilderFactory;import javax.xml.transform.Transformer;import javax.xml.transform.TransformerFactory;import javax.xml.transform.dom.DOMSource;import javax.xml.transform.stream.StreamResult;import org.w3c.dom.Document;import org.w3c.dom.NamedNodeMap;import org.w3c.dom.Node;import org.w3c.dom.NodeList;
class WeatherReport {  
	public static HashMap<String, String> cityCode = new HashMap<String, String>();  
	private final String[] dictionaryStrings = { "龙卷风", "热带风暴", "飓风", "强雷阵雨",      
			"雷阵雨", "小雨加雪", "雨加冰雹", "雪加冰雹", "冰雨", "毛毛雨", "冻雨", "阵雨", "阵雨", "小雪",    
			"零星小雪", "高吹雪", "雪", "冰雹", "雨夹雪", "尘", "雾", "薄雾", "多烟的", "大风", "有风",       
			"寒冷", "阴天", "夜间阴天", "白天阴天", "夜间多云", "白天多云", "夜间清亮", "晴朗", "转晴",      
			"转晴", "雨夹冰雹", "热", "雷阵雨", "雷阵雨", "雷阵雨", "雷阵雨", "大雪", "阵雪", "大雪",      
			"多云", "雷", "阵雪", "雷雨" };   
	public WeatherReport() {        initCitys();    }    /* 初始化城市代号 */ 
	private void initCitys() {        cityCode.put("北京", "0008");       
	cityCode.put("天津", "0133");        cityCode.put("武汉", "0138");     
	cityCode.put("杭州", "0044");        cityCode.put("合肥 ", "0448");    
	cityCode.put("上海 ", "0116");        cityCode.put("福州 ", "0031");     
	cityCode.put("重庆 ", "0017");        cityCode.put("南昌 ", "0097");     
	cityCode.put("香港 ", "0049");        cityCode.put("济南 ", "0064");      
	cityCode.put("澳门 ", "0512");        cityCode.put("郑州 ", "0165");      
	cityCode.put("呼和浩特 ", "0249");        cityCode.put("乌鲁木齐 ", "0135");   
	cityCode.put("长沙 ", "0013");        cityCode.put("银川 ", "0259");     
	cityCode.put("广州 ", "0037");        cityCode.put("拉萨 ", "0080");      
	cityCode.put("海口 ", "0502");        cityCode.put("南京 ", "0100");      
	cityCode.put("成都 ", "0016");        cityCode.put("石家庄 ", "0122");    
	cityCode.put("贵阳 ", "0039");        cityCode.put("太原 ", "0129");    
	cityCode.put("昆明 ", "0076");        cityCode.put("沈阳 ", "0119");    
	cityCode.put("西安 ", "0141");        cityCode.put("长春 ", "0010");     
	cityCode.put("兰州 ", "0079");        cityCode.put("西宁 ", "0236");    }   
	private Document getWeatherXML(String cityCode) throws IOException {      
		URL url = new URL("http://weather.yahooapis.com/forecastrss?p=CHXX"                + cityCode + "&u=c");     
		URLConnection connection = url.openConnection();      
		Document Doc = stringToElement(connection.getInputStream());    
		return Doc;    }  
	/* 保存获取的天气信息XML文档 */    
	private void saveXML(Document Doc, String Path) {   
		TransformerFactory transFactory = TransformerFactory.newInstance();       
		Transformer transformer;     
		try {            transformer = transFactory.newTransformer();          
		DOMSource domSource = new DOMSource(Doc);            File file = new File(Path);      
		FileOutputStream out = new FileOutputStream(file);        
		StreamResult xmlResult = new StreamResult(out);    
		transformer.transform(domSource, xmlResult);    
		} catch (Exception e) {        
			System.out.println("保存文件出错！");     
			}    }    /* 获取天气信息 */   
	
	
	public String getweather(String city)
	{
		String result =null;
		
		try {          
			Document doc = getWeatherXML(WeatherReport.cityCode.get(city));  
			NodeList nodeList = doc.getElementsByTagName("channel");         
			for (int i = 0; i < nodeList.getLength(); i++) {            
				Node node = nodeList.item(i);                NodeList nodeList1 = node.getChildNodes();       
				for (int j = 0; j < nodeList1.getLength(); j++) {               
					Node node1 = nodeList1.item(j);              
					if (node1.getNodeName().equalsIgnoreCase("item")) {             
						NodeList nodeList2 = node1.getChildNodes();                 
						for (int k = 0; k < nodeList2.getLength(); k++) {           
							Node node2 = nodeList2.item(k);          
							if (node2.getNodeName().equalsIgnoreCase(         
									"yweather:forecast")) {                 
								NamedNodeMap nodeMap = node2.getAttributes();         
								Node lowNode = nodeMap.getNamedItem("low");  
							
								Node highNode = nodeMap.getNamedItem("high");
							
								Node codeNode = nodeMap.getNamedItem("code");                 
								String day = "今天";                    
								if (result == null) {                 
									result = "";                            
									}
								else {    
										day = "\n明天";                      
										}                        
								result=result+lowNode.getNodeValue()+"|"+highNode.getNodeValue()+"|";
								/*
								result = result                      
										+ day                          
										+ " "                                    
										+ dictionaryStrings[Integer.parseInt(codeNode.getNodeValue())] + "\t最低温度：" + lowNode.getNodeValue()  + "℃ \t最高温度：" + highNode.getNodeValue() + "℃ ";
										*/ 
								}                  
							}       
						}          
					}       
				}     
			saveXML(doc, "C:\\Weather.xml");        } 
		
		catch (Exception e) {            e.printStackTrace();  
		System.out.println(e.getMessage());
		
		}   
		return result;    
		
	}
	
	public temprature getMaxWeather(String city) {    
		temprature result = new temprature();
		int Maxtempinfo;
		int Mintempinfo;
		try {          
			Document doc = getWeatherXML(WeatherReport.cityCode.get(city));  
			NodeList nodeList = doc.getElementsByTagName("channel");         
			for (int i = 0; i < nodeList.getLength(); i++) {            
				Node node = nodeList.item(i);                NodeList nodeList1 = node.getChildNodes();       
				for (int j = 0; j < nodeList1.getLength(); j++) {               
					Node node1 = nodeList1.item(j);              
					if (node1.getNodeName().equalsIgnoreCase("item")) {             
						NodeList nodeList2 = node1.getChildNodes();                 
						for (int k = 0; k < nodeList2.getLength(); k++) {           
							Node node2 = nodeList2.item(k);          
							if (node2.getNodeName().equalsIgnoreCase(         
									"yweather:forecast")) {                 
								NamedNodeMap nodeMap = node2.getAttributes();         
								Node lowNode = nodeMap.getNamedItem("low");  
							
								Node highNode = nodeMap.getNamedItem("high");
								
								if(highNode.getNodeValue()==null||highNode.getNodeValue()=="")
								{
									System.out.println("11111111111111okkkkkkkkkkkkkkkkkkk");
								}
								else
								{
									System.out.println(highNode.getNodeValue()+"okkkkkkkkkkkkkkkkkkk");
									Maxtempinfo=Integer.parseInt(highNode.getNodeValue());
									Mintempinfo=Integer.parseInt(lowNode.getNodeValue());
									System.out.println(Maxtempinfo);
									System.out.println(Mintempinfo);
									System.out.println("2222222okkkkkkkkkkkkkkkkkkk");
									result.setMaxtemp(Maxtempinfo);
									result.setMintemp(Mintempinfo);
									
								//result.setMintemp( Integer.parseInt(lowNode.getNodeValue()));
								//System.out.println(lowNode.getNodeValue()+"okkkkkkkkkkkkkkkkkkk");
							//	result.setMaxtemp(Maxtempinfo);
							//	result.setMintemp(Mintempinfo);
								
								}
								
							//	result=highNode.getNodeValue()+"|"+lowNode.getNodeValue();
								Node codeNode = nodeMap.getNamedItem("code");
																	
								String day = "今天";    
								
								
//								if (result == null) {                 
//									result = "";                                } else {    
//										day = "\n明天";                      
//										}                        
//								result = result                      
//										+ day                          
//										+ " "                                    
//										+ dictionaryStrings[Integer                                                .parseInt(codeNode                                                        .getNodeValue())]                                        + "\t最低温度：" + lowNode.getNodeValue()                                        + "℃ \t最高温度：" + highNode.getNodeValue()                                        + "℃ ";                        
								}                        }                    }                }            }     
	//		saveXML(doc, "C:\\Weather.xml");     
			} 
		
		catch (Exception e) {            e.printStackTrace();        }   
		return result; 
								}   
	

	public Document stringToElement(InputStream input) {   
		try {            DocumentBuilder db = DocumentBuilderFactory.newInstance()            
				.newDocumentBuilder();            Document doc = db.parse(input);            return doc;        } catch (Exception e) {            return null;        }    }
  public static void main(String arg[]) {    
	  WeatherReport info = new WeatherReport();

	  temprature result=info.getMaxWeather("南昌 ");
	  
	  
	//  int[] weather = info.getMaxWeather("南昌 ");
	  
	
	  System.out.println(result.getMaxtemp());    
	  System.out.println(result.getMintemp());
  
  }}
