package cn.com.tm.utils;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FileUtil {
	Logger logger = LoggerFactory.getLogger(FileUtil.class);

	public static void getFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 得到上传文件的保存目录，将上传的文件存放于WEB-INF目录下，不允许外界直接访问，保证上传文件的安全
		String savePath = "D:\\test";
		File file = new File(savePath);
		// 判断上传文件的保存目录是否存在
		if (!file.exists() && !file.isDirectory()) {
			System.out.println(savePath + "目录不存在，需要创建");
			// 创建目录
			file.mkdir();
		}
		// 消息提示
		String message = "";
		try {
			// 使用Apache文件上传组件处理文件上传步骤：
			// 1、创建一个DiskFileItemFactory工厂
			DiskFileItemFactory factory = new DiskFileItemFactory();
			// 2、创建一个文件上传解析器
			ServletFileUpload upload = new ServletFileUpload(factory);
			// 解决上传文件名的中文乱码
			upload.setHeaderEncoding("UTF-8");
			// 3、判断提交上来的数据是否是上传表单的数据
			if (!ServletFileUpload.isMultipartContent(request)) {
				// 按照传统方式获取数据
				System.out.println(ServletFileUpload.isMultipartContent(request));
				return;
			}
			// 4、使用ServletFileUpload解析器解析上传数据，解析结果返回的是一个List<FileItem>集合，每一个FileItem对应一个Form表单的输入项
			List<FileItem> list = upload.parseRequest(request);
			for (FileItem item : list) {
				// 如果fileitem中封装的是普通输入项的数据
				if (item.isFormField()) {
					String name = item.getFieldName();
					// 解决普通输入项的数据的中文乱码问题
					String value = item.getString("UTF-8");
					// value = new String(value.getBytes("iso8859-1"),"UTF-8");
					System.out.println(name + "=" + value);
				} else {// 如果fileitem中封装的是上传文件
					// 得到上传的文件名称，
					String filename = item.getName();
					System.out.println(filename);
					if (filename == null || filename.trim().equals("")) {
						continue;
					}
					// 注意：不同的浏览器提交的文件名是不一样的，有些浏览器提交上来的文件名是带有路径的，如：
					// c:\a\b\1.txt，而有些只是单纯的文件名，如：1.txt
					// 处理获取到的上传文件的文件名的路径部分，只保留文件名部分
					filename = filename.substring(filename.lastIndexOf("\\") + 1);
					// 获取item中的上传文件的输入流
					InputStream in = item.getInputStream();
					// 通过流的方式 放入ftp
					FTPManager.uploadByStreamUtils(in, filename);

					// 创建一个文件输出流
					FileOutputStream out = new FileOutputStream(savePath + "\\" + filename);
					// 创建一个缓冲区
					byte buffer[] = new byte[1024];
					// 判断输入流中的数据是否已经读完的标识
					int len = 0;
					// 循环将输入流读入到缓冲区当中，(len=in.read(buffer))>0就表示in里面还有数据
					while ((len = in.read(buffer)) > 0) {
						// 使用FileOutputStream输出流将缓冲区的数据写入到指定的目录(savePath + "\\"
						// + filename)当中
						out.write(buffer, 0, len);
					}
					// 关闭输入流
					in.close();
					// 关闭输出流
					out.close();
					// 删除处理文件上传时生成的临时文件
					item.delete();
					message = "文件上传成功！";
				}
			}
		} catch (Exception e) {
			message = "文件上传失败！";
			e.printStackTrace();

		}
	}

	/**
	 * @Title: uploadFile
	 * @Description:得到上传文件输入流
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @return Map   InputStream:输入流    filename:上传文件名    message:消息提示
	 * @throws
	 * @author yangdongxu
	 * @date 2016年9月9日 上午10:36:01
	 */
	public static Map uploadFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map map=new HashMap();
		boolean flag=true;
		// 消息提示
		String message = "";
		map.put("InputStream", null);
		map.put("filename", null);

		// 使用Apache文件上传组件处理文件上传步骤：
		// 1、创建一个DiskFileItemFactory工厂
		DiskFileItemFactory factory = new DiskFileItemFactory();
		// 2、创建一个文件上传解析器
		ServletFileUpload upload = new ServletFileUpload(factory);
		// 解决上传文件名的中文乱码
		upload.setHeaderEncoding("UTF-8");
		// 3、判断提交上来的数据是否是上传表单的数据
		if (!ServletFileUpload.isMultipartContent(request)) {
			// 按照传统方式获取数据
			// System.out.println(ServletFileUpload.isMultipartContent(request));
			message = "未包含上传文件";
		}
		// 4、使用ServletFileUpload解析器解析上传数据，解析结果返回的是一个List<FileItem>集合，每一个FileItem对应一个Form表单的输入项
		List<FileItem> list = upload.parseRequest(request);
		for (FileItem item : list) {
			// 如果fileitem中封装的是普通输入项的数据
			if (item.isFormField()) {
				String name = item.getFieldName();
				// 解决普通输入项的数据的中文乱码问题
				String value = item.getString("UTF-8");
				// value = new String(value.getBytes("iso8859-1"),"UTF-8");
				System.out.println(name + "=" + value);
			} else {// 如果fileitem中封装的是上传文件
				// 得到上传的文件名称，
				String filename = item.getName();
				if (filename == null || filename.trim().equals("")) {
					continue;
				}
				// 注意：不同的浏览器提交的文件名是不一样的，有些浏览器提交上来的文件名是带有路径的，如：
				// c:\a\b\1.txt，而有些只是单纯的文件名，如：1.txt
				// 处理获取到的上传文件的文件名的路径部分，只保留文件名部分
				filename = filename.substring(filename.lastIndexOf("\\") + 1);
				// 获取item中的上传文件的输入流
				InputStream in = item.getInputStream();
				// 通过流的方式 放入ftp
				//FTPManager.uploadByStreamUtils(in, filename);
				map.put("filename", filename);
				map.put("InputStream", in);
				// 删除处理文件上传时生成的临时文件
				item.delete();
				message = "文件上传成功！";
			}
		}

		map.put("message", message);
		return map;
	}


	/**
	 * @Title: FileUtil.java
	 * @Package cn.com.cvinfo.utils
	 * @Description:根据上传请求 得到参数Map
	 * @author 杨东旭
	 * @date 2016年10月6日 下午3:45:37
	 * @version V1.0
	 */
	public static Map getParamMap(HttpServletRequest request) throws Exception{
		Map paramsMap=new HashMap();
		// 使用Apache文件上传组件处理文件上传步骤：
		// 1、创建一个DiskFileItemFactory工厂
		DiskFileItemFactory factory = new DiskFileItemFactory();
		// 2、创建一个文件上传解析器
		ServletFileUpload upload = new ServletFileUpload(factory);
		// 解决上传文件名的中文乱码
		upload.setHeaderEncoding("UTF-8");
		List<FileItem> list = upload.parseRequest(request);

		Map parmaMap = new HashMap<>();
		for(FileItem fileItem:list) {
//		FileItem fileItem=list.get(0);
			if (fileItem.isFormField()) {
				String name = fileItem.getFieldName();
				// 解决普通输入项的数据的中文乱码问题
				String value = fileItem.getString("UTF-8");
				value = new String(value.getBytes("iso8859-1"),"UTF-8");

				parmaMap.put(name,value);
//			paramsMap=MapUtil.toHashMapObj(value);//json格式字符串 转成Map
			}
		}
		//取得参数
//		paramsMap.put("itemList", list);
		parmaMap.put("itemList",list);
		return parmaMap;
	}

	public static Map getParamMap4client(HttpServletRequest request) throws Exception{
		Map paramsMap=new HashMap();
		// 使用Apache文件上传组件处理文件上传步骤：
		// 1、创建一个DiskFileItemFactory工厂
		DiskFileItemFactory factory = new DiskFileItemFactory();
		// 2、创建一个文件上传解析器
		ServletFileUpload upload = new ServletFileUpload(factory);
		// 解决上传文件名的中文乱码
		upload.setHeaderEncoding("UTF-8");
		List<FileItem> list = upload.parseRequest(request);

		FileItem fileItem=list.get(0);
		if (fileItem.isFormField()) {
			String name = fileItem.getFieldName();
			// 解决普通输入项的数据的中文乱码问题
			String value = fileItem.getString("UTF-8");
//			 value = new String(value.getBytes("iso8859-1"),"UTF-8");

			//paramsMap=MapUtil.toHashMapObj4client(value);//json格式字符串 转成Map



		}

		//取得参数
		list.remove(0);
		paramsMap.put("itemList", list);
		return paramsMap;
	}


	/**
	 * @Title: uploadFile
	 * @Description:得到上传文件输入流
	 *
	 * @return
	 * @throws Exception
	 * @return Map   InputStream:输入流    filename:上传文件名    message:消息提示
	 * @throws
	 * @author yangdongxu
	 * @date 2016年9月9日 上午10:36:01
	 */
	public static List<Map> uploadFile(List<FileItem> list) throws Exception {
		List<Map> result=new ArrayList<Map>();
		try {
			// 使用ServletFileUpload解析器解析上传数据，解析结果返回的是一个List<FileItem>集合，每一个FileItem对应一个Form表单的输入项
			for (FileItem item : list) {
				Map map=new HashMap();
				// 如果fileitem中封装的是普通输入项的数据
				if (item.isFormField()) {
					String name = item.getFieldName();
					// 解决普通输入项的数据的中文乱码问题
					String value = item.getString("UTF-8");
					// value = new String(value.getBytes("iso8859-1"),"UTF-8");
					System.out.println(name + "=" + value);
				} else {// 如果fileitem中封装的是上传文件
					// 得到上传的文件名称，
					String filename = item.getName();
					if (filename == null || filename.trim().equals("")) {
						continue;
					}
					// 注意：不同的浏览器提交的文件名是不一样的，有些浏览器提交上来的文件名是带有路径的，如：
					// c:\a\b\1.txt，而有些只是单纯的文件名，如：1.txt
					// 处理获取到的上传文件的文件名的路径部分，只保留文件名部分
					//截取后缀
					String str=filename.substring(filename.lastIndexOf(".") + 1);
					filename = filename.substring(0,filename.lastIndexOf(".") );

					String realName=KeyUtil.getKeys()+"."+str;
					filename=filename+"."+str;
					map.put("filename", realName);
					item.setFieldName(realName);
					// 获取item中的上传文件的输入流
					InputStream in = item.getInputStream();

					// 通过流的方式 放入ftp
					FTPManager.uploadByStreamUtils(in, realName);
					map.put("realName", filename);
					result.add(map);
					// 删除处理文件上传时生成的临时文件
					item.delete();
				}
			}

		} catch (Exception e) {
			throw new Exception("上传异常");
		}
		return result;

	}

	public static List<Map> uploadFile4Finance(List<FileItem> list) throws Exception {
		List<Map> result=new ArrayList<Map>();
		try {
			// 使用ServletFileUpload解析器解析上传数据，解析结果返回的是一个List<FileItem>集合，每一个FileItem对应一个Form表单的输入项
			for (FileItem item : list) {
				Map map=new HashMap();
				// 如果fileitem中封装的是普通输入项的数据
				if (item.isFormField()) {
					String name = item.getFieldName();
					// 解决普通输入项的数据的中文乱码问题
					String value = item.getString("UTF-8");
					// value = new String(value.getBytes("iso8859-1"),"UTF-8");
					System.out.println(name + "=" + value);
				} else {// 如果fileitem中封装的是上传文件
					// 得到上传的文件名称，
					String filename = item.getName();
					if (filename == null || filename.trim().equals("")) {
						continue;
					}
					// 注意：不同的浏览器提交的文件名是不一样的，有些浏览器提交上来的文件名是带有路径的，如：
					// c:\a\b\1.txt，而有些只是单纯的文件名，如：1.txt
					// 处理获取到的上传文件的文件名的路径部分，只保留文件名部分
					//截取后缀
					String str=filename.substring(filename.lastIndexOf(".") + 1);
					filename = filename.substring(0,filename.lastIndexOf(".") );

					String realName=KeyUtil.getKeys()+"."+str;
					filename=filename+"."+str;
					map.put("filename", realName);
					item.setFieldName(realName);
					// 获取item中的上传文件的输入流
					InputStream in = item.getInputStream();

					// 通过流的方式 放入ftp
					FTPManager.uploadByStreamUtils(in, realName);
					map.put("realName", filename);
					result.add(map);
				}
			}

		} catch (Exception e) {
			throw new Exception("上传异常");
		}
		return result;

	}



}
