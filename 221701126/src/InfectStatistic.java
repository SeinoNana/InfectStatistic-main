import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.annotation.PostConstruct;
import javax.print.attribute.standard.OutputDeviceAssigned;

import sun.nio.cs.ext.IBM037;






/**
 * InfectStatistic
 * TODO
 *
 * @author 221701126_�ƾ���
 * @version 1.0
 * @since 2020/2/8
 */
class InfectStatistic {
	private static String inputPath = "C://";
	private static String outputPath = "C://";
	private static String targetDate = "";
	private static ArrayList<String> provinceArray = new ArrayList<String>();
	private static Map<String, Province> map = new HashMap<String, Province>();
	 
	//�������в���ת���ɶ���洢����
	private static void solveArgs(String[] args, Vector<Order> orders) {
		int i = 0;
		int pos = 1;
		while(pos < args.length) {
			String arg = args[pos];
		//	System.out.println(pos + "-" + arg);
			if(arg.indexOf('-') == 0) {//��������
				
	    		Order oneOrder = new Order();
	    		oneOrder.orderName = arg;
	    		if(arg.equals("-log")) {//��������·��
	    			inputPath = args[pos + 1] + "\\";
	    		}
	    		else if(arg.equals("-out")) {//�������·��
	    			outputPath = args[pos + 1] + "\\";
	    		}
	    		else if(arg.equals("-date")) {//��������
	    			targetDate = args[pos + 1];
	    		}
	    		for(i = pos + 1; i < args.length; i++) {
	    			//System.out.println("I:" + i);
	    			String newArg = args[i];
	    			if(newArg.indexOf('-') != 0) {//���ǲ���
	    				oneOrder.orderParams.add(newArg);
	    			}
	    			else {
	    				pos = i;
	    				break;
	    			}
	    		}
	    		orders.add(oneOrder);
	    		if(i == args.length) {
	    			break;
	    		}
	    	}
			try {
				Thread.sleep(2000);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		
	}
	
	//��ӡ�����������
	private static void printOrders(Vector<Order> orders) {
		for(Order order : orders) {
			System.out.println("������Ϊ" + order.orderName);
			System.out.println("�������Ϊ");
			for(String param : order.orderParams) {
				System.out.println(param + " ");
			}
		}
	}
	
	//���Դ�ӡ���
	private static void printResult() {
		if(map.get("����") != null) {
			Province province  = map.get("����");
			System.out.println("������Ⱦ����" + province.infect);
			System.out.println("������������" + province.seeming);
			System.out.println("������������" + province.cured);
			System.out.println("������������" + province.dead);
		}
		if(map.get("����") != null) {
			Province province  = map.get("����");
			System.out.println("������Ⱦ����" + province.infect);
			System.out.println("������������" + province.seeming);
			System.out.println("������������" + province.cured);
			System.out.println("������������" + province.dead);
		}
	}
	
	//��ȡ���е�����
	private static int getNumber(String[] information) {
		//��ȡ����
		String numString = information[information.length - 1];
		int index = numString.indexOf("��");
		numString = numString.substring(0, index);
		int number = Integer.parseInt(numString);
		return number;
	}
	
	//����������ļ���ÿһ���ļ�
	private static void solveEveryFile(Vector<String> toHandleDate) {
		
		
		StringBuffer sb = new StringBuffer();
		for(String dateFile : toHandleDate) {
			dateFile = inputPath + dateFile + ".log.txt";
			try {
	    		File file = new File(dateFile);
	    		InputStreamReader inputReader = new InputStreamReader(new FileInputStream(file), "UTF-8");
	    		BufferedReader bf = new BufferedReader(inputReader);
	    		
//	    		File output = new File(outputPath + ".output.txt");
//	    		if(!output.exists()){
//	    			output.createNewFile();
//	    		}
//	    		FileWriter fileWriter = new FileWriter(output.getAbsoluteFile());
//	    		BufferedWriter bw = new BufferedWriter(fileWriter);
	    		
	    		
	    		String str;
	    		while ((str = bf.readLine()) != null && str.indexOf("//") != 0) {
	    			//System.out.println(str);
	    			String[] information = str.split("\\s+");
	    			//System.out.println(information[0]);
	    			String province = information[0];//��ȡ��ʡ��
	    			int number = getNumber(information);//ȡ����������
	    			
	    			if(map.get(province) != null) {//ʡ���Ѿ����ֹ�
	    				Province p = map.get(province);
	    				switch (information[1]) {
						case "����":
							if(information[2].equals("��Ⱦ����")) {
								p.infect += number;
								//System.out.println(num);
							}
							else {//���ƻ��ߵ����
								p.seeming += number;
							}
							break;
						case "��Ⱦ����":
							String p2 = information[3];//ȡ�������ʡ������
							if(map.get(p2) != null) {//����ʡ���Ѿ����ֹ�
								Province anotherProvince = map.get(p2);
								anotherProvince.infect += number;
								p.infect -= number;
							}
							break;
						case "���ƻ���":
							//�ж������뻹��ȷ��
							if(information[2].equals("����")) {
								String p3 = information[3];//ȡ�������ʡ������
								if(map.get(p3) != null) {//����ʡ���Ѿ����ֹ�
									Province anotherProvince = map.get(p3);
									anotherProvince.seeming += number;
									p.seeming -= number;
								}
							}
							else {//ȷ��
								p.infect += number;
								p.seeming -= number;
							}
							break;
						case "����":
							p.infect -= number;
							p.dead += number;
							break;
						case "����":
							p.infect -= number;
							p.cured += number;
							break;
						case "�ų�":
							p.seeming -= number;
							break;
						default:
							break;
						}
	    			}
	    			else {//ʡ�ݻ�δ���ֹ�
	    				Province p = new Province();
	    				switch (information[1]) {
						case "����":
							if(information[2].equals("��Ⱦ����")) {
								p.infect += number;
								//System.out.println(num);
							}
							else {//���ƻ��ߵ����
								p.seeming += number;
							}
							break;
						case "��Ⱦ����":
							String p2 = information[3];//ȡ�������ʡ������
							if(map.get(p2) != null) {//����ʡ���Ѿ����ֹ�
								Province anotherProvince = map.get(p2);
								anotherProvince.infect += number;
								p.infect -= number;
							}
							break;
						case "���ƻ���":
							//�ж������뻹��ȷ��
							if(information[2].equals("����")) {
								String p3 = information[3];//ȡ�������ʡ������
								if(map.get(p3) != null) {//����ʡ���Ѿ����ֹ�
									Province anotherProvince = map.get(p3);
									anotherProvince.seeming += number;
									p.seeming -= number;
								}
							}
							else {//ȷ��
								p.infect += number;
								p.seeming -= number;
							}
							break;
						case "����":
							p.infect -= number;
							p.dead += number;
							break;
						case "����":
							p.infect -= number;
							p.cured += number;
							break;
						case "�ų�":
							p.seeming -= number;
							break;
						default:
							break;
						}
	    				map.put(province, p);
	    			}
	    			//System.out.println(str);
	    			//bw.write(str);
	    		}			
	    		bf.close();		
	    	//	bw.close();
	    		inputReader.close();
	    	
			} 
	    	catch (IOException  e) {
				// TODO: handle exception
	    		e.printStackTrace();
			}
		}
		
	}
	
	//����date����
	private static void solveDateOrder(String targetDate) {
		//��ȡ����·���µ������ļ�
		File file = new File(inputPath);
		if(file.isDirectory()) {
			Vector<String> toHandleDate = new Vector<String>();//��ȡ����Ҫ�������������ļ�
			String[] fileNames = file.list(); // ���Ŀ¼�µ������ļ����ļ���
			boolean flag = false;
			for(String fileName : fileNames) {//�ضϺ�׺��
				fileName = fileName.substring(0, fileName.indexOf('.'));
				//���ڱȽ�
				if(fileName.compareTo(targetDate) <= 0) {
					toHandleDate.add(fileName);
				}
				else {
					flag = true;
					break;
				}
				//System.out.println(fileName);
			}
			if(flag == false) {
				System.out.println("���ڳ�����Χ");
				toHandleDate.clear();
			}
			else {
				flag = false;
			}
//			for(String aString : toHandleDate) {
//				System.out.println(aString);
//			}
			if(toHandleDate.size() > 0) {
				solveEveryFile(toHandleDate);
			}
		}
		
	}
	
	public static void main(String[] args) {
	
    	Vector<Order> orders = new Vector<Order>();
//    	try {
//    		if(args.length != 0 && !args[0].equals("list")) {
//        		System.out.println("��������ȷ������(list)");
//        		return;
//        	}
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
    	
    	solveArgs(args, orders);
//    	try {
//    		File file = new File("../log/2020-01-22.log.txt");
//
//    		InputStreamReader inputReader = new InputStreamReader(new FileInputStream(file));
//    		BufferedReader bf = new BufferedReader(inputReader);
//    		
//    		String str;
//    		while ((str = bf.readLine()) != null) {
//    			System.out.println(str);	
//    		}			
//    		bf.close();			
//    		inputReader.close();
//    	
//		} 
//    	catch (IOException  e) {
//			// TODO: handle exception
//    		e.printStackTrace();
//		}

//		try {
//
//				String content = "a dog will be write in file";
//
//				File file = new File("test_appendfile2.txt");
//
//				if(!file.exists()){
//
//					file.createNewFile();
//
//				}
//
//				FileWriter fileWriter = new FileWriter(file.getAbsoluteFile());
//
//				BufferedWriter bw = new BufferedWriter(fileWriter);
//
//				bw.write(content);
//
//				bw.close();
//
//				System.out.println("finish");
//
//		    } catch (IOException e) {
//
//		        e.printStackTrace();
//
//		    }


    	printOrders(orders);
    	System.out.println(inputPath);
    	System.out.println(outputPath);
    	solveDateOrder(targetDate);
    	printResult();
    	//setVariable(orders);
    }
}
class Order{
	String orderName;//������
	Vector<String> orderParams;//������Ĳ���
	 Order() {
		 orderParams =  new Vector<String>();
	}
}

class Province{
	String name;//ʡ��
	int infect;//��Ⱦ����
	int seeming;//��������
	int dead;//��������
	int cured;//��������
	
	public Province() {
		infect = seeming = dead = cured = 0;
	}
}
