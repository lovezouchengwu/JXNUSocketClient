package com.eryansky.socket.util;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * 报文解析工具类. 
 * <br>注:部分方法仅限该项目使用.
 * 
 * @author 温春平&wencp wencp@strongit.com.cn
 * @date 2012-9-18 下午3:34:34
 * @version
 */
public class PacketUtil {

	/*
	 * 16进制数字字符集
	 */
	private static String HEXSTRING = "0123456789ABCDEF";
	/*
	 * 编码方式
	 */
	public static final String CHARSET = "UTF-16LE";//UnicodeLittleUnmarked / UTF-16LE
	
	
	
	/**
	 * 根据字节数组转换字符串.
	 * @param src
	 * @return
	 */
	public static String bytesToString(byte[] src){  
		return hexStringToString(bytesToHexString(src));
	} 
	
	/**
	 * 字符串转换字节数组.
	 * @param str
	 * @return
	 */
	public static byte[] stringToBytes(String str){ 
		return hexStringToBytes(stringToHexString(str));
	} 
	
	
	/**
	 * 根据字节数组转换整型.
	 * @param intBytes
	 * @return
	 */
	public static int bytesToInt(byte[] intBytes) {
		int fromByte = 0;
		for (int i = 0; i < intBytes.length; i++) {
			int n = (intBytes[i] < 0 ? (int) intBytes[i] + 256 : (int) intBytes[i]) << (8 * i);
			fromByte += n;
		}
		return fromByte;
	}
	
	
	public  static int bytesToInt2(byte[] bytes) {  
        int addr = ((bytes[0] << 24) & 0xFF000000);  
        addr |= ((bytes[1] << 16) & 0xFF0000);  
        addr |= ((bytes[2] << 8) & 0xFF00);  
        addr |= bytes[3] & 0xFF;  
        return addr;  
    }
	
	/**
	 * 整型转字节数组.
	 * @param i
	 * @return 
	 */
	public static byte[] intToBytes(int i) {
		byte[] bytes = new byte[4];
		bytes[0] = (byte) (0xff & i);
        bytes[1] = (byte) ((0xff00 & i) >> 8);
        bytes[2] = (byte) ((0xff0000 & i) >> 16);
        bytes[3] = (byte) ((0xff000000 & i) >> 24);
        return bytes;
	}
	
	/**
	 * 整型转字节数组.
	 * @param i
	 * @return 
	 */
	public static byte[] intToBytes2(int i) {
		byte[] bytes = new byte[4];
		bytes[0] = (byte) ((0xff000000 & i) >> 24);
        bytes[1] = (byte) ((0xff0000 & i) >> 16);
        bytes[2] = (byte) ((0xff00 & i) >> 8);
        bytes[3] = (byte) (0xff & i);
        return bytes;
	}
	
	
	
	/**
	 * 将字符串编码成16进制数字.
	 * 
	 * @param str
	 * @return
	 */
	public static String stringToHexString(String str) {
		// 根据默认编码获取字节数组
		byte[] bytes = null;
		try {
			bytes = str.getBytes(CHARSET);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		StringBuilder sb = new StringBuilder(bytes.length * 2);
		// 将字节数组中每个字节拆解成2位16进制整数
		for (int i = 0; i < bytes.length; i++) {
			sb.append(HEXSTRING.charAt((bytes[i] & 0xf0) >> 4));
			sb.append(HEXSTRING.charAt((bytes[i] & 0x0f) >> 0));
		}
		return sb.toString();
	}
	
	/**
	 * 将16进制字符串解码成字符串.
	 * 
	 * @param hexStr
	 * @return
	 */
	public static String hexStringToString(String hexStr) {
		if(hexStr ==null || "".equals(hexStr) ){
			return null;
		}
		hexStr = hexStr.replaceAll(" ", "");
		byte[] baKeyword = new byte[hexStr.length() / 2];
		for (int i = 0; i < baKeyword.length; i++) {
			try {
				baKeyword[i] = (byte) (0xff & Integer.parseInt(
						hexStr.substring(i * 2, i * 2 + 2), 16));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		try {
			hexStr = new String(baKeyword, CHARSET);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return hexStr;
	}
	
	/**
	 * 将整型转换16进制字符串.
	 * @param i
	 * @return
	 * @author 温春平&wencp wencp@strongit.com.cn
	 */
	public static String intToHexString(int i) {
		return bytesToHexString(intToBytes(i));
	}
	
	/**
	 * 将整型转换16进制字符串.
	 * @param i
	 * @return
	 * @author 温春平&wencp wencp@strongit.com.cn
	 */
	public static String intToHexString2(int i) {
		return bytesToHexString(intToBytes2(i));
	}
	
	
	

	/**
	 * 将16进制字符串(BIG-ENDIAN方式的16进制字符串)解码成整型.
	 * @param hexStr
	 * @return
	 */
	public static int hexStringToInt(String hexStr) {
		hexStr = hexStr.replaceAll(" ", "");
		StringBuffer sb = new StringBuffer(hexStr.length());
		int len = hexStr.length() / 2;
		for (int i = len; i > 0; i--) {
			sb.append(hexStr.substring(2 * i - 2, 2 * i));
		}
		return Integer.parseInt(sb.toString().substring(0, hexStr.length()), 16);
	}

	
	
	/**
	 * 16进制字符串转换成字节数组.
	 * @param hexStr
	 * @return
	 */
	public static byte[] hexStringToBytes(String hexStr) { 
	    if (hexStr == null || hexStr.equals("")) {  
	        return new byte[0];  
	    }  
	    hexStr = hexStr.toUpperCase();  
	    int length = hexStr.length() / 2;  
	    char[] hexChars = hexStr.toCharArray();  
	    byte[] d = new byte[length];  
	    for (int i = 0; i < length; i++) {  
	        int pos = i * 2;  
	        d[i] = (byte) (HEXSTRING.indexOf(hexChars[pos]) << 4 | HEXSTRING.indexOf(hexChars[pos + 1]));  
	    }  
	    return d;  
	}  
	
	/**
	 * 字节数组转换16进制字符串.
	 * 这里我们可以将byte转换成int，然后利用Integer.toHexString(int)来转换成16进制字符串。  
	 * @param bytes byte[] data  
	 * @return hex string  
	 */     
	public static String bytesToHexString(byte[] bytes){ 
		if(bytes.length<=0){
			return null;
		}
	    StringBuilder stringBuilder = new StringBuilder("");  
	    if (bytes == null || bytes.length <= 0) {  
	        return null;  
	    }  
	    for (int i = 0; i < bytes.length; i++) {  
	        int v = bytes[i] & 0xFF;  
	        String hv = Integer.toHexString(v);  
	        if (hv.length() < 2) {  
	            stringBuilder.append(0);  
	        }  
	        stringBuilder.append(hv);  
	    }  
	    return stringBuilder.toString().toUpperCase();  
	} 
	
	
	

	

	/**
	 * 字节数组截取.
	 * @param bytes 字节数组
	 * @param beginIndex 开始位置
	 * @param endIndex 结束位置
	 * @return
	 */
	public static byte[] subBytes(byte[] bytes, int beginIndex, int endIndex){
		int size = endIndex-beginIndex + 1;
		byte[] data = new byte[size];
		for(int i = 0;i<size;i++){
			data[i] = bytes[beginIndex++];
		}
		return data;
		
	}
	/**
	 * 字节数组相加.
	 * <br>将src追加到dest.
	 * @param dest
	 * @param src
	 * @return
	 */
	public static byte[] addBytes(byte[] dest, byte[] src){
		byte[] data = new byte[dest.length + src.length];
		//1.copy数组(效率更高)
		System.arraycopy(dest, 0, data, 0, dest.length);
		System.arraycopy(src, 0, data, dest.length, src.length);
		/*
		//2.自定义copy数组
		for(int i = 0;i<bytes1.length;i++){
			data[i] = bytes1[i];
		}
		int initlen = bytes1.length;
		for(int j = 0;j<bytes2.length;j++){
			data[initlen++] = bytes2[j];
		}
		*/
		return data;
		
	}
	
	
	/**
	 * 将字符串转成unicode.
	 * 
	 * @param str
	 *            待转字符串
	 * @return unicode字符串
	 */
	public static String stringToUnicodeString(String str) {
		str = (str == null ? "" : str);
		String tmp;
		StringBuffer sb = new StringBuffer(1000);

		char c;
		int i, j;
		sb.setLength(0);
		for (i = 0; i < str.length(); i++) {
			c = str.charAt(i);
			sb.append("\\u");
			j = (c >>> 8); // 取出高8位
			tmp = Integer.toHexString(j);
			if (tmp.length() == 1)
				sb.append("0");
			sb.append(tmp);
			j = (c & 0xFF); // 取出低8位
			tmp = Integer.toHexString(j);
			if (tmp.length() == 1)
				sb.append("0");
			sb.append(tmp);

		}
		return (new String(sb));
	}

	/**
	 * 将unicode转换成字符串.
	 * 
	 * @param unicodeStr
	 *            待转字符串
	 * @return 普通字符串
	 */
	public static String unicodeStringToString(String unicodeStr) {
		unicodeStr = (unicodeStr == null ? "" : unicodeStr);
		if (unicodeStr.indexOf("\\u") == -1)// 如果不是unicode码则原样返回
			return unicodeStr;

		StringBuffer sb = new StringBuffer(1000);

		for (int i = 0; i < unicodeStr.length() - 6;) {
			String strTemp = unicodeStr.substring(i, i + 6);
			String value = strTemp.substring(2);
			int c = 0;
			for (int j = 0; j < value.length(); j++) {
				char tempChar = value.charAt(j);
				int t = 0;
				switch (tempChar) {
				case 'a':
					t = 10;
					break;
				case 'b':
					t = 11;
					break;
				case 'c':
					t = 12;
					break;
				case 'd':
					t = 13;
					break;
				case 'e':
					t = 14;
					break;
				case 'f':
					t = 15;
					break;
				default:
					t = tempChar - 48;
					break;
				}

				c += t * ((int) Math.pow(16, (value.length() - j - 1)));
			}
			sb.append((char) c);
			i = i + 6;
		}
		return sb.toString();
	}
	
	/**
	 * 测试.
	 * @param args
	 * @throws Exception
	 * @author 温春平&wencp wencp@strongit.com.cn
	 */
	public static void main(String[] args) throws Exception {
		/*String packet = "23 00 00 00 01 00 01 00 01 00 00 00 0D 31 00 30 00 2E 00 31 00 34 00 2E 00 32 00 30 00 30 00 2E 00 32 00 32 00 31 00";
		packet = packet.replaceAll(" ", "");
		byte[] data = PacketUtil.hexStringToBytes(packet);*/
		
		
		//报文构造
		//Message Type:2B
		byte[] type = PacketUtil.intToBytes(1);
		type = PacketUtil.subBytes(type, 0, 1);//2B 
		//Message SubType:2B
		byte[] subType = PacketUtil.intToBytes(1);
		subType = PacketUtil.subBytes(subType, 0, 1);//2B
		//NetID:4B
		byte[] netId = PacketUtil.intToBytes(1);//4B
		
		//IP
		byte[] ip = PacketUtil.stringToBytes("10.14.200.221");
		
		//IPAddress Length: 1B
		byte[] ipLen = PacketUtil.intToBytes(ip.length/2);//此处长度IP字符(unicode编码)个数长度
		ipLen = PacketUtil.subBytes(ipLen, 0, 0);//1B
		
		
		byte[] data = PacketUtil.addBytes(type, subType);
		data = PacketUtil.addBytes(data, netId);
		data = PacketUtil.addBytes(data, ipLen);
		data = PacketUtil.addBytes(data, ip);
		
		//计算得到报文头长度
		byte[] head = PacketUtil.intToBytes(data.length);
		data =  PacketUtil.addBytes(head, data);//构造好的字节数组 即可发送至服务器端
		
		PacketUtil.handlePacket(data);//检查报文是否正确 （根据打印日志 查看信息）
		
		/*System.out.println("Packet:" +PacketUtil.bytesToHexString(data));
		//报文解析 读取网络字节流
		System.out.println("Message Size:" + PacketUtil.bytesToInt(subBytes(data, 0, 3)));
		System.out.println("Message Type:" + PacketUtil.bytesToInt(subBytes(data, 4, 5)));
		System.out.println("Message SubType:" + PacketUtil.bytesToInt(subBytes(data, 6, 7)));
		
		System.out.println("子网号:" + PacketUtil.bytesToInt(subBytes(data, 8, 11)));
		int ipLen2 = PacketUtil.bytesToInt(subBytes(data, 12, 12));
		System.out.println("IP地址长度:" + ipLen2);
		System.out.println("IP地址:" + PacketUtil.bytesToString(subBytes(data, 13,ipLen2*2 + 13-1)));*/
		
		
	}
	
	 /**
     * 报文处理 解析.
     * @param bytes 报文字节数组(包含报文头)
     * @throws Exception
     */
	public static void  handlePacket( byte[] bytes) throws Exception{
		int netCode;// 子网号
	    int nodeCode;// 节点号
	    
	    int ipLen;// IP地址长度
		String ip;//IP
	    
	    int routeLen;// 路由数组长度
	    String routes;// 路由数组
	    
	    int valueLen; // 数据长度
	    String value;// 数据值
	    
	    int len; // 下一条数据起始位置
	    
	    int sendTimeLen;// 时间长度
	    String sendTime;// 信息发送时间String格式
	    Date time; // 信息发送时间
		
	    SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss") ;// 格式化日期
		System.out.println("报文："+PacketUtil.bytesToHexString(bytes));

		//去除报文头后的报文
		byte[] data = PacketUtil.subBytes(bytes, 4, bytes.length-1);
		try {
			//上行消息类型
			int type = PacketUtil.bytesToInt(PacketUtil.subBytes(data, 0, 1));
			System.out.println("上行网络管理消息类型:"+type);
			//上行消息子类型
			int subType = PacketUtil.bytesToInt(PacketUtil.subBytes(data, 2, 3));
			System.out.println("新增子网消息子类型:"+subType);
			
			switch (type) {
			case 1://上行网络管理消息
				switch (subType) {
				case 1://上行网络管理消息
				case 21://新增甲醇子网消息子类型
				case 31://新增大气子网消息子类型
					//新增子网...
					netCode = PacketUtil.bytesToInt(PacketUtil.subBytes(data, 4, 7));// 子网号 4B
					ipLen = PacketUtil.bytesToInt(PacketUtil.subBytes(data, 8, 8));// 1B，IP地址长度
					ip = PacketUtil.bytesToString(PacketUtil.subBytes(data, 9, 9+ipLen*2 -1));//IP地址，长度由ipLen决定
					
					System.out.println("子网号:"+netCode);
					System.out.println("IP地址长度:" + ipLen);
					System.out.println("IP:" + ip);
					break;
				case 2://新增水质节点消息子类型
				case 22://新增甲醛节点消息子类型
				case 32://新增大气节点消息子类型
				case 3://修改水质节点消息子类型
				case 23://修改甲醇节点消息子类型
				case 33://修改大气节点消息子类型
					// 新增节点/修改节点...	
					
					netCode = PacketUtil.bytesToInt(PacketUtil.subBytes(data, 4, 7));// 子网号 4B
					nodeCode = PacketUtil.bytesToInt(PacketUtil.subBytes(data, 8, 11));//节点号 4B
					System.out.println("子网号:"+netCode);
					System.out.println("节点号:"+nodeCode);
					
					//纬度
					int latLen = PacketUtil.bytesToInt(PacketUtil.subBytes(data, 16, 16));//1B，节点纬度长度
					String lat = PacketUtil.bytesToString(PacketUtil.subBytes(data, 17, 17+latLen*2 -1));//节点纬度
					double latitude = Double.parseDouble(lat);
					System.out.println("节点纬度长度::"+latLen);
					System.out.println("节点纬度::"+latitude);
					
					// 下一条数据起始位置
					len = 17+latLen*2;
					//经度
					int lonLen = PacketUtil.bytesToInt(PacketUtil.subBytes(data, len, len));//1B，节点经度长度
					String lon = PacketUtil.bytesToString(PacketUtil.subBytes(data, len +1, (len +1)+lonLen*2-1));//节点经度
					double longtitude = Double.parseDouble(lon);
					System.out.println("节点经度长度::"+lonLen);
					System.out.println("节点经度::"+longtitude);
					
					// 下一条数据起始位置
					len = (len +1)+lonLen*2;
					//海拔
					int altiLen = PacketUtil.bytesToInt(PacketUtil.subBytes(data, len, len ));//1B，节点海拔长度
					String alt = PacketUtil.bytesToString(PacketUtil.subBytes(data, len +1, (len +1)+altiLen*2 -1));//节点海拔
					double altitude = Double.parseDouble(alt);
					System.out.println("节点海拔长度::"+altiLen);
					System.out.println("节点海拔::"+altitude);
					
					
					break;
					
				case 4://删除水质节点消息子类型
				case 24://删除甲醇节点消息子类型
				case 34://删除大气节点消息子类型
					//删除节点...
					nodeCode = PacketUtil.bytesToInt(PacketUtil.subBytes(data, 8, 11));// 节点号 4B
					System.out.println("节点号::"+nodeCode);
					break;

				default:
					System.out.println("上行网络管理消息,非法消息子类型.");
					break;
				}

				break;
			case 2://上行数据消息
				switch (subType) {
				case 5://水质数据 水温数据  
				case 6://水质数据 PH数据  
				case 7://水质数据  电导率数据
				case 8://水质数据 溶解氧数据 
				case 9://水质数据  浊度数据  
				case 25://甲醛数据 甲醇感知数据
					
					// 路由数组
					routeLen = PacketUtil.bytesToInt(PacketUtil.subBytes(data, 4, 4));//1B，路由数组长度
					routes = PacketUtil.bytesToString(PacketUtil.subBytes(data, 5, 5+routeLen*2 -1));//路由数组
					System.out.println("路由数组长度:"+routeLen);
					System.out.println("数据路由数组:"+routes);
					
					// 下一条数据起始位置
					len = 5+routeLen*2;
					netCode = PacketUtil.bytesToInt(PacketUtil.subBytes(data, len, len + 3));//子网号  4B   6,9
					nodeCode = PacketUtil.bytesToInt(PacketUtil.subBytes(data, (len + 3) +1, ((len + 3) +1) +3));//节点号4B    10,13
					System.out.println("数据子网号:"+netCode);
					System.out.println("数据节点号:"+nodeCode);
					
					// 下一条数据起始位置
					len = ((len + 3) +1) +3;
					// 电压数据
					valueLen = PacketUtil.bytesToInt(PacketUtil.subBytes(data, len + 1, len + 1));// 数据长度  1B
					value = PacketUtil.bytesToString(PacketUtil.subBytes(data, (len + 1) +1, ((len + 1) +1)+valueLen*2 -1));//数据  1B
					System.out.println("电压数据长度:"+valueLen);
					System.out.println("电压数据:"+value);
					
					// 下一条数据起始位置
					len = ((len + 1) +1)+valueLen*2;
					// 水温数据、PH数据等
					valueLen = PacketUtil.bytesToInt(PacketUtil.subBytes(data, len , len ));// 数据长度  1B
					value = PacketUtil.bytesToString(PacketUtil.subBytes(data, (len + 1) , (len + 1 )+valueLen*2 -1));//数据  1B
					System.out.println("数据长度:"+valueLen);
					System.out.println("数据:"+value);
					
					// 下一条数据起始位置
					len = (len + 1 )+valueLen*2 ;
					// 报文发送时间
					
					sendTimeLen = PacketUtil.bytesToInt(PacketUtil.subBytes(data, len, len));// 发送报文时间长度   1B
					sendTime = PacketUtil.bytesToString(PacketUtil.subBytes(data, len + 1, (len +1)+sendTimeLen*2 -1));// 报文时间
					time = format.parse(sendTime);// yyyyMMddHHmmss 时间格式
					System.out.println("发送报文时间长度:"+sendTimeLen);
					System.out.println("发送报文时间:"+sendTime);

					
					break;
				case 10://水质 中继感知数据子类型0x000A
					//注释 暂不处理
					
					break;
				case  35://大气感知数据子类型0x0023 (光照、气压、温度以及空湿度)
					// 路由数据
					routeLen = PacketUtil.bytesToInt(PacketUtil.subBytes(data, 4, 4));//1B，路由数组长度
					routes = PacketUtil.bytesToString(PacketUtil.subBytes(data, 5, 5+routeLen*2 -1));//1B，路由数组
					System.out.println("路由数组长度:"+routeLen);
					System.out.println("路由数组:"+routes);
					
					// 下一条数据起始位置
					len = 5+routeLen*2;
					netCode = PacketUtil.bytesToInt(PacketUtil.subBytes(data, len, len+3));//子网号  4B
					nodeCode = PacketUtil.bytesToInt(PacketUtil.subBytes(data, (len+3)+1, ((len+3)+1)+3));//节点号  4B
					System.out.println("大气子网号:"+netCode);
					System.out.println("大气节点号:"+nodeCode);
					
					// 下一条数据起始位置
					len = ((len+3)+1)+3;
					// 电压数据
					valueLen = PacketUtil.bytesToInt(PacketUtil.subBytes(data, len + 1, len + 1));// 数据长度  1B
					value = PacketUtil.bytesToString(PacketUtil.subBytes(data, (len + 1) +1, ((len + 1) +1)+valueLen*2 -1));//数据  1B
					System.out.println("电压数据长度:"+valueLen);
					System.out.println("电压数据:"+value);
					
					// 下一条数据起始位置
					len = ((len + 1) +1)+valueLen*2;
					// 光照数据
					int sunDataLen = PacketUtil.bytesToInt(PacketUtil.subBytes(data, len , len ));// 光照数据长度  1B
					String sunDataValue = PacketUtil.bytesToString(PacketUtil.subBytes(data, len +1, (len +1)+sunDataLen*2 -1));//光照数据
					System.out.println("光照数据长度:"+sunDataLen);
					System.out.println("光照数据:"+sunDataValue);
					
					// 下一条数据起始位置
					len = (len +1)+sunDataLen*2;
					// 气压数据
					int gprDataLen = PacketUtil.bytesToInt(PacketUtil.subBytes(data, len, len));// 气压数据长度
					String gprDataValue = PacketUtil.bytesToString(PacketUtil.subBytes(data, len+1, (len+1)+gprDataLen*2 -1));//气压数据
					System.out.println("气压数据长度:"+gprDataLen);
					System.out.println("气压数据:"+gprDataValue);
					// 下一条数据起始位置
					len = (len+1)+gprDataLen*2;
					// 温度数据
					int temDataLen = PacketUtil.bytesToInt(PacketUtil.subBytes(data, len, len));// 温度数据长度
					String temDataValue = PacketUtil.bytesToString(PacketUtil.subBytes(data, len+1, (len+1)+temDataLen*2 -1));//温度数据
					System.out.println("温度数据长度:"+temDataLen);
					System.out.println("温度数据:"+temDataValue);
					
					// 下一条数据起始位置
					len = (len+1)+temDataLen*2;
					// 空湿度数据
					int gweDataLen = PacketUtil.bytesToInt(PacketUtil.subBytes(data, len, len));// 空湿度数据长度
					String gweDataValue = PacketUtil.bytesToString(PacketUtil.subBytes(data, len+1, (len+1)+gweDataLen*2 -1));//空湿度数据
					System.out.println("空湿度数据长度:"+gweDataLen);
					System.out.println("空湿度数据:"+gweDataValue);
					// 下一条数据起始位置
					len = (len+1)+gweDataLen*2;
					// 发送报文时间
					sendTimeLen = PacketUtil.bytesToInt(PacketUtil.subBytes(data, len, len));// 发送报文时间长度
					sendTime = PacketUtil.bytesToString(PacketUtil.subBytes(data, len+1, (len+1)+sendTimeLen*2 -1));// 发送报文时间
					time = format.parse(sendTime);// yyyyMMddHHmmss 时间格式
					System.out.println("发送报文时间长度:"+sendTimeLen);
					System.out.println("发送报文时间:"+sendTime);
					break;

				default:
					System.out.println("上行网络数据消息,非法消息子类型.");
					break;
				}

				break;

			default:
				System.out.println("非法消息类型.");
				break;
			}
			
		}catch (Exception e) {
			System.out.println("报文处理,异常:"+e.getMessage());
		}
	}
	

}
