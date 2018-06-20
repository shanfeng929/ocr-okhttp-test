package com.test.test;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) throws Exception {

		// test01 正面（手机拍照-赖山锋）
		String front = "WechatIMG3.jpeg";
		String result = App.test(front);
		System.out.println(result);

		// test02 反面（手机拍照-赖山锋）
		String back = "WechatIMG4.jpeg";
		result = App.test(back);
		System.out.println(result);

		// test03 正反面（百度图片-郑燕彬）
		String frontAndBack = "timg.jpeg";
		result = App.test(frontAndBack);
		System.out.println(result);

	}

	public static String test(String file) throws Exception {
		// img
		URL imgUrl = App.class.getClassLoader().getResource(file);
		String imgPath = imgUrl.getPath();
		String imgEncode = encryptToBase64(imgPath);

		// url
		String url = "http://bdp.data.api.autohome.com.cn/ai/cv/ocr/idcard_recog";

		// json body
		Map<String, String> params = new HashMap<String, String>();
		params.put("image_base64", imgEncode);
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(params);

		// header
		Map<String, String> headerMap = new HashMap<String, String>();
		headerMap.put("Content-Type", "application/json");
		headerMap.put("APPKEY", "09BD349B353A8D9EFA833D05E98E3B4A");

		// okhttp
		MediaType JSON = MediaType.parse("application/json; charset=utf-8");
		OkHttpClient client = new OkHttpClient();

		RequestBody body = RequestBody.create(JSON, json);
		Headers headers = Headers.of(headerMap);
		Request request = new Request.Builder().url(url).headers(headers).post(body).build();
		Response response = client.newCall(request).execute();

		// response
		String result = "";
		if (response.isSuccessful()) {
			result = response.body().string();
		} else {
			throw new IOException("Unexpected code " + response);
		}

		return result;
	}

	public static String encryptToBase64(String filePath) {
		if (filePath == null) {
			return null;
		}
		try {
			byte[] b = Files.readAllBytes(Paths.get(filePath));
			return Base64.getEncoder().encodeToString(b);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}
}
