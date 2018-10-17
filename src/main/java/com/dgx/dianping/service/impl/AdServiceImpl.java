package com.dgx.dianping.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.dgx.dianping.dto.AdDto;
import com.dgx.dianping.mapper.AdMapper;
import com.dgx.dianping.pojo.Ad;
import com.dgx.dianping.service.AdService;
import com.dgx.dianping.util.FileUtil;

@Service
public class AdServiceImpl implements AdService {

	@Autowired
	private AdMapper adMapper;
	
	@Value("${adImage.savePath}")
	private String adImageSavePath; // 获取配置文件属性
	
	@Value("${adImage.url}")
	private String adImageUrl;
	
	@Override
	public boolean add(AdDto adDto) {
		Ad ad = new Ad();
		ad.setTitle(adDto.getTitle());
		ad.setLink(adDto.getLink());
		ad.setWeight(adDto.getWeight());
		
		if(adDto.getImgFile() != null && adDto.getImgFile().getSize() > 0) {
			String fileName = System.currentTimeMillis() + "_" + adDto.getImgFile().getOriginalFilename();
			File file = new File(adImageSavePath + fileName);
			// 判断是否存在文件夹，否则创建文件夹
			File fileFolder = new File(adImageSavePath);
			if(!fileFolder.exists()) {
				fileFolder.mkdirs();
			}
			
			try {
				adDto.getImgFile().transferTo(file);
				ad.setImgFileName(fileName);
				adMapper.insert(ad);
				System.err.println("返回主键id:" + ad.getId());
				return true; // 保存成功
			} catch (IllegalStateException | IOException e) {
				// TODO 需要添加日志，待续~~
				return false;
			}
		} else {
			return false;
		}
	}

	@Override
	public List<AdDto> searchByPage(AdDto adDto) {
		List<AdDto> result = new ArrayList<>();
		Ad condition = new Ad();
		BeanUtils.copyProperties(adDto, condition); // 使用spring提供的bean拷贝功能
		List<Ad> adList = adMapper.selectByPage(condition);
		for(Ad ad : adList) {
			AdDto adDtoTemp = new AdDto();
			result.add(adDtoTemp);
			adDtoTemp.setImg(adImageUrl + ad.getImgFileName()); // 设置图片URL地址
			ad.setImgFileName(null); // 将不必要传输的参数属性置空
			BeanUtils.copyProperties(ad, adDtoTemp);
		}
		return result;
	}

	@Override
	public AdDto getById(Long id) {
		Ad ad = adMapper.selectById(id);
		AdDto result = new AdDto();
		BeanUtils.copyProperties(ad, result);
		result.setImg(adImageUrl + ad.getImgFileName());
		return result;
	}
	
	@Override
	public boolean remove(Long id) {
		Ad ad = adMapper.selectById(id); // 先查询后删除
		if(ad != null) { // 查询不为空才执行删除
			int deleteRows = adMapper.delete(id);
			if(deleteRows == 1) { // 数据删除成功时
				FileUtil.delete(adImageSavePath + ad.getImgFileName()); // 删除图片文件
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean modify(AdDto adDto) {
		Ad ad = new Ad();
		BeanUtils.copyProperties(adDto, ad);
		String fileName = null;
		// 保存新图片~~
		if (adDto.getImgFile() != null && adDto.getImgFile().getSize() > 0) {
			try {
				fileName = FileUtil.save(adDto.getImgFile(), adImageSavePath);
				ad.setImgFileName(fileName);
			} catch (IllegalStateException | IOException e) {
				// TODO 需要添加日志
				return false;
			}
		}
		
		int updateCount = adMapper.update(ad);
		if(updateCount != 1) {
			return false;
		}
		if(fileName != null) {
			// 删除旧图片~~
			return FileUtil.delete(adImageSavePath + adDto.getImgFileName());
		}
		return true;
	}

}
