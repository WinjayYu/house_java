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
}
