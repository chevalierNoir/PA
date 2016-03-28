/**
 * Created by bowenshi on 29/02/16.
 */
package com.example.bowenshi.intersaclay;

import android.os.Environment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;

public class PersonalFileOperator {

    public void addItem(String data){
        File root= Environment.getExternalStorageDirectory();
        File dir=new File(root.getAbsolutePath()+"/InterSaclay/UserInfo");
        boolean isExisted=dir.exists();
        if(!isExisted)
            dir.mkdirs();
        File file=new File(dir, "myData.txt");
        if(!file.exists()){
            try{
                file.createNewFile();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        try {
            BufferedWriter bufferedWriter=new BufferedWriter(new FileWriter(file,true));
            bufferedWriter.write(data);
            bufferedWriter.close();

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }


    public String[] readItem(String username){
        String[] item=new String[20];
        FileInputStream is;
        File root= Environment.getExternalStorageDirectory();
        File file=new File(root.getAbsolutePath()+"/InterSaclay/UserInfo/myData.txt");

        if(!file.exists()){
            return item;
        }
        try{
            is=new FileInputStream(file);
        } catch (Exception e) {
            e.printStackTrace();
            return item;
        }
        BufferedReader bufferedReader;
//        InputStreamReader streamReader=new InputStreamReader(is);
        try{
            bufferedReader=new BufferedReader(new FileReader(file));
        }catch (Exception e){
            e.printStackTrace();
            return item;
        }

        String line=new String("init");
        try{
            while(((line=bufferedReader.readLine())!=null)){

                String[] parts=line.split("\\$");
                if(parts[0].equals(username)){
                    for(int i=0;i<parts.length;++i){
                        item[i]=parts[i];
                    }
                    break;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            return item;
        }
        return item;
    }

    private String returnItemString(String username){
        String item=new String();
        FileInputStream is;
        File root= Environment.getExternalStorageDirectory();
        File file=new File(root.getAbsolutePath()+"/InterSaclay/UserInfo/myData.txt");

        if(!file.exists()){
            return item;
        }
//        try{
//            is=new FileInputStream(file);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return item;
//        }
        BufferedReader bufferedReader;
//        InputStreamReader streamReader=new InputStreamReader(is);
        try{
            bufferedReader=new BufferedReader(new FileReader(file));
        }catch (Exception e){
            e.printStackTrace();
            return item;
        }

        String line=new String("init");
        try{
            while(((line=bufferedReader.readLine())!=null)){

                String[] parts=line.split("\\$");
                if(parts[0].equals(username)){
                    item=line;
                    break;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            return item;
        }
        return item;
    }

    public boolean hasUser(String username){
        String rightPsw=new String();
        boolean hasUser=false;
        FileInputStream is;
        File root= Environment.getExternalStorageDirectory();
        File file=new File(root.getAbsolutePath()+"/InterSaclay/UserInfo/myData.txt");

        if(!file.exists()){
            return false;
        }
        try{
            is=new FileInputStream(file);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        BufferedReader bufferedReader;
//        InputStreamReader streamReader=new InputStreamReader(is);
        try{
            bufferedReader=new BufferedReader(new FileReader(file));
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

        String line=new String("init");
        try{
            while(((line=bufferedReader.readLine())!=null)){

                String[] parts=line.split("\\$");
                if(parts[0].equals(username)){
                    hasUser=true;
                    break;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return hasUser;
    }


    public boolean updateItem(String username, String newItemString){
        File root= Environment.getExternalStorageDirectory();
        File originalFile=new File(root.getAbsolutePath()+"/InterSaclay/UserInfo/myData.txt");
        File tempFile=new File(root.getAbsolutePath()+"/InterSaclay/UserInfo/myTempData.txt");
        try{
            if(!tempFile.exists()){
                tempFile.createNewFile();
            }
            BufferedReader reader=new BufferedReader(new FileReader(originalFile));
            BufferedWriter writer=new BufferedWriter(new FileWriter(tempFile));

            String oldItem=returnItemString(username);
            String currentLine=new String();
            while ((currentLine=reader.readLine())!=null){
                String trimmedLine=currentLine.trim();
                if(trimmedLine.equals(oldItem)){
                    currentLine=newItemString;
                }
                writer.write(currentLine+System.getProperty("line.separator"));
            }
            writer.close();
            reader.close();


        }catch (Exception e){
            e.printStackTrace();
        }
        boolean isSuccessful=tempFile.renameTo(originalFile);
        return isSuccessful;

    }
}
