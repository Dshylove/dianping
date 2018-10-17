package com.dgx.dianping.dto;

import org.springframework.web.multipart.MultipartFile;

import com.dgx.dianping.pojo.Ad;

/**
 * Dto数据传输对象
 * @author dgx
 */
public class AdDto extends Ad{

	private String img;
	private MultipartFile imgFile; //图片上传属性
	
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public MultipartFile getImgFile() {
		return imgFile;
	}
	public void setImgFile(MultipartFile imgFile) {
		this.imgFile = imgFile;
	}
	
}
