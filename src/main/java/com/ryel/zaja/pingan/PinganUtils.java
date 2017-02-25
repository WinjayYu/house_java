package com.ryel.zaja.pingan;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class PinganUtils {
	/**
	 * 生成平安银行接口流水请求号
	 */
	public static String generateThirdLogNo() {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");// 设置日期格式
		String rdNum = df.format(new Date());
		Random random = new Random();
		int ird = random.nextInt(999999);
		String srd = String.format("%06d", ird);
		String thirdLogNo = rdNum + srd;
		return thirdLogNo;
	}

	public static String generateThirdHtId() {
		return "TH" + generateThirdLogNo();
	}

	public static String getOderId(String masterId, String datetamp) {
		int orderid;
		String orderids;
		java.util.Random r = new java.util.Random();
		while (true) {
			orderid = r.nextInt(99999999);
			if (orderid < 0)
				orderid = -orderid;
			orderids = String.valueOf(orderid);
			System.out.println("--orderids----" + orderids);
			if (orderids.length() < 8) {
				System.out.println("--order22222ids----" + orderids);
				continue;
			}
			if (orderids.length() >= 8) {
				orderids = orderids.substring(0, 8);
				System.out.println("--orderids222----" + orderids);
				break;
			}
		}
		return masterId + datetamp + orderids;
	}
}
