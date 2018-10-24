package com.dgx.dianping.controller.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dgx.dianping.constant.ApiCodeEnum;
import com.dgx.dianping.dto.AdDto;
import com.dgx.dianping.dto.ApiCodeDto;
import com.dgx.dianping.dto.BusinessDto;
import com.dgx.dianping.dto.BusinessListDto;
import com.dgx.dianping.dto.CommentForSubmitDto;
import com.dgx.dianping.dto.CommentListDto;
import com.dgx.dianping.dto.OrderForBuyDto;
import com.dgx.dianping.dto.OrdersDto;
import com.dgx.dianping.pojo.Page;
import com.dgx.dianping.service.AdService;
import com.dgx.dianping.service.BusinessService;
import com.dgx.dianping.service.CommentService;
import com.dgx.dianping.service.MemberService;
import com.dgx.dianping.service.OrdersService;
import com.dgx.dianping.util.CommonUtil;

@RestController
@RequestMapping("/api")
public class ApiController {

	@Autowired
	private AdService adService;
	
	@Autowired
	private BusinessService businessService;
	
	@Autowired
	private MemberService memberService;
	
	@Autowired
	private OrdersService ordersService;
	
	@Autowired
	private CommentService commentService;
	
	@Value("${ad.number}") // 读取配置文件
	private int adNumber;
	
	@Value("${businessHome.number}")
	private int businessHomeNumber;

	@Value("${businessSearch.number}")
	private int businessSearchNumber;
	
	/**
	 * 首页 —— 广告（超值特惠）
	 */
	@RequestMapping(value="/homead",method=RequestMethod.GET)
	public List<AdDto> homead(){
		AdDto adDto = new AdDto();
		adDto.getPage().setPageNumber(adNumber);
		return adService.searchByPage(adDto);
	}
	
	/**
	 * 首页 —— 推荐列表（猜你喜欢）
	 */
	@RequestMapping(value="/homelist/{city}/{page.currentPage}",method=RequestMethod.GET)
	public BusinessListDto homelist(BusinessDto businessDto) {
		businessDto.getPage().setPageNumber(businessHomeNumber);
		return businessService.searchByPageForApi(businessDto);
	}
	
	/**
	 * 搜索结果页 - 搜索结果 - 三个参数
	 */
	@RequestMapping(value="/search/{page.currentPage}/{city}/{category}/{keyword}",method=RequestMethod.GET)
	public BusinessListDto searchByKeyword(BusinessDto businessDto) {
		businessDto.getPage().setPageNumber(businessSearchNumber);
		return businessService.searchByPageForApi(businessDto);
	}
	
	/**
	 * 搜索结果页 - 搜索结果 - 两个参数
	 */
	@RequestMapping(value="/search/{page.currentPage}/{city}/{category}",method=RequestMethod.GET)
	public BusinessListDto search(BusinessDto businessDto) {
		businessDto.getPage().setPageNumber(businessSearchNumber);
		return businessService.searchByPageForApi(businessDto);
	}
	
	/**
	 * 详情页 - 商户信息
	 */
	@RequestMapping(value="/detail/info/{id}",method=RequestMethod.GET)
	public BusinessDto detail(@PathVariable("id")Long id) {
		return businessService.getById(id);
	}
	
	/**
	 * 详情页 - 用户评论
	 */
	@RequestMapping(value="/detail/comment/{currentPage}/{businessId}",method=RequestMethod.GET)
	public CommentListDto detail(@PathVariable("businessId")Long businessId,Page page) {
		return commentService.getListByBusinessId(businessId, page);
	}

	/**
	 * 订单列表
	 */
	@RequestMapping(value="/orderlist/{username}",method=RequestMethod.GET)
	public List<OrdersDto> orderlist(@PathVariable("username")Long username) {
		// TODO 验证用户，校验token，只允许查询本用户的订单列表~
		// 根据手机号取出会员id
		Long memberId = memberService.getIdByPhone(username);
		if(memberId != null) {	// 会员主键id不为空才查询
			return ordersService.getListByMemberId(memberId);
		}
		return null;
	}
	
	
	
	/**
	 * 根据手机号下发短信验证码
	 */
	@RequestMapping(value="/sms",method=RequestMethod.POST)
	public ApiCodeDto sms(@RequestParam("username")Long username) {
		ApiCodeDto dto;
		// 1、验证用户手机号是否存在（是否注册过）
		if(memberService.exists(username)) {
			// 2、生成6位随机数
			String code = String.valueOf(CommonUtil.random(6));
			// 3、保存手机号与对应的md5(6位随机数)（一般保存1分钟，1分钟后失效）
			if(memberService.saveCode(username, code)) {
				// 4、调用短信通道，将明文6位随机数发送到对应的手机上。
				if(memberService.sendCode(username, code)) {
					dto = new ApiCodeDto(ApiCodeEnum.SUCCESS.getErrno(), ApiCodeEnum.SUCCESS.getMsg(), code);
				} else {
					dto = new ApiCodeDto(ApiCodeEnum.SEND_FAIL);
				}
			} else {
				dto = new ApiCodeDto(ApiCodeEnum.REPEAT_REQUEST);
			}
		} else {
			dto = new ApiCodeDto(ApiCodeEnum.USER_NOT_EXISTS);
		}
		return dto;
	}
	
	/**
	 * 会员登录
	 */
	@RequestMapping(value="/login",method=RequestMethod.POST)
	public ApiCodeDto login(@RequestParam("username")Long username,@RequestParam("code")String code) {
		ApiCodeDto dto;
		// 1、用手机号取出保存的md5(6位随机数)，能取到，并且与提交上来的code值相同为校验通过
		String saveCode = memberService.getCode(username);
		if(saveCode != null) {
			if(saveCode.equals(code)) {
				// 2、如果校验通过，生成一个32位的token
				String token = CommonUtil.getUUID();
				// 3、保存手机号与对应的token（一般这个手机号中途没有与服务端交互的情况下，保持10分钟）
				memberService.saveToken(token, username);
				// 4、将这个token返回给前端
				dto = new ApiCodeDto(ApiCodeEnum.SUCCESS);
				dto.setToken(token);
			} else {
				dto = new ApiCodeDto(ApiCodeEnum.CODE_ERROR);
			}
		} else {
			dto = new ApiCodeDto(ApiCodeEnum.CODE_INVALID);
		}
		return dto;
	}
	
	/**
	 * 买单
	 */
	@RequestMapping(value="/order",method=RequestMethod.POST)
	public ApiCodeDto order(OrderForBuyDto orderForBuyDto) {
		ApiCodeDto dto;
		// 1、校验token是否有效（缓存中是否存在这样一个token，并且对应存放的会员信息（这里指的是手机号）与提交上来的信息一致）
		Long phone = memberService.getPhone(orderForBuyDto.getToken());
		if(phone != null && phone.equals(orderForBuyDto.getUsername())) {
			// 2、根据手机号获取会员主键
			Long memberId = memberService.getIdByPhone(phone);
			// 3、保存订单
			OrdersDto ordersDto = new OrdersDto();
			ordersDto.setNum(orderForBuyDto.getNum());
			ordersDto.setPrice(orderForBuyDto.getPrice());
			ordersDto.setBusinessId(orderForBuyDto.getId());
			ordersDto.setMemberId(memberId);
			ordersService.add(ordersDto);
			dto = new ApiCodeDto(ApiCodeEnum.SUCCESS);
			// 4、TODO 还有一件重要的事未做
		} else {
			dto = new ApiCodeDto(ApiCodeEnum.NOT_LOGGED);
		}
		return dto;
	}
	
	/**
	 * 提交评论
	 */
	@RequestMapping(value="/submitComment",method=RequestMethod.POST)
	public ApiCodeDto submitComment(CommentForSubmitDto commentForSubmitDto) {
		ApiCodeDto dto;
		// 1、校验登录信息：token、手机号
		Long phone = memberService.getPhone(commentForSubmitDto.getToken());
		if(phone != null && phone.equals(commentForSubmitDto.getUsername())) {
			// 2、根据手机号获取会员id
			Long memberId = memberService.getIdByPhone(phone);
			// 3、(判断订单是否属于该登录会员)根据提交上来的订单id获取对应的会员id，校验与当前登录的会员是否一致
			OrdersDto ordersDto = ordersService.getById(commentForSubmitDto.getId());
			if(memberId.equals(ordersDto.getMemberId())) {
				// 4、保存评论
				if(commentService.add(commentForSubmitDto)) {
					dto = new ApiCodeDto(ApiCodeEnum.SUCCESS);
					// TODO 5、定时任务
				} else {
					dto = null;
					System.err.println("保存失败！");
				}
			} else {
				dto = new ApiCodeDto(ApiCodeEnum.NO_AUTH);
			}
		} else {
			dto = new ApiCodeDto(ApiCodeEnum.NOT_LOGGED);
		}
		return dto;
	}
}
