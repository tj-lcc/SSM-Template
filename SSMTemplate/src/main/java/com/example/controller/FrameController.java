package com.example.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.utils.ValidateCode;

@Controller
public class FrameController {

	@Scheduled(cron = "0/5 * *  * * ? ")
	public void printMsg() {
		System.out.println("scheduled annotation test");
	}

	@RequestMapping(value = "/index")
	public String doIndex() {
		return "index";
	}

	@RequestMapping(value = "/getCode")
	public void getCode(HttpServletRequest req, HttpServletResponse res) {
		ValidateCode validateCode = ValidateCode.generateValidateCode();
		String codeStr = validateCode.getCodeStr();
		BufferedImage codeImg = validateCode.getCodeImg();
		
		// ����λ���ֵ���֤�뱣�浽Session�С�
		HttpSession session = req.getSession();
		session.setAttribute("code", codeStr);

		// ��ֹͼ�񻺴档
		res.setHeader("Pragma", "no-cache");
		res.setHeader("Cache-Control", "no-cache");
		res.setDateHeader("Expires", 0);

		res.setContentType("image/jpeg");

		// ��ͼ�������Servlet������С�
		ServletOutputStream sos;
		try {
			sos = res.getOutputStream();
			ImageIO.write(codeImg, "jpeg", sos);
			sos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
