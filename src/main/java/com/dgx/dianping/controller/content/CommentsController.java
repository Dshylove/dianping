package com.dgx.dianping.controller.content;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dgx.dianping.dto.CommentDto;
import com.dgx.dianping.pojo.Page;
import com.dgx.dianping.service.CommentService;

@Controller
@RequestMapping("/comments")
public class CommentsController {

	@Autowired
	private CommentService commentService;
	
	@RequestMapping(method=RequestMethod.GET)
	public String init(CommentDto commentDto,Model model) {
		// TODO commentList.jsp有待完善
//		model.addAttribute("list", commentService.getListByBusinessId(null, new Page()));
		model.addAttribute("searchParam", commentDto);
		return "/content/commentList";
	}
	
}
