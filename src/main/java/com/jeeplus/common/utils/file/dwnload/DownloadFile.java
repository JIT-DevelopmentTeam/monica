package com.jeeplus.common.utils.file.dwnload;

import javax.net.ssl.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.UUID;

/**
 *
 * @author
 * @version
 */
public class DownloadFile {
    /**
     * @MethodName httpConverBytes
     * @Description http路径文件内容获取
     *
     * @param path
     * @return
     */
    public static byte[] httpConverBytes(String path) {
        BufferedInputStream in = null;
        ByteArrayOutputStream out = null;
        URLConnection conn = null;

        try {
            URL url = new URL(path);
            conn = url.openConnection();
            in = new BufferedInputStream(conn.getInputStream());
            out = new ByteArrayOutputStream(1024);
            byte[] temp = new byte[1024];
            int size = 0;
            while ((size = in.read(temp)) != -1) {
                out.write(temp, 0, size);
            }
            byte[] content = out.toByteArray();
            return content;
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * @MethodName httpsConverBytes
     * @Description https路径文件内容获取
     *
     * @param url
     * @return
     */
    public static byte[] httpsConverBytes(String url) {
        BufferedInputStream inStream = null;
        ByteArrayOutputStream outStream = null;

        try {

            TrustManager[] tm = { new TrustAnyTrustManager() };
            SSLContext sc = SSLContext.getInstance("SSL", "SunJSSE");
            sc.init(null, tm, new java.security.SecureRandom());
            URL console = new URL(url);

            HttpsURLConnection conn = (HttpsURLConnection) console.openConnection();
            conn.setSSLSocketFactory(sc.getSocketFactory());
            conn.setHostnameVerifier(new TrustAnyHostnameVerifier());
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");
            conn.connect();

            inStream = new BufferedInputStream(conn.getInputStream());
            outStream = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = inStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, len);
            }

            byte[] content = outStream.toByteArray();
            return content;

        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (null != inStream) {
                try {
                    inStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (null != outStream) {
                try {
                    outStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }
    private static class TrustAnyTrustManager implements X509TrustManager {
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {}

        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {}

        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[] {};
        }
    }

    private static class TrustAnyHostnameVerifier implements HostnameVerifier {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }

    public static String getExtensionName(String filename) {
        if (filename != null && filename.length() > 0) {
            int dot = filename.lastIndexOf('.');
            if (dot > -1 && dot < filename.length() - 1) {
                return filename.substring(dot + 1);
            }
        }
        return filename;
    }

    public static void downloadFile(String fileUrl,String newFileName,HttpServletResponse response, HttpServletRequest request){
        String path = request.getContextPath();
        String filePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
        if (null == filePath || filePath.trim().length() == 0 || !filePath.startsWith("http")) {
            return;
        }
        String url =filePath + fileUrl;  //网络下载url地址
        if (null == fileUrl || fileUrl.trim().length() == 0) {
            fileUrl =  UUID.randomUUID().toString() +newFileName+"." +getExtensionName(fileUrl);
        }else{
            fileUrl =  newFileName + "." + getExtensionName(fileUrl);
        }
        BufferedOutputStream bf = null;
        try {
            fileUrl = URLEncoder.encode(fileUrl, "UTF-8");
            response.setHeader("Content-disposition", "attachment; filename*=utf-8'zh_cn'" + fileUrl);
            bf = new BufferedOutputStream(response.getOutputStream());
            if (filePath.startsWith("http://")) {
                bf.write(httpConverBytes(url));
            } else if (filePath.startsWith("https://")) {
                bf.write(httpsConverBytes(url));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (bf != null) {
                try {
                    bf.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    bf.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}