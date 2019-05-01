import java.util.*;
import java.util.stream.IntStream;
import java.io.*;
public class KNN3 {
  static double[][] instances;
 static int z=0;
  
  private static String findMajority(String[] array) {
	  Set<String> h=new HashSet<String>(Arrays.asList(array));
	  String[] uniqueValues=h.toArray(new String[0]);
	  //counts for unique strings
	  int[] counts=new int[uniqueValues.length];
	  //loop thru unique strings and counts how many times they apper in original array
	  for(int i=0;i<uniqueValues.length;i++) {
		  for(int j=0;j<array.length;j++) {
			  if(array[j].equals(uniqueValues[i])) {	   
			  counts[i]++;
			  }
		  }
	  }
	  for(int i=0;i<counts.length;i++)
		  System.out.println("count:"+counts[i]);
	  
	  for(int i=0;i<uniqueValues.length;i++)
		  System.out.println(uniqueValues[i]);
	  
	  int max=counts[0];
	  for(int counter=1;counter<counts.length;counter++) {
		  if(counts[counter]>max) {
			  max=counts[counter];
		  }
	  }
	  
	  int freq=0;
	  for(int counter=0;counter<counts.length;counter++) {
		  if(counts[counter]==max) {
			  freq++;
		  }
	  }
	  int index=-1;
	  if(freq==1) {
		  for(int counter=0;counter<counts.length;counter++) {
			  if(counts[counter]==max) {
				  index=counter;
				  break;
			  }
		  }
		  return uniqueValues[index];
	  }
	  else {
		  int[] ix=new int[freq];
		  System.out.println("multiple majority classes:"+freq+"classes");
		  int ixi=0;
		  for(int counter=0;counter<counts.length;counter++) {
			  if(counts[counter]==max) {
				  ix[ixi]=counter;
				  ixi++;
			  }
		  }
		  for(int counter=0;counter<ix.length;counter++) {
			  System.out.println("class index:"+ix[counter]);
		  }
		  //now choose one at random
		  Random generator=new Random();
		  int rIndex=generator.nextInt(ix.length);
		  System.out.println("random index:"+rIndex);
		  int nIndex=ix[rIndex];
		  return uniqueValues[nIndex];
		  
	  }
  }
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Scanner sc=new Scanner(System.in);
		int k=1;
		instances= new double[26000][k];
	       List<City> cityList=new ArrayList<City>();
	       List<Result> resultList=new ArrayList<Result>();
		File file=new File("Height.txt");
		BufferedReader br=new BufferedReader(new FileReader(file));
		String[] st = br.readLine().replaceAll("\\s+$", "").split(" ");
		int n=Integer.parseInt(st[0]);
		 br.readLine();
		IntStream.range(0, n).forEach(i -> {
            try {
                String[] st1 = br.readLine().replaceAll("\\s+$", "").split(" ");
                //System.out.println(st1[0]);
                instances[z][0]=Double.parseDouble(st1[1]);
                /**instances[z][1]=Double.parseDouble(st1[1]);
                instances[z][2]=Double.parseDouble(st1[2]);
                instances[z][3]=Double.parseDouble(st1[3]);
                instances[z][4]=Double.parseDouble(st1[4]);**/
                cityList.add(new City(instances[z],st1[2]));
                //System.out.println(instances[z][0]+" "+st1[5]);
                z+=1;
                
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
                );
		br.close();
       
       
       //data about unknown city
		System.out.print("Enter Height(in Inches):");
       double[] query=new double[k];
       for(int q=0;q<k;q++){
       	query[q]=sc.nextDouble();
       }
       //find distnaces
       
       for(City city:cityList) {
    	   double dist=0.0;
    	   float accuracy=0;
    	   float sum=0;
    	   float sum1=0;
    	   for(int j=0;j<city.cityAttributes.length;j++) {
    	   	//System.out.println(city.cityAttributes[j]+" "+j);
    		   dist+=Math.pow(city.cityAttributes[j]-query[j], 2);
    		   sum=(float)city.cityAttributes[j];
    		   
    	   }
    	   sum1=(float)query[0];
    	   accuracy=((sum1)/sum)*100;
    	   if(accuracy>100)
    	   	accuracy=(sum/sum1)*100;
    	   if(accuracy<0)
    	   	 accuracy=-accuracy;

    	   double distance=Math.sqrt(dist);
    	   resultList.add(new Result(distance, city.cityName,accuracy));
    	   
       }
      Collections.sort(resultList, new DistanceComparator());
      String[] ss=new String[k];
      for(int x=0;x<k;x++) {
    	  //System.out.println(resultList.get(x).cityName+"...."+resultList.get(x).distance);
    	  ss[x]=resultList.get(x).cityName;
      }
      String majClass=findMajority(ss);
      float acc=0;
      for(int x=0;x<k;x++){
      	if(resultList.get(x).cityName==majClass){
      		acc=resultList.get(x).accuracy;
      		break;
      	}
      }
      System.out.println("Weight is: "+majClass+"(Pounds)");
      System.out.println("Accuracy:"+acc+"%");
       
	}
	static class City{
		double[] cityAttributes;
		String cityName;
		public City(double[] cityAttributes,String cityName) {
			this.cityName=cityName;
			this.cityAttributes=cityAttributes;
		}
	}
	static class Result{
		float accuracy;
		double distance;
		String cityName;
		public Result(double distance,String cityName,float accuracy) {
			this.accuracy=accuracy;
			this.cityName=cityName;
			this.distance=distance;
	}
	}
	//simple comparator class used to compare results via distances
	static class DistanceComparator implements Comparator<Result>{

		@Override
		public int compare(Result a, Result b) {
			// TODO Auto-generated method stub
			return a.distance<b.distance? -1 : a.distance == b.distance ? 0:1;
		}
		
	}

}
