package com.java110.community.smo.impl;

import com.java110.community.dao.IRoomRenovationServiceDao;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.RoomDto;
import com.java110.dto.roomRenovation.RoomRenovationDto;
import com.java110.intf.IRoomRenovationInnerServiceSMO;
import com.java110.po.roomRenovation.RoomRenovationPo;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 装修申请内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@Service
@RequestMapping("/roomRenovationApi")
public class RoomRenovationInnerServiceSMOImpl extends BaseServiceSMO implements IRoomRenovationInnerServiceSMO {

    @Autowired
    private IRoomRenovationServiceDao roomRenovationServiceDaoImpl;


    @Override
    @RequestMapping(value = "/saveRoomRenovation", method = RequestMethod.POST)
    public int saveRoomRenovation(@RequestBody RoomRenovationPo roomRenovationPo) {
        int saveFlag = 1;
        roomRenovationServiceDaoImpl.saveRoomRenovationInfo(BeanConvertUtil.beanCovertMap(roomRenovationPo));
        return saveFlag;
    }

    @Override
    @RequestMapping(value = "/updateRoomRenovation", method = RequestMethod.POST)
    public int updateRoomRenovation(@RequestBody RoomRenovationPo roomRenovationPo) {
        int saveFlag = 1;
        roomRenovationServiceDaoImpl.updateRoomRenovationInfo(BeanConvertUtil.beanCovertMap(roomRenovationPo));
        return saveFlag;
    }

    @Override
    @RequestMapping(value = "/updateRoom", method = RequestMethod.POST)
    public int updateRoom(@RequestBody RoomDto roomDto) {
        int saveFlag = 1;
        roomRenovationServiceDaoImpl.updateRoom(BeanConvertUtil.beanCovertMap(roomDto));
        return saveFlag;
    }

    @Override
    @RequestMapping(value = "/deleteRoomRenovation", method = RequestMethod.POST)
    public int deleteRoomRenovation(@RequestBody RoomRenovationPo roomRenovationPo) {
        int saveFlag = 1;
        roomRenovationPo.setStatusCd("1");
        roomRenovationServiceDaoImpl.updateRoomRenovationInfo(BeanConvertUtil.beanCovertMap(roomRenovationPo));
        return saveFlag;
    }

    @Override
    @RequestMapping(value = "/queryRoomRenovations", method = RequestMethod.POST)
    public List<RoomRenovationDto> queryRoomRenovations(@RequestBody RoomRenovationDto roomRenovationDto) {

        //校验是否传了 分页信息

        int page = roomRenovationDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            roomRenovationDto.setPage((page - 1) * roomRenovationDto.getRow());
        }

        List<RoomRenovationDto> roomRenovations = BeanConvertUtil.covertBeanList(roomRenovationServiceDaoImpl.getRoomRenovationInfo(BeanConvertUtil.beanCovertMap(roomRenovationDto)), RoomRenovationDto.class);

        return roomRenovations;
    }


    @Override
    @RequestMapping(value = "/queryRoomRenovationsCount", method = RequestMethod.POST)
    public int queryRoomRenovationsCount(@RequestBody RoomRenovationDto roomRenovationDto) {
        return roomRenovationServiceDaoImpl.queryRoomRenovationsCount(BeanConvertUtil.beanCovertMap(roomRenovationDto));
    }

    public IRoomRenovationServiceDao getRoomRenovationServiceDaoImpl() {
        return roomRenovationServiceDaoImpl;
    }

    public void setRoomRenovationServiceDaoImpl(IRoomRenovationServiceDao roomRenovationServiceDaoImpl) {
        this.roomRenovationServiceDaoImpl = roomRenovationServiceDaoImpl;
    }
}
