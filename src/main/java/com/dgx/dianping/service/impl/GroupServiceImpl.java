package com.dgx.dianping.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dgx.dianping.dto.ActionDto;
import com.dgx.dianping.dto.GroupDto;
import com.dgx.dianping.dto.MenuDto;
import com.dgx.dianping.mapper.GroupActionMapper;
import com.dgx.dianping.mapper.GroupMapper;
import com.dgx.dianping.mapper.GroupMenuMapper;
import com.dgx.dianping.pojo.Action;
import com.dgx.dianping.pojo.Group;
import com.dgx.dianping.pojo.GroupAction;
import com.dgx.dianping.pojo.GroupMenu;
import com.dgx.dianping.pojo.Menu;
import com.dgx.dianping.service.GroupService;

@Service
public class GroupServiceImpl implements GroupService {

	@Autowired
	private GroupMapper groupMapper;
	@Autowired
	private GroupMenuMapper groupMenuMapper;
	@Autowired
	private GroupActionMapper groupActionMapper;
	
	@Override
	public List<GroupDto> getList() {
		List<GroupDto> result = new ArrayList<>();
		List<Group> groupList = groupMapper.select(new Group());
		for (Group group : groupList) {
			GroupDto groupDto = new GroupDto();
			result.add(groupDto);
			BeanUtils.copyProperties(group, groupDto);
			groupDto.setpId(0);
		}
		return result;
	}

	@Override
	public boolean add(GroupDto groupDto) {
		Group group = new Group();
		BeanUtils.copyProperties(groupDto, group);
		return groupMapper.insert(group) == 1;
	}

	@Override
	public GroupDto getById(Long id) {
		GroupDto groupDto = new GroupDto();
		Group group = groupMapper.selectById(id);
		BeanUtils.copyProperties(group, groupDto);
		return groupDto;
	}

	@Override
	public boolean modify(GroupDto groupDto) {
		Group group = new Group();
		BeanUtils.copyProperties(groupDto, group);
		return groupMapper.update(group) == 1;
	}

	@Override
	public boolean remove(Long id) {
		return groupMapper.delete(id) == 1;
	}

	@Override
	public GroupDto getByIdWithMenuAction(Long id) {
		GroupDto result = new GroupDto();
		List<MenuDto> menuDtoList = new ArrayList<>();
		List<ActionDto> actionDtoList = new ArrayList<>();
		result.setMenuDtoList(menuDtoList);
		result.setActionDtoList(actionDtoList);
		
		Group group = groupMapper.selectMenuListById(id);
		if(group != null) {
			BeanUtils.copyProperties(group, result);
			for(Menu menu : group.getMenuList()) {
				MenuDto menuDto = new MenuDto();
				menuDtoList.add(menuDto);
				BeanUtils.copyProperties(menu, menuDto);
			}
			
			for(Action action : group.getActionList()) {
				ActionDto actionDto = new ActionDto();
				actionDtoList.add(actionDto);
				BeanUtils.copyProperties(action, actionDto);
			}
		}
		return result;
	}

	@Override
	@Transactional
	public boolean assignMenu(GroupDto groupDto) {
		groupMenuMapper.deleteByGroupId(groupDto.getId());
		groupActionMapper.deleteByGroupId(groupDto.getId());
		// 保存为用户组分配的菜单
		if(groupDto.getMenuIdList() != null && groupDto.getMenuIdList().size() > 0) {
			List<GroupMenu> list = new ArrayList<>();
			for(Long menuId : groupDto.getMenuIdList()) {
				if(menuId != null) {
					GroupMenu groupMenu = new GroupMenu();
					groupMenu.setGroupId(groupDto.getId());
					groupMenu.setMenuId(menuId);
					list.add(groupMenu);
				}
			}
			groupMenuMapper.insertBatch(list);
		}
		// 保存为用户组分配的动作
		if(groupDto.getActionIdList() != null && groupDto.getActionIdList().size() > 0) {
			List<GroupAction> list = new ArrayList<>();
			for(Long actionId : groupDto.getActionIdList()) {
				if(actionId != null) {
					GroupAction groupAction = new GroupAction();
					groupAction.setGroupId(groupDto.getId());
					groupAction.setActionId(actionId);
					list.add(groupAction);
				}
			}
			groupActionMapper.insertBatch(list);
		}
		return true;
	}

}