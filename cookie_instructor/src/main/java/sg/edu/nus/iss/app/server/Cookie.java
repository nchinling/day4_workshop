package sg.edu.nus.iss.app.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Cookie {
    
    public static String getRandomCookie(String cookieFilepath, String cookieResultPath, String mailMergePath){
        String randomCookie = "";
        List<String> cookies = new LinkedList<>();
        List<String> cookiesResult = new ArrayList<>();
        try {
            cookies = getDataFromText(cookieFilepath);
            cookiesResult = getDataFromText(cookieResultPath);
            System.out.println(cookiesResult.size());
            if(cookiesResult.size() == 0){
                System.out.println("init result file ...");
                for(int x = 0; x < cookies.size(); x++){
                    cookiesResult.add("");
                }
                initCookieResultFile(cookieResultPath, cookies.size());
            }
                
            Random rand = new Random();
            int randVal = rand.nextInt(cookies.size());
            System.out.println(randVal);
            randomCookie = cookies.get(randVal);
            
            if(mailMergePath != null ){
                int charSizeOFRandom = randomCookie.length();
                String mailMergePara = readMailMergeText(mailMergePath);
                mailMergePara = mailMergePara.replaceAll("\\$count\\b", String.valueOf(charSizeOFRandom));
                System.out.println(mailMergePara);
                initCookieResultFile(mailMergePath);
                PrintWriter writer = new PrintWriter(mailMergePath);
                writer.print(mailMergePara);
                writer.close();
            }
            
            writeCookieNametoResultFile(randomCookie, 
                randVal, cookiesResult, cookieResultPath);
            System.out.println("RANDOM COOKIE >> " + randomCookie );
        }catch(FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return randomCookie;
    }

    private static void initCookieResultFile(String resultPath, int resultSize) throws IOException{
        FileWriter filewriter = new FileWriter(resultPath);
        if(resultSize > 0){
            for(int x = 0 ; x  < resultSize; x++){
                filewriter.write(""
                    + "\n");
            }
        }else{
            filewriter.write(""
                    + "\n");
        }
        
        filewriter.close();
    }

    private static void initCookieResultFile(String resultPath) throws IOException{
        initCookieResultFile(resultPath, 0);
    }

    private static synchronized String readMailMergeText(String mailmergeFile) throws IOException{
        System.out.println(mailmergeFile);
        BufferedReader br = new BufferedReader(new FileReader(mailmergeFile));
        StringBuilder strBuilder = new StringBuilder();
        String line;
        while((line = br.readLine()) != null){
            //process the line
            System.out.println(line);
            strBuilder.append(line);
        }
        br.close();
        System.out.println(strBuilder.toString());
        return strBuilder.toString();
    }

    private static void writeCookieNametoResultFile(
            String cookieName, int cookieNameIndex, 
            List<String>cookiesResult,
            String resultPath) throws IOException{
        System.out.println( "cookiesResult >>> " +cookiesResult);
        if(!cookiesResult.contains(cookieName))
                cookiesResult.set(cookieNameIndex, cookieName);
        System.out.println(cookiesResult);
        PrintWriter writer = new PrintWriter(resultPath);
        writer.print("");
        writer.close();

        FileWriter filewriter = new FileWriter(resultPath);
        for(String cookieNameFromArr : cookiesResult){
            filewriter.write(cookieNameFromArr
                + "\n");
        }
        filewriter.close();
    }

    public static List<String> getDataFromText(String filepath) throws IOException{
        BufferedReader br = new BufferedReader(new FileReader(filepath));

        List<String> lists = new LinkedList<>();

        String line;
        while ((line = br.readLine()) != null) {
            lists.add(line);         
        }
        return lists;
    }
}