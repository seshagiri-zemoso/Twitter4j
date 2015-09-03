import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.Function3;
import org.apache.spark.api.java.function.PairFunction;

import scala.Tuple2;


import java.io.File;
import java.util.Arrays;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class TwitterTrending {
  private static final FlatMapFunction<String, String> WORDS_EXTRACTOR =
      new FlatMapFunction<String, String>() {
        public Iterable<String> call(String s) throws Exception {
          return Arrays.asList(s.split(" "));
        }
      };

  private static final PairFunction<String, String, Integer> WORDS_MAPPER =
      new PairFunction<String, String, Integer>() {
        public Tuple2<String, Integer> call(String s) throws Exception {
          return new Tuple2<String, Integer>(s, 1);
        }
      };

  private static final Function2<Integer, Integer, Integer> WORDS_REDUCER =
      new Function2<Integer, Integer, Integer>() {
        public Integer call(Integer a, Integer b) throws Exception {
          return a + b;
        }
      };
     
private static final Function<String,Boolean> WORDS_FILTER = 
		   new Function<String,Boolean>(){
	   public Boolean call(String s)throws Exception {
		   return s.contains("#");
	   }
   };
   
   private static final Function2<Integer, Integer, Integer> WORDS_POPULAR =
		     new Function2<Integer, Integer, Integer>() {
		       public Integer call(Integer a, Integer b) throws Exception {
		 
		         return (a>b)?a:b;
		       }
		     };
  

  public static void main(String[] args) {
	  
    if (args.length < 1) {
      System.err.println("Please provide the input file full path as argument");
      System.exit(0);
    }
    
    Map<Integer,String> list = new TreeMap<Integer,String>();

    SparkConf conf = new SparkConf().setAppName("org.sparkexample.WordCount").setMaster("local");
    JavaSparkContext context = new JavaSparkContext(conf);

    JavaRDD<String> file = context.textFile(args[0]);
    JavaRDD<String> words = file.flatMap(WORDS_EXTRACTOR);
    JavaRDD<String> hash = words.filter(WORDS_FILTER);
    JavaPairRDD<String, Integer> pairs = hash.mapToPair(WORDS_MAPPER);
    JavaPairRDD<String, Integer> counter = pairs.reduceByKey(WORDS_REDUCER);
    int max=1;
    String trending_Now="";
    
    for(Tuple2<String,Integer> tuple :counter.collect()){
    	String hashtag = tuple._1;
    	int count = tuple._2;
    	
    	list.put(count,hashtag);
    	
    	if(count > max){
    		max=count;
    		trending_Now=hashtag;
    	}
    }
    counter.saveAsTextFile(args[1]);
   
    System.out.println("Trending Now " + trending_Now);
    System.out.println(list);
  }
}