package com.dgx.dianping.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.dgx.dianping.dto.BusinessDto;
import com.dgx.dianping.dto.BusinessListDto;
import com.dgx.dianping.mapper.BusinessMapper;
import com.dgx.dianping.pojo.Business;
import com.dgx.dianping.pojo.Page;
import com.dgx.dianping.service.BusinessService;
import com.dgx.dianping.util.CommonUtil;
import com.dgx.dianping.util.FileUtil;

@Service
public class BusinessServiceImpl implements BusinessService {

	@Autowired
	private BusinessMapper businessMapper;
	
	@Value("${businessImage.savePath}")
	private String savePath;

	@Value("${businessImage.url}")
	private String url;
	
	@Override
	public boolean add(BusinessDto businessDto) {
		Business business = new Business();
		BeanUtils.copyProperties(businessDto, business);
		if(businessDto.getImgFile() != null && businessDto.getImgFile().getSize() > 0) {
			try {
				String fileName = FileUtil.save(businessDto.getImgFile(), savePath);
				business.setImgFileName(fileName);
				business.setNumber(0); // 默认已售数量为0
				business.setCommentTotalNum(0L); // 默认评论总次数为0
				business.setStarTotalNum(0L); // 默认评论星星总数为0
				businessMapper.insert(business);
				System.err.println("返回主键id:" + business.getId());
				return true; // 保存成功
			} catch (IllegalStateException | IOException e) {
				// TODO 需要添加日志，待续~~
				e.printStackTrace();
				return false;
			}
		} else {
			return false;
		}
	}

	@Override
	public BusinessDto getById(Long id) {
		Business business = businessMapper.selectById(id);
		BusinessDto businessDto = new BusinessDto();
		BeanUtils.copyProperties(business, businessDto);
		businessDto.setImg(url + business.getImgFileName());
		businessDto.setStar(this.getStar(business));
		return businessDto;
	}

	@Override
	public List<BusinessDto> searchByPage(BusinessDto businessDto) {
		List<BusinessDto> result = new ArrayList<>();
		Business businessForSelect = new Business();
		BeanUtils.copyProperties(businessDto, businessForSelect);
		List<Business> list = businessMapper.selectByPage(businessForSelect);
		for(Business business : list) {
			BusinessDto businessDtoTemp = new BusinessDto();
			result.add(businessDtoTemp);
			BeanUtils.copyProperties(business, businessDtoTemp);
			businessDtoTemp.setImg(url + business.getImgFileName());
			businessDtoTemp.setStar(this.getStar(business));
		}
		return result;
	}

	@Override
	public BusinessListDto searchByPageForApi(BusinessDto businessDto) {
		BusinessListDto result = new BusinessListDto();
		// 组织查询条件
		Business businessForSelect = new Business();
		BeanUtils.copyProperties(businessDto, businessForSelect);
		
		// 当关键字不为空时，把关键字的值分别设置到标题、副标题、描述中
		// TODO 改进做法：全文检索
		if(!CommonUtil.isEmpty(businessDto.getKeyword())) {
			businessForSelect.setTitle(businessDto.getKeyword());
			businessForSelect.setSubtitle(businessDto.getKeyword());
			businessForSelect.setDesc(businessDto.getKeyword());
		}
		// 当类别为全部(all)时，需要将类别清空，不作为过滤条件
		if(businessDto.getCategory() != null && "all".equals(businessDto.getCategory())) {
			businessForSelect.setCategory(null);
		}
		// 前端app页码从0开始计算，这里需要+1
		int currentPage = businessForSelect.getPage().getCurrentPage();
		businessForSelect.getPage().setCurrentPage(currentPage + 1);
		
		List<Business> list = businessMapper.selectLikeByPage(businessForSelect);
		
		// 经过查询后根据page对象设置hasMore
		Page page = businessForSelect.getPage();
		result.setHasMore(page.getCurrentPage() < page.getTotalPage());
		
		// 对查询出的结果进行格式化
		for(Business business : list) {
			BusinessDto businessDtoTemp = new BusinessDto();
			result.getData().add(businessDtoTemp);
			BeanUtils.copyProperties(business, businessDtoTemp);
			businessDtoTemp.setImg(url + business.getImgFileName());
			// 为兼容前端mumber这个属性
			businessDtoTemp.setMumber(business.getNumber());
			businessDtoTemp.setStar(this.getStar(business));
		}
		return result;
	}
	
	@Override
	public boolean remove(Long id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean modify(BusinessDto businessDto) {
		// TODO Auto-generated method stub
		return false;
	}
	
	private int getStar(Business business) {
		if(business.getStarTotalNum() != null && business.getCommentTotalNum() != null && business.getCommentTotalNum() != 0) {
			return (int)(business.getStarTotalNum() / business.getCommentTotalNum());
		} else {
			return 0;
		}
	}
}
