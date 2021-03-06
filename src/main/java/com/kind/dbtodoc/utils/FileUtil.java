package com.kind.dbtodoc.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** 
 * Description: 文件工具类
 * 
 * @author jiujiya
 * @version 1.0 
 */
public class FileUtil {

    /**
     * 写内容
     * @param file
     * @param content
     * @throws Exception 
     */
    public static void createFile(String path, String content) throws Exception {
    	File file = new File(path);
    	if(!file.exists())    
    	{    
    	    try {    
    	        file.createNewFile();    
    	    } catch (IOException e) {
                UtilHiddleException.hiddleException(e);
    	    }    
    	}else {
    		throw new Exception("文件存在无法创建" + file.getAbsolutePath());
    	}
        BufferedWriter bw = null;
        try {
            FileWriter fw = new FileWriter(file, true);
            bw = new BufferedWriter(fw);
            bw.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 追加内容
     * @param file
     * @param content
     */
    public static void writeToTxtByFileWriter(File file, String content) {
		FileOutputStream fos = null;
		OutputStreamWriter osw = null;
		try {
			fos = new FileOutputStream(file);
			osw = new OutputStreamWriter(fos, "UTF-8");
			osw.write(content);
			osw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			close(osw);
			close(fos);
		}
    }
    
    
	/**
	 * 已UTF-8读取 文本文件
	 * @param path
	 * @return
	 */
	public static StringBuffer readTextFile(String path) {
		return readTextFile(path, "UTF-8");
	}
    
    public static List<String> readTextsFile(String path){
        return readTextsFile(path, "UTF-8");
    }
    
    /**
     * 获得 要保存的 文件 的文件夹
     * @param path
     * @return
     */
    public static String getDir(String path){
        
        int youindexOf = path.lastIndexOf("\\");
        int zuoindexOf = path.lastIndexOf("/");
        
        if(zuoindexOf > youindexOf){
            path = path.substring(0, path.lastIndexOf("/"));
        }else{
            path = path.substring(0, path.lastIndexOf("\\"));
        }
        
        return path + "/";
    }

    /**
     * 获得 文件的类型
     * @param path
     * @return
     */
    public static String getFileType(String path){
        return path.substring(path.lastIndexOf(".") + 1, path.length());
    }
    
    /**
     * 获得 要保存的 文件 的文件夹
     * @param path
     * @return
     */
    public static String getFileName(String path){
        
        int youindexOf = path.lastIndexOf("\\");
        int zuoindexOf = path.lastIndexOf("/");
        
        if(zuoindexOf > youindexOf){
            path = path.substring(path.lastIndexOf("/") + 1, path.length());
        }else{
            path = path.substring(path.lastIndexOf("\\") + 1, path.length());
        }
        
        return path;
    }
    
    /**
     * 生成 要保存的 文件 的文件夹
     * @param path
     */
    public static void addDirs(String path){
        try {
            File dirs = new File(getDir(path));
            if (!dirs.exists()) {
                dirs.mkdirs();
            }
        } catch (Exception e) {
            UtilHiddleException.hiddleException(e);
        }
    }

    /**
     * 关闭资源
     * @param close
     */
    public static void close(Closeable obj) {
        if (obj != null) {
            try {
                obj.close();
            } catch (IOException e) {
                // 不可能存在的异常
                UtilHiddleException.hiddleException(e);
            }
        }
    }
    
    /**
     * 读取 文本文件
     * @param path
     * @param encoding 编码
     * @return
     */
    public static List<String> readTextsFile(String path, String encoding){
        File file = new File(path);
        if (!file.exists()) return null;
        List<String> list = new ArrayList<String>();
        BufferedReader br = null;
        try {
            try {
                br = new BufferedReader(new InputStreamReader (new FileInputStream(file), encoding));
                String line = null;
                while( (line = br.readLine()) != null) {
                    list.add(line);
                }
                return list;
            }finally {
                if (br != null) {
                    br.close();
                }
            }
        } catch (Exception e) {
            UtilHiddleException.hiddleException(e);
            return null;
        }
    }
    
	/**
	 * 读取 文本文件
	 * @param path
	 * @param encoding 编码
	 * @return
	 */
	public static StringBuffer readTextFile(String path, String encoding) {
		File file = new File(path);
		if (!file.exists()) return new StringBuffer("");
		StringBuffer str = new StringBuffer();
        List<String> list = readTextsFile(path, encoding);
        for (String string : list) {
            str.append(string);
        }
        return str;
	}
	
	/**
	 * 获得 文本文件 的行数
	 * @param path
	 * @return
	 */
	public static long getLine(String path) {
		File file = new File(path);
		if (!file.exists()) return 0;
		long lines = 0;
		BufferedReader br = null;
		try {
			try {
				br = new BufferedReader(new FileReader(file));
				while(br.readLine() != null) {
					lines ++;
				}
				return lines;
			}finally {
				if (br != null) {
					br.close();
				}
			}
		} catch (Exception e) {
			UtilHiddleException.hiddleException(e);
			return 0;
		}
	}

	/**
	 * 获得 文本文件 或 文件夹 中 文件 共有多少行
	 * @param path
	 * @return
	 */
	public static long getTextFileLines(String path, String[] fileSuffix){
		List<String> list = getFileList(path, fileSuffix);
		long lines = 0;
		for (String one : list) {
			System.out.println(one + " : " + getLine(one) + " 行");
			lines += getLine(one);
		}
		return lines;
	}
	
	/**
	 * 获得 文本文件 或 文件夹 中 文件 共有多少字
	 * @param path
	 * @return
	 */
	public static long getTextFileFontLength(String path, String[] fileSuffix){
		List<String> list = getFileList(path, fileSuffix);
		long fontLength = 0;
		for (String one : list) {
			System.out.println(one + " : " + readTextFile(one).toString().length() +  " 字");
			fontLength += readTextFile(one).toString().length();
		}
		return fontLength;
	}

	/**
	 * 获得文件夹中 的 文件
	 * @param strPath
	 * @return
	 */
	public static List<String> getFileList(String strPath) {
		return new FileList(strPath).getFileList();
	}

	/**
	 * 获得文件夹中 的 文件
	 * @param strPath 文件夹路径
	 * @param fileSuffix 要获得文件的后缀
	 * @return
	 */
	public static List<String> getFileList(String strPath, String fileSuffix) {
		return getFileList(strPath, new String[]{fileSuffix});
	}
	
	/**
	 * 获得文件夹中 的 文件
	 * @param strPath 文件夹路径
	 * @param fileSuffix 要获得文件夹的后缀
	 * @return
	 */
	public static List<String> getFileList2(String strPath, String fileSuffix) {
	    return getFileList(strPath, new String[]{fileSuffix});
	}

    /**
     * 获得文件夹中 的 文件
     * @param strPath 文件路径
     * @param fileSuffix 要获得文件的后缀
     * @return
     */
    public static List<String> getFileList(String strPath, String[] fileSuffix) {
        return getFileList(strPath, fileSuffix, new String[]{});
    }
    
    /**
     * 获得文件夹中 的 文件
     * @param strPath 文件夹路径
     * @param fileSuffix 要获得文件的后缀
     * @return
     */
    public static List<String> getFileList2(String strPath, String[] fildDirSuffix) {
        return getFileList(strPath, new String[]{}, fildDirSuffix);
    }
    
	/**
	 * 获得文件夹中 的 文件
	 * @param strPath 文件夹路径
	 * @param fileSuffix 要获得文件的后缀
	 * @return
	 */
	private static List<String> getFileList(String strPath, String[] fileSuffix, String[] fildDirSuffix) {
		List<String> list = new ArrayList<String>();
		for (String s : fileSuffix) {
			list.add("." + s);
		}
		List<String> list2 = new ArrayList<String>();
		for (String s : fildDirSuffix) {
		    list2.add(s);
		}
		return new FileList(strPath, list, list2).getFileList();
	}
    
	/**
	 * 管道对管道传输，java最快的copy方法
	 * @param f1
	 * @param f2
	 */
	public static void copyFile(File f1, File f2) {
    	if(!f1.exists()){
    		try {
				f1.createNewFile();
			} catch (IOException e) {
			}
    	}
    	if(!f2.exists()){
    		try {
				f2.createNewFile();
			} catch (IOException e) {
			}
    		
    	}
		FileInputStream in = null;
		FileOutputStream out = null;
		try {
			int length = 2097152;
			in = new FileInputStream(f1);
			out = new FileOutputStream(f2);
			FileChannel inC = in.getChannel();
			FileChannel outC = out.getChannel();
			while (true) {
				if (inC.position() == inC.size()) {
					inC.close();
					outC.close();
					return;
				}
				if ((inC.size() - inC.position()) < 20971520)
					length = (int) (inC.size() - inC.position());
				else
					length = 20971520;
				inC.transferTo(inC.position(), length, outC);
				inC.position(inC.position() + length);
			}
		} catch (Exception e) {
			UtilHiddleException.hiddleException(e);
		} finally{
			close(in);
			close(out);
		}
	}
	
	//复制文件夹
	public static void copyDir(String oldPath, String newPath) throws IOException {
        File file = new File(oldPath);
        String[] filePath = file.list();
        
        if (!(new File(newPath)).exists()) {
            (new File(newPath)).mkdir();
        }
        
        for (int i = 0; i < filePath.length; i++) {

            File sourcePath = new File(oldPath  + file.separator + filePath[i]);
            if (sourcePath.isDirectory()) {
                copyDir(oldPath  + file.separator  + filePath[i], newPath  + file.separator + filePath[i]);
            }
            if (sourcePath.isFile()) {
            	copyFile(sourcePath, new File(newPath + file.separator + filePath[i]));
            }
            
        }
    }
    
    public static byte[] readInputStream(InputStream inStream) throws Exception{  
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();  
        //创建一个Buffer字符串  
        byte[] buffer = new byte[1024];  
        //每次读取的字符串长度，如果为-1，代表全部读取完毕  
        int len = 0;  
        //使用一个输入流从buffer里把数据读取出来  
        while( (len=inStream.read(buffer)) != -1 ){  
            //用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度  
            outStream.write(buffer, 0, len);  
        }  
        //关闭输入流  
        inStream.close();  
        //把outStream里的数据写入内存  
        return outStream.toByteArray();  
    }  
	
	/**
	 * 將网络文件保存
	 * @param url 网络url
	 * @param fileDir 要保存到本地的文件名 
	 */
	public static void saveWebFile2(final String url, final String filePath) {
		Thread thread = new Thread() {
			public void run() {
				try {
					URL urll = new URL(url);
					URLConnection uc = urll.openConnection();
					InputStream is = uc.getInputStream();
					File file = new File(filePath);
					FileOutputStream out = new FileOutputStream(file);
					int i = 0;
					while ((i = is.read()) != -1) {
						out.write(i);
					}
					is.close();
				} catch (Exception e) {
					UtilHiddleException.hiddleException(e);
				}
			}
		};
		thread.start();
	}

	/**
	 * 將网络文件保存
	 * @param url 网络url
	 * @param fileDir 要保存到本地的文件夹 D: 或者 D:/test
	 */
	public static void saveWebFile(final String url, final String fileDir) {
		Thread thread = new Thread() {
			public void run() {
				try {
					URL urll = new URL(url);
					URLConnection uc = urll.openConnection();
					String houzhui = url.substring(url.lastIndexOf("."), url
							.length());
					InputStream is = uc.getInputStream();
					File file = new File(fileDir + "//" + "" + houzhui);
					FileOutputStream out = new FileOutputStream(file);
					int i = 0;
					while ((i = is.read()) != -1) {
						out.write(i);
					}
					is.close();
				} catch (Exception e) {
					UtilHiddleException.hiddleException(e);
				}
			}
		};
		thread.start();
	}
	
	/**
     * 递归删除目录下的所有文件及子目录下所有文件
     * @param dir 将要删除的文件目录
     * @return boolean Returns "true" if all deletions were successful.
     *                 If a deletion fails, the method stops attempting to
     *                 delete and returns "false".
     */
    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            //递归删除目录中的子目录下
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }
    
   
    public static boolean deleteDirChidren(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            //递归删除目录中的子目录下
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public static List<Map<String, Object>> getFilesNameAndTime(String path,int start, int end){
        List<Map<String, Object>> files=new ArrayList<Map<String,Object>>();
         File file=new File(path);
         File[] tempList = file.listFiles();
         Arrays.sort(tempList, new FileUtil.CompratorByLastModified());  
         System.out.println("该目录下对象个数："+tempList.length);
         for (int i = start; i < end; i++) {
        	 if ( i<tempList.length) {
        		 if (tempList[i].isFile()) {
        			 Map<String,Object> filesMap=new HashMap<String, Object>();
        			 filesMap.put("TITLE", tempList[i].getName().toString().replaceAll("\\\\", "/"));
        			 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        			 Calendar cal = Calendar.getInstance();
        			 cal.setTimeInMillis(tempList[i].lastModified());
//        			 filesMap.put("TIME",  sdf.format(cal.getTime()));
        			 files.add(filesMap);
        		 }
			}
         }
        for(Map<String, Object> realfile:files){
              System.out.println("文件   路径："+realfile.get("fileUrl")+"&文件名:"+realfile.get("fileName"));
        }
          
        return files;
    }
  //根据文件修改时间进行比较的内部类
    static class CompratorByLastModified implements Comparator<File> {  
        
        public int compare(File f1, File f2) {  
            long diff = f1.lastModified() - f2.lastModified();  
            if (diff > 0) {  
                   return -1;  
            } else if (diff == 0) {  
                   return 0;  
            } else {  
                  return 1;  
            }  
        } 

}
	public static long getFileNum(String path) {
		File file=new File(path);
        File[] tempList = file.listFiles();
		return tempList.length;
	}
}

class FileList{
	private List<String> filelist = new ArrayList<String>();
	
	/**
	 * @param strPath 要查询的文件夹
	 * @param fileSuffix 要查询文件的后缀
	 * @param fileDirSuffix 
	 */
	public FileList(String strPath, List<String> fileSuffix, List<String> fileDirSuffix){
		findFileList(strPath, fileSuffix, fileDirSuffix);
	}

    /**
	 * @param strPath 要查询的文件夹
	 */
	public FileList(String strPath){
		findFileList(strPath, null, null);
	}
	
	/**
	 * 获得找到的文件名
	 * @return
	 */
	public List<String> getFileList(){
		return filelist;
	}
	/**
	 * 递归找文件
	 * @param strPath
	 */
	private void findFileList(String strPath, List<String> fileSuffix, List<String> fileDirSuffix) {
		File strFile = new File(strPath);
		if(!strFile.exists()) {
			return;
		}
		if(strFile.isDirectory()) {
			String dirName = strFile.getName();
			if(fileDirSuffix!= null && fileDirSuffix.contains(dirName)) {
				filelist.add(strFile.getAbsolutePath());
			}
			for (File file : strFile.listFiles()) {
				findFileList(file.getAbsolutePath(), fileSuffix, fileDirSuffix);
			}
		} else if(strFile.isFile()){
			String fileName = strFile.getName();
			if(fileName.contains(".")) {
				String suffix = fileName.substring(fileName.lastIndexOf(".")); 
				if(fileSuffix!=null && fileSuffix.contains(suffix)) {
					filelist.add(strFile.getAbsolutePath());
				}else if(fileDirSuffix == null && fileSuffix == null) {
					filelist.add(strFile.getAbsolutePath());
				}
			}else {
				filelist.add(strFile.getAbsolutePath());
			}
		}
	}
}