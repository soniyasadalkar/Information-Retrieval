import java.io.IOException;
import java.util.StringTokenizer;
import java.util.*;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

public class InvertedIndexJob{
	public static class MyMapper extends Mapper<LongWritable, Text, Text, Text>{
		private final static IntWritable one = new IntWritable(1);
		private Text word = new Text();

		public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
			String[] line = value.toString().split("\t");
			if( line.length == 2){
				Text doc_id = new Text(line[0]);
				StringTokenizer itr = new StringTokenizer(line[1]);
				while( itr.hasMoreTokens()){
					word.set(itr.nextToken());
					context.write(word, doc_id);
				}
			}
		}
	}

	public static class MyReducer extends Reducer<Text, Text, Text, Text>{
		private IntWritable result = new IntWritable();
		
		public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException{
			int sum = 0;
			HashMap<String, Integer> counts_map = new HashMap<String, Integer>();
			for( Text val : values){
				String doc_id = val.toString();
				if( counts_map.containsKey(doc_id ))
					counts_map.put(doc_id, counts_map.get(doc_id) + 1);
				else
					counts_map.put(doc_id, 1);
			}
			Iterator it = counts_map.entrySet().iterator();
			StringBuilder invert_index_str = new StringBuilder();
    			while (it.hasNext()) {
        			Map.Entry pair = (Map.Entry)it.next();
				invert_index_str.append(pair.getKey() + ":" + pair.getValue()+"\t");
			}
			Text inverted_index_list = new Text(invert_index_str.toString());
        		context.write(key, inverted_index_list);
		}
	}

	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		Job job = Job.getInstance(conf, "word count");
		job.setMapperClass(MyMapper.class);
	//	job.setCombinerClass(IntSumReducer.class);
		job.setReducerClass(MyReducer.class);

		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(Text.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);

		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);

		job.setNumReduceTasks(10);
		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		job.setJarByClass(InvertedIndexJob.class);
		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}

}

