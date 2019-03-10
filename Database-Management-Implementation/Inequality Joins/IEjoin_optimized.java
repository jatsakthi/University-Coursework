package tests;

import java.io.*;
import java.util.*;

import com.opencsv.CSVReader;

import javafx.util.Pair;

public class IEjoin_optimized {
	public IEjoin_optimized(){
		//System.out.println("Optimized Join in process.....");
	}
	
	
	public static Integer IEjoin(TwoPredicate tp)
	{
		
		int tuplesCount = 0;
		
		double sampling_rate= 0.01;
		
		
		int his=1;

		//System.out.println("Optimized Join in process.....");
		//Scanner n = new Scanner(System.in);
		//String fileName = "res/"+Filename;
		String line = null;
		String relations [] = null;
		String[] query = new String [5];
		int temp2pos =0,temp1pos=0,countlast=1;
		

		int lineNumber = 0,x,xdash,y,ydash,op1,op2,off2,off1,t,last,b,ano,rev=0,orderpos;
		int start,end,current,left;
		String roneTypes = null;
		String rtwoTypes = null;
		ArrayList<ArrayList<Integer>> maps = new ArrayList<ArrayList<Integer>>();
		ArrayList<Integer> temp = new ArrayList<Integer>();
		List<Integer> temp1 = new ArrayList<Integer>();
		ArrayList<Integer> empty = new ArrayList<Integer>();
		List<Integer> temp2 = new ArrayList<Integer>();
		List<Integer> order = new ArrayList<Integer>();
		ArrayList<Integer> l1 = new ArrayList<Integer>();
		ArrayList<Integer> l2 = new ArrayList<Integer>();
		ArrayList<Integer> l1pos = new ArrayList<Integer>();
		ArrayList<Integer> l2pos = new ArrayList<Integer>();
		ArrayList<Integer> p = new ArrayList<Integer>();
		ArrayList<Integer> pdash = new ArrayList<Integer>();
		ArrayList<Integer> l1dash = new ArrayList<Integer>();
		ArrayList<Integer> l2dash = new ArrayList<Integer>();
		ArrayList<Integer> l1dashpos = new ArrayList<Integer>();
		ArrayList<Integer> l2dashpos = new ArrayList<Integer>();
		ArrayList<Integer> o1 = new ArrayList<Integer>();
		ArrayList<Integer> o2 = new ArrayList<Integer>();
		ArrayList<Integer> bdash = new ArrayList<Integer>();
		ArrayList<Integer> inter = new ArrayList<Integer>();
		ArrayList<Integer> answer = new ArrayList<Integer>();
		ArrayList<Integer> inter1 = new ArrayList<Integer>();
		try {
			
			/*
            // FileReader reads text files in the default encoding.
            FileReader fileReader =new FileReader(fileName);
            
            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader =new BufferedReader(fileReader);

            while((line = bufferedReader.readLine()) != null) 
            {   
               		query[lineNumber] = line;
            		lineNumber++;			
            }   
            relations = query[1].split(" ");

            // Always close files.
            bufferedReader.close();
            //System.out.println(relations[0]);
            */
            
            // INITIALIZATION process
         
            
            x= tp.getLHS1Column();
            y= tp.getRHS1Column();
            
            xdash=tp.getLHS2Column();
            
            ydash=tp.getRHS2Column();
            
            op1= tp.getLHSOperator();
            op2= tp.getRHSOperator();
            		
            
            //histogram *********************************************************************************************
             
            /*
             Histogram h = new Histogram();

             
             l1=h.getHistogram(tp.getLHS1(),tp.getLHS1Column(),his);
         
             
             l2=h.getHistogram(tp.getLHS2(),tp.getLHS2Column(),his);
        
 			
 			l1dash=h.getHistogram(tp.getRHS1(),tp.getRHS1Column(),his);
            
            l2dash=h.getHistogram(tp.getRHS2(),tp.getRHS2Column(),his);		
       
            
            
            System.out.println("l1 " + l1.size() );
           */
            
           // *********************************************************************************************************************************
            
           
            
            LineNumberReader  lnr = new LineNumberReader(new FileReader("res/"+tp.getLHS1()+".txt"));
            lnr.skip(Long.MAX_VALUE);
            int number_of_records=lnr.getLineNumber();
            //System.out.println("R table has records : "+(number_of_records)); //Add 1 because line index starts at 0
            lnr.close();
            
            int u= (int) Math.floor(number_of_records * sampling_rate);
            int randomR[]=new int[u];
            
           // System.out.println("no. of files in first after sampling " + u);
            
            ArrayList<Integer> listl = new ArrayList<Integer>();
            for (int i=1; i<number_of_records+1; i++) {
                listl.add(new Integer(i));
            }
            
           // Collections.shuffle(listl);
            //System.out.println("Random number generation");
            
            for (int i=0; i<u; i++) 
            {
         	   randomR[i]=listl.get(i);
            //     System.out.println(randomR[i]);
            }
           
            Arrays.sort(randomR);
            //System.out.println("RandomRRR");
            //System.out.println(java.util.Arrays.toString(randomR));
            
            
            FileReader fileReader = new FileReader("res/"+tp.getLHS1()+".txt");   
            BufferedReader bufferedReader =new BufferedReader(fileReader);
            bufferedReader = new BufferedReader(fileReader);
            lineNumber = 0;
            
            int counter=0;
            int u1=1;
            
            //System.out.println("random r length  " + randomR.length );
            
            while((line = bufferedReader.readLine()) != null) 
            {
            	//System.out.println("Read");
            		if(lineNumber == 0)
            		{
            			roneTypes = line;
            		}
            		else
            		{
            			
            			if(u1 == randomR[counter] )
            	    	{
            				if(roneTypes.split(",")[x-1].equals("attrInteger"))
                				l1.add(Integer.parseInt(line.split(",")[x-1]));
                			else
                				System.out.println("Error: Integer Column not Provided");
                			
                			if(roneTypes.split(",")[y-1].equals("attrInteger"))
                				l2.add(Integer.parseInt(line.split(",")[y-1]));
                			else
                				System.out.println("Error: Integer Column not Provided");
                			
                			++counter;
                			
                				
                			//System.out.println("counter  " + counter);
                			if(counter==randomR.length)
                			{
                    			//System.out.println("counter break " );
                				break ;
                			}
                			
            	    	}
            			
            			++u1;
            		
            		}
            		
            		lineNumber++;			
            }
            
            
            
           // System.out.println("u1  " + u1);
            
           //System.out.println("array list no. of rows " + l1.size() );
            
           // System.out.println("no. of rows in r " + lineNumber);
            
  
       /// **************************************************************************************************************************************** 
        
       //System.out.println("no. of rows in s " + lineNumber);
            
            ///////////////////// l1pos
            //System.out.println("Computing l1pos");
            temp1pos = 0;temp2pos =0;countlast=1;
            order.clear();
            maps.clear();
            temp1.clear();
            inter1.clear();
            answer.clear();
            temp2.clear();
            inter.clear();
            temp.clear();
            temp.clear();
            rev = 0;
            temp.addAll(l1);
            //System.out.println(temp);
            Collections.sort(l1);

            if(op1==3 || op1==4){
            	// > or <=
            	Collections.reverse(l1);
            	rev = 1;
            }
            if(rev == 1){
            	// to make it in ascending order for the binary search operation
            	Collections.reverse(l1);
            }
            for(int i=0;i<temp.size();i++){
            	inter.add(Collections.binarySearch(l1, temp.get(i))+1);
            	
            }
            if(rev == 1){
            	Collections.reverse(l1);
            }
            if(rev == 1){
            	b=0;
            	if(inter.size()%2==0){
            		// even
            		b = inter.size()/2;
            		for(int i =0;i<inter.size();i++){
            			if(inter.get(i)>b)
            				inter.set(i, b-inter.get(i)+b+1);
            			else
            				inter.set(i, b+b-inter.get(i)+1);
            		}
            	}
            	else{
            		// odd

            		b = (inter.size()/2)+1;
            		for(int i =0;i<inter.size();i++){
            			if(inter.get(i)>b)
            				inter.set(i, b-inter.get(i)+b);
            			else
            				inter.set(i, b+b-inter.get(i));
            		}
            	}
            	
            	rev = 0;
            }
            // inter is in correct order
            for(int i=0;i<inter.size();i++){
            	l1pos.add(0);
            }
            
            for(int i=0;i<inter.size();i++){
            	t = inter.get(i)-1;
            	if(l1pos.get(t) == 0)
            	l1pos.set(t, i+1);
            	else
            		l1pos.set(t, -1);
            }
            inter.clear();
            temp1.clear();// stores zeros
            temp2.clear();// stores -1's
            orderpos = 0;
            for(int i=0;i<l1pos.size();i++){
            	if(l1pos.get(i)==0){
            		temp1.add(i);
            		orderpos ++;
            	}
            	if(l1pos.get(i)==-1){
            		inter1.add(l1.get(i));
            		temp2.add(i);
            		order.add(orderpos);
            		orderpos ++;
            	}
            }
            orderpos=0;
            
            // to make more fast
            
            
            empty.clear();
            for(int i=0;i<inter1.size();i++){
				
				maps.add(new ArrayList<Integer>());
			}
            for(int i=0;i<inter1.size();i++){
            	last=0;
            	//System.out.println(i);
            	b = temp.subList(last, temp.size()).indexOf(inter1.get(i)); 
            	while(b != -1){
            		//System.out.println("co"+temp.subList(last, temp.size()));
            		last = last + b;
            		maps.get(i).add(last);
            		last ++;
            		b = temp.subList(last, temp.size()).indexOf(inter1.get(i));
            	}
            }
            
            while(temp2pos<temp2.size()){
            	countlast = 1;
            	last=1;
            	b = temp2.get(temp2pos);
            	
            	l1pos.set(b, 0);
            	temp1.add(order.get(orderpos), b);
            	orderpos++;
            	lineNumber = 0;
            	
            	t = l1.get(b);
            	ano = temp.subList(lineNumber, temp.size()).indexOf(t);
				//ano = answer.get(answerpos);
				//answerpos++;
				ano = maps.get(temp2pos).get(last-1);
				countlast = ano;
				last++;
				l1pos.set(temp1.get(temp1pos),lineNumber + ano + 1);
        		temp1pos++;
            	while(last <= maps.get(temp2pos).size()){

            		lineNumber =  lineNumber+ ano + 1;
            		ano = maps.get(temp2pos).get(last-1) - countlast - 1;
            		countlast = maps.get(temp2pos).get(last-1);
            		last++;
    				l1pos.set(temp1.get(temp1pos),lineNumber + ano + 1);
            		temp1pos++;

            	}
            	temp2pos++;
            	}
          // System.out.println("l1pos ready");
            ///////////////////////// l2pos 
          // System.out.println("Computing l2pos");
            
            temp1pos = 0;temp2pos =0;countlast=1;
            order.clear();
            maps.clear();
            temp1.clear();
            inter1.clear();
            answer.clear();
            temp2.clear();
            inter.clear();
            temp.clear();
            temp.clear();
            rev = 0;
            temp.addAll(l2);
            Collections.sort(l2);

            if(op2==2 || op2==1){
            	// > or <=
            	Collections.reverse(l2);
            	rev = 1;
            }
            if(rev == 1){
            	// to make it in ascending order for the binary search operation
            	Collections.reverse(l2);
            }
            for(int i=0;i<temp.size();i++){
            	inter.add(Collections.binarySearch(l2, temp.get(i))+1);
            	
            }
            
            if(rev == 1){
            	Collections.reverse(l2);
            }
            if(rev == 1){
            	b=0;
            	if(inter.size()%2==0){
            		// even
            		b = inter.size()/2;
            		for(int i =0;i<inter.size();i++){
            			if(inter.get(i)>b)
            				inter.set(i, b-inter.get(i)+b+1);
            			else
            				inter.set(i, b+b-inter.get(i)+1);
            		}
            	}
            	else{
            		// odd

            		b = (inter.size()/2)+1;
            		for(int i =0;i<inter.size();i++){
            			if(inter.get(i)>b)
            				inter.set(i, b-inter.get(i)+b);
            			else
            				inter.set(i, b+b-inter.get(i));
            		}
            	}
            	
            	//Collections.reverse(inter);
            	rev = 0;
            }
            // inter is in correct order
             for(int i=0;i<inter.size();i++){
            	l2pos.add(0);
            }
            
            for(int i=0;i<inter.size();i++){
            	t = inter.get(i)-1;
            	if(l2pos.get(t) == 0)
            	l2pos.set(t, i+1);
            	else
            		l2pos.set(t, -1);
            }
            inter.clear();
            temp1.clear();// stores zeros
            temp2.clear();// stores -1's
            orderpos = 0;
            for(int i=0;i<l2pos.size();i++){
            	if(l2pos.get(i)==0){
            		temp1.add(i);
            		orderpos ++;
            	}
            	if(l2pos.get(i)==-1){
            		inter1.add(l2.get(i));
            		temp2.add(i);
            		order.add(orderpos);
            		orderpos ++;
            	}
            }
            orderpos=0;
            
            // to make more fast
            
           // System.out.println(inter1);
            empty.clear();
            for(int i=0;i<inter1.size();i++){
				
				maps.add(new ArrayList<Integer>());
			}
            for(int i=0;i<inter1.size();i++){
            	last=0;
            	//System.out.println(i);
            	b = temp.subList(last, temp.size()).indexOf(inter1.get(i)); 
            	while(b != -1){
            		//System.out.println("co"+temp.subList(last, temp.size()));
            		last = last + b;
            		maps.get(i).add(last);
            		last ++;
            		b = temp.subList(last, temp.size()).indexOf(inter1.get(i));
            	}
            }
            
            //System.out.println(maps);
            //System.out.println(countOfdup);
            while(temp2pos<temp2.size()){
            	countlast = 1;
            	last=1;
            	b = temp2.get(temp2pos);
            	
            	l2pos.set(b, 0);
            	temp1.add(order.get(orderpos), b);
            	orderpos++;
            	lineNumber = 0;
            	
            	t = l2.get(b);
            	ano = temp.subList(lineNumber, temp.size()).indexOf(t);
				//ano = answer.get(answerpos);
				//answerpos++;
				ano = maps.get(temp2pos).get(last-1);
				countlast = ano;
				last++;
				l2pos.set(temp1.get(temp1pos),lineNumber + ano + 1);
        		temp1pos++;
            	while(last <= maps.get(temp2pos).size()){

            		lineNumber =  lineNumber+ ano + 1;
            		ano = maps.get(temp2pos).get(last-1) - countlast - 1;
            		countlast = maps.get(temp2pos).get(last-1);
            		last++;
    				l2pos.set(temp1.get(temp1pos),lineNumber + ano + 1);
            		temp1pos++;

            	}
            	temp2pos++;
            	}
            //System.out.println("l2pos ready");
            //////////////////////// p
            //System.out.println("Computing p");
            	temp.clear();
            	inter.clear();
     			temp.clear();
     			temp1.clear();
     			temp.addAll(l1pos);
     			Collections.sort(temp);
     			for(int i=0;i<l1pos.size();i++){
     				inter.add(Collections.binarySearch(temp, l1pos.get(i))+1);
     				}
     			
     			for(int i=0;i<l1pos.size();i++){
     				temp1.add(0);
     			}
     			
     			for(int i=0;i<inter.size();i++){
     				temp1.set(inter.get(i)-1, i+1);
     			}
     			
     			for(int i=0;i<l2pos.size();i++){
     				p.add(temp1.get(l2pos.get(i)-1));
     			}
                //bufferedReader.close();	
              //  System.out.println("p ready");
            ///////////////////////////////////////////////////////////
            // *****************************************************************************************Initialization ( second relation )
                
     			
     			
     		   LineNumberReader  lnrs = new LineNumberReader(new FileReader("res/"+tp.getLHS2()+".txt"));
     	        lnrs.skip(Long.MAX_VALUE);
     	        int number_of_recordsS=lnrs.getLineNumber();
     	        //System.out.println("S table has records : "+(number_of_recordsS)); //Add 1 because line index starts at 0
     	        lnrs.close();
     	        
     	        int v= (int) Math.floor(number_of_recordsS * sampling_rate);
     	        int randomS[]=new int[v];
     	        
     	        //System.out.println("no. of files in second after sampling " + v);
     	        
     	        ArrayList<Integer> listS = new ArrayList<Integer>();
     	        for (int i=1; i<number_of_recordsS+1; i++) {
     	            listS.add(new Integer(i));
     	        }
     	       // Collections.shuffle(listS);
     	        //System.out.println("Random number generation");
     	        for (int i=0; i<v; i++)
     	        {
     	     	   randomS[i]=listS.get(i);
     	     	   	// System.out.println(randomR[i]);
     	        }
     	       
     	        Arrays.sort(randomS);
     	        //System.out.println("RandomRRR");
     	        //System.out.println(java.util.Arrays.toString(randomS));
     	        
     			 fileReader = new FileReader("res/"+tp.getLHS2()+".txt");
     	        bufferedReader = new BufferedReader(fileReader);
     	        int lineNumber2 = 0;
     	        
     	        int counterS=0;
     	        int v1=1;
     	        //System.out.println("random s length  " + randomS.length );
     	        
     	        
     	        while((line = bufferedReader.readLine()) != null) 
     	        {
     	        	//System.out.println("line " + line);
     	        	
     	        		if(lineNumber2 == 0)
     	        		{
     	        			rtwoTypes = line;
     	        		}
     	        		else
     	        		
     	        		{
     	        			

     	        			if(v1 == randomS[counterS] )
     	        	    	{
     	        				//System.out.println(roneTypes+" "+ydash);
     	        				if(rtwoTypes.split(",")[xdash-1].equals("attrInteger"))
     	            				l1dash.add(Integer.parseInt(line.split(",")[xdash-1]));
     	            			else
     	            				System.out.println("Error: Integer Column not Provided");
     	            			if(rtwoTypes.split(",")[ydash-1].equals("attrInteger"))
     	            				l2dash.add(Integer.parseInt(line.split(",")[ydash-1]));
     	            			else
     	            				System.out.println("Error: Integer Column not Provided");
     	            			
     	            			++counterS;
     	            				
     	            			//System.out.println("counter  " + counter);
     	            			if(counterS==randomS.length)
     	            			{
     	                			//System.out.println("counter break " );
     	            				break ;
     	            			}
     	            			
     	        	    	}
     	        			
     	        			++v1;
     	        		
     	        			
     	        		}
     	        		
     	        		lineNumber2++;			
     	        }
     	        
     	        
     	            
     	       // System.out.println("array list no. of rows in 2nd relations " + l1dash.size() );
     			
            
            //////////////////// l1dashpos
           // System.out.println("Computing l1dashpos");
            temp1pos = 0;temp2pos =0;countlast=1;
            order.clear();
            maps.clear();
            temp1.clear();
            inter1.clear();
            answer.clear();
            temp2.clear();
            inter.clear();
            temp.clear();
            temp.clear();
            rev = 0;
            temp.addAll(l1dash);
            //System.out.println(temp);
            Collections.sort(l1dash);

            if(op1==3 || op1==4){
            	// > or <=
            	Collections.reverse(l1dash);
            	rev = 1;
            }
            if(rev == 1){
            	// to make it in ascending order for the binary search operation
            	Collections.reverse(l1dash);
            }
            for(int i=0;i<temp.size();i++){
            	inter.add(Collections.binarySearch(l1dash, temp.get(i))+1);
            	
            }
            if(rev == 1){
            	Collections.reverse(l1dash);
            }
            if(rev == 1){
            	b=0;
            	if(inter.size()%2==0){
            		// even
            		b = inter.size()/2;
            		for(int i =0;i<inter.size();i++){
            			if(inter.get(i)>b)
            				inter.set(i, b-inter.get(i)+b+1);
            			else
            				inter.set(i, b+b-inter.get(i)+1);
            		}
            	}
            	else{
            		// odd

            		b = (inter.size()/2)+1;
            		for(int i =0;i<inter.size();i++){
            			if(inter.get(i)>b)
            				inter.set(i, b-inter.get(i)+b);
            			else
            				inter.set(i, b+b-inter.get(i));
            		}
            	}
            	
            	rev = 0;
            }
            // inter is in correct order
            for(int i=0;i<inter.size();i++){
            	l1dashpos.add(0);
            }
            
            for(int i=0;i<inter.size();i++){
            	t = inter.get(i)-1;
            	if(l1dashpos.get(t) == 0)
            	l1dashpos.set(t, i+1);
            	else
            		l1dashpos.set(t, -1);
            }
            inter.clear();
            temp1.clear();// stores zeros
            temp2.clear();// stores -1's
            orderpos = 0;
            for(int i=0;i<l1dashpos.size();i++){
            	if(l1dashpos.get(i)==0){
            		temp1.add(i);
            		orderpos ++;
            	}
            	if(l1dashpos.get(i)==-1){
            		inter1.add(l1dash.get(i));
            		temp2.add(i);
            		order.add(orderpos);
            		orderpos ++;
            	}
            }
            orderpos=0;
            
            // to make more fast
            
            empty.clear();
            for(int i=0;i<inter1.size();i++){
				
				maps.add(new ArrayList<Integer>());
			}
            for(int i=0;i<inter1.size();i++){
            	last=0;
            	//System.out.println(i);
            	b = temp.subList(last, temp.size()).indexOf(inter1.get(i)); 
            	while(b != -1){
            		//System.out.println("co"+temp.subList(last, temp.size()));
            		last = last + b;
            		maps.get(i).add(last);
            		last ++;
            		b = temp.subList(last, temp.size()).indexOf(inter1.get(i));
            	}
            }
            
            //System.out.println(maps);
            //System.out.println(countOfdup);
            while(temp2pos<temp2.size()){
            	countlast = 1;
            	last=1;
            	b = temp2.get(temp2pos);
            	
            	l1dashpos.set(b, 0);
            	temp1.add(order.get(orderpos), b);
            	orderpos++;
            	lineNumber = 0;
            	
            	t = l1dash.get(b);
            	ano = temp.subList(lineNumber, temp.size()).indexOf(t);
				//ano = answer.get(answerpos);
				//answerpos++;
				ano = maps.get(temp2pos).get(last-1);
				countlast = ano;
				last++;
				l1dashpos.set(temp1.get(temp1pos),lineNumber + ano + 1);
        		temp1pos++;
            	while(last <= maps.get(temp2pos).size()){

            		lineNumber =  lineNumber+ ano + 1;
            		ano = maps.get(temp2pos).get(last-1) - countlast - 1;
            		countlast = maps.get(temp2pos).get(last-1);
            		last++;
    				l1dashpos.set(temp1.get(temp1pos),lineNumber + ano + 1);
            		temp1pos++;

            	}
            	temp2pos++;
            	}
          //  System.out.println("l1dashpos ready");
            //////////////////////////////// l2dashpos
           // System.out.println("Computing l2dashpos");
            temp1pos = 0;temp2pos =0;countlast=1;
            order.clear();
            maps.clear();
            temp1.clear();
            inter1.clear();
            answer.clear();
            temp2.clear();
            inter.clear();
            temp.clear();
            temp.clear();
            rev = 0;
            temp.addAll(l2dash);
            //System.out.println(temp);
            Collections.sort(l2dash);

            if(op2==2 || op2==1){
            	// > or <=
            	Collections.reverse(l2dash);
            	rev = 1;
            }
            if(rev == 1){
            	// to make it in ascending order for the binary search operation
            	Collections.reverse(l2dash);
            }
            for(int i=0;i<temp.size();i++){
            	inter.add(Collections.binarySearch(l2dash, temp.get(i))+1);
            	
            }
            if(rev == 1){
            	Collections.reverse(l2dash);
            }
            if(rev == 1){
            	b=0;
            	if(inter.size()%2==0){
            		// even
            		b = inter.size()/2;
            		for(int i =0;i<inter.size();i++){
            			if(inter.get(i)>b)
            				inter.set(i, b-inter.get(i)+b+1);
            			else
            				inter.set(i, b+b-inter.get(i)+1);
            		}
            	}
            	else{
            		// odd

            		b = (inter.size()/2)+1;
            		for(int i =0;i<inter.size();i++){
            			if(inter.get(i)>b)
            				inter.set(i, b-inter.get(i)+b);
            			else
            				inter.set(i, b+b-inter.get(i));
            		}
            	}
            	
            	rev = 0;
            }
            // inter is in correct order
            for(int i=0;i<inter.size();i++){
            	l2dashpos.add(0);
            }
            
            for(int i=0;i<inter.size();i++){
            	t = inter.get(i)-1;
            	if(l2dashpos.get(t) == 0)
            	l2dashpos.set(t, i+1);
            	else
            		l2dashpos.set(t, -1);
            }
            inter.clear();
            temp1.clear();// stores zeros
            temp2.clear();// stores -1's
            orderpos = 0;
            for(int i=0;i<l2dashpos.size();i++){
            	if(l2dashpos.get(i)==0){
            		temp1.add(i);
            		orderpos ++;
            	}
            	if(l2dashpos.get(i)==-1){
            		inter1.add(l2dash.get(i));
            		temp2.add(i);
            		order.add(orderpos);
            		orderpos ++;
            	}
            }
            orderpos=0;
            
            // to make more fast
            
            empty.clear();
            for(int i=0;i<inter1.size();i++){
				
				maps.add(new ArrayList<Integer>());
			}
            for(int i=0;i<inter1.size();i++){
            	last=0;
            	//System.out.println(i);
            	b = temp.subList(last, temp.size()).indexOf(inter1.get(i)); 
            	while(b != -1){
            		//System.out.println("co"+temp.subList(last, temp.size()));
            		last = last + b;
            		maps.get(i).add(last);
            		last ++;
            		b = temp.subList(last, temp.size()).indexOf(inter1.get(i));
            	}
            }
            
            //System.out.println(maps);
            //System.out.println(countOfdup);
            while(temp2pos<temp2.size()){
            	countlast = 1;
            	last=1;
            	b = temp2.get(temp2pos);
            	
            	l2dashpos.set(b, 0);
            	temp1.add(order.get(orderpos), b);
            	orderpos++;
            	lineNumber = 0;
            	
            	t = l2dash.get(b);
            	ano = temp.subList(lineNumber, temp.size()).indexOf(t);
				//ano = answer.get(answerpos);
				//answerpos++;
				ano = maps.get(temp2pos).get(last-1);
				countlast = ano;
				last++;
				l2dashpos.set(temp1.get(temp1pos),lineNumber + ano + 1);
        		temp1pos++;
            	while(last <= maps.get(temp2pos).size()){

            		lineNumber =  lineNumber+ ano + 1;
            		ano = maps.get(temp2pos).get(last-1) - countlast - 1;
            		countlast = maps.get(temp2pos).get(last-1);
            		last++;
    				l2dashpos.set(temp1.get(temp1pos),lineNumber + ano + 1);
            		temp1pos++;

            	}
            	temp2pos++;
            	}
            //System.out.println("l2dashpos ready");
            ///////////////////////////////// pdash
            //System.out.println("Computing pdash");
            
            temp.clear();
         
  			inter.clear();
  			temp.clear();
  			temp1.clear();
  			temp.addAll(l1dashpos);
  			Collections.sort(temp);
  			for(int i=0;i<l1dashpos.size();i++){
  				inter.add(Collections.binarySearch(temp, l1dashpos.get(i))+1);
  				}
  			for(int i=0;i<l1dashpos.size();i++){
  				temp1.add(0);
  			}
  			
  			for(int i=0;i<inter.size();i++){
  				temp1.set(inter.get(i)-1, i+1);
  			}
  			
  			
  			for(int i=0;i<l2dashpos.size();i++){
  				pdash.add(temp1.get(l2dashpos.get(i)-1));
  			}
  			//bufferedReader.close();	
  			//System.out.println("pdash ready");
  			///////////////////////////// intialization over above
  			
  			
  			 // System.out.println("Initialization .......");
  			/*//n.nextInt();
            System.out.println("l1:\t\t"+l1);
//            System.out.println("l1pos:\t "+l1pos);
            System.out.println("l2:\t\t"+l2);
//            System.out.println("l2pos:\t "+l2pos);
            System.out.println("p:\t\t"+p);            
            System.out.println("l1dash: \t"+l1dash);
            System.out.println("l2dash: \t"+l2dash);
//            System.out.println("l2dashpos: \t"+l2dashpos);
            System.out.println("pdash: \t\t"+pdash);*/
           // System.out.println("--------------------");
 
            /////////////////////////////////////////////////////////////////////////////////////////////////
            
            								//Works Fine above here
            
            /////////////////////////////////////////////////////////////////////////////////////////////////
            
         //System.out.println("Computing Offsets");   
             // Computing offset o1 
         if(op1==2 || op1==3){
        	 
        	 if(op1==2){
        		 // l1dash is in ascending order
        		 for(int i=0;i<l1.size();i++){
                 	lineNumber = 0;
                 	while(lineNumber<l1dash.size() &&l1dash.get(lineNumber)<l1.get(i))
                 		lineNumber++;
                 	o1.add(lineNumber+1);
                 }	 
        		 
        	 }
        	 if(op1==3){
        		 //l1dash is in descending order

        		 for(int i=0;i<l1.size();i++){
                 	lineNumber = 0;
                 	while(lineNumber<l1dash.size() &&l1dash.get(lineNumber)>l1.get(i))
                 		lineNumber++;
                 	o1.add(lineNumber+1);
                 }
        	 }
        	 
         }
         if(op1==1 || op1==4){
        	 
        	 if(op1==1){
            		 // l1dash is in ascending order
            		 for(int i=0;i<l1.size();i++){
                     	lineNumber = 0;
                     	while(lineNumber<l1dash.size() &&l1dash.get(lineNumber)<=l1.get(i))
                     		lineNumber++;
                     	o1.add(lineNumber+1);
                     }	 
        	 }
        	 if(op1==4){
        		 //l1dash is in descending order
        		 for(int i=0;i<l1.size();i++){
                  	lineNumber = 0;
                  	while(lineNumber<l1dash.size() &&l1dash.get(lineNumber)>=l1.get(i))
                  		lineNumber++;
                  	o1.add(lineNumber+1);
                  }
        	 
        	 }
        	 
         }
         /*System.out.println(l1);
         System.out.println(l2);
         System.out.println(p);
         System.out.println(pdash);
         System.out.println(o1);
         */   // Computing offset o2 
        if(op2==1 || op2==4){
       	 
       	 if(op2==1){
       		 // l2dash is in descending order
       		 for(int i=0;i<l2.size();i++){
                	lineNumber = 0;
                	while(lineNumber<l2dash.size() &&l2dash.get(lineNumber)>l2.get(i))
                		lineNumber++;
                	o2.add(lineNumber+1);
                }	 
       		 
       	 }
       	 if(op2==4){
       		 // l2dash is in ascending order
       		 for(int i=0;i<l2.size();i++){
                	lineNumber = 0;
                	while(lineNumber<l2dash.size() &&l2dash.get(lineNumber)<l2.get(i))
                		lineNumber++;
                	o2.add(lineNumber+1);
                }	 
       		 
       	 }       	 
        }
        if(op2==2 || op2==3){
       	 
          	 if(op2==2){
           		 // l2dash is in descending order
           		 for(int i=0;i<l2.size();i++){
                    	lineNumber = 0;
                    	while(lineNumber<l2dash.size() &&l2dash.get(lineNumber)>=l2.get(i))
                    		lineNumber++;
                    	o2.add(lineNumber+1);
                    }	 
           		 
           	 }
           	 if(op2==3){
           		 // l2dash is in ascending order
           		 for(int i=0;i<l2.size();i++){
                    	lineNumber = 0;
                    	while(lineNumber<l2dash.size() &&l2dash.get(lineNumber)<=l2.get(i))
                    		lineNumber++;
                    	o2.add(lineNumber+1);
                    }	 
           		 
           	 }       	        	 
        }
    
            /*for(int i=0;i<l1dash.size();i++){
            	bdash.add(0);
            }
           */
        for(int i=0;i<l1dash.size();i++){
        	bdash.add(-1);
        }
       
            
           
           
            
           /* System.out.println("o1: \t\t"+o1);
            System.out.println("o2: \t\t"+o2);
            System.out.println("bdash: \t\t"+bdash);*/
           // System.out.println("------------Initialization Over !!-------------");
            //n.nextInt();
            // Initialization over
            start = bdash.size()+1;
            end = -1;
            current = 0;
            left = 0;
            //System.out.println(start+" "+end);
            for(int i=0;i<l2.size();i++){
            	off2 = o2.get(i);
            	if(off2>=2){
            		for(int j=0;j<=off2-2;j++){
            			current = pdash.get(j);
            			//System.out.println(j+"current"+current);
            			if(bdash.get(current-1)==-1)
            			{
            				//System.out.println("Processing....");
            			if(current<start){
            				bdash.set(current-1, start);
            				start = current;
            			}
            			if(current>end){
            				if(end>-1){
            					bdash.set(end-1, current);
            					end = current;
            					bdash.set(end-1, bdash.size()+1);
            				}
            				else{
            					end = current;
            				}
            			}
            			else if(start<current && current<end){
            				//System.out.println("came");
            				for(int z=current-1;z>=start;z--){
            					if(bdash.get(z-1)!=-1){
            						left = z;
            						break;
            					}
            					
            				}
            				bdash.set(current-1, bdash.get(left-1));
            				bdash.set(left-1, current);
            			}
            		}
            			//System.out.println("bdash"+bdash+"start"+start+"end"+end);
            		}
            	}
            	
            	off1 = o1.get(p.get(i)-1);
            	if(off1<start)
            		b = start;
            	else
            		b = off1;
            	//System.out.println("value of b"+b);
            	if(b<end){
            		if(bdash.get(b-1)==-1){
            		for(int z=b+1;z<=end;z++){
            			if(bdash.get(z-1)!= -1)
            			{
            				b=z;
            				break;
            			}
            		}
            		}
            	}
            	//System.out.println("Got offset"+off1+"b"+b);            	
            	while(b<=end){
            		//System.out.println("Ans:\t\t"+l2pos.get(i)+" "+l1dashpos.get(b-1));
            		tuplesCount++;
            		b = bdash.get(b-1);
            	}

            }
           // System.out.println("Tuples count "+tuplesCount);
            
            
        }
        catch(FileNotFoundException ex) {
                          
        }
        catch(IOException ex) {
                             
            // Or we could just do this: 
             ex.printStackTrace();
        }
        
		return tuplesCount;
	}
	
	public static void main(String[] args) throws IOException 
	{
	
	    boolean sortstatus;
	    
	    
	    // Creating TwoPredicate Object from Queryfile
	    Map <Integer, TwoPredicate> hashMap = new HashMap<Integer, TwoPredicate>();
	    Map<Pair<String, String>, Integer> findColumn = new HashMap<Pair<String, String>, Integer>();
	    
	    findColumn.put(new Pair<String, String>("F1r","id"), 1);
	    findColumn.put(new Pair<String, String>("F1r","dept"), 2);
	    findColumn.put(new Pair<String, String>("F1r","salary"), 3);
	    findColumn.put(new Pair<String, String>("F1r","tax"), 4);
	    
	    findColumn.put(new Pair<String, String>("F2r","id"), 1);
	    findColumn.put(new Pair<String, String>("F2r","dept"), 2);
	    findColumn.put(new Pair<String, String>("F2r","salary"), 3);
	    findColumn.put(new Pair<String, String>("F2r","tax"), 4);
	    findColumn.put(new Pair<String, String>("F2r","id2"), 5);
	    findColumn.put(new Pair<String, String>("F2r","start"), 6);
	    findColumn.put(new Pair<String, String>("F2r","end"), 7);
	    
	    findColumn.put(new Pair<String, String>("F3r","id2"), 1);
	    findColumn.put(new Pair<String, String>("F3r","start"), 2);
	    findColumn.put(new Pair<String, String>("F3r","end"), 3);
	    findColumn.put(new Pair<String, String>("F3r","id"), 4);
	    findColumn.put(new Pair<String, String>("F3r","dept"), 5);
	    findColumn.put(new Pair<String, String>("F3r","salary"), 6);
	    findColumn.put(new Pair<String, String>("F3r","tax"), 7);
	    
	    findColumn.put(new Pair<String, String>("F4r","id"), 1);
	    findColumn.put(new Pair<String, String>("F4r","dept"), 2);
	    findColumn.put(new Pair<String, String>("F4r","salary"), 3);
	    findColumn.put(new Pair<String, String>("F4r","tax"), 4);
	    findColumn.put(new Pair<String, String>("F4r","id2"), 5);
	    findColumn.put(new Pair<String, String>("F4r","start"), 6);
	    findColumn.put(new Pair<String, String>("F4r","end"), 7);
	    
	    findColumn.put(new Pair<String, String>("F5r","id2"), 1);
	    findColumn.put(new Pair<String, String>("F5r","start"), 2);
	    findColumn.put(new Pair<String, String>("F5r","end"), 3);

	    FileInputStream fstream = null;
	  try {
	    fstream = new FileInputStream("res/"+"QueryFile.txt");
	  } catch (FileNotFoundException e1) {
	    // TODO Auto-generated catch block
	    e1.printStackTrace();
	  }
	    BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

	    Integer lineno = 0, dualPredicateNumber = 0;
	    String strLine;
	    try {
	      
	    while ((strLine = br.readLine()) != null)   {
	      lineno++;
	      if(lineno > 2) {
	        dualPredicateNumber++;
	        String[] parts = strLine.split(" |\\.|\\;");
	        System.out.println("Print delimited String: " + Arrays.toString(parts));
	        System.out.println("Print each parts: " + parts[0] + " " + parts[1] + " " + parts[2] + " " + parts[3] + " " + parts[4] 
	            + " " + parts[5] + " " + parts[6] + " " + parts[7] + " " + parts[8] + " " + parts[9] 
	            + " " + parts[10] + " " + parts[11] );
	        
	        TwoPredicate twoPredicateObj = new TwoPredicate();
	        
	        twoPredicateObj.setLHS1(table(parts[1]));    
	        twoPredicateObj.setLHS1Column(findColumn.get(new Pair<String, String>(table(parts[1]), parts[2])));
	        twoPredicateObj.setLHSOperator(oper(parts[3]));
	        twoPredicateObj.setLHS2(table(parts[4]));
	        twoPredicateObj.setLHS2Column(findColumn.get(new Pair<String, String>(table(parts[4]), parts[5])));
	        
	       // twoPredicateObj.setANDorOR(parts[6]);
	        twoPredicateObj.setRHS1(table(parts[7]));
	        twoPredicateObj.setRHS1Column(findColumn.get(new Pair<String, String>(table(parts[7]), parts[8])));
	        twoPredicateObj.setRHSOperator(oper(parts[9]));
	        twoPredicateObj.setRHS2(table(parts[10]));
	        //System.out.println(parts[10] +  " " + parts[11]);
	        twoPredicateObj.setRHS2Column(findColumn.get(new Pair<String, String>(table(parts[10]), parts[11])));
	        //System.out.println("Testing : " + twoPredicateObj.getRHS2Column());
	        
	        //twoPredicateObj.setProjection(findColumn.get(new Pair<String, String>(table(parts[1]), parts[2])));
	        
	        hashMap.put(dualPredicateNumber, twoPredicateObj);          
	      }
	    }
	    
	   // System.out.println("line numbers " + lineno);
	  } catch (IOException e1) {
	    // TODO Auto-generated catch block
	    e1.printStackTrace();
	  }
	    //Close the input stream
	    try {
	    br.close();
	  } catch (IOException e1) {
	    // TODO Auto-generated catch block
	    e1.printStackTrace();
	  }
	    
	    ArrayList<TwoPredicate> input = new ArrayList<TwoPredicate>();
	
	    System.out.println("Map contents");
	    for (Map.Entry entry : hashMap.entrySet()) 
	    {
	      TwoPredicate TwoPredicatevalue = (TwoPredicate) entry.getValue();
	      input.add(TwoPredicatevalue);
	      System.out.println(entry.getKey() + ", " + TwoPredicatevalue.getLHS1() + " " + TwoPredicatevalue.getLHS1Column() + 
	       " " + TwoPredicatevalue.getLHSOperator() + " " +  TwoPredicatevalue.getLHS2() + " " + TwoPredicatevalue.getLHS2Column() +
	       " " + TwoPredicatevalue.getRHS1() + " " + TwoPredicatevalue.getRHS1Column() +
	       " " + TwoPredicatevalue.getRHSOperator() + " " + TwoPredicatevalue.getRHS2() + " " + TwoPredicatevalue.getRHS2Column() );
	    }

	      
	    
	    
	   
	    int count=0;
		Float time;
		Long start = System.currentTimeMillis();
		
		//System.out.println("no. of predicates  "+dualPredicateNumber);
		
		int carr[]= new int[dualPredicateNumber];
		int nruns=1; // number of runs 
		System.out.println("No. of runs : " + nruns);
		int mad=0;
		int avg=0;
	
		// ***************************************************************
		
		/*
		
		Map<Integer,TwoPredicate> tmp = new TreeMap<Integer,TwoPredicate>();
		
		Long start2 = System.currentTimeMillis();
		
		for (Map.Entry<Integer,TwoPredicate> entry1 : hashMap.entrySet())
		{
			TwoPredicate madhu1 = (TwoPredicate) entry1.getValue();
			
			for (int i=0;i<nruns;i++)
			{
				count=IEjoin(madhu1);
				carr[mad]=carr[mad]+count;
				//System.out.println("i = "+ i + " tuples " + count);
						
			}
			
			
			avg = (carr[mad]/nruns);
			if(tmp.containsKey(avg)){
				ArrayList<Integer> keys = new ArrayList<Integer>();
				for(int key = 0 ;key<tmp.keySet().toArray().length;key++){
					keys.add(Integer.parseInt(tmp.keySet().toArray()[key].toString()));
				}	
				Collections.sort(keys);
				for(int key = keys.size()-1;key>=0;key--){
					if(keys.get(key)>avg){
						tmp.put(keys.get(key)+2, tmp.get(keys.get(key)));
						tmp.remove(keys.get(key));
					}
				}
				int a = 1;
			tmp.put(avg+1, madhu1);		
			}
			else{
				tmp.put(avg, madhu1);
			}
			//tmp.put(avg,madhu1);			
		 System.out.println("average tuples "+mad+" condition " + avg);
		 mad=mad+1;
		 
		}
		
		float sptime = (float)((float)(System.currentTimeMillis() - start2)/(float)60000);
		System.out.println("After Sampling (Mins): "+sptime/nruns+" Time(Seconds): "+(sptime*60/nruns));
		
		//   final array of objects ** tpo 
		TwoPredicate[] tpo = new TwoPredicate[dualPredicateNumber];
		int id = 0;
		int id2=1;
		for(Map.Entry<Integer,TwoPredicate> entry : tmp.entrySet()) 
		{
			id2++;
			  TwoPredicate value = entry.getValue();
			  tpo[id] = value;
			 // System.out.println(tpo[id].getLHS1()+"     "+tpo[id].getLHS2() +"			" + tpo[id].getRHS1()+"     "+tpo[id].getRHS2());
			  id++;
		}
		
		System.out.println("don " + dualPredicateNumber);
		
		
		System.out.println("before diagonal " );
		
		for(int i=0;i<dualPredicateNumber;i++)
		{
			System.out.println(tpo[i].getLHS1()+"     "+tpo[i].getLHS2() +"			" + tpo[i].getRHS1()+"     "+tpo[i].getRHS2());
		}
		
		ArrayList<String> pt = new ArrayList<String>();
		int index = 0;
		pt.add(tpo[0].getLHS1());
		index++;
		pt.add(tpo[0].getLHS2());
		index++;
		int countOfAnswer = 0;
		ArrayList<String> visited = new ArrayList<String>();
		ArrayList<String> outputOrder = new ArrayList<String>();
		ArrayList<String> outputLeftOrder = new ArrayList<String>();
		ArrayList<Integer> outputRightOrder = new ArrayList<Integer>();
		ArrayList<String> inputLeftOrder = new ArrayList<String>();
		ArrayList<String> inputRightOrder = new ArrayList<String>();
		ArrayList<TwoPredicate> oporder = new ArrayList<TwoPredicate>();
		int changed = 0;
		for(int i =0;i<tpo.length;i++){
			inputLeftOrder.add(tpo[i].getLHS1());
			inputRightOrder.add(tpo[i].getLHS2());
		}
		while(countOfAnswer < tpo.length){
			changed = 0;
			for(int j=0;j<tpo.length;j++){
				if(!outputOrder.contains(inputLeftOrder.get(j)+inputRightOrder.get(j))){
					if(countOfAnswer == 0){
						visited.add(inputRightOrder.get(j));
						visited.add(inputLeftOrder.get(j));
						outputOrder.add(inputLeftOrder.get(j)+inputRightOrder.get(j));
						countOfAnswer++;
						changed = 1;
						oporder.add(tpo[j]);
						break;
						
					}
					else{
						if((visited.contains(inputLeftOrder.get(j)) && !visited.contains(inputRightOrder.get(j)))||(!visited.contains(inputLeftOrder.get(j)) && visited.contains(inputRightOrder.get(j)))){
							countOfAnswer++;
							outputOrder.add(inputLeftOrder.get(j)+inputRightOrder.get(j));
							if(visited.contains(inputLeftOrder.get(j)))
								visited.add(inputRightOrder.get(j));
							else
								visited.add(inputLeftOrder.get(j));
							changed = 1;
							oporder.add(tpo[j]);
							break;
						}
					}
				}
				
			}
			if(changed == 0){
				System.out.println("Solution does not exist");
				break;
			}
		}
		
		
		
		
		System.out.println("after dialognal " );

		
		
		
		System.out.println("op order size " + oporder.size());
		
		if(oporder.size() == 0)
		{
			System.out.println("Cannot be diagonalized");
		}
		
		showQuery(oporder);
				if(diag(oporder) == 1)
				{
					
					Long start1 = System.currentTimeMillis();
					thejoin(oporder);
					float optime = (float)((float)(System.currentTimeMillis() - start2)/(float)60000);
					//System.out.println("Time(Mins): "+optime+" Time(Seconds): "+optime*60);
				
					System.out.println("Time(Seconds) total : "+optime*60);
				}
		
				*/
				
		// Comment till here for input query
		//*******************************************************************************************************************
		
		
		
		
		
		//showQuery(input);
		if(diag(input) == 1){
			showQuery(input);
			Long start1 = System.currentTimeMillis();
			thejoin(input);
			float optime = (float)((float)(System.currentTimeMillis() - start1)/(float)60000);
		//System.out.println("Time(Mins): "+optime+" Time(Seconds): "+optime*60);
		
			System.out.println("Time(Seconds) total : "+optime*60);
		}
		
		
		
		
		
		
		
		
		

		//Arrays.sort(carr);
			
	}
	
	public static String table(String lHS1) 
	{
		String temp="";
		if(lHS1.equals("r"))
			temp = "F1r";
		else if(lHS1.equals("s"))
			temp = "F2r";
		else if(lHS1.equals("t"))
			temp = "F3r";
		else if(lHS1.equals("v"))
			temp = "F4r";
		else if(lHS1.equals("w"))
			temp = "F5r";
	
			
	return temp;
	}
	

	public static Integer oper(String op) 
	{
		int temp=-1;
		
		if(op.equals("<"))
			temp=1;
		else if (op.equals("<="))
			temp=2;
		else if (op.equals(">="))
			temp=3;
		else if (op.equals(">"))
			temp=4;
		
		return temp;
	}
	
	static HashMap<String, ArrayList<Integer>> join(TwoPredicate q, ArrayList<Integer> next, int which, HashMap<String, ArrayList<Integer>> tables) throws IOException{
		// Variable declaration
		//ArrayList<ArrayList<Integer>> tuples = new ArrayList<ArrayList<Integer>>();
		HashMap<String,ArrayList<Integer>> store = new HashMap<String,ArrayList<Integer>>();
		// store the keys of tables in store 
		for(int i=0;i<tables.size();i++){
			store.put(tables.keySet().toArray()[i].toString(), new ArrayList<Integer>());
		}
		/*for(int i=0;i<next.size();i++)
			tuples.add(new ArrayList<Integer>());
		*/ArrayList<Integer> columns = new ArrayList<Integer>();
		
		String line = null;
		String relations [] = null;
		String[] query = new String [5];
		int temp2pos =0,temp1pos=0,countlast=1;

		int lineNumber = 0,x,xdash,y,ydash,op1,op2,off2,off1,last,b,ano,rev=0,orderpos;
		
		int t,start,end,current,left;
		String rF1rTypes = null;
		String rtwoTypes = null;
		ArrayList<ArrayList<Integer>> maps = new ArrayList<ArrayList<Integer>>();
		ArrayList<Integer> temp = new ArrayList<Integer>();
		List<Integer> temp1 = new ArrayList<Integer>();
		ArrayList<Integer> empty = new ArrayList<Integer>();
		List<Integer> temp2 = new ArrayList<Integer>();
		List<Integer> order = new ArrayList<Integer>();
		ArrayList<Integer> l1 = new ArrayList<Integer>();
		ArrayList<Integer> l2 = new ArrayList<Integer>();
		ArrayList<Integer> l1pos = new ArrayList<Integer>();
		ArrayList<Integer> l2pos = new ArrayList<Integer>();
		ArrayList<Integer> p = new ArrayList<Integer>();
		ArrayList<Integer> pdash = new ArrayList<Integer>();
		ArrayList<Integer> l1dash = new ArrayList<Integer>();
		ArrayList<Integer> l2dash = new ArrayList<Integer>();
		ArrayList<Integer> l1dashpos = new ArrayList<Integer>();
		ArrayList<Integer> l2dashpos = new ArrayList<Integer>();
		ArrayList<Integer> o1 = new ArrayList<Integer>();
		ArrayList<Integer> o2 = new ArrayList<Integer>();
		ArrayList<Integer> bdash = new ArrayList<Integer>();
		ArrayList<Integer> inter = new ArrayList<Integer>();
		ArrayList<Integer> answer = new ArrayList<Integer>();
		ArrayList<Integer> inter1 = new ArrayList<Integer>();
		
		
		
        // INITIALIZATION process
        x = q.LHS1Column;
        xdash = q.LHS2Column;
        op1 = q.LHSOperator;
        y = q.RHS1Column;
        ydash = q.RHS2Column;
        op2 = q.RHSOperator;
       
        
        String Filename ="res/"+q.LHS1+".csv";
        if(tables.get(q.LHS1).size()>0){
        	// already visited table
        // l1 should contain the columns it wants
        //l2 should contain the columns it wants
        // Filename,l1,colNumber,rows
        	singlereadFromExcel(Filename,l1,q.LHS1Column,tables.get(q.LHS1));
        	singlereadFromExcel(Filename,l2,q.RHS1Column,tables.get(q.LHS1));
        }
        else{
        	// not visited already
        	readFromExcel(Filename,l1,x);
            readFromExcel(Filename,l2,y);
        }
        //System.out.println(l1);
        //System.out.println(l2);
        //System.out.println(x+""+xdash+""+y+""+ydash+""+op1+""+op2);
        
        
        
        // l1 & l2 ready
///////////////////// l1pos
//System.out.println("Computing l1pos");
temp1pos = 0;temp2pos =0;countlast=1;
order.clear();
maps.clear();
temp1.clear();
inter1.clear();
answer.clear();
temp2.clear();
inter.clear();
temp.clear();
temp.clear();
rev = 0;
temp.addAll(l1);
//System.out.println(temp);
Collections.sort(l1);

if(op1==3 || op1==4){
	// > or <=
	Collections.reverse(l1);
	rev = 1;
}
if(rev == 1){
	// to make it in ascending order for the binary search operation
	Collections.reverse(l1);
}
for(int i=0;i<temp.size();i++){
	inter.add(Collections.binarySearch(l1, temp.get(i))+1);
	
}
if(rev == 1){
	Collections.reverse(l1);
}
if(rev == 1){
	b=0;
	if(inter.size()%2==0){
		// even
		b = inter.size()/2;
		for(int i =0;i<inter.size();i++){
			if(inter.get(i)>b)
				inter.set(i, b-inter.get(i)+b+1);
			else
				inter.set(i, b+b-inter.get(i)+1);
		}
	}
	else{
		// odd

		b = (inter.size()/2)+1;
		for(int i =0;i<inter.size();i++){
			if(inter.get(i)>b)
				inter.set(i, b-inter.get(i)+b);
			else
				inter.set(i, b+b-inter.get(i));
		}
	}
	
	rev = 0;
}
// inter is in correct order
for(int i=0;i<inter.size();i++){
	l1pos.add(0);
}

for(int i=0;i<inter.size();i++){
	t = inter.get(i)-1;
	if(l1pos.get(t) == 0)
	l1pos.set(t, i+1);
	else
		l1pos.set(t, -1);
}
inter.clear();
temp1.clear();// stores zeros
temp2.clear();// stores -1's
orderpos = 0;
for(int i=0;i<l1pos.size();i++){
	if(l1pos.get(i)==0){
		temp1.add(i);
		orderpos ++;
	}
	if(l1pos.get(i)==-1){
		inter1.add(l1.get(i));
		temp2.add(i);
		order.add(orderpos);
		orderpos ++;
	}
}
orderpos=0;

// to make more fast


empty.clear();
for(int i=0;i<inter1.size();i++){
	
	maps.add(new ArrayList<Integer>());
}
for(int i=0;i<inter1.size();i++){
	last=0;
	//System.out.println(i);
	b = temp.subList(last, temp.size()).indexOf(inter1.get(i)); 
	while(b != -1){
		//System.out.println("co"+temp.subList(last, temp.size()));
		last = last + b;
		maps.get(i).add(last);
		last ++;
		b = temp.subList(last, temp.size()).indexOf(inter1.get(i));
	}
}

while(temp2pos<temp2.size()){
	countlast = 1;
	last=1;
	b = temp2.get(temp2pos);
	
	l1pos.set(b, 0);
	temp1.add(order.get(orderpos), b);
	orderpos++;
	lineNumber = 0;
	
	t = l1.get(b);
	ano = temp.subList(lineNumber, temp.size()).indexOf(t);
	//ano = answer.get(answerpos);
	//answerpos++;
	ano = maps.get(temp2pos).get(last-1);
	countlast = ano;
	last++;
	l1pos.set(temp1.get(temp1pos),lineNumber + ano + 1);
	temp1pos++;
	while(last <= maps.get(temp2pos).size()){

		lineNumber =  lineNumber+ ano + 1;
		ano = maps.get(temp2pos).get(last-1) - countlast - 1;
		countlast = maps.get(temp2pos).get(last-1);
		last++;
		l1pos.set(temp1.get(temp1pos),lineNumber + ano + 1);
		temp1pos++;

	}
	temp2pos++;
	}
	//System.out.println("l1pos ready");
	//System.out.println(l1pos);
	
	
	
	///
	 ///////////////////////// l2pos 
    //System.out.println("Computing l2pos");
     
     temp1pos = 0;temp2pos =0;countlast=1;
     order.clear();
     maps.clear();
     temp1.clear();
     inter1.clear();
     answer.clear();
     temp2.clear();
     inter.clear();
     temp.clear();
     temp.clear();
     rev = 0;
     temp.addAll(l2);
     Collections.sort(l2);

     if(op2==2 || op2==1){
     	// > or <=
     	Collections.reverse(l2);
     	rev = 1;
     }
     if(rev == 1){
     	// to make it in ascending order for the binary search operation
     	Collections.reverse(l2);
     }
     for(int i=0;i<temp.size();i++){
     	inter.add(Collections.binarySearch(l2, temp.get(i))+1);
     	
     }
     
     if(rev == 1){
     	Collections.reverse(l2);
     }
     if(rev == 1){
     	b=0;
     	if(inter.size()%2==0){
     		// even
     		b = inter.size()/2;
     		for(int i =0;i<inter.size();i++){
     			if(inter.get(i)>b)
     				inter.set(i, b-inter.get(i)+b+1);
     			else
     				inter.set(i, b+b-inter.get(i)+1);
     		}
     	}
     	else{
     		// odd

     		b = (inter.size()/2)+1;
     		for(int i =0;i<inter.size();i++){
     			if(inter.get(i)>b)
     				inter.set(i, b-inter.get(i)+b);
     			else
     				inter.set(i, b+b-inter.get(i));
     		}
     	}
     	
     	//Collections.reverse(inter);
     	rev = 0;
     }
     // inter is in correct order
      for(int i=0;i<inter.size();i++){
     	l2pos.add(0);
     }
     
     for(int i=0;i<inter.size();i++){
     	t = inter.get(i)-1;
     	if(l2pos.get(t) == 0)
     	l2pos.set(t, i+1);
     	else
     		l2pos.set(t, -1);
     }
     inter.clear();
     temp1.clear();// stores zeros
     temp2.clear();// stores -1's
     orderpos = 0;
     for(int i=0;i<l2pos.size();i++){
     	if(l2pos.get(i)==0){
     		temp1.add(i);
     		orderpos ++;
     	}
     	if(l2pos.get(i)==-1){
     		inter1.add(l2.get(i));
     		temp2.add(i);
     		order.add(orderpos);
     		orderpos ++;
     	}
     }
     orderpos=0;
     
     // to make more fast
     
    // System.out.println(inter1);
     empty.clear();
     for(int i=0;i<inter1.size();i++){
			
			maps.add(new ArrayList<Integer>());
		}
     for(int i=0;i<inter1.size();i++){
     	last=0;
     	//System.out.println(i);
     	b = temp.subList(last, temp.size()).indexOf(inter1.get(i)); 
     	while(b != -1){
     		//System.out.println("co"+temp.subList(last, temp.size()));
     		last = last + b;
     		maps.get(i).add(last);
     		last ++;
     		b = temp.subList(last, temp.size()).indexOf(inter1.get(i));
     	}
     }
     
     //System.out.println(maps);
     //System.out.println(countOfdup);
     while(temp2pos<temp2.size()){
     	countlast = 1;
     	last=1;
     	b = temp2.get(temp2pos);
     	
     	l2pos.set(b, 0);
     	temp1.add(order.get(orderpos), b);
     	orderpos++;
     	lineNumber = 0;
     	
     	t = l2.get(b);
     	ano = temp.subList(lineNumber, temp.size()).indexOf(t);
			//ano = answer.get(answerpos);
			//answerpos++;
			ano = maps.get(temp2pos).get(last-1);
			countlast = ano;
			last++;
			l2pos.set(temp1.get(temp1pos),lineNumber + ano + 1);
 		temp1pos++;
     	while(last <= maps.get(temp2pos).size()){

     		lineNumber =  lineNumber+ ano + 1;
     		ano = maps.get(temp2pos).get(last-1) - countlast - 1;
     		countlast = maps.get(temp2pos).get(last-1);
     		last++;
				l2pos.set(temp1.get(temp1pos),lineNumber + ano + 1);
     		temp1pos++;

     	}
     	temp2pos++;
     	}
     
    // System.out.println("l2pos ready");
     //System.out.println(l1pos);
     //System.out.println(l2pos);
     //System.out.println("Computing p");
 	temp.clear();
 	inter.clear();
		temp.clear();
		temp1.clear();
		temp.addAll(l1pos);
		Collections.sort(temp);
		for(int i=0;i<l1pos.size();i++){
			inter.add(Collections.binarySearch(temp, l1pos.get(i))+1);
			}
		
		for(int i=0;i<l1pos.size();i++){
			temp1.add(0);
		}
		
		for(int i=0;i<inter.size();i++){
			temp1.set(inter.get(i)-1, i+1);
		}
		
		for(int i=0;i<l2pos.size();i++){
			p.add(temp1.get(l2pos.get(i)-1));
		}
    // System.out.println("p ready");
	//////////////////////////////////////////
    //F1r side over above
     ////////////////////////////////////////
     
     /*Filename ="res/"+q.LHS2+".csv";
     readFromExcel(Filename,l1dash,xdash);
     readFromExcel(Filename,l2dash,ydash);
     */
     Filename ="res/"+q.LHS2+".csv";
     if(tables.get(q.LHS2).size()>0){
     	// already visited table
     // l1 should contain the columns it wants
     //l2 should contain the columns it wants
     // Filename,l1,colNumber,rows
     	singlereadFromExcel(Filename,l1dash,q.LHS2Column,tables.get(q.LHS2));
     	singlereadFromExcel(Filename,l2dash,q.RHS2Column,tables.get(q.LHS2));
     }
     else{
     	// not visited already
     	readFromExcel(Filename,l1dash,xdash);
         readFromExcel(Filename,l2dash,ydash);
     }
     
////////////////////l1dashpos
//System.out.println("Computing l1dashpos");
temp1pos = 0;temp2pos =0;countlast=1;
order.clear();
maps.clear();
temp1.clear();
inter1.clear();
answer.clear();
temp2.clear();
inter.clear();
temp.clear();
temp.clear();
rev = 0;
temp.addAll(l1dash);
//System.out.println(temp);
Collections.sort(l1dash);

if(op1==3 || op1==4){
// > or <=
Collections.reverse(l1dash);
rev = 1;
}
if(rev == 1){
// to make it in ascending order for the binary search operation
Collections.reverse(l1dash);
}
for(int i=0;i<temp.size();i++){
inter.add(Collections.binarySearch(l1dash, temp.get(i))+1);

}
if(rev == 1){
Collections.reverse(l1dash);
}
if(rev == 1){
b=0;
if(inter.size()%2==0){
	// even
	b = inter.size()/2;
	for(int i =0;i<inter.size();i++){
		if(inter.get(i)>b)
			inter.set(i, b-inter.get(i)+b+1);
		else
			inter.set(i, b+b-inter.get(i)+1);
	}
}
else{
	// odd

	b = (inter.size()/2)+1;
	for(int i =0;i<inter.size();i++){
		if(inter.get(i)>b)
			inter.set(i, b-inter.get(i)+b);
		else
			inter.set(i, b+b-inter.get(i));
	}
}

rev = 0;
}
// inter is in correct order
for(int i=0;i<inter.size();i++){
l1dashpos.add(0);
}

for(int i=0;i<inter.size();i++){
t = inter.get(i)-1;
if(l1dashpos.get(t) == 0)
l1dashpos.set(t, i+1);
else
	l1dashpos.set(t, -1);
}
inter.clear();
temp1.clear();// stores zeros
temp2.clear();// stores -1's
orderpos = 0;
for(int i=0;i<l1dashpos.size();i++){
if(l1dashpos.get(i)==0){
	temp1.add(i);
	orderpos ++;
}
if(l1dashpos.get(i)==-1){
	inter1.add(l1dash.get(i));
	temp2.add(i);
	order.add(orderpos);
	orderpos ++;
}
}
orderpos=0;

// to make more fast

empty.clear();
for(int i=0;i<inter1.size();i++){

maps.add(new ArrayList<Integer>());
}
for(int i=0;i<inter1.size();i++){
last=0;
//System.out.println(i);
b = temp.subList(last, temp.size()).indexOf(inter1.get(i)); 
while(b != -1){
	//System.out.println("co"+temp.subList(last, temp.size()));
	last = last + b;
	maps.get(i).add(last);
	last ++;
	b = temp.subList(last, temp.size()).indexOf(inter1.get(i));
}
}

//System.out.println(maps);
//System.out.println(countOfdup);
while(temp2pos<temp2.size()){
countlast = 1;
last=1;
b = temp2.get(temp2pos);

l1dashpos.set(b, 0);
temp1.add(order.get(orderpos), b);
orderpos++;
lineNumber = 0;

t = l1dash.get(b);
ano = temp.subList(lineNumber, temp.size()).indexOf(t);
//ano = answer.get(answerpos);
//answerpos++;
ano = maps.get(temp2pos).get(last-1);
countlast = ano;
last++;
l1dashpos.set(temp1.get(temp1pos),lineNumber + ano + 1);
temp1pos++;
while(last <= maps.get(temp2pos).size()){

	lineNumber =  lineNumber+ ano + 1;
	ano = maps.get(temp2pos).get(last-1) - countlast - 1;
	countlast = maps.get(temp2pos).get(last-1);
	last++;
	l1dashpos.set(temp1.get(temp1pos),lineNumber + ano + 1);
	temp1pos++;

}
temp2pos++;
}
//System.out.println("l1dashpos ready");
//////////////////////////////// l2dashpos

//System.out.println("Computing l2dashpos");
temp1pos = 0;temp2pos =0;countlast=1;
order.clear();
maps.clear();
temp1.clear();
inter1.clear();
answer.clear();
temp2.clear();
inter.clear();
temp.clear();
temp.clear();
rev = 0;
temp.addAll(l2dash);
//System.out.println(temp);
Collections.sort(l2dash);

if(op2==2 || op2==1){
// > or <=
Collections.reverse(l2dash);
rev = 1;
}
if(rev == 1){
// to make it in ascending order for the binary search operation
Collections.reverse(l2dash);
}
for(int i=0;i<temp.size();i++){
inter.add(Collections.binarySearch(l2dash, temp.get(i))+1);

}
if(rev == 1){
Collections.reverse(l2dash);
}
if(rev == 1){
b=0;
if(inter.size()%2==0){
	// even
	b = inter.size()/2;
	for(int i =0;i<inter.size();i++){
		if(inter.get(i)>b)
			inter.set(i, b-inter.get(i)+b+1);
		else
			inter.set(i, b+b-inter.get(i)+1);
	}
}
else{
	// odd

	b = (inter.size()/2)+1;
	for(int i =0;i<inter.size();i++){
		if(inter.get(i)>b)
			inter.set(i, b-inter.get(i)+b);
		else
			inter.set(i, b+b-inter.get(i));
	}
}

rev = 0;
}
// inter is in correct order
for(int i=0;i<inter.size();i++){
l2dashpos.add(0);
}

for(int i=0;i<inter.size();i++){
t = inter.get(i)-1;
if(l2dashpos.get(t) == 0)
l2dashpos.set(t, i+1);
else
	l2dashpos.set(t, -1);
}
inter.clear();
temp1.clear();// stores zeros
temp2.clear();// stores -1's
orderpos = 0;
for(int i=0;i<l2dashpos.size();i++){
if(l2dashpos.get(i)==0){
	temp1.add(i);
	orderpos ++;
}
if(l2dashpos.get(i)==-1){
	inter1.add(l2dash.get(i));
	temp2.add(i);
	order.add(orderpos);
	orderpos ++;
}
}
orderpos=0;

// to make more fast

empty.clear();
for(int i=0;i<inter1.size();i++){

maps.add(new ArrayList<Integer>());
}
for(int i=0;i<inter1.size();i++){
last=0;
//System.out.println(i);
b = temp.subList(last, temp.size()).indexOf(inter1.get(i)); 
while(b != -1){
	//System.out.println("co"+temp.subList(last, temp.size()));
	last = last + b;
	maps.get(i).add(last);
	last ++;
	b = temp.subList(last, temp.size()).indexOf(inter1.get(i));
}
}

//System.out.println(maps);
//System.out.println(countOfdup);
while(temp2pos<temp2.size()){
countlast = 1;
last=1;
b = temp2.get(temp2pos);

l2dashpos.set(b, 0);
temp1.add(order.get(orderpos), b);
orderpos++;
lineNumber = 0;

t = l2dash.get(b);
ano = temp.subList(lineNumber, temp.size()).indexOf(t);
//ano = answer.get(answerpos);
//answerpos++;
ano = maps.get(temp2pos).get(last-1);
countlast = ano;
last++;
l2dashpos.set(temp1.get(temp1pos),lineNumber + ano + 1);
temp1pos++;
while(last <= maps.get(temp2pos).size()){

	lineNumber =  lineNumber+ ano + 1;
	ano = maps.get(temp2pos).get(last-1) - countlast - 1;
	countlast = maps.get(temp2pos).get(last-1);
	last++;
	l2dashpos.set(temp1.get(temp1pos),lineNumber + ano + 1);
	temp1pos++;

}
temp2pos++;
}
//System.out.println("l2dashpos ready");
///////////////////////////////// pdash
//System.out.println("Computing pdash");

temp.clear();

inter.clear();
temp.clear();
temp1.clear();
temp.addAll(l1dashpos);
Collections.sort(temp);
for(int i=0;i<l1dashpos.size();i++){
	inter.add(Collections.binarySearch(temp, l1dashpos.get(i))+1);
	}
for(int i=0;i<l1dashpos.size();i++){
	temp1.add(0);
}

for(int i=0;i<inter.size();i++){
	temp1.set(inter.get(i)-1, i+1);
}


for(int i=0;i<l2dashpos.size();i++){
	pdash.add(temp1.get(l2dashpos.get(i)-1));
}
//System.out.println("pdash ready");
///////////////////////////// intialization over above


/*  System.out.println("Initialization .......");
//n.nextInt();
System.out.println("l1:\t\t"+l1);
//System.out.println("l1pos:\t "+l1pos);
System.out.println("l2:\t\t"+l2);
//System.out.println("l2pos:\t "+l2pos);
System.out.println("p:\t\t"+p);            
System.out.println("l1dash: \t"+l1dash);
System.out.println("l2dash: \t"+l2dash);
//System.out.println("l2dashpos: \t"+l2dashpos);
System.out.println("pdash: \t\t"+pdash);*/
//System.out.println("--------------------");

/////////////////////////////////////////////////////////////////////////////////////////////////

							//Works Fine above here

/////////////////////////////////////////////////////////////////////////////////////////////////

//System.out.println("Computing Offsets");   
// Computing offset o1 
if(op1==2 || op1==3){

if(op1==2){
 // l1dash is in ascending order
 for(int i=0;i<l1.size();i++){
 	lineNumber = 0;
 	while(lineNumber<l1dash.size() &&l1dash.get(lineNumber)<l1.get(i))
 		lineNumber++;
 	o1.add(lineNumber+1);
 }	 
 
}
if(op1==3){
 //l1dash is in descending order

 for(int i=0;i<l1.size();i++){
 	lineNumber = 0;
 	while(lineNumber<l1dash.size() &&l1dash.get(lineNumber)>l1.get(i))
 		lineNumber++;
 	o1.add(lineNumber+1);
 }
}

}
if(op1==1 || op1==4){

if(op1==1){
	 // l1dash is in ascending order
	 for(int i=0;i<l1.size();i++){
     	lineNumber = 0;
     	while(lineNumber<l1dash.size() &&l1dash.get(lineNumber)<=l1.get(i))
     		lineNumber++;
     	o1.add(lineNumber+1);
     }	 
}
if(op1==4){
 //l1dash is in descending order
 for(int i=0;i<l1.size();i++){
  	lineNumber = 0;
  	while(lineNumber<l1dash.size() &&l1dash.get(lineNumber)>=l1.get(i))
  		lineNumber++;
  	o1.add(lineNumber+1);
  }

}

}

// Computing offset o2 
if(op2==1 || op2==4){

if(op2==1){
// l2dash is in descending order
for(int i=0;i<l2.size();i++){
	lineNumber = 0;
	while(lineNumber<l2dash.size() &&l2dash.get(lineNumber)>l2.get(i))
		lineNumber++;
	o2.add(lineNumber+1);
}	 

}
if(op2==4){
// l2dash is in ascending order
for(int i=0;i<l2.size();i++){
	lineNumber = 0;
	while(lineNumber<l2dash.size() &&l2dash.get(lineNumber)<l2.get(i))
		lineNumber++;
	o2.add(lineNumber+1);
}	 

}       	 
}
if(op2==2 || op2==3){

if(op2==2){
	 // l2dash is in descending order
	 for(int i=0;i<l2.size();i++){
    	lineNumber = 0;
    	while(lineNumber<l2dash.size() &&l2dash.get(lineNumber)>=l2.get(i))
    		lineNumber++;
    	o2.add(lineNumber+1);
    }	 
	 
}
if(op2==3){
	 // l2dash is in ascending order
	 for(int i=0;i<l2.size();i++){
    	lineNumber = 0;
    	while(lineNumber<l2dash.size() &&l2dash.get(lineNumber)<=l2.get(i))
    		lineNumber++;
    	o2.add(lineNumber+1);
    }	 
	 
}       	        	 
}

/*for(int i=0;i<l1dash.size();i++){
bdash.add(0);
}
*/
for(int i=0;i<l1dash.size();i++){
bdash.add(-1);
}





/* System.out.println("o1: \t\t"+o1);
System.out.println("o2: \t\t"+o2);
System.out.println("bdash: \t\t"+bdash);*/
//System.out.println("------------Initialization Over !!-------------");
//n.nextInt();
// Initialization over
start = bdash.size()+1;
end = -1;
current = 0;
left = 0;
//System.out.println(start+" "+end);
for(int i=0;i<l2.size();i++){
	off2 = o2.get(i);
	if(off2>=2){
		for(int j=0;j<=off2-2;j++){
			current = pdash.get(j);
			//System.out.println(j+"current"+current);
			if(bdash.get(current-1)==-1)
			{
				//System.out.println("Processing....");
			if(current<start){
				bdash.set(current-1, start);
				start = current;
			}
			if(current>end){
				if(end>-1){
					bdash.set(end-1, current);
					end = current;
					bdash.set(end-1, bdash.size()+1);
				}
				else{
					end = current;
				}
			}
			else if(start<current && current<end){
				//System.out.println("came");
				for(int z=current-1;z>=start;z--){
					if(bdash.get(z-1)!=-1){
						left = z;
						break;
					}
					
				}
				bdash.set(current-1, bdash.get(left-1));
				bdash.set(left-1, current);
			}
		}
			//System.out.println("bdash"+bdash+"start"+start+"end"+end);
		}
	}
	
	off1 = o1.get(p.get(i)-1);
	if(off1<start)
		b = start;
	else
		b = off1;
	//System.out.println("value of b"+b);
	if(b<end){
		if(bdash.get(b-1)==-1){
		for(int z=b+1;z<=end;z++){
			if(bdash.get(z-1)!= -1)
			{
				b=z;
				break;
			}
		}
		}
	}
	//System.out.println("Got offset"+off1+"b"+b);            	
	while(b<=end){
		//System.out.println("Ans:\t\t"+l2pos.get(i)+" "+l1dashpos.get(b-1));
		if(which==0){
			store.get(q.LHS1).add(l2pos.get(i));
			store.get(q.LHS2).add(l1dashpos.get(b-1));	
		}
		else{

			for(int fornext=0;fornext<tables.size();fornext++){
				if(tables.keySet().toArray()[fornext].toString().equals(q.LHS2)){
					// new tables elements
					store.get(q.LHS2).add(l1dashpos.get(b-1));
				}
				else{
					// old ones
					//System.out.println(tables.get(tables.keySet().toArray()[fornext].toString()).size()+"--->"+(l2pos.get(i)-1));
					if(tables.get(tables.keySet().toArray()[fornext].toString()).size()==0){
						int tostop=9;
					}
					store.get(tables.keySet().toArray()[fornext].toString()).add(tables.get(tables.keySet().toArray()[fornext].toString()).get(l2pos.get(i)-1));
	
				}
				
			}
			
		}
		
		
		/*columns.clear();
		singlereadFromExcel(Filename,columns,l1dashpos.get(b-1),next);
		for(int add=0;add<next.size();add++)
			tuples.get(add).add(columns.get(add));*/
		b = bdash.get(b-1);
	}

}
return store;
	}
	public static void singlereadFromExcel(String Filename, ArrayList<Integer> a,int col,ArrayList<Integer> rows) throws IOException{
		//System.out.println(col);
		CSVReader reader = new CSVReader(new FileReader(Filename));
		 String [] nextLine;
		 int count=0;
		 ArrayList<String []> file = new ArrayList<String []>();
		 while ((nextLine = reader.readNext()) != null) {
			 file.add(nextLine);
			 /*count++;
			 if(rows.contains(o)){
				 // row to be read;
				 for(int i=0;i<col.size();i++){
					 a.add(Double.valueOf(nextLine[col.get(i)-1]).intValue());
					 
				 }
			 }*/
		 }
		 for(int i=0;i<rows.size();i++){
			 a.add(Double.valueOf(file.get(rows.get(i)-1)[col-1]).intValue());
		 }
	}
	
	public static void readFromExcel(String Filename,ArrayList<Integer> a,int x) throws IOException{
		//System.out.println(Filename);
		CSVReader reader = new CSVReader(new FileReader(Filename));
		 String [] nextLine;
	     while ((nextLine = reader.readNext()) != null) {
	        // nextLine[] is an array of values from the line
	        //System.out.println(nextLine[0] + nextLine[1] + " -   -"+ nextLine.length);
	    	 //a.add(Integer.parseInt(nextLine[x-1]));
	    	 //b.add(Integer.parseInt(nextLine[y-1]));
	    	 //System.out.println(x);
	    	 a.add(Double.valueOf(nextLine[x-1]).intValue());
	    	 //b.add(Double.valueOf(nextLine[y-1]).intValue());
	     }
		}
	public static int diag(ArrayList<TwoPredicate> q){
		String a,b,c,d;
		int found = -1;
		int temp;String temp1;
		ArrayList<String> visited = new ArrayList<String>();
		visited.add(q.get(0).LHS1);
		visited.add(q.get(0).LHS2);
		a = q.get(0).LHS1;
		b = q.get(0).LHS2;
		c = q.get(0).RHS1;
		d = q.get(0).RHS2;
		if(!a.equals(c)|| !b.equals(d))
			return 0;
		for(int i=1;i<q.size();i++){
			a = q.get(i).LHS1;
			b = q.get(i).LHS2;
			c = q.get(i).RHS1;
			d = q.get(i).RHS2;
			if(visited.contains(a)&&visited.contains(b)){
				// both of the tables have been visited;
				return 0;
			} 
			else if(visited.contains(a)|| visited.contains(b)){
				// exactly one of them has been visited earlier
				if(visited.contains(a)){
					if((a.equals(c) && b.equals(d)) || (a.equals(d)&&b.equals(c))){
						// right and left has same predicates
						if(a.equals(d)&&b.equals(c)){
							// swap d & c
							q.get(i).RHS2 = c;
							q.get(i).RHS1 = d;
							temp = q.get(i).RHS2Column;
							q.get(i).RHS2Column = q.get(i).RHS1Column;
							q.get(i).RHS1Column = temp;
							if(q.get(i).RHSOperator==1)
								q.get(i).RHSOperator = 4;
							else if(q.get(i).RHSOperator==2)
								q.get(i).RHSOperator = 3;
							else if(q.get(i).RHSOperator==3)
								q.get(i).RHSOperator = 2;
							else if(q.get(i).RHSOperator==4)
								q.get(i).RHSOperator = 1;
							
						}
						visited.add(b);
					}
					
				
					else{
						// not same predicates
						return 0;
					}
					
				}
				else{
					// visited contains b
					q.get(i).LHS2 = a;
					q.get(i).LHS1 = b;
					temp = q.get(i).LHS2Column;
					q.get(i).LHS2Column = q.get(i).LHS1Column;
					q.get(i).LHS1Column = temp;
					if(q.get(i).LHSOperator==1)
						q.get(i).LHSOperator = 4;
					else if(q.get(i).LHSOperator==2)
						q.get(i).LHSOperator = 3;
					else if(q.get(i).LHSOperator==3)
						q.get(i).LHSOperator = 2;
					else if(q.get(i).LHSOperator==4)
						q.get(i).LHSOperator = 1;
					a = q.get(i).LHS1;
					b = q.get(i).LHS2;
					// now visited contains a

					if((a.equals(c) && b.equals(d)) || (a.equals(d)&&b.equals(c))){
						// right and left has same predicates
						if(a.equals(d)&&b.equals(c)){
							// swap d & c
							q.get(i).RHS2 = c;
							q.get(i).RHS1 = d;
							temp = q.get(i).RHS2Column;
							q.get(i).RHS2Column = q.get(i).RHS1Column;
							q.get(i).RHS1Column = temp;
							if(q.get(i).RHSOperator==1)
								q.get(i).RHSOperator = 4;
							else if(q.get(i).RHSOperator==2)
								q.get(i).RHSOperator = 3;
							else if(q.get(i).RHSOperator==3)
								q.get(i).RHSOperator = 2;
							else if(q.get(i).RHSOperator==4)
								q.get(i).RHSOperator = 1;
							
						}
						visited.add(b);
					}
					
				
					else{
						// not same predicates
						return 0;
					}
					
				
				}
			}
			else{
				// none of them has been visited earlier
				return 0;
			}
		
		}
		return 1;
		
	}
	public static void diagonalize(ArrayList<TwoPredicate> q){
		String a ;
		String b ;
		int found = -1;
		int temp;String temp1;
		for(int i=1;i<q.size();i++){
			// see which is continued
			 a = q.get(i-1).LHS1;
			 b = q.get(i-1).LHS2;
			found = -1;
			if(q.get(i).LHS1.equals(a)||q.get(i).LHS2.equals(a))
				found = 0;
			else 
				found =1;
			if(found == 0){
				if(!q.get(i).LHS1.equals(a)){
					q.get(i).LHS2 = q.get(i).LHS1;
					q.get(i).LHS1 = a;
					temp = q.get(i).LHS2Column;
					q.get(i).LHS2Column = q.get(i).LHS1Column;
					q.get(i).LHS1Column = temp;
					if(q.get(i).LHSOperator==1)
						q.get(i).LHSOperator = 4;
					else if(q.get(i).LHSOperator==2)
						q.get(i).LHSOperator = 3;
					else if(q.get(i).LHSOperator==3)
						q.get(i).LHSOperator = 2;
					else if(q.get(i).LHSOperator==4)
						q.get(i).LHSOperator = 1;
				}
				if(!q.get(i).RHS1.equals(a)){
					q.get(i).RHS2 = q.get(i).RHS1;
					q.get(i).RHS1 = a;
					temp = q.get(i).RHS2Column;
					q.get(i).RHS2Column = q.get(i).RHS1Column;
					q.get(i).RHS1Column = temp;
					if(q.get(i).RHSOperator==1)
						q.get(i).RHSOperator = 4;
					else if(q.get(i).RHSOperator==2)
						q.get(i).RHSOperator = 3;
					else if(q.get(i).RHSOperator==3)
						q.get(i).RHSOperator = 2;
					else if(q.get(i).RHSOperator==4)
						q.get(i).RHSOperator = 1;
				}
			}
			else if(found ==1){

				if(!q.get(i).LHS1.equals(b)){
					q.get(i).LHS2 = q.get(i).LHS1;
					q.get(i).LHS1 = b;
					temp = q.get(i).LHS2Column;
					q.get(i).LHS2Column = q.get(i).LHS1Column;
					q.get(i).LHS1Column = temp;
					if(q.get(i).LHSOperator==1)
						q.get(i).LHSOperator = 4;
					else if(q.get(i).LHSOperator==2)
						q.get(i).LHSOperator = 3;
					else if(q.get(i).LHSOperator==3)
						q.get(i).LHSOperator = 2;
					else if(q.get(i).LHSOperator==4)
						q.get(i).LHSOperator = 1;
				}
				if(!q.get(i).RHS1.equals(b)){
					q.get(i).RHS2 = q.get(i).RHS1;
					q.get(i).RHS1 = b;
					temp = q.get(i).RHS2Column;
					q.get(i).RHS2Column = q.get(i).RHS1Column;
					q.get(i).RHS1Column = temp;
					if(q.get(i).RHSOperator==1)
						q.get(i).RHSOperator = 4;
					else if(q.get(i).RHSOperator==2)
						q.get(i).RHSOperator = 3;
					else if(q.get(i).RHSOperator==3)
						q.get(i).RHSOperator = 2;
					else if(q.get(i).RHSOperator==4)
						q.get(i).RHSOperator = 1;
				}
			
				
			}
			
			
		}
	}
	public static void showQuery(ArrayList<TwoPredicate> q){
		for(int i=0;i<q.size();i++){
			q.get(i).show();
		}
	}
	
	public static void thejoin(ArrayList<TwoPredicate> qs) throws IOException{
		ArrayList<Integer> next = new ArrayList<Integer>();
		HashMap<String, ArrayList<Integer>> got = new HashMap<String, ArrayList<Integer>>();
		HashMap<String,ArrayList<Integer>> tables = new HashMap<String,ArrayList<Integer>>();
		int found = -1;
		for(int i=0;i<qs.size();i++){
			if(tables.get(qs.get(i).LHS1) == null){
				tables.put(qs.get(i).LHS1, new ArrayList<Integer>());	
			}
			if(tables.get(qs.get(i).LHS2) == null){
				tables.put(qs.get(i).LHS2, new ArrayList<Integer>());	
			}
			
			tables = join(qs.get(i),next,i,tables);
			if(tables.get(qs.get(0).LHS1).size()<=0){
				break;
			}
			//System.out.println("------Over---"+"-----------");
			//qs.get(i).show();
		}
/*		next.clear();
		next.add(1);
		next.add(2);
		got=join(qs.get(qs.size()-1),next,qs.size(),got);*/
		//System.out.println("\n\nFinally");
		//System.out.println(got);
		System.out.println("Count(*) = "+tables.get(qs.get(0).LHS1).size());
	}
	
}
