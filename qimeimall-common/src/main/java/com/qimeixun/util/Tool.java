package com.qimeixun.util;

import cn.hutool.core.lang.ObjectId;
import cn.hutool.core.util.IdcardUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Source;
import javax.xml.transform.sax.SAXSource;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.StringReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @desc 工具类
 * @author yueyufan
 * @date 2019年11月6日 下午4:55:42
 */
@Slf4j
public class Tool {

	@Value("${md5.key}")
	static String md5_key;

	/**
	 * @desc 根据委托方获取配货单号
	 * @author yueyufan
	 * @date 2020年1月7日 下午4:23:14
	 * @param entrustName
	 * @return
	 */
	public static String getDistributionNumber(String entrustName) {
		String returnStr = "";
		// A代表马上项目的，B代表即有的，再有其它项目就用C、D往后排
		if (!isBlank(entrustName)) {
			String entrustType = "";
			if (entrustName.contains("马上")) {
				entrustType = "A";
			} else if (entrustName.contains("即有")) {
				entrustType = "B";
			}
			try {
				Thread.sleep(1);// 停顿一毫秒
			} catch (InterruptedException e) {
				log.error("getDistributionNumber获取配货单号——发生异常：{}", e);
			}
			String currentTimeString = DateUtil.parseDateToStr(new Date(), DateUtil.DATE_TIME_FORMAT_YYYYMMDDHHMISSSSS);
			returnStr = entrustType + currentTimeString;
		}
		return returnStr;
	}

	/**
	 * @desc 生成指定位数的随机数
	 * @author yueyufan
	 * @date 2020年1月7日 下午4:50:22
	 * @param length
	 * @return
	 */
	public static String getRandom(int length) {
		StringBuffer val = new StringBuffer("");
		Random random = new Random();
		for (int i = 0; i < length; i++) {
			val.append(String.valueOf(random.nextInt(10)));
		}
		return val.toString();
	}

	/**
	 * @desc 两数相除得到百分比（保留两位小数四舍五入）
	 * @author yueyufan
	 * @date 2019年12月24日 下午4:30:07
	 * @param a
	 * @param b
	 * @return String 结果百分比
	 */
	public static String intDivision(int a, int b) {
		if (b == 0) {
			return "0.00%";
		}
		DecimalFormat df = new DecimalFormat("0.0000");// 设置保留位数
		df.setRoundingMode(RoundingMode.HALF_EVEN);
		float result = (float) a / b;
		BigDecimal bd = new BigDecimal(df.format(result));
		bd = bd.multiply(new BigDecimal("100")).setScale(2);
		return bd.toString() + "%";
	}

	/**
	 * @desc 拆分地址
	 * @author yueyufan
	 * @date 2019年12月11日 下午3:08:32
	 * @param address
	 * @return
	 */
	public static Map<String, String> spiltAddress(String address) {
		Map<String, String> returnMap = null;
		try {
			if (!isBlank(address) && address.contains(" ")) {
				returnMap = new HashMap<String, String>();
				String[] addressArray = address.split(" ");
				returnMap.put("province", addressArray[0]);// 省份
				returnMap.put("city", addressArray[1]);// 市
				returnMap.put("area", addressArray[2]);// 县/区
				returnMap.put("other", addressArray[3]);// 详细地址
			}
		} catch (Exception e) {
			return returnMap;
		}
		return returnMap;
	}

	/**
	 * @desc 获取当前年月字符串
	 * @author yueyufan
	 * @date 2019年12月3日 上午9:19:59
	 * @return
	 */
	public static String getCurrentYearMonth() {
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
		return String.valueOf(year) + String.valueOf(month);
	}

	/**
	 * @desc 将文件转换成字节
	 * @author yueyufan
	 * @date 2019年12月2日 下午6:38:29
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public static byte[] fileToByte(File file) {
		byte[] data = null;
		try {
			FileInputStream fis = new FileInputStream(file);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			int len;
			byte[] buffer = new byte[1024];
			while ((len = fis.read(buffer)) != -1) {
				baos.write(buffer, 0, len);
			}
			data = baos.toByteArray();
			fis.close();
			baos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}

	/**
	 * @desc 将XML内容转换成JavaBean对象
	 * @author yueyufan
	 * @date 2019年11月25日 下午5:11:32
	 * @param xml
	 * @param t
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T convertXmlToJavaBean(String xml, Class<T> t) {
		T obj = null;
		try {
			JAXBContext context = JAXBContext.newInstance(t);
			StringReader stringReader = new StringReader(xml);
			SAXParserFactory sax = SAXParserFactory.newInstance();
			sax.setNamespaceAware(false);
			XMLReader xmlReader = sax.newSAXParser().getXMLReader();
			Source source = new SAXSource(xmlReader, new InputSource(stringReader));
			Unmarshaller unmarshaller = context.createUnmarshaller();
			obj = (T) unmarshaller.unmarshal(source);
		} catch (JAXBException e) {
			log.error("xml转换成JavaBean------异常JAXBException：{}", e);
		} catch (SAXException e) {
			log.error("xml转换成JavaBean------异常SAXException：{}", e);
		} catch (ParserConfigurationException e) {
			log.error("xml转换成JavaBean------异常ParserConfigurationException：{}", e);
		}
		return obj;
	}

	/**
	 * @desc 根据逗号(英文状态)将字符串拆分成List
	 * @author yueyufan
	 * @date 2019年11月22日 上午11:06:42
	 * @return
	 */
	public static List<String> spiltByComma(String str) {
		List<String> result = Arrays.asList(str.split(","));
		return result;
	}

	/**
	 * @desc 根据逗号(英文状态)将字符串拆分成数组
	 * @author yueyufan
	 * @date 2019年11月22日 上午11:06:42
	 * @return
	 */
	public static String[] spiltByCommaToArray(String str) {
		return str.split(",");
	}

	/**
	 * @desc 获取当前日期时间戳
	 * @author yueyufan
	 * @date 2019年11月20日 下午5:43:56
	 * @return String yyyyMMddHHmmss
	 */
	public static String getCurrentTimeString() {
		return DateUtil.parseDateToStr(new Date(), DateUtil.DATE_TIME_FORMAT_YYYYMMDDHHMISS);
	}

	/**
	 * @desc 根据身份证号获取性别、出生日期、年龄、省份等信息
	 * @author yueyufan
	 * @date 2019年11月19日 下午3:26:43
	 * @param idCard
	 * @return Map<String, Object>
	 */
	public static Map<String, Object> getInfoByIdCard(String idCard) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (isBlank(idCard) || !IdcardUtil.isValidCard(idCard)) {
			return map;
		}
		int sex_code = IdcardUtil.getGenderByIdCard(idCard);// (1 : 男 ， 0 : 女)
		map.put("sex", sex_code == 0 ? "1" : "0");// 性别（0：男；1：女；2：保密）
		map.put("birthday", IdcardUtil.getYearByIdCard(idCard) + "-" + IdcardUtil.getMonthByIdCard(idCard) + "-" + IdcardUtil.getDayByIdCard(idCard));
		map.put("age", IdcardUtil.getAgeByIdCard(idCard));
		map.put("province", IdcardUtil.getProvinceByIdCard(idCard));
		return map;
	}

	/**
	 * @desc 判断终端是否为移动终端
	 * @author yueyufan
	 * @date 2019年11月19日 上午9:26:48
	 * @param request
	 * @return String 是否是移动终端访问（0：非移动端；1：是移动终端）
	 */
	public static String getIsMobile(HttpServletRequest request) {
		String isMobile = "0";
		try {
			// 获取浏览器信息UA
			String uaStr = request.getHeader("User-Agent");
			UserAgent ua = UserAgentUtil.parse(uaStr);
			boolean isMobileString = ua.isMobile();
			isMobile = isMobileString ? "1" : "0";
		} catch (Exception e) {
			e.printStackTrace();
			log.error("获取是否是移动终端信息异常：{}", e);
		}
		return isMobile;
	}

	/**
	 * @desc 获取客户端系统信息
	 * @author yueyufan
	 * @date 2019年11月19日 上午9:26:48
	 * @param request
	 * @return String
	 */
	public static String getSystemInfo(HttpServletRequest request) {
		String system = "";
		try {
			// 获取浏览器信息UA
			String uaStr = request.getHeader("User-Agent");
			UserAgent ua = UserAgentUtil.parse(uaStr);
			system = ua.getOs().toString();// Windows 7
		} catch (Exception e) {
			e.printStackTrace();
			log.error("获取客户端系统信息异常：{}", e);
		}
		return system;
	}

	/**
	 * @desc 获取客户端浏览器信息
	 * @author yueyufan
	 * @date 2019年11月19日 上午9:26:30
	 * @param request
	 * @return String
	 */
	public static String getBrowserInfo(HttpServletRequest request) {
		String browser = "";
		String version = "";
		try {
			// 获取浏览器信息UA
			String uaStr = request.getHeader("User-Agent");
			UserAgent ua = UserAgentUtil.parse(uaStr);
			browser = ua.getBrowser().toString();// Chrome
			version = ua.getVersion();// 14.0.835.163
			browser = browser + " " + version;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("获取客户端浏览器信息异常：{}", e);
		}
		return browser;
	}

	/**
	 * 替换掉HTML标签方法
	 */
	public static String replaceHtml(String html) {
		if (isBlank(html)) {
			return "";
		}
		String regEx = "<.+?>";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(html);
		String s = m.replaceAll("");
		return s;
	}

	/**
	 * @desc 将明文密码进行加密，并返回
	 * @author yueyufan
	 * @date 2019年11月15日 上午9:35:52
	 * @param password
	 * @return
	 */
	public static String passwordMd5(String password) {
		return MD5Util.md5(password, md5_key);
	}

	/**
	 * @desc 获取32位长度的UUID
	 * @author yueyufan
	 * @date 2019年11月14日 下午4:48:25
	 * @return
	 */
	public static String createUUID32() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

	/**
	 * @desc 文件命，统一调用此方法获取文件名
	 * @author yueyufan
	 * @date 2019年11月13日 下午2:21:00
	 * @return String
	 */
	public static String getFileName() {
		String current_time = DateUtil.parseDateToStr(new Date(), DateUtil.DATE_TIME_FORMAT_YYYYMMDDHHMISS);
		String random_str = RandomUtil.randomNumbers(11);
		return current_time + random_str;
	}

	/**
	 * @desc 将字符串格式的整数转换成int型
	 * @author yueyufan
	 * @date 2019年11月11日 下午4:23:42
	 * @param str
	 * @return
	 */
	public static int getIntByStr(String str) {
		if (!isBlank(str)) {
			return Integer.valueOf(str);
		} else {
			return 0;
		}
	}

	/**
	 * @desc 将字符串格式的整数转换成int型(适用于excel未文本格式化)
	 * @author zhukunpeng
	 * @date 2019年11月11日 下午4:23:42
	 * @param str
	 * @return
	 */
	public static int getIntByExcelStr(String str) {
		if (!isBlank(str)) {
			return Double.valueOf(str).intValue();
		} else {
			return 0;
		}
	}

	/**
	 * @desc 将字符串格式的数值转换成BigDecimal格式数值
	 * @author yueyufan
	 * @date 2019年11月11日 下午4:18:01
	 * @param str
	 * @return
	 */
	public static BigDecimal getBigDecimalByStr(String str) {
		if (!isBlank(str)) {
			BigDecimal bd = new BigDecimal(str);
			// 设置小数位数，第一个变量是小数位数，第二个变量是取舍方法(四舍五入)
			bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
			return bd;
		} else {
			return new BigDecimal("0.00");
		}
	}

	/**
	 * @desc 判断字符串是否为空
	 * @author yueyufan
	 * @date 2019年11月7日 下午6:24:48
	 * @param str
	 * @return boolean (true:空;false:不为空)
	 */
	public static boolean isBlank(String str) {
		return StringUtils.isBlank(str);
	}

	/**
	 * @desc 去掉字符串中的双引号
	 * @author yueyufan
	 * @date 2019年11月7日 下午6:23:13
	 * @param str
	 * @return
	 */
	public static String replaceMarks(String str) {
		if (!Tool.isBlank(str)) {
			return str.replace("\"", "");
		}
		return "";
	}

	/**
	 * @desc 获取主键ID，与业务无关
	 * @author yueyufan
	 * @date 2019年11月6日 下午4:55:29
	 * @return
	 */
	public static String getPrimaryKey() {
		return ObjectId.next();
	}

}
