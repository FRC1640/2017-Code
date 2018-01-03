package utilities;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class FileIO{
	
	private File file;
	private FileReader fileReader;
	private FileWriter fileWriter;
	private BufferedWriter bufferedWriter;
	private BufferedReader bufferedReader;
	
	public FileIO(String filePath){
		try{
			file = new File(filePath);
			if(file.exists()){
				file.delete();
			}
			file.createNewFile();
			
			fileReader = new FileReader(file);
			fileWriter = new FileWriter(file);
			bufferedWriter = new BufferedWriter(fileWriter);
			bufferedReader = new BufferedReader(fileReader);
		}catch(Exception e){e.printStackTrace();}
	}
	
	public String readLine(){
		try{
			return bufferedReader.readLine();
		}catch(Exception e){e.printStackTrace();}
		return "File did not work";
	}
	
	public void write(String write){
		try{
			bufferedWriter.write(write);
			bufferedWriter.flush();
		}catch(Exception e){e.printStackTrace();}
	}
	
	public void close(){
		try{
			bufferedWriter.close();
			bufferedReader.close();
		}catch(Exception e){e.printStackTrace();}
	}
	
	public void flush(){
		try{
			bufferedWriter.flush();
		}catch(Exception e){e.printStackTrace();}
	}
	
	public void clearFile(){
		try{
			file.delete();
			file.createNewFile();
		}catch(Exception e){e.printStackTrace();}
	}

}
