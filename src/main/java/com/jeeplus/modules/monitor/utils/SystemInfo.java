package com.jeeplus.modules.monitor.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;


import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.ObjectName;
import javax.management.ReflectionException;

public class SystemInfo {
	public static Map SystemProperty() {
		Map<String, Comparable> monitorMap = new HashMap();
		Runtime r = Runtime.getRuntime();
		Properties props = System.getProperties();
		InetAddress addr = null;
		String ip = "";
		String hostName = "";
		try {
			addr = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			ip = "无法获取主机IP";
			hostName = "无法获取主机名";
		}
		if (null != addr) {
			try {
				ip = addr.getHostAddress();
			} catch (Exception e) {
				ip = "无法获取主机IP";
			}
			try {
				hostName = addr.getHostName();
			} catch (Exception e) {
				hostName = "无法获取主机名";
			}
		}
		monitorMap.put("hostIp", ip);// 本地ip地址
		monitorMap.put("hostName", hostName);// 本地主机名
		monitorMap.put("osName", props.getProperty("os.name"));// 操作系统的名称
		monitorMap.put("arch", props.getProperty("os.arch"));// 操作系统的构架
		monitorMap.put("osVersion", props.getProperty("os.version"));// 操作系统的版本
		monitorMap.put("processors", r.availableProcessors());// JVM可以使用的处理器个数
		monitorMap.put("javaVersion", props.getProperty("java.version"));// Java的运行环境版本
		monitorMap.put("vendor", props.getProperty("java.vendor"));// Java的运行环境供应商
		monitorMap.put("javaUrl", props.getProperty("java.vendor.url"));// Java供应商的URL
		monitorMap.put("javaHome", props.getProperty("java.home"));// Java的安装路径
		monitorMap.put("tmpdir", props.getProperty("java.io.tmpdir"));// 默认的临时文件路径
		return monitorMap;
	}

	/**
	 * 获取本地IP地址
	 * @return
	 * @throws UnknownHostException
	 */
	public static String getHostAddress() throws UnknownHostException {
		Enumeration<NetworkInterface> netInterfaces = null;
		try {
			netInterfaces = NetworkInterface.getNetworkInterfaces();
			while (netInterfaces.hasMoreElements()) {
				NetworkInterface ni = netInterfaces.nextElement();
				Enumeration<InetAddress> ips = ni.getInetAddresses();
				while (ips.hasMoreElements()) {
					InetAddress ip = ips.nextElement();
					if (ip.isSiteLocalAddress()) {
						return ip.getHostAddress();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return InetAddress.getLocalHost().getHostAddress();
	}


	/**
	 * 获取服务端口号
	 * @return 端口号
	 * @throws ReflectionException
	 * @throws MBeanException
	 * @throws InstanceNotFoundException
	 * @throws AttributeNotFoundException
	 */
	public static String getServerPort(boolean secure) throws AttributeNotFoundException, InstanceNotFoundException, MBeanException, ReflectionException {
		MBeanServer mBeanServer = null;
		if (MBeanServerFactory.findMBeanServer(null).size() > 0) {
			mBeanServer = (MBeanServer)MBeanServerFactory.findMBeanServer(null).get(0);
		}

		if (mBeanServer == null) {
//			log.debug("调用findMBeanServer查询到的结果为null");
			return "";
		}

		Set<ObjectName> names = null;
		try {
			names = mBeanServer.queryNames(new ObjectName("Catalina:type=Connector,*"), null);
		} catch (Exception e) {
			return "";
		}
		Iterator<ObjectName> it = names.iterator();
		ObjectName oname = null;
		while (it.hasNext()) {
			oname = (ObjectName)it.next();
			String protocol = (String)mBeanServer.getAttribute(oname, "protocol");
			String scheme = (String)mBeanServer.getAttribute(oname, "scheme");
			Boolean secureValue = (Boolean)mBeanServer.getAttribute(oname, "secure");
			Boolean SSLEnabled = (Boolean)mBeanServer.getAttribute(oname, "SSLEnabled");
			if (SSLEnabled != null && SSLEnabled) {// tomcat6开始用SSLEnabled
				secureValue = true;// SSLEnabled=true但secure未配置的情况
				scheme = "https";
			}
			if (protocol != null && ("HTTP/1.1".equals(protocol) || protocol.contains("http"))) {
				if (secure && "https".equals(scheme) && secureValue) {
					return ((Integer)mBeanServer.getAttribute(oname, "port")).toString();
				} else if (!secure && !"https".equals(scheme) && !secureValue) {
					return ((Integer)mBeanServer.getAttribute(oname, "port")).toString();
				}
			}
		}
		return "";
	}
}
