import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Vector;

import javax.annotation.PostConstruct;





/**
 * InfectStatistic
 * TODO
 *
 * @author 221701126_�ƾ���
 * @version 1.0
 * @since 2020/2/8
 */
class InfectStatistic {
	 
	
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
    	try {
    		File file = new File("../log/2020-01-22.log.txt");

    		InputStreamReader inputReader = new InputStreamReader(new FileInputStream(file));
    		BufferedReader bf = new BufferedReader(inputReader);
    		
    		String str;
    		while ((str = bf.readLine()) != null) {
    			System.out.println(str);	
    		}			
    		bf.close();			
    		inputReader.close();
    	
		} 
    	catch (IOException  e) {
			// TODO: handle exception
    		e.printStackTrace();
		}
    	//printOrders(orders);
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
