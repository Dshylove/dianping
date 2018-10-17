package com.dgx.dianping.pojo;

public class BaseBean {

	private Page page;

	// 使用构造方法实例化Page对象，进而执行Page对象的构造方法进行初始化Page属性值
	public BaseBean() {
		this.page = new Page();
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}
	
}
