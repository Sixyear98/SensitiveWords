package com.perfect;

import com.perfect.support.ApplyConfig;
import com.perfect.support.OperatingSystem;
import com.perfect.util.FunctionUtil;


public class SensitiveMain {

	public static void main(String[] args) throws Exception {

		OperatingSystem.initialise();
		FunctionUtil.initialise(ApplyConfig.class);
		SensitiveWords.initialise();

		int totalTimes = 1000000;
		long start, finish, total = 0L;
//		String message = "测试ABCD-BC-就毛不特淫就泽成别女成懂不色公情图片得反恐委员会";
		String message = "政治反对派asmao.ze|dong⺿周.en。来 *&&^&^^^林若峰";
//		String message = "测试ABCD-BC-毛-----毛---泽-----泽---东毛-------东---泽-----东";
		System.out.println(message);
		{
			System.out.println("*********----contains----*********");
			int type = 0;
			for (int times = 0; times < 10; times++) {
				start = System.currentTimeMillis();
				for (int j = 0; j < totalTimes; j++) {
					type = SensitiveWords.contains(message);
				}
				finish = System.currentTimeMillis();
				total = finish - start;
				System.out.println("10万次搜索时间：" + total + " " + type);
			}
		}
		{
			System.out.println("*********----find----*********");
			String result = SensitiveWords.find(message);
			System.out.println("查找敏感字：" + result);
		}
		{
			char value = 21999;
			System.out.println(value);
		}
	}

}
