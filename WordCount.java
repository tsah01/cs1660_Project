// Importing libraries 

import java.io.IOException;
import java.util.StringTokenizer;
import javax.naming.Context;
import org.apache.hadoop.io.IntWritable; 
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.io.Text;

// Importing libraries 
import java.io.IOException;
import java.util.Iterator;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.Reducer;
import javax.naming.Context;

// Importing libraries 
import java.io.IOException; 
import org.apache.hadoop.fs.Path; 
import org.apache.hadoop.io.IntWritable; 
import org.apache.hadoop.io.Text; 
import org.apache.hadoop.mapred.FileInputFormat; 
import org.apache.hadoop.mapred.FileOutputFormat; 
import org.apache.hadoop.mapred.Job; 
//import com.example.servlets.WordCountMapper;//importing the WordCountMapper file
//mport com.example.servlets.WordCountReducer;//importing the WordCountReducer file


class WordCountMapper extends Mapper<LongWritable, Text, Text, IntWritable> {

    private final static IntWritable one = new IntWritable(1);
    private Text word = new Text();

        public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String line = value.toString();
            StringTokenizer tokenizer = new StringTokenizer(line);

            while (tokenizer.hasMoreTokens()) {
                word.set(tokenizer.nextToken());
                context.write(word, one);
            }

        }
    
}


class WordCountReducer extends Reducer<Text, IntWritable, Text , IntWritable>{
    public void reduce(Text key, Iterable<IntWritable> values, Context context)
            throws IOException, InterruptedException {
        int sum = 0;
        for (IntWritable value : values) {
            sum += value.get();
        }
        context.write(key, new IntWritable(sum));
    }
}


public class WordCount {
   

    public static void main(String[] args)
            throws IOException, ClassNotFoundException, InterruptedException {
        if (args.length != 2) {
            System.err.println("Word count <input path> <output path>");
            System.exit(-1);
        }
        Job job = new Job();
        job.setJarByClass(WordCount.class);
        job.setJobName("Word Count");

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        
        job.setMapperClass(WordCountMapper.class);
        job.setReducerClas(WordCountReducer.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
        job.waitForCompletion(true);
    }
}
