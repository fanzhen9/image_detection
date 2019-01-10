package com.sailing.image.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public interface SnapshotDao {

    @Select("select device_id,PANORAMA_PHOTOS,PERSON_PHOTOS from T_VIAS_SNA_PERSON where device_id = #{apeId} and " +
            "PERSON_APPEAR_TIME >=to_date(#{startTime},'yyyy-mm-dd hh24:mi:ss' ) " +
            "and PERSON_APPEAR_TIME <=to_date(#{endTime},'yyyy-mm-dd hh24:mi:ss')  and rownum<=10")
    List<Map> queryPerson(@Param("startTime")String startTime, @Param("endTime")String endTime, @Param("apeId")String apeId);
}
