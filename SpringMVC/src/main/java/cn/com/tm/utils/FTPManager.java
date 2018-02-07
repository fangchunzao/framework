package cn.com.tm.utils;

import org.apache.commons.net.ftp.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.filechooser.FileSystemView;
import java.io.*;
import java.net.SocketException;
import org.apache.commons.net.ftp.FTPClientConfig;


public class FTPManager {
	
	private static final String IP="10.10.0.25";
	private static final int PORT=8200;
	private static final String USERNAME="test1";
	private static final String PASSWORD="helloword";
	
	public static String getIp() {
		return IP;
	}
	public static int getPort() {
		return PORT;
	}
	public static String getUsername() {
		return USERNAME;
	}
	public static String getPassword() {
		return PASSWORD;
	}



	Logger logger = LoggerFactory.getLogger(FTPManager.class);

	private static FTPClient ftpClient = null;
	private String path = "";

	/**
	 * 获取FTPClient对象
	 * 
	 * @param ftpHost
	 *            FTP主机服务器
	 * @param ftpPassword
	 *            FTP 登录密码
	 * @param ftpUserName
	 *            FTP登录用户名
	 *            FTP端口 默认为21
	 * @return
	 */
	public FTPClient getFTPClient(String ftpHost, String ftpUserName, String ftpPassword, int port) {
		try {
			ftpClient = new FTPClient();

			ftpClient.setDefaultPort(port); // 设置默认端口
			ftpClient.configure(getFtpConfig());
			ftpClient.connect(ftpHost);// 连接FTP服务器

			ftpClient.enterLocalPassiveMode();
			// ftpClient.setConnectTimeout(60000);
			boolean isSuccess = ftpClient.login(ftpUserName, ftpPassword);// 登录FTP服务器
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
			if (isSuccess) {
				logger.info("FTP登录成功。");
			}
			if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
				logger.info("未连接到FTP，用户名或密码错误。");
				ftpClient.disconnect();
			} else {
				logger.info("FTP连接成功。");
			}
		} catch (SocketException e) {
			logger.info("FTP的IP地址可能错误，请正确配置。");
			e.printStackTrace();
		} catch (IOException e) {
			logger.info("FTP的端口错误,请正确配置。");
			e.printStackTrace();
		}
		return ftpClient;
	}

	// 设置FTP客服端的配置--一般可以不设置
	private FTPClientConfig getFtpConfig() {
		FTPClientConfig ftpConfig = new FTPClientConfig(FTPClientConfig.SYST_UNIX);
		ftpConfig.setServerLanguageCode(FTP.DEFAULT_CONTROL_ENCODING);
		return ftpConfig;
	}

	public static void recursion(String root) {
		File file = new File(root);
		File[] subFile = file.listFiles();
		for (int i = 0; i < subFile.length; i++) {
			if (subFile[i].isDirectory()) {
				System.out.println("目录: " + subFile[i].getName());
				recursion(subFile[i].getAbsolutePath());
			} else {
				System.out.println("文件: " + subFile[i].getName());
			}
		}
	}

	/**
	 * 列出Ftp服务器上的所有文件和目录
	 */
	public void listRemoteAllFiles(String remotePath) {
		FTPManager util = new FTPManager();
		ftpClient = util.getFTPClient(IP,USERNAME,PASSWORD,PORT);
		try {
			String[] names = ftpClient.listNames();
			System.out.println(names[0]);
			FTPFile[] files = ftpClient.listFiles(remotePath);
			for (int i = 0; i < files.length; i++) {
				if (files[i].isFile()) {
					System.out.println(files[i].getName());
				} else if (files[i].isDirectory()) {
					System.out.println("目录" + files[i].getName());
					listRemoteAllFiles(remotePath + files[i].getName() + "/");
					System.out.println("遍历结束");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean uploadFile(String localFilePath, String remoteFileName) throws IOException {
		boolean flag = false;
		InputStream iStream = null;
		try {
			iStream = new FileInputStream(localFilePath);
			// 我们可以使用BufferedInputStream进行封装
			// BufferedInputStream bis=new BufferedInputStream(iStream);
			// flag = ftpClient.storeFile(remoteFileName, bis);
			flag = ftpClient.storeFile(remoteFileName, iStream);
		} catch (IOException e) {
			flag = false;
			return flag;
		} finally {
			if (iStream != null) {
				iStream.close();
			}
		}
		return flag;
	}
	
	
	/**
	* @Title: uploadFileByStream
	* @Description: 通过流的方式上传文件到ftp
	* @param iStream
	* @param remoteFileName
	* @return
	* @throws IOException   
	* @return boolean   
	* @throws
	* @author yangdongxu
	* @date 2016年9月9日 上午9:26:50
	*/ 
	public boolean uploadFileByStream(InputStream iStream, String remoteFileName) throws IOException {
		boolean flag = false;
		try {
			// 我们可以使用BufferedInputStream进行封装
			// BufferedInputStream bis=new BufferedInputStream(iStream);
			// flag = ftpClient.storeFile(remoteFileName, bis);
			flag = ftpClient.storeFile(remoteFileName, iStream);
		} catch (IOException e) {
			flag = false;
			return flag;
		} finally {
			if (iStream != null) {
				iStream.close();
			}
		}
		return flag;
	}

	/**
	 * @Title: download
	 * @Description: 下载
	 * @param remoteFileName
	 *            源文件名
	 * @param localFileName
	 *            目标文件名
	 * @return
	 * @throws IOException
	 * @return boolean
	 * @throws @author
	 *             yangdongxu
	 * @date 2016年9月8日 下午1:17:25
	 */
	public boolean download(String remoteFileName, String localFileName) throws IOException {
		boolean flag = false;
		File outfile = new File(localFileName);
		OutputStream oStream = null;
		try {
			String[] names = ftpClient.listNames();
			for(String str:names){
				System.out.println();
			}
			oStream = new FileOutputStream(outfile);
			// 我们可以使用BufferedOutputStream进行封装
			// BufferedOutputStream bos=new BufferedOutputStream(oStream);
			// flag = ftpClient.retrieveFile(remoteFileName, bos);
			flag = ftpClient.retrieveFile(remoteFileName, oStream);
		} catch (IOException e) {
			flag = false;
			e.printStackTrace();
			return flag;
		} finally {
			oStream.close();
		}
		return flag;
	}
	
	
	public static void downFile(String remoteFileName, OutputStream oStream) throws IOException {
		FTPManager util = new FTPManager();
		ftpClient = util.getFTPClient(IP,USERNAME,PASSWORD,PORT);// 21是端口，根据自己情况而定
		try {
			String[] names = ftpClient.listNames();
			for(String str:names){
				System.out.println();
			}
			// 我们可以使用BufferedOutputStream进行封装
			// BufferedOutputStream bos=new BufferedOutputStream(oStream);
			// flag = ftpClient.retrieveFile(remoteFileName, bos);
			 ftpClient.retrieveFile(remoteFileName, oStream);
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	
	/**
	 * @comment: 这个不好用
	 * @history: 2016年9月18日 任伟杰
	 * @param pathname  FTP服务器文件目录
	 * @param filename  文件名称
	 * @param localpath 下载后的文件路径
	 * @return
	 */
	public static boolean downloadFile(String pathname, String filename, String localpath){
	    boolean flag = false;
	    try {
	    	FTPManager util = new FTPManager();
		      ftpClient = util.getFTPClient(IP,USERNAME,PASSWORD,PORT);// 21是端口，根据自己情况而定
		      int replyCode = ftpClient.getReplyCode();
		      if(!FTPReply.isPositiveCompletion(replyCode)){
		        return flag;
		      }
		      //切换FTP目录
		      ftpClient.changeWorkingDirectory(pathname);
		      ftpClient.enterLocalPassiveMode();   
	          //由于apache不支持中文语言环境，通过定制类解析中文日期类型  
		      // ftpClient.configure(new FTPClientConfig("cn.com.clps.entity.UnixFTPEntryParser"));
		      FTPFile[] ftpFiles = ftpClient.listFiles();
		      System.out.println(ftpFiles.length);
		      for(FTPFile file : ftpFiles){
		        if(filename.equalsIgnoreCase(file.getName())){
		          File localFile = new File(localpath + "/" + file.getName());
		          OutputStream os = new FileOutputStream(localFile);
		          ftpClient.retrieveFile(file.getName(), os);
		          os.close();
		        }
		      }
		      ftpClient.logout();
		      flag = true;
	    } catch (Exception e) {
	      e.printStackTrace();
	    } finally{
	      if(ftpClient.isConnected()){
	        try {
	          ftpClient.logout();
	        } catch (IOException e) {
	          
	        }
	      }
	    }
	    return flag;
	  }
	
	
	/**
	 * @comment: 下载文件,非常好用
	 * @history: 2016年9月19日 任伟杰
	 * @param pathname	服务器目录
	 * @param filename	下载的文件名
	 * @return
	 */
	public static boolean downloadFiles(String pathname, String filename){
		boolean flag = false;
		FTPClient ftpClient = new FTPClient();
		try {
			//创建连接
			ftpClient.connect(IP, PORT);
			//进行登录
			ftpClient.login(USERNAME, PASSWORD);
			//设置字符集,解决乱码问题
			ftpClient.setControlEncoding("gb2312");
			//登录后设置文件类型为二进制否则可能导致乱码文件无法打开
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
			//找到该目录
			FTPFile[] ftpFiles = ftpClient.listFiles(pathname);
			//获取桌面的路径
			String path = FileSystemView.getFileSystemView().getHomeDirectory()
					.getAbsolutePath();
			//遍历文件夹文件
			for (FTPFile file : ftpFiles) {
				if (filename.equalsIgnoreCase(file.getName())) {
					//获取下载地址
					File localFile = new File(path + "/" + file.getName());
					//输出结果集
					OutputStream os = new FileOutputStream(localFile);
					ftpClient.retrieveFile(file.getName(), os);
					//关闭流
					os.close();
					break;
				}
			}
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
		} finally {
			if (ftpClient.isConnected()) {
				try {
					ftpClient.logout();
				} catch (IOException e) {
					e.printStackTrace();
					flag = false;
				}
			}
		}
		return flag;
	}
	  


	
	
	/**
	* @Title: downLoadUtils
	* @Description: 下载工具
	* @param remoteFileName  源文件路径
	* @param localFileName	 目标文件路径
	* @return
	* @throws Exception   
	* @return boolean   
	* @throws
	* @author yangdongxu
	* @date 2016年9月8日 下午2:39:16
	*/ 
	public static boolean downLoadUtils(String remoteFileName, String localFileName) throws Exception{
		FTPManager util = new FTPManager();
		ftpClient = util.getFTPClient(IP,USERNAME,PASSWORD,PORT);// 21是端口，根据自己情况而定
		boolean flag = util.download(remoteFileName, localFileName);
		return flag;
	}
	
	
	
	/**
	* @Title: uploadUtils
	* @Description: 上传工具
	* @param localFilePath  源文件路径
	* @param remoteFileName	上传文件路径
	* @return
	* @throws Exception   
	* @return boolean   
	* @throws
	* @author yangdongxu
	* @date 2016年9月8日 下午2:39:20
	*/ 
	public static boolean uploadUtils(String localFilePath, String remoteFileName) throws Exception{
		FTPManager util = new FTPManager();
		boolean flag=false;
		ftpClient = util.getFTPClient(IP,USERNAME,PASSWORD,PORT);// 21是端口，根据自己情况而定
		flag=util.uploadFile(localFilePath,remoteFileName);
		return flag;
	}
	
	/**
	* @Title: uploadByStreamUtils
	* @Description:通过流的方式 上传文件到ftp
	* @param remoteFileName
	* @return
	* @throws Exception   
	* @return boolean   
	* @throws
	* @author yangdongxu
	* @date 2016年9月9日 上午9:27:44
	*/ 
	public static boolean uploadByStreamUtils(InputStream iStream, String remoteFileName) throws Exception{
		FTPManager util = new FTPManager();
		boolean flag=false;
		ftpClient = util.getFTPClient(IP,USERNAME,PASSWORD,PORT);//
		ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
		//FileInputStream iStream = new FileInputStream(localFilePath);
		flag=util.uploadFileByStream(iStream,remoteFileName);
		return flag;
	}
	

/*	public void test() {
		FTPManager util = new FTPManager();
		ftpClient = util.getFTPClient(IP,USERNAME,PASSWORD,PORT);// 21是端口，根据自己情况而定
		try {
			String[] names = ftpClient.listNames();
			for (String str : names) {
				System.out.println(str);
			}
			// boolean s=util.uploadFile("D:\\test2.pdf", "test2.pdf");
			boolean s = util.download("test2.pdf", "E:\\test7872.pdf");
			System.out.println(s);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}*/
	
	public static void main(String args[]) {

		try {
			//boolean flag=downLoadUtils("test2.pdf","E://test222.pdf");
			//boolean flag=uploadUtils("E://test222.pdf","test2222.pdf ");
			// boolean flag=downloadFiles("E://","cc7d37f9f4f44e2991a9371634519500.txt");
			boolean flag=downLoadUtils("cc7d37f9f4f44e2991a9371634519500.txt", "E://cc7d37f9f4f44e2991a9371634519500.txt");
			//flag=downloadFiles(File.separator,"cc7d37f9f4f44e2991a9371634519500.txt");
			System.out.println(flag);
		} catch (Exception e) {
			e.printStackTrace();
		}

	
	}
	

}