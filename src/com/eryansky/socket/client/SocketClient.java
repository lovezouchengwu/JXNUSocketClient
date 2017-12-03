package com.eryansky.socket.client;

import java.io.OutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.eryansky.db.PacketDao;
import com.eryansky.socket.util.PacketUtil;
import com.eryansky.socket.util.ThreadUtils;

/**
 * ��ʱ����socket�ͷ���.
 * ��չӦ�ã�socket��Ȼ���������ţ���ʵ���Ͽ����Ѿ����ˣ���ô��ĳЩ�����Ĵ����£�����60����û���socket��ݣ�����������socket����
 * 
 */
public class SocketClient extends Thread {

	private Socket cilent;// socket�ͻ��˶���

	private String host = "117.40.138.25";// ������IP������
	private int port = 9001;// �������˿�

	// private String host = "127.0.0.1";// ������IP������

	// private int port = 8080;// �������˿�

	private int reConnectTime = 30 * 1000;

	private int sendTime = 5 * 60 * 1000;// ���ļ��ʱ��

	private OutputStream outStream; // �����

	private boolean isLive = false;// ��֤Socket�ͻ������̴߳��ı�־

	private boolean isConnected = false;// socket�����Ƿ�ɹ��ı�ʶ

	private boolean isPause = false;// socket��������ʱ�õ�����ͣ��ʶ

	private boolean isNeedSend = true;// �Ƿ���Ҫ�������� �ڵ����

	private SocketListener socketListener;// socket�������������߳�

	private PacketDao dao;// ��ݿ��������

	public SocketClient() {
		socketListener = new SocketListener();
		dao = PacketDao.getInstance();
	}

	/**
	 * ����socket������.
	 */
	private synchronized void connectSocket() throws Exception {
		try {// �ر�socket
			if (cilent != null) {
				try {
					if (cilent.isClosed() == false) {
						cilent.shutdownInput();
						cilent.close();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			cilent = new Socket(host, port);// ���������˷���δ���� �˴����׳��쳣
			cilent.setKeepAlive(true);
			cilent.setSoLinger(true, 0);
			cilent.setReceiveBufferSize(4 * 10240);
			outStream = cilent.getOutputStream();
			isConnected = true;
		} catch (Exception e) {
			System.out.println("连接Socket服务发生异常:" + e.getMessage());
			isConnected = false;
			ThreadUtils.sleep(reConnectTime);
		}
	}

	/**
	 * ��������socket������.
	 * 
	 */
	public void reconnectSocket() {
		try {
			isPause = true;
			connectSocket();
			isPause = false;
		} catch (Exception e) {
		}
	}

	/**
	 * �߳�����.
	 */
	public void start() {
		isLive = true;
		System.out.println("Socket客户端主线程启动.");
		socketListener.start();// ����socket�����߳�
		super.start();
	}

	/**
	 * �߳�ֹͣ.
	 */
	public void cancel() {
		socketListener.cancel();
		isLive = false;
		isPause = true;
		System.out.println("Socket客户端主线程启动.");
	}

	// ���������ݰ�

	public byte[] packetDQ(int battery, float light, float intersemapressure,
			float intersetemperature, int humidity, int netid, int nodeid,
			String Nowtime) {
		// ���Ĺ���
		// Message Type��2B
		byte[] type = PacketUtil.intToBytes(2);
		type = PacketUtil.subBytes(type, 0, 1);// 2B
		// Message SubType��2B
		byte[] subType = PacketUtil.intToBytes(35);
		subType = PacketUtil.subBytes(subType, 0, 1);// 2B

		byte[] data = PacketUtil.addBytes(type, subType);

		// Route NodeIDs
		// int[] node = { 4, 5, 0 };

		byte[] node = PacketUtil.stringToBytes("3003,3001");

		if (netid == 1001)
			node = PacketUtil.stringToBytes("3003,3001");

		if (netid == 1002)
			node = PacketUtil.stringToBytes("3004,3006");

		if (netid == 1003)
			node = PacketUtil.stringToBytes("3007,3008");

		// Route Length��1B
		byte[] routeLen = PacketUtil.intToBytes(node.length / 2);// 1B
		routeLen = PacketUtil.subBytes(routeLen, 0, 0);// 1B
		data = PacketUtil.addBytes(data, routeLen);
		data = PacketUtil.addBytes(data, node);
		/*
		 * for (int i = 0; i < node.length; i++) { byte[] NodeIDs =
		 * PacketUtil.intToBytes(node[i]);// 2*node.length B
		 * 
		 * // System.out.println("route node id :" + //
		 * PacketUtil.bytesToInt(NodeIDs));
		 * 
		 * data = PacketUtil.addBytes(data, NodeIDs); }
		 */

		// NetID��4B
		netid=4;
		byte[] netId = PacketUtil.intToBytes(netid);// 4B
		
		
		// NodeID��4B
		byte[] nodId = PacketUtil.intToBytes(nodeid);// 4B
		data = PacketUtil.addBytes(data, netId);
		data = PacketUtil.addBytes(data, nodId);

		// voltage��4B
		byte[] vt = PacketUtil.stringToBytes(String.valueOf(battery));// 4B
		// Voltage length 1B
		byte[] vtlen = PacketUtil.intToBytes(vt.length / 2);// 4B
		vtlen = PacketUtil.subBytes(vtlen, 0, 0);// 1B
		data = PacketUtil.addBytes(data, vtlen);
		data = PacketUtil.addBytes(data, vt);

		byte[] GTI = PacketUtil.stringToBytes(String.valueOf(light));
		byte[] GTILen = PacketUtil.intToBytes(GTI.length / 2);
		GTILen = PacketUtil.subBytes(GTILen, 0, 0);

		data = PacketUtil.addBytes(data, GTILen);
		data = PacketUtil.addBytes(data, GTI);

		byte[] GPR = PacketUtil
				.stringToBytes(String.valueOf(intersemapressure));

		byte[] GPRLen = PacketUtil.intToBytes(GPR.length / 2);

		GPRLen = PacketUtil.subBytes(GPRLen, 0, 0);

		data = PacketUtil.addBytes(data, GPRLen);
		data = PacketUtil.addBytes(data, GPR);

		byte[] GTE = PacketUtil.stringToBytes(String
				.valueOf(intersetemperature));

		byte[] GTELen = PacketUtil.intToBytes(GTE.length / 2);

		GTELen = PacketUtil.subBytes(GTELen, 0, 0);

		data = PacketUtil.addBytes(data, GTELen);
		data = PacketUtil.addBytes(data, GTE);

		byte[] GWE = PacketUtil.stringToBytes(String.valueOf(humidity));

		byte[] GWELen = PacketUtil.intToBytes(GWE.length / 2);

		GWELen = PacketUtil.subBytes(GWELen, 0, 0);

		data = PacketUtil.addBytes(data, GWELen);
		data = PacketUtil.addBytes(data, GWE);
		// time:
		byte[] time = PacketUtil.stringToBytes(Nowtime);

		// time length��1B
		byte[] timelen = PacketUtil.intToBytes(time.length / 2);// 4B
		timelen = PacketUtil.subBytes(timelen, 0, 0);// 1B
		data = PacketUtil.addBytes(data, timelen);
		data = PacketUtil.addBytes(data, time);

		// ����õ�����ͷ����
		byte[] head = PacketUtil.intToBytes(data.length);
		data = PacketUtil.addBytes(head, data);// ����õ��ֽ����� ���ɷ�������������
		return data;

	}

	// ���ڼ�ȩŨ����ݵı��ķ�װ
	public byte[] packetJQ(String jqvalue, int netid, int nodeid, String Nowtime) {
		// ���Ĺ���
		// Message Type��2B
		byte[] type = PacketUtil.intToBytes(2);
		type = PacketUtil.subBytes(type, 0, 1);// 2B
		// Message SubType��2B
		byte[] subType = PacketUtil.intToBytes(25);
		subType = PacketUtil.subBytes(subType, 0, 1);// 2B

		byte[] data = PacketUtil.addBytes(type, subType);

		// Route NodeIDs
		// int[] node = { 4, 5, 0 };
		byte[] node = PacketUtil.stringToBytes("1004,1111");

		// Route Length��1B
		byte[] routeLen = PacketUtil.intToBytes(node.length / 2);// 1B
		routeLen = PacketUtil.subBytes(routeLen, 0, 0);// 1B
		data = PacketUtil.addBytes(data, routeLen);
		data = PacketUtil.addBytes(data, node);
		/*
		 * for (int i = 0; i < node.length; i++) { byte[] NodeIDs =
		 * PacketUtil.intToBytes(node[i]);// 2*node.length B
		 * 
		 * // System.out.println("route node id :" + //
		 * PacketUtil.bytesToInt(NodeIDs));
		 * 
		 * data = PacketUtil.addBytes(data, NodeIDs); }
		 */

		// NetID��4B
		byte[] netId = PacketUtil.intToBytes(netid);// 4B
		// NodeID��4B
		byte[] nodId = PacketUtil.intToBytes(nodeid);// 4B
		data = PacketUtil.addBytes(data, netId);
		data = PacketUtil.addBytes(data, nodId);

		// voltage��4B
		byte[] vt = PacketUtil.stringToBytes("5");// 4B
		// Voltage length 1B
		byte[] vtlen = PacketUtil.intToBytes(vt.length / 2);// 4B
		vtlen = PacketUtil.subBytes(vtlen, 0, 0);// 1B
		data = PacketUtil.addBytes(data, vtlen);
		data = PacketUtil.addBytes(data, vt);

		// JQ 5B
		byte[] jq = PacketUtil.stringToBytes(jqvalue);
		// JQ length��1B
		byte[] jqlen = PacketUtil.intToBytes(jq.length / 2);// 4B
		jqlen = PacketUtil.subBytes(jqlen, 0, 0);// 1B
		data = PacketUtil.addBytes(data, jqlen);
		data = PacketUtil.addBytes(data, jq);
		// time:
		byte[] time = PacketUtil.stringToBytes(Nowtime);

		// time length��1B
		byte[] timelen = PacketUtil.intToBytes(time.length / 2);// 4B
		timelen = PacketUtil.subBytes(timelen, 0, 0);// 1B
		data = PacketUtil.addBytes(data, timelen);
		data = PacketUtil.addBytes(data, time);

		// ����õ�����ͷ����
		byte[] head = PacketUtil.intToBytes(data.length);
		data = PacketUtil.addBytes(head, data);// ����õ��ֽ����� ���ɷ�������������
		return data;

	}

	// ��������ڵ���ݵı��ķ�װ
	public byte[] packetNet(String IPaddress, int NetID) {
		// ���Ĺ��� �����Ǿ�̬���(ʵ�ʻ��� ��Ҫ����ݿ��ȡ��� ,Ȼ�����±��Ĺ��췽ʽ���ͱ���)
		// Message Type��2B
		byte[] type = PacketUtil.intToBytes(1);
		type = PacketUtil.subBytes(type, 0, 1);// 2B
		// Message SubType��2B
		byte[] subType = PacketUtil.intToBytes(21);
		subType = PacketUtil.subBytes(subType, 0, 1);// 2B
		// NetID��4B
		byte[] netId = PacketUtil.intToBytes(NetID);// 4B

		// IP
		byte[] ip = PacketUtil.stringToBytes(IPaddress);

		// IPAddress Length: 1B
		byte[] ipLen = PacketUtil.intToBytes(ip.length / 2);// �˴�����ΪIP�ַ�(unicode����)�����
		ipLen = PacketUtil.subBytes(ipLen, 0, 0);// 1B

		byte[] data = PacketUtil.addBytes(type, subType);
		data = PacketUtil.addBytes(data, netId);
		data = PacketUtil.addBytes(data, ipLen);
		data = PacketUtil.addBytes(data, ip);

		// ����õ�����ͷ����
		byte[] head = PacketUtil.intToBytes(data.length);
		data = PacketUtil.addBytes(head, data);// ����õ��ֽ�����
		// ���ɷ�������������
		return data;
	}

	// �����ӽڵ���ݵı��ķ�װ
	public byte[] packetNode(int netid, int nodeid, int nodetypeid,
			String latitute, String longitute, String altitute) {
		// ���Ĺ���
		// Message Type��2B
		byte[] type = PacketUtil.intToBytes(1);
		type = PacketUtil.subBytes(type, 0, 1);// 2B
		// Message SubType��2B
		byte[] subType = PacketUtil.intToBytes(33);
		subType = PacketUtil.subBytes(subType, 0, 1);// 2B
		byte[] data = PacketUtil.addBytes(type, subType);

		// NetID��4B
		byte[] netId = PacketUtil.intToBytes(netid);// 4B
		// NodeID��4B
		byte[] nodId = PacketUtil.intToBytes(nodeid);// 4B
		data = PacketUtil.addBytes(data, netId);
		data = PacketUtil.addBytes(data, nodId);
		// NodeTypeID��4B
		byte[] NodeTypeID = PacketUtil.intToBytes(nodetypeid);// 4B
		data = PacketUtil.addBytes(data, NodeTypeID);
		// Node Latitude
		//byte[] NodeLat = PacketUtil.stringToBytes(longitute);
		
		byte[] NodeLat = PacketUtil.stringToBytes(latitute);
		
		
		// Node Latitude Length 1B
		byte[] NodeLatlen = PacketUtil.intToBytes(NodeLat.length / 2);// 4B
		NodeLatlen = PacketUtil.subBytes(NodeLatlen, 0, 0);// 1B
		data = PacketUtil.addBytes(data, NodeLatlen);
		data = PacketUtil.addBytes(data, NodeLat);
		// Node Longtitude
		//byte[] NodeLgt = PacketUtil.stringToBytes(latitute);
		
		byte[] NodeLgt = PacketUtil.stringToBytes(longitute);
		
		
		// Node Longtitude Length 1B
		byte[] NodeLgten = PacketUtil.intToBytes(NodeLgt.length / 2);// 4B
		NodeLgten = PacketUtil.subBytes(NodeLgten, 0, 0);// 1B
		data = PacketUtil.addBytes(data, NodeLgten);
		data = PacketUtil.addBytes(data, NodeLgt);

		// Node Altitude
		byte[] NodeAt = PacketUtil.stringToBytes(altitute);
		// Node Altitude length��1B
		byte[] NodeAtlen = PacketUtil.intToBytes(NodeAt.length / 2);// 4B
		NodeAtlen = PacketUtil.subBytes(NodeAtlen, 0, 0);// 1B
		data = PacketUtil.addBytes(data, NodeAtlen);
		data = PacketUtil.addBytes(data, NodeAt);

		// ����õ�����ͷ����
		byte[] head = PacketUtil.intToBytes(data.length);
		data = PacketUtil.addBytes(head, data);// ����õ��ֽ����� ���ɷ�������������
		return data;
	}

	public void run() {
		
		GetWeatherInfo info=new GetWeatherInfo();
		
		temprature result = info.getWeather1("南昌");
		
        System.out.println(result);
		
		System.out.println("--------------test-------------nanchuang----");
		
		int min = result.getMintemp();
		int max = result.getMaxtemp();
		
		
		while (isLive) {// ��֤�̴߳��ı�־
			try {
				
			
				
				if (!isConnected) {// �ж�socket�Ƿ��Ѿ������ˣ���֤����socket
					connectSocket();
					continue;
				}
				while (!isPause) {// socket��ʱ�ؽ�ʱ�õ�����ͣ��ʶ
					try {
						if (isNeedSend) {// ����Ƿ���Ҫ��������ڵ����

							List<?> list1 = dao.getSubNetList();
							List<?> list2 = dao.getNodeList();
							for (int j = 0; j < list1.size(); j++) {
								Map<?, ?> map = (Map<?, ?>) list1.get(j);
								String ipaddress = (String) map
										.get("IPAddress");
								int netid = Integer.valueOf((Integer) map
										.get("netID"));
								netid=4;
								byte[] data1 = packetNet(ipaddress, netid);

								outStream.write(data1, 0, data1.length);// ���ͱ���

								String date1 = new SimpleDateFormat(
										"yyyyMMdd HHmmss").format(new Date());// ��ǰʱ��
								// ��ӡ��
								System.out.println(date1 + " 发送报文:"
										+ PacketUtil.bytesToHexString(data1));

							}

							for (int j = 0; j < list2.size(); j++) {
								Map<?, ?> map = (Map<?, ?>) list2.get(j);

								int netid = Integer.valueOf((Integer) map
										.get("NetID"));
								netid=4;
								int nodeid = Integer.valueOf((Integer) map
										.get("NodeID"));
								int nodetypeid = Integer.valueOf((Integer) map
										.get("NodeTypeID"));
								String latitude = (String) map
										.get("NodeLatitude");
								String longitude = (String) map
										.get("NodeLongitude");
								String altitude = String.valueOf(map
										.get("NodeAltitude"));
								byte[] data2 = packetNode(netid, nodeid,
										nodetypeid, latitude, longitude,
										altitude);
								PacketUtil.handlePacket(data2);// ��鱨���Ƿ���ȷ
																// ����ݴ�ӡ��־ �鿴��Ϣ��

								outStream.write(data2, 0, data2.length);// ���ͱ���
									//	"yyyy-MM-dd HH:mm:ss")
									//	.format(new Date());// ��ǰʱ��
								// ��ӡ��
								System.out.println(data2 + " 发送报文:"
										+ PacketUtil.bytesToHexString(data2));
								System.out.println(data2 + " 发送jidedian:" + nodeid);

							}
							isNeedSend = false;
						}

						// ����õ��ֽ�����
						// // ���ɷ�������������
						List<?> list = dao.getSeneorList();
						if (list.size() == 0) {
						

							

						} else {
							
							/*
							for (int j = 0; j < list.size(); j++) {
								Map<?, ?> map = (Map<?, ?>) list.get(j);
								// String JQNO = (String) map.get("JQ");

								int battery = Integer.valueOf((Integer) map
										.get("battery"));
								float light = Float.valueOf((Float) map
										.get("light"));
								float intersemapressure = Float
										.valueOf((Float) map
												.get("intersemapressure"));
								float intersetemperature = Float
										.valueOf((Float) map
												.get("intersetemperature"));
								int humidity = Integer.valueOf((Integer) map
										.get("humidity"));

								String time = new SimpleDateFormat(
										"yyyyMMddHHmmss").format((Date) map
										.get("Shijian"));//
								int nodeID = Integer.valueOf((Integer) map
										.get("NodeID"));
								// int ID = Integer.valueOf((Integer)
								// map.get("ID"));
								int netID = Integer.valueOf((Integer) map
										.get("NetID"));

								byte[] data = this.packetDQ(battery, light,
										intersemapressure, intersetemperature,
										humidity, netID, nodeID, time);
								// PacketUtil.handlePacket(data);//��鱨���Ƿ���ȷ
								// ����ݴ�ӡ��־ �鿴��Ϣ��
								outStream.write(data, 0, data.length);// ���ͱ���
								// dao.updateSensor("��", ID);
								String date = new SimpleDateFormat(
										"yyyyMMddHHmmss").format(new Date());// ��ǰʱ��

								// ��ӡ��
								System.out.println(date + " ���ʹ�����ݱ���:"
										+ PacketUtil.bytesToHexString(data));
								/*
								 * int jqlen2 = PacketUtil
								 * .bytesToInt(PacketUtil.subBytes( data, 34,
								 * 34)); System.out.println("JQ length  :" +
								 * PacketUtil.bytesToInt(PacketUtil
								 * .subBytes(data, 34, 34)));
								 */

								// System.out.println("Ũ��ֵ:"+
								// PacketUtil.bytesToString(PacketUtil.subBytes(data,35,35
								// + jqlen2 * 2 - 1)));

							//}
							/*  */
						
							/*
							WeatherReport info = new WeatherReport();
							*/
						
							System.out.println(result.getMaxtemp());
							
							System.out.println(result.getMintemp());
							
							List<?> list3 = dao.getNodeList();
							
							float intersetemperature = (float)(min+max)/2;
							
							int icount=0;
							
							
							int battery = 30004;

							//float intersemapressure = (float) 1015.843933;
							
							float intersemapressure=(float)result.getPressure();
							
							float light=(float)result.getVisibility();
							
							int humidity=result.getHumidty();
							
							int maxhumidity=result.getHumidty()+5;
							
							int minhumidity=result.getHumidty()-5;
							
							float maxlight=(float)result.getVisibility()+2;
							
							float minlight=(float)result.getVisibility()-1;
							
							float maxpress=(float)result.getPressure()+20;
							
							float minpress=(float)result.getPressure()-10;
							Date now = new Date();
							
							if (0 < now.getHours() && now.getHours() < 6) {
								intersetemperature = (float) (Math.random() * 1.0)+ min;	
							}
							
							if (6 < now.getHours() && now.getHours() < 9) {
								intersetemperature = (float) (Math.random() * 1.0)+ (min+max)/2;	
							}
							if (9 < now.getHours() && now.getHours() < 14) {
								intersetemperature=(float) (Math.random() * 1.0)+max;
								//intersetemperature = (float) (Math.random() * 1.0)+  max;	
							}
							if (14 < now.getHours() && now.getHours() < 19) {
								intersetemperature=(float) (Math.random() * 1.0)+(max+ min)/2+5;
								//intersetemperature = (float) (Math.random() * 1.0)+ (max+ min)/2;	
							}
							if (19 < now.getHours() && now.getHours() < 24) {
								intersetemperature = (float) (Math.random() * 1.0)+  min;	
							}
							intersetemperature=(float) (Math.random() * 1.0)+10;
							humidity=minhumidity+(int)(1+Math.random()*(2-1+1));
							light=minlight+(float)(Math.random() * 1.0);
							intersemapressure=minpress+(int)(1+Math.random()*(50-1+1));

							//int humidity =48;
							//float light=(float)12.705206;
							
							for (int j = 0; j < list3.size(); j++) {
							   Thread.sleep(30000);
								Map<?, ?> map = (Map<?, ?>) list3.get(j);			
								
								
								now = new Date();
							
								battery = battery - (int) battery / 10000;

								int nodeid = Integer.valueOf((Integer) map
										.get("NodeID"));

								int netID = Integer.valueOf((Integer) map
										.get("NetID"));

								String time = new SimpleDateFormat(
										"yyyyMMddHHmmss").format(now);//
								
								System.out.println(time+"shi jian");
								
							
							//	humidity=humidity+(int)(1+Math.random()*(4-1+1));

			
							
								
								if (0 < now.getHours() && now.getHours() < 6) {
									
									
									
									intersetemperature=intersetemperature+ (float) (Math.random()*1.000);
									humidity=maxhumidity-(int)(1+Math.random()*(2-1+1));
								
									light=maxlight-(float)(Math.random() * 1.000);
									
									
									intersemapressure = minpress	+ (float) (Math.random() * 2.000);
									intersemapressure=intersemapressure+(int) (Math.random() * (4-1+1));
									if(intersetemperature>(min+max)/2)
									{
										intersetemperature = (min+max)/2-(float)(Math.random()*1.0);
									}
									
									if(humidity>=maxhumidity)
							        {
							        	humidity=maxhumidity-(int)(1+Math.random()*(2-1+1));
							        }
									if(light>=maxlight)
									{
										light=maxlight-(float)(Math.random() * 1.000);
									}
								    if(intersemapressure>=(maxpress+minpress)/2)
								    {
								    	intersemapressure=intersemapressure- (float) (Math.random() * 2.000);
								    }	
								} else if (now.getHours() < 14
										&& now.getHours() > 7) {
									
									
									
									intersetemperature = intersetemperature
											+(float) max / (7 * min * 60 * 12)+(float)(Math.round(Math.random() * 2.00)); // 7
								//		humidity=humidity-(int)(1+Math.random()*(10-1+1));	// Сʱ*60���ӣ�һ���Ӵ�12�����
									
									if (intersetemperature > max) {
										intersetemperature = max-(float)(Math.round(Math.random() * 1.00));										
									}
									
									humidity=maxhumidity-(int)(1+Math.random()*(2-1+1));
									
									light=maxlight-(float)(Math.random() * 1.000);
								
									intersemapressure=intersemapressure+(int) (Math.random() * (4-1+1));
									if(intersetemperature>(min+max)/2)
									{
										intersetemperature = (min+max)/2-(float)(Math.random()*1.0);
									}
									
									if(humidity<=minhumidity)
							        {
							        	humidity=humidity-(int)(1+Math.round(Math.random()*(2-1+1)));
							        }
									if(light>=maxlight)
									{
										light=light-(float)(Math.random() * 1.000);
									}
								    if(intersemapressure>=maxpress)
								    {
								    	intersemapressure=intersemapressure- (float) (Math.random() * 2.000);
								    }
									
									
								} else if (now.getHours() > 14 && now.getHours() < 24) {
								intersetemperature = intersetemperature-(float) max / (7 * min * 60*12)+(float) Math.round((Math.random()*3));
									if (intersetemperature < min) {
										intersetemperature = min+(float)(Math.random() * 1.00);
										humidity=humidity+(int)(1+Math.round(Math.random()*(10-1+1)));									// Сʱ*60���ӣ�һ���Ӵ�12�����
									}
									
									
									humidity=maxhumidity-(int)(1+Math.round((Math.random()*(2-1+1))));
									
									light=maxlight-(float)(Math.round((Math.random() * 1.000)));
									
									
									intersemapressure=intersemapressure-(int) (Math.round(Math.random() * (4-1+1)));
									
									
									if(intersetemperature>(min+max)/2)
									{
										intersetemperature = (min+max)/2-(float)( Math.round(Math.random()*1.0));
									}
									
									if(humidity>=maxhumidity)
							        {
							        	humidity=maxhumidity-(int)(1+ Math.round(Math.random()*(3-1+1)));
							        }
									if(light<=minlight)
									{
										light=minlight+(float) Math.round((Math.random() * 1.000));
									}
								    if(intersemapressure<=minpress)
								    {
								    	intersemapressure=intersemapressure+ (float) Math.round(Math.random() * 2.000);
								    }	
									
								}

						

					//			humidity = humidity+ (int) (Math.random() * 3);
								
								System.out.println("battery, light,intersemapressure, intersetemperature,humidity, netID, nodeid+"+String.valueOf(battery)+"battery|"+String.valueOf(light)+"light|"+String.valueOf(intersemapressure)+"semapressure|"+String.valueOf(humidity)+"humidty|"+String.valueOf(intersetemperature)+"intersetemperature|"+String.valueOf(nodeid)+"|");
								
								time = new SimpleDateFormat(
										"yyyyMMddHHmmss").format(new Date());//
								
								byte[] data = this.packetDQ(battery, light,
										intersemapressure, intersetemperature,
										humidity, netID, nodeid, time);

								// System.out.println("Ŀǰ�������µĴ�����Ϣ��");
								System.out.println(time + " 发送报文:"
										+ PacketUtil.bytesToHexString(data));
								
								cilent = new Socket(host, port);//          ˷   δ      ˴    ׳  쳣
								cilent.setKeepAlive(true);
								cilent.setSoLinger(true, 0);
								cilent.setReceiveBufferSize(4 * 1024);
								outStream = cilent.getOutputStream();

								outStream.write(data, 0, data.length);// ���ͱ���
								
								outStream.flush();
								
								outStream.close();
								
								cilent.close();
				
								String date = new SimpleDateFormat(
										"yyyyMMddHHmmss").format(new Date());// ��ǰʱ��

								// ��ӡ��
								System.out.println(time + " 发送报文:"
										+ PacketUtil.bytesToHexString(data));
								if(j==list3.size()-1)
								{
									j=-1;
									Thread.sleep(180000);
								}
								icount++;
								System.out.println("-------"+icount);
						}
						}
						ThreadUtils.sleep(sendTime);
					} catch (java.net.SocketException socket_e) {
						isConnected = false;
						isNeedSend = true;
						System.out.println(socket_e.getMessage());
						
						socket_e.printStackTrace();
						
						// �������?������socketʱ�����׳��Ĵ���
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * ��ʱ��������Socket�����������߳� �ڲ���.
	 */
	class SocketListener extends Thread {
		private boolean flag = false;// Socket�������������̴߳��ı�־

		private int time;// �������������߳� ��ʱ������ѯ���ʱ��

		/**
		 * �߳�����.
		 */
		public void start() {
			System.out.println("Socket�������������߳�����.");
			flag = true;
			super.start();
		}

		/**
		 * �߳�ֹͣ.
		 */
		public void cancel() {
			flag = false;
			System.out.println("Socket�������������߳�ֹͣ.");
		}

		public void run() {
			while (flag) {
				try {
					// ��socket�Ѿ������˲��������������û���ϵ������������Ļ���������
					// ��ݲ�ͬ��ҵ����Ҫ�����������������������������������Ƶ������Ļ���socket����ʱ����
					if (!isConnected) {
						reconnectSocket();
					}
					ThreadUtils.sleep(time);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * ���������.
	 */
	public static void main(String[] args) {
		new SocketClient().start();// /����Socket�������߳�

	}
}