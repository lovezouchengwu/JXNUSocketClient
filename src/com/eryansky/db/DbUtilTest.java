package com.eryansky.db;

import java.util.List;
import java.util.Map;

/**
 * DbUtils测试类.
 */

public class DbUtilTest {

	public static void main(String[] args) {

		PacketDao dao = PacketDao.getInstance();
		try {
			//List<?> list = dao.getSubNetList();
			
			
			
			List<?> list=dao.getSeneorList();
			System.out.println(list.size());
//			if (list.size()== 0) {
//				System.out.println("目前未采集到甲醛浓度数据");
//			} else {
//				for (int i = 0; i < list.size(); i++) {
//					Map<?, ?> map = (Map<?, ?>) list.get(i);
//					String date = (String) map.get("Nowdate");
//					System.out.println(date);
					//int nodeID=Integer.valueOf((Integer) map.get("NodeID"));
					//int netID=Integer.valueOf((Integer) map.get("NetID"));
//					System.out.println("日期:" + map.get("Nowdate") + ",时间:"
//							+ map.get("Nowtime") + ",甲醛浓度:" + map.get("JQ")
//							+ ",检测结果:" + map.get("checkresult"));
//				}
			//}
		} catch (Exception e) {
			System.out.println("操作数据库失败," + e.getMessage());
			e.printStackTrace();
		}
	}

	private static int Integer(Object object) {
		// TODO Auto-generated method stub
		return 0;
	}
}